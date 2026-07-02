package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.ProjectDao;
import dao.ProjectSelectionDao;
import fileHandler.FileHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.*;

public class LoginController {
    @FXML
    private TextField name;
    @FXML
    private PasswordField password;
    @FXML
    private Label message;
    @FXML
    private Button login;
    @FXML
    private Button signup;

    private Model model;
    private Stage stage;
    private InitData initData;

   private User loggedUser;

    public LoginController(Stage stage, Model model) {
        this.stage = stage;
        this.model = model;
        this.loggedUser = loggedUser;

    }

    @FXML
    public void initialize() {
        login.setOnAction(event -> {
            if (!name.getText().isEmpty() && !password.getText().isEmpty()) {
                User user;
                try {
                    user = model.getUserDao().getUser(name.getText(), password.getText());

                    if (user != null) {
                        model.setCurrentUser(user);

                        try {
                            //load the csv data
                            InitializeInstances initializeInstances = new InitializeInstances();
                            initData = initializeInstances.initialize();
                            initData.setUser(user);

                            //load Participation history
                            ProjectSelectionDao projectSelectionDao = model.getProjectSelectionDao();
                            List<UserParticipationHistory> historyList = projectSelectionDao.loadParticipationHistory(user);
                            initData.setLoadedHistory(historyList);

                            //load availiable project from db
                            ProjectDao projectDao = model.getProjectDao();
                            List<AvailableProject> projects = projectDao.loadAvailableProjects();
                            initData.setAvailableProjects(new ArrayList<>(projects));

                            //check project if empty
                            if (projects.isEmpty()) {
                                FileHandler fileHandler = new FileHandler();
                                ArrayList<AvailableProject> csvProjects = fileHandler.readAvailableProject();

                                for (AvailableProject project : csvProjects) {
                                    projectDao.insertOrUpdateProject(project);
                                }

                                projects = projectDao.loadAvailableProjects(); // Reload after seeding
                            }

                            initData.setAvailableProjects(new ArrayList<>(projects));

                            if (user.getAdmin()) {
                                //Admin user: load AdminView
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminView.fxml"));
                                AdminController adminController = new AdminController(stage, model, initData);

                                loader.setController(adminController);
                                VBox root = loader.load();

                                adminController.showStage(root);
                            } else {
                                //Regular user: load HomeView
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/HomeView.fxml"));
                                UserController userController = new UserController(stage, model, initData);

                                loader.setController(userController);
                                VBox root = loader.load();

                                userController.showStage(root);
                            }

                            loggedUser = user;
                            stage.close();

                        }catch (IOException e) {
                            message.setText(e.getMessage());
                        } catch (Exception e) {
                            System.out.println("unable to load initializeInstances");
                            e.printStackTrace();
                        }

                    } else {
                        message.setText("Wrong username or password");
                        message.setTextFill(Color.RED);
                    }
                } catch (SQLException e) {
                    message.setText(e.getMessage());
                    message.setTextFill(Color.RED);
                }

            } else {
                message.setText("Empty username or password");
                message.setTextFill(Color.RED);
            }
            name.clear();
            password.clear();
        });

        signup.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SignupView.fxml"));

                // Customize controller instance
                SignupController signupController =  new SignupController(stage, model);

                loader.setController(signupController);
                VBox root = loader.load();

                signupController.showStage(root);

                message.setText("");
                name.clear();
                password.clear();

                stage.close();
            } catch (IOException e) {
                message.setText(e.getMessage());
            }});
    }

    public void showStage(Pane root) {
        Scene scene = new Scene(root, 500, 300);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setTitle("Welcome");
        stage.show();
    }
}
