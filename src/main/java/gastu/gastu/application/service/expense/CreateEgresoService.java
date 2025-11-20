package gastu.gastu.application.service.expense;

import gastu.gastu.application.port.input.expense.CreateEgresoUseCase;
import gastu.gastu.application.port.output.EgresoRepositoryPort;
import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.domain.model.expense.Egreso;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.expense.CreateEgresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.EgresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.ConceptoEgresoEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoEgresoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Servicio para crear egresos
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateEgresoService implements CreateEgresoUseCase {

    private final EgresoRepositoryPort egresoRepository;
    private final JpaConceptoEgresoRepository conceptoRepository;
    private final gastu.gastu.application.service.shared.BalanceService balanceService;
    private final gastu.gastu.application.service.notification.NotificacionService notificacionService;

    @Override
    @Transactional
    public EgresoResponse execute(CreateEgresoRequest request, Long usuarioId) {
        log.info("Creando egreso para usuario: {}, monto: {}, fecha: {}",
                usuarioId, request.getMonto(), request.getFechaTransaccion());

        // 1. Validar que el concepto existe y está activo
        ConceptoEgresoEntity concepto = conceptoRepository.findById(request.getConceptoEgresoId())
                .orElseThrow(() -> new BusinessException(
                        "Concepto de egreso no encontrado con ID: " + request.getConceptoEgresoId()));

        if (!concepto.isActivo()) {
            throw new BusinessException("El concepto de egreso está inactivo");
        }

        // 2. Validar que el concepto sea válido para transacciones variables
        if (!concepto.getTipoTransaccion().esValidoParaVariable()) {
            throw new BusinessException(
                    "El concepto '" + concepto.getNombre() +
                            "' solo puede usarse para egresos recurrentes (proyecciones)");
        }

        // 3. Crear el egreso
        Egreso egreso = Egreso.builder()
                .monto(request.getMonto())
                .descripcion(request.getDescripcion())
                .fechaTransaccion(request.getFechaTransaccion())
                .conceptoEgresoId(request.getConceptoEgresoId())
                .usuarioId(usuarioId)
                .activo(true)
                .build();

        // 4. Validar el egreso
        if (!egreso.isValido()) {
            throw new BusinessException("Los datos del egreso son inválidos");
        }

        // 5. Guardar
        Egreso egresoGuardado = egresoRepository.save(egreso);
        log.info("Egreso guardado con ID: {}", egresoGuardado.getEgresoId());

        // 6. CALCULAR BALANCE Y VERIFICAR ALERTA
        BigDecimal balance = balanceService.calcularBalanceMes(
                usuarioId,
                request.getFechaTransaccion()
        );

        log.info("Balance calculado del mes: {}", balance);

        gastu.gastu.infrastructure.adapter.input.rest.dto.response.shared.AlertaResponse alerta =
                balanceService.verificarBalanceNegativo(
                        usuarioId,
                        request.getFechaTransaccion(),
                        balance
                );

        if (alerta != null) {
            log.warn("ALERTA GENERADA: {}", alerta.getTitulo());

            String mesReferencia = request.getFechaTransaccion().getMonth().toString();

            // Calcular totales para la notificación
            BigDecimal totalEgresos = balance.abs().add(request.getMonto());
            BigDecimal totalIngresos = request.getMonto().subtract(balance.abs());

            log.info("Total Ingresos: {}, Total Egresos: {}", totalIngresos, totalEgresos);

            notificacionService.crearNotificacionSaldoNegativo(
                    usuarioId,
                    balance,
                    mesReferencia,
                    totalIngresos,
                    totalEgresos
            );
            log.info("Notificación creada en BD");
        } else {
            log.info("No se generó alerta. Balance positivo.");
        }

        // 7. Retornar respuesta con alerta
        EgresoResponse response = mapToResponse(egresoGuardado, concepto);
        response.setAlerta(alerta);

        return response;
    }

    private EgresoResponse mapToResponse(Egreso egreso, ConceptoEgresoEntity concepto) {
        return EgresoResponse.builder()
                .egresoId(egreso.getEgresoId())
                .monto(egreso.getMonto())
                .descripcion(egreso.getDescripcion())
                .fechaTransaccion(egreso.getFechaTransaccion())
                .fechaRegistro(egreso.getFechaRegistro())
                .activo(egreso.isActivo())
                .concepto(EgresoResponse.ConceptoEgresoSimpleResponse.builder()
                        .conceptoEgresoId(concepto.getConceptoEgresoId())
                        .nombre(concepto.getNombre())
                        .build())
                .build();
    }
}