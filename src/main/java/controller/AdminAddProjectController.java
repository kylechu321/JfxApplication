package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.AvailableProject;
import viewSwitch.SwitchPathWay;
import model.InitData;
import model.Model;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminAddProjectController {
    @FXML
    private TextField titleField;
    @FXML
    private TextField locationField;
    @FXML
    private Button addProjectButton;
    @FXML
    private Button backToHomeButton;
    @FXML
    private ComboBox<String> dayComboBox;
    @FXML
    private TextField hourlyRateField;
    @FXML
    private TextField totalSlotField;
    @FXML
    private Label errorMessage;

    private Model model;
    private Stage stage;
    private InitData initData;
    private AvailableProject selectedProject;

    public AdminAddProjectController(Stage stage, Model model, InitData initData) {
        this.stage = stage;
        this.model = model;
        this.initData = initData;
    }

    private void populateFieldsForEdit() {
        titleField.setText(selectedProject.getProjectName());
        locationField.setText(selectedProject.getProjectLocation());
        dayComboBox.setValue(selectedProject.getProjectWeekday());
        hourlyRateField.setText(String.valueOf(selectedProject.getHourlyRate()));
        totalSlotField.setText(String.valueOf(selectedProject.getTotalSlot()));
        addProjectButton.setText("Update Project");
    }

    public void setSelectedProject(AvailableProject project) {
        this.selectedProject = project;
    }

    @FXML
    public void addProjectHandler(ActionEvent event) {
        String title = titleField.getText().trim();
        String location = locationField.getText().trim();
        String day = dayComboBox.getValue();
        String hourlyRateText = hourlyRateField.getText().trim();
        String totalSlotText = totalSlotField.getText().trim();

        // Validation
        if (title.isEmpty() || title.length() > 30) {
            errorMessage.setText("Title must be 1–30 characters.");
            return;
        }
        if (location.isEmpty() || location.length() > 30) {
            errorMessage.setText("Location must be 1–30 characters.");
            return;
        }
        if (day == null || !List.of("Mon","Tue","Wed","Thu","Fri","Sat","Sun").contains(day)) {
            errorMessage.setText("Day must be a valid weekday.");
            return;
        }

        int hourlyRate;
        int totalSlot;
        try {
            hourlyRate = Integer.parseInt(hourlyRateText);
            totalSlot = Integer.parseInt(totalSlotText);
            if (hourlyRate < 1 || hourlyRate > 100 || totalSlot < 1 || totalSlot > 100) {
                errorMessage.setText("Hourly rate and total slots must be between 1 and 100.");
                return;
            }
        } catch (NumberFormatException e) {
            errorMessage.setText("Hourly rate and total slots must be numbers.");
            return;
        }

        // Duplicate check and insert
        try {
            if (selectedProject == null) {
                if (model.getProjectDao().isDuplicateProject(title, location, day)) {
                    errorMessage.setText("Duplicate project exists.");
                    return;
                }

                int generatedId = model.getProjectDao().insertNewProject(title, location, day, hourlyRate, totalSlot);
                if (generatedId == -1) {
                    errorMessage.setText("Failed to generate project ID.");
                    return;
                }
                errorMessage.setTextFill(Color.GREEN);
                errorMessage.setText("Project added successfully.");
                System.out.println(generatedId);
            } else {
                // MODIFY MODE
                boolean success = model.getProjectDao().updateProjectDetails(
                        selectedProject.getProjectId(), title, location, day, hourlyRate, totalSlot
                );

                if (!success) {
                    errorMessage.setText("Duplicate project exists.");
                    return;
                }

                errorMessage.setTextFill(Color.GREEN);
                errorMessage.setText("Project updated successfully.");
            }

            // Refresh local data
            List<AvailableProject> updatedProjects = model.getProjectDao().loadAvailableProjects();
            initData.setAvailableProjects(new ArrayList<>(updatedProjects));
            initData.groupProjectsByName();

        } catch (SQLException e) {
            errorMessage.setText("Database error: " + e.getMessage());
        }
    }

    @FXML
    public void backToHomeHandler(ActionEvent event) throws IOException {
        AdminController controller = new AdminController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/AdminView.fxml", "admin home", event, controller);
    }
}
