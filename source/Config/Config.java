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

package Config;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Almacena, guarda y lee los datos de configuración del programa.
 */
public abstract class Config {
    private static final HashMap<String, String> elements = new HashMap<>();
    private static final HashMap<String, ArrayList<String>> typeList = new HashMap<>();
    private static Document doc, docConfig;
    private static Node dtd;
    
    public static final String basePath      = ".sessionTranscriber";
    public static final String pathTemp      = ".sessionTranscriber/.temp/";
    public static final String pathConfig    = ".sessionTranscriber/config.xml";
    public static final String pathAllDTD    = ".sessionTranscriber/dtd/";
    public static final String pathShortCuts = ".sessionTranscriber/atajos/atajos.xml";
    public static String pathSaves           = "SesionesGuardadas/";
    
    public static String pathBaseDTD         = "";
    public static String pathBaseDTDConfig   = "";
    public static String nameDTD             = "";
    
    public static final String version       = "v1.1.0";
    
    /**
     * Lee la configuración de inicio del programa.
     */
    public static void readStartConfig() {
        Logger.getLogger(Config.class.getName()).log(Level.INFO, "Leyendo configuración inicial");
        exists();
        
        elements.clear();
        typeList.clear();
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pathConfig);

            // Obtenemos la lista de elementos del xml de configuración
            NodeList info = doc.getDocumentElement().getChildNodes();
            
