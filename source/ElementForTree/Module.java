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

import ElementForDTD.DTDChild.Amount;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.w3c.dom.Element;

/**
 * Modulo de interfaz para mostrar elementos de un XML.
 */
public class Module {
    protected int currentAmount;
    protected Amount amount;
    protected VBox elementPane;
    protected VBox buttonPane;
    protected final VBox bgPane, parentPane;
    protected final Element parentElement;
    protected final UIOr orParent;
    
    public Module(VBox parent, Element parentElement, Amount amount, UIOr orParent) {
        this.parentElement = parentElement;
        this.amount = amount;
        currentAmount = 0;
        this.orParent = orParent;
        
        parentPane = parent;
        bgPane = new VBox();
        bgPane.setSpacing(10);
        
        elementPane = new VBox();
        elementPane.setSpacing(10);
        
        buttonPane = new VBox();
        buttonPane.setSpacing(10);
        buttonPane.setPadding(new Insets(0, 10, 0, 10));
        
        bgPane.getChildren().addAll(elementPane, buttonPane);
        parent.getChildren().add(bgPane);
    }
    
    /**
     * Muestra los botones de añadir elementos asociados al módulo.
     * @param visible Si se van a mostrar u ocultar.
     */
    protected void showButtons(boolean visible) {
        if(!visible) bgPane.getChildren().remove(buttonPane);
        else if(!bgPane.getChildren().contains(buttonPane))bgPane.getChildren().add(buttonPane);
    }
    
    /**
     * Elimina el módulo de su padre.
     */
    public void removeFromParent() {
        Logger.getLogger(Module.class.getName()).log(Level.INFO, "Eliminando módulo de su padre OR");
        parentPane.getChildren().remove(bgPane);
        if(orParent != null) orParent.removeChild(this);
    }
    
    /**
     * Resta uno a la cantidad de elementos del módulo.
     */
    protected void removeAmount() {
        currentAmount--;
        if(currentAmount == 0 && canDelete()) removeFromParent();
    }
    
    /**
     * Añade uno a la cantidad de elementos del módulo.
     */
    protected void addAmount() {
        currentAmount++;
    }

    /**
     * @return Panel donde se visualizan los hijos del módulo.
     */
    public VBox getAddingPane() {
        return null;
    }

    /**
     * Devuelve una cadena con la información del módulo.
     * @param indent Indentación que lleva ese elemento dentro del XML.
     * @return Cadena con la información del módulo.
     */
    public String toString(int indent) {
        return null;
    }

    /**
     * @return El nodo que tiene el focus actual o null en caso de no existir.
     */
    public Node getFocusNode() {
        return null;
    }
    
    public void setFocus() { }

    /**
     * Actualiza los valores del XML según la interfaz.
     */
    public void actualize() {}

    /**
     * @return True si se puede eliminar un elemento dentro del módulo, false en caso contrario.
     */
    public boolean canDelete() {
        if(orParent != null && orParent.canDelete()) return true;
        return !(currentAmount == 0 || (currentAmount == 1 && (amount == Amount.UNO || amount == Amount.MAS)));
    }
}