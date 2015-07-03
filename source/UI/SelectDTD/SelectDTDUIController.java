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

package UI.SelectDTD;

import Config.Config;
import UI.Controller;
import UI.UI;
import XML.XMLTree;
import java.io.File;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * Controlador de la interfaz de seleccionar un DTD entre los existentes.
 */
public class SelectDTDUIController extends Controller {
    @FXML
    Label noValidLabel;
    @FXML
    ListView listDTD;
    
    /**
     * Muestra la ventana.
     */
    @Override
    public void show() {
        File[] files = new File(Config.pathAllDTD).listFiles();
        if(listDTD == null) listDTD = new ListView();
        listDTD.getItems().clear();
        for (File file : files) {
            if(file.isDirectory()) {
                File f = new File(file.getPath() + "/" + file.getName() + ".dtd");
                if(f.exists()) listDTD.getItems().add(file.getName());
            }
        }
        
        noValidLabel.setVisible(false);
        type.show();
    }
    
    /**
     * Abre la interfaz para crear un nuevo DTD.
     */
    @FXML
    public void newDTD() {
        UI.showNewDTDWindow();
    }
    
    /**
     * Acepta las acciones y cierra la ventana.
     */
    @FXML
    public void accept() {
        String text = (String)listDTD.getSelectionModel().getSelectedItem();
        if(text == null) noValidLabel.setVisible(true);
        else {
            Config.setNewBaseDTD(text);
            if(UI.getLastScreen() == null) { // Para editar la configuración del DTD
                type.hide();
                UI.showEditDTDWindow(null);
            } else { // Para cargar un nuevo XML
                XMLTree.loadXML(true);
                hide();
            }
        }
    }
}