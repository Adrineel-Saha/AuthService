package com.cognizant.authservice.test.repositories;

import com.cognizant.authservice.entities.UserCredential;
import com.cognizant.authservice.main.AuthServiceApplication;
import com.cognizant.authservice.repositories.UserCredentialRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = AuthServiceApplication.class)
@ActiveProfiles("test")
class TestUserCredentialRepository {
    @Autowired
    private UserCredentialRepository userCredentialRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testFindAllPositive() {
        UserCredential userCredential = new UserCredential();
        userCredential.setUserName("Adrineel");
        userCredential.setEmail("Adrineel@example.com");
        userCredential.setPassword("password123");
        userCredential.setRole("USER");

        entityManager.persist(userCredential);

        List<UserCredential> userCredentialList = userCredentialRepository.findAll();
        assertTrue(userCredentialList.iterator().hasNext());
    }

    @Test
    void testFindByIdPositive() {
        UserCredential userCredential = new UserCredential();
        userCredential.setUserName("Akash");
        userCredential.setEmail("Akash@example.com");
        userCredential.setPassword("password123");
        userCredential.setRole("ADMIN");

        entityManager.persist(userCredential);
        int id = userCredential.getId();

        Optional<UserCredential> userCredentialOptional = userCredentialRepository.findById(id);
        assertTrue(userCredentialOptional.isPresent());
    }

    @Test
    void testFindByIdNegative() {
        Optional<UserCredential> userCredentialOptional = userCredentialRepository.findById(-1);
        assertTrue(!userCredentialOptional.isPresent());
    }

    @Test
    void testSavePositive() {
        UserCredential userCredential = new UserCredential();
        userCredential.setUserName("Yash");
        userCredential.setEmail("Yash@example.com");
        userCredential.setPassword("password123");
        userCredential.setRole("USER");

        userCredentialRepository.save(userCredential);
        int id = userCredential.getId();

        Optional<UserCredential> userCredentialOptional = userCredentialRepository.findById(id);
        assertTrue(userCredentialOptional.isPresent());
    }

    @Test
    void deletePositive() {
        UserCredential userCredential = new UserCredential();
        userCredential.setUserName("Arunabh");
        userCredential.setEmail("Arunabh@example.com");
        userCredential.setPassword("password123");
        userCredential.setRole("MODERATOR");

        entityManager.persist(userCredential);
        int id = userCredential.getId();

        userCredentialRepository.delete(userCredential);
        Optional<UserCredential> userCredentialOptional = userCredentialRepository.findById(id);
        assertTrue(!userCredentialOptional.isPresent());
    }

    @Test
    void findByUserNamePositive() {
        UserCredential userCredential = new UserCredential();
        userCredential.setUserName("Suraj");
        userCredential.setEmail("Suraj@example.com");
        userCredential.setPassword("password123");
        userCredential.setRole("GUEST");

        entityManager.persist(userCredential);
        String userName = userCredential.getUserName();

        Optional<UserCredential> userCredentialOptional = userCredentialRepository.findByUserName(userName);
        assertTrue(userCredentialOptional.isPresent());
    }

    @Test
    void findByUserNameNegative() {
        Optional<UserCredential> userCredentialOptional = userCredentialRepository.findByUserName("NonExistentUser");
        assertTrue(!userCredentialOptional.isPresent());
    }
}
