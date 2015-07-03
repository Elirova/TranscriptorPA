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

import XML.UIXMLTree;
import XML.XMLTree;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.xhtmlrenderer.pdf.ITextRenderer;


/**
 * Maneja el guardado y lectura de ficheros.
 */
public abstract class FileManager {
    
    /**
     * Lee un DTD del fichero con la ruta indicada.
     * @param path Ruta del fichero a ller.
     * @param name Nombre del DTD a leer.
     * @return Cadena que contiene el DTD.
     */
    public static String readDTD(String path, String name) {
        Logger.getLogger(FileManager.class.getName()).log(Level.INFO, "Leyendo DTD");
        String dtd = "";
        try {
            if(path == null) path = Config.pathBaseDTD;
            if(path == null) return null;
            File f = new File(path);
            if(!f.exists()) return null;
            
            
            BufferedReader entrada = new BufferedReader (new FileReader(path));
            String line;
            
            while(true) {
                line = entrada.readLine();
                if(line == null) break;
                dtd = dtd.concat(line.trim() + " ");
            }
        } catch (Exception ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dtd;
    }
    
    /**
     * Guarda un DTD en el fichero con la ruta indicada.
     * @param path Ruta donde se guardará el DTD.
     * @param dtd Cadena que contiene el DTD.
     */
    public static void saveDTD(String path, String dtd) {
        if(path == null) path = Config.pathAllDTD; 
        
        try(FileWriter fichero = new FileWriter(path)){
            fichero.write(dtd + "\r\n");
            fichero.close();
            Logger.getLogger(FileManager.class.getName()).log(Level.INFO, "Guardado DTD");
        } catch(Exception ex){
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Guarda la configuración de interfaz asociada al DTD.
     * 
     * @param name Nombre del DTD asociado a la configuración.
     * @param xml Cadena que contiene la configuración.
     */
    public static void saveDTDConfig(String name, String xml) {
        try (FileWriter f = new FileWriter(Config.pathAllDTD + name + "/" + name + "_config.xml")) {
            f.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            f.write(xml);
        } catch (Exception ex) {
            Logger.getLogger(UIXMLTree.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Obtiene un árbol tipo DOM que almacena un XML del fichero indicado.
     * @param path Ruta del fichero XML a leer.
     * @return Documento con la información del XML leido.
     */
    public static Document readXML(String path) {
        if(path == null) return null;
        Document doc = null;
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);
            dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
            
            doc = dbf.newDocumentBuilder().parse(path);
        } catch(Exception ex) {
            Logger.getLogger(Config.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc;
    }
    
    /**
     * Guarda un fichero XML en la ruta indicada.
     * @param path Ruta donde se guardará el fichero XML.
     * @param dtdName Nombre del DTD asociado, en caso de existir.
     * @param doc Documento tipo DOm que almacena el XML a guardar.
     * @param withDTD Si se va a incluir el DTD o no
     */
    public static void saveXML(String path, Document doc, String dtdName, boolean withDTD) {
        if(path == null) path = ExtensionFileFilter.getFileName(".", "Guardar fichero XML", "", 1);
        if(path == null) return;
        
        try {
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            if(withDTD && dtdName != null && !dtdName.equals("")) { // Si tiene DTD
                DocumentType doctype = doc.getImplementation().createDocumentType(dtdName, Config.pathAllDTD, dtdName + ".dtd");
                transformer.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, doctype.getSystemId());
                transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            }
            transformer.transform(new DOMSource(doc), new StreamResult(new File(path)));
            
            Logger.getLogger(FileManager.class.getName()).log(Level.INFO, "Guardado XML en: {0}", path);
        } catch (Exception ex) {
              Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Guarda un fichero en la ruta indicada con el texto pasado.
     * @param path Ruta donde se guardará el fichero XML.
     * @param dtdName Nombre del DTD asociado, en caso de existir.
     * @param xml Cadena de texto con la información que contendrá el fichero.
     * @param dtd Indica si el XML a guardar tendrá DTD asociado o no.
     */
    public static void saveXML(String path, String dtdName, String xml, boolean dtd) {
        if(path == null) path = ExtensionFileFilter.getFileName(".", "Guardar fichero XML", "xml", 1);
        if(path == null) return;
        
        try (FileWriter f = new FileWriter(path)) {
            f.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            if(dtd) f.write("<!DOCTYPE " + dtdName + " SYSTEM \"" + Config.pathBaseDTD + dtdName + "/\">\n");
            f.write(xml);
        } catch (Exception ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Copia un fichero en la ruta seleccionada.
     * 
     * @param pathStart Ruta en la que se encuentra el fichero a copiar.
     * @param pathEnd Ruta donde se copiará el fichero.
     */
    public static void copyFile(String pathStart, String pathEnd) {
        if(pathStart == null || pathEnd == null) return;
        
        try {
            // Creamos la carpeta donde almacenar el fichero en caso de no existir
            String names[] = pathEnd.split("/");
            String name = names[names.length-1];
            File f = new File(pathEnd.replace(name, ""));
            if(!f.exists()) f.mkdirs();
            
            // Copiamos el fichero a la carpeta antes creada
            Path file = Paths.get(pathStart);
            Path fileTo = Paths.get(pathEnd);
            
            Files.copy(file, fileTo, StandardCopyOption.REPLACE_EXISTING);
            
            Logger.getLogger(FileManager.class.getName()).log(Level.INFO, "Guardado fichero: {0} -- en: {1}", new Object[]{file, fileTo});
        } catch (Exception e) {
            System.err.println(e);
        }
    }
    
    /**
     * Elimina un directorio recursivamente.
     * @param folder Directorio a eliminar.
     */
    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files != null) 
            for (File f: files) 
                if(f.isDirectory()) deleteFolder(f);
                else f.delete();
            
        folder.delete();
    }
    
    /**
     * Crea una copia del DTD existente en la ruta indicada, con el nombre indicado, 
     * en la ruta por defecto de la configuración.
     * @param path Ruta donde se encuentra el DTD a copiar.
     * @param name Nombre bajo el que se guardará el DTD copiado.
     */
    public static void importDTD(String path, String name) {
        copyFile(path, Config.pathAllDTD + name + "/" + name + ".dtd");
    }
    
    /**
     * Crea una copia del fichero XML que guarda el tipo de los elementos de un DTD
     * existente en la ruta indicada, con el nombre indicado, 
     * en la ruta por defecto de la configuración.
     * @param path Ruta donde se encuentra el fichero a copiar.
     * @param name Nombre bajo el que se guardará el fichero copiado.
     */
    public static void importDTDConfig(String path, String name) {
        copyFile(path, Config.pathAllDTD + name + "/" + name + "_config.xml");
    }
    
    /**
     * Exporta el XML actual a PDF en la ruta indicada.
     * 
     * @param path Ruta en la que se guardará el PDF generado.
     * @param nameDTD Nombre del DTD asociado.
     * @return Devuelve si se ha podido crear o no el fichero PDF.
     */
    public static String exportPDF (String path, String nameDTD) {
        if(path == null) return "Ruta de guardado no válida";
        else if(nameDTD == null || nameDTD.equals("")) return "DTD no válido";
        
        String xmlPath = Config.pathTemp + nameDTD + ".xml";
        String xslPath = Config.pathAllDTD + nameDTD + "/" + nameDTD + ".xsl";
        String htmlPath = Config.pathAllDTD + nameDTD + "/" + nameDTD + ".xhtml";
        
        String pdfPath = path;
        
        File f = new File(Config.pathAllDTD + nameDTD + "/" + nameDTD + ".xsl");
        if(!f.exists()) return "No se ha asignado un fichero XSL para poder generar el PDF";
        
        // Elimina el fichero html en caso de existir
        f = new File(htmlPath);
        if(f.exists()) f.delete();
        
        // Creando fichero XML temporal
        XMLTree.saveXML(xmlPath, false);
        Logger.getLogger(FileManager.class.getName()).log(Level.INFO, "Creado XML temporal");
        
        // Creando fichero HTML temporal a partir del XML y el XSLT
        try {
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer(new StreamSource(xslPath));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new StreamSource(xmlPath), new StreamResult(new FileOutputStream(htmlPath)));
        } catch (Exception ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, "Excepcion al generar el XML. ", ex);
            return "No se ha podido crear el XML para generar el PDF.";
        }
        
            // Creando el PDF del HTML anterior
        try (OutputStream os = new FileOutputStream(pdfPath)) {
            ITextRenderer renderer = new ITextRenderer();
            
            renderer.setDocument(new File(htmlPath));
            renderer.layout();
            renderer.createPDF(os);
            Logger.getLogger(FileManager.class.getName()).log(Level.INFO, "Creado PDF");
        } catch (Exception ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, "Excepcion al generar el PDF. ", ex);
            return "No se ha podido generar el PDF.\n Pregunta a un administrador.";
        }
        
        // Eliminar html temporal
        f = new File(htmlPath);
        if(f.exists()) f.delete();
        
        // Eliminar xml temporal
        f = new File(xmlPath);
        if(f.exists()) f.delete();
        
        return null;
    }
}
