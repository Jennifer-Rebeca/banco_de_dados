package com.example.demo.service;

import com.example.demo.model.Pokemon;
import com.example.demo.model.PokemonInstance;
import com.example.demo.model.PokemonInstanceRequestDTO;
import com.example.demo.model.PokemonInstanceResponseDTO;
import com.example.demo.model.User;
import com.example.demo.repository.PokemonInstanceRepository;
import com.example.demo.repository.PokemonRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PokemonInstanceService {

    private final PokemonInstanceRepository pokemonInstanceRepository;
    private final PokemonRepository pokemonRepository;

    public PokemonInstanceService(PokemonInstanceRepository pokemonInstanceRepository,
            PokemonRepository pokemonRepository) {
        this.pokemonInstanceRepository = pokemonInstanceRepository;
        this.pokemonRepository = pokemonRepository;
    }

    public List<PokemonInstanceResponseDTO> listarTodos() {
        return pokemonInstanceRepository.findAll().stream()
                .map(PokemonInstanceResponseDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<PokemonInstanceResponseDTO> buscarPorId(UUID id) {
        return pokemonInstanceRepository.findById(id).map(PokemonInstanceResponseDTO::new);
    }

    @Transactional
    public PokemonInstanceResponseDTO salvar(PokemonInstanceRequestDTO dto) {
        if (dto.pokemonId() == null) {
            throw new IllegalArgumentException("O ID do Pokémon base é obrigatório para instanciar.");
        }
        if (dto.power() == null || dto.power() < 0) {
            throw new IllegalArgumentException("O poder (power) é obrigatório e não pode ser negativo.");
        }
        if (dto.shine() == null) {
            throw new IllegalArgumentException("O atributo shine é obrigatório.");
        }

        Pokemon pokemon = pokemonRepository.findById(dto.pokemonId())
                .orElseThrow(() -> new IllegalArgumentException("Pokémon não encontrado com o ID fornecido."));

        PokemonInstance instance = new PokemonInstance();
        instance.setPokemon(pokemon);
        instance.setPower(dto.power());
        instance.setShine(dto.shine());
        return new PokemonInstanceResponseDTO(pokemonInstanceRepository.save(instance));
    }

    @Transactional
    public PokemonInstanceResponseDTO atualizar(UUID id, PokemonInstanceRequestDTO dto) {
        Optional<PokemonInstance> existingOpt = pokemonInstanceRepository.findById(id);
        if (existingOpt.isEmpty()) {
            throw new IllegalArgumentException("Instância de Pokémon não encontrada.");
        }

        PokemonInstance existing = existingOpt.get();
        if (dto.pokemonId() != null) {
            Pokemon pokemon = pokemonRepository.findById(dto.pokemonId())
                    .orElseThrow(() -> new IllegalArgumentException("Pokémon não encontrado com o ID fornecido."));
            existing.setPokemon(pokemon);
        }
        if (dto.power() != null) {
            existing.setPower(dto.power());
        }
        if (dto.shine() != null) {
            existing.setShine(dto.shine());
        }

        return new PokemonInstanceResponseDTO(pokemonInstanceRepository.save(existing));
    }

    @Transactional
    public void deletar(UUID id) {
        pokemonInstanceRepository.deleteById(id);
    }

    @Transactional
    public Map<String, Object> explore(User user) {
        Optional<PokemonInstance> randomWildOpt = pokemonInstanceRepository.findRandomAvailable();
        if (randomWildOpt.isEmpty()) {
            throw new IllegalStateException("Nenhum Pokémon encontrado no mundo!");
        }
        var wildPokemon = randomWildOpt.get();

        var currentPokedex = pokemonInstanceRepository.findAllByTrainer(user);
        int totalPower = currentPokedex.stream()
                .mapToInt(PokemonInstance::getPower)
                .sum();

        if (totalPower > wildPokemon.getPower()) {
            wildPokemon.setTrainer(user);
            pokemonInstanceRepository.save(wildPokemon);

            return Map.of(
                    "message", "Você encontrou e capturou um " + wildPokemon.getPokemon().getNome() + "!",
                    "pokemon", wildPokemon.getPokemon(),
                    "yourPower", totalPower);
        } else {
            return Map.of(
                    "message",
                    "Você encontrou um " + wildPokemon.getPokemon().getNome() + ", mas o poder dele ("
                            + wildPokemon.getPower()
                            + ") era maior ou igual ao seu (" + totalPower + "), então ele fugiu!",
                    "pokemon", wildPokemon.getPokemon(),
                    "yourPower", totalPower);
        }
    }

    @Transactional
    public Map<String, Object> chooseStarterPokemon(User user, Long pokemonId) {
        List<PokemonInstance> currentPokedex = pokemonInstanceRepository.findAllByTrainer(user);
        if (!currentPokedex.isEmpty()) {
            throw new IllegalStateException("Você já pegou o seu Pokémon inicial!");
        }

        if (pokemonId != 1 && pokemonId != 4 && pokemonId != 7 && pokemonId != 25) {
            throw new IllegalStateException("Você só pode escolher Bulbasaur, Charmander ou Squirtle ... como inicial!");
        }

        Pokemon pokemon = pokemonRepository.findById(pokemonId)
                .orElseThrow(() -> new IllegalArgumentException("Pokémon inicial não encontrado."));

        PokemonInstance newEntry = new PokemonInstance(pokemon, new java.util.Random().nextInt(75) + 25, false, user);
        pokemonInstanceRepository.save(newEntry);

        return Map.of("message", "Professor Carvalho entregou o seu " + pokemon.getNome() + " com sucesso!");
    }
}
