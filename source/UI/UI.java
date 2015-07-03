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

import Config.ExtensionFileFilter;
import UI.Controller.WindowType;
import UI.Message.MessageController;
import UI.ShortcutsUI.ShortcutsUIController;
import UI.XMLUI.XMLUIController;
import XML.XMLTree;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import main.Main;

/**
 * Clase que gestiona la interfaz.
 */
public abstract class UI {
    private static AnchorPane root;
    private static WindowType backScreen, lastScreen, actualScreen;
    private static XMLUIController mainController;
    private static Stage stage;
    
    /**
     * Establece el elemento raíz de toda la interfaz.
     * @param root Elemento raíz de toda la interfaz.
     */
    public static void setRoot(AnchorPane root) {
        UI.root = root;
    }
    
    /**
     * Muestra el programa.
     * @param stage Escenario del programa.
     * @param title Título del programa.
     */
    public static void show(Stage stage, String title) { 
        UI.stage = stage;
        try {
            root = (AnchorPane)FXMLLoader.load(UI.class.getResource("XMLUI/XMLUI.fxml"));
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Controller.start(root);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.show();
    }
    
    /**
     * Devuelve el stage del programa.
     * @return 
     */
    public static Stage getStage() {
        return stage;
    }
    
    /**
     * @return Última ventana visualizada.
     */
    public static WindowType getLastScreen() {
        return lastScreen;
    }
    
    /**
     * Nueva ventana visualizada.
     * @param screen Ventana visualizada.
     */
    public static void setScreen(WindowType screen) {
        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Cambiando pantalla. Actual: {0} -- Anterior: {1}", new Object[]{screen, actualScreen});
        backScreen = lastScreen;
        lastScreen = actualScreen;
        actualScreen = screen;
    }
    
    /**
     * Muestra la ventana visualizada anteriormente.
     */
    public static void showLastScreen() {
        Logger.getLogger(UI.class.getName()).log(Level.INFO, "Cambiando pantalla. Actual: {0} -- Anterior: {1}", new Object[]{lastScreen, backScreen});
        actualScreen = lastScreen;
        lastScreen = backScreen;
        backScreen = null;
        
        if(actualScreen != null) actualScreen.show();
    }
    
    // XML
    
    /**
     * @param pane Panel en el que se visualizará el XML.
     */
    public static void setPaneXML(Pane pane) {
        mainController.getScrollXML().setContent(pane);
    }
    
    public static ScrollPane getScrollPane() {
        return mainController.getScrollXML();
    }
    
    /**
     * Importa un XML de un fichero dado.
     * @param path Ruta del fichero con el XML a importar.
     */
    public static void importXML(String path) {
        setScreen(null);
        if(path == null) path = ExtensionFileFilter.getFileName(".", "Fichero XML", "xml");
        if(path != null) XMLTree.readXML(path);
    }
    
    /**
     * Carga un XML.
     */
    public static void loadXML() {
        setScreen(null);
        mainController.loadXML();
    }
    
    /**
     * Crea un nuevo XML vacío.
     * @param dtdPath Ruta donde se encuentra el DTD asociado al XML a crear.
     * @param name Nombre que tendrá el XML.
     */
    public static void newXML(String dtdPath, String name) {
        setScreen(null);
        mainController.getScrollXML().setContent(null);
        XMLTree.newXML(dtdPath, name);
    }
    
    /**
     * Muestra una ventana de información con un mensaje.
     * @param text Mensaje a mostrar.
     * @param button Mensaje que contendrá el botón de cerrar la ventana.
     */
    public static void showMessage(String text, String button) {
        setScreen(WindowType.MESSAGE);
        ((MessageController) WindowType.MESSAGE.getController()).show(text, button);
    }
    
    // Shortcuts
    
    /**
     * Edita un atajo de teclado.
     * @param keyComb Atajo de teclado a editar.
     * @param text Nuevo texto que contendrá el atajo.
     */
    public static void editShortcut(KeyCombination keyComb, String text) {
        setScreen(WindowType.SHORTCUT);
        ((ShortcutsUIController) WindowType.SHORTCUT.getController()).editShortcut(keyComb, text);
    }
    
    /**
     * Añade un atajo de teclado nuevo.
     */
    public static void showShortcutWindow() {
        setScreen(WindowType.SHORTCUT);
        WindowType.SHORTCUT.getController().show();
    }
    
    // Windows
    
    public static void showMainWindow() {
        setScreen(null);
    }
    
    /**
     * Muestra la ventana de añadir un nuevo DTD.
     */
    public static void showNewDTDWindow() {
        setScreen(WindowType.NEWDTD);
        WindowType.NEWDTD.getController().show();
    }
    
    /**
     * Muestra la ventana de añadir un nuevo XML.
     */
    public static void showNewXMLWindow() {
        setScreen(WindowType.NEWXML);
        WindowType.NEWXML.getController().show();
    }
    
    /**
     * Muestra la ventana de editar un DTD.
     * @param path Dirección del fichero con el DTD a editar (null si se toma el
     * DTD almacenado en la configuración).
     */
    public static void showEditDTDWindow(String path) {
        setScreen(WindowType.EDITDTD);
        WindowType.EDITDTD.getController().show();
    }
    
    /**
     * Muestra la ventana de mensaje de error.
     */
    public static void showMessageWindow() {
        setScreen(WindowType.ERRORDTDFROMXML);
        WindowType.ERRORDTDFROMXML.getController().show();
    }
    
    /**
     * Muestra la ventana de seleccionar un DTD.
     */
    public static void showSelectDTDWindow() {
        setScreen(WindowType.SELECTDTD);
        WindowType.SELECTDTD.getController().show();
    }
    
    /**
     * Asigna un nuevo controlador de la interfaz de XML.
     * 
     * @param aXmlUIController Controlador a asignar.
     */
    public static void setXmlUIController(XMLUIController aXmlUIController) {
        mainController = aXmlUIController;
    }
}