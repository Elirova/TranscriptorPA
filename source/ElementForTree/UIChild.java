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

import ElementForDTD.DTDChild;
import ElementForDTD.DTDChild.Amount;
import ElementForDTD.DTDItem;
import UI.UI;
import XML.XMLTree;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import org.w3c.dom.Element;

/**
 * Contenedor de interfaz que almacena una lista del mismo tipo de UIItem.
 */
public class UIChild extends Module {
    private final List<UIItem> items;
    private final DTDItem element;

    public UIChild(DTDChild child, Element parentElement, VBox parent, boolean init, UIOr orParent) {
        super(parent, parentElement, child.getAmount(), orParent);
        
        items = new ArrayList<>();
        element = child.getElement();
        
        if(amount != Amount.UNO) {
            final Button addButton = new Button();
            buttonPane.getChildren().add(addButton);

            addButton.setText("Añadir " + element.getName());
            addButton.setStyle("-fx-padding: 5px;");  
            addButton.getStyleClass().add("button-add");

            addButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    Logger.getLogger(UIChild.class.getName()).log(Level.INFO, "Agregando elemento: {0} con cantidad: {1}", new Object[]{element.getName(), amount});
                    newItem("");
                }
            });
        }
        
        if(init && (amount == Amount.UNO || amount == Amount.MAS)) newItem("");
    }
    
    public UIChild(Element parentElement, VBox parent, boolean init, UIOr orParent) {
        super(parent, parentElement, Amount.UNO, orParent);
        
        items = new ArrayList<>();
        element = null;
        showButtons(false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void removeAmount() {
        super.removeAmount();
        if(!canDelete()) for (UIItem it : items) it.showDeleteButton(false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void addAmount() {
        super.addAmount();
        if(canDelete()) for (UIItem it : items) it.showDeleteButton(true);
        else for (UIItem it : items) it.showDeleteButton(false);
    }
    
    /**
     * Crea un nuevo hijo.
     * @param text Texto que contendrá el hijo creado.
     */
    public void newItem(String text) {
        newItem(new UIItem(element, XMLTree.addChild(parentElement, element.getName(), text), this, elementPane, true, canDelete()));
    }
    
    /**
     * Añade un hijo.
     * @param item Hijo a añadir.
     */
    public void newItem(UIItem item) {
        items.add(item);
        item.setFocus();
//        item.ensureVisible(UI.getScrollPane());
        addAmount();
        if(amount == Amount.INTERROGACION) showButtons(false);
    }
    
    /**
     * Elimina un hijo.
     * @param item Hijo a eliminar.
     */
    public void removeItem(UIItem item) {
        removeAmount();
        if(orParent != null && currentAmount == 0 && canDelete()) removeFromParent();
        else {
            item.removeFromParent();
            items.remove(item);
            if(currentAmount == 0) showButtons(true);
        }
    }
    
    /**
     * {@inheritDoc}
     * @return 
     */
    @Override
    public VBox getAddingPane() {
        return elementPane;
    }
    
    /**
     * Devuelve los hijos.
     * @return Lista de hijos o null si no tiene.
     */
    public UIItem[] getItems() {
        return (items.isEmpty())? null : items.toArray(new UIItem[0]);
    }

    /**
     * {@inheritDoc}
     * @return 
     */
    @Override
    public Node getFocusNode() {
        Node node;
        for (UIItem item : items) {
            node = item.getFocusNode();
            if(node != null) return node;
        }
        return null;
    }
    
    @Override
    public void setFocus() {
        if(!items.isEmpty()) items.get(0).setFocus();
        else bgPane.requestFocus();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actualize() {
        for (UIItem item : items) item.actualize();
    }
    
    /**
     * {@inheritDoc}
     * @return 
     */
    @Override
    public String toString(int indent) {
        String text = "", ind = "";
        
        for (int i = 0; i < indent; i++) ind += "\t";
        for (UIItem item : items) text += ind + item.toString(indent);
        
        return (text.replace("\t", "").equals(""))? "" : text;
    }
    
    /**
     * {@inheritDoc}
     * @return
     */
    @Override
    public String toString() {
        String text = "";
        for (UIItem item : items) text += item.toString();
        
        return text;
    }
}
