package br.com.alunoonline.api.services;

import br.com.alunoonline.api.models.Aluno;
import br.com.alunoonline.api.models.Professor;
import br.com.alunoonline.api.repositories.AlunoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class AlunoService {

    @Autowired
    AlunoRepository alunoRepository;


    public void criarAluno(Aluno aluno) {
        alunoRepository.save(aluno);
    }

    
    public List<Aluno> listarTodosAlunos() {
        return alunoRepository.findAll();
    }


    public Optional<Aluno> buscarAlunoPorId(Long id) {
        return alunoRepository.findById(id);
    }


    public void atualizarAlunoPorId(Long id, Aluno aluno) {
        Optional<Aluno> alunoDoBanco = buscarAlunoPorId(id);

        if (alunoDoBanco.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Aluno não encontrado");
        }

        Aluno alunoParaEditar = alunoDoBanco.get();
        alunoParaEditar.setNome(aluno.getNome());
        alunoParaEditar.setCpf(aluno.getCpf());
        alunoParaEditar.setEmail(aluno.getEmail());

        alunoRepository.save(alunoParaEditar);


    }

    public void deletarAlunoPorId(Long id) {
        Optional<Aluno> alunoDoBanco = buscarAlunoPorId(id);

        if (alunoDoBanco.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Aluno não encontrado para exclusão");
        }

        alunoRepository.deleteById(id);
    }
}