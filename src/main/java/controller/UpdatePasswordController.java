package controller;

import dao.UserDaoImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.InitData;
import model.Model;
import viewSwitch.SwitchPathWay;

import java.io.IOException;
import java.sql.SQLException;

public class UpdatePasswordController {
    @FXML
    private PasswordField newPassword;
    @FXML
    private Label message;
    @FXML
    private Button confirmNewPasswordButton;
    @FXML
    private Button backToProfileButton;
    @FXML
    private PasswordField confirmNewPassword;

    private Model model;
    private Stage stage;
    private InitData initData;

    public UpdatePasswordController(Stage stage, Model model, InitData initData) {
        this.model = model;
        this.stage = stage;
        this.initData = initData;
    }

    @FXML
    public void initialize() {
        confirmNewPasswordButton.setOnAction(event -> {
            String newPwd = newPassword.getText();
            String confirmPwd = confirmNewPassword.getText();

            // Check if fields are empty
            if (newPwd.isEmpty() || confirmPwd.isEmpty()) {
                message.setText("Please fill in both password fields.");
                message.setTextFill(Color.RED);
                return;
            }

            // Check if passwords match
            if (!newPwd.equals(confirmPwd)) {
                message.setText("Passwords do not match.");
                message.setTextFill(Color.RED);
                return;
            }

            // Validate password format
            if (!UserDaoImpl.isValidPassword(newPwd)) {
                message.setText("Password must be at least 8 characters, " +
                                "include uppercase, number, and special character.");
                message.setTextFill(Color.RED);
                return;
            }


            // Hash the password
            String hashedPassword = UserDaoImpl.hashPasswordWithSha(newPwd);
            String username = initData.getUser().getUsername();

            //update Db file
            try {
                boolean isUpdated = model.getUserDao().updatePassword(username, hashedPassword);
                if (isUpdated) {
                    System.out.println("Updating password for user: " + username);
                    message.setText("Password updated successfully.");
                    message.setTextFill(Color.GREEN);
                } else {
                    message.setText("Failed to update password.");
                    message.setTextFill(Color.RED);
                }
            } catch (SQLException e) {
                message.setText("Database error: " + e.getMessage());
                message.setTextFill(Color.RED);
            }
        });
    }


    @FXML
    public void backToProfileHandler(ActionEvent event) throws IOException {
        UserController userController = new UserController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/HomeView.fxml", "Home", event, userController);
    }
}
