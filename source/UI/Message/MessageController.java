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

package UI.Message;

import UI.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Muestra una ventana de información con un mensaje.
 */
public class MessageController extends Controller {
    @FXML
    private Label text;
    @FXML
    private Button button;
    
    /**
     * Muestra la ventana.
     * @param message Mensaje a mostrar en la ventana.
     * @param buttonText texto que contendrá el botón de cerrar la ventana.
     */
    public void show(String message, String buttonText) {
        text.setText(message);
        button.setText(buttonText);
        type.show();
    }
}
