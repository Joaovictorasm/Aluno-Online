package br.com.alunoonline.api.domain.disciplina;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DisciplinaService {

    @Autowired
    DisciplinaRepository repository;

    public Disciplina create(Disciplina disciplina) {
        return repository.save(disciplina);
    }

    public List<Disciplina> findByProfessorId(Long professorId) {
        return repository.findByProfessorId(professorId);
    }

    public  Disciplina findById(Long id) {
        return repository.findById(id).get();
    }

    public List<Disciplina> findAll() {
        return repository.findAll();
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}