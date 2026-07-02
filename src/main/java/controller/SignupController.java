package controller;

import java.sql.SQLException;

import dao.UserDaoImpl;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Model;
import model.User;

public class SignupController {
    @FXML
    private TextField username;
    @FXML
    private TextField password;
    @FXML
    private TextField firstName;
    @FXML
    private TextField lastName;
    @FXML
    private TextField email;
    @FXML
    private Button createUser;
    @FXML
    private Button close;
    @FXML
    private Label status;
    @FXML
    private CheckBox admin;


    private Stage stage;
    private Stage parentStage;
    private Model model;

    public SignupController(Stage parentStage, Model model) {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
    }

    @FXML
    public void initialize() {
        createUser.setOnAction(event -> {
            if (!username.getText().isEmpty() && !password.getText().isEmpty() && !firstName.getText().isEmpty() && !lastName.getText().isEmpty() && !email.getText().isEmpty()) {
                User user;

                String uname = username.getText();
                String pwd = password.getText();
                String fname = firstName.getText();
                String lname = lastName.getText();
                String mail = email.getText();
                boolean adminSelected = admin.isSelected();


                try {
                    if (!model.getUserDao().isUsernameUnique(uname)) {
                        status.setText("Username already exists");
                        status.setTextFill(Color.RED);
                        return;
                    }

                    if (!isValidEmail(mail)) {
                        status.setText("Invalid email format");
                        status.setTextFill(Color.RED);
                        return;
                    }

                    if (!UserDaoImpl.isValidPassword(pwd)) {
                        status.setText("password must be 8 characters long and need to include Uppercase, numbers and special character");
                        status.setTextFill(Color.RED);
                        return;
                    }

                    if (!isValidUsername(uname)) {
                        status.setText("Invalid username name, must be atleast 4 character long and only Contains only letters, digits, underscores, or dots");
                        status.setTextFill(Color.RED);
                        return;
                    }

                    if (!isValidFirstAndSecondName(fname, lname)) {
                        status.setText("First and last names must contain only letters");
                        status.setTextFill(Color.RED);
                        return;
                    }

                    String hashPassword = UserDaoImpl.hashPasswordWithSha(pwd);

                    user = model.getUserDao().createUser(uname, hashPassword, adminSelected, fname, lname, mail);
                    if (user != null) {
                        status.setText("Created " + user.getUsername());
                        status.setTextFill(Color.GREEN);
                    } else {
                        status.setText("Cannot create user");
                        status.setTextFill(Color.RED);
                    }


                } catch (SQLException e) {
                    status.setText(e.getMessage());
                    status.setTextFill(Color.RED);
                }

            } else {
                status.setText("Empty username or password");
                status.setTextFill(Color.RED);
            }
        });

        close.setOnAction(event -> {
            stage.close();
            parentStage.show();
        });
    }


    public void showStage(Pane root) {
        Scene scene = new Scene(root, 600, 500);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Sign up");
        stage.show();
    }

    public boolean isValidEmail (String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";

        return email.matches(regex);
    }


    public boolean isValidUsername(String username) {
        String regex = "^[A-Za-z][A-Za-z0-9._]{3,}$";
        return username != null && username.matches(regex);
    }

    public boolean isValidFirstAndSecondName(String firstName, String secondName) {
        String nameRegex = "^[A-Za-z]+([\\-']?[A-Za-z]+)*$";
        return firstName != null && secondName != null &&
                firstName.matches(nameRegex) && secondName.matches(nameRegex);
    }

}
