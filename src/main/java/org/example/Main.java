package org.example;

import org.example.dto.MyMap;
import org.example.helper.MapHelper;

import java.util.List;

import static org.example.helper.MapHelper.newMapGenerate;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello world!");
        long startTime = System.currentTimeMillis(); // Tiempo de inicio
        final MyMap mapFromJson = newMapGenerate(MapHelper.MAP_BERLIN52_JSON);
        mapFromJson.computarCandidatos(Parameters.CANDIDATES_LENGTH);
        int[] padres = mapFromJson.prim();
        int[] padresbeta = mapFromJson.primBeta();
        int largo = mapFromJson.calcularLargo(padres);
        int largobeta = mapFromJson.calcularLargo(padresbeta);
        String dot = MapHelper.generarDot(padresbeta);
        String dotOrden = MapHelper.generarDotOrden(padresbeta,mapFromJson.getDistancesMap());
        System.out.println("Minimum Spanning Tree (MST) edges:");
        int totalCost = 0;


        // Imprimir el costo total del MST
        System.out.println("Total cost of MST: " + totalCost);

        long endTime = System.currentTimeMillis(); // Tiempo de finalización
        System.out.println("Execution time: " + (endTime - startTime) + " ms");
    }
}