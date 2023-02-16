package ch.hftm.astrodynamic.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import ch.hftm.astrodynamic.model.Mission;
import ch.hftm.astrodynamic.model.conditions.Approach;
import ch.hftm.astrodynamic.model.conditions.Avoid;
import ch.hftm.astrodynamic.model.conditions.Condition;
import ch.hftm.astrodynamic.model.conditions.Depart;
import ch.hftm.astrodynamic.model.conditions.HoldoutTime;
import ch.hftm.astrodynamic.model.conditions.MaximumTime;
import ch.hftm.astrodynamic.model.conditions.SetupHeavyLander;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.scalar.TimeScalar;
import ch.hftm.astrodynamic.utils.BaseScalar;
import ch.hftm.astrodynamic.utils.Log;
import ch.hftm.astrodynamic.utils.MissionRepository;
import ch.hftm.astrodynamic.utils.Named;
import ch.hftm.astrodynamic.utils.Quad;
import ch.hftm.astrodynamic.utils.Scalar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

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

    ObservableList<Class> possibleConditions;

    ObservableList<Condition> conditions;

    ObservableList<String> possibleUnits;
    String lastSelectedUnitsize;

    ObservableList<Named> possibleConditionRelationObjects;

    Mission editedMission;

    public MissionEditController() {
        super();
    }

    @Override
    public void initialize(){

        conditions = FXCollections.observableArrayList();
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
    }

    // test data, missions would be stored on disk
    private void initializeMissionData() {
        editedMission = MissionRepository.getActiveMission();

        if (editedMission.getPlanetoidNames().size() < 1) {
            editedMission.setupStandardSolarSystem();
        }

        for (Condition c: editedMission.getConditions()) {
            conditions.add(c);
        }
        
        possibleConditions.add(MaximumTime.class);
        possibleConditions.add(HoldoutTime.class);
        possibleConditions.add(Approach.class);
        possibleConditions.add(Avoid.class);
        possibleConditions.add(Depart.class);
        possibleConditions.add(SetupHeavyLander.class);

        possibleConditionRelationObjects.addAll(editedMission.getAllNamedAstronomicalObjects());
    }

    private void missionDataToFields() {
        missionName.setText(editedMission.getName());
        missionDescription.setHtmlText(editedMission.getDescription());
    }

    private void fieldsToMissionData() {
        editedMission.setName(missionName.getText());
        editedMission.setDescription(missionDescription.getHtmlText());
    }

    private void hideNewConditionRelation() {
        newConditionInRelationLabel.setVisible(false);
        newConditionObject.setVisible(false);
    }

    private void showNewConditionRelation() {
        newConditionInRelationLabel.setVisible(true);
        newConditionObject.setVisible(true);
    }

    private void hideNewChoiceInput() {
        newUnitsize.setVisible(false);
        newConditionParameter.setVisible(false);
    }

    private void showNewChoiceInput(String defaultUnitsize, String[] unitsizes) {
        newUnitsize.setVisible(true);
        possibleUnits.clear();
        possibleUnits.addAll(unitsizes);
        newUnitsize.getSelectionModel().select(defaultUnitsize);
        newConditionParameter.setVisible(true);
        //newConditionParameter.setText(""); // to clear

        lastSelectedUnitsize = newUnitsize.getSelectionModel().getSelectedItem();
    }

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

    @FXML
    void addChoiceToList(ActionEvent e) {
        Class selConditionClass = newCondition.getSelectionModel().getSelectedItem();
        
        Parameter firstParam = getCurrentChoiceParameter(getCurrentChoiceConstructor(true), false, 0);

        Parameter secondParam = getCurrentChoiceParameter(getCurrentChoiceConstructor(true), false, 1);

        Scalar scalarParam = null;
        Named referenceObject = null;

        List<Object> instanceParams = new ArrayList<>();

        if (firstParam != null) {
            try {
                scalarParam = ScalarFactory.create(Double.parseDouble(newConditionParameter.getText()), ScalarFactory.getUnitFromClass(firstParam.getType()), lastSelectedUnitsize);
                instanceParams.add(scalarParam);
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        }

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

        try {
            Condition condition = (Condition)selConditionClass.getConstructors()[0].newInstance(instanceParams.toArray(new Object[0]));
            conditions.add(condition);
            editedMission.addCondition(condition);
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
            Quad newValue = ScalarFactory.convert(ScalarFactory.getUnitFromClass(firstParam.getType()), oldValue, lastSelectedUnitsize, newUnitsize.getSelectionModel().getSelectedItem());
            newConditionParameter.setText(newValue.doubleValue().toString());
        } catch (Exception ex) {
            log.warning("Conversion between old and new unit sizes impossible");
        }

        lastSelectedUnitsize = newUnitsize.getSelectionModel().getSelectedItem();
    }

    @FXML
    void saveMission(ActionEvent e) {
        fieldsToMissionData();
        getCurrentStage(e).close();
    }
}
