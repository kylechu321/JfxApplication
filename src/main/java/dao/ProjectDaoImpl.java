package dao;

import model.AvailableProject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectDaoImpl implements ProjectDao{
    private final String TABLE_NAME = "available_projects";

    @Override
    public void setup() throws SQLException {

        //availiable project table
        String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME +
                "(project_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "project_name VARCHAR(30) NOT NULL," +
                "project_location VARCHAR(30) NOT NULL," +
                "project_weekday VARCHAR(30) NOT NULL," +
                "hourly_rate VARCHAR(30) NOT NULL," +
                "available_slots VARCHAR(30) NOT NULL," +
                "total_slot VARCHAR(30) NOT NULL," +
                "enabled BOOLEAN NOT NULL DEFAULT 1)";


        try (Connection connection = Database.getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.executeUpdate(sql);
        }
    }

    @Override
    public void updateProjectSlots(List<AvailableProject> projects) throws SQLException {
        String sql = "UPDATE " + TABLE_NAME + " SET available_slots = ? WHERE project_id = ?";
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (AvailableProject project : projects) {
                stmt.setInt(1, project.getRegisteredSlot());
                stmt.setInt(2, project.getProjectId());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public List<AvailableProject> loadAvailableProjects() throws SQLException {
        List<AvailableProject> projects = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_NAME;
        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                AvailableProject project = new AvailableProject(
                        rs.getInt("project_id"),
                        rs.getString("project_name"),
                        rs.getString("project_location"),
                        rs.getString("project_weekday"),
                        rs.getInt("hourly_rate"),
                        rs.getInt("available_slots"),
                        rs.getInt("total_slot"),
                        rs.getBoolean("enabled")
                );
                projects.add(project);
            }
        }
        return projects;
    }

    @Override
    public void insertOrUpdateProject(AvailableProject project) throws SQLException {
        String sql = "INSERT OR REPLACE INTO " + TABLE_NAME + " (project_id, project_name, project_location, project_weekday, hourly_rate, available_slots, total_slot, enabled) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, project.getProjectId());
            stmt.setString(2, project.getProjectName());
            stmt.setString(3, project.getProjectLocation());
            stmt.setString(4, project.getProjectWeekday());
            stmt.setInt(5, project.getHourlyRate());
            stmt.setInt(6, project.getRegisteredSlot());
            stmt.setInt(7, project.getTotalSlot());
            stmt.setBoolean(8, project.isEnabled());
            stmt.executeUpdate();
        }
    }

    //admin function
    @Override
    public void updateProjectStatus(String projectId, boolean enabled) throws SQLException {
        String sql = "UPDATE available_projects SET enabled = ? WHERE project_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, enabled);
            stmt.setString(2, projectId);
            stmt.executeUpdate();
        }
    }
    @Override
    public boolean isDuplicateProject(String title, String location, String day) throws SQLException {
        String sql = "SELECT COUNT(*) FROM available_projects " +
                "WHERE LOWER(project_name) = ? AND LOWER(project_location) = ? AND LOWER(project_weekday) = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title.toLowerCase());
            stmt.setString(2, location.toLowerCase());
            stmt.setString(3, day.toLowerCase());

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    @Override
    public int insertNewProject(String title, String location, String day, int hourlyRate, int totalSlot) throws SQLException {
        String sql = "INSERT INTO available_projects " +
                "(project_name, project_location, project_weekday, hourly_rate, available_slots, total_slot, enabled) " +
                "VALUES (?, ?, ?, ?, ?, ?, 1)";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, title);
            stmt.setString(2, location);
            stmt.setString(3, day);
            stmt.setInt(4, hourlyRate);
            stmt.setInt(5, 0);
            stmt.setInt(6, totalSlot);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
            return -1;
        }
    }

    @Override
    public boolean updateProjectDetails(int projectId, String title, String location, String day, int hourlyRate, int totalSlot) throws SQLException {
        // Check for duplicate project (excluding the current one)
        String duplicateCheckSql = "SELECT COUNT(*) FROM available_projects " +
                "WHERE LOWER(project_name) = ? AND LOWER(project_location) = ? AND LOWER(project_weekday) = ? AND project_id != ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(duplicateCheckSql)) {
            checkStmt.setString(1, title.toLowerCase());
            checkStmt.setString(2, location.toLowerCase());
            checkStmt.setString(3, day.toLowerCase());
            checkStmt.setInt(4, projectId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false; // Duplicate found
            }
        }

        // Proceed with update
        String updateSql = "UPDATE available_projects SET " +
                "project_name = ?, project_location = ?, project_weekday = ?, hourly_rate = ?, total_slot = ? " +
                "WHERE project_id = ?";
        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(updateSql)) {
            stmt.setString(1, title);
            stmt.setString(2, location);
            stmt.setString(3, day);
            stmt.setInt(4, hourlyRate);
            stmt.setInt(5, totalSlot);
            stmt.setInt(6, projectId);
            stmt.executeUpdate();
            return true;
        }
    }

}

