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

package UI.NewXML;

import Config.Config;
import UI.Controller;
import UI.UI;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javax.swing.text.html.ImageView;

/**
 * Controlador de la interfaz de añadir un nuevo XML.
 */
public class NewXMLController extends Controller {
    @FXML
    Label noValidLabel;
    @FXML
    ChoiceBox name1;
    @FXML
    TextField name2;
    @FXML
    TextField name3;
    @FXML
    ImageView imageName1;
    @FXML
    ImageView imageName2;
    @FXML
    ImageView imageName3;
    
    static String nameXML, nameDTD;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        ArrayList<String> list = new ArrayList<>();
        
        File[] files = new File(Config.pathAllDTD).listFiles();
        for (File file : files) {
            if(file.isDirectory()) {
                File f = new File(file.getPath() + "/" + file.getName() + ".dtd");
                if(f.exists()) list.add(file.getName());
            }
        }
        
        name1.setItems(FXCollections.observableArrayList(list));
        name1.setValue(list.get(0));
    }
    
    /**
     * Muestra la ventana.
     */
    @Override
    public void show() {
        ArrayList<String> list = new ArrayList<>();
        
        File[] files = new File(Config.pathAllDTD).listFiles();
        for (File file : files) {
            if(file.isDirectory()) {
                File f = new File(file.getPath() + "/" + file.getName() + ".dtd");
                if(f.exists()) list.add(file.getName());
            }
        }
        
        name1.setItems(FXCollections.observableArrayList(list));
        name1.setValue(list.get(0));
        
        noValidLabel.setText("Seleccione nombre para su XML");
        type.show();
    }
    
    /**
     * @return Si soin válidos los parámetros introducidos para el nombre del XML.
     */
    private boolean isValid() {
        nameXML = "";
        nameDTD = name1.getValue().toString();
        nameXML += nameDTD;
        String str = name2.getText();
        if(str.equals("") || !str.matches("\\d|\\d\\d")) {
            noValidLabel.setText("Legislatura no válida");
            return false;
        }
        
        int cont = Integer.parseInt(str);
       
        if(cont < 10) nameXML += "0" + cont;
        else nameXML += cont;
        
        str = name3.getText();
        if(str.equals("") || !str.matches("\\d|\\d\\d|\\d\\d\\d|\\d\\d\\d\\d")) {
            noValidLabel.setText("Número no válido");
            return false;
        }
        cont = Integer.parseInt(str);
        
        if(cont < 10) nameXML += "000" + cont;
        else if(cont < 100) nameXML += "00" + cont;
        else if(cont < 1000) nameXML += "0" + cont;
        else nameXML += cont;
        
        return true;
    }
    
    /**
     * Acepta las acciones y cierra la ventana.
     */
    public void accept() {
        if(isValid()) {
            Config.setNewBaseDTD(nameDTD);
            UI.newXML(null, nameXML);
            hide();
        }
    }
}
