package com.project.controller;

import java.util.Map;

import com.project.util.ProjecUtilContext;
import com.project.util.StringUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * <pre>
 * <b>Description : </b>
 * FilterController.
 * 
 * @version $Revision: 1 $ $Date: Oct 16, 2017 8:15:49 PM $
 * @author $Author: pritam.ghosh $ 
 * </pre>
 */
public class FilterController {
    private boolean isEdit;
    HomeScreenController controller;
    @FXML
    private Button actionFilterButton;
    @FXML
    private TextField filterName;
    @FXML
    private TextField filterProperties;
    @FXML
    private Label moduleTite;
    @FXML
    private ComboBox<String> filterCombo;

    public final void setEdit(boolean isEdit) {
        filterCombo.setVisible(isEdit);
        filterName.setVisible(!isEdit);
        if (isEdit) {
            moduleTite.setText("Edit Filter");
            actionFilterButton.setText("Edit Filter");
            if (ProjecUtilContext.getFilters() != null) {
                filterCombo.getItems().addAll(ProjecUtilContext.getFilters().keySet());
                filterCombo.getSelectionModel().selectFirst();
                filterComboAction();
            }
        }
        this.isEdit = isEdit;
    }

    public void filterAction() {
        if ((StringUtils.isNotEmpty(filterName.getText())
            || StringUtils.isNotEmpty(filterCombo.getSelectionModel().getSelectedItem()))
            && StringUtils.isNotEmpty(filterProperties.getText())) {
            if (isEdit) {
                ProjecUtilContext.addFilter(filterCombo.getSelectionModel().getSelectedItem(),
                    filterProperties.getText());
            }
            else {
                ProjecUtilContext.addFilter(filterName.getText(), filterProperties.getText());
            }
            closeWindow();
        }
    }

    private void closeWindow() {
        Stage currentStage = (Stage) actionFilterButton.getScene().getWindow();
        controller.refresh();
        currentStage.close();
    }

    public final void setController(HomeScreenController controller) {
        this.controller = controller;
    }

    public void filterComboAction() {
        Map<String, String> filters = ProjecUtilContext.getFilters();
        if (filters != null) {
            filterProperties.setText(filters.get(filterCombo.getSelectionModel().getSelectedItem()));
        }
    }
}
