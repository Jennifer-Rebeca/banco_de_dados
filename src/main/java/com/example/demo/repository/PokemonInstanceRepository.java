package com.example.demo.repository;

import org.springframework.stereotype.Repository;
import com.example.demo.model.PokemonInstance;
import com.example.demo.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class PokemonInstanceRepository {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<PokemonInstance> findAllByTrainer(User trainer) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT p FROM PokemonInstance p WHERE p.trainer = :trainer", PokemonInstance.class)
                    .setParameter("trainer", trainer)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public Long countByTrainerIsNull() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager
                    .createQuery("SELECT COUNT(p) FROM PokemonInstance p WHERE p.trainer IS NULL", Long.class)
                    .getSingleResult();
        } finally {
            entityManager.close();
        }
    }

    public Optional<PokemonInstance> findRandomAvailable() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            List<PokemonInstance> results = entityManager.createNativeQuery(
                    "SELECT * FROM pokemon_instances WHERE trainer_id IS NULL ORDER BY RAND() LIMIT 1",
                    PokemonInstance.class)
                    .getResultList();

            if (results.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(results.get(0));
        } finally {
            entityManager.close();
        }
    }

    public List<PokemonInstance> findAll() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("SELECT p FROM PokemonInstance p", PokemonInstance.class)
                    .getResultList();
        } finally {
            entityManager.close();
        }
    }

    public Optional<PokemonInstance> findById(UUID id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            PokemonInstance instance = entityManager.find(PokemonInstance.class, id);
            return Optional.ofNullable(instance);
        } finally {
            entityManager.close();
        }
    }

    public PokemonInstance save(PokemonInstance instance) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            if (instance.getId() == null) {
                entityManager.persist(instance);
            } else {
                instance = entityManager.merge(instance);
            }
            entityManager.getTransaction().commit();
            return instance;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }

    public void deleteById(UUID id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            PokemonInstance instance = entityManager.find(PokemonInstance.class, id);
            if (instance != null) {
                entityManager.remove(instance);
            }
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e;
        } finally {
            entityManager.close();
        }
    }
}