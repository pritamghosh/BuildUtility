package com.project.controller;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import com.project.constant.ProjectUtilityConstant;
import com.project.dto.ProjectDO;
import com.project.dto.ProjectHolder;
import com.project.util.BuildUtilityContextUtil;
import com.project.util.StringUtils;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * <pre>
 * <b>Description : </b>
 * HomeScreenController.
 * 
 * @version $Revision: 1 $ $Date: Oct 16, 2017 8:15:58 PM $
 * @author $Author: pritam.ghosh $ 
 * </pre>
 */
public class HomeScreenController implements Initializable {
    @FXML
    private Button removeFilterButton;
    @FXML
    private Button removeResourceButton;
    @FXML
    private Button openInExpButton;
    @FXML
    private Accordion rootPane;
    @FXML
    private TitledPane resourcePane;
    @FXML
    private TitledPane codePane;
    @FXML
    private CheckBox isDebug;
    @FXML
    private CheckBox isSonar;
    @FXML
    private CheckBox isEclipse;
    @FXML
    private CheckBox isClean;
    @FXML
    private CheckBox isInstall;
    @FXML
    private TextField modulePath;
    @FXML
    private TextField resourcePath;
    @FXML
    private TextField testCaseName;
    @FXML
    private TextField testMetodName;
    @FXML
    private ComboBox<String> projectCombo;
    @FXML
    private ComboBox<String> moduleCombo;
    @FXML
    private ComboBox<String> resourceCombo;
    @FXML
    private ComboBox<String> filterCombo;
    @FXML
    private RadioButton test;
    @FXML
    private RadioButton testSkip;
    @FXML
    private RadioButton dskipTests;
    @FXML
    private CheckBox isDevUserContext;
    @FXML
    private CheckBox isRootProject;
    @FXML
    ToggleGroup group = new ToggleGroup();
    @FXML
    TextArea commandLine;
    @FXML
    MenuItem removeFilterMenu;
    @FXML
    MenuItem removeProjectMenu;
    @FXML
    MenuItem removeResourceMenu;
    @FXML
    MenuItem editFilterMenu;
    @FXML
    MenuItem editProjectMenu;
    @FXML
    MenuItem editResourceMenu;

    public void buildAction() {
        onSelectionAction();
        executeCommand(constructBuildCommand());
    }

