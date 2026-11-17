package br.com.alunoonline.api.domain.matricula;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class MatriculaAlunoService {

    static final Double GRADEAVGTOAPPROVE = 7.0;

    @Autowired
    MatriculaAlunoRepository repository;

    public MatriculaAluno create(MatriculaAluno matriculaAluno) {
        return repository.save(matriculaAluno);
    }

    public void updateGrades(DadosAtualizacaoNotas dados, Long matriculaAlunoId) {
        Optional<MatriculaAluno> matriculaAlunoToUpdate = repository.findById(matriculaAlunoId);

        if (matriculaAlunoToUpdate.isPresent()) {
            MatriculaAluno matriculaAluno = matriculaAlunoToUpdate.get();

            if (dados.nota1() != null) {
                matriculaAluno.setNota1(dados.nota1());
            }

            if (dados.nota2() != null) {
                matriculaAluno.setNota2(dados.nota2());
            }

            if (matriculaAluno.getNota1() != null && matriculaAluno.getNota2() != null) {
                double average = (matriculaAluno.getNota1() + matriculaAluno.getNota2()) / 2.0;

                if (average >= GRADEAVGTOAPPROVE) {
                    matriculaAluno.setStatus("APROVADO");
                } else {
                    matriculaAluno.setStatus("REPROVADO");
                }
            }

            repository.save(matriculaAluno);
        }
    }

    public void updateStatusToBreak(Long matriculaAlunoId) {
        Optional<MatriculaAluno> matriculaAlunoToUpdate = repository.findById(matriculaAlunoId);

        if (matriculaAlunoToUpdate.isPresent()) {
            MatriculaAluno matriculaAluno = matriculaAlunoToUpdate.get();
            String currentStatus = matriculaAluno.getStatus();

            if (currentStatus.equals("MATRICULADO")) {
                matriculaAluno.setStatus("TRANCADO");
                repository.save(matriculaAluno);
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Só é possível trancar uma matrícula com status MATRICULADO.");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Matrícula não encontrada.");
        }
    }

    public DadosHistoricoAluno getHistoricoFromAluno(Long alunoId) {
        List<MatriculaAluno> matriculasDoAluno = repository.findByAlunoId(alunoId);

        if (matriculasDoAluno.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Esse aluno não possui matrículas.");
        }

        var aluno = matriculasDoAluno.get(0).getAluno();
        var disciplinas = matriculasDoAluno.stream()
                .map(DadosDisciplinaAluno::new)
                .toList();

        return new DadosHistoricoAluno(aluno.getNome(), aluno.getEmail(), disciplinas);
    }

}