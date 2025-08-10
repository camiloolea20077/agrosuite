package com.erp.backend_erp.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class HtmlTemplateUtil {
    
        public static String cargarPlantilla(String nombreArchivo, Map<String, String> variables) throws IOException {
        String contenido = Files.readString(Paths.get("src/main/resources/templates/" + nombreArchivo));
        for (Map.Entry<String, String> entry : variables.entrySet()) {
            contenido = contenido.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }
        return contenido;
    }
}
