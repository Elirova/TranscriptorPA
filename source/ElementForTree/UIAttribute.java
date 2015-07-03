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
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * Contenedor que almacena una lista de atributos de un elemento XML.
 */
public class UIAttribute {
    String name;
    private final XMLAttributePane[] attributes;
    VBox bgPane;
    
    UIAttribute(NamedNodeMap attribs, Pane parent) {
        bgPane = new VBox();
        bgPane.getStyleClass().add("background-attribute-xml");
        bgPane.setSpacing(10);
        bgPane.setPadding(new Insets(10, 0, 10, 0));
        
        parent.getChildren().add(bgPane);
        Node node;
        attributes = new XMLAttributePane[attribs.getLength()];
        for (int i = 0; i < attribs.getLength(); i++) {
            node = attribs.item(i);
            
            HBox pane = new HBox();
            pane.setSpacing(20);
            bgPane.getChildren().add(pane);
            
            Label name = new Label(node.getNodeName());
            name.getStyleClass().add("name-xml");
            pane.getChildren().add(name);
            attributes[i] = new XMLAttributePane(Config.getType(node.getNodeName()), pane, node.getNodeName(), node.getNodeValue());
        }
    }
    
    /**
     * Actualiza el elemento indicado con los datos del atributo.
     * @param element Elemento a actualizar.
     */
    public void actualize(Element element) {
        for(XMLAttributePane attrib : attributes) {
            attrib.actualize(element);
        }
    }
    
    /**
     * Situa el focus del programa en su primer hijo.
     */
    public void setFocus() {
        attributes[0].setFocus();
    }
}
