package ch.hftm.astrodynamic.controller;

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
import ch.hftm.astrodynamic.physics.Spaceship;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.scalar.UnitlessScalar;
import ch.hftm.astrodynamic.utils.MissionRepository;
import ch.hftm.astrodynamic.utils.Quad;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.Unit;
import ch.hftm.astrodynamic.utils.UnitConversionError;
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

    @FXML
    Canvas orbitView;

    @FXML
    ComboBox<AstronomicalObject> focus;

    @FXML
    ComboBox<AstronomicalObject> reference;

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

        try {
            projection.draw();
        } catch (UnitConversionError e) {
            showError(e.toString());
        }

        //currentMission.start();
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
}
