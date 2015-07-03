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
import ElementForDTD.DTDChild;
import ElementForDTD.DTDItem;
import ElementForDTD.DTDOr;
import java.util.HashMap;

/**
 * Crea y almacena el árbol correspondiente a un DTD leído.
 */
public abstract class DTDTree {
    private static final String[] allQuantities = {"+", "?", "*"};
    private static HashMap<String, DTDItem> elements;
    private static DTDItem root;
    private static String dtd, dtdName;
    
    /**
     * Crea un arbol DTD a partir de un fichero DTD.
     * 
     * @param path Ruta del fichero que contiene el DTD.
     * @return Si se ha podido o no crear el arbol DTD.
     */
    public static boolean createDTD(String path) {
        if(elements == null) elements = new HashMap<>();
        else elements.clear();
        
        // Obtenemos el nombre del DTD por su ruta de guardado
        if(path == null) DTDTree.dtdName = Config.nameDTD;
        else {
            String names[] = path.split("/");
            DTDTree.dtdName = names[names.length-1].replace(".dtd","");
        }
        
        dtd = FileManager.readDTD(path, dtdName);
        if(dtd != null) makeTree();
        
        return (dtd != null);
    }
    
    /**
     * Crea el árbol DTD.
     */
    private static void makeTree() {
        String [] parts = dtd.split(", | |,");
        boolean elem = false, atr = false, com = false, or = false, or2 = false;
        String name = null, nameChild, amount;
        for(int i = 0; i < parts.length; i++) {
            String part = parts[i];
            
            // Eliminamos comentarios y lineas vacías
            if (part.length() == 0) continue;
            if(com) {
                if(part.contains("-->")) com = false;
                continue;
            } else if(part.contains("<!--")) {
                com = true;
                continue;
            }
            
            // Comprobamos si vamos a tratar con un elemento o un atributo
            if(part.contains("ELEMENT")) { 
                elem = true; 
                name = parts[++i];
                if(!elements.containsKey(name)) {
                    if(elements.isEmpty()) {
                        root = new DTDItem(name);
                        elements.put(name, root);
                    } else elements.put(name, new DTDItem(name));
                }
            } else if(part.contains("ATTLIST")) {
                atr = true; 
                name = parts[i+1];
            } else {
                if(elem) {
                    if(part.contains(">")) { part = part.replace(">", ""); elem = false; }             
                    // Comprobamos si hay un OR
                     if(part.contains("(")) {
                        if(part.contains("|") || (i+1 < parts.length && !parts[i+1].contains("(") && parts[i+1].contains("|"))) {
                            if(!part.contains(")")) or = true;
                            DTDOr childOr = new DTDOr(null, "");
                            elements.get(name).addChild(childOr);
                        }
                    }
                    if(part.contains(")") && or) {
                        // Miramos si tiene cantidad
                        for (String cont : allQuantities) {
                            if (part.contains(cont)) {
                                ((DTDOr)elements.get(name).getLastChild()).setAmount(cont);
                                part = part.replace(cont, "");
                                break;
                            }
                        }
                        or2 = true;
                    }
                     
                    nameChild = part.replace("(", "").replace("|", "").replace(")", "");
                    if (nameChild.length() == 0) continue;
                    
                    // Obtenemos la cantidad
                    amount = "1";
                    for (String cont : allQuantities) {
                        if (part.contains(cont)) {
                            amount = cont;
                            nameChild = nameChild.replace(cont, "");
                            break;
                        }
                    }
                    
                    if(!elements.containsKey(nameChild)) elements.put(nameChild, new DTDItem(nameChild));

                    if(nameChild.contains("#PCDATA")) continue;
                    if(or) {
                        ((DTDOr)elements.get(name).getLastChild()).addChild(new DTDChild(elements.get(nameChild), amount));
                        
                        if(or2) or = or2 = false;
                        
                    } else elements.get(name).addChild(new DTDChild(elements.get(nameChild), amount));
                } else if(atr) {
                    elements.get(name).setAttribute(parts[i+1]);
                    i += 3;
                    atr = false;
                }
            }
        }
    }
    
    /**
     * Devuelve el nodo raíz del árbol.
     * 
     * @return Nodo raíz del árbol.
     */
    public static DTDItem getRoot() {
        return root;
    }
}
