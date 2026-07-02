package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.AvailableProject;
import model.User;
import viewSwitch.SwitchPathWay;
import model.InitData;
import model.Model;

import java.io.IOException;
import java.util.List;

public class AdminController {
    private Model model;
    private Stage stage;
    private Stage parentStage;
    private InitData initData;

    @FXML
    private Menu adminProfile;
    @FXML
    private MenuItem viewAdminProfile;
    @FXML
    private MenuItem updateAdminProfile;
    @FXML
    private Button viewDetailsButton;
    @FXML
    private Button addProjectButton;
    @FXML
    private Button viewAllRegistrationButton;
    @FXML
    private Label adminWelcomeMessage;
    @FXML
    private TableView<AvailableProject> adminAvailableProjectsTableView;
    @FXML
    private TableColumn<AvailableProject, String> adminProjectNameColumn;
    @FXML
    private TableColumn<AvailableProject, String> adminProjectLocationColumn;
    @FXML
    private TableColumn<AvailableProject, String> adminProjectWeekdayColumn;
    @FXML
    private TableColumn<AvailableProject, Integer> adminHourlyRateColumn;
    @FXML
    private TableColumn<AvailableProject, Integer> adminRegisteredSlotColumn;
    @FXML
    private TableColumn<AvailableProject, Integer> adminTotalSlotColumn;

    public AdminController(Stage parentStage, Model model, InitData initData) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
        this.initData = initData;

        this.initData.setModel(model);
    }

    @FXML
    public void initialize() {
        User user = this.model.getCurrentUser();
        adminWelcomeMessage.setText("Welcome Admin User: " + user.getUsername());

        try {
            adminProjectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
            adminProjectLocationColumn.setCellValueFactory(new PropertyValueFactory<>("projectLocation"));
            adminProjectWeekdayColumn.setCellValueFactory(new PropertyValueFactory<>("projectWeekday"));
            adminHourlyRateColumn.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));
            adminRegisteredSlotColumn.setCellValueFactory(new PropertyValueFactory<>("registeredSlot"));
            adminTotalSlotColumn.setCellValueFactory(new PropertyValueFactory<>("totalSlot"));

            initData.groupProjectsByName();
            List<AvailableProject> groupedList = initData.getGroupedProjectRepresentatives();
            adminAvailableProjectsTableView.setItems(FXCollections.observableArrayList(groupedList));

        } catch (Exception e) {
            System.out.println(e.getMessage()   + "no data found");
        }
    }

    @FXML
    public void viewDetailsHandler(ActionEvent event) throws IOException {
        AvailableProject selected = adminAvailableProjectsTableView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            String projectName = selected.getProjectName();

            List<AvailableProject> relatedProjects = initData.getGroupedProjects().get(projectName);

            initData.setRelatedProjects(relatedProjects);

            AdminProjectGroupController controller = new AdminProjectGroupController(stage, model, initData);
            SwitchPathWay.buttonLoadAndSetNewRoot("/view/AdminProjectGroupView.fxml", "view project details" ,event, controller);
        }
    }


    //handler to get to profile
    @FXML
    public void viewProfileHandler(ActionEvent event) throws IOException {
        AdminProfileController controller = new AdminProfileController(stage, model, initData);
        SwitchPathWay.loadAndSetNewRoot("/view/AdminProfileView.fxml", event, controller);
    }

    @FXML
    public void adminLogOutHandler (ActionEvent event) throws IOException {
        LoginController controller = new LoginController(stage, model);
        SwitchPathWay.loadAndSetNewRoot("/view/LoginView.fxml", event, controller);
    }

    @FXML
    public void addProjectHandler(ActionEvent event) throws IOException {
        AdminAddProjectController controller = new AdminAddProjectController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/AdminAddProjectView.fxml", "Add Project", event, controller);
    }


    @FXML
    public void viewAllRegistrationHandler (ActionEvent event) throws IOException {
        AdminViewAllUserParticipationController controller = new AdminViewAllUserParticipationController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/AdminViewAllUserParticipationView.fxml", "All User Participation", event, controller);
    }


    public void showStage(Pane root) {
        Scene scene = new Scene(root, 500, 300);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Admin Home");
        stage.show();
    }
}
