package ch.hftm.astrodynamic.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;

import ch.hftm.astrodynamic.model.Mission;
import ch.hftm.astrodynamic.model.conditions.Approach;
import ch.hftm.astrodynamic.model.conditions.Avoid;
import ch.hftm.astrodynamic.model.conditions.Condition;
import ch.hftm.astrodynamic.model.conditions.Depart;
import ch.hftm.astrodynamic.model.conditions.HoldoutTime;
import ch.hftm.astrodynamic.model.conditions.MaximumTime;
import ch.hftm.astrodynamic.model.conditions.SetupHeavyLander;
import ch.hftm.astrodynamic.model.conditions.SetupISS;
import ch.hftm.astrodynamic.physics.Planetoid;
import ch.hftm.astrodynamic.physics.Spaceship;
import ch.hftm.astrodynamic.physics.AtmosphereModel;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.scalar.LengthScalar;
import ch.hftm.astrodynamic.scalar.MassScalar;
import ch.hftm.astrodynamic.scalar.UnitlessScalar;
import ch.hftm.astrodynamic.utils.Log;
import ch.hftm.astrodynamic.utils.MissionRepository;
import ch.hftm.astrodynamic.utils.Named;
import ch.hftm.astrodynamic.utils.Quad;
import ch.hftm.astrodynamic.utils.Scalar;
import ch.hftm.astrodynamic.utils.BaseVector;
import ch.hftm.astrodynamic.utils.Unit;

/*
 *  Project Astrodynamic
 *  HFTM BBIN21.2a
 *  Rafael Stauffer, Marc Singer
 */

 // mission edit handles modification of parameters and conditions of a mission
public class MissionEditController extends BaseController{

    private Logger log = Log.build();

    @FXML
    TextField missionName;

    @FXML
    HTMLEditor missionDescription;

    @FXML
    ComboBox<Class> newCondition;

    @FXML
    TextField newConditionParameter;

    @FXML
    ComboBox<String> newUnitsize;

    @FXML
    ListView<Condition> missionConditions;

    @FXML
    Label newConditionInRelationLabel;

    @FXML
    ComboBox<Named> newConditionObject;

    @FXML
    ComboBox<Spaceship> playerSpaceship;

    ObservableList<Class> possibleConditions;

    ObservableList<Condition> conditions;

    ObservableList<String> possibleUnits; // unit sizes for current condition parameter entry
    String lastSelectedUnitsize; // save last unit size for conversion calculations

    ObservableList<Named> possibleConditionRelationObjects; // object which could be used as relation to a condition in the add new condition dropdown

    @FXML
    ListView<Planetoid> missionPlanetoids;
    ObservableList<Planetoid> planetoids;

    @FXML
    ListView<Spaceship> missionSpaceships;
    ObservableList<Spaceship> spaceships;

    Mission editedMission;

    public MissionEditController() {
        super();
    }

