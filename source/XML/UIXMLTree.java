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

package XML;

import Config.Config;
import DTD.DTDTree;
import ElementForDTD.DTDChild;
import ElementForDTD.DTDItem;
import ElementForDTD.DTDOr;
import ElementForTree.Int;
import ElementForTree.UIChild;
import ElementForTree.UIItem;
import ElementForTree.UIOr;
import UI.UI;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Interfaz para un XML.
 */
public class UIXMLTree {
    private static final VBox pane = new VBox();
    static UIItem root;
    
    /**
     * Crea la interfaz de usuario para un XML ya existente, teniendo en cuenta su DTD asociando en caso de existir.
     * 
     * @param withDTD Si tiene un DTD asociado o no.
     * @param doc Documento que contiene el XML.
     */
    public static void newExistentXML(boolean withDTD, Document doc) {
        Config.readConfig();
        pane.getChildren().clear();
        UI.setPaneXML(pane);
        
        root = (withDTD)? addItem(DTDTree.getRoot(), doc.getDocumentElement(), null, true) :
            addItem(doc.getDocumentElement(), null);
    }
    
    /**
     * Crea el árbol de interfaz de un XML vacío a partir del árbol de DTD indicado.
     * 
     * @param root Elemento raíz del árbol que contiene la información de DTD.
//     * @param parent Elemento de interfaz en el cual se mostrará el XML.
     */
    public static void newEmptyXML(DTDItem root) {
        pane.getChildren().clear();
        UI.setPaneXML(pane);
        UIXMLTree.root = new UIItem(root, XMLTree.setRoot(root.getName()), pane, true, false);
    }
    
    /**
     * Devuelve el elemento con focus dentro del árbol o null si no existe.
     * 
     * @return Nodo con focus o null si no hay ninguno con focus.
     */
    public static Node getFocusedNode() {
        return root.getFocusNode();
    }
    
    /**
     * Sustituye el actual árbol de interfaz por el indicado. 
     * 
     * @param item Raíz del árbol que sustituirá al actual.
     */
    public static void setRoot(UIItem item) {
        root = item;
    }
    
    public static UIItem getRoot() {
        return root;
    }
    
    
    // ************************ Creación de XML sin DTD ************************ //
    
    /**
     * Crea un item basado en un elemento XML indicado y lo asocia a su UIChild padre.
     * 
     * @param xml Elemento XML que tomará el item como base para crear su estructura.
     * @param parentChild UIChild padre del item a crear.
     * @return Elemento de interfaz creado.
     */
    private static UIItem addItem(Element xml, UIChild parentChild) {
        Int cont = new Int(0);
        UIItem ui = (parentChild == null)? new UIItem(null, xml, pane, false, false) : 
            new UIItem(null, xml, parentChild, parentChild.getAddingPane(), false, false);
        
        NodeList nodes = xml.getChildNodes();
        
        if(nodes != null && nodes.getLength() > 0 && nodes.item(0).getNodeType() != 3) { // Tiene hijos
            for(int j = 0; j < nodes.getLength(); j++) {
                UIChild child = addChild(xml, ui.getChildPane(), cont);
                if(child != null) ui.addModule(child);
            }
        } else ui.setText(xml.getTextContent());
        
        return ui;
    }
    
    /**
     * Crea un contenedor de items (UIChild) usando como base el elemento XML indicado,
     * asignándole el panel en el cual se visualizará.
     * 
     * @param parent Elemento XML en el que se basará para crearse.
     * @param parentPane Panel en el que se visualizará el elemento de interfaz creado.
     * @param cont Indica el número de hijo que será el elemento creado con respecto al padre (Múltiplo de 2).
     * @return Elemento de interfaz creado.
     */
    private static UIChild addChild(Element parent, VBox parentPane, Int cont) {
        NodeList childs = parent.getChildNodes();
        if(childs.getLength() > cont.getNumber()) {
            UIChild ui = new UIChild(parent, parentPane, false, null);

            Element child = (Element)(childs.item(cont.getNumber()));
            ui.newItem(addItem(child, ui));
            cont.addNumber(1);
            
            return ui;
        }
        return null;
    }
    
    
    // ************************ Creación de XML con DTD ************************ //
    