    private void executeCommand(String command) {
        try {
            Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"" + command + "\"");
        }
        catch (IOException e) {
            System.out.println("unable to execute command");
        }
    }

    public void buildActionForResource() {
        onSelectionAction();
        executeCommand(constructResourceCommand());
    }

    private String constructBuildCommand() {
        StringBuilder command = new StringBuilder();
        if (StringUtils.isNotEmpty(modulePath.getText())) {
            command.append(StringUtils.substring(modulePath.getText(), 0, 2));
            command.append(" && cd ");
            command.append(modulePath.getText());
            command.append(" && ").append(constructMavenCommandForBuild());
        }
        return command.toString();
    }

    private String constructMavenCommandForBuild() {
        StringBuilder command = new StringBuilder();
        if (StringUtils.isNotEmpty(modulePath.getText())) {
            command.append("mvn");
            if (isClean.isSelected()) {
                command.append(" clean");
            }
            if (isInstall.isSelected()) {
                command.append(" install");
            }
            if (dskipTests.isSelected()) {
                command.append(" -DskipTests");
            }
            if (testSkip.isSelected()) {
                command.append(" -Dmaven.test.skip=true");
            }
            if (isDevUserContext.isSelected()) {
                command.append(" -Pdev-user-context");
            }
            if (test.isSelected()) {
                constructMavenTestCommand(command);
            }
            if (isSonar.isSelected()) {
                command.append(" sonar:sonar");
                if (StringUtils.isNotEmpty(BuildUtilityContextUtil.getProperties(ProjectUtilityConstant.SONAR_HOST_URL))) {
                    command.append(" -Dsonar.host.url=")
                        .append(BuildUtilityContextUtil.getProperties(ProjectUtilityConstant.SONAR_HOST_URL));
                }
            }
            if (isEclipse.isSelected()) {
                command.append(" eclipse:eclipse");
            }
        }
        return command.toString();
    }

    private void constructMavenTestCommand(StringBuilder command) {
        command.append(" test ");
        if (isDebug.isSelected()) {
            command.append(" -Dmaven.surefire.debug");
        }
        if (StringUtils.isNotEmpty(testCaseName.getText())) {
            command.append(" -Dtest=");
            command.append(testCaseName.getText());
            if (StringUtils.isNotEmpty(testMetodName.getText().trim())) {
                command.append('#');
                command.append(testMetodName.getText().trim());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        rootPane.setExpandedPane(codePane);
        refresh();

    }

    public void refresh() {
        ObservableList<String> items = projectCombo.getItems();
        items.clear();
        ProjectHolder projectHolder = BuildUtilityContextUtil.getProjectHolder();
        if (projectHolder != null) {
            List<String> filteredProjects = projectHolder.getMap().values().stream()
                .filter(project -> (project.getModules() != null && !project.getModules().isEmpty())
                    || !StringUtils.isNotEmpty(project.getParentId()))
                .map(ProjectDO::getProjectName).collect(Collectors.toList());
            items.addAll(filteredProjects);
            projectCombo.getSelectionModel().selectFirst();
        }
        moduleCombo.getItems().clear();
        ProjectDO project = BuildUtilityContextUtil.getProject(projectCombo.getSelectionModel().getSelectedItem());
        if (project != null) {
            moduleCombo.getItems().addAll(project.getModules().keySet());
            moduleCombo.getSelectionModel().selectFirst();
            modulePath.setText(project.getPath());
        }
        resourceCombo.getItems().clear();
        if (BuildUtilityContextUtil.getResourceNames() != null) {
            resourceCombo.getItems().addAll(BuildUtilityContextUtil.getResourceNames());
            resourceCombo.getSelectionModel().selectFirst();
        }
        filterCombo.getItems().clear();
        if (BuildUtilityContextUtil.getFilters() != null) {
            filterCombo.getItems().addAll(BuildUtilityContextUtil.getFilters().keySet());
            filterCombo.getSelectionModel().selectFirst();
        }
        onSelectionAction();
    }

    public void projectComboOnClick() {
        moduleCombo.getItems().clear();
        ProjectDO project = BuildUtilityContextUtil.getProject(projectCombo.getSelectionModel().getSelectedItem());
        if (project != null) {
            moduleCombo.getItems().addAll(project.getModules().keySet());
            modulePath.setText(project.getPath());
            moduleCombo.getSelectionModel().selectFirst();
        }
        onSelectionAction();
    }

    public void moduleComboOnClick() {
        ProjectDO project = BuildUtilityContextUtil.getProject(moduleCombo.getSelectionModel().getSelectedItem());
        if (project != null) {
            modulePath.setText(project.getPath());
        }
        isRootProject.setSelected(false);
        onSelectionAction();
    }

    public void addNewModule() {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        try {
            Pane root = loader.load(getClass().getResource("/ModuleLayout.fxml").openStream());
            ModuleController controller = loader.getController();
            controller.setController(this);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(ProjectUtilityConstant.CREATE_PROJECT_TITLE);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.initOwner(rootPane.getScene().getWindow());
            primaryStage.setResizable(false);
            primaryStage.showAndWait();
        }
        catch (IOException e) {
            System.out.println("unable to load Module layout");
        }
        catch (Exception e) {
            System.out.println("unable to load Module module");
        }
    }

    private void openProjectPopUp(String dialogTitle, boolean isProject, boolean isCreate, String buttonName) {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        try {
            Pane root = loader.load(getClass().getResource("/ProjetLayout.fxml").openStream());
            ProjectController controller = loader.getController();
            controller.setController(this);
            controller.setProject(isProject);
            controller.setCreate(isCreate);
            controller.getActionButton().setText(buttonName);
            controller.getModuleTite().setText(buttonName);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle(dialogTitle);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.initOwner(rootPane.getScene().getWindow());
            primaryStage.showAndWait();
        }
        catch (IOException e) {
            System.out.println("unable to load layout");
        }
        catch (Exception ex) {
            System.out.println("unable to load module ");
        }
    }

    public void addNewProjct() {
        openProjectPopUp(ProjectUtilityConstant.CREATE_PROJECT_TITLE, true, true, "Create Project");
    }

    public void addNewResource() {
        openProjectPopUp(ProjectUtilityConstant.CREATE_RESOURCE_TITLE, false, true, "Create Resource");
    }

    public void editProjct() {
        if (BuildUtilityContextUtil.getProjectHolder() != null
            && !BuildUtilityContextUtil.getProjectHolder().getMap().keySet().isEmpty())
            openProjectPopUp(ProjectUtilityConstant.EDIT_PROJECT_TITLE, true, false, "Edit Project");
    }

    public void editResource() {
        if (BuildUtilityContextUtil.getResourceNames() != null && !BuildUtilityContextUtil.getResourceNames().isEmpty())
            openProjectPopUp(ProjectUtilityConstant.CREATE_RESOURCE_TITLE, false, false, "Create Resource");
    }

    public void removeFilter() {
        openRemovePopUp(false, false, true, ProjectUtilityConstant.REMOVE_FILTER_TITLE);
    }

    public void removeProjct() {
        openRemovePopUp(true, false, false, ProjectUtilityConstant.REMOVE_PROJECT_TITLE);
    }

    private void openRemovePopUp(boolean isProject, boolean isResource, boolean isFilter, String dialogTitle) {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        try {
            Pane root = loader.load(getClass().getResource("/RemoveLayout.fxml").openStream());
            RemoveController controller = loader.getController();
            controller.setController(this);
            controller.setProject(isProject);
            controller.setResource(isResource);
            controller.setFilter(isFilter);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.setTitle(dialogTitle);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.initOwner(rootPane.getScene().getWindow());
            primaryStage.showAndWait();
        }
        catch (IOException ex) {
            System.out.println("unable to load layout");
        }
        catch (Exception ex) {
            System.out.println("unable to load module ");
        }
    }

    public void removeResource() {
        openRemovePopUp(false, true, false, ProjectUtilityConstant.REMOVE_RESOURCE_TITLE);
    }

    public void closeWindow() {
        save();
        Stage currentStage = (Stage) rootPane.getScene().getWindow();
        currentStage.close();
    }

    public void onSelectionAction() {
        if (codePane.isExpanded()) {
            testMetodName.setDisable(!test.isSelected());
            testCaseName.setDisable(!test.isSelected());
            isDebug.setDisable(!test.isSelected());
            if (isRootProject.isSelected()) {
                String selectedItem = projectCombo.getSelectionModel().getSelectedItem();
                ProjectDO selectedProject = BuildUtilityContextUtil.getProject(selectedItem);
                if (selectedProject != null)
                    modulePath.setText(selectedProject.getPath());
            }
            else {
                String selectedItem = moduleCombo.getSelectionModel().getSelectedItem();
                ProjectDO selectedProject = BuildUtilityContextUtil.getProject(selectedItem);
                if (selectedProject != null)
                    modulePath.setText(selectedProject.getPath());
            }
            commandLine.setText(constructMavenCommandForBuild());
            resourcePane.setExpanded(false);
        }
        else if (resourcePane.isExpanded()) {
            ProjectDO selectedProject = BuildUtilityContextUtil
                .getResource(resourceCombo.getSelectionModel().getSelectedItem());
            if (selectedProject != null)
                resourcePath.setText(selectedProject.getPath());
            commandLine.setText(constructMavenCommandResourceCommand());
            codePane.setExpanded(false);
            removeFilterButton
                .setDisable(BuildUtilityContextUtil.getFilters() == null || BuildUtilityContextUtil.getFilters().isEmpty());
            removeResourceButton.setDisable(
                BuildUtilityContextUtil.getResourceNames() == null || BuildUtilityContextUtil.getResourceNames().isEmpty());
        }
        else {
            commandLine.setText("");
        }
        removeFilterMenu
        .setDisable(BuildUtilityContextUtil.getFilters() == null || BuildUtilityContextUtil.getFilters().isEmpty());
        removeResourceMenu.setDisable(
        BuildUtilityContextUtil.getResourceNames() == null || BuildUtilityContextUtil.getResourceNames().isEmpty());
        removeProjectMenu.setDisable(BuildUtilityContextUtil.getProjectHolder() == null || BuildUtilityContextUtil.getProjectHolder().getMap().isEmpty());
        editFilterMenu
        .setDisable(BuildUtilityContextUtil.getFilters() == null || BuildUtilityContextUtil.getFilters().isEmpty());
        editResourceMenu.setDisable(
        BuildUtilityContextUtil.getResourceNames() == null || BuildUtilityContextUtil.getResourceNames().isEmpty());
        editProjectMenu.setDisable(BuildUtilityContextUtil.getProjectHolder() == null || BuildUtilityContextUtil.getProjectHolder().getMap().isEmpty());

    }

    private String constructResourceCommand() {
        StringBuilder command = new StringBuilder();
        String selectedFilter = filterCombo.getSelectionModel().getSelectedItem();
        if (BuildUtilityContextUtil.getFilters() != null && StringUtils.isNotEmpty(selectedFilter)
            && StringUtils.isNotEmpty(resourcePath.getText())
            && StringUtils.isNotEmpty(BuildUtilityContextUtil.getFilters().get(selectedFilter))) {
            command.append(StringUtils.substring(resourcePath.getText(), 0, 2));
            command.append(" && cd ");
            command.append(resourcePath.getText());
            command.append(" && ").append(constructMavenCommandResourceCommand());

        }
        return command.toString();
    }

    private String constructMavenCommandResourceCommand() {
        StringBuilder command = new StringBuilder();
        String selectedFilter = filterCombo.getSelectionModel().getSelectedItem();
        if (BuildUtilityContextUtil.getFilters() != null && StringUtils.isNotEmpty(selectedFilter)
            && StringUtils.isNotEmpty(resourcePath.getText())
            && StringUtils.isNotEmpty(BuildUtilityContextUtil.getFilters().get(selectedFilter))) {
            command.append("mvn clean install -Dvoyager.env=");
            command.append(BuildUtilityContextUtil.getFilters().get(filterCombo.getSelectionModel().getSelectedItem()));
        }
        return command.toString();
    }

    public void openMvnSettingsMenuAction() {
        try {
            String path = BuildUtilityContextUtil.getProperties(ProjectUtilityConstant.NOTEPAD_HOME);
            String mvnSettingsPath = BuildUtilityContextUtil.getProperties(ProjectUtilityConstant.MVN_SETTINGS_PATH);
            if (StringUtils.isNotEmpty(mvnSettingsPath) && StringUtils.isNotEmpty(path)) {
                path = path + " " + mvnSettingsPath;
                Runtime.getRuntime().exec(path);
            }
        }
        catch (IOException e) {
            System.out.println("unable to open setteings.xml");
        }
    }

    public void editFilterAcion() {
        filterAcion(true);
    }

    public void newFilterAcion() {
        filterAcion(false);
    }

    public void filterAcion(boolean isEdit) {

        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        try {
            Pane root = loader.load(getClass().getResource("/FilterLayout.fxml").openStream());
            FilterController controller = loader.getController();
            controller.setController(this);
            controller.setEdit(isEdit);
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(ProjectUtilityConstant.CREATE_FILTER_TITLE);
            primaryStage.initModality(Modality.APPLICATION_MODAL);
            primaryStage.setResizable(false);
            primaryStage.initOwner(rootPane.getScene().getWindow());
            primaryStage.showAndWait();
        }
        catch (IOException ex) {
            System.out.println("unable to load filter layout");
        }
        catch (Exception ex) {
            System.out.println("unable to load filter module");
        }
    }

    public void openInExplorer() {
        try {
            Desktop.getDesktop().open(new File(modulePath.getText()));
        }
        catch (IOException ex) {
            System.out.println("unable to open folder :: " + ex.getMessage());
        }
    }

    public void runSonar() {
        executeCommand(BuildUtilityContextUtil.getProperties(ProjectUtilityConstant.SONAR_PATH));
    }

    public void save() {
        BuildUtilityContextUtil.saveToContext();
        refresh();
    }

    public void load() {
        BuildUtilityContextUtil.loadContext();
        refresh();
    }
}
