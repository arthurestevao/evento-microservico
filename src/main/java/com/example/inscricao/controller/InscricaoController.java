package com.example.inscricao.controller;

import com.example.inscricao.dto.InscricaoRequest;
import com.example.inscricao.dto.InscricaoResponse;
import com.example.inscricao.services.InscricaoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inscricoes")
@RequiredArgsConstructor
public class InscricaoController {

    private final InscricaoService inscricaoService;

    @PostMapping
    public ResponseEntity<InscricaoResponse> inscrever(@RequestBody @Valid InscricaoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(inscricaoService.inscrever(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<InscricaoResponse> atualizar(@PathVariable Long id, @RequestBody @Valid InscricaoRequest request) {
        return ResponseEntity.ok(inscricaoService.atualizar(id, request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InscricaoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(inscricaoService.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<InscricaoResponse>> buscarTodos() {
        return ResponseEntity.ok(inscricaoService.buscarTodos());
    }

    @GetMapping("/evento/{eventoId}")
    public ResponseEntity<List<InscricaoResponse>> buscarPorEvento(@PathVariable Long eventoId) {
        return ResponseEntity.ok(inscricaoService.buscarPorEvento(eventoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelar(@PathVariable Long id) {
        inscricaoService.cancelar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}