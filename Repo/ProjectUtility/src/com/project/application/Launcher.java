package com.project.application;

import com.project.constant.ProjectUtilityConstant;
import com.project.util.BuildUtilityContextUtil;
import com.project.util.ResourceLoaderUtil;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <pre>
 * <b>Description : </b>
 * Launcher.
 * 
 * @version $Revision: 1 $ $Date: Oct 16, 2017 8:14:01 PM $
 * @author $Author: pritam.ghosh $ 
 * </pre>
 */
public class Launcher extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/HomeScreenLayout.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.setTitle(ProjectUtilityConstant.TITLE);
            primaryStage.setResizable(false);
            primaryStage.setOnCloseRequest(e->BuildUtilityContextUtil.saveToContext());
            primaryStage.show();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        loadContext();
        launch(args);
    }

    public static void loadContext() {
        ResourceLoaderUtil.copyPropertirs();
        BuildUtilityContextUtil.loadContext();
    }
}
