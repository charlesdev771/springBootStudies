package com.exemplo.api.controller;

import com.exemplo.api.dto.LoginRequest;
import com.exemplo.api.model.Usuario;
import com.exemplo.api.repository.UsuarioRepository;
import com.exemplo.api.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final UsuarioRepository usuarioRepo;

    public AuthController(JwtUtil jwtUtil, UsuarioRepository usuarioRepo) {
        this.jwtUtil = jwtUtil;
        this.usuarioRepo = usuarioRepo;
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<Usuario> usuarioOpt = usuarioRepo.findByEmail(request.getEmail());

        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(401).body("Usuário não encontrado");
        }

        Usuario usuario = usuarioOpt.get();

        // Em produção você deve usar hash de senha
        if (!usuario.getSenha().equals(request.getSenha())) {
            return ResponseEntity.status(401).body("Senha incorreta");
        }

        String token = jwtUtil.gerarToken(usuario.getEmail());
        return ResponseEntity.ok(token);
    }

    // Logout (apenas para frontend, JWT não precisa invalidar no backend)
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok("Logout realizado com sucesso");
    }
}
