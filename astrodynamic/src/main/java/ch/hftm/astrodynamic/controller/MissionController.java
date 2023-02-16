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
import javafx.collections.ObservableSet;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

import java.io.File;
import java.util.logging.Logger;

import ch.hftm.astrodynamic.model.*;
import ch.hftm.astrodynamic.utils.Log;
import ch.hftm.astrodynamic.utils.MissionRepository;

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

        missions = MissionRepository.getObservableMissions();

        if (missions.size() < 1) {
            //showInfo("No missions found, added test missions to list.");
            MissionRepository.addTestMissions();
        }

        filteredMissions = new FilteredList<>(missions);
        missionList.setItems(missions);
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

    Mission getSelectedMission() {
        return missionList.getSelectionModel().getSelectedItem();
    }

    // 
    @FXML
    void missionSelected(MouseEvent e) {
        Mission selectedMission = getSelectedMission();
        if (selectedMission != null) {
            missionData.setVisible(true);
            missionDescription.getEngine().loadContent(selectedMission.getDescription());
            MissionRepository.setActiveMission(selectedMission);
        } else {
            missionData.setVisible(false);
        }
    }

    // user clicked edit button
    @FXML
    void startEditor(ActionEvent e) {
        Mission selectedMission = getSelectedMission();
        showSceneOnNewStage("Mission Editor - " + selectedMission.getName(), true, "view/MissionEditView.fxml");
    }

    // user clicked simulate button
    @FXML
    void startSimulation(ActionEvent e) {
        Mission selectedMission = getSelectedMission();

        showSceneOnNewStage("Simulation - " + selectedMission.getName(), true, "view/SimulationView.fxml");
    }

    @FXML
    void deleteMission(ActionEvent e) {
        Mission selectedMission = getSelectedMission();

        if (selectedMission != null) {
            if (askYesNo(String.format("Do you really want to delete mission '%s'?", selectedMission.getName()))) {
                MissionRepository.deleteMission(selectedMission);
                missionData.setVisible(false);
            }
        }
    }

    @FXML
    void newMission(ActionEvent e) {
        Mission newMission = new Mission("","");
        MissionRepository.addMission(newMission);
        MissionRepository.setActiveMission(newMission);
        showSceneOnNewStage("Mission Editor - New mission", true, "view/MissionEditView.fxml");
    }

    // user clicked copy button
    @FXML
    void copyMission(ActionEvent e) {
        showError("Error copy mission.\nNot implemented!");
    }
}
