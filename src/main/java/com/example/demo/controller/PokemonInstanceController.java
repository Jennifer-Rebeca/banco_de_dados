package com.example.demo.controller;

import com.example.demo.model.PokemonInstanceRequestDTO;
import com.example.demo.model.PokemonInstanceResponseDTO;
import com.example.demo.service.PokemonInstanceService;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/pokemon-instances")
public class PokemonInstanceController {

    @Autowired
    private PokemonInstanceService pokemonInstanceService;

    @GetMapping
    public List<PokemonInstanceResponseDTO> getAll() {
        return pokemonInstanceService.listarTodos();
    }

    @PostMapping
    public ResponseEntity<PokemonInstanceResponseDTO> create(@RequestBody PokemonInstanceRequestDTO dto) {
        try {
            PokemonInstanceResponseDTO saved = pokemonInstanceService.salvar(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @SecurityRequirements()
    public ResponseEntity<PokemonInstanceResponseDTO> getById(@PathVariable UUID id) {
        Optional<PokemonInstanceResponseDTO> instance = pokemonInstanceService.buscarPorId(id);
        return instance.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PokemonInstanceResponseDTO> update(@PathVariable UUID id, @RequestBody PokemonInstanceRequestDTO dto) {
        try {
            PokemonInstanceResponseDTO updated = pokemonInstanceService.atualizar(id, dto);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        pokemonInstanceService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
