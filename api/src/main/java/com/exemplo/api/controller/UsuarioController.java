package com.exemplo.api.controller;

import com.exemplo.api.model.Usuario;
import com.exemplo.api.repository.UsuarioRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioRepository usuarioRepo;

    public UsuarioController(UsuarioRepository usuarioRepo) {
        this.usuarioRepo = usuarioRepo;
    }

    /** Listar todos os usuários */
    @GetMapping
    public ResponseEntity<List<Usuario>> listar() {
        List<Usuario> usuarios = usuarioRepo.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(usuarios);
    }

    /** Buscar usuário por ID */
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> buscarPorId(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepo.findById(id);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Criar novo usuário */
    @PostMapping
    public ResponseEntity<Usuario> criar(@RequestBody Usuario usuario) {
        if (usuario.getId() != null && usuarioRepo.existsById(usuario.getId())) {
            return ResponseEntity.badRequest().build();
        }
        Usuario salvo = usuarioRepo.save(usuario);
        return ResponseEntity.ok(salvo);
    }

    /** Atualizar usuário */
    @PutMapping("/{id}")
    public ResponseEntity<Usuario> atualizar(@PathVariable Long id, @RequestBody Usuario usuario) {
        Optional<Usuario> existing = usuarioRepo.findById(id);
        if (existing.isEmpty()) return ResponseEntity.notFound().build();

        usuario.setId(id);
        Usuario atualizado = usuarioRepo.save(usuario);
        return ResponseEntity.ok(atualizado);
    }

    /** Deletar usuário */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (!usuarioRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        usuarioRepo.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
