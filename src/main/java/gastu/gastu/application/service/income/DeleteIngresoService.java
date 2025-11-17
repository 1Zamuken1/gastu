package gastu.gastu.application.service.income;

import gastu.gastu.application.port.input.income.DeleteIngresoUseCase;
import gastu.gastu.application.port.output.IngresoRepositoryPort;
import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.domain.model.income.Ingreso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para eliminar (desactivar o eliminar físicamente) ingresos
 */
@Service
@RequiredArgsConstructor
public class DeleteIngresoService implements DeleteIngresoUseCase {

    private final IngresoRepositoryPort ingresoRepository;

    /**
     * Desactiva un ingreso (soft delete)
     */
    @Override
    @Transactional
    public void softDelete(Long ingresoId, Long usuarioId) {
        // 1. Buscar el ingreso
        Ingreso ingreso = ingresoRepository.findById(ingresoId)
                .orElseThrow(() -> new BusinessException("Ingreso no encontrado con ID: " + ingresoId));

        // 2. Verificar que el ingreso pertenece al usuario
        if (!ingreso.getUsuarioId().equals(usuarioId)) {
            throw new BusinessException("No tienes permiso para eliminar este ingreso");
        }

        // 3. Desactivar
        ingreso.desactivar();
        ingresoRepository.save(ingreso);
    }

    /**
     * Elimina físicamente un ingreso
     * Solo si ya está inactivo
     */
    @Transactional
    public void hardDelete(Long ingresoId, Long usuarioId) {
        // 1. Buscar el ingreso
        Ingreso ingreso = ingresoRepository.findById(ingresoId)
                .orElseThrow(() -> new BusinessException("Ingreso no encontrado con ID: " + ingresoId));

        // 2. Verificar que el ingreso pertenece al usuario
        if (!ingreso.getUsuarioId().equals(usuarioId)) {
            throw new BusinessException("No tienes permiso para eliminar este ingreso");
        }

        // 3. Verificar que esté inactivo
        if (ingreso.isActivo()) {
            throw new BusinessException("Solo se pueden eliminar permanentemente ingresos inactivos. Primero desactívalo.");
        }

        // 4. Eliminar físicamente
        ingresoRepository.deleteById(ingresoId);
    }
}