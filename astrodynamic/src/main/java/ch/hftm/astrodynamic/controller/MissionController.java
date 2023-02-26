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
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;

import java.util.logging.Logger;

import ch.hftm.astrodynamic.model.*;
import ch.hftm.astrodynamic.utils.Log;
import ch.hftm.astrodynamic.utils.MissionRepository;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Controller for overview list of missions
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

    ObservableList<Mission> missions; // get from MissionRepository DAO
    FilteredList<Mission> filteredMissions; // for searchField filtering

    public MissionController() {
        super();
    }

    @Override
    public void initialize(){

        // mission list shows mission name
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

        // adding testmissions if no mission is loaded
        if (missions.size() < 1) {
            //showInfo("No missions found, added test missions to list.");
            MissionRepository.addTestMissions();
        }

        filteredMissions = new FilteredList<>(missions);
        missionList.setItems(filteredMissions); // show filtered missions in list
    }

    // user searches mission in top search bar
    @FXML
    void searchTyping(KeyEvent e) {
        String searchText = searchField.getText().toLowerCase();
        missionList.setItems(filteredMissions.filtered((t) -> {
            return (t.getName().toLowerCase().contains(searchText)) || (t.getDescription().toLowerCase().contains(searchText));
        }));
    }

    // empty search field
    @FXML
    void clearSearch(ActionEvent e) {
        searchField.setText("");
        missionList.setItems(filteredMissions);
    }

    // selected mission from mission list
    Mission getSelectedMission() {
        return missionList.getSelectionModel().getSelectedItem();
    }

    // fired when user clicks on mission in list, sets active mission in repository and shows description
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

    // user clicked edit button, open mission editor
    @FXML
    void startEditor(ActionEvent e) {
        Mission selectedMission = getSelectedMission();
        showSceneOnNewStage("Mission Editor - " + selectedMission.getName(), true, "view/MissionEditView.fxml");
    }

    // user clicked simulate button, open simulation window
    @FXML
    void startSimulation(ActionEvent e) {
        Mission selectedMission = getSelectedMission();

        showSceneOnNewStage("Simulation - " + selectedMission.getName(), false, "view/SimulationView.fxml");
    }

    // user clicked delete mission, ask him if he is sure, if yes delete mission from repository
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

    // user clicked new mission, add new mission to repository, open mission editor
    @FXML
    void newMission(ActionEvent e) {
        Mission newMission = new Mission("","");
        MissionRepository.addMission(newMission);
        MissionRepository.setActiveMission(newMission);
        showSceneOnNewStage("Mission Editor - New mission", false, "view/MissionEditView.fxml");
    }

    // user clicked copy button, copy selected mission in repository, open mission editor
    @FXML
    void copyMission(ActionEvent e) {
        showError("Not implemented");
        return;

        /*
        Mission clonedMission = MissionRepository.cloneMission();
        clonedMission.setName(clonedMission.getName() + " kopie");
        MissionRepository.addMission(clonedMission);
        MissionRepository.setActiveMission(clonedMission);
        showSceneOnNewStage("Mission Editor - " + MissionRepository.getActiveMission().getName(), false, "view/MissionEditView.fxml");
        */
    }
}
