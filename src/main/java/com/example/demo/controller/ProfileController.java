package com.example.demo.controller;

import com.example.demo.model.PokemonInstanceResponseDTO;
import com.example.demo.repository.PokemonInstanceRepository;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/me")
public class ProfileController {

    @Autowired
    private PokemonInstanceRepository pokemonInstanceRepository;

    @GetMapping
    public ResponseEntity<?> getProfileName() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            return ResponseEntity.ok(Map.of(
                    "user", user.getLogin(),
                    "role", user.getRole()));
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/pokedex")
    public ResponseEntity<List<PokemonInstanceResponseDTO>> getPokedex() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            var entries = pokemonInstanceRepository.findAllByTrainer(user);
            List<PokemonInstanceResponseDTO> response = entries.stream()
                    .map(PokemonInstanceResponseDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(401).build();
    }
}
