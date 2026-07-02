package controller;

import model.InitData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationConfirmationControllerTest {

    private RegistrationConfirmationController registrationConfirmationController;
    private InitData dummyInitData;

    @BeforeEach
    void setup() {
        dummyInitData = new InitData();
    }

    @Test
    void testValidCode() {
        RegistrationConfirmationController controller = new RegistrationConfirmationController(null, null, dummyInitData);
        assertTrue(controller.isValidConfirmationCode("123456"));
    }

    @Test
    public void testInvalidCodeTooShort() {
        RegistrationConfirmationController controller = new RegistrationConfirmationController(null, null, dummyInitData);
        assertFalse(controller.isValidConfirmationCode("12345"));
    }

}