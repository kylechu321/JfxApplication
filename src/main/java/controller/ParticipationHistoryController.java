package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;
import model.CartItem;
import model.InitData;
import model.Model;
import fileHandler.FileHandler;
import model.UserParticipationHistory;
import viewSwitch.SwitchPathWay;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class ParticipationHistoryController {
    private Stage stage;
    private Model model;
    private InitData initData;
    private List<CartItem> cartItems;

    @FXML
    private TableView<UserParticipationHistory> tableHistory;
    @FXML
    private TableColumn<UserParticipationHistory, String> participationIdColumn;
    @FXML
    private TableColumn<UserParticipationHistory, String> userDateColumn;
    @FXML
    private TableColumn<UserParticipationHistory, String> userNameColumn;
    @FXML
    private TableColumn<UserParticipationHistory, String> locationColumn;
    @FXML
    private TableColumn<UserParticipationHistory, String> weekdayColumn;
    @FXML
    private TableColumn<UserParticipationHistory, Integer> participationSlotColumn;
    @FXML
    private TableColumn<UserParticipationHistory, Integer> participationHoursColumn;
    @FXML
    private TableColumn<UserParticipationHistory, Integer> participationValueColumn;
    @FXML
    private Button returnHomeButton;
    @FXML
    private Button exportHistoryButton;
    @FXML
    private Label messageLabel;

    public ParticipationHistoryController(Stage stage ,Model model, InitData initData) {
        this.stage = stage;
        this.model = model;
        this.initData = initData;
        this.cartItems = initData.getCartItems();
    }

    public void initialize() {
        participationIdColumn.setCellValueFactory(new PropertyValueFactory<>("registrationId"));
        userDateColumn.setCellValueFactory(new PropertyValueFactory<>("FormatDate"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("projectLocation"));
        weekdayColumn.setCellValueFactory(new PropertyValueFactory<>("projectWeekday"));
        participationSlotColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfSlots"));
        participationHoursColumn.setCellValueFactory(new PropertyValueFactory<>("hoursPerSlot"));
        participationValueColumn.setCellValueFactory(new PropertyValueFactory<>("totalContribution"));

        List<UserParticipationHistory> histories = initData.getUserParticipationHistoryList();
        Collections.reverse(histories);
        ObservableList<UserParticipationHistory> data = FXCollections.observableArrayList(histories);
        tableHistory.setItems(data);
    }

    public void returnHomeHandlerFromPartiticipationHistory (ActionEvent event) throws IOException {
        UserController controller = new UserController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/HomeView.fxml", "Home Page", event, controller);
    }

    @FXML
    public void exportHistoryHandler(ActionEvent event) throws Exception {
        FileHandler fileHandler = new FileHandler();
        boolean success = fileHandler.exportParticipationHistory(initData);

        if (success) {
            messageLabel.setText("History exported successfully.");
        } else {
            messageLabel.setText("Export failed.");
        }


    }

}
