package controller;

import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import model.AvailableProject;
import model.CartItem;
import model.InitData;
import model.Model;
import javafx.scene.control.Label;
import viewSwitch.SwitchPathWay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RegisterProjectController {
    private Model model;
    private Stage stage;
    private Stage parentStage;
    private InitData initData;

    @FXML
    private ComboBox<AvailableProject> projectComboBox;
    @FXML
    private Spinner<Integer> hoursSpinner;
    @FXML
    private Spinner<Integer> slotsSpinner;
    @FXML
    private Button addToCartButton;
    @FXML
    private Button confirmButton;
    @FXML
    private Button updateCartButton;
    @FXML
    private Button removeCartButton;
    @FXML
    private ListView<String> cartListView;
    @FXML
    private Label errorMessageLabel;

    private ObservableList<CartItem> cartItems = FXCollections.observableArrayList();

    public RegisterProjectController(Stage parentStage, Model model, InitData initData)  {
        this.stage = new Stage();
        this.parentStage = parentStage;
        this.model = model;
        this.initData = initData;
    }

    @FXML
    public void initialize() {
        hoursSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3, 1));
        slotsSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 3, 1));
        projectComboBox.getItems().addAll(initData.getEnabledProjects());
        cartListView.setItems(FXCollections.observableArrayList());

        List<CartItem> existingCart = initData.getCartItems();
        if (existingCart != null && !existingCart.isEmpty()) {
            cartItems.setAll(existingCart);
            ObservableList<String> cartDisplay = FXCollections.observableArrayList();
            for (CartItem item : existingCart) {
                cartDisplay.add(item.toString());
            }
            cartListView.setItems(cartDisplay);
        } else {
            cartListView.setItems(FXCollections.observableArrayList());
        }
    }

    @FXML
    public void addToCartHandler(ActionEvent event) throws IOException {
        AvailableProject selectedProject = projectComboBox.getValue();
        int hours = hoursSpinner.getValue();
        int slots = slotsSpinner.getValue();

        if (selectedProject == null) {
            errorMessageLabel.setText("Please select a project");
            return;
        }

        if (selectedProject.getRegisteredSlot() <= 0) {
            errorMessageLabel.setText("No available slots for this project.");
            return;
        }

        if (slots > selectedProject.getRegisteredSlot()) {
            errorMessageLabel.setText("Requested slots exceed availability.");
            return;
        }

        for (CartItem item : cartItems) {
            if (String.valueOf(item.getAvailableProject().getProjectId()).equals(selectedProject.getProjectId())) {
                errorMessageLabel.setText("Project already in carts. ");
                return;
            }

        }

        CartItem newItem = new CartItem(selectedProject, hours, slots);
        cartItems.add(newItem);
        cartListView.getItems().add(newItem.toString());
        errorMessageLabel.setText("");
    }

    @FXML
    public void removeCartSelectedHandler(ActionEvent event) throws IOException{
        int selectedIndex = cartListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            cartItems.remove(selectedIndex);
            cartListView.getItems().remove(selectedIndex);
            errorMessageLabel.setText("Item removed.");
        } else {
            errorMessageLabel.setText("Select an item to remove.");
        }
    }

    @FXML
    public void updateCartSelectedHandler(ActionEvent event) {
        int selectedIndex = cartListView.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            CartItem selectedItem = cartItems.get(selectedIndex);
            int newHours = hoursSpinner.getValue();
            int newSlots = slotsSpinner.getValue();

            if (newHours > 3 || newSlots > 3) {
                errorMessageLabel.setText("Max 3 hours and 3 slots allowed.");
                return;
            }

            CartItem updatedItem = new CartItem(selectedItem.getAvailableProject(), newHours, newSlots);
            cartItems.set(selectedIndex, updatedItem);
            cartListView.getItems().set(selectedIndex, updatedItem.toString());
            errorMessageLabel.setText("Item updated.");
        } else {
            errorMessageLabel.setText("Select an item to update.");
        }
    }



    @FXML
    public void confirmRegistrationHandler(ActionEvent event) throws IOException {
        if (cartItems.isEmpty()) {
            errorMessageLabel.setText("Cart is empty.");
            return;
        }

        initData.setCartItems(new ArrayList<>(cartItems));

        RegistrationConfirmationController controller = new RegistrationConfirmationController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/RegistrationConfirmationView.fxml", "Confirm Registration", event, controller);
    }
}
