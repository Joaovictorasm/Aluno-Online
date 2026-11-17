package br.com.alunoonline.api.infra.springdoc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Documentação da API (Swagger): http://localhost:8080/swagger-ui/index.html
 * O esquema "bearer-key" faz aparecer o botão Authorize, onde se cola o token JWT.
 */
@Configuration
public class SpringDocConfigurations {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Aluno Online API")
                        .description("API REST do Aluno Online, com o CRUD de alunos, professores e disciplinas, "
                                + "além de matrículas, lançamento de notas e histórico do aluno"));
    }
}
