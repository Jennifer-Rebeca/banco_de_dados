package com.example.demo.service;

import com.example.demo.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceUnit;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthorizationService implements UserDetailsService {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        try {
            UserDetails user = entityManager.createQuery("SELECT u FROM User u WHERE u.login = :login", User.class)
                    .setParameter("login", username)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }
            return user;
        } finally {
            entityManager.close();
        }
    }
}