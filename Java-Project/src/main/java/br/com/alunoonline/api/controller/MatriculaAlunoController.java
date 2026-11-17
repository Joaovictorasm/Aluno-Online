package br.com.alunoonline.api.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import br.com.alunoonline.api.domain.matricula.MatriculaAluno;
import br.com.alunoonline.api.domain.matricula.DadosAtualizacaoNotas;
import br.com.alunoonline.api.domain.matricula.DadosHistoricoAluno;
import br.com.alunoonline.api.domain.matricula.MatriculaAlunoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "bearer-key")
@RequestMapping("/matricula-aluno")
public class MatriculaAlunoController {

    @Autowired
    MatriculaAlunoService service;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<MatriculaAluno> create(@RequestBody MatriculaAluno matriculaAluno) {
        MatriculaAluno matriculaAlunoCreated = service.create(matriculaAluno);

        return ResponseEntity.status(201).body(matriculaAlunoCreated);
    }

    @PatchMapping("/update-grades/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGrades(@RequestBody @Valid DadosAtualizacaoNotas dados,
                             @PathVariable Long id) {
        service.updateGrades(dados, id);
    }

    @PatchMapping("/update-status/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Long id) throws Exception {
        service.updateStatusToBreak(id);
    }

    @GetMapping("/historico-aluno/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DadosHistoricoAluno getStudentHistory(@PathVariable Long id) {
        return service.getHistoricoFromAluno(id);
    }
}