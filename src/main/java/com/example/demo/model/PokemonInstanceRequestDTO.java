package com.example.demo.model;

public record PokemonInstanceRequestDTO(
        Long pokemonId,
        Integer power,
        Boolean shine) {
}
