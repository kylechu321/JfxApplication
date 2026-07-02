package dao;

import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoImplTest {

    private UserDaoImpl userDao;

    @BeforeEach
    void setup() {
        userDao = new UserDaoImpl();
        try {
            userDao.setup();

            try (Connection connection = Database.getConnection();
                 PreparedStatement stmt = connection.prepareStatement("DELETE FROM users WHERE username = ?")) {
                stmt.setString(1, "testuser");
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void getUser() {
        String username = "testuser";
        String password = "Valid123!";
        String hashedPassword = UserDaoImpl.hashPasswordWithSha(password);

        try {
            userDao.createUser(username, hashedPassword, false, "Test", "User", "test@example.com");
        } catch (SQLException e) {
            Assertions.fail(e.getMessage());
        }

        try {
            User result = userDao.getUser(username, password);

            assertNotNull(result);
            assertEquals(username, result.getUsername());
            assertEquals(hashedPassword, result.getPassword());
            assertEquals("Test", result.getFirstname());
            assertEquals("User", result.getLastname());
            assertEquals("test@example.com", result.getEmail());
            assertFalse(result.getAdmin());
        } catch (SQLException e) {
            Assertions.fail(e.getMessage());
        }
    }


    @Test
    void isUsernameUnique() {
        String username = "testuser";
        String password = UserDaoImpl.hashPasswordWithSha("Valid123!");

        try {
            // Create test user
            userDao.createUser(username, password, false, "Test", "User", "test@example.com");

            // Should return false (user exists)
            assertFalse(userDao.isUsernameUnique("testuser"));

            // Should return true (user does not exist)
            assertTrue(userDao.isUsernameUnique("newuser"));

        } catch (SQLException e) {
            fail("Test failed: " + e.getMessage());
        }

    }

    @Test
    void isValidPassword() {
        assertTrue(UserDaoImpl.isValidPassword("Valid123!"));
        assertTrue(UserDaoImpl.isValidPassword("Strong@2023"));
        assertTrue(UserDaoImpl.isValidPassword("Aa1@aaaa"));

        //too short
        assertFalse(UserDaoImpl.isValidPassword("short1!"));
        //no uppercase
        assertFalse(UserDaoImpl.isValidPassword("nouppercase1!"));
        //no lowercase
        assertFalse(UserDaoImpl.isValidPassword("NOLOWERCASE1!"));
        //no special character
        assertFalse(UserDaoImpl.isValidPassword("NoSpecial123"));
        //no number
        assertFalse(UserDaoImpl.isValidPassword("NoNumber!"));
    }
}