    @Override
    public void initialize(){

        conditions = FXCollections.observableArrayList();
        planetoids = FXCollections.observableArrayList();
        spaceships = FXCollections.observableArrayList();
        
        possibleConditions = FXCollections.observableArrayList();
        possibleUnits = FXCollections.observableArrayList();
        possibleConditionRelationObjects = FXCollections.observableArrayList();

        initializeMissionData();
        missionDataToFields();

        // dropdown for possible conditions
        newCondition.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Class item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getSimpleName());
                }
            }
        });
        newCondition.setItems(possibleConditions);

        // active conditions for the mission
        missionConditions.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Condition item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDescription());
                }
            }
        });
        missionConditions.setItems(conditions);

        newUnitsize.setItems(possibleUnits);

        newConditionObject.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Named item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        newConditionObject.setItems(possibleConditionRelationObjects);

        hideNewChoiceInput();
        hideNewConditionRelation();

        missionPlanetoids.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Planetoid item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        missionPlanetoids.setItems(planetoids);

        missionSpaceships.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Spaceship item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        missionSpaceships.setItems(spaceships);

        // player spaceship has the same observable list as the mission spaceship list
        playerSpaceship.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Spaceship item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName());
                }
            }
        });
        playerSpaceship.setItems(spaceships);

        if (editedMission.getPlayerControlledVessel() != null) {
            playerSpaceship.setValue(editedMission.getPlayerControlledVessel()); // TODO: fix this non working
        }

        updateUnobservedListsFromObject();
    }

    // planetoid and spaceship changes are unobservable from gui, refresh them here
    private void updateUnobservedListsFromObject() {
        planetoids.clear();
        // load existing planetoids into gui list
        for (Planetoid p: editedMission.getPlanetoids()) {
            planetoids.add(p);
        }

        spaceships.clear();
        for (Spaceship s: editedMission.getSpaceships()) {
            spaceships.add(s);
        }

        possibleConditionRelationObjects.clear();
        possibleConditionRelationObjects.addAll(editedMission.getAllNamedAstronomicalObjects());
    }

    // test data, missions would be stored on disk
    private void initializeMissionData() {
        editedMission = MissionRepository.getActiveMission();

        // if we have no planetoids in the mission, add default planetoids
        if (editedMission.getPlanetoidNames().size() < 1) {
            log.info("Mission has no planetoids, setting up standard planets");
            editedMission.setupStandardSolarSystem();
        }

        // load existing conditions into gui list
        for (Condition c: editedMission.getConditions()) {
            conditions.add(c);
        }
        
        possibleConditions.add(MaximumTime.class);
        possibleConditions.add(HoldoutTime.class);
        possibleConditions.add(Approach.class);
        possibleConditions.add(Avoid.class);
        possibleConditions.add(Depart.class);
        possibleConditions.add(SetupHeavyLander.class);
        possibleConditions.add(SetupISS.class);

        updateUnobservedListsFromObject();
    }

    // name and description from mission object to gui fields
    private void missionDataToFields() {
        missionName.setText(editedMission.getName());
        missionDescription.setHtmlText(editedMission.getDescription());
    }

    // name and description from gui fields to mission object
    private void fieldsToMissionData() {
        editedMission.setName(missionName.getText());
        editedMission.setDescription(missionDescription.getHtmlText());
    }

    // hide condition relation parameter fields
    private void hideNewConditionRelation() {
        newConditionInRelationLabel.setVisible(false);
        newConditionObject.setVisible(false);
    }

    // show condition relation parameter fields
    private void showNewConditionRelation() {
        newConditionInRelationLabel.setVisible(true);
        newConditionObject.setVisible(true);
    }

    // hide condition unit value parameter fields
    private void hideNewChoiceInput() {
        newUnitsize.setVisible(false);
        newConditionParameter.setVisible(false);
    }

    // show condition unit value parameter fields with supported unitsizes in the dropdown
    private void showNewChoiceInput(String defaultUnitsize, String[] unitsizes) {
        newUnitsize.setVisible(true);
        possibleUnits.clear();
        possibleUnits.addAll(unitsizes);
        newUnitsize.getSelectionModel().select(defaultUnitsize);
        newConditionParameter.setVisible(true);
        //newConditionParameter.setText(""); // to clear

        lastSelectedUnitsize = newUnitsize.getSelectionModel().getSelectedItem();
    }

    // reflection get the constructor of the condition
    Constructor getCurrentChoiceConstructor(boolean showErrorMsg) {
        // check if we have a constructor else error
        Class selConditionClass = newCondition.getSelectionModel().getSelectedItem();
        Constructor[] constructors = selConditionClass.getConstructors();
        if (constructors.length < 1) {
            if (showErrorMsg)
                showError("No public constructor found in class " + selConditionClass.getName());
            return null;
        }
        return constructors[0];
    }

    // reflection get the parameter of a condition constructor on position paramPosition
    Parameter getCurrentChoiceParameter(Constructor constructor, boolean showErrorMsg, int paramPosition) {
        Parameter[] parameters = constructor.getParameters();
        if (parameters.length - 1 < paramPosition) {
            if (showErrorMsg)
                showError("No matching parameters found in" + constructor.getName());
            return null;
        }

        return parameters[paramPosition];
    }

    // changed new condition dropdown choice, update parameter and unitsize
    @FXML
    void newConditionChoice(ActionEvent e) {
        Parameter firstParam = getCurrentChoiceParameter(getCurrentChoiceConstructor(true), false, 0);

        hideNewChoiceInput();

        if (firstParam != null) {
            // if it is a scalar ask the factory to determine units
            //if (firstParam.getType().isInstance(Scalar.class)) {
            showNewChoiceInput(ScalarFactory.getBaseUnitSize(firstParam.getType()), ScalarFactory.getUnitSizes(firstParam.getType()));
            //}
        }

        lastSelectedUnitsize = newUnitsize.getSelectionModel().getSelectedItem();

        Parameter secondParam = getCurrentChoiceParameter(getCurrentChoiceConstructor(true), false, 1);

        hideNewConditionRelation();

        if (secondParam != null) {
            if (secondParam.getType() == Named.class) {
                showNewConditionRelation();
            }
        }
    }

    // user clicked add condition, initialize the condition with the parameters, add to the mission, show in the gui condition list
    @FXML
    void addChoiceToList(ActionEvent e) {
        Class selConditionClass = newCondition.getSelectionModel().getSelectedItem();
        
        Parameter firstParam = getCurrentChoiceParameter(getCurrentChoiceConstructor(true), false, 0);

        Parameter secondParam = getCurrentChoiceParameter(getCurrentChoiceConstructor(true), false, 1);

        Scalar scalarParam = null;
        Named referenceObject = null;

        List<Object> instanceParams = new ArrayList<>();

        // if we have a first parameter this is a scalar value, create and add it to the instanceParams
        if (firstParam != null) {
            try {
                scalarParam = ScalarFactory.create(Double.parseDouble(newConditionParameter.getText()), ScalarFactory.getUnitFromClass(firstParam.getType()), lastSelectedUnitsize);
                instanceParams.add(scalarParam);
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }

        // if we have a second parameter this is a reference, get it and add it to the instanceParams
        if (secondParam != null) {
            try {
                if (secondParam.getType() == Named.class) {
                    referenceObject = newConditionObject.getSelectionModel().getSelectedItem();
                    instanceParams.add(referenceObject);
                }
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }

        // create the new condition, add to the mission, add to the gui list
        try {
            Condition condition = (Condition)selConditionClass.getConstructors()[0].newInstance(instanceParams.toArray(new Object[0]));
            conditions.add(condition);
            editedMission.addCondition(condition);
            updateUnobservedListsFromObject(); // condition might changed something in planetoids/spaceships, update listviews
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    // if the unitsize is switched we try to convert between old and new unitsize, if it fails we ignore it
    @FXML
    void newUnitsizeChanged(ActionEvent e) {
        Parameter firstParam = getCurrentChoiceParameter(getCurrentChoiceConstructor(true), false, 0);

        try {
            Quad oldValue = new Quad(Double.parseDouble(newConditionParameter.getText()));

            // use factory to convert between old and new unitsize
            Quad newValue = ScalarFactory.convert(ScalarFactory.getUnitFromClass(firstParam.getType()), oldValue, lastSelectedUnitsize, newUnitsize.getSelectionModel().getSelectedItem());
            newConditionParameter.setText(newValue.doubleValue().toString());
        } catch (Exception ex) {
            log.warning("Conversion between old and new unit sizes impossible");
        }

        lastSelectedUnitsize = newUnitsize.getSelectionModel().getSelectedItem();
    }

    // user clicked save, update mission from gui fields and exit edit window
    @FXML
    void saveMission(ActionEvent e) {
        fieldsToMissionData();
        getCurrentStage(e).close();
    }

    // check if we have a valid planetoid selected else display error
    boolean checkPlanetoidSelected() {
        if (missionPlanetoids.getSelectionModel().getSelectedItem() == null) {
            showError("No planetoid selected in list. Please select planetoid first");
            return false;
        }
        return true;
    }

    // user clicked on edit button under planetoids, open edit planetoid window if planetoid selected
    @FXML
    void editPlanetoid(ActionEvent e) {
        if (!checkPlanetoidSelected()) return;

        editedMission.setReferencePlanetoid(missionPlanetoids.getSelectionModel().getSelectedItem());

        showSceneOnNewStage("Planetoid Editor - " + missionPlanetoids.getSelectionModel().getSelectedItem().getName(), false, "view/PlanetoidEditView.fxml");
    }

    // usr clicked delete planetoids: if we have one selected ask yes/no and delete if clicked yes
    @FXML
    void deletePlanetoid(ActionEvent e) {
        if (!checkPlanetoidSelected()) return;

        Planetoid selectedPlanetoid = missionPlanetoids.getSelectionModel().getSelectedItem();
        if (askYesNo(String.format("Do you really want to delete planetoid '%s'?", selectedPlanetoid.getName()))) {
            editedMission.removeAstronomicalObjectByName(selectedPlanetoid.getName());
            updateUnobservedListsFromObject();
        }
    }

    // add new planetoid: create new planetoid to set as reference for editing
    @FXML
    void newPlanetoid(ActionEvent e) {

        Planetoid tempPlanetoid = new Planetoid(
            new LengthScalar(new Quad(1.0, 0)),
            new MassScalar(new Quad(1.0, 0)), 
            new BaseVector(Unit.LENGTH), 
            new BaseVector(Unit.ANGLE), 
            new BaseVector(Unit.VELOCITY), 
            new BaseVector(Unit.ANGULAR_VELOCITY),
            new LengthScalar(new Quad(1.0, 0)),
            AtmosphereModel.QUADRATIC_FALLOFF,
            new UnitlessScalar(0.0)
        );

        tempPlanetoid.setName("New Planetoid");

        editedMission.addPlanetoid(tempPlanetoid);
        editedMission.setReferencePlanetoid(tempPlanetoid);

        showSceneOnNewStage("Planetoid Editor - " + tempPlanetoid.getName(), false, "view/PlanetoidEditView.fxml");
    }

    // user has chosen a spaceship for the player in the dropdown, set in mission
    @FXML
    void setPlayerSpaceship(ActionEvent e) {
        editedMission.setPlayerControlledVessel(playerSpaceship.getSelectionModel().getSelectedItem());
    }
}
