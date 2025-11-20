package gastu.gastu.application.service.shared;

import gastu.gastu.application.port.output.EgresoRepositoryPort;
import gastu.gastu.application.port.output.IngresoRepositoryPort;
import gastu.gastu.domain.model.expense.Egreso;
import gastu.gastu.domain.model.income.Ingreso;
import gastu.gastu.infrastructure.adapter.input.rest.dto.response.shared.AlertaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

/**
 * Servicio para calcular balances y generar alertas financieras
 */
@Service
@RequiredArgsConstructor
public class BalanceService {

    private final IngresoRepositoryPort ingresoRepository;
    private final EgresoRepositoryPort egresoRepository;

    /**
     * Calcula el balance de un mes específico
     */
    public BigDecimal calcularBalanceMes(Long usuarioId, LocalDate fecha) {
        LocalDate inicioMes = fecha.withDayOfMonth(1);
        LocalDate finMes = fecha.withDayOfMonth(fecha.lengthOfMonth());

        // Sumar ingresos activos del mes
        List<Ingreso> ingresos = ingresoRepository.findByUsuarioIdAndFechaTransaccionBetween(
                usuarioId, inicioMes, finMes);

        BigDecimal totalIngresos = ingresos.stream()
                .filter(Ingreso::isActivo)
                .map(Ingreso::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Sumar egresos activos del mes
        List<Egreso> egresos = egresoRepository.findByUsuarioIdAndFechaTransaccionBetween(
                usuarioId, inicioMes, finMes);

        BigDecimal totalEgresos = egresos.stream()
                .filter(Egreso::isActivo)
                .map(Egreso::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalIngresos.subtract(totalEgresos);
    }

    /**
     * Verifica si el balance es negativo y genera una alerta
     */
    public AlertaResponse verificarBalanceNegativo(Long usuarioId, LocalDate fecha, BigDecimal balance) {
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            // Formatear balance en COP
            NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("es", "CO"));
            String balanceFormateado = currencyFormatter.format(balance.abs());

            String nombreMes = obtenerNombreMes(fecha.getMonthValue());

            return AlertaResponse.builder()
                    .tipo("SALDO_NEGATIVO")
                    .titulo("⚠️ Gastos superiores a ingresos")
                    .mensaje(String.format(
                            "Tus gastos de %s superan tus ingresos en %s",
                            nombreMes,
                            balanceFormateado
                    ))
                    .balanceMes(balance)
                    .severidad("WARNING")
                    .build();
        }
        return null;
    }

    /**
     * Obtiene el nombre del mes en español
     */
    private String obtenerNombreMes(int mes) {
        String[] meses = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };
        return meses[mes - 1];
    }
}