package com.erp.backend_erp.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.core.io.ClassPathResource;

public class HtmlTemplateUtil {
    
    public static String cargarPlantilla(String nombreArchivo, Map<String, String> variables) throws IOException {
        // Cargar archivo HTML desde resources/templates dentro del JAR
        ClassPathResource resource = new ClassPathResource("templates/" + nombreArchivo);
        String contenido = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

        // Reemplazar variables {{nombreVariable}}
        if (variables != null) {
            for (Map.Entry<String, String> entry : variables.entrySet()) {
                contenido = contenido.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
        }

        return contenido;
    }
}
