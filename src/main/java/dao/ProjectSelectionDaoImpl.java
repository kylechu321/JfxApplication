package dao;

import model.User;
import model.UserParticipationHistory;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ProjectSelectionDaoImpl implements ProjectSelectionDao{
    private final String TABLE_NAME = "participation_history";

    @Override
    public void setup() throws SQLException {
        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement();) {
            //user table
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                    "(registration_id VARCHAR(30) PRIMARY KEY," +
                    "confirmation_datetime VARCHAR(100) NOT NULL," +
                    "project_id VARCHAR(30) NOT NULL," +
                    "project_name VARCHAR(30) NOT NULL," +
                    "project_location VARCHAR(30) NOT NULL," +
                    "project_weekday VARCHAR(30) NOT NULL," +
                    "number_of_slots VARCHAR(30) NOT NULL," +
                    "hours_per_slot VARCHAR(30) NOT NULL," +
                    "hourly_rate VARCHAR(30) NOT NULL," +
                    "total_contribution VARCHAR(30) NOT NULL," +
                    "username VARCHAR(30) NOT NULL," +
                    "FOREIGN KEY (username) REFERENCES users(username)," +
                    "FOREIGN KEY (project_id) REFERENCES available_projects(project_id)" +
                    ")";

            stmt.executeUpdate(sql);

        }
    }

    @Override
    public List<UserParticipationHistory> loadParticipationHistory(User user) throws SQLException {
        List<UserParticipationHistory> historyList = new ArrayList<>();
        String sql = "SELECT * FROM participation_history WHERE username = ?";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {

                    UserParticipationHistory history = new UserParticipationHistory(
                            rs.getString("registration_id"),
                            rs.getTimestamp("confirmation_datetime").toLocalDateTime(),
                            rs.getInt("project_id"),
                            rs.getString("project_name"),
                            rs.getString("project_location"),
                            rs.getString("project_weekday"),
                            rs.getInt("number_of_slots"),
                            rs.getInt("hours_per_slot"),
                            rs.getInt("hourly_rate"),
                            Double.parseDouble(rs.getString("total_contribution"))
                    );
                    historyList.add(history);
                }
            }
        }
        return historyList;
    }

    @Override
    public void saveParticipationHistory(List<UserParticipationHistory> historyList, User user) throws SQLException {
        String sql = "INSERT INTO participation_history " +
                        "(registration_id, " +
                        "confirmation_datetime, " +
                        "project_id ," +
                        "project_name, " +
                        "project_location, " +
                        "project_weekday, " +
                        "number_of_slots, " +
                        "hours_per_slot, " +
                        "hourly_rate, " +
                        "total_contribution, " +
                        "username) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)){

            for (UserParticipationHistory history : historyList) {
                String formattedTimeStamp = history.getConfirmationDateTime().format(formatter);

                stmt.setString(1, history.getRegistrationId());
                stmt.setString(2, formattedTimeStamp);
                stmt.setInt(3, history.getProjectId());
                stmt.setString(4, history.getProjectName());
                stmt.setString(5, history.getProjectLocation());
                stmt.setString(6, history.getProjectWeekday());
                stmt.setInt(7, history.getNumberOfSlots());
                stmt.setInt(8, history.getHoursPerSlot());
                stmt.setInt(9, history.getHourlyRate());
                stmt.setDouble(10, history.getTotalContribution());
                stmt.setString(11, user.getUsername());
                stmt.addBatch();
            }

            stmt.executeBatch();
        }
    }
    @Override
    public List<UserParticipationHistory> loadAllParticipationHistory() throws SQLException {
        List<UserParticipationHistory> historyList = new ArrayList<>();
        String sql = "SELECT * FROM participation_history";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                UserParticipationHistory history = new UserParticipationHistory(
                        rs.getString("registration_id"),
                        rs.getTimestamp("confirmation_datetime").toLocalDateTime(),
                        rs.getString("username"),
                        rs.getInt("project_id"),
                        rs.getString("project_name"),
                        rs.getString("project_location"),
                        rs.getString("project_weekday"),
                        rs.getInt("number_of_slots"),
                        rs.getInt("hours_per_slot"),
                        rs.getInt("hourly_rate"),
                        Double.parseDouble(rs.getString("total_contribution"))
                );
                historyList.add(history);
            }
        }

        return historyList;
    }

}
