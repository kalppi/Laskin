package com.jarnoluu.laskin.ui.control;

import com.jarnoluu.laskin.Util;
import com.jarnoluu.laskin.logic.CalculationString;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import org.javatuples.Pair;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class FancyLabelBehaviorEditable implements IFancyLabelBehavior {
    private final FancyLabel label;
    private final CalculationString calc;
    private final List<Pair<KeyCombination, Runnable>> combinations = new LinkedList();
    private final Clipboard clipboard;
    
    public FancyLabelBehaviorEditable(FancyLabel label) {
        this.label = label;
        this.calc = label.getCalculation();
        
        this.clipboard = Clipboard.getSystemClipboard();
        
        final KeyCombination kbCopy = new KeyCodeCombination(KeyCode.C, KeyCombination.CONTROL_DOWN);
        final KeyCombination kbClear = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
        final KeyCombination kbPaste = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN);
        final KeyCombination kbToBin = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN);
        final KeyCombination kbToHex = new KeyCodeCombination(KeyCode.H, KeyCombination.CONTROL_DOWN);
        final KeyCombination kbToDec = new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN);
        final KeyCombination kbToOct = new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN);
        final KeyCombination kbNegate = new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
        
        this.combinations.addAll(Arrays.asList(
            Pair.with(kbCopy, this::copyToClipboard),
            Pair.with(kbClear, this.calc::clear),
            Pair.with(kbPaste, this::pasteFromClipboard),
            Pair.with(kbToBin, () -> {
                this.calc.convertAtCursor("bin");
            }),
            Pair.with(kbToHex, () -> {
                this.calc.convertAtCursor("hex");
            }),
            Pair.with(kbToDec, () -> {
                this.calc.convertAtCursor("dec");
            }),
            Pair.with(kbToOct, () -> {
                this.calc.convertAtCursor("oct");
            }),
            Pair.with(kbNegate, () -> {
                this.calc.negateAtCursor();
            })
        ));
        
        this.calc.setSelected(0);
    }
    
    private void moveLeft() {
        if (this.calc.getCursor() < this.calc.length() - 1) {
            this.calc.setCursor(this.calc.getCursor() + 1);
            this.calc.setSelected(this.calc.getCursor());
            this.label.requestLayout();
        }
    }
    
    private void moveRight() {
        if (this.calc.getCursor() > 0) {
            this.calc.setCursor(this.calc.getCursor() - 1);
            this.calc.setSelected(this.calc.getCursor());
            this.label.requestLayout();
        }
    }
    
    @Override
    public void onKeyPressed(KeyEvent event) {
        this.combinations.stream().filter((p) -> (p.getValue0().match(event))).forEach((p) -> {
            p.getValue1().run();
        });
        
        KeyCode code = event.getCode();
                
        switch (code) {
            case BACK_SPACE:
                this.calc.eraseAtCursor();
                break;
            case ENTER:    
                this.calc.calculate();
                break;
            case LEFT:
                this.moveLeft();
                break;
            case RIGHT:
                this.moveRight();
                break;
        }
    }

    @Override
    public void onKeyTyped(KeyEvent event) {
        if (Util.isValidChar(event.getCharacter().charAt(0))) {
            this.label.getCalculation().insertAtCursor(event.getCharacter().charAt(0));
        }
    }
    
    private void copyToClipboard() {
        ClipboardContent content = new ClipboardContent();
        content.putString(this.calc.toString());
        this.clipboard.setContent(content);
    }
    
    private void pasteFromClipboard() {
        if (this.clipboard.hasString()) {
            String str = this.clipboard.getString();
            this.calc.insertAtCursor(str);
        }
    }
}
