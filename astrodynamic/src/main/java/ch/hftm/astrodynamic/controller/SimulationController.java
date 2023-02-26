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
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
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
import ch.hftm.astrodynamic.utils.BaseVector;
import ch.hftm.astrodynamic.utils.MissionRepository;
import ch.hftm.astrodynamic.utils.Quad;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.Unit;
import ch.hftm.astrodynamic.utils.UnitConversionError;
import ch.hftm.astrodynamic.utils.Vector;
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

    @FXML
    AnchorPane shipController;

    @FXML
    Canvas burnCanvas;
    GraphicsContext burnContext;

    @FXML
    Label distanceToReference;

    @FXML
    Label velocityToReference;

    @FXML
    Label remainingDeltaV;

    @FXML
    Label maneuverDeltaV;

    FlatProjection projection;

    Mission currentMission;

    public SimulationController() {
        super();
    }

    @Override
    public void initialize(){
        currentMission = MissionRepository.getActiveMission();
        projection = new FlatProjection(orbitView, currentMission);

        burnContext = burnCanvas.getGraphicsContext2D();

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
            updateSpaceshipInfo();
        } catch (Exception ex) {
            showError(ex.toString());
        }

        // if condition is met, show info to user
        if (currentMission.isResolved()) {
            showInfo(currentMission.getMetConditionInfo());
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
            updateSpaceshipInfo();
        } catch (Exception ex) {
            showError(ex.toString());
        }
    }

    public void referenceChanged(ActionEvent e) {
        try {
            //projection.draw(); // not relevant at the moment because we do not mark the reference on the map
            updateSpaceshipInfo();
        } catch (Exception ex) {
            showError(ex.toString());
        }
    }

    // user clicked on burn canvas
    public void burnCanvasClicked(MouseEvent e) {
        AstronomicalObject focusObject = focus.getSelectionModel().getSelectedItem();

        if (!(focusObject instanceof Spaceship)) {
            return;
        }

        Spaceship ship = (Spaceship)focusObject;

        try {
            Scalar diffX = new UnitlessScalar(e.getX() - burnCanvas.getWidth()/2).divide(new UnitlessScalar(burnCanvas.getWidth()/2)); // calculate relative click position to middle of canvas to get direction
            Scalar diffY = new UnitlessScalar(e.getY() - burnCanvas.getHeight()/2).divide(new UnitlessScalar(burnCanvas.getHeight()/2));
            Vector burnVector = new BaseVector(diffX, diffY, new UnitlessScalar());

            burnVector = burnVector.multiply(ship.getDeltaV()); // multiply direction with total deltaV available

            ship.setBurn(burnVector);

            updateSpaceshipInfo();
        } catch (UnitConversionError ex) {
            showError(ex.toString());
        }
    }

    private void updateSpaceshipInfo() {
        AstronomicalObject focusObject = focus.getSelectionModel().getSelectedItem();

        if (focusObject == null) {
            shipController.setVisible(false);
            return;
        }

        if (focusObject instanceof Spaceship) {
            shipController.setVisible(true);
            updateReferenceData();
            drawBurnCanvas();
        } else {
            shipController.setVisible(false);
        }
    }

    private void updateReferenceData() {
        AstronomicalObject focusObject = focus.getSelectionModel().getSelectedItem();
        AstronomicalObject referenceObject = reference.getSelectionModel().getSelectedItem();

        distanceToReference.setText("");
        velocityToReference.setText("");
        remainingDeltaV.setText("");
        maneuverDeltaV.setText("");

        Spaceship ship = (Spaceship)focusObject;
        remainingDeltaV.setText(ship.getDeltaV().toFittedString());

        if (ship.isManeuvering()) {
            maneuverDeltaV.setText(ship.getBurn().getLength().toFittedString());
        }

        if (referenceObject == null) {
            return;
        }

        if (focusObject == referenceObject) {
            return;
        }

        try {
            Scalar distance = focusObject.getDistance(referenceObject);
            distanceToReference.setText(distance.toFittedString());

            velocityToReference.setText(focusObject.getVelocity().subtract(referenceObject.getVelocity()).getLength().toFittedString());
        } catch (UnitConversionError e) {

        }
    }

    private void drawBurnCanvas() {
        burnContext.clearRect(0, 0, burnCanvas.getWidth(), burnCanvas.getHeight());

        burnContext.setStroke(Color.BLACK);
        burnContext.strokeOval(0, 0, burnCanvas.getWidth(), burnCanvas.getHeight());
        burnContext.stroke();

        AstronomicalObject focusObject = focus.getSelectionModel().getSelectedItem();

        Spaceship ship = (Spaceship)focusObject;

        if (ship.isManeuvering()) {
            try {
                Vector burnVector = ship.getBurn().divide(ship.getDeltaV()); // get -1.0 to 1.0 values of direction

                double pointX = burnCanvas.getWidth()/2 + (burnCanvas.getWidth()/2) * burnVector.getX().getValue().doubleValue(); // calculate point on burn canvas
                double pointY = burnCanvas.getHeight()/2 + (burnCanvas.getHeight()/2) * burnVector.getY().getValue().doubleValue();

                burnContext.setStroke(Color.RED);
                burnContext.strokeLine(burnCanvas.getWidth()/2, burnCanvas.getHeight()/2, pointX, pointY);
                burnContext.stroke();
            } catch (UnitConversionError ex) {

            }
        }

        burnContext.setStroke(Color.BLUE);
        burnContext.strokeOval(burnCanvas.getWidth()/2-1, burnCanvas.getHeight()/2-1, 2, 2);
        burnContext.stroke();
    }
}
