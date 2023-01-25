package ch.hftm.astrodynamic.controller;

import java.lang.reflect.Parameter;

import ch.hftm.astrodynamic.model.conditions.BaseCondition;
import ch.hftm.astrodynamic.model.conditions.HoldoutTime;
import ch.hftm.astrodynamic.model.conditions.MaximumTime;
import ch.hftm.astrodynamic.scalar.TimeScalar;
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
    Label unitLabel;

    @FXML
    ListView<BaseCondition> missionConditions;

    ObservableList<Class> possibleConditions;

    ObservableList<BaseCondition> conditions;

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

        hideNewChoiceInput();

        initializeTestdata();
    }

    // test data, missions would be stored on disk
    private void initializeTestdata() {
        possibleConditions = FXCollections.observableArrayList();

        possibleConditions.add(MaximumTime.class);
        possibleConditions.add(HoldoutTime.class);

        newCondition.setItems(possibleConditions);
    }

    private void hideNewChoiceInput() {
        unitLabel.setVisible(false);
        newConditionParameter.setVisible(false);
    }

    private void showNewChoiceInput(String unit) {
        unitLabel.setVisible(true);
        unitLabel.setText(unit);
        newConditionParameter.setVisible(true);
        //newConditionParameter.setText(""); // to clear
    }

    // 
    @FXML
    void newConditionChoice(ActionEvent e) {
        System.out.println("new condition " + e.toString());
        Class selConditionClass = newCondition.getSelectionModel().getSelectedItem();
        Parameter firstParam = selConditionClass.getConstructors()[0].getParameters()[0];
        System.out.println(firstParam.toString());

        hideNewChoiceInput();

        if (firstParam.getType() == TimeScalar.class) {
            showNewChoiceInput("seconds");
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
}
