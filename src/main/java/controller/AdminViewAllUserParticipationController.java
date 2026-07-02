package controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import viewSwitch.SwitchPathWay;
import model.InitData;
import model.Model;
import model.UserParticipationHistory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AdminViewAllUserParticipationController {
    private Stage stage;
    private Model model;
    private InitData initData;

    @FXML
    private TableView<UserParticipationHistory> tableHistory;
    @FXML
    private TableColumn<UserParticipationHistory, String> participationIdColumn;
    @FXML
    private TableColumn<UserParticipationHistory, String> userDateColumn;
    @FXML
    private TableColumn<UserParticipationHistory, String> projectNameColumn;
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
    private TableColumn<UserParticipationHistory, String> userColumn;
    @FXML
    private Button backButton;

    public AdminViewAllUserParticipationController(Stage stage, Model model, InitData initData) {
        this.stage = stage;
        this.model = model;
        this.initData = initData;
    }

    @FXML
    public void initialize() {
        // Bind columns to model properties
        participationIdColumn.setCellValueFactory(new PropertyValueFactory<>("registrationId"));
        userDateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getFormatDate()
        ));
        projectNameColumn.setCellValueFactory(new PropertyValueFactory<>("projectName"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<>("projectLocation"));
        weekdayColumn.setCellValueFactory(new PropertyValueFactory<>("projectWeekday"));
        participationSlotColumn.setCellValueFactory(new PropertyValueFactory<>("numberOfSlots"));
        participationHoursColumn.setCellValueFactory(new PropertyValueFactory<>("hoursPerSlot"));
        participationValueColumn.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        try {
            List<UserParticipationHistory> allHistory = model.getProjectSelectionDao().loadAllParticipationHistory();
            tableHistory.setItems(FXCollections.observableArrayList(allHistory));
        } catch (SQLException e) {
            System.out.println("Error loading participation history: " + e.getMessage());
        }
    }

    @FXML
    public void backButtonHandler(ActionEvent event) throws IOException {
        AdminController controller = new AdminController(stage, model, initData);
        SwitchPathWay.buttonLoadAndSetNewRoot("/view/AdminView.fxml", "Admin Home", event, controller);
    }

}
