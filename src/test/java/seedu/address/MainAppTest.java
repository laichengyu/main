package seedu.address;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class MainAppTest {

    @Test
    public void isMainApp() {
        MainApp mainApp = new MainApp();
        assertFalse(mainApp.isTest);
    }
}
