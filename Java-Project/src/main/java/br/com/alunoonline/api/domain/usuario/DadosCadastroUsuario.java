package br.com.alunoonline.api.domain.usuario;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DadosCadastroUsuario(
        @NotBlank(message = "Login é obrigatório")
        @Size(max = 100, message = "Login pode ter no máximo 100 caracteres")
        String login,

        @NotBlank(message = "Senha é obrigatória")
        @Size(min = 6, message = "Senha precisa ter pelo menos 6 caracteres")
        String senha
) {
}
