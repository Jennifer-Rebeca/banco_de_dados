package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pokemon_instance")
public class PokemonInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "pokemon_id", nullable = false)
    private Pokemon pokemon;

    private Integer power;
    private Boolean shine;

    public PokemonInstance() {}

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }
    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Integer getPower() {
        return power;
    }
    public void setPower(Integer power) {
        this.power = power;
    }

    public Boolean getShine() {
        return shine;
    }
    public void setShine(Boolean shine) {
        this.shine = shine;
    }
}
