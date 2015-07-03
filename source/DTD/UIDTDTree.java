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

package DTD;

import Config.Config;
import Config.FileManager;
import ElementForDTD.DTDElementPane;
import ElementForDTD.DTDChild;
import ElementForDTD.DTDItem;
import ElementForDTD.DTDOr;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.TreeItem;

/**
 * Crea y almacena el árbol de interfaz con la que se tratará la configuración
 * de los elementos de un DTD.
 */
public class UIDTDTree {
    private static final HashMap<String, DTDElementPane> elements = new HashMap<>();
    private static TreeItem rootNode;
    
    /**
     * Dibuja el árbol DTD en la interfaz a partir del nodo indicado.
     * 
     * @param root Nodo raíz del árbol o subárbol a dibujar.
     * @return Nodo raíz del árbol con la información de la interfaz.
     */
    public static TreeItem printTree(DTDItem root) {
        elements.clear();
        rootNode = new TreeItem(root.getName());
        rootNode.setExpanded(true);
        if(root.hasChilds()) addChilds(root.getChilds(), rootNode);
        return rootNode;
    }
    
    /**
     * Devuelve el nodo raíz del árbol que contiene la información de interfaz del DTD.
     * @return Nodo raíz del árbol que contiene la información de interfaz del DTD.
     */
    public static TreeItem getTree() {
        return rootNode;
    }
    
    /**
     * Añade un elemento de interfaz al nodo padre indicado.
     * 
     * @param element Elemento a añadir.
     * @param parent Nodo padre del elemento a añadir.
     */
    private static void addElement(DTDItem element, TreeItem parent) {
        if(element.hasChilds()) {
            TreeItem node = new TreeItem(element.getName());
            node.setExpanded(true);
            parent.getChildren().add(node);
            addChilds(element.getChilds(), node);
        } else {
            addFinalElement(element, parent, false);
        }
    }
    
    /**
     * Añade una lista de elementos al nodo padre indicado.
     * 
     * @param childs Lista de hijos a añadir.
     * @param parent Nodo padre al que añadir los hijos.
     */
    private static void addChilds(ArrayList<DTDChild> childs, TreeItem parent) {
        for(DTDChild child : childs)
            if(child instanceof DTDOr) addChilds(((DTDOr)child).getChilds(), parent);
            else {
                DTDItem element = child.getElement();
                String nameElement = element.getName();
                
                if(elements.containsKey(nameElement))addFinalElement(element, parent, true);
                else {
                    elements.put(nameElement, null);
                    addElement(element, parent);
                }
            }
    }

    /**
     * Añade un elemento sin hijos (nodo hoja).
     * 
     * @param element Elemento a añadir.
     * @param parent Nodo padre al que añadir el elemento.
     * @param exist Si el elemento a añadir existe ya dentro del árbol o no.
     */
    private static void addFinalElement(DTDItem element, TreeItem parent, boolean exist) {
        DTDElementPane pane = new DTDElementPane(element.getName(), Config.getType(element.getName()), exist);
        parent.getChildren().add(new TreeItem(pane));
        if(!exist)elements.put(element.getName(), pane);
    }
    
    /**
     * Guarda el árbol de información.
     */
    public static void saveData() {
        for (Map.Entry<String, DTDElementPane> entry : elements.entrySet()) {
            DTDElementPane value = entry.getValue();
            if(value != null) Config.addElement(entry.getKey(), value.getType());
        }
    }
    
    /**
     * Guarda la información de los elementos de la interfaz.
     * 
     * @param name Nombre del DTD asociado.
     */
    public static void saveDTDConfig(String name) {
        if(name == null || name.equals("")) name = Config.nameDTD;
        FileManager.saveDTDConfig(name, getXML());
    }
    
    /**
     * Crea el XML asociado a la configuración del DTD.
     * 
     * @return Cadena que contiene el XML generado.
     */
    public static String getXML() {
        String xml = "<elementos>\n";
        
        for (Map.Entry<String, DTDElementPane> entry : elements.entrySet()) {
            DTDElementPane value = entry.getValue();
            if(value != null) {
                xml += "\t<elemento nombre=\"" + entry.getKey() + "\" tipo=\"" + value.getType() + "\" formato=\"\"/>\n";
            }
        }
        xml += "</elementos>";
        
        return xml;
    }
}
