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

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;

/**
 * Elemento de interfaz para seleccionar entre dos posibles resultados de texto.
 */
public class SwitchButton extends Label {
    private SimpleBooleanProperty switchedOn = new SimpleBooleanProperty(true);

    public SwitchButton() {
        setPrefHeight(20);
        Button switchBtn = new Button();
        switchBtn.setPrefWidth(30);
        switchBtn.setPrefHeight(30);
        switchBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                switchedOn.set(!switchedOn.get());
            }
        });

        setGraphic(switchBtn);

        switchedOn.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> ov, Boolean t, Boolean t1) {
                if (t1) {
                    setText("Si");
                    setStyle("-fx-background-color: green;-fx-text-fill:white;");
                    setContentDisplay(ContentDisplay.RIGHT);
                } else {
                    setText("No");
                    setStyle("-fx-background-color: grey; -fx-text-fill:black;");
                    setContentDisplay(ContentDisplay.LEFT);
                }
            }
        });

        switchedOn.set(false);
    }

    public SimpleBooleanProperty switchOnProperty() { return switchedOn; }
}