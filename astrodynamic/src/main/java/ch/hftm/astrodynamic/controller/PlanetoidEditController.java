package ch.hftm.astrodynamic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

import java.util.logging.Logger;

import ch.hftm.astrodynamic.physics.AtmosphereModel;
import ch.hftm.astrodynamic.physics.Planetoid;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.utils.BaseVector;
import ch.hftm.astrodynamic.utils.Log;
import ch.hftm.astrodynamic.utils.MissionRepository;
import ch.hftm.astrodynamic.utils.Quad;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.Unit;
import ch.hftm.astrodynamic.utils.UnitConversionError;
import ch.hftm.astrodynamic.utils.Vector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.stage.Stage;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

// edit parameters of a planetoid
public class PlanetoidEditController extends BaseController{
    // for better exception messages
    static final String VECTOR_NAME_POSITION = "position_";
    static final String VECTOR_NAME_VELOCITY = "velocity_";

    @FXML
    TextField posX;

    @FXML
    TextField posY;

    @FXML
    TextField posZ;

    @FXML
    TextField posMagnitude;

    @FXML
    TextField velX;

    @FXML
    TextField velY;

    @FXML
    TextField velZ;

    @FXML
    TextField velMagnitude;

    @FXML
    TextField name;

    @FXML
    TextField description;

    @FXML
    TextField mass;

    @FXML
    TextField zeroElevation;

    @FXML
    TextField atmosHeight;

    @FXML
    ComboBox<AtmosphereModel> atmosModel;

    @FXML
    TextField atmosOxygenPercentage;

    // now the unit combo boxes for convinient unit display
    @FXML
    ComboBox<String> massUnit;
    ObservableList<String> massUnitOptions;
    String previousMassUnit;

    @FXML
    ComboBox<String> zeroElevationUnit;
    ObservableList<String> zeroElevationUnitOptions;
    String previousZeroElevationUnit;

    @FXML
    ComboBox<String> positionUnit;
    ObservableList<String> positionUnitOptions;
    String previousPositionUnit;

    @FXML
    ComboBox<String> velocityUnit;
    ObservableList<String> velocityUnitOptions;
    String previousVelocityUnit;

    @FXML
    ComboBox<String> atmosHeightUnit;
    ObservableList<String> atmosHeightUnitOptions;
    String previousAtmosHeightUnit;

    Planetoid planetoidToEdit;

    Logger log = Log.build();

    public PlanetoidEditController() {
        super();
    }

    @Override
    public void initialize(){
        planetoidToEdit = MissionRepository.getActiveMission().getReferencePlanetoid();

        initializeUnitComboboxes();

        moveDataFromObjectToGui();
    }

    // here we load a single unitsize into a dropdown and set the base unit
    void initializeUnitBox(ComboBox<String> box, ObservableList<String> correspondingUnitsizeList, Unit unit) {
        correspondingUnitsizeList = FXCollections.observableArrayList(ScalarFactory.getUnitSizes(unit));
        box.setItems(correspondingUnitsizeList);
        box.getSelectionModel().select(ScalarFactory.getBaseUnitSize(unit));
    }

    // here we load all the available unitsizes into the dropdown menues and select the base unit, via initializeUnitBox
    void initializeUnitComboboxes() {
        initializeUnitBox(massUnit, massUnitOptions, Unit.MASS);
        initializeUnitBox(zeroElevationUnit, zeroElevationUnitOptions, Unit.LENGTH);
        initializeUnitBox(positionUnit, positionUnitOptions, Unit.LENGTH);
        initializeUnitBox(velocityUnit, velocityUnitOptions, Unit.VELOCITY);
        initializeUnitBox(atmosHeightUnit, atmosHeightUnitOptions, Unit.LENGTH);

        previousMassUnit = ScalarFactory.getBaseUnitSize(Unit.MASS);
        previousZeroElevationUnit = ScalarFactory.getBaseUnitSize(Unit.LENGTH);
        previousPositionUnit = ScalarFactory.getBaseUnitSize(Unit.LENGTH);
        previousVelocityUnit = ScalarFactory.getBaseUnitSize(Unit.VELOCITY);
        previousAtmosHeightUnit = ScalarFactory.getBaseUnitSize(Unit.LENGTH);
    }

    // converts scalar to size set in unitsizeBox and outputs it to the field as String
    void fillScalarToField(Scalar scalar, TextField field, ComboBox<String> unitsizeBox) {
        field.setText(ScalarFactory.convert(scalar, unitsizeBox.getSelectionModel().getSelectedItem()).doubleValue().toString());
    }

