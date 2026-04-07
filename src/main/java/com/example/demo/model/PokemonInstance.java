package com.example.demo.model;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pokemon_instances")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PokemonInstance {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pokemon_id", nullable = false)
    private Pokemon pokemon;

    @Column(nullable = false)
    private Integer power;

    @Column(nullable = false)
    private Boolean shine;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id")
    private User trainer;

    public PokemonInstance(Pokemon pokemon, Integer power, Boolean shine, User trainer) {
        this.pokemon = pokemon;
        this.power = power;
        this.shine = shine;
        this.trainer = trainer;
    }

}
