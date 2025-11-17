package gastu.gastu.application.service.expense;

import gastu.gastu.application.port.input.expense.CreateEgresoUseCase;
import gastu.gastu.application.port.output.EgresoRepositoryPort;
import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.domain.model.expense.ConceptoEgreso;
import gastu.gastu.domain.model.expense.Egreso;
import gastu.gastu.domain.model.user.Usuario;
import gastu.gastu.infrastructure.adapter.input.rest.dto.request.expense.CreateEgresoRequest;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.expense.EgresoResponse;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaConceptoEgresoRepository;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para crear Egresos
 */
@Service
@RequiredArgsConstructor
public class CreateEgresoService implements CreateEgresoUseCase {

    private final EgresoRepositoryPort egresoRepository;
    private final JpaConceptoEgresoRepository conceptoRepository;
    private final JpaUsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public EgresoResponse execute(CreateEgresoRequest request, Long usuarioId) {
        // Validar que el usuario existe
        var usuarioEntity = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new BusinessException("Usuario no encontrado"));

        // Validar que el concepto existe y está activo
        var conceptoEntity = conceptoRepository.findById(request.getConceptoEgresoId())
                .orElseThrow(() -> new BusinessException("Concepto de egreso no encontrado"));

        if (!conceptoEntity.isActivo()) {
            throw new BusinessException("El concepto de egreso está inactivo");
        }

        // Validar que el concepto es válido para egresos variables (VARIABLE o AMBOS)
        if (!conceptoEntity.getTipoTransaccion().esValidoParaVariable()) {
            throw new BusinessException("El concepto seleccionado no es válido para egresos variables. Use una proyección para conceptos recurrentes.");
        }

        // Crear el egreso
        Egreso egreso = Egreso.builder()
                .monto(request.getMonto())
                .descripcion(request.getDescripcion())
                .fechaTransaccion(request.getFechaTransaccion())
                .activo(true)
                .conceptoEgreso(ConceptoEgreso.builder()
                        .conceptoEgresoId(conceptoEntity.getConceptoEgresoId())
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
        if (!egreso.esMontoValido()) {
            throw new BusinessException("El monto debe ser mayor a cero");
        }

        if (!egreso.esFechaValida()) {
            throw new BusinessException("La fecha de transacción es obligatoria");
        }

        // Guardar
        Egreso egresoGuardado = egresoRepository.save(egreso);

        // Retornar response
        return mapToResponse(egresoGuardado);
    }

    private EgresoResponse mapToResponse(Egreso egreso) {
        return EgresoResponse.builder()
                .egresoId(egreso.getEgresoId())
                .monto(egreso.getMonto())
                .descripcion(egreso.getDescripcion())
                .fechaTransaccion(egreso.getFechaTransaccion())
                .fechaRegistro(egreso.getFechaRegistro())
                .concepto(EgresoResponse.ConceptoEgresoSimple.builder()
                        .conceptoEgresoId(egreso.getConceptoEgreso().getConceptoEgresoId())
                        .nombre(egreso.getConceptoEgreso().getNombre())
                        .descripcion(egreso.getConceptoEgreso().getDescripcion())
                        .build())
                .build();
    }
}