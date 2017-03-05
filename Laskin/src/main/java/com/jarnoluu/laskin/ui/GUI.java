package com.jarnoluu.laskin.ui;

import com.jarnoluu.laskin.io.FileManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Jarno Luukkonen
 */
public class GUI extends Application implements UI {
    @Override
    public void start() {
        Application.launch(new String[] {});
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(FileManager.getResource("fxml/GUI.fxml"));
        loader.load();
        
        Parent root = loader.getRoot();
        Scene scene = new Scene(root);
        
        stage.setTitle("Laskin");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        
        GUIController controller = loader.getController();
        controller.setup(scene);
    }
}
