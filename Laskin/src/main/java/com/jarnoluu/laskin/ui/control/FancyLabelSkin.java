package com.jarnoluu.laskin.ui.control;

import com.jarnoluu.laskin.Util;
import com.jarnoluu.laskin.logic.CalculationString;
import com.jarnoluu.laskin.logic.Token;
import java.util.LinkedList;
import java.util.ListIterator;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Skin;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 *
 * @author Jarno Luukkonen <luukkonen.jarno@gmail.com>
 */
public class FancyLabelSkin implements Skin<FancyLabel> {
    private static final int PADDING = 4;
    
    private final FancyLabel label;
    private final StackPane rootNode = new StackPane();
    private final Font font;
    private final Background bg;
    
    private final Background bgError = new Background(
        new BackgroundFill(Color.rgb(255, 230, 230), CornerRadii.EMPTY, Insets.EMPTY)
    );
    
    public FancyLabelSkin(final FancyLabel label) {
        this.label = label;
        
        this.rootNode.setAlignment(Pos.CENTER_RIGHT);
        this.rootNode.setPadding(new Insets(5, 5, 5, 5));
        this.font = this.label.getFont();
        this.bg = this.label.getBackground();
    }
    
    @Override
    public Node getNode() {
        this.render(this.font.getSize());
        return this.rootNode;
    }
    
    @Override
    public void dispose() {
    }

    @Override
    public FancyLabel getSkinnable() {
        return this.label;
    }
    
    private Node generateTextNode(String text, double fontSize, Color color) {
        Text node = new Text(text);
        node.setFont(new Font(this.font.getName(), fontSize));
        node.setFill(color);
        
        return node;
    }
    
    private Color getPartColor(Token.Type type) {
        switch (type) {
            case NUMBER:
            case NUMBER_HEX:
            case NUMBER_BIN:
            case NUMBER_OCT:
                return Color.rgb(70, 70, 70);
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
    
    public void render(double fontSize) {
        double w = this.rootNode.getWidth() - this.rootNode.getPadding().getLeft() - this.rootNode.getPadding().getRight();
        
        if (this.label.getPseudoClassStates().contains(PseudoClass.getPseudoClass("error"))) {
            this.rootNode.setBackground(this.bgError);
        } else {
            this.rootNode.setBackground(this.bg);
        }
        
        ObservableList<Node> children = this.rootNode.getChildren();
        children.clear();
        
        CalculationString calc = this.label.getCalculation();
        
        if (calc == null) {
            return;
        }
        
        LinkedList<Token> parts = calc.getTokens();
        Group textGroup = new Group();
        textGroup.setTranslateY(2);
        Rectangle selected = null;
        
        if (parts.size() > 0) {
            double x = w;
            
            for (Token t : parts) {
                if (t.getType() == Token.Type.UNKNOWN) {
                    this.rootNode.setBackground(this.bgError);
                    break;
                }
            }

            double totalTextWidth = 0;
            ListIterator<Token> it = parts.listIterator(parts.size());
            int selectedIndex = parts.size() - calc.getSelected() - 1;
            
            while (it.hasPrevious()) {
                Token part = it.previous();
                Token.Type type = part.getType();
                
                if (type == Token.Type.NUMBER) {
                    String data = part.getData();
                    
                    LinkedList<String> numberParts = Util.splitNumber(data, 3);
                    
                    Group numberGroup = new Group();
                    int xx = 0;

                    for (String numberPart : numberParts) {
                        Node node = this.generateTextNode(numberPart, fontSize, this.getPartColor(Token.Type.NUMBER));
                        node.setLayoutX(xx);
                        
                        Bounds b = node.getLayoutBounds();
                        xx += b.getWidth() + FancyLabelSkin.PADDING;
                        
                        numberGroup.getChildren().add(node);
                    }
                    
                    Bounds textBounds = numberGroup.getLayoutBounds();
                    totalTextWidth += textBounds.getWidth() - FancyLabelSkin.PADDING;
                    x -= textBounds.getWidth() + FancyLabelSkin.PADDING;

                    numberGroup.setLayoutX(x);

                    textGroup.getChildren().add(numberGroup);
                    
                    if (it.nextIndex() == selectedIndex) {
                        selected = new Rectangle(x, this.label.getHeight() / 2 + textBounds.getMinY() + 3, textBounds.getWidth() + 3, textBounds.getHeight());
                    }
                } else {
                    Node node = this.generateTextNode(part.toPrettyString(), fontSize, this.getPartColor(type));
                    
                    Bounds textBounds = node.getLayoutBounds();
                    x -= textBounds.getWidth() + FancyLabelSkin.PADDING;
                    totalTextWidth += textBounds.getWidth() + FancyLabelSkin.PADDING + 8;

                    node.setLayoutX(x);
                    textGroup.getChildren().add(node);
                    
                    if (it.nextIndex() == selectedIndex) {
                        selected = new Rectangle(x, this.label.getHeight() / 2 + textBounds.getMinY() + 3, textBounds.getWidth() + 3, textBounds.getHeight());
                    }
                }
                
                if (totalTextWidth >= w) {
                    if (fontSize > 5) {
                        this.render(fontSize - 0.5);
                    }
                    return;
                }
            }
        }
        
        if (selected != null) {
            selected.setFill(Color.rgb(250, 195, 170));
            selected.setTranslateX(selected.getX() - w + selected.getWidth() + 3);
            children.add(selected);
        }
        
        children.add(textGroup);
    }
}