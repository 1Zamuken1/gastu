package gastu.gastu.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de OpenAPI/Swagger para documentación de la API
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI gastuOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("GASTU - API de Gestión Financiera")
                        .description("API REST para la gestión de salud financiera personal: ingresos, egresos y ahorros")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipo GASTU")
                                .email("contacto@gastu.com"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("Ingrese el token JWT en el formato: Bearer {token}")));
    }
}