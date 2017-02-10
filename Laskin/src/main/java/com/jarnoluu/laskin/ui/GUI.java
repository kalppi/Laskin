package com.jarnoluu.laskin.ui;

import com.jarnoluu.laskin.io.FileManager;
import com.jarnoluu.laskin.logic.TokenCalculationString;
import com.jarnoluu.laskin.logic.Calculator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.stage.Stage;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
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