    /**
     * Crea un item basado en un elemento DTD y un elemento XML indicados y lo asocia a su UIChild padre.
     * 
     * @param dtd Elemento DTD en el que se basará para crear el elemento de interfaz.
     * @param xml Elemento XML en el que se basará para crear el elemento de interfaz.
     * @param parentChild UIChild padre del item a crear.
     * @return Elemento de interfaz creado.
     */
    private static UIItem addItem(DTDItem dtd, Element xml, UIChild parentChild, boolean existent) {
        Int cont = new Int(existent? 0 : 1);
        UIItem ui;
        if(parentChild == null) ui = new UIItem(dtd, xml, pane, false, false);
        else ui = new UIItem(dtd, xml, parentChild, parentChild.getAddingPane(), false, parentChild.canDelete());
        NodeList nodes = xml.getChildNodes();
        
        if(nodes != null && nodes.getLength() > 0) { // Tiene hijos
            for(DTDChild child : dtd.getChilds()) { // Mira los hijos que tiene el item en el DTD
                if(child instanceof DTDOr) ui.addModule(addOr((DTDOr)child, xml, ui.getChildPane(), cont, existent));
                else ui.addModule(addChild(child, xml, ui.getChildPane(), cont, existent));
            }
            if(nodes.getLength() == 1) ui.setText(xml.getTextContent());
        }
        
        return ui;
    }
    
    /**
     * Crea un contenedor de items (UIChild) usando como base el elemento XML 
     * y el elemento DTD indicado, asignándole el panel en el cual se visualizará.
     * 
     * @param dtd Elemento DTD en el que se basará para crearse.
     * @param parent Elemento XML en el que se basará para crearse.
     * @param parentPane Panel en el que se visualizará el elemento de interfaz creado.
     * @param cont Indica el número de hijo que será el elemento creado con respecto al padre (Múltiplo de 2).
     * @return Elemento de interfaz creado.
     */
    private static UIChild addChild(DTDChild dtd, Element parent, VBox parentPane, Int cont, boolean existent) {
        NodeList childs = parent.getChildNodes();
        UIChild ui = new UIChild(dtd, parent, parentPane, false, null);
        
        // Para cada hijo de el elemento parent se mira si los nombres son iguales
        // Si lo son añade un item a la UI con el texto que tenga ese hijo
        // Se repite hasta que deja de ser los nombres iguales
        boolean flag = false;
        for(int i = cont.getNumber(); i < childs.getLength(); i++) {
            if(childs.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE){
                Element child = (Element)(childs.item(i));
                if(child.getNodeName().equals(dtd.getElement().getName())) {
                    ui.newItem(addItem(dtd.getElement(), child, ui, existent));
                    flag = true;
                    cont.addNumber(existent? 1 : 2);
                } else if(flag) break;
            }
        }
        return ui;
    }
    
    /**
     * Crea un contenedor de items (UIOr) usando como base el elemento XML 
     * y el elemento DTD indicado, asignándole el panel en el cual se visualizará.
     * 
     * @param dtd Elemento DTD en el que se basará para crearse.
     * @param parent Elemento XML en el que se basará para crearse.
     * @param parentPane Panel en el que se visualizará el elemento de interfaz creado.
     * @param cont Indica el número de hijo que será el elemento creado con respecto al padre (Múltiplo de 2).
     * @return Elemento de interfaz creado.
     */
    private static UIOr addOr(DTDOr dtd, Element parent, VBox parentPane, Int cont, boolean existent) {
        NodeList childs = parent.getChildNodes();
        UIOr ui = new UIOr(dtd, parent, parentPane, null);
        
        Map<String, DTDChild> dtdChilds = new HashMap<>();
        for(DTDChild child : dtd.getChilds()) dtdChilds.put(child.getElement().getName(), child);
        
        boolean flag = false;
        for(int i = cont.getNumber(); i < childs.getLength(); i++) {
            if(childs.item(i).getNodeType() == org.w3c.dom.Node.ELEMENT_NODE){
                Element child = (Element)(childs.item(i));
                if(dtdChilds.containsKey(child.getTagName())) {
                    ui.addChild(addChild(dtdChilds.get(child.getTagName()), parent, parentPane, cont, existent));
                    flag = true;
                } else if(flag) break;
            } 
        }
        
        return ui;
    }
}