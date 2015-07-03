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

import javafx.scene.layout.Pane;
import org.w3c.dom.Element;

/**
 * Elemento de interfaz para introducir la informacion de un atributo XML.
 */
public class XMLAttributePane extends XMLElementPane {
    String name;

    public XMLAttributePane(String type, Pane parent, String name, String value) {
        super(type, parent, name);
        this.name = name;
        if(value != null) setText(value);
    }
    
    @Override
    public void actualize(Element element) {
        element.setAttribute(name, getText());
    }
}