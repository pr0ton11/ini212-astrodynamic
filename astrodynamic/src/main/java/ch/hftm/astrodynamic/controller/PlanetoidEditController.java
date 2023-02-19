package ch.hftm.astrodynamic.controller;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import ch.hftm.astrodynamic.physics.AtmosphereModel;
import ch.hftm.astrodynamic.physics.Planetoid;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.utils.MissionRepository;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.Unit;
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

    @FXML
    ComboBox<String> zeroElevationUnit;
    ObservableList<String> zeroElevationUnitOptions;

    @FXML
    ComboBox<String> positionUnit;
    ObservableList<String> positionUnitOptions;

    @FXML
    ComboBox<String> velocityUnit;
    ObservableList<String> velocityUnitOptions;

    @FXML
    ComboBox<String> atmosHeightUnit;
    ObservableList<String> atmosHeightUnitOptions;

    Planetoid planetoidToEdit;

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

    // user changed the unitsize, convert value
    @FXML
    void massUnitChanged(ActionEvent e) {
        askYesNo("You like tests?");
    }

    // user changed the unitsize, convert value
    @FXML
    void zeroElevationUnitChanged(ActionEvent e) {
        askYesNo("You like tests?");
    }

    // user changed the unitsize, convert value
    @FXML
    void positionUnitChanged(ActionEvent e) {
        askYesNo("You like tests?");
    }

    // user changed the unitsize, convert value
    @FXML
    void velocityUnitChanged(ActionEvent e) {
        askYesNo("You like tests?");
    }

    // user changed the unitsize, convert value
    @FXML
    void atmosHeightChanged(ActionEvent e) {
        askYesNo("You like tests?");
    }

    // user changed the atmospheric model
    @FXML
    void atmosModelChanged(ActionEvent e) {
        askYesNo("You like tests?");
    }

    // user cancels editing
    @FXML
    void cancelClicked(ActionEvent e) {
        askYesNo("You like tests?");
    }

    // user saves editing
    @FXML
    void okClicked(ActionEvent e) {
        askYesNo("You like tests?");
    }
}
