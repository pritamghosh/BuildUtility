package com.project.controller;

import com.project.dto.ProjectDO;
import com.project.util.BuildUtilityContextUtil;
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
 * RemoveController.
 * 
 * @version $Revision: 1 $ $Date: Oct 16, 2017 8:16:30 PM $
 * @author $Author: pritam.ghosh $ 
 * </pre>
 */
public class RemoveController {
    @FXML
    private Button actionButton;
    HomeScreenController controller;
    @FXML
    private Label detailsLebel;
    @FXML
    private TextField detailsTestField;
    private boolean isFilter;
    private boolean isProject;
    private boolean isResource;
    @FXML
    private Label moduleTite;
    @FXML
    private ComboBox<String> nameCombo;
    @FXML
    private Label nameLebel;

    private void closeWindow() {
        final Stage currentStage = (Stage) actionButton.getScene().getWindow();
        controller.refresh();
        currentStage.close();
    }

    public void defaultButtonAction() {
        final String selectedItem = nameCombo.getSelectionModel().getSelectedItem();
        if (StringUtils.isNotEmpty(selectedItem)) {
            if (isProject) {
                BuildUtilityContextUtil.removeProject(selectedItem);
            }
            else if (isResource) {
                BuildUtilityContextUtil.removeResource(selectedItem);
            }
            else if (isFilter) {
                BuildUtilityContextUtil.removeFilter(selectedItem);
            }
            closeWindow();
        }
    }

    public final Button getActionButton() {
        return actionButton;
    }

    public final Label getModuleTite() {
        return moduleTite;
    }

    public void nameComboAction() {
        if (isFilter) {
            detailsTestField
                .setText(BuildUtilityContextUtil.getFilters().get(nameCombo.getSelectionModel().getSelectedItem()));
        }
        else if (isResource) {
            final ProjectDO project = BuildUtilityContextUtil.getResource(nameCombo.getSelectionModel().getSelectedItem());
            if (project != null) {
                detailsTestField.setText(project.getPath());
            }
        }
        else if (isProject) {
            final ProjectDO project = BuildUtilityContextUtil.getProject(nameCombo.getSelectionModel().getSelectedItem());
            if (project != null) {
                detailsTestField.setText(project.getPath());
            }
        }
    }

    public final void setController(final HomeScreenController controller) {
        this.controller = controller;
    }

    public final void setFilter(final boolean isFilter) {
        this.isFilter = isFilter;
        if (isFilter) {
            moduleTite.setText("Remove Filter");
            nameLebel.setText("Filter Naeme");
            detailsLebel.setText("Filter Details");
            nameCombo.getItems().addAll(BuildUtilityContextUtil.getFilters().keySet());
            nameCombo.getSelectionModel().selectFirst();
            detailsTestField
                .setText(BuildUtilityContextUtil.getFilters().get(nameCombo.getSelectionModel().getSelectedItem()));
        }
    }

    public final void setProject(final boolean isProject) {
        this.isProject = isProject;
        if (isProject) {
            moduleTite.setText("Remove Project");
            nameLebel.setText("Project Name");
            detailsLebel.setText("Resource Path");
            nameCombo.getItems().addAll(BuildUtilityContextUtil.getProjectHolder().getMap().keySet());
            nameCombo.getSelectionModel().selectFirst();
            final ProjectDO project = BuildUtilityContextUtil.getProject(nameCombo.getSelectionModel().getSelectedItem());
            if (project != null) {
                detailsTestField.setText(project.getPath());
            }
        }
    }

    public final void setResource(final boolean isResource) {
        this.isResource = isResource;
        if (isResource) {
            moduleTite.setText("Remove Resource");
            nameLebel.setText("Resource Name");
            detailsLebel.setText("Resource Path");
            nameCombo.getItems().addAll(BuildUtilityContextUtil.getResourceNames());
            nameCombo.getSelectionModel().selectFirst();
            final ProjectDO project = BuildUtilityContextUtil.getResource(nameCombo.getSelectionModel().getSelectedItem());
            if (project != null) {
                detailsTestField.setText(project.getPath());
            }
        }
    }

}
