package ch.hftm.astrodynamic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import ch.hftm.astrodynamic.model.*;

public class MissionController extends BaseController{
    @FXML
    TextField searchField;

    @FXML
    ListView<Mission> missionList;

    @FXML
    WebView missionDescription;

    @FXML
    Button missionEditButton;

    @FXML
    Button missionSimulateButton;

    @FXML
    VBox missionData;

    ObservableList<Mission> missions;

    public MissionController() {
        super();
    }

    @Override
    protected Stage getCurrentStage() {
        return null;
    }

    @Override
    public void initialize(){
        missionList.setCellFactory(param -> new ListCell<Mission>() {
            @Override
            protected void updateItem(Mission item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });

        missionData.setVisible(false); // hide mission data if no mission is selected

        initializeTestdata();
    }

    private void initializeTestdata() {
        missions = FXCollections.observableArrayList();
        missions.add(new Mission("Driving Miss Daisy", "<h1>Driving Miss Daisy</h1><br>A bunch of scientists want to travel to the ISS.<br>You'll be the driver.<br><img src=\"https://www.nasa.gov/sites/default/files/styles/full_width/public/thumbnails/image/progress_1_29_tianzhou_4_depating_from_tiangong.jpg?itok=sqE2bAY_\" width=300px height=200px>"));
        missions.add(new Mission("New Dawn", "<h1>New Dawn</h1><br>We picked a suitable landingspot on Ganymede.<br>Bring a flag.<br><img src=\"https://www.nasa.gov/sites/default/files/thumbnails/image/e1_-_pia24682_-_juno_ganymede_sru_-_darkside.png\" alt=\"Ganymede landing spot\" width=200px height=200px>"));
        missions.add(new Mission("In the Well", "<h1>In the Well</h1><br>The jovian ammonia harvesting station lost its engines.<br>Evacuate the personel before it drifts into Jupiter.<br><img src=\"https://www.nasa.gov/sites/default/files/thumbnails/image/hotspot_cover_1280.jpg\" width=400px height=200px>"));
        missionList.setItems(missions);
    }

    // user searches mission in top search bar
    @FXML
    void searchTyping(KeyEvent e) {
        System.out.println("Searching: " + searchField.getText());
    }

    // 
    @FXML
    void missionSelected(MouseEvent e) {
        missionData.setVisible(true);

        Mission selectedMission = missionList.getSelectionModel().getSelectedItem();
        missionDescription.getEngine().loadContent(selectedMission.getDescription());
    }

    // user clicked edit button
    @FXML
    void startEditor(ActionEvent e) {
        Mission selectedMission = missionList.getSelectionModel().getSelectedItem();
        showError("Error starting editor for mission " + selectedMission.getName() + ".\nNot implemented!");
    }

    // user clicked simulate button
    @FXML
    void startSimulation(ActionEvent e) {
        Mission selectedMission = missionList.getSelectionModel().getSelectedItem();
        showError("Error starting simulation of mission " + selectedMission.getName() + ".\nNot implemented!");
    }
}
