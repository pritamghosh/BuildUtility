package com.project.controller;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.project.dto.ProjectDO;
import com.project.dto.ProjectHolder;
import com.project.exception.BuildUtilCustomException;
import com.project.util.BuildUtilityContextUtil;
import com.project.util.StringUtils;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * <pre>
 * <b>Description : </b>
 * ModuleController.
 * 
 * @version $Revision: 1 $ $Date: Oct 16, 2017 8:16:21 PM $
 * @author $Author: pritam.ghosh $ 
 * </pre>
 */
public class ModuleController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(ModuleController.class);
    @FXML
    private ComboBox<String> projectCombo;
    HomeScreenController controller;
    @FXML
    private Button addMouduleButton;
    @FXML
    private TextField moduleName;
    @FXML
    private TextField modulePath;
    @FXML
    private TextArea errorPanel;

    public void addModuleAction() {
        String selectedKey = projectCombo.getSelectionModel().getSelectedItem();
        if (selectedKey != null && StringUtils.isNotEmpty(moduleName.getText())
            && StringUtils.isNotEmpty(modulePath.getText())) {
            ProjectDO selectedProject = BuildUtilityContextUtil.getProject(selectedKey);
            ProjectDO newModule = new ProjectDO();
            newModule.setProjectName(selectedKey + "-" + moduleName.getText());
            newModule.setPath(modulePath.getText());
            selectedProject.getModules().put(newModule.getProjectName(), newModule);
            newModule.setParentId(selectedKey);
            try {
                BuildUtilityContextUtil.addProject(newModule);
                closeWindow();
            }
            catch (BuildUtilCustomException ex) {
                errorPanel.setText(ex.getMessage().concat("\n").concat(ex.getCorrectiveAcction()));
                errorPanel.setVisible(true);
            }
        }
        else {
            errorPanel.setText("Please enter mandetory fields");
            errorPanel.setVisible(true);
        
        }
    }

    private void closeWindow() {
        Stage currentStage = (Stage) addMouduleButton.getScene().getWindow();
        controller.refresh();
        currentStage.close();
    }

    public final void setController(HomeScreenController controller) {
        this.controller = controller;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> items = projectCombo.getItems();
        items.clear();
        ProjectHolder projectHolder = BuildUtilityContextUtil.getProjectHolder();
        if (projectHolder != null) {
            items.addAll(projectHolder.getMap().keySet());
        }
        projectCombo.getSelectionModel().select(0);
    }

    public void modulePathOnAction() {
        String selectedKey = projectCombo.getSelectionModel().getSelectedItem();
        ProjectDO selectedProject = BuildUtilityContextUtil.getProject(selectedKey);
        Stage currentStage = (Stage) addMouduleButton.getScene().getWindow();
        final DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Some Directories");
        directoryChooser.setInitialDirectory(
            new File(selectedProject != null ? selectedProject.getPath() : System.getProperty("user.home")));
        File dir = directoryChooser.showDialog(currentStage);
        if (dir != null) {
            try {
                modulePath.setText(dir.getAbsolutePath());
            }
            catch (Exception ex) {
                LOGGER.error(ex);
                LOGGER.error("dir is not available");
            }
        }
        else {
            modulePath.setText(null);
        }
    }

}
