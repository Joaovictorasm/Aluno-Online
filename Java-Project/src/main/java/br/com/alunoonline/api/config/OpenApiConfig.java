package br.com.alunoonline.api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configura o Swagger UI (http://localhost:8080/swagger-ui.html).
 * O botão "Authorize" pede usuário e senha (HTTP Basic) para testar as rotas protegidas.
 */
@Configuration
public class OpenApiConfig {

    private static final String BASIC_AUTH = "basicAuth";

    @Bean
    public OpenAPI alunoOnlineOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Aluno Online API")
                        .description("API de alunos, professores, disciplinas e matrículas")
                        .version("v1"))
                .components(new Components()
                        .addSecuritySchemes(BASIC_AUTH, new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("basic")))
                .addSecurityItem(new SecurityRequirement().addList(BASIC_AUTH));
    }
}
