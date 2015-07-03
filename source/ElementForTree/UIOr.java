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
import ElementForDTD.DTDOr;
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
 * Contenedor de interfaz que almacena una lista de modulos.
 */
public class UIOr extends Module {
    private final List<Module> modules;

    public UIOr(DTDOr or, Element parentElement, VBox parent, UIOr orParent) {
        super(parent, parentElement, or.getAmount(), orParent);
        
        modules = new ArrayList<>();
        
        for (DTDChild child : or.getChilds()) createButton(child);
    }
    
    /**
     * Crea el botón de añadir un hijo.
     * @param child Hijo que se añadirá al pulsar el botón.
     */
    private void createButton(final DTDChild child) {
        Button button = new Button();
        buttonPane.getChildren().add(button);
        
        // TODO ver cuando meter un or dentro de otro
        button.setText("Añadir " + child.getElement().getName());
        button.setStyle("-fx-padding: 5px;");  
        button.getStyleClass().add("button-add");    
        
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                Logger.getLogger(UIOr.class.getName()).log(Level.INFO, "Agregando elemento: {0} con cantidad: {1}", new Object[]{child.getElement().getName(), amount});
                addAmount();
                addChild((child instanceof DTDOr)? new UIOr((DTDOr)child, parentElement, elementPane, UIOr.this) : 
                    new UIChild(child, parentElement, elementPane, true, UIOr.this));
            }
        });
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    protected void addAmount() {
        super.addAmount();
    }
    
    /**
     * {@inheritDoc} 
     */
    @Override
    protected void removeAmount() {
        super.removeAmount();
        if(currentAmount == 0) showButtons(true);
    }
    
    /**
     * Añade un hijo.
     * @param module Hijo a añadir.
     */
    public void addChild(Module module) {
        modules.add(module);
        if(amount == DTDChild.Amount.INTERROGACION || amount == Amount.UNO) showButtons(false);
    }
    
    /**
     * Elimina un hijo.
     * @param module Hijo a eliminar.
     */
    public void removeChild(Module module) {
        removeAmount();
        if(orParent != null && currentAmount == 0 && canDelete()) removeFromParent();
        else {
            modules.remove(module);
            if(currentAmount == 0) showButtons(true);
        }
    }
    
    /**
     * Devuelve los hijos del UIOr.
     * @return Lista de hijos o null si no tiene.
     */
    public Module[] getChilds() {
        return (modules.isEmpty())? null : modules.toArray(new Module[0]);
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
     * {@inheritDoc} 
     * @return 
     */
    @Override
    public String toString() {
        String text = "";
        for (Module module : modules) text += module.toString();
        
        return text;
    }
    
    /**
     * {@inheritDoc} 
     * @param indent
     * @return 
     */
    @Override
    public String toString(int indent) {
        String text = "";
        for (Module module : modules) text += module.toString(indent);
        
        return text;
    }

    /**
     * {@inheritDoc} 
     * @return 
     */
    @Override
    public Node getFocusNode() {
        Node node;
        for (Module module : modules) {
            node = module.getFocusNode();
            if(node != null) return node;
        }
        
        return null;
    }
    
    @Override
    public void setFocus() {
        if(!modules.isEmpty()) modules.get(0).setFocus();
        else bgPane.requestFocus();
    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public void actualize() {
        for (Module module : modules) module.actualize();
    }
}
