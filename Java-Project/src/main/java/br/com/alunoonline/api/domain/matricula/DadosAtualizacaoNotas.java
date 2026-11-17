package br.com.alunoonline.api.domain.matricula;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;

public record DadosAtualizacaoNotas(
        @DecimalMin(value = "0.0", message = "A nota 1 não pode ser menor que 0")
        @DecimalMax(value = "10.0", message = "A nota 1 não pode ser maior que 10")
        Double nota1,

        @DecimalMin(value = "0.0", message = "A nota 2 não pode ser menor que 0")
        @DecimalMax(value = "10.0", message = "A nota 2 não pode ser maior que 10")
        Double nota2
) {
}
