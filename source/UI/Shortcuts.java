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

package UI;

import Config.Config;
import DTD.DTDTree;
import XML.UIXMLTree;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import javafx.util.StringConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Clase que guarda los atajos actuales y permite modificarlos.
 */
public abstract class Shortcuts {
    static Map<KeyCombination, Map<String, Object>> data = new HashMap<>();
    static AnchorPane all;
    static ObservableList<Map> allData = FXCollections.observableArrayList();
    static Map<KeyCombination, EventHandler<KeyEvent>> handlers = new HashMap<>();
    
    static TableView table;
    public static final String cShortcut = "Atajo", cText = "Texto", cEdit = "";
    
    /**
     * Lee un fichero con atajos y los carga.
     * @param all Panel donde afectarán los atajos de teclado.
     * @param tableShortcuts Tabla dodne se mostrarán los atajos.
     * @param path Dirección del fichero que contiene los atajos.
     */
    public static void read(AnchorPane all, TableView tableShortcuts, String path) {
        Shortcuts.all = all;
        table = tableShortcuts;
        allData.clear();
        data.clear();
        handlers.clear();
        initializeTable();
        
        try {
            DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();

            // Obtenemos la lista de atajos del xml de configuración
            NodeList childs;
            if(path == null) path = Config.pathShortCuts;
            if(new File(path).exists()) childs = db.parse(path).getDocumentElement().getChildNodes();
            else return; // ??
            
            for( int i = 0; i < childs.getLength(); i++){
                Node child = childs.item(i);

                if(!child.getNodeName().equals("atajo") || 
                    child.getAttributes().getNamedItem("teclas") == null ||
                    child.getAttributes().getNamedItem("texto") == null) continue;
                
                String key  = child.getAttributes().getNamedItem("teclas").getNodeValue();
                String text = child.getAttributes().getNamedItem("texto").getNodeValue();
                
                KeyCombination keyComb = getKeyCombination(key);
                
                if(keyComb == null) {
                    Logger.getLogger(Shortcuts.class.getName()).log(Level.WARNING, "Error leyendo atajo");
                    continue;
                }
                
                addShortCut(keyComb, text);
            }
            
        }catch(ParserConfigurationException | SAXException | IOException ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Guarda los atajos.
     * @param path Dirección donde se guardarán los atajos.
     */
    public static void save(String path) {
        if(path != null && !path.endsWith(".xml")) path += ".xml";
        try(FileWriter fichero = new FileWriter((path == null)? Config.pathShortCuts : path)){
            String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<atajos>\n";
            
            for (Map.Entry<KeyCombination, Map<String, Object>> entrySet : data.entrySet()) {
                KeyCombination key = entrySet.getKey();
                Map<String, Object> value = entrySet.getValue();
                text += "\t<atajo teclas=\"" + key.toString() + "\" texto=\""+ value.get(cText) + "\"/>\n";
            }
            
            text += "</atajos>";
            
            fichero.write(text + "\r\n");
            fichero.close();
            Logger.getLogger(DTDTree.class.getName()).log(Level.FINE, null, "Guardados atajos");
        }catch(Exception ex){
            Logger.getLogger(DTDTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Compara dos atajos.
     * @param key Atajo uno.
     * @param comb Atajo dos.
     * @return Si son iguales o no.
     */
    public static boolean compare(String key, KeyCombination comb) {
        if(comb == null) return false;
        return getKeyCombination(key)==comb;
    }
    
    /**
     * Devuelve el KeyCombination de una cadena.
     * @param keycomb Cadena a leer.
     * @return Atajo resultante.
     */
    private static KeyCombination getKeyCombination(String keycomb) {
        String[] keys = keycomb.split("\\+|-");
        
        if(KeyCode.getKeyCode(keys[keys.length-1]) == null) return null;
        
        KeyCombination.Modifier[] combs = new KeyCombination.Modifier[keys.length-1];

        int j = 0;
        for(int k = 0; k < combs.length; k++){
            switch(keys[k]){
                case "CNTR":
                case "CONTROL":
                case "Ctrl":
                case "Control":
                    combs[j] = KeyCombination.CONTROL_DOWN;
                    j++;
                    break;
                case "ALT":
                case "Alt":
                    combs[j] = KeyCombination.ALT_DOWN;
                    j++;
                    break;
                case "SHIFT":
                case "Shift":
                    combs[j] = KeyCombination.SHIFT_DOWN;
                    j++;
                    break;
                default:
                    return null;
            }
        }
        return new KeyCodeCombination(KeyCode.getKeyCode(keys[keys.length-1]), combs);
    }
    
    /**
     * Añade un atajo.
     * @param key Atajo a añadir.
     * @param text Texto del atajo.
     */ 
    public static void addShortCut(String key, String text) {
        addShortCut(getKeyCombination(key), text);
    }
    
    /**
     * Añade un atajo.
     * @param key Atajo a añadir.
     * @param text Texto del atajo.
     */
    public static void addShortCut(final KeyCombination key, final String text) {
        if(!data.containsKey(key)) { // No existe el atajo
            if(handlers.containsKey(key)) {
                all.removeEventHandler(KeyEvent.KEY_RELEASED, handlers.get(key));
            }
            handlers.put(key, new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    if (key.match(event)) {
                        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Pulsado atajo con texto: {0}", (String)data.get(key).get(cText));
                        javafx.scene.Node node = UIXMLTree.getFocusedNode();
                        
                        if(node instanceof TextArea) 
                            ((TextArea)node).replaceSelection((String)data.get(key).get(cText));
                        else if (node instanceof TextField)
                            ((TextField)node).replaceSelection((String)data.get(key).get(cText));
                    }
                }
            });
            
            all.addEventHandler(KeyEvent.KEY_RELEASED, handlers.get(key));
            
            Button bEdit = new Button("...");
            bEdit.setMaxWidth(30);
            bEdit.setMinWidth(30);
            
            Button bDelete = new Button("X");
            bDelete.setMaxWidth(30);
            bDelete.setMinWidth(30);
            
            HBox box = new HBox();
            box.getChildren().addAll(bEdit, bDelete);
            box.setSpacing(5);
            
            final Map <String, Object> row = new HashMap<>();
            row.put(cShortcut, key.toString());
            row.put(cText, text);
            row.put(cEdit, box);
            
            bEdit.getStyleClass().add("button-default");
            bDelete.getStyleClass().add("button-default");
            
            bEdit.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    UI.editShortcut(key, text);
                }
            });
            
            bDelete.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    deleteShortcut(key);
                }
            });
            
            data.put(key, row);
            allData.add(row);
            
        } else data.get(key).put(cText, text); // Existe el atajo y hay que modificarlo
    }
    
    /**
     * Elimina un atajo.
     * @param key Atajo a eliminar.
     */
    public static void deleteShortcut(String key) {
        KeyCombination keyComb = getKeyCombination(key);
        deleteShortcut(keyComb);
    }
    
    /**
     * Elimina un atajo.
     * @param keyComb Atajo a eliminar.
     */
    public static void deleteShortcut(KeyCombination keyComb) {
        if(data.containsKey(keyComb)) {
            allData.remove(data.get(keyComb));
            data.remove(keyComb);
            
            all.removeEventHandler(KeyEvent.KEY_RELEASED, handlers.get(keyComb));
            handlers.remove(keyComb);
        }
    }

    /**
     * Inicializa la tabla con los atajos.
     */
    private static void initializeTable() {
        TableColumn<Map, String> firstDataColumn = new TableColumn<>(cShortcut);
        TableColumn<Map, String> secondDataColumn = new TableColumn<>(cText);
        TableColumn<Map, HBox> thirdDataColumn = new TableColumn<>(cEdit);
 
        firstDataColumn.setCellValueFactory(new MapValueFactory(cShortcut));
        secondDataColumn.setCellValueFactory(new MapValueFactory(cText));
        thirdDataColumn.setCellValueFactory(new MapValueFactory(cEdit));
        
        firstDataColumn.setMaxWidth(120);
        firstDataColumn.setMinWidth(120);
        thirdDataColumn.setMaxWidth(72);
        thirdDataColumn.setMinWidth(72);
 
        table.setItems(allData);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.getColumns().setAll(firstDataColumn, secondDataColumn, thirdDataColumn);
        
        Callback<TableColumn<Map, String>, TableCell<Map, String>>
            cellFactoryForMap = new Callback<TableColumn<Map, String>,
                TableCell<Map, String>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        return new TextFieldTableCell(new StringConverter() {
                            @Override
                            public String toString(Object t) {
                                return t.toString();
                            }
                            @Override
                            public Object fromString(String string) {
                                return string;
                            }                                    
                        });
                    }
        };
        firstDataColumn.setCellFactory(cellFactoryForMap);
        secondDataColumn.setCellFactory(cellFactoryForMap);
    }
}
