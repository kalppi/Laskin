package com.jarnoluu.laskin.ui;

import com.jarnoluu.laskin.Util;
import com.jarnoluu.laskin.logiikka.Parser;
import com.jarnoluu.laskin.logiikka.Token;
import java.util.LinkedList;
import javafx.css.PseudoClass;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Skin;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class CustomLabelSkin implements Skin<Label> {
    private final Label textField;
    private final StackPane rootNode = new StackPane();
    private final String fontName;
    private final double fontSize;
    
    private static final int PADDING = 4;
    
    private final Background bg;
    
    private final Background bgError = new Background(
        new BackgroundFill(Color.rgb(255, 230, 230), CornerRadii.EMPTY, Insets.EMPTY)
    );
    
    public CustomLabelSkin(final Label textField, Color background) {
        this.textField = textField;
        
        this.rootNode.alignmentProperty().setValue(Pos.CENTER_RIGHT);
        this.rootNode.setPadding(new Insets(5, 5, 5, 5));
        
        Font font = this.textField.fontProperty().get();
        this.fontName = font.getName();
        this.fontSize = font.getSize();
       
        this.bg = new Background(
            new BackgroundFill(background, CornerRadii.EMPTY, Insets.EMPTY)
        );
    }
    
    @Override
    public Node getNode() {
        this.draw(this.fontSize);
        return this.rootNode;
    }
    
    @Override
    public void dispose() {
    }

    @Override
    public Label getSkinnable() {
        return this.textField;
    }
    
    private Node generateTextNode(String text, double fontSize, Color color) {
        Text node = new Text(text);
        node.setFont(new Font(this.fontName, fontSize));
        node.setFill(color);
        
        return node;
    }
    
    private Color getPartColor(Token.Type type) {
        switch (type) {
            case NUMBER:
                return Color.BLACK;
            case OPER:
                return Color.GREEN;
            case FUNC:
                return Color.BLUE;
            case BRACKET_START:
            case BRACKET_END:
                return Color.BLUE;
            case COMMA:
                return Color.BLUE;
            case SPECIAL:
                return Color.MAGENTA;
            case UNKNOWN:
                return Color.RED;
            default:
                return Color.BLACK;
        }
    }
    
    public void draw(double fontSize) {
        double w = this.textField.getWidth();
        
        if (this.textField.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error"))) {
            this.rootNode.setBackground(this.bgError);
        } else {
            this.rootNode.setBackground(this.bg);
        }
        
        String te = this.textField.getText();
        
        Group textGroup = new Group();
        
        if (te.length() > 0) {
            double x = w;
            double y = 0;

            LinkedList<Token> parts;
            
            try {
                parts = Parser.tokenize(te, false, true);
            } catch (Exception e) {
                return;
            }
            
            for (Token t : parts) {
                if (t.getType() == Token.Type.UNKNOWN) {
                    this.rootNode.setBackground(this.bgError);
                    break;
                }
            }

            double totalTextWidth = 0;

            while (parts.size() > 0) {
                Token part = parts.removeLast();
                Token.Type type = part.getType();
                
                if (type == Token.Type.NUMBER) {
                    LinkedList<String> numberParts = Util.splitNumber(part.getData(), 3);
                    
                    Group numberGroup = new Group();
                    int xx = 0;
                    int numberTotalWidth = 0;

                    for (String numberPart : numberParts) {
                        Node node = this.generateTextNode(numberPart, fontSize, this.getPartColor(Token.Type.NUMBER));

                        Bounds b = node.getLayoutBounds();

                        node.setLayoutX(xx);

                        xx += b.getWidth() + CustomLabelSkin.PADDING;
                        numberTotalWidth += b.getWidth() + CustomLabelSkin.PADDING;

                        numberGroup.getChildren().add(node);
                    }

                    totalTextWidth += numberTotalWidth;
                    x -= numberTotalWidth;

                    numberGroup.setLayoutX(x);
                    numberGroup.setLayoutY(y);

                    textGroup.getChildren().add(numberGroup);
                } else {
                    Node node = this.generateTextNode(part.toString(), fontSize, this.getPartColor(type));

                    Bounds b = node.getLayoutBounds();
                    x -= b.getWidth() + CustomLabelSkin.PADDING;
                    totalTextWidth += b.getWidth() + CustomLabelSkin.PADDING;

                    node.setLayoutX(x);
                    node.setLayoutY(y);

                    textGroup.getChildren().add(node);
                }
                
                if (totalTextWidth >= w - 5) {
                    if (fontSize > 5) {
                        this.draw(fontSize - 0.5);
                    }
                    return;
                }
            }
        }
        
        this.rootNode.getChildren().setAll(textGroup);
    }
}