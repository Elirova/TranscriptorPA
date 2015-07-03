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
import Config.FileManager;
import DTD.DTDTree;
import ElementForTree.Module;
import ElementForTree.UIChild;
import ElementForTree.UIItem;
import ElementForTree.UIOr;
import UI.UI;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Árbol de XML.
 */
public class XMLTree {
    static Document doc;
    static Element root;
    private static String name;
    
    /**
     * Carga la interfaz del XML actual.
     * 
     * @param withDTD Si el XML tiene un DTD asociado o no.
     */
    public static void loadXML(boolean withDTD) {
        if(withDTD) DTDTree.createDTD(null);
        Config.readConfig();
        UIXMLTree.newExistentXML(withDTD, doc);
    }
    
    /**
     * Crea un nuevo arbol a partir del arbol existente en UIXMLTree.
     */
    public static void createXMLfromUI() {
        if(doc == null) {
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            } catch (Exception ex) {
                Logger.getLogger(XMLTree.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(doc.getDocumentElement() != null) {
            doc.removeChild(doc.getDocumentElement());
            root = null;
        }
        UIItem rootItem = UIXMLTree.getRoot();
        
        root = doc.createElement(rootItem.getName());
        doc.appendChild(root);
        
        readItem(root, rootItem);
        rootItem.actualize();
    }
    
    /**
     * Lee los hijos de un elemento.
     * @param parent Elemento en el que se guardaran los hijos leidos.
     * @param child Hijos a leer.
     */
    private static void readChild(Element parent, UIChild child) {
        UIItem[] items = child.getItems();
        if(items == null) return;
        
        Element elem;
        for(UIItem item : items) {
            elem = doc.createElement(item.getName());
            parent.appendChild(elem);
            readItem(elem, item);
        }
    }
    
    /**
     * Lee los or de un elemento.
     * @param parent Elemento en el que se guardaran los or leidos.
     * @param or Or a leer.
     */
    private static void readOr(Element parent, UIOr or) {
        Module[] modules = or.getChilds();
        if(modules == null) return;
        
        for(Module module : modules) {
            if(module instanceof UIChild) readChild(parent, (UIChild) module);
            else readOr(parent, (UIOr) module);
        }
    }
    
    /**
     * Lee un elemento.
     * @param element Elemento en el que se guardaran los elementos leidos.
     * @param parent Elemento a leer.
     */
    private static void readItem(Element element, UIItem parent) {
        parent.setElement(element);
        
        Module[] childs = parent.getChilds();
        if(childs != null) { // Nodo hoja, leemos su información
            for(Module child : childs) {
                if(child instanceof UIChild) readChild(element, (UIChild) child);
                else readOr(element, (UIOr) child); 
            }
        }
    }

    /**
     * Carga la interfaz de un XML vacío a partir de un DTD existente.
     * 
     * @param path Ruta del fichero DTD asociado en caso de existir.
     * @param name Nombre que tendrá el fichero XML al guardarse.
     */
    public static void newXML(String path, String name) {
        XMLTree.name = name;
        DTDTree.createDTD(path);
        Config.readConfig();
        UIXMLTree.newEmptyXML(DTDTree.getRoot());
    }
    
    /**
     * Lee un XML.
     * 
     * @param path Ruta del fichero en el que se encuentra el XML.
//     * @param lastType Última ventana visitada.
     */
    public static void readXML(String path) {
        if(path == null) return;
        String dtdPath;
        doc = FileManager.readXML(path);
        if(doc == null) return;

        String[] names = path.split("/");
        name = names[names.length-1].replace(".xml", "");
        
        // Se comprueba que el XML tenga un DTD asociado
        if(doc.getDoctype() == null) {
            UI.showMessageWindow();
            return;
        }
        String dtdName = doc.getDoctype().getSystemId().replace(".dtd", "");
        // Se obtiene el path del DTD
        dtdPath = Config.pathAllDTD + dtdName + "/" + doc.getDoctype().getSystemId();
        
        // Se comprueba que el DTD existe
        File dtdFile =  new File(dtdPath);
        if(dtdFile.exists()) {
            DTDTree.createDTD(dtdPath);
            Config.setNewBaseDTD(dtdName);
            Config.readConfig();

            UIXMLTree.newExistentXML(true, doc);
        } else UI.showMessageWindow();
    }
    
    /**
     * Elimina el árbol actual y asigna como raíz el elemento raíz del documento indicado.
     * 
     * @param doc Documento que contiene el nuevo árbol XML.
     */
    public static void setDocument(Document doc) {
        XMLTree.doc = doc;
        root = XMLTree.doc.getDocumentElement();
    }
    
    /**
     * Elimina el árbol actual y crea un nuevo elemento raíz con el nombre indicado.
     * 
     * @param name Nombre del nuevo elemento raíz del árbol.
     * @return Elemento raíz del árbol.
     */
    public static Element setRoot(String name) {
        if(doc == null) {
            try {
                doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            } catch (Exception ex) {
                Logger.getLogger(XMLTree.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else if(doc.getDocumentElement() != null) {
            doc.removeChild(doc.getDocumentElement());
            root = null;
        }
        root = doc.createElement(name);
        doc.appendChild(root);
        
        return root;
    }

    /**
     * Devuelve el nodo raíz del árbol actual.
     * 
     * @return Raíz del árbol.
     */
    public static Element getRoot() {
        return root;
    }
    
    /**
     * Añade un hijo al padre indicado.
     * 
     * @param parent Padre al que añadir un hijo.
     * @param name Nombre del hijo a añadir.
     * @param text Texto, en caso de existir, que contendrá el hijo a añadir.
     * @return Elemento hijo añadido.
     */
    public static Element addChild(Element parent, String name, String text) {
        Element elem = doc.createElement(name);
        if(text != null) elem.setTextContent(text);
        
        parent.appendChild(elem);
        return elem;
    }
    
    /**
     * Añade texto a un elemento.
     * 
     * @param elem Elemento al que añadir el texto.
     * @param text texto a añadir.
     */
    public static void setText(Element elem, String text) {
        if(text != null) elem.setTextContent(text);
    }
    
    /**
     * Elimina el hijo indicado del elemento padre indicado dentro del árbol en caso de existir ambos.
     * 
     * @param parent Elemento al que se le eliminará un hijo.
     * @param child Elemento a eliminar.
     */
    public static void removeChild(Element parent, Element child) {
        parent.removeChild(child);
    }
    
    /**
     * Guarda el XMl actual en el fichero con la ruta indicada.
     * 
     * @param path Ruta donde se guardará el XML.
     * @param withDTD Si se va a añadir el DTD o no.
     */
    public static void saveXML(String path, boolean withDTD) {
        XMLTree.createXMLfromUI();
        FileManager.saveXML(path, doc, Config.nameDTD, withDTD);
    } 

    /**
     * @return El nombre del XML.
     */
    public static String getName() {
        return name;
    }
}
