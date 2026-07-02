package fileHandler;

import model.AvailableProject;
import model.InitData;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FileHandlerTest {

    private FileHandler fileHandler;

    @BeforeEach
    public void setup() {
        fileHandler = new FileHandler();
    }

    @Test
    void readAvailableProjectSuccessfulTest() {

        try {

            ArrayList<AvailableProject> projects = fileHandler.readAvailableProject();
            Assertions.assertNotNull(projects);
            Assertions.assertEquals(9, projects.size());

        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    void exportParticipationHistorySuccessfulTest() {

        User user = new User();
        user.setUsername("test");

        InitData initData = new InitData();
        initData.setUser(user);

        try {
            boolean result = fileHandler.exportParticipationHistory(initData);
            Assertions.assertTrue(result);

        } catch (Exception e) {
           Assertions.fail(e.getMessage());
        }

    }
}