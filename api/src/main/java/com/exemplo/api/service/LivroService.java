package com.exemplo.api.service;

import com.exemplo.api.model.Livro;
import com.exemplo.api.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository repo;

    public LivroService(LivroRepository repo) {
        this.repo = repo;
    }

    public List<Livro> listarTodos() {
        return repo.findAll();
    }

    public Optional<Livro> buscarPorId(Long id) {
        return repo.findById(id);
    }

    public Livro salvar(Livro livro) {
        return repo.save(livro);
    }

    public void deletar(Long id) {
        repo.deleteById(id);
    }
}
