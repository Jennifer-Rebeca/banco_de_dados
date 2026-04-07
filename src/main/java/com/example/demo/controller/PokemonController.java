package com.example.demo.controller;

import com.example.demo.model.Pokemon;
import com.example.demo.repository.PokemonRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pokemons")
public class PokemonController {

    @Autowired
    private PokemonRepository repository;

    @GetMapping
    @SecurityRequirements()
    public List<Pokemon> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Pokemon> create(@RequestBody Pokemon pokemon) {
        Pokemon saved = repository.save(pokemon);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping
    public ResponseEntity<Pokemon> update(@RequestBody Pokemon pokemon) {
        if (pokemon.getId() == null || !repository.existsById(pokemon.getId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(repository.save(pokemon));
    }

    @GetMapping("/{id}")
    @SecurityRequirements()
    public ResponseEntity<Pokemon> getById(@PathVariable Long id) {
        Optional<Pokemon> pokemon = repository.findById(id);
        return pokemon.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
