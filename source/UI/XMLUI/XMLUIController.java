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

package UI.XMLUI;

import Config.Config;
import Config.ExtensionFileFilter;
import Config.FileManager;
import DTD.DTDTree;
import UI.UI;
import UI.Shortcuts;
import XML.UIXMLTree;
import XML.XMLTree;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;

/**
 * Controlador de la interfaz del programa.
 */
public class XMLUIController implements Initializable {
    @FXML
    AnchorPane all;
    @FXML
    TableView tableShortcuts;
    @FXML
    StackPane startPopUpPanel;
    @FXML
    StackPane DTDWindowPanel;
    @FXML
    StackPane shortcutsEditPanel;
    @FXML
    StackPane selectDTDWindowPanel;
    @FXML
    StackPane newDTDPanel;
    @FXML
    StackPane newXMLPanel;
    @FXML
    StackPane errorDTDFromXMLPanel;
    @FXML
    StackPane errorMessagePanel;
    @FXML
    ScrollPane scroll;

    /**
     * {@inheritDoc}
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Shortcuts.read(all, tableShortcuts, null);
        
        UI.setXmlUIController(this);
        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Inicializada la clase: {0}", getClass());
    }
    
    /**
     * Asigna una ruta de guardado de ficheros XML y PDF.
     */
    @FXML
    public void setPathSaves() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("Selecciona una ruta de guardado");
        File defaultDirectory = new File(".");
        chooser.setInitialDirectory(defaultDirectory);
        File selectedDirectory = chooser.showDialog(UI.getStage());
        Config.pathSaves = selectedDirectory.getPath();
        
        UI.showMessage("La nueva ruta de guardado de XML y PDF es: \n" + Config.pathSaves, "Aceptar");
        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Nueva ruta de guardado de ficheros XML y PDF: {0}", Config.pathSaves);
    }
    
    /**
     * Abre la interdfaz para seleccionar un DTD.
     */
    @FXML
    public void showSelectDTD() {
        UI.showSelectDTDWindow();
    }
    
    /**
     * Carga un XML vacio.
     */
    public void loadXML() {
        Config.readConfig();
        UIXMLTree.newEmptyXML(DTDTree.getRoot());
        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Cargando nuevo XML");
    }
    
    /**
     * @return Scroll que contendrá la interfaz del XML.
     */
    public ScrollPane getScrollXML() {
        return scroll;
    }
    
    /**
     * Crea un nuevo DTD.
     */
    @FXML
    public void newDTD() {
        UI.showNewDTDWindow();
    }
    
    /**
     * Crea un nuevo XML vacío.
     */
    @FXML
    public void newEmptyXML() {
        UI.showNewXMLWindow();
        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Mostrando XML");
    }
    
    /**
     * Importa un XML.
     */
    @FXML
    public void importXML() {
        UI.importXML(null);
        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Importado XML");
    }
    
    /**
     * Guarda el XML actual, mostrando un mensaje de la localización del fichero
     * donde se ha guardado.
     */
    @FXML
    public void saveXML() {
        String name = XMLTree.getName();
        if(name == null) UI.showMessage("No hay ningún documento a guardar", "Aceptar");
        else UI.showMessage("XML guardado correctamente en:\n" + exportXML(), "Aceptar");
    }
    
    /**
     * Guarda el XML actual.
     * @return Dirección del fichero donde se ha guardado.
     */
    public String exportXML() {
        String path, name = XMLTree.getName();
        File file = new File(Config.pathSaves + name.substring(0, 4) + "/XML");
        if(!file.exists()) file.mkdirs();
        path = file.getPath() + "/" + name + ".xml";
        
        XMLTree.saveXML(path, true);
        
        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Guardado XML");
        return path;
    }
    
    /**
     * Guarda el PDF y el XML del XML actual.
     */
    @FXML
    public void savePDF() {
        String name = XMLTree.getName();
        if(name == null) {
            UI.showMessage("No hay ningún documento a guardar", "Aceptar");
            return;
        }
        String path;
        File file = new File(Config.pathSaves + name.substring(0, 4) + "/PDF");
        if(!file.exists()) file.mkdirs();
        path = file.getPath() + "/" + name + ".pdf";
        
        exportXML();
        
        String result = FileManager.exportPDF(path, Config.nameDTD);
        UI.showMessage((result == null)? "PDF guardado correctamente en:\n" + path : result, "Aceptar");
        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Guardado PDF");
    }
    
    /**
     * Importa un fichero de atajos.
     */
    @FXML
    public void importShortcuts() {
        String path = ExtensionFileFilter.getFileName(".", "Fichero de atajos", "xml");
        Shortcuts.read(all, tableShortcuts, path);
        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Atajos importados");
    }
    
    /**
     * Exporta a fichero los atajos actuales.
     */
    @FXML
    public void exportShortCuts() {
        String path = ExtensionFileFilter.getFileName(".", "Fichero de atajos", "xml", 1);
        Shortcuts.save(null);
        Shortcuts.save(path);
        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Atajos exportados");
    }
    
    /**
     * Abre la interfaz para añadir un atajo.
     */
    @FXML
    public void addShortCuts() {
        UI.showShortcutWindow();
    }
    
    /**
     * Cierra el programa.
     */
    @FXML
    public void close() {
        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Cerrando programa");
        System.exit(0);
    }
}