    // convert and transfer data from the planetoid object to the gui fields
    void moveDataFromObjectToGui() {
        name.setText(planetoidToEdit.getName());
        description.setText(planetoidToEdit.getDescription());

        fillScalarToField(planetoidToEdit.getMass(), mass, massUnit);
        fillScalarToField(planetoidToEdit.getZeroElevation(), zeroElevation, zeroElevationUnit);

        fillScalarToField(planetoidToEdit.getPosition().getX(), posX, positionUnit);
        fillScalarToField(planetoidToEdit.getPosition().getY(), posY, positionUnit);
        fillScalarToField(planetoidToEdit.getPosition().getZ(), posZ, positionUnit);
        fillScalarToField(planetoidToEdit.getPosition().getLength(), posMagnitude, positionUnit);

        fillScalarToField(planetoidToEdit.getVelocity().getX(), velX, velocityUnit);
        fillScalarToField(planetoidToEdit.getVelocity().getY(), velY, velocityUnit);
        fillScalarToField(planetoidToEdit.getVelocity().getZ(), velZ, velocityUnit);
        fillScalarToField(planetoidToEdit.getVelocity().getLength(), velMagnitude, velocityUnit);

        atmosModel.getSelectionModel().select(planetoidToEdit.getAtmosphereModel());
        fillScalarToField(planetoidToEdit.getAtmosphereHeight(), atmosHeight, atmosHeightUnit);
        atmosOxygenPercentage.setText(planetoidToEdit.getOxygenPercentage().getValue().doubleValue().toString());
    }

    // tries to convert field to scalar, throws descriptive exception with field id if fail
    Scalar fillFieldToScalar(TextField field, Unit unit, String unitsize, String fieldPrefixForError) throws UnitConversionError, NumberFormatException {
        try {
            return ScalarFactory.create(new Quad(Double.parseDouble(field.getText())), unit, unitsize);
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(String.format("Text '%s' in field '%s%s' can not be parsed to a number", field.getText(), fieldPrefixForError, field.getId()));
        } catch (UnitConversionError ex) {
            throw new UnitConversionError(String.format("Text '%s' in field '%s%s' can not be converted from %s, %s to base unitsize", field.getText(), fieldPrefixForError, field.getId(), unit.toString(), unitsize));
        }
    }

    // overload without field prefix
    Scalar fillFieldToScalar(TextField field, Unit unit, String unitsize) throws UnitConversionError, NumberFormatException {
        return fillFieldToScalar(field, unit, unitsize, "");
    }

    Vector fillFieldsToVector(TextField x, TextField y, TextField z, Unit unit, String unitsize, String vectorName) throws UnitConversionError, NumberFormatException {
        Scalar sx = fillFieldToScalar(x, unit, unitsize, vectorName);
        Scalar sy = fillFieldToScalar(y, unit, unitsize, vectorName);
        Scalar sz = fillFieldToScalar(z, unit, unitsize, vectorName);

        return new BaseVector(sx, sy, sz);
    }

    // convert and transfer data from the gui fields to the planetoid object
    void moveDataFromGuiToObject() throws UnitConversionError, NumberFormatException {
        planetoidToEdit.setName(name.getText());
        planetoidToEdit.setDescription(description.getText());
        
        planetoidToEdit.setMass(fillFieldToScalar(mass, Unit.MASS, previousMassUnit));
        planetoidToEdit.setZeroElevation(fillFieldToScalar(zeroElevation, Unit.LENGTH, previousZeroElevationUnit));

        planetoidToEdit.setPosition(fillFieldsToVector(posX, posY, posZ, Unit.LENGTH, previousPositionUnit, VECTOR_NAME_POSITION));
        planetoidToEdit.setVelocity(fillFieldsToVector(velX, velY, velZ, Unit.VELOCITY, previousVelocityUnit, VECTOR_NAME_VELOCITY));

        // TODO atmos model
        planetoidToEdit.setAtmosphereHeight(fillFieldToScalar(atmosHeight, Unit.LENGTH, previousAtmosHeightUnit));
        // TODO oxygen percentage
    }

    // converts field text from one unitsize to another via ScalarFactory
    void convertUnit(TextField field, ComboBox<String> unitsizeBox, Unit unit, String previousUnitsize) {
        try {
            Quad oldValue = new Quad(Double.parseDouble(field.getText()));

            // use factory to convert between old and new unitsize
            Quad newValue = ScalarFactory.convert(unit, oldValue, previousUnitsize, unitsizeBox.getSelectionModel().getSelectedItem());
            field.setText(newValue.doubleValue().toString());
        } catch (Exception ex) {
            log.warning(String.format("Conversion between old %s and new %s unit sizes impossible", previousUnitsize, unitsizeBox.getSelectionModel().getSelectedItem()));
        }
    }

    // user changed the unitsize, convert value
    @FXML
    void massUnitChanged(ActionEvent e) {
        convertUnit(mass, massUnit, Unit.MASS, previousMassUnit);
        previousMassUnit = massUnit.getSelectionModel().getSelectedItem();
    }

    // user changed the unitsize, convert value
    @FXML
    void zeroElevationUnitChanged(ActionEvent e) {
        convertUnit(zeroElevation, zeroElevationUnit, Unit.LENGTH, previousZeroElevationUnit);
        previousZeroElevationUnit = zeroElevationUnit.getSelectionModel().getSelectedItem();
    }

