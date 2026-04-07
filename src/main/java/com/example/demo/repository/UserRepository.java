package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import java.util.Optional;

@Repository
public class UserRepository {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    public UserDetails findByLogin(String login) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            return entityManager.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", login)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);
        } finally {
            entityManager.close();
        }
    }

    public Optional<User> findById(Long id) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            User user = entityManager.find(User.class, id);
            return Optional.ofNullable(user);
        } finally {
            entityManager.close();
        }
    }

    public User save(User user) {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            if (user.getId() == null) {
                entityManager.persist(user);
            } else {
                user = entityManager.merge(user);
            }
            entityManager.getTransaction().commit();
            return user;
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
