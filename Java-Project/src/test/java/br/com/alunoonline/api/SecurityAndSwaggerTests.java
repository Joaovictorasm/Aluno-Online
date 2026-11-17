package br.com.alunoonline.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityAndSwaggerTests {

    private static final String ALUNO_JSON = """
            {"nome": "Maria", "cpf": "12345678900", "email": "maria@email.com"}
            """;

    @Autowired
    MockMvc mockMvc;

    @Test
    void semLoginRetorna401() throws Exception {
        mockMvc.perform(get("/alunos/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void senhaErradaRetorna401() throws Exception {
        mockMvc.perform(get("/alunos/").with(httpBasic("admin", "errada")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void usuarioComumPodeConsultar() throws Exception {
        mockMvc.perform(get("/alunos/").with(httpBasic("usuario", "usuario123")))
                .andExpect(status().isOk());
    }

    @Test
    void usuarioComumNaoPodeCriar() throws Exception {
        mockMvc.perform(post("/alunos/create")
                        .with(httpBasic("usuario", "usuario123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ALUNO_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminPodeCriar() throws Exception {
        mockMvc.perform(post("/alunos/create")
                        .with(httpBasic("admin", "admin123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ALUNO_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void swaggerEPublico() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }
}
