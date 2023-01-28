package ch.hftm.astrodynamic.controller;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;

import ch.hftm.astrodynamic.model.conditions.Altitude;
import ch.hftm.astrodynamic.model.conditions.BaseCondition;
import ch.hftm.astrodynamic.model.conditions.HoldoutTime;
import ch.hftm.astrodynamic.model.conditions.MaximumTime;
import ch.hftm.astrodynamic.scalar.ScalarFactory;
import ch.hftm.astrodynamic.scalar.TimeScalar;
import ch.hftm.astrodynamic.utils.BaseScalar;
import ch.hftm.astrodynamic.utils.Scalar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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
    ListView<BaseCondition> missionConditions;

    ObservableList<Class> possibleConditions;

    ObservableList<BaseCondition> conditions;

    ObservableList<String> possibleUnits;

    public MissionEditController() {
        super();
    }

    @Override
    protected Stage getCurrentStage() {
        return null;
    }

    @Override
    public void initialize(){

        conditions = FXCollections.observableArrayList();

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

        // active conditions for the mission
        missionConditions.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(BaseCondition item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getDescription());
                }
            }
        });
        missionConditions.setItems(conditions);

        possibleUnits = FXCollections.observableArrayList();
        newUnitsize.setItems(possibleUnits);

        hideNewChoiceInput();

        initializeTestdata();
    }

    // test data, missions would be stored on disk
    private void initializeTestdata() {
        possibleConditions = FXCollections.observableArrayList();

        possibleConditions.add(MaximumTime.class);
        possibleConditions.add(HoldoutTime.class);
        possibleConditions.add(Altitude.class);

        newCondition.setItems(possibleConditions);
    }

    private void hideNewChoiceInput() {
        newUnitsize.setVisible(false);
        newConditionParameter.setVisible(false);
    }

    private void showNewChoiceInput(String defaultUnitsize, String[] unitsizes) {
        System.out.println(unitsizes);

        newUnitsize.setVisible(true);
        possibleUnits.clear();
        possibleUnits.addAll(unitsizes);
        newUnitsize.getSelectionModel().select(defaultUnitsize);
        newConditionParameter.setVisible(true);
        //newConditionParameter.setText(""); // to clear
    }

    // 
    @FXML
    void newConditionChoice(ActionEvent e) {
        System.out.println("new condition " + e.toString());

        // check if we have a constructor else error
        Class selConditionClass = newCondition.getSelectionModel().getSelectedItem();
        Constructor[] constructors = selConditionClass.getConstructors();
        if (constructors.length < 1) {
            showError("No public constructor found in class " + selConditionClass.getName());
            return;
        }

        // check if one of the constructors matches our parameter requirements else error
        Constructor selectedConstructor = constructors[0];
        boolean correctConstructorFound = false;
        for (Constructor c: constructors) {
            if (c.getParameterCount() == 1) {
                selectedConstructor = c;
                correctConstructorFound = true;
                break;
            }
        }
        if (!correctConstructorFound) {
            showError("No matching public constructor found in class " + selConditionClass.getName());
            return;
        }

        hideNewChoiceInput();

        Parameter[] parameters = selectedConstructor.getParameters();
        if (parameters.length > 0) {
            Parameter firstParam = parameters[0];
            System.out.println(firstParam.toString());

            // if it is a scalar ask the factory to determine units
            //if (firstParam.getType().isInstance(Scalar.class)) {
            showNewChoiceInput(ScalarFactory.getBaseUnitSize(firstParam.getType()), ScalarFactory.getUnitSizes(firstParam.getType()));
            //}
        }
    }

    @FXML
    void addChoiceToList(ActionEvent e) {
        Class selConditionClass = newCondition.getSelectionModel().getSelectedItem();
        Parameter firstParam = selConditionClass.getConstructors()[0].getParameters()[0];
        Object paramObject = new Object();

        if (firstParam.getType() == TimeScalar.class) {
            paramObject = (Object)new TimeScalar(Integer.parseInt(newConditionParameter.getText()));
        }

        try {
            conditions.add((BaseCondition)selConditionClass.getConstructors()[0].newInstance(paramObject));
        } catch (Exception ex) {
            showError(e.toString());
        }
    }

    @FXML
    void newUnitsizeChanged(ActionEvent e) {
        System.out.println(e.toString());
    }
}