    // user changed the unitsize, convert value
    @FXML
    void positionUnitChanged(ActionEvent e) {
        convertUnit(posX, positionUnit, Unit.LENGTH, previousPositionUnit);
        convertUnit(posY, positionUnit, Unit.LENGTH, previousPositionUnit);
        convertUnit(posZ, positionUnit, Unit.LENGTH, previousPositionUnit);
        convertUnit(posMagnitude, positionUnit, Unit.LENGTH, previousPositionUnit);
        previousPositionUnit = positionUnit.getSelectionModel().getSelectedItem();
    }

    // user changed the unitsize, convert value
    @FXML
    void velocityUnitChanged(ActionEvent e) {
        convertUnit(velX, velocityUnit, Unit.VELOCITY, previousVelocityUnit);
        convertUnit(velY, velocityUnit, Unit.VELOCITY, previousVelocityUnit);
        convertUnit(velZ, velocityUnit, Unit.VELOCITY, previousVelocityUnit);
        convertUnit(velMagnitude, velocityUnit, Unit.VELOCITY, previousVelocityUnit);
        previousVelocityUnit = velocityUnit.getSelectionModel().getSelectedItem();
    }

    // user changed the unitsize, convert value
    @FXML
    void atmosHeightChanged(ActionEvent e) {
        convertUnit(atmosHeight, atmosHeightUnit, Unit.LENGTH, previousAtmosHeightUnit);
        previousAtmosHeightUnit = atmosHeightUnit.getSelectionModel().getSelectedItem();
    }

    // user changed the atmospheric model
    @FXML
    void atmosModelChanged(ActionEvent e) {
        askYesNo("You like tests?");
    }

    // user cancels editing
    @FXML
    void cancelClicked(ActionEvent e) {
        getCurrentStage(e).close();
    }

    // user saves editing
    @FXML
    void okClicked(ActionEvent e) {
        try {
            moveDataFromGuiToObject();
            getCurrentStage(e).close();
        } catch (UnitConversionError | NumberFormatException ex) {
            showError("Can not save planetoid!\n"+ex.toString());
        }
    }

    // shorthand conversion between position fields to magnitude field, the unit and unitsize is unused in this case because it has no impact on the calculation
    void updateMagnitudeFromPositions(TextField x, TextField y, TextField z, TextField m) throws UnitConversionError, NumberFormatException {
        Vector vec = new BaseVector(Double.parseDouble(x.getText()), Double.parseDouble(y.getText()), Double.parseDouble(z.getText()));
        m.setText(vec.getLength().getValue().doubleValue().toString());
    }

    // shorthand conversion between magnitude field to position fields, the unit and unitsize is unused in this case because it has no impact on the calculation
    void updatePositionsFromMagnitude(TextField x, TextField y, TextField z, TextField m) throws UnitConversionError, NumberFormatException {
        // make a normalized unitvector to get direction
        Vector vec = new BaseVector(Double.parseDouble(x.getText()), Double.parseDouble(y.getText()), Double.parseDouble(z.getText()));
        Vector normalized = vec.normalize();

        // multiply (scale) by magnitude to get back new positions
        Double magnitude = Double.parseDouble(m.getText());
        Double xValue = normalized.getX().getValue().doubleValue() * magnitude;
        Double yValue = normalized.getY().getValue().doubleValue() * magnitude;
        Double zValue = normalized.getZ().getValue().doubleValue() * magnitude;

        // fill into fields
        x.setText(xValue.toString());
        y.setText(yValue.toString());
        z.setText(zValue.toString());
    }

    // user types in position dimension, update magnitude
    @FXML
    void updatePosMagnitude(KeyEvent e) {
        try {
            updateMagnitudeFromPositions(posX, posY, posZ, posMagnitude);
        } catch (UnitConversionError | NumberFormatException ex) {
            // user could still be typing, ignore error to not distract him
        }
    }

    // user types in position magnitude, scale dimensions
    @FXML
    void updatePosVector(KeyEvent e) {
        try {
            updatePositionsFromMagnitude(posX, posY, posZ, posMagnitude);
        } catch (UnitConversionError | NumberFormatException ex) {
            // user could still be typing, ignore error to not distract him
        }
    }

    // user types in velocity dimension, update magnitude
    @FXML
    void updateVelMagnitude(KeyEvent e) {
        try {
            updateMagnitudeFromPositions(velX, velY, velZ, velMagnitude);
        } catch (UnitConversionError | NumberFormatException ex) {
            // user could still be typing, ignore error to not distract him
        }
    }

    // user types in velocity magnitude, scale dimensions
    @FXML
    void updateVelVector(KeyEvent e) {
        try {
            updatePositionsFromMagnitude(velX, velY, velZ, velMagnitude);
        } catch (UnitConversionError | NumberFormatException ex) {
            // user could still be typing, ignore error to not distract him
        }
    }
}
