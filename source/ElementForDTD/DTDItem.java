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

import java.util.ArrayList;

/**
 * Elemento que contiene una lista de hijos y atributos asociados a un 
 * elemento DTD.
 */
public class DTDItem {
    private final String name;
    private final ArrayList<DTDChild> childs;
    private final ArrayList<String> attributes;
    
    public DTDItem(String name) {
        this.name = name;
        childs = new ArrayList<>();
        attributes = new ArrayList<>();
    }

    /**
     * @return Nombre.
     */
    public String getName() {
        return name;
    }
    
    /**
     * Añade un hijo a la lista existente.
     * @param child 
     */
    public void addChild(DTDChild child) {
        childs.add(child);
        
    }
    
    /**
     * @return Lista de hijos del elemento.
     */
    public ArrayList<DTDChild> getChilds() {
        return childs;
    }
    
    /**
     * @return Último hijo de la lista.
     */
    public DTDChild getLastChild() {
        return childs.get(childs.size()-1);
    }
    
    /**
     * Añade un atributo a la lista existente.
     * 
     * @param attribute Nombre del atributo a añadir.
     */
    public void setAttribute(String attribute) {
        attributes.add(attribute);
    }
    
    /**
     * @return Lista de atributos que tiene el elemento.
     */
    public ArrayList<String> getAttributes() {
        return attributes;
    }
    
    /**
     * @return Si tiene o no atributos.
     */
    public boolean hasAttributes() {
        return !attributes.isEmpty();
    }
    
    /**
     * @return Si tiene o no hijos.
     */
    public boolean hasChilds() {
        return !childs.isEmpty();
    }
}
