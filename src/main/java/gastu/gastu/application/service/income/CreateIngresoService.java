package gastu.gastu.application.service.income;

import gastu.gastu.application.port.input.income.CreateIngresoUseCase;
import gastu.gastu.application.port.output.IngresoRepositoryPort;
import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.domain.model.income.ConceptoIngreso;
import gastu.gastu.domain.model.income.Ingreso;
import gastu.gastu.domain.model.user.Usuario;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.income.CreateIngresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.income.IngresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoIngresoRepository;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para crear Ingresos
 */
@Service
@RequiredArgsConstructor
public class CreateIngresoService implements CreateIngresoUseCase {

    private final IngresoRepositoryPort ingresoRepository;
    private final JpaConceptoIngresoRepository conceptoRepository;
    private final JpaUsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public IngresoResponse execute(CreateIngresoRequest request, Long usuarioId) {
        // Validar que el usuario existe
        var usuarioEntity = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        // Validar que el concepto existe y está activo
        var conceptoEntity = conceptoRepository.findById(request.getConceptoIngresoId())
                .orElseThrow(() -> new BusinessException("Concepto de ingreso no encontrado"));

        if (!conceptoEntity.isActivo()) {
            throw new BusinessException("El concepto de ingreso está inactivo");
        }

        // Validar que el concepto es válido para ingresos variables (VARIABLE o AMBOS)
        if (!conceptoEntity.getTipoTransaccion().esValidoParaVariable()) {
            throw new BusinessException("El concepto seleccionado no es válido para ingresos variables. Use una proyección para conceptos recurrentes.");
        }

        // Crear el ingreso
        Ingreso ingreso = Ingreso.builder()
                .monto(request.getMonto())
                .descripcion(request.getDescripcion())
                .fechaTransaccion(request.getFechaTransaccion())
                .activo(true)
                .conceptoIngreso(ConceptoIngreso.builder()
                        .conceptoIngresoId(conceptoEntity.getConceptoIngresoId())
                        .nombre(conceptoEntity.getNombre())
                        .descripcion(conceptoEntity.getDescripcion())
                        .tipoTransaccion(conceptoEntity.getTipoTransaccion())
                        .build())
                .usuario(Usuario.builder()
                        .usuarioId(usuarioEntity.getUsuarioId())
                        .nombre(usuarioEntity.getNombre())
                        .correo(usuarioEntity.getCorreo())
                        .build())
                .build();

        // Validar el dominio
        if (!ingreso.esMontoValido()) {
            throw new BusinessException("El monto debe ser mayor a cero");
        }

        if (!ingreso.esFechaValida()) {
            throw new BusinessException("La fecha de transacción es obligatoria");
        }

        // Guardar
        Ingreso ingresoGuardado = ingresoRepository.save(ingreso);

        // Retornar response
        return mapToResponse(ingresoGuardado);
    }

    private IngresoResponse mapToResponse(Ingreso ingreso) {
        return IngresoResponse.builder()
                .ingresoId(ingreso.getIngresoId())
                .monto(ingreso.getMonto())
                .descripcion(ingreso.getDescripcion())
                .fechaTransaccion(ingreso.getFechaTransaccion())
                .fechaRegistro(ingreso.getFechaRegistro())
                .concepto(IngresoResponse.ConceptoIngresoSimple.builder()
                        .conceptoIngresoId(ingreso.getConceptoIngreso().getConceptoIngresoId())
                        .nombre(ingreso.getConceptoIngreso().getNombre())
                        .descripcion(ingreso.getConceptoIngreso().getDescripcion())
                        .build())
                .build();
    }
}