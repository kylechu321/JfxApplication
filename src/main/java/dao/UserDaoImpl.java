package dao;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import model.User;
import model.UserParticipationHistory;

public class UserDaoImpl implements UserDao{
    private final String TABLE_NAME = "users";

    public UserDaoImpl() {
    }

    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();) {
            //user table
            String sql =
                    "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                            " (username VARCHAR(10) NOT NULL," +
                            "password VARCHAR(8) NOT NULL," +
                            "admin BOOLEAN NOT NULL," +
                            "firstname VARCHAR(30) NOT NULL," +
                            "lastname VARCHAR(30) NOT NULL," +
                            "email VARCHAR(30) NOT NULL," +
                            "PRIMARY KEY (username))";

            stmt.executeUpdate(sql);
        }

    }

    @Override
    public User getUser(String username, String password) throws SQLException {
        String sql = "SELECT * FROM " + TABLE_NAME +
                     " WHERE username = ? AND password = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);) {

            String hashPassword = hashPasswordWithSha(password);

            stmt.setString(1, username);
            stmt.setString(2, hashPassword);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    User user = new User();
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setAdmin(rs.getBoolean("admin"));
                    user.setFirstname(rs.getString("firstname"));
                    user.setLastname(rs.getString("lastname"));
                    user.setEmail(rs.getString("email"));
                    return user;

                }

                return null;

            }

        }

    }

    @Override
    public User createUser(String username, String password, Boolean admin, String firstName, String lastName, String email) throws SQLException {
        System.out.println("Updating password for user: " + username);

        String sql = "INSERT INTO " + TABLE_NAME + " VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setBoolean(3, admin);
            stmt.setString(4, firstName);
            stmt.setString(5, lastName);
            stmt.setString(6, email);

            stmt.executeUpdate();
            return new User(username, password, admin, firstName, lastName, email);

        }

    }

    @Override
    public boolean isUsernameUnique(String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM " + TABLE_NAME + " WHERE username = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(1) == 0;

                }

            }
        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }

    @Override
    public boolean updatePassword(String username, String hashPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE username = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashPassword);
            stmt.setString(2, username);
            return stmt.executeUpdate() > 0;
        }
    }


    //hashing password
    public static String hashPasswordWithSha(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b: hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    //validate password
    public static boolean isValidPassword(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,}$";
        Pattern pattern = Pattern.compile(PASSWORD_REGEX);
        Matcher matcher = pattern.matcher(password);

        return matcher.matches();

    }

}
