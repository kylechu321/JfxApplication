package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.AvailableProject;
import viewSwitch.SwitchPathWay;

import java.io.IOException;
import java.util.List;

import javafx.stage.Stage;
import model.InitData;
import model.Model;

public class AdminProjectGroupController {
    private Model model;
    private Stage stage;
    private Stage parentStage;
    private InitData initData;

    @FXML
    private Label errorMessage;
    @FXML
    private Button toggleStatusButton;
    @FXML
    private Button adminBackToHomrButton;
    @FXML
    private Button modifyProjectButton;
    @FXML
    private TableView<AvailableProject> projectGroupTable;
    @FXML
    private TableColumn<AvailableProject, String> projectNameColumn;
    @FXML
    private TableColumn<AvailableProject, String> locationColumn;
    @FXML
    private TableColumn<AvailableProject, String> weekdayColumn;
    @FXML
    private TableColumn<AvailableProject, Integer> hourlyRateColumn;
    @FXML
    private TableColumn<AvailableProject, Integer> registeredSlotColumn;
    @FXML
    private TableColumn<AvailableProject, Integer> totalSlotColumn;
    @FXML
    private TableColumn<AvailableProject, Boolean> enabledColumn;


    public AdminProjectGroupController(Stage parentStage, Model model, InitData initData) {
        this.stage = stage;
        this.parentStage = parentStage;
        this.model = model;
        this.initData = initData;
    }

    @FXML
    public void initialize() {
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("projectLocation"));
        weekdayColumn.setCellValueFactory(new PropertyValueFactory<>("projectWeekday"));
        hourlyRateColumn.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));
        registeredSlotColumn.setCellValueFactory(new PropertyValueFactory<>("registeredSlot"));
        totalSlotColumn.setCellValueFactory(new PropertyValueFactory<>("totalSlot"));
        enabledColumn.setCellValueFactory(new PropertyValueFactory<>("enabled"));

        projectGroupTable.setItems(FXCollections.observableArrayList(initData.getRelatedProjects()));

    }

    @FXML
    public void toggleStatusHandler(ActionEvent event) {
        AvailableProject selected = projectGroupTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            initData.toggleProjectStatus(selected.getProjectId());

            List<AvailableProject> refreshedGroup = initData.getGroupedProjects().get(selected.getProjectName());
            initData.setRelatedProjects(refreshedGroup);

            projectGroupTable.setItems(FXCollections.observableArrayList(refreshedGroup));
            projectGroupTable.refresh();

        } else {
            System.out.println("selected is null");
        }
    }

    public void modifyProjectHandler(ActionEvent event) throws IOException {
        AvailableProject selected = projectGroupTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            errorMessage.setText("Please select a project to modify.");
            return;
        }

        // Pass selected project to the edit screen
        AdminAddProjectController controller = new AdminAddProjectController(stage, model, initData);
        controller.setSelectedProject(selected);

        SwitchPathWay.buttonLoadAndSetNewRoot("/view/AdminAddProjectView.fxml", "Modify Project", event, controller);
    }


    @FXML
    public void backToAdminHandler(ActionEvent event) throws IOException {
        AdminController adminController = new AdminController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/AdminView.fxml", "Admin profile" ,event, adminController);
    }

}
