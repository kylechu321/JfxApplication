package controller;

import dao.ProjectDao;
import dao.ProjectSelectionDao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.UserParticipationHistory;
import viewSwitch.SwitchPathWay;
import model.InitData;
import model.Model;
import model.User;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class UserProfileController {
    @FXML
    private Button homePageButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button editPasswordButton;
    @FXML
    private Label usernameLabel;
    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label emailLabel;
    @FXML
    private Label adminLabel;

    private Model model;
    private Stage stage;
    private InitData initData;

    public UserProfileController(Stage stage, Model model, InitData initData) {
        this.stage = stage;
        this.model = model;
        this.initData = initData;
    }

    public void refreshUserDetails() {
        User user = model.getCurrentUser();
        if (user != null) {
            usernameLabel.setText("Username: " + user.getUsername());
            firstNameLabel.setText("Firstname: " + user.getFirstname());
            lastNameLabel.setText("Lastname: " + user.getLastname());
            emailLabel.setText("Email: " +user.getEmail());
            adminLabel.setText("Is it an Admin user? : " + user.getAdmin());
        }
    }


    //button to go back to home page
    @FXML
    public void backToHomePage(ActionEvent event) throws IOException {
        UserController userController = new UserController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/HomeView.fxml", "Home", event, userController);
    }

    //buttton to edit password
    @FXML
    public void editPasswordHandler(ActionEvent event) throws IOException {
        UpdatePasswordController updatePasswordController = new UpdatePasswordController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/UpdatePasswordView.fxml", "Change Password", event, updatePasswordController);
    }


    @FXML
    public void logoutHandler(ActionEvent event) throws IOException {
        try {
            //save participation history
            ProjectSelectionDao projectSelectionDao = model.getProjectSelectionDao();

            //check duplicate id
            List<UserParticipationHistory> unsavedRecords = initData.getUserParticipationHistoryList().stream()
                    .filter(record -> !initData.isAlreadySaved(record.getRegistrationId()))
                    .collect(Collectors.toList());

            if (!unsavedRecords.isEmpty()) {
                projectSelectionDao.saveParticipationHistory(unsavedRecords, model.getCurrentUser());

                // Mark records as saved
                unsavedRecords.forEach(record -> initData.markAsSaved(record.getRegistrationId()));
            }

            //save project slot availability
            ProjectDao projectDao = model.getProjectDao();
            projectDao.updateProjectSlots(initData.getAvailableProjects());

            model.setCurrentUser(null);

        } catch (Exception e) {
            System.out.println("error in saving participation history" + e.getMessage());
            return;
        }

        LoginController loginController = new LoginController(stage, model);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/LoginView.fxml", "Welcome", event, loginController);

    }

    @FXML
    public void editProfileDetailHandler (ActionEvent event) throws IOException {
        EditProfileDetailController editProfileDetailController = new EditProfileDetailController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/EditProfileDetailView.fxml", "Edit Profile", event, editProfileDetailController);

    }
}
