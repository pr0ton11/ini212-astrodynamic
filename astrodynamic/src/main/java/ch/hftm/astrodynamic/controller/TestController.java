package ch.hftm.astrodynamic.controller;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

public class TestController extends BaseController{
    //@FXML

    public TestController() {
        super();
    }

    @Override
    public void initialize(){
        showError("Test");
    }

    @FXML
    void onTestClicked(ActionEvent e) {
        askYesNo("You like tests?");
    }
}
