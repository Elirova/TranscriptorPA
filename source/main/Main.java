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

package main;

import Config.Config;
import UI.UI;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) {
        Config.readStartConfig();
        
        UI.show(stage, "Transcriptor de diarios de sesiones y boletines oficiales del Parlamento de Andalucía");
    }

    /**
     * @param args Argumentos de la línea de comandos.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
