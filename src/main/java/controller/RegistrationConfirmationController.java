package controller;

import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import model.*;
import javafx.scene.control.Label;
import viewSwitch.SwitchPathWay;

import java.io.IOException;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RegistrationConfirmationController {
    private Stage stage;
    private Model model;
    private InitData initData;
    private List<CartItem> cartItems;

    @FXML
    private ListView<String> summaryListView;
    @FXML
    private TextField confirmationCodeField;
    @FXML
    private Label resultLabel;
    @FXML
    private Button returnHomeButton;
    @FXML
    private Button backToCartSelection;

    private boolean confirmationSuccessful = false;

    public RegistrationConfirmationController(Stage stage, Model model, InitData initData) {
        this.stage = stage;
        this.model = model;
        this.initData = initData;
        this.cartItems = initData.getCartItems();
    }

    @FXML
    public void initialize() {
        ObservableList<String> summary = FXCollections.observableArrayList();
        if (cartItems != null) {
            for (CartItem item : cartItems) {
                summary.add(item.toString());
            }
        } else {
            System.out.println("cartItem is null");
        }
        summaryListView.setItems(summary);
        returnHomeButton.setDisable(true);

    }

    @FXML
    public void submitConfirmationHandler(ActionEvent event) {
        String code = confirmationCodeField.getText().trim();
        if (!isValidConfirmationCode(code)) {
            resultLabel.setText("Invalid code. Must be 6 digits.");
            return;
        }

        DayOfWeek today = LocalDate.now().getDayOfWeek();
        // Monday = 1, Sunday = 7
        int todayValue = today.getValue();

        double total = 0;

        for (CartItem item : cartItems) {
            String whichWeekday = item.getAvailableProject().getProjectWeekday().toLowerCase();
            DayOfWeek projectDay = weekdayMap.get(whichWeekday);

            if (projectDay == null) {
                resultLabel.setText("Invalid weekday: " + whichWeekday);
                return;
            }

            if (projectDay.getValue() < todayValue) {
                resultLabel.setStyle("-fx-text-fill: red;");
                resultLabel.setText("Cannot register for " + whichWeekday + " projects today.");
                return;
            }

            int rate = item.getAvailableProject().getHourlyRate();
            int hours = item.getHoursPerSlot();
            int slots = item.getNumberOfSlots();
            total += rate * hours * slots;


            // Generate and store participation record
            String registrationId = UUID.randomUUID().toString();
            UserParticipationHistory record = new UserParticipationHistory(
                    registrationId,
                    LocalDateTime.now(),
                    item.getAvailableProject().getProjectId(),
                    item.getAvailableProject().getProjectName(),
                    item.getAvailableProject().getProjectLocation(),
                    item.getAvailableProject().getProjectWeekday(),
                    slots,
                    hours,
                    rate,
                    rate * hours * slots
            );
            initData.addUserParticipationHistory(record);

            if (!initData.getUserParticipationHistoryList().contains(record)) {
                resultLabel.setStyle("-fx-text-fill: red;");
                resultLabel.setText("Failed to save participation record for project: " + item.getAvailableProject().getProjectName());
                return;
            }
        }

        //Deduct slots from projects based on cart
        initData.applySlotDeductionFromCart();

        try {
            model.getProjectDao().updateProjectSlots(initData.getAvailableProjects());
        } catch (SQLException e) {
            resultLabel.setStyle("-fx-text-fill: red;");
            resultLabel.setText("Failed to update slot availability in database.");
            return;
        }

        confirmationSuccessful = true;
        returnHomeButton.setDisable(false);
        resultLabel.setText("Registration successful! Total contribution: $" + total);
    }

    public void returnHomeHandler(ActionEvent event) throws IOException {
        if (!confirmationSuccessful) {
            resultLabel.setText("Please confirm the registration");
            return;
        }

        try {
            UserController controller = new UserController(stage, model, initData);
            SwitchPathWay.buttonLoadAndSetNewRoot("/view/HomeView.fxml", "Home Page", event, controller);
        } catch (IOException e) {
            e.printStackTrace();
            resultLabel.setStyle("-fx-text-fill: red;");
            resultLabel.setText("Error returning to Home view.");
        }
    }

    public void backToCartHandler (ActionEvent event) throws IOException {
        RegisterProjectController controller = new RegisterProjectController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/ProjectRegistrationView.fxml", "Project Registration", event, controller);
    }


    private static final Map<String, DayOfWeek> weekdayMap = Map.of(
            "mon", DayOfWeek.MONDAY,
            "tue", DayOfWeek.TUESDAY,
            "wed", DayOfWeek.WEDNESDAY,
            "thu", DayOfWeek.THURSDAY,
            "fri", DayOfWeek.FRIDAY,
            "sat", DayOfWeek.SATURDAY,
            "sun", DayOfWeek.SUNDAY
    );

    public static boolean isValidConfirmationCode(String code) {
        return code != null && code.trim().matches("\\d{6}");
    }
}
