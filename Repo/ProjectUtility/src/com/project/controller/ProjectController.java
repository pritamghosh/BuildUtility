package com.project.controller;

import java.io.File;

import com.project.dto.ProjectDO;
import com.project.util.BuildUtilityContextUtil;
import com.project.util.StringUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * <pre>
 * <b>Description : </b>
 * ProjectController.
 * 
 * @version $Revision: 1 $ $Date: Oct 16, 2017 8:16:26 PM $
 * @author $Author: pritam.ghosh $ 
 * </pre>
 */
public class ProjectController {
    @FXML
    private Button actionButton;
    HomeScreenController controller;
    private boolean isCreate;
    private boolean isProject;
    @FXML
    private Label moduleTite;
    @FXML
    private ComboBox<String> projectCombo;
    @FXML
    private TextField projectName;
    @FXML
    private TextField projectPath;

    private void closeWindow() {
        final Stage currentStage = (Stage) actionButton.getScene().getWindow();
        controller.refresh();
        currentStage.close();
    }

    private void configuringDirectoryChooser(final DirectoryChooser directoryChooser) {
        directoryChooser.setTitle("Select Some Directories");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

    }

    public void defaultButtonAction() {
        if ((StringUtils.isNotEmpty(projectName.getText())
            || StringUtils.isNotEmpty(projectCombo.getSelectionModel().getSelectedItem()))
            && StringUtils.isNotEmpty(projectPath.getText())) {
            final ProjectDO newProject = new ProjectDO();
            newProject.setPath(projectPath.getText());
            if (isProject) {
                if (isCreate) {
                    newProject.setProjectName(projectName.getText());
                    BuildUtilityContextUtil.addProject(newProject);
                }
                else {
                    newProject.setProjectName(projectCombo.getSelectionModel().getSelectedItem());
                    BuildUtilityContextUtil.editProject(newProject);
                }
            }
            else {
                if (isCreate) {
                    newProject.setProjectName(projectName.getText());
                    BuildUtilityContextUtil.addResource(newProject);
                }
                else {
                    newProject.setProjectName(projectCombo.getSelectionModel().getSelectedItem());
                    BuildUtilityContextUtil.editProject(newProject);
                }
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

    public void projectComboAction() {
        final ProjectDO project = BuildUtilityContextUtil.getProject(projectCombo.getSelectionModel().getSelectedItem());
        if (project != null) {
            projectPath.setText(project.getPath());
        }
    }

    public void projectPathOnAction() {
        final Stage currentStage = (Stage) actionButton.getScene().getWindow();
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        configuringDirectoryChooser(directoryChooser);
        final File dir = directoryChooser.showDialog(currentStage);
        if (dir != null) {
            projectPath.setText(dir.getAbsolutePath());
        }
        else {
            projectPath.setText(null);
        }
    }

    public final void setController(final HomeScreenController controller) {
        this.controller = controller;
    }

    public final void setCreate(final boolean isCreate) {
        projectName.setVisible(isCreate);
        projectCombo.setVisible(!isCreate);
        if (!isCreate) {
            if (isProject) {
                projectCombo.getItems().addAll(BuildUtilityContextUtil.getProjectHolder().getMap().keySet());
            }
            else {
                projectCombo.getItems().addAll(BuildUtilityContextUtil.getResourceNames());
            }
            projectCombo.getSelectionModel().selectFirst();
        }
        this.isCreate = isCreate;
    }

    public final void setProject(final boolean isProject) {
        this.isProject = isProject;
    }

}
