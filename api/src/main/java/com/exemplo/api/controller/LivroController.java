package com.exemplo.api.controller;

import com.exemplo.api.model.Livro;
import com.exemplo.api.service.LivroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livros")
public class LivroController {

    private final LivroService service;

    public LivroController(LivroService service) {
        this.service = service;
    }

    @GetMapping
    public List<Livro> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Livro criar(@RequestBody Livro livro) {
        return service.salvar(livro);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Long id, @RequestBody Livro livro) {
        return service.buscarPorId(id)
                .map(l -> {
                    livro.setId(id);
                    return ResponseEntity.ok(service.salvar(livro));
                }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(l -> {
                    service.deletar(id);
                    return ResponseEntity.noContent().<Void>build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
