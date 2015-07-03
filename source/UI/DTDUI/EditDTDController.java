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

package UI.DTDUI;

import DTD.DTDTree;
import DTD.UIDTDTree;
import UI.Controller;
import UI.UI;
import javafx.fxml.FXML;
import javafx.scene.control.TreeView;

/**
 * Controlador de la interfaz de editar la configuración de un DTD.
 */
public class EditDTDController extends Controller {
    @FXML
    TreeView dtdTree;
    
    String path;
    
    /**
     * Muestra la ventana.
     */
    @Override
    public void show() {
        boolean result = loadDTD(path);
        if(result) type.show();
    }
    
    /**
     * Carga un DTD de fichero.
     * @param path Ruta del fichero con el DTD a cargar.
     * @return Si se ha podido cargar o no el fichero.
     */
    public boolean loadDTD(String path) {
        boolean result = DTDTree.createDTD(path);
        if(result) dtdTree.setRoot(UIDTDTree.printTree(DTDTree.getRoot()));
        
        return result;
    }
    
    /**
     * Asigna el archivo desde donde se cargara el DTD.
     * @param path Ruta de donde se cargara el DTD.
     */
    public void setPath(String path) {
        this.path = path;
    }
    
    /**
     * Acepta las acciones y cierra la ventana.
     */
    public void accept() {
        UIDTDTree.saveData();
        if(UI.getLastScreen() == WindowType.SELECTDTD) {
            hide();
            UIDTDTree.saveDTDConfig(null);
        } else close();
    }
}