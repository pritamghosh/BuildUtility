package com.project.controller;

import com.project.dto.ProjectDO;
import com.project.util.ProjecUtilContext;
import com.project.util.StringUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
                ProjecUtilContext.removeProject(selectedItem);
            }
            else if (isResource) {
                ProjecUtilContext.removeResource(selectedItem);
            }
            else if (isFilter) {
                ProjecUtilContext.removeFilter(selectedItem);
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
                .setText(ProjecUtilContext.getFilters().get(nameCombo.getSelectionModel().getSelectedItem()));
        }
        else if (isResource) {
            final ProjectDO project = ProjecUtilContext.getResource(nameCombo.getSelectionModel().getSelectedItem());
            if (project != null) {
                detailsTestField.setText(project.getPath());
            }
        }
        else if (isProject) {
            final ProjectDO project = ProjecUtilContext.getProject(nameCombo.getSelectionModel().getSelectedItem());
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
            nameCombo.getItems().addAll(ProjecUtilContext.getFilters().keySet());
            nameCombo.getSelectionModel().selectFirst();
            detailsTestField
                .setText(ProjecUtilContext.getFilters().get(nameCombo.getSelectionModel().getSelectedItem()));
        }
    }

    public final void setProject(final boolean isProject) {
        this.isProject = isProject;
        if (isProject) {
            moduleTite.setText("Remove Project");
            nameLebel.setText("Project Name");
            detailsLebel.setText("Resource Path");
            nameCombo.getItems().addAll(ProjecUtilContext.getProjectHolder().getMap().keySet());
            nameCombo.getSelectionModel().selectFirst();
            final ProjectDO project = ProjecUtilContext.getProject(nameCombo.getSelectionModel().getSelectedItem());
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
            nameCombo.getItems().addAll(ProjecUtilContext.getResourceNames());
            nameCombo.getSelectionModel().selectFirst();
            final ProjectDO project = ProjecUtilContext.getResource(nameCombo.getSelectionModel().getSelectedItem());
            if (project != null) {
                detailsTestField.setText(project.getPath());
            }
        }
    }

}
