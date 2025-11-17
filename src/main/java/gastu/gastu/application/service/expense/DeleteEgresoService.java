package gastu.gastu.application.service.expense;

import gastu.gastu.application.port.input.expense.DeleteEgresoUseCase;
import gastu.gastu.application.port.output.EgresoRepositoryPort;
import gastu.gastu.domain.exception.BusinessException;
import gastu.gastu.domain.model.expense.Egreso;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Servicio para eliminar (desactivar o eliminar físicamente) egresos
 */
@Service
@RequiredArgsConstructor
public class DeleteEgresoService implements DeleteEgresoUseCase {

    private final EgresoRepositoryPort egresoRepository;

    /**
     * Desactiva un egreso (soft delete)
     */
    @Override
    @Transactional
    public void softDelete(Long egresoId, Long usuarioId) {
        // 1. Buscar el egreso
        Egreso egreso = egresoRepository.findById(egresoId)
                .orElseThrow(() -> new BusinessException("Egreso no encontrado con ID: " + egresoId));

        // 2. Verificar que el egreso pertenece al usuario
        if (!egreso.getUsuarioId().equals(usuarioId)) {
            throw new BusinessException("No tienes permiso para eliminar este egreso");
        }

        // 3. Desactivar
        egreso.desactivar();
        egresoRepository.save(egreso);
    }

    /**
     * Elimina físicamente un egreso
     * Solo si ya está inactivo
     */
    @Transactional
    public void hardDelete(Long egresoId, Long usuarioId) {
        // 1. Buscar el egreso
        Egreso egreso = egresoRepository.findById(egresoId)
                .orElseThrow(() -> new BusinessException("Egreso no encontrado con ID: " + egresoId));

        // 2. Verificar que el egreso pertenece al usuario
        if (!egreso.getUsuarioId().equals(usuarioId)) {
            throw new BusinessException("No tienes permiso para eliminar este egreso");
        }

        // 3. Verificar que esté inactivo
        if (egreso.isActivo()) {
            throw new BusinessException("Solo se pueden eliminar permanentemente egresos inactivos. Primero desactívalo.");
        }

        // 4. Eliminar físicamente
        egresoRepository.deleteById(egresoId);
    }
}