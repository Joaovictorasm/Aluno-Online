package br.com.alunoonline.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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
    void semTokenRetorna401() throws Exception {
        mockMvc.perform(get("/alunos/"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void tokenInvalidoRetorna401() throws Exception {
        mockMvc.perform(get("/alunos/").header("Authorization", "Bearer token-falso"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void cadastroLoginEUsoDoToken() throws Exception {
        var token = cadastrarELogar();

        mockMvc.perform(post("/alunos/create")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ALUNO_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/alunos/").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void senhaErradaNoLoginRetorna401() throws Exception {
        var login = cadastrarUsuario();

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(login, "senha-errada")))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginRepetidoNoCadastroRetorna400() throws Exception {
        var login = cadastrarUsuario();

        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(login, "123456")))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cadastroInvalidoMostraOsCamposComErro() throws Exception {
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json("", "123")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[*].campo", hasItem("login")))
                .andExpect(jsonPath("$[*].campo", hasItem("senha")));
    }

    @Test
    void disciplinaInexistenteRetorna404() throws Exception {
        var token = cadastrarELogar();

        mockMvc.perform(get("/disciplina/999999").header("Authorization", "Bearer " + token))
                .andExpect(status().isNotFound());
    }

    @Test
    void swaggerEPublicoETemBearerKey() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.components.securitySchemes.bearer-key.scheme").value("bearer"));
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());
    }

    private String cadastrarUsuario() throws Exception {
        var login = "teste-" + UUID.randomUUID() + "@email.com";
        mockMvc.perform(post("/usuarios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(login, "123456")))
                .andExpect(status().isCreated());
        return login;
    }

    private String cadastrarELogar() throws Exception {
        var login = cadastrarUsuario();
        var resposta = mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json(login, "123456")))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Matcher m = Pattern.compile("\"token\"\\s*:\\s*\"([^\"]+)\"").matcher(resposta);
        if (!m.find()) {
            throw new AssertionError("Login não devolveu token: " + resposta);
        }
        return m.group(1);
    }

    private static String json(String login, String senha) {
        return "{\"login\": \"" + login + "\", \"senha\": \"" + senha + "\"}";
    }
}
