package viewSwitch;

import controller.UserProfileController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

import java.io.IOException;

public class SwitchPathWay {
    @FXML
    public static void loadAndSetNewRoot(String fxmlPath, ActionEvent event, Object controller) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(SwitchPathWay.class.getResource(fxmlPath));
            loader.setController(controller); // inject your controller
            Parent newRoot = loader.load();

            if (controller instanceof UserProfileController profileController) {
                profileController.refreshUserDetails();
            }

            // Get the current stage from the MenuItem's parent scene and window
            Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();

            // Get the current scene
            Scene currentScene = stage.getScene();

            // Set the new root to the current scene
            currentScene.setRoot(newRoot);
            stage.setResizable(false);
            stage.setTitle("Profile");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @FXML
    public static void buttonLoadAndSetNewRoot(String fxmlPath, String title, ActionEvent event, Object controller) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(SwitchPathWay.class.getResource(fxmlPath));
            loader.setController(controller);
            Parent root = loader.load();

            // Call refreshUserDetails if applicable
            if (controller instanceof UserProfileController profileController) {
                profileController.refreshUserDetails();
            }

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);

            stage.setTitle(title);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }
}
