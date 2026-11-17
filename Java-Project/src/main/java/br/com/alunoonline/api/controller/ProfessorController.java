package br.com.alunoonline.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import br.com.alunoonline.api.domain.professor.Professor;
import br.com.alunoonline.api.domain.professor.ProfessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    ProfessorService professorService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Professor> ListarTodosProfessores() {
        return professorService.listarTodosProfessores();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Professor> buscarProfessorPorId(@PathVariable Long id) {
        return professorService.buscarProfessorPorId(id);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void criarProfessor(@RequestBody Professor professor) {
        professorService.criarProfessor(professor);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void atualizarProfessorPorId(@RequestBody Professor professor, @PathVariable Long id) {  professorService.atualizarProfessorPorId(id, professor); }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable Long id) {
        professorService.deletarProfessorPorId(id);
    }
}
