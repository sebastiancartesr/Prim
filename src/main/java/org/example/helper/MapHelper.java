package org.example.helper;

import org.example.dto.MyMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapHelper {
    public static final String MAP_ATT532 = "att532.dat";
    public static final String MAP_ATT532_JSON = "att532.json";
    public static final String MAP_BERLIN52 = "berlin52.dat";
    public static final String MAP_BERLIN52_JSON = "berlin52.json";
    private MapHelper() {
    }
    public static MyMap newMapGenerate(String fileName) throws IOException {
        try {
            // Lee el archivo JSON
            File file = new File("src/main/resources/"+fileName);
            FileInputStream fis = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            fis.read(data);
            fis.close();

            // Convierte el contenido del archivo a JSONArray
            String jsonContent = new String(data, "UTF-8");
            JSONArray jsonArray = new JSONArray(new JSONTokener(jsonContent));

            // Determina las dimensiones de la matriz (suponiendo que sea una matriz cuadrada)
            int size = jsonArray.length();
            int[][] matrix = new int[size][size];

            // Llena la matriz con los valores del JSONArray
            for (int i = 0; i < size; i++) {
                JSONArray rowArray = jsonArray.getJSONArray(i);
                for (int j = 0; j < size; j++) {
                    matrix[i][j] = rowArray.getInt(j);
                }
            }
            return new MyMap(fileName, matrix);
        } catch (IOException | JSONException e) {
            throw new IllegalArgumentException("El archivo al intentar leer el archivo.");
        }
    }
    public static String generarDot(int[] padres) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph Arbol {\n");

        int n = padres.length;
        for (int i = 1; i < n; i++) {
            List<Integer> hijos = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (padres[j] == i) {
                    hijos.add(j);
                }
            }
            if (!hijos.isEmpty()) {
                sb.append(i)
                        .append(" -> {")
                        .append(String.join(", ", hijos.stream().map(String::valueOf).toArray(String[]::new)))
                        .append("};\n");
            } else {
                sb.append(i).append(" -> {};\n");
            }
        }

        sb.append("}");
        return sb.toString();
    }
    public static String generarDotOrden(int[] padres,int[][] distancesMap) {
        StringBuilder sb = new StringBuilder();
        sb.append("digraph Arbol {\n");

        List<String> edges = new ArrayList<>();
        int n = padres.length;
        for (int i = 1; i < n; i++) {
            int padre = padres[i];
            if (padre != -1) {
                edges.add(padre + " -> " + i + " [label=\"" + distancesMap[padre][i] + "\"]\n");
            }
        }

        // Ordenamos las aristas alfabéticamente
        Collections.sort(edges);

        // Añadimos las aristas ordenadas al StringBuilder
        for (String edge : edges) {
            sb.append(edge);
        }

        sb.append("}");
        return sb.toString();
    }
    public static void execDot(String dotInput) throws IOException, InterruptedException {
        // Comando para ejecutar Graphviz dot
        String[] cmd = {"dot", "-Tpng", "-ooutput.png"};

        // Crear un proceso con el comando
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        Process process = processBuilder.start();

        // Escribir el contenido DOT en la entrada estándar del proceso dot
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()))) {
            writer.write(dotInput);
            writer.flush();
        }

        // Esperar a que el proceso termine
        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("dot command failed with exit code " + exitCode);
        }
        openImage("output.png");
    }
    private static void openImage(String filePath) throws IOException {
        File imageFile = new File(filePath);
        if (!imageFile.exists()) {
            throw new FileNotFoundException("Image file not found: " + filePath);
        }

        // Abrir la imagen con el visor de imágenes predeterminado
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            if (desktop.isSupported(Desktop.Action.OPEN)) {
                desktop.open(imageFile);
            } else {
                throw new UnsupportedOperationException("Open action is not supported on this desktop");
            }
        } else {
            throw new UnsupportedOperationException("Desktop is not supported on this system");
        }
    }
}
