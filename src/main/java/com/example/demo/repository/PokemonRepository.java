package com.example.demo.repository;

import com.example.demo.model.Pokemon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceUnit;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PokemonRepository {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public List<Pokemon> findAll() {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            return em.createQuery("SELECT p FROM Pokemon p", Pokemon.class).getResultList();
        } finally {
            em.close();
        }
    }

    public Optional<Pokemon> findById(Long id) {
        EntityManager em = entityManagerFactory.createEntityManager();
        try {
            Pokemon pokemon = em.find(Pokemon.class, id);
            return Optional.ofNullable(pokemon);
        } finally {
            em.close();
        }
    }

    public boolean existsById(Long id) {
        return findById(id).isPresent();
    }

    public Pokemon save(Pokemon pokemon) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        try {
            transaction.begin();

            if (pokemon.getId() == null) {
                em.persist(pokemon);
            } else {
                pokemon = em.merge(pokemon);
            }

            transaction.commit();
            return pokemon;
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
            Pokemon pokemon = em.find(Pokemon.class, id);
            if (pokemon != null) {
                em.remove(pokemon);
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
