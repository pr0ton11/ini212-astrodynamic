package ch.hftm.astrodynamic.controller;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;


import ch.hftm.astrodynamic.gui.GroundTrack;
import ch.hftm.astrodynamic.model.Mission;
import ch.hftm.astrodynamic.model.Simulation;
import ch.hftm.astrodynamic.model.conditions.SetupHeavyLander;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.utils.Unit;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// Running simulation information
public class SimulationController extends BaseController{
    @FXML
    Canvas orbitView;

    @FXML
    ComboBox<String> groundtrackPlanet;

    ObservableList<String> groundtrackPlanets;
    GroundTrack groundTrack;

    Mission currentMission;

    public SimulationController() {
        super();
    }

    @Override
    public void initialize(){
        
        String initialTrackedPlanet = "Earth";

        currentMission = new Mission("null", "null");
        currentMission.setupStandardSolarSystem();
        
        try {
            SetupHeavyLander shl = new SetupHeavyLander((LengthScalar)ScalarFactory.create(350000, Unit.LENGTH), currentMission.getAstronomicalObjectByName(initialTrackedPlanet));
            currentMission.addCondition(shl);
        } catch (Exception e) {
            showError(e.toString());
        }

        groundtrackPlanets = FXCollections.observableArrayList();
        for (String name: currentMission.getPlanetoidNames())
        {
            groundtrackPlanets.add(name);
        }
        groundtrackPlanet.setItems(groundtrackPlanets);

        groundtrackPlanet.getSelectionModel().select(initialTrackedPlanet); // TODO: this should come from the mission

        groundTrack = new GroundTrack(orbitView);
        groundTrack.setPlanet(initialTrackedPlanet); // TODO: this should come from the mission

        groundTrack.draw();

        //currentMission.start();
    }

    @FXML
    public void groundtrackPlanetChanged(ActionEvent e) {
        groundTrack.setPlanet(groundtrackPlanet.getSelectionModel().getSelectedItem());
        groundTrack.clearTrack();
        groundTrack.draw();
    }
}
