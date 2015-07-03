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
package UI.ShortcutsUI;

import UI.Controller;
import UI.Shortcuts;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;

/**
 * Módulo que edita o añade atajos de teclado al programa.
 */
public class ShortcutsUIController extends Controller {
    @FXML
    AnchorPane all;
    @FXML
    Label noValidLabel;
    @FXML
    TextArea textInfo;
    @FXML
    ChoiceBox optionControl;
    @FXML
    ChoiceBox optionAlt;
    @FXML
    ChoiceBox optionShift;
    @FXML
    ChoiceBox optionLetter;
    @FXML
    Button acceptButton;

    private static KeyCombination keyCombUsing;

    /**
     * {@inheritDoc}
     * @param url
     * @param rb 
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        optionControl.getItems().addAll("", "Ctrl");
        optionAlt.getItems().addAll("", "Alt");
        optionShift.getItems().addAll("", "Shift");
        optionLetter.getItems().addAll("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        noValidLabel.setVisible(false);
        textInfo.setText("");
    }

    @Override
    public void show() {
        optionControl.setValue("");
        optionAlt.setValue("");
        optionShift.setValue("");
        optionLetter.setValue("A");
        
        keyCombUsing = null;
        
        noValidLabel.setVisible(false);
        type.show();
    }
    
    /**
     * Actualiza un atajo de teclado.
     * @param keyComb Nueva combinación de teclas del atajo.
     * @param text Nuevo texto del atajo.
     */
    public void editShortcut(KeyCombination keyComb, String text) {
        show();
        keyCombUsing = keyComb;
        textInfo.setText(text);
        String[] keys = keyComb.toString().split("\\+|-");
        for (String key : keys) {
            switch (key) {
                case "Ctrl":
                    optionControl.setValue("Ctrl");
                    break;
                case "Alt":
                    optionAlt.setValue("Alt");
                    break;
                case "Shift":
                    optionShift.setValue("Shift");
                    break;
                default:
                    optionLetter.setValue(key);
                    break;
            }
        }
    }
    
    /**
     * Acepta y guarda las acciones realizadas en el módulo en caso de ser correctas.
     */
    public void accept() {
        String key = "";
        if(optionControl.getValue() != "" && optionControl.getValue() != null)  key += optionControl.getValue() + "+";
        if(optionAlt.getValue() != "" && optionAlt.getValue() != null) key += optionAlt.getValue() + "+";
        if(optionShift.getValue() != "" && optionShift.getValue() != null) key += optionShift.getValue() + "+";
        
        if(key.equals("")) {
            noValidLabel.setVisible(true);
            noValidLabel.setText("Introduzca una combinación válida");
        } else if(textInfo.getText() == null || textInfo.getText().equals("")) {
            noValidLabel.setVisible(true);
            noValidLabel.setText("Introduzca un texto válido");
        } else {
            key += optionLetter.getValue();
            if(keyCombUsing == null) {
                Shortcuts.deleteShortcut(key);
            } else if(!Shortcuts.compare(key, keyCombUsing)) Shortcuts.deleteShortcut(keyCombUsing);
            noValidLabel.setVisible(false);
            
            Shortcuts.addShortCut(key, textInfo.getText());
            hide();
        }
    }
}
