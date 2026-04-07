package com.example.demo.repository;

import com.example.demo.model.PokemonInstance;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PokemonInstanceRepository {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<PokemonInstance> findAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT pi FROM PokemonInstance pi", PokemonInstance.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<PokemonInstance> findById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            PokemonInstance instance = em.find(PokemonInstance.class, id);
            return Optional.ofNullable(instance);
        } finally {
            em.close();
        }
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    public PokemonInstance save(PokemonInstance pokemonInstance) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (pokemonInstance.getId() == null) {
                em.persist(pokemonInstance);
            } else {
                pokemonInstance = em.merge(pokemonInstance);
            }

            transaction.commit();
            return pokemonInstance;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    public void deleteById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();
            PokemonInstance instance = em.find(PokemonInstance.class, id);
            if (instance != null) {
                em.remove(instance);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }
}
