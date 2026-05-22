package br.com.alunoonline.api.controllers;

import br.com.alunoonline.api.models.MatriculaAluno;
import br.com.alunoonline.api.models.dtos.AtualizarNotasRequestDto;
import br.com.alunoonline.api.models.dtos.HistoricoAlunoDto;
import br.com.alunoonline.api.services.MatriculaAlunoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
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
    public void updateGrades(@RequestBody AtualizarNotasRequestDto atualizarNotasRequestDto,
                             @PathVariable Long id) {
        service.updateGrades(atualizarNotasRequestDto, id);
    }

    @PatchMapping("/update-status/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateStatus(@PathVariable Long id) throws Exception {
        service.updateStatusToBreak(id);
    }

    @GetMapping("/historico-aluno/{id}")
    @ResponseStatus(HttpStatus.OK)
    public HistoricoAlunoDto getStudentHistory(@PathVariable Long id) {
        return service.getHistoricoFromAluno(id);
    }
}