package br.com.alunoonline.api.domain.matricula;

public record DadosDisciplinaAluno(
        String nomeDisciplina,
        String professorDisciplina,
        Double nota1,
        Double nota2,
        Double media,
        String status
) {
    public DadosDisciplinaAluno(MatriculaAluno matricula) {
        this(
                matricula.getDisciplina().getName(),
                matricula.getDisciplina().getProfessor().getNome(),
                matricula.getNota1(),
                matricula.getNota2(),
                calcularMedia(matricula),
                matricula.getStatus()
        );
    }

    private static Double calcularMedia(MatriculaAluno matricula) {
        if (matricula.getNota1() == null || matricula.getNota2() == null) {
            return null;
        }
        return (matricula.getNota1() + matricula.getNota2()) / 2;
    }
}
