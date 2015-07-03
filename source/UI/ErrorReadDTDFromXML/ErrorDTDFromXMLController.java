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

package UI.ErrorReadDTDFromXML;

import UI.Controller;
import UI.UI;
import XML.XMLTree;

/**
 * Controlador de la interfaz de mensaje de error al abrir un XML sin DTD.
 */
public class ErrorDTDFromXMLController extends Controller {
    /**
     * Abre la ventana de seleccionar un DTD.
     */
    public void selectDTD() {
        type.hide();
        UI.showSelectDTDWindow();
    }
    
    /**
     * Abre la ventana de crear un DTD.
     */
    public void newDTD() {
        type.hide();
        UI.showNewDTDWindow();
    }
    
    /**
     * Carga el XML sin DTD.
     */
    public void loadWithoutDTD() {
        hide();
        XMLTree.loadXML(false);
    }
}