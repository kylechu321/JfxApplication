package dao;

import java.sql.SQLException;
import java.util.List;

import model.User;
import model.UserParticipationHistory;

public interface UserDao {
    void setup() throws SQLException;

    User getUser(String username, String password) throws SQLException;
    User createUser(String username, String password, Boolean admin, String firstName, String lastName, String email) throws SQLException;
    boolean isUsernameUnique(String username) throws SQLException;
    boolean updatePassword(String username, String hashedPassword) throws SQLException;
}
