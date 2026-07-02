package controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import viewSwitch.SwitchPathWay;
import model.AvailableProject;
import model.InitData;
import model.Model;
import model.User;

import java.io.IOException;
import java.util.List;

public class UserController {
    private Model model;
    private Stage stage;
    private Stage parentStage;
    private InitData initData;

    @FXML
    private Menu profile;
    @FXML
    private MenuItem viewProfile;
    @FXML
    private MenuItem updateProfile;
    @FXML
    private Button viewAvaiableProject;
    @FXML
    private Button viewParticipartionHistory;
    @FXML
    private Label welcomeMessage;
    @FXML
    private TableView<AvailableProject> availableProjectsTableView;
    @FXML
    private TableColumn<AvailableProject, String> projectNameColumn;
    @FXML
    private TableColumn<AvailableProject, String> projectLocationColumn;
    @FXML
    private TableColumn<AvailableProject, String> projectWeekdayColumn;
    @FXML
    private TableColumn<AvailableProject, Integer> hourlyRateColumn;
    @FXML
    private TableColumn<AvailableProject, Integer> registeredSlotColumn;
    @FXML
    private TableColumn<AvailableProject, Integer> totalSlotColumn;

    public UserController(Stage parentStage, Model model, InitData initData) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
        this.initData = initData;
    }

    @FXML
    private void initialize() {
        User user = this.model.getCurrentUser();
        welcomeMessage.setText("Welcome user: " + user.getUsername());

        try {
            List<AvailableProject> visibleProjects = initData.getEnabledProjects();

            projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
            projectLocationColumn.setCellValueFactory(new PropertyValueFactory<>("projectLocation"));
            projectWeekdayColumn.setCellValueFactory(new PropertyValueFactory<>("projectWeekday"));
            hourlyRateColumn.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));
            registeredSlotColumn.setCellValueFactory(new PropertyValueFactory<>("registeredSlot"));
            totalSlotColumn.setCellValueFactory(new PropertyValueFactory<>("totalSlot"));


            availableProjectsTableView.setItems(FXCollections.observableArrayList(visibleProjects));

        } catch (Exception e) {
            welcomeMessage.setText("no user found. ");
            System.out.println(e.getMessage()   + "no data found");
        }

    }

    @FXML
    public void registerProjectHandler(ActionEvent event) throws IOException {
        RegisterProjectController registerProjectController = new RegisterProjectController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/ProjectRegistrationView.fxml", "Register password", event, registerProjectController);
    }


    @FXML
    public void viewParticipartionHistoryHandler(ActionEvent event) throws IOException{
        ParticipationHistoryController controller = new ParticipationHistoryController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/ParticipationHistoryView.fxml", "Participation history", event, controller);
    }

    //handler to get to profile
    @FXML
    public void viewProfileHandler(ActionEvent event) throws IOException {
        UserProfileController controller = new UserProfileController(stage, model, initData);
        SwitchPathWay.loadAndSetNewRoot("/view/UserProfileView.fxml", event, controller);
    }

    @FXML
    public void toUpdateProfileHandler(ActionEvent event) throws IOException {
        EditProfileDetailController controller = new EditProfileDetailController(stage, model, initData);
        SwitchPathWay.loadAndSetNewRoot("/view/EditProfileDetailView.fxml", event, controller);
    }


    @FXML
    public void showStage(Pane root) {
        Scene scene = new Scene(root, 500, 350);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Home");
        stage.show();
    }

}