            Node child;
            NodeList childs;
            for( int i = 0; i < info.getLength(); i++){
                child = info.item(i);
                
                switch (child.getNodeName()) {
                    case "dtd":
                        dtd = child;
                        String path = child.getTextContent();
                        
                        if(path != null && !path.equals("")) {
                            nameDTD = path;
                            pathBaseDTD = pathAllDTD + path + "/" + path + ".dtd";
                            pathBaseDTDConfig = pathAllDTD + path + "/" + path + "_config.xml";
                        } else nameDTD = pathBaseDTD = pathBaseDTDConfig = "";
                        break;
                    case "listas":
                        childs = child.getChildNodes();
                        for(int j = 0; j < childs.getLength(); j++){ // Leyendo hijos de listas (lista)
                            child = childs.item(j);
                            if(child.getNodeName().equals("lista") && child.hasAttributes()) {
                                String name = child.getAttributes().getNamedItem("nombre").getNodeValue();
                                ArrayList<String> items = new ArrayList<>();
                                NodeList cs = child.getChildNodes();
                                for(int k = 0; k < cs.getLength(); k++){ // Objetos dentro de la lista
                                    child = cs.item(k);
                                    if(child.hasAttributes())
                                        items.add(child.getAttributes().getNamedItem("nombre").getNodeValue());
                                }
                                typeList.put(name, items);
                            }
                        }
                        break;
                    case "elementos":
                        readElements(child.getChildNodes());
                        break;
                    case "version":
                        child.setTextContent(Config.version);
                        saveConfig();
                        break;
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static File f = null;
    /**
     * Crea un nuevo fichero en la ruta indicada.
     * @param path Ruta donde crear el fichero.
     */
    private static void create(String path) {
        f = new File(path);
        if(!f.exists()) f.mkdirs();
    }
    
    /**
     * @return Si hay una nueva versión del programa
     */
    private static boolean newVersion() {
        f = new File(pathConfig);
        if(!f.exists()) return true;
        try {
            doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pathConfig);
        } catch (Exception ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Obtenemos la lista de elementos del xml de configuración
        NodeList info = doc.getDocumentElement().getChildNodes();

        Node child;
        for( int i = 0; i < info.getLength(); i++){
            child = info.item(i);
            if(child.getNodeName().equals("version")) return !child.getTextContent().equals(Config.version); 
        }
        
        return true;
    }
    
    /**
     * Comprueba que existen todos los ficheros y directorios necesarios para el
     * uso del programa y los crea en caso contrario.
     */
    private static void exists() {
        if(!newVersion()) return;
        
        Logger.getLogger(Config.class.getName()).log(Level.INFO, "Creando nueva configuración");
        f = new File(basePath);
        FileManager.deleteFolder(f);
        
        //Crea todos los ficheros base
        create(basePath);
        create(pathAllDTD);
        create(pathTemp);
        create(pathShortCuts.replace("atajos.xml", ""));
        
        // Comprueba que existan los ficheros, en caso contrario los copia
        f = new File(pathConfig);
        if(!f.exists()) {
            ClassLoader cLoader = Config.class.getClassLoader();
            
            try {
                String[] names = {"BOPA", "DSPA", "DSCA", "DSCB", "DSSA"};
                String[] extensions = {".dtd", ".css", ".xsl", "_config.xml"};
                String path, dir;
                for (String name : names) {
                    create(pathAllDTD + name + "/images");
                    dir = name + "/" + name;
                    path = "Assets/dtd/" + dir;
                    for (String ext : extensions) {
                        Files.copy(cLoader.getResourceAsStream(path + ext), Paths.get(pathAllDTD + dir + ext));
                    }
                    Files.copy(cLoader.getResourceAsStream("Assets/dtd/" + name + "/images/title.png"), 
                        Paths.get(pathAllDTD + name + "/" + "images/title.png"));
                }
                
                Files.copy(cLoader.getResourceAsStream("Assets/config.xml"), Paths.get(pathConfig));
                Files.copy(cLoader.getResourceAsStream("Assets/shortcuts/atajos.xml"), Paths.get(pathShortCuts));
            } catch (Exception ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Lee la configuración del fichero DTD por defecto.
     */
    public static void readConfig() {
        if(!pathBaseDTDConfig.equals("")) {
            try {
                f = new File(pathBaseDTDConfig);
                if(f.exists()) {
                    docConfig = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(pathBaseDTDConfig);
                    readElements(docConfig.getDocumentElement().getChildNodes());   
                }
            } catch (Exception ex) {
                Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    /**
     * Lee los elementos de la configuración del DTD por defecto.
     * 
     * @param childs Lista de elementos a leer.
     */
    private static void readElements(NodeList childs) {
        Node child;
        for(int j = 0; j < childs.getLength(); j++){
            child = childs.item(j);
            if(child.getNodeName().equals("elemento") && child.hasAttributes()) {
                if(child.getAttributes().getNamedItem("nombre") == null) continue;
                    String name  = child.getAttributes().getNamedItem("nombre").getNodeValue();
                    String type = (child.getAttributes().getNamedItem("tipo") == null)?
                        "Texto" : child.getAttributes().getNamedItem("tipo").getNodeValue();
                    // Metemos un nuevo elemento en el mapa con su tipo y formato correspondiente
                    elements.put(name, type);
            }
        }
    }
    
    /**
     * Asigna un nuevo DTD por defecto.
     * 
     * @param name Nombre del nuevo DTD por defecto.
     */
    public static void setNewBaseDTD(String name) {
        if(name != null) {
            dtd.setTextContent(name);

            if(!name.equals("")) {
                nameDTD = name;
                pathBaseDTD = pathAllDTD + name + "/" + name + ".dtd";
                pathBaseDTDConfig = pathAllDTD + name + "/" + name + "_config.xml";
            } else pathBaseDTD = pathBaseDTDConfig = "";

            saveConfig();
            Logger.getLogger(Config.class.getName()).log(Level.INFO, "Asignado nuevo DTD por defecto: {0}", name);
        }
    }
    
    /**
     * Añade un elemento a la configuración actual del DTD.
     * 
     * @param name Nombre del elemento a añadir.
     * @param type Tipo que tendrá el elemento a añadir.
     */
    public static void addElement(String name, String type) {
        elements.put(name, type);
    }
    
    /**
     * Guarda la configuración.
     */
    public static void saveConfig() {
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(doc), new StreamResult(new File(pathConfig)));
            Logger.getLogger(Config.class.getName()).log(Level.INFO, "Guardando configuración");
        } catch (Exception ex) {
              Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Devuelve el tipo del elemento indicado.
     * 
     * @param name Nombre del elemento.
     * @return Tipo del elemento.
     */
    public static String getType (String name) {
        return (elements.containsKey(name))? elements.get(name) : "Texto";
    }
    
    /**
     * Devuelve la lista de valores asociados a un elemento.
     * @param name Nombre del elemento.
     * @return Lista de valores.
     */
    public static ObservableList getListElement(String name) {
        return (typeList.containsKey(name))? FXCollections.observableArrayList(typeList.get(name)) : null;
    }
}
