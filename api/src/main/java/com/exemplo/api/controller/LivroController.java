package com.exemplo.api.controller;

import com.exemplo.api.model.Livro;
import com.exemplo.api.service.LivroService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/livros")
public class LivroController {

    private final LivroService service;

    public LivroController(LivroService service) {
        this.service = service;
    }

    /** Listar todos os livros */
    @GetMapping
    public ResponseEntity<List<Livro>> listar() {
        List<Livro> livros = service.listarTodos();
        if (livros.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(livros);
    }

    /** Buscar livro por ID */
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Criar novo livro */
    @PostMapping
    public ResponseEntity<Livro> criar(@RequestBody Livro livro) {
        if (livro.getId() != null && service.buscarPorId(livro.getId()).isPresent()) {
            return ResponseEntity.badRequest().build();
        }
        Livro salvo = service.salvar(livro);
        return ResponseEntity.ok(salvo);
    }

    /** Atualizar livro */
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Long id, @RequestBody Livro livro) {
        return service.buscarPorId(id)
                .map(existing -> {
                    livro.setId(id);
                    Livro atualizado = service.salvar(livro);
                    return ResponseEntity.ok(atualizado);
                }).orElse(ResponseEntity.notFound().build());
    }

    /** Deletar livro */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(existing -> {
                    service.deletar(id);
                    return ResponseEntity.noContent().<Void>build();
                }).orElse(ResponseEntity.notFound().build());
    }
}
