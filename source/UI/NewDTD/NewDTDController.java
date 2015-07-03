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

package UI.NewDTD;

import Config.Config;
import Config.ExtensionFileFilter;
import Config.FileManager;
import DTD.UIDTDTree;
import UI.Controller;
import UI.DTDUI.EditDTDController;
import UI.UI;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

/**
 * Controlador de la interfaz de añadir un nuevo DTD.
 */
public class NewDTDController extends Controller {
    // 0 -> Path DTD
    // 1 -> Name DTD
    // 2 -> Config DTD
    // 3 -> Path XSLT
    // 4 -> Path CSS
    @FXML
    Label dtdOpenName;
    @FXML
    TextField nameText;
    @FXML
    Button openConfigButton;
    @FXML
    Button createConfigButton;
    @FXML
    Pane pane1;
    @FXML
    Pane pane2;
    @FXML
    Pane pane3;
    @FXML
    Pane pane4;
    @FXML
    Pane pane5;
    
    static String pathDTD, pathConfigDTD, pathXSLT, pathCSS, name;
    static boolean[] options;
    static boolean create;
    static Pane[] panels;

    /**
     * {@inheritDoc}
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        options = new boolean[5];
        panels = new Pane[]{pane1, pane2, pane3, pane4, pane5};
    }
    
    @Override
    public void show() {
        super.show();
        for(int i = 0; i < panels.length; i++) {
            if(i == 2) setOption(i, true);
            else setOption(i, false);
            panels[i].getStyleClass().setAll("circle-default");
        }
        
        pathDTD = pathConfigDTD = pathCSS = pathXSLT = name = "";
    }
    
    /**
     * Guarda la configuración.
     */
    public void saveConfig() {
        create = true;
    }
    
    /**
     * Establece la opción indicada como correcta o incorrecta.
     * @param number Número de la opción a cambiar.
     * @param correct Si pasa a ser correcta o no la opción.
     */
    private void setOption(int number, boolean correct) {
        options[number] = correct;
        if(correct) panels[number].getStyleClass().setAll("circle-correct"); 
        else panels[number].getStyleClass().setAll("circle-incorrect"); 
    }
    
    /**
     * @return Si todas las opciones están correctas.
     */
    private boolean isValid() {
        boolean flag = true;
        for (int i = 0; i < options.length; i++) {
            if(!options[i]) {
                setOption(i, false);
                flag = false;
            } else setOption(i, true);
        }
        return flag;
    }
    
    /**
     * Abre un seleccionador de ficheros para seleccionar la ruta del DTD.
     */
    public void openNewDTD() {
        pathDTD = ExtensionFileFilter.getFileName(".", "Fichero DTD", "dtd");
        if(pathDTD != null) {
            String [] paths = pathDTD.split("/");
            name = paths[paths.length-1].replace(".dtd", "");
            nameText.setText(name);
            setOption(1, true);
            openConfigButton.setDisable(false);
            createConfigButton.setDisable(false);
            ((EditDTDController)WindowType.EDITDTD.getController()).setPath(pathDTD);
        }
        setOption(0, pathDTD != null);
    }
    
    /**
     * Abre un seleccionador de ficheros para seleccionar la ruta de la configuración del DTD.
     */
    public void openNewConfigDTD() {
        pathConfigDTD = ExtensionFileFilter.getFileName(".", "Fichero de configuración (XML)", "xml");
        if(pathConfigDTD == null) pathConfigDTD = "";
    }
    
    /**
     * 
     * Abre la interfaz para cambiar la configuración del DTD.
     */
    public void openCreateNewConfigDTD() {
        type.hide();
        UI.showEditDTDWindow(pathDTD);
    }
    
    /**
     * Abre un seleccionador de ficheros para seleccionar la ruta del XSLT.
     */
    public void openNewXSLT() {
        pathXSLT = ExtensionFileFilter.getFileName(".", "Fichero de configuración (XML)", "xml");
        setOption(3, pathXSLT != null && !pathXSLT.equals(""));
        if(pathXSLT == null) pathXSLT = "";
    }
    
    /**
     * Abre un seleccionador de ficheros para seleccionar la ruta de la configuración del CSS.
     */
    public void openNewCSS() {
        pathCSS = ExtensionFileFilter.getFileName(".", "Fichero de configuración (XML)", "xml");
        setOption(4, pathCSS != null && !pathCSS.equals(""));
        if(pathCSS == null) pathCSS = "";
    }
    
    /**
     * Acepta las acciones y cierra la ventana.
     */
    public void accept() {
        name = nameText.getText();
        setOption(1, (name != null && !name.equals("")));
        if(isValid()) {
            FileManager.importDTD(pathDTD, name);
            
            if(create || pathConfigDTD.equals("")) UIDTDTree.saveDTDConfig(name);
            else FileManager.importDTDConfig(pathConfigDTD, name);
            
            Config.setNewBaseDTD(name);
            UI.showMessage("Creado proyecto: " + name, "Aceptar");
            UI.loadXML();
            hide();
        }
    }
}
