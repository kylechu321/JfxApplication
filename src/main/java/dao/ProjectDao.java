package dao;

import model.AvailableProject;

import java.sql.SQLException;
import java.util.List;

public interface ProjectDao {
    void setup() throws SQLException;
    void updateProjectSlots(List<AvailableProject> projects) throws SQLException;
    List<AvailableProject> loadAvailableProjects() throws SQLException;
    void insertOrUpdateProject(AvailableProject project) throws SQLException;
    void updateProjectStatus(String projectId, boolean enabled) throws SQLException;
    boolean isDuplicateProject(String title, String location, String day) throws SQLException;
    int insertNewProject(String title, String location, String day, int hourlyRate, int totalSlot) throws SQLException;
    boolean updateProjectDetails(int projectId, String title, String location, String day, int hourlyRate, int totalSlot) throws SQLException;
}

