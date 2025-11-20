package gastu.gastu.application.service.notification;

import gastu.gastu.domain.model.notification.Notificacion;
import gastu.gastu.infrastructure.adapter.output.persistence.entity.NotificacionEntity;
import gastu.gastu.infrastructure.adapter.output.persistence.repository.JpaNotificacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para crear y gestionar notificaciones
 */
@Service
@RequiredArgsConstructor
public class NotificacionService {

    private final JpaNotificacionRepository notificacionRepository;

    /**
     * Crea una notificación de saldo negativo
     */
    @Transactional
    public void crearNotificacionSaldoNegativo(
            Long usuarioId,
            BigDecimal balance,
            String mesReferencia,
            BigDecimal totalIngresos,
            BigDecimal totalEgresos) {

        Map<String, Object> datos = new HashMap<>();
        datos.put("balance", balance.toString());
        datos.put("mesReferencia", mesReferencia);
        datos.put("totalIngresos", totalIngresos.toString());
        datos.put("totalEgresos", totalEgresos.toString());
        datos.put("severidad", "WARNING");

        NotificacionEntity notificacion = NotificacionEntity.builder()
                .usuarioId(usuarioId)
                .tipo("ALERTA_SALDO")
                .categoria("FINANCIERA")
                .titulo("⚠️ Gastos superiores a ingresos")
                .mensaje(String.format(
                        "Tus gastos de %s superan tus ingresos. Balance negativo: $%s",
                        mesReferencia,
                        balance.abs()
                ))
                .datos(datos)
                .leida(false)
                .build();

        notificacionRepository.save(notificacion);
    }

    /**
     * Crea una notificación genérica
     */
    @Transactional
    public void crearNotificacion(
            Long usuarioId,
            Notificacion.TipoNotificacion tipo,
            Notificacion.CategoriaNotificacion categoria,
            String titulo,
            String mensaje,
            Map<String, Object> datos) {

        NotificacionEntity notificacion = NotificacionEntity.builder()
                .usuarioId(usuarioId)
                .tipo(tipo.name())
                .categoria(categoria.name())
                .titulo(titulo)
                .mensaje(mensaje)
                .datos(datos)
                .leida(false)
                .build();

        notificacionRepository.save(notificacion);
    }
}