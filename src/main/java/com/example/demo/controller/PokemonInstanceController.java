package com.example.demo.controller;

import com.example.demo.model.PokemonInstance;
import com.example.demo.repository.PokemonInstanceRepository;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/pokemon-instances")
public class PokemonInstanceController {

    @Autowired
    private PokemonInstanceRepository repository;

    @GetMapping
    public List<PokemonInstance> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<PokemonInstance> create(@RequestBody PokemonInstance pokemonInstance) {
        PokemonInstance saved = repository.save(pokemonInstance);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @GetMapping("/{id}")
    @SecurityRequirements()
    public ResponseEntity<PokemonInstance> getById(@PathVariable Long id) {
        Optional<PokemonInstance> instance = repository.findById(id);
        return instance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PokemonInstance> update(@PathVariable Long id, @RequestBody PokemonInstance updatedInstance) {
        return repository.findById(id).map(instance -> {
            instance.setPokemon(updatedInstance.getPokemon());
            instance.setPower(updatedInstance.getPower());
            instance.setShine(updatedInstance.getShine());
            return ResponseEntity.ok(repository.save(instance));
        }).orElseGet(() -> ResponseEntity.notFound().build());
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
