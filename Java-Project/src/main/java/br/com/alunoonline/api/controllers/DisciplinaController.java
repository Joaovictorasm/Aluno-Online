package br.com.alunoonline.api.controllers;

import br.com.alunoonline.api.models.Disciplina;
import br.com.alunoonline.api.services.DisciplinaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/disciplina")
public class DisciplinaController {

    @Autowired
    DisciplinaService service;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Disciplina> create(@RequestBody Disciplina disciplina) {
        Disciplina disciplinaCreated = service.create(disciplina);

        return ResponseEntity.status(201).body(disciplinaCreated);
    }

    @GetMapping("/professor/{professorId}")
    @ResponseStatus(HttpStatus.OK)
    public List<Disciplina> findByProfessorId(@PathVariable Long professorId){
        return service.findByProfessorId(professorId);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Disciplina findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/foundall")
    @ResponseStatus(HttpStatus.OK)
    public List<Disciplina> findAll() {
        return service.findAll();
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}