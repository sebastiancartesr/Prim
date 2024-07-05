package org.example;

import org.example.dto.MyMap;
import org.example.helper.MapHelper;

import static org.example.helper.MapHelper.newMapGenerate;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello world!");
        long startTime = System.currentTimeMillis(); // Tiempo de inicio
        final MyMap mapFromJson = newMapGenerate(MapHelper.MAP_BERLIN52_JSON);
        mapFromJson.computarCandidatos(Parameters.CANDIDATES_LENGTH);
    }
}