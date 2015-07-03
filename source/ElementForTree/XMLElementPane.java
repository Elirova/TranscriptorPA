/*
 * Copyright (c) 2015 Elisabet Romero <eliromeva at gmail.com>. 
 * 
 * This file is part of TranscriptorPA.
 * 
 * TranscriptorPA is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * TranscriptorPA is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with TranscriptorPA.  If not, see <http ://www.gnu.org/licenses/>.
 */

package ElementForTree;

import Config.Config;
import Config.ExtensionFileFilter;
import UI.SwitchButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import org.w3c.dom.Element;

/**
 * Elemento de interfaz para introducir la informacion de un elemento XML.
 */
public class XMLElementPane {
    Pane bgPane;
    String type;
    Node info;
    public static final ObservableList<String> values = FXCollections.observableArrayList("Línea", "Texto", "Selector_de_fichero", "Si/No", "Lista");
    
    public XMLElementPane(String type, Pane parent, String name) {
        this.type = type;
        switch (type) {
            case "Línea":
                bgPane = new HBox();
                
                info = new TextField();
                ((TextField)info).setMinWidth(600);
                ((TextField)info).getStyleClass().add("textArea"); 
                
                bgPane.getChildren().add(info);
                break;
            case "Selector_de_fichero":
                bgPane = new HBox();   
                ((HBox)bgPane).setSpacing(20);
                
                info = new TextField();
                ((TextField)info).setMinWidth(600);
                ((TextField)info).getStyleClass().add("textArea"); 
                
                Button button = new Button("Seleccionar...");
                
                button.getStyleClass().add("button-add"); 
                
                button.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        String path = ExtensionFileFilter.getFileName(".", "Selecciona un fichero", new String[] {"png", "jpg"});
                        ((TextField)info).setText(path);
                    }
                });
                
                bgPane.getChildren().addAll(info, button);
                break;
            case "Si/No":
                bgPane = new HBox();   
                info = new SwitchButton();
                info.getStyleClass().add("switch");
                bgPane.getChildren().add(info);
                break;
            case "Lista":
                bgPane = new HBox();   
                info = new ChoiceBox();
                ((ChoiceBox)info).setMinWidth(120);
                ((ChoiceBox)info).setMinHeight(30);
                ((ChoiceBox)info).getStyleClass().add("listSpinner");
                ObservableList list = Config.getListElement(name);
                ((ChoiceBox)info).setItems(list);
                ((ChoiceBox)info).setValue(list.get(0));
                bgPane.getChildren().add(info);
                break;
            default: // En caso de que no sea una de las anteriores se pone un área de texto
                bgPane = new HBox();
                info = new TextArea();
                ((TextArea)info).setMinWidth(600);
                ((TextArea)info).setMinHeight(300);
                ((TextArea)info).setWrapText(true);
                ((TextArea)info).getStyleClass().add("textArea"); 
                bgPane.getChildren().add(info);
                
                break;
        }
        
        parent.getChildren().add(bgPane);
    }
    
    /**
     * Actualiza el valor del elemento XML segun la interfaz.
     * @param element Elemento a actualizar.
     */
    public void actualize(Element element) {
        element.setTextContent(getText());
    }
    
    /**
     * Situa el focus del programa en este elemento.
     */
    public void setFocus() {
        info.requestFocus();
    }
    
    /**
     * Devuelve los tipos de valores posibles de un elemento.
     * @return Valores posibles.
     */
    public static ObservableList getValues() {
        return values;
    }
    
    /**
     * @return Cadena con la informacion contenida en la interfaz.
     */
    public String getText() {
        switch (type) {
            case "Si/No":
                return ((SwitchButton)info).getText();
            case "Lista":
                return ((ChoiceBox)info).getValue().toString();
            case "Línea":
            case "Selector_de_fichero":
                return ((TextField)info).getText();
            default:
                return ((TextArea)info).getText();
        }
    }
    
    /**
     * Asigna la informacion a la interfaz.
     * @param text Cadena con la informacion a asignar.
     */
    public void setText(String text) {
        switch (type) {
            case "Si/No":
                if(text.equals("Si") || text.equals("No")) ((SwitchButton)info).setText(text);
                else ((SwitchButton)info).setText("No");
                break;
            case "Lista":
                ((ChoiceBox)info).setValue(text);
                break;
            case "Línea":
            case "Selector_de_fichero":
                ((TextField)info).setText(text);
                break;
            default:
                ((TextArea)info).setText(text);
                break;
        }
    }
    
    /**
     * @return elemento con focus en caso de existir o null en caso contrario.
     */
    public Node getFocusNode() {
        return (info.isFocused())? info : null;
    }
}
