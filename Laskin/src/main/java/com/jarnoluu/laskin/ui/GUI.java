package com.jarnoluu.laskin.ui;

import com.jarnoluu.laskin.Util;
import com.jarnoluu.laskin.io.FileManager;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class GUI extends Application implements UI {
    private GUIController controller;
    private Clipboard clipboard;
    
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(FileManager.getResource("fxml/GUI.fxml"));
        loader.load();
        
        Parent root = loader.getRoot();
        
        this.clipboard = Clipboard.getSystemClipboard();
        this.controller = loader.getController();
                
        Scene scene = new Scene(root);
        
        scene.setOnKeyTyped((KeyEvent event) -> {
            if (Util.validChar(event.getCharacter().charAt(0))) {
                this.controller.insert(event.getCharacter().charAt(0));
            }
        });
        
        final KeyCombination kbClear = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
        final KeyCombination kbCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
        final KeyCombination kbPaste = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
        
        scene.setOnKeyPressed((KeyEvent event) -> {
            if (kbClear.match(event)) {
                this.controller.executeCommand("clear");
            } else if (kbCopy.match(event)) {
                this.copyToClipboard();
            } else if (kbPaste.match(event)) {
                this.pasteFromClipboard();
            } else { 
                KeyCode code = event.getCode();

                switch (code) {
                    case BACK_SPACE:
                        this.controller.executeCommand("erase");
                        break;
                    case ENTER:
                        this.controller.executeCommand("calc");
                        break;
                }
            }
        });
        
        stage.setScene(scene);
        stage.show();
        
        stage.setResizable(false);
    }
    
    private void copyToClipboard() {
        ClipboardContent content = new ClipboardContent();
        content.putString(this.controller.getCalculation());
        this.clipboard.setContent(content);
    }
    
    private void pasteFromClipboard() {
        if (this.clipboard.hasString()) {
            String str = this.clipboard.getString();
            this.controller.insert(str);
        }
    }
    
    @Override
    public void start() {
        launch(new String[] {});
    }
}
