package com.example.demo.model;

import java.util.UUID;

public record PokemonInstanceResponseDTO(
        UUID id,
        String pokemonName,
        Integer pokemonNumero,
        Integer power,
        Boolean shine,
        String trainerName) {
    public PokemonInstanceResponseDTO(PokemonInstance instance) {
        this(
                instance.getId(),
                instance.getPokemon().getNome(),
                instance.getPokemon().getNumero(),
                instance.getPower(),
                instance.getShine(),
                instance.getTrainer() != null ? instance.getTrainer().getLogin() : null);
    }
}
