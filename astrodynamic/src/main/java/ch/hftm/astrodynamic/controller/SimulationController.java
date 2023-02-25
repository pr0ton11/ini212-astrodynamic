package ch.hftm.astrodynamic.controller;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Sphere;
import ch.hftm.astrodynamic.gui.FlatProjection;
import ch.hftm.astrodynamic.gui.GroundTrack;
import ch.hftm.astrodynamic.model.Mission;
import ch.hftm.astrodynamic.model.Simulation;
import ch.hftm.astrodynamic.model.conditions.SetupHeavyLander;
import ch.hftm.astrodynamic.physics.AstronomicalObject;
import ch.hftm.astrodynamic.physics.BaseAstronomicalObject;
import ch.hftm.astrodynamic.physics.Spaceship;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.scalar.UnitlessScalar;
import ch.hftm.astrodynamic.utils.MissionRepository;
import ch.hftm.astrodynamic.utils.Quad;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.Unit;
import ch.hftm.astrodynamic.utils.UnitConversionError;
import ch.hftm.astrodynamic.scalar.*;
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
    private static final int MIN_ZOOM = -12;
    private static final int MAX_ZOOM = 0;

    private static final double MIN_ENLARGE = 1.0;
    private static final double MAX_ENLARGE = 100.0;

    static final int DEFAULT_ZOOM_FACTOR = -5;
    static final double DEFAULT_ENLARGE_FACTOR = 10.0;

    // time units for buttons
    static final Scalar ONE_SECOND = new TimeScalar(1);
    static final Scalar ONE_MINUTE = new TimeScalar(60);
    static final Scalar ONE_HOUR = new TimeScalar(3600);
    static final Scalar ONE_DAY = new TimeScalar(86400);

    @FXML
    Canvas orbitView;

    @FXML
    ComboBox<AstronomicalObject> focus;
    ObservableList<AstronomicalObject> focusList;

    @FXML
    ComboBox<AstronomicalObject> reference;
    ObservableList<AstronomicalObject> referenceList;

    @FXML
    Label zoomReadout;

    @FXML
    Slider zoomSlider;

    @FXML
    Label enlargeReadout;

    @FXML
    Slider enlargeSlider;

    FlatProjection projection;

    Mission currentMission;

    public SimulationController() {
        super();
    }

    @Override
    public void initialize(){
        
        currentMission = MissionRepository.getActiveMission();
        projection = new FlatProjection(orbitView, currentMission);

        initializeZoom();
        initializeEnlarge();
        initializeComboboxes();

        for (AstronomicalObject o: currentMission.getAstronomicalObjects()) {
            focusList.add(o);
            referenceList.add(o);
        }

        // TODO thick labels and tick marks: https://docs.oracle.com/javase/8/javafx/api/javafx/scene/control/Slider.html

        try {
            projection.draw();
        } catch (UnitConversionError e) {
            showError(e.toString());
        }

        //currentMission.start();
    }

    // focus and reference drop downs
    private void initializeComboboxes() {
        focusList = FXCollections.observableArrayList();
        referenceList = FXCollections.observableArrayList();

        focus.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(AstronomicalObject item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    BaseAstronomicalObject bao = (BaseAstronomicalObject)item;
                    setText(bao.getName());
                }
            }
        });
        focus.setItems(focusList);

        reference.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(AstronomicalObject item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    BaseAstronomicalObject bao = (BaseAstronomicalObject)item;
                    setText(bao.getName());
                }
            }
        });
        reference.setItems(referenceList);
    }

    // set up zoom slider with listener on change
    private void initializeZoom() {
        zoomSlider.setMin(MIN_ZOOM);
        zoomSlider.setMax(MAX_ZOOM);
        
        zoomSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            try {
                projection.setZoomFactor(calculateZoomFactor(newValue.doubleValue()));
    
                projection.draw();
            } catch (UnitConversionError ex) {
                showError(ex.toString());
            }

            zoomReadout.setText("-1E" + Double.toString(newValue.intValue()*-1.0)); // we have to invert it to display in a readable engineering notation
            zoomSlider.setValue(newValue.intValue());
        });

        // set slider and text to default factor
        zoomSlider.setValue(DEFAULT_ZOOM_FACTOR);
        zoomReadout.setText(Double.toString(DEFAULT_ZOOM_FACTOR));
    }

    // set up enlarge slider with listener on change
    private void initializeEnlarge() {
        enlargeSlider.setMin(MIN_ENLARGE);
        enlargeSlider.setMax(MAX_ENLARGE);
        
        enlargeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            try {
                projection.setEnlargeFactor(calculateEnlargeFactor(newValue.doubleValue()));
    
                projection.draw();
            } catch (UnitConversionError ex) {
                showError(ex.toString());
            }

            enlargeReadout.setText(Double.toString(newValue.intValue()));
        });

        // set slider and text to default factor
        enlargeSlider.setValue(DEFAULT_ENLARGE_FACTOR);
        enlargeReadout.setText(Double.toString(DEFAULT_ENLARGE_FACTOR));
    }

    // zoomfactor is 1E1-sliderValue to allow huge zoom out range
    private Scalar calculateZoomFactor(double sliderValue) throws UnitConversionError {
        return new UnitlessScalar(new Quad(1, (int)(1-sliderValue)));
    }

    // enlargefactor is a 1:1 mapping of the slider, this method is here as a shim for future changes
    private Scalar calculateEnlargeFactor(double sliderValue) throws UnitConversionError {
        return new UnitlessScalar(new Quad(sliderValue));
    }

    // save mission to repository
    public void saveMission(ActionEvent e) {
        showError("Not implemented yet");
    }

    // copy save mission to repository
    public void copyMission(ActionEvent e) {
        showError("Not implemented yet");
    }

    private void simulate(Scalar totalTime) {
        try {
            currentMission.simulateInSteps(totalTime);
            projection.draw();
        } catch (Exception ex) {
            showError(ex.toString());
        }
    }

    public void simulateSecond(ActionEvent e) {
        simulate(ONE_SECOND);
    }

    public void simulateMinute(ActionEvent e) {
        simulate(ONE_MINUTE);
    }

    public void simulateHour(ActionEvent e) {
        simulate(ONE_HOUR);
    }

    public void simulateDay(ActionEvent e) {
        simulate(ONE_DAY);
    }

    public void focusChanged(ActionEvent e) {
        try {
            projection.setFocus(focus.getSelectionModel().getSelectedItem());
            projection.draw();
        } catch (Exception ex) {
            showError(ex.toString());
        }
    }

    public void referenceChanged(ActionEvent e) {
        //reference.getSelectionModel().getSelectedItem();
        //projection.draw();
    }
}
