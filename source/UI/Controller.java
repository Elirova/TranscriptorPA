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

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

/**
 * Ventana de interfaz para mostrar módulos.
 */
public abstract class Controller implements Initializable {
    public static enum WindowType {
        SHORTCUT("ShortcutsUI/ShortcutsUI.fxml"),
        NEWDTD("NewDTD/NewDTD.fxml"), 
        NEWXML("NewXML/NewXML.fxml"), 
        SELECTDTD("SelectDTD/SelectDTDUI.fxml"), 
        ERRORDTDFROMXML("ErrorReadDTDFromXML/ErrorDTDFromXML.fxml"), 
        EDITDTD("DTDUI/DTDUI.fxml"), 
        MESSAGE("Message/Message.fxml");
        
        StackPane stack;
        String path;
        Controller controller;
        
        private WindowType(String path) {
            this.path = path;
            stack = new StackPane();
            stack.setVisible(false);
            
            AnchorPane.setBottomAnchor(stack, 0d);
            AnchorPane.setTopAnchor(stack, 0d);
            AnchorPane.setRightAnchor(stack, 0d);
            AnchorPane.setLeftAnchor(stack, 0d);
            
            stack.setStyle("-fx-background-color: #0005;");
        }
        
        public void setParent(Pane paneParent) {
            paneParent.getChildren().add(stack);
            try {
                Parent parent = (Parent)FXMLLoader.load(UI.class.getResource(path));
                controller.createWindow(this, parent);
            } catch (Exception ex) {
                Logger.getLogger(UI.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        public void setController(Controller c) {
            controller = c;
            controller.setType(this);
        }
        
        public Controller getController() {
            return controller;
        }
        
        public void addChildren(Node child) {
            stack.getChildren().add(child);
        }
        
        public void show() {
            stack.setVisible(true);
        }
        
        public void hide() {
            stack.setVisible(false);
        }
    }
    
    protected Pane window;
    protected WindowType type;
    protected static WindowType aux;
    
    /**
     * Carga la ventana.
     * @param paneParent Padre de la ventana a crear.
     */
    public static void start(Pane paneParent) {
        for (WindowType type : WindowType.values()) {
            aux = type;
            type.setParent(paneParent);
        }
    }
    
    /**
     * Le asigna el tipo de venta a a ser.
     * @param type Tipo de ventana.
     */
    public void setType(WindowType type) {
        this.type = type;
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        window = new Pane();
        aux.setController(this);
        
        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Inicializada la clase: {0}", getClass());
    }
    
    /**
     * Crea la ventana.
     * @param type Tipo de ventana a crear.
     * @param parent Elemento que será el padre de la ventana en la interfaz.
     */
    public void createWindow(WindowType type, Parent parent) {
        window.getChildren().add(parent);
        window.setPrefSize(((Pane)parent).getPrefWidth(), ((Pane)parent).getPrefHeight());
        window.setMaxSize(window.getPrefWidth(), window.getPrefHeight());
        type.addChildren(window);
    }
    
    /**
     * Muestra la ventana.
     */
    public void show() {
        type.show();
    }
    
    /**
     * Cierra la ventana y muestra la anterior.
     */
    public void close() {
        type.hide();
        UI.showLastScreen();
    }
    
    /**
     * Cierra la ventana.
     */
    public void hide() {
        type.hide();
        UI.showMainWindow();
    }
}
