package ch.hftm.astrodynamic.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

public class MissionEditController extends BaseController{
    @FXML
    Canvas graphic;

    @FXML
    TextField missionName;

    public MissionEditController() {
        super();
    }

    @Override
    protected Stage getCurrentStage() {
        return null;
    }

    @Override
    public void initialize(){

        initializeTestdata();
    }

    // test data, missions would be stored on disk
    private void initializeTestdata() {
        
    }

    // 
    @FXML
    void clickGraphic(MouseEvent e) {
        // x and y coords
        System.out.println("click " + e.toString());
    }

    // 
    @FXML
    void dragGraphic(MouseEvent e) {
        // we have no delta here
        System.out.println("drag " + e.toString());
    }

    // 
    @FXML
    void scrollGraphic(ScrollEvent e) {
        // deltaY moves to indicate vertical scroll
        System.out.println("scroll " + e.toString());
    }
}
