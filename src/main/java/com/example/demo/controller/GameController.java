package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.PokemonInstanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/game")
public class GameController {

    @Autowired
    private PokemonInstanceService pokemonInstanceService;

    @PostMapping("/starter/{pokemonId}")
    public ResponseEntity<?> chooseStarterPokemon(@PathVariable Long pokemonId) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            try {
                Map<String, Object> result = pokemonInstanceService.chooseStarterPokemon(user, pokemonId);
                return ResponseEntity.ok(result);
            } catch (IllegalStateException e) {
                return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            } catch (IllegalArgumentException e) {
                return ResponseEntity.notFound().build();
            }
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/explore")
    public ResponseEntity<?> explore() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof User user) {
            try {
                Map<String, Object> result = pokemonInstanceService.explore(user);
                return ResponseEntity.ok(result);
            } catch (IllegalStateException e) {
                return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
            }
        }
        return ResponseEntity.status(401).build();
    }
}
