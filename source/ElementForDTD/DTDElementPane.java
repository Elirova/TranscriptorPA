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

package ElementForDTD;

import ElementForTree.XMLElementPane;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

/**
 * Elemento de interfaz que contiene el nombre del elemento DTD asociado y un
 * componente para modificar su configuración.
 */
public class DTDElementPane extends BorderPane {
    private ComboBox listType;
    
    public DTDElementPane(String name, String type, boolean exist){
        setLeft(new Label(name));
        
        if(exist){
            Label defined = new Label("(Ya definido)");
            defined.setMinWidth(120);
            setRight(defined);
        } else {
            listType = new ComboBox(XMLElementPane.getValues());
            if(XMLElementPane.getValues().contains(type)) listType.setValue(type);
            else listType.setValue("Texto");

            listType.setMinWidth(120);
            listType.setMinHeight(30);
            listType.getStyleClass().add("listSpinner");
            setRight(listType);
        }
    }
    
    /**
     * @return El tipo de configuración.
     */
    public String getType() {
        return (listType != null)? listType.getValue().toString() : "Texto";
    }
}
