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
import ElementForDTD.DTDChild;
import ElementForDTD.DTDItem;
import ElementForDTD.DTDOr;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Contenedor que almacena un elemento de XML y la interfaz necesaria para indicar
 * su contenido o una lista de hijos.
 */
public class UIItem {
    private Label nameLabel;
    private XMLElementPane info;
    private List<Module> listChild;
    private VBox childPane;
    private UIAttribute attributes;
    private final BorderPane namePane;
    private final Button deleteButton;
    private final VBox bgPane;
    private final UIChild parent;
    private Element xmlElement;
    private final VBox parentPane;
    
    public UIItem(DTDItem item, Element elem, VBox parent, boolean init, boolean canDelete) {
        this(item, elem, null, parent, init, canDelete);
    }
    
    public UIItem(DTDItem item, Element elem, UIChild parent, boolean init, boolean canDelete) {
        this(item, elem, parent, parent.getAddingPane(), init, canDelete);
    }
    
    public UIItem(DTDItem item, Element elem, UIChild parent, VBox parentPane, boolean init, boolean canDelete) {
        xmlElement = elem;
        bgPane = new VBox();
        bgPane.setPadding(new Insets(10));
        bgPane.setSpacing(10);
        bgPane.getStyleClass().add("background-xml");
        
        // Añadimos los atributos al elemento en caso de tenerlos
        if(item != null && item.hasAttributes()) 
            for (String attribute : item.getAttributes()) 
                elem.setAttribute(attribute, elem.getAttribute(attribute));
        
        this.parent = parent;
        
        this.parentPane = parentPane;
        parentPane.getChildren().add(bgPane);
        
        deleteButton = new Button("X");
        deleteButton.getStyleClass().add("button-add");      
        
        deleteButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Logger.getLogger(UIChild.class.getName()).log(Level.INFO, "Eliminando elemento: {0}", nameLabel.getText());
                if(UIItem.this.parent != null) UIItem.this.parent.removeItem(UIItem.this);
            }
        });
        
        if(!canDelete) deleteButton.setVisible(false);
        
        listChild = new LinkedList<>();
        
        namePane = new BorderPane();
        nameLabel = new Label(elem.getTagName());
        nameLabel.getStyleClass().add("name-xml"); 
        
        bgPane.getChildren().add(namePane);
        
        namePane.setLeft(nameLabel);
        namePane.setRight(deleteButton);
        
        String type = Config.getType(elem.getTagName());
        
        if(elem.hasAttributes()) {
            attributes = new UIAttribute(elem.getAttributes(), bgPane);
        }
        
        if(item != null) {
            if(item.hasChilds()) info = null;
            else {
                info = new XMLElementPane(type, bgPane, elem.getTagName());
                // Añadimos un controlador si el elemento es de tipo parrafo para al pulsar enter crear un nuevo elemento párrafo
                if(xmlElement != null && xmlElement.getNodeName().equals("parrafo")) {
                    ((TextArea)info.info).addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent event) {
                            if (event.getCode() == KeyCode.ENTER) UIItem.this.parent.newItem("");
                        }
                    });
                }
            }
            
            if(init) init(item);
        } else {
            NodeList nodes = elem.getChildNodes();
            if(nodes != null && ((nodes.getLength() == 1 && nodes.item(0).getNodeType() == 3) || nodes.getLength() == 0)){
                info = new XMLElementPane(type, bgPane, elem.getTagName());
            }
        }
    }
    
    /**
     * Elimina el objeto, quitándolo de su padre.
     * @return Si se ha podido eliminar o no.
     */
    public boolean removeFromParent() {
        return parentPane.getChildren().remove(bgPane);
    }
    
    /**
     * Asigna el elemento XML.
     * @param element Elemento XML a asignar.
     */
    public void setElement(Element element) {
        xmlElement = element;
    }
    
    /**
     * Inicializa el objeto a partir de un DTDItem.
     * @param item Elemento en el que se basará para inicializar el objeto.
     */
    private void init(DTDItem item) {
         //Añadir hijos si tiene
        if(item.hasChilds()){
            initChildPane();
            for (DTDChild child : item.getChilds()) 
                listChild.add((child instanceof DTDOr)? new UIOr((DTDOr)child, xmlElement, childPane, null) : 
                    new UIChild(child, xmlElement, childPane, true, null));
        }
    }
    
    /**
     * Situa el focus del programa en este elemento.
     */
    public void setFocus() {
        if(attributes != null) attributes.setFocus();
        else if(info != null) info.setFocus();
        else if(listChild != null && !listChild.isEmpty()) listChild.get(0).setFocus();
        else namePane.requestFocus();
    }
    
    /**
     * Añade un hijo.
     * @param module Hijo a añadir.
     */
    public void addModule(Module module) {
        initChildPane();
        listChild.add(module);
    }
    
    /**
     * Inicializa el panel donde se mostrarán los hijos.
     */
    private void initChildPane() {
        if(childPane != null) return;
        
        childPane = new VBox();
        childPane.getStyleClass().add("background-xml");
        childPane.setSpacing(10);
        childPane.setPadding(new Insets(10, 0, 10, 0));
        bgPane.getChildren().add(childPane);
    }
    
    /**
     * @return Panel donde se muestran los hijos.
     */
    public VBox getChildPane() {
        if(childPane == null) initChildPane();
        return childPane;
    }
    
    /**
     * Devuelve los hijos que tiene.
     * @return Lista de hijos o null si no tiene.
     */
    public Module[] getChilds() {
        return (listChild.isEmpty())? null : listChild.toArray(new Module[0]);
    }
    
    /**
     * Devuelve los atributos del elemento.
     * @return Atributos del elemento o null si no tiene.
     */
    public UIAttribute getAttributes() {
        return attributes;
    }
    
    /**
     * Asigna un texto.
     * @param text Texto a asignar.
     */
    public void setText(String text) {
        if(info != null) info.setText(text);
    }
    
    /**
     * Devuelve el texto del elemento.
     * @return Texte del elemento.
     */
    public String getText() {
        return (info == null)? "" : info.getText();
    }
    
    /**
     * @return Nombre del elemento.
     */
    public String getName() {
        return xmlElement.getNodeName();
    }
    
    /**
     * Muestra u oculta el boton de eliminar elemento.
     * @param visible Si muestra u oculta el boton de eliminar.
     */
    public void showDeleteButton(boolean visible) {
        deleteButton.setVisible(visible);
    }
    
    /**
     * {@inheritDoc}
     * @return 
     */
    @Override
    public String toString() {
        return toString(0);
    }
    
    /**
     * Devuelve una cadena con la informacion del elemento y sus hijos.
     * @param indent Numero de indentaciones de ese elemento en el XML.
     * @return Cadena con la informacion del elemento y sus hijos.
     */
    public String toString(int indent) {
        String text = "<" + nameLabel.getText() + ">";
        String str = "";
        
        if(info != null) text += info.getText();
        else if (listChild != null && listChild.size() > 0) {
            for (Module child : listChild)
                if(child != null) str += child.toString(indent+1);
            
            if(str.equals("")) return ""; // No tiene datos
            else text += "\n" + str;
            for (int i = 0; i < indent; i++) text += "\t";
        }
        
        text += "</" + nameLabel.getText() + ">\n";
        
        return text;
    }
    
    /**
     * Actualiza la informacion del elemento y sus hijos segun la interfaz.
     */
    public void actualize() {
        if(attributes != null) attributes.actualize(xmlElement);
        if(info != null) info.actualize(xmlElement);
        
        if(listChild != null) for (Module child : listChild) child.actualize();
    }
    
    /**
     * @return Elemento XML asociado.
     */
    public Element getXMLElement() {
        return xmlElement;
    }
    
    /**
     * @return Nodo con focus actual o null en caso de no haberlo.
     */
    public Node getFocusNode() {
        Node node;
        if(info != null) return info.getFocusNode(); // Si tiene información no tiene hijos
        
        for (Module child : listChild) {
            node = child.getFocusNode();
            if(node != null) return node;
        }
        
        return null;
    }
}