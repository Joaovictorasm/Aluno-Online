package br.com.alunoonline.api.domain.matricula;

import java.util.List;

public record DadosHistoricoAluno(
        String nomeAluno,
        String emailAluno,
        List<DadosDisciplinaAluno> disciplinasAlunoList
) {
}
