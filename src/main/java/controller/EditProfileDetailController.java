package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.stage.Stage;
import viewSwitch.SwitchPathWay;
import model.InitData;
import model.Model;

import java.io.IOException;
import java.sql.SQLException;

public class EditProfileDetailController {
    private Model model;

    private Scene currentScene;
    private Scene nextScene;
    private Stage stage;
    private InitData  initData;

    public EditProfileDetailController(Stage stage, Model model, InitData initData) {
        this.stage = stage;
        this.model = model;
        this.initData = initData;
    }

    @FXML
    public void isAdminHandler(ActionEvent event) throws SQLException {

    }

    @FXML
    public void closeEditProfileHandler (ActionEvent event) throws IOException {
        UserProfileController controller = new UserProfileController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/UserProfileView.fxml", "User Profile", event, controller);
    }
}
