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

package Config;

import java.io.File;
import java.util.*;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

/** 
 * Selector de ficheros que te permite seleccionar laas extensiones mostradas.
 */
public class ExtensionFileFilter extends FileFilter {
    public static final int LOAD = 0, SAVE = 1;
    private String description;
    private boolean allowDirectories;
    private final Hashtable extensionsTable = new Hashtable();
    private boolean allowAll = false;

    public ExtensionFileFilter (boolean allowDirectories) {
        this.allowDirectories = allowDirectories;
    }
    
    public ExtensionFileFilter() {
        this(true);
    }
  
    /** 
     * Muestra una ventana de selección de fichero.
     * 
     * @param initialDirectory Directorio que se mostrará al abrir la ventana.
     * @param description Descripción que contendrá la ventana.
     * @param extension
     * @return Path del fichero seleccionado.
     */
    public static String getFileName(String initialDirectory, String description, String extension) {
        return getFileName(initialDirectory, description, new String[]{ extension }, LOAD);
    }  

    /** 
     * Muestra una ventana de selección de fichero.
     * 
     * @param initialDirectory Directorio que se mostrará al abrir la ventana.
     * @param description Descripción que contendrá la ventana.
     * @param extension
     * @param mode Modo en el que se visualizará la ventana (0 -> Abrir fichero, 1 -> Guardar fichero)
     * @return Path del fichero seleccionado.
     */
    public static String getFileName(String initialDirectory, String description, String extension, int mode) {
        return getFileName(initialDirectory, description, new String[]{ extension }, mode);
    }

    /** 
     * Muestra una ventana de selección de fichero.
     * 
     * @param initialDirectory Directorio que se mostrará al abrir la ventana.
     * @param description Descripción que contendrá la ventana.
     * @param extensions Extensiones permitidas.
     * @return Path del fichero seleccionado.
     */
    public static String getFileName(String initialDirectory, String description, String[] extensions) {
        return getFileName(initialDirectory, description, extensions, LOAD);
    }


    /** 
     * Muestra una ventana de selección de fichero.
     * 
     * @param initialDirectory Directorio que se mostrará al abrir la ventana.
     * @param description Descripción que contendrá la ventana.
     * @param extensions Extensiones permitidas.
     * @param mode Modo en el que se visualizará la ventana (0 -> Abrir fichero, 1 -> Guardar fichero)
     * @return Path del fichero seleccionado.
     */

    public static String getFileName(String initialDirectory, String description, String[] extensions, int mode) {
        ExtensionFileFilter filter = new ExtensionFileFilter();
        filter.setDescription(description);
        
        for (String extension : extensions) filter.addExtension(extension, true);
      
        JFileChooser chooser = new JFileChooser(initialDirectory);
        chooser.setFileFilter(filter);
        
        int selectVal = (mode == SAVE) ? chooser.showSaveDialog(null) : chooser.showOpenDialog(null);
        if (selectVal == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            return path;
        } else return null;
    }

    /**
     * Añade una extensión válida de fichero a las ya existentes.
     * 
     * @param extension Extensión a añadir.
     * @param caseInsensitive Si es sensible a mayúsculas (false) o no (true).
     */
    public void addExtension(String extension, boolean caseInsensitive) {
        if (caseInsensitive) extension = extension.toLowerCase();

        if (!extensionsTable.containsKey(extension)) {
            extensionsTable.put(extension, caseInsensitive);
            if (extension.equals("*") || extension.equals("*.*") || extension.equals(".*")) 
                allowAll = true;
        }
    }

    /**
     * Acciones del botón aceptar.
     * 
     * @param file Fichero o directorio seleccionado en ese momento.
     * @return Si es un fichero válido o no.
     */
    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) return allowDirectories;
        if (allowAll) return true;

        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if ((dotIndex == -1) || (dotIndex == name.length() - 1)) return false;
        String extension = name.substring(dotIndex + 1);
        if (extensionsTable.containsKey(extension)) return true ;

        Enumeration keys = extensionsTable.keys();
        while(keys.hasMoreElements()) {
            String possibleExtension = (String)keys.nextElement();
            Boolean caseFlag = (Boolean)extensionsTable.get(possibleExtension);
            if ((caseFlag != null) && (caseFlag.equals(Boolean.FALSE)) && (possibleExtension.equalsIgnoreCase(extension)))
                return true;
        }
        
        return false ;
    }

    /**
     * Asigna la descripción que tendrá la ventana de selección de fichero.
     * 
     * @param description Texto de la descripción.
     */
    public void setDescription(String description) {
        this.description = description;
    }
  
    /**
     * Devuelve la descripción que muestra la ventana de selección de fichero.
     * 
     * @return Texto de la descripción.
     */
    @Override
    public String getDescription() {
        return description;
    }
}