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
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.File;
import java.util.logging.Logger;

import ch.hftm.astrodynamic.model.*;
import ch.hftm.astrodynamic.utils.Log;

public class MissionController extends BaseController{

    private Logger log = Log.build();

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
    Button clearSearchButton;

    @FXML
    VBox missionData;

    ObservableList<Mission> missions;
    FilteredList<Mission> filteredMissions;

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

    // test data, missions would be stored on disk
    private void initializeTestdata() {
        missions = FXCollections.observableArrayList();
        missions.add(new Mission("Driving Miss Daisy", "<h1>Driving Miss Daisy</h1><br>A bunch of scientists want to travel to the ISS.<br>You'll be the driver.<br><img src=\"https://www.nasa.gov/sites/default/files/styles/full_width/public/thumbnails/image/progress_1_29_tianzhou_4_depating_from_tiangong.jpg?itok=sqE2bAY_\" width=300px height=200px>"));
        missions.add(new Mission("New Dawn", "<h1>New Dawn</h1><br>We picked a suitable landingspot on Ganymede.<br>Bring a flag.<br><img src=\"https://www.nasa.gov/sites/default/files/thumbnails/image/e1_-_pia24682_-_juno_ganymede_sru_-_darkside.png\" alt=\"Ganymede landing spot\" width=200px height=200px>"));
        missions.add(new Mission("In the Well", "<h1>In the Well</h1><br>The jovian ammonia harvesting station lost its engines.<br>Evacuate the personel before it drifts into Jupiter.<br><img src=\"https://www.nasa.gov/sites/default/files/thumbnails/image/hotspot_cover_1280.jpg\" width=400px height=200px>"));
        
        filteredMissions = new FilteredList<>(missions);

        missionList.setItems(filteredMissions);
    }

    // user searches mission in top search bar
    @FXML
    void searchTyping(KeyEvent e) {
        String searchText = searchField.getText().toLowerCase();
        missionList.setItems(filteredMissions.filtered((t) -> {
            return (t.getName().toLowerCase().contains(searchText)) || (t.getDescription().toLowerCase().contains(searchText));
        }));
    }

    @FXML
    void clearSearch(ActionEvent e) {
        searchField.setText("");
        missionList.setItems(filteredMissions);
    }

    // 
    @FXML
    void missionSelected(MouseEvent e) {
        Mission selectedMission = missionList.getSelectionModel().getSelectedItem();
        if (selectedMission != null) {
            missionData.setVisible(true);
            missionDescription.getEngine().loadContent(selectedMission.getDescription());
        } else {
            missionData.setVisible(false);
        }
    }

    // user clicked edit button
    @FXML
    void startEditor(ActionEvent e) {
        Mission selectedMission = missionList.getSelectionModel().getSelectedItem();

        File f = new File("view/MissionEditView.fxml");
        log.info(f.getAbsolutePath());

        showSceneOnNewStage("Mission Editor - " + selectedMission.getName(), true, "view/MissionEditView.fxml");
    }

    // user clicked simulate button
    @FXML
    void startSimulation(ActionEvent e) {
        Mission selectedMission = missionList.getSelectionModel().getSelectedItem();
        //showError("Error starting simulation of mission " + selectedMission.getName() + ".\nNot implemented!");

        showSceneOnNewStage("Simulation - " + selectedMission.getName(), true, "view/SimulationView.fxml");
    }
}
