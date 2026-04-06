package br.com.alunoonline.api.services;

import br.com.alunoonline.api.models.Professor;
import br.com.alunoonline.api.repositories.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    ProfessorRepository professorRepository;

    public void criarProfessor(Professor professor) {
        professorRepository.save(professor);
    }

    public Iterable<Professor> listarTodosProfessores() {
        return professorRepository.findAll();
    }

    public Optional<Professor> buscarProfessorPorId(Long id) {
        return professorRepository.findById(id);
    }

    public void atualizarProfessorPorId(Long id, Professor professor) {
        Optional<Professor> professorDoBanco = buscarProfessorPorId(id);

        if (professorDoBanco.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Professor não encontrado");
        }

        Professor professorParaEditar = professorDoBanco.get();
        professorParaEditar.setNome(professor.getNome());
        professorParaEditar.setCpf(professor.getCpf());
        professorParaEditar.setEmail(professor.getEmail());

        professorRepository.save(professorParaEditar);
    }



    public void deletarProfessorPorId(Long id) {
        Optional<Professor> professorDoBanco = buscarProfessorPorId(id);

        if (professorDoBanco.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Professor não encontrado para exclusão");
        }

        professorRepository.deleteById(id);
    }
}