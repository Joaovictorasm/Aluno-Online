package br.com.alunoonline.api.models.dtos;

import lombok.Data;

import java.util.List;

@Data
public class HistoricoAlunoDto {
    private String nomeAluno;
    private String emailAluno;
    private List<DisciplinasAlunoDto> disciplinasAlunoList;
}
