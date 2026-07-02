package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.InitData;
import model.Model;

import java.io.IOException;

public class AdminProfileController {
    @FXML
    private Button adminHomePageButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button adminEditPasswordButton;
    @FXML
    private Label adminUsernameLabel;
    @FXML
    private Label adminFirstNameLabel;
    @FXML
    private Label adminLastNameLabel;
    @FXML
    private Label adminEmailLabel;
    @FXML
    private Label adminLabel;

    private Model model;

    private Scene currentScene;
    private Scene nextScene;
    private Stage stage;
    private InitData initData;

    public AdminProfileController(Stage stage, Model model, InitData initData) {
        this.stage = stage;
        this.model = model;
        this.initData = initData;
    }

    //button to go back to home page
    @FXML
    public void backToAdminHomePage(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
            loader.setController(new UserController(stage, model, initData)); // pass instance of HomeController
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Admin Profile");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //buttton to edit password
    @FXML
    public void editAdminPasswordHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/UpdatePasswordView.fxml"));
            UpdatePasswordController updatePasswordController = new UpdatePasswordController(stage, model, initData);

            loader.setController(updatePasswordController);
            Parent root = loader.load();

            // Get current stage from the button that was clicked
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Replace the scene
            Scene scene = new Scene(root, 500, 300);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Change Password");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void logoutHandler(ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LoginView.fxml"));
            loader.setController(new LoginController(stage, model)); // pass instance of HomeController
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Welcome");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void editAdminProfileDetailHandler (ActionEvent event) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/EditProfileDetailView.fxml"));
            EditProfileDetailController editProfileDetailController = new EditProfileDetailController(stage, model, initData);

            loader.setController(editProfileDetailController);
            Parent root = loader.load();

            // Get current stage from the button that was clicked
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Replace the scene
            Scene scene = new Scene(root, 500, 300);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Edit Profile");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
