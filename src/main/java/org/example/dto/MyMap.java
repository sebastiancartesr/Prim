package org.example.dto;

import lombok.Getter;
import org.example.Parameters;

import java.util.*;

@Getter
public class MyMap {
    private final String name;
    private final int[][] distancesMap;
    private final List<List<Integer>> nodesCandidates;
    private List<List<Double>> probabilidadInicial;
    private List<List<Integer>> candidatosDeNodo;
    public MyMap(final String name, final int[][] distancesMap) {
        this.name = name;
        this.distancesMap = distancesMap;
        this.probabilidadInicial = computarProbabilidades(distancesMap);
        this.nodesCandidates = this.computeCandidates(distancesMap);
    }
    @Getter
    public static class Pair {
        int distance;
        int node;

        Pair(int distance, int node) {
            this.distance = distance;
            this.node = node;
        }

    }
    public int getNodesDistanceId(int a, int b) {
        return this.distancesMap[a][b];
    }
    public int getCost(int a, int b) {
        return this.distancesMap[a][b];
    }
    public int getLength() {
        return this.distancesMap.length;
    }
    public List<Integer> getNodeCandidates(int nodeId) {
        return this.nodesCandidates.get(nodeId);
    }

    //computar tipo gustavo
    public List<List<Double>> computarProbabilidades(int[][] data) {
        // Obtiene la cantidad de filas (o columnas) de la matriz de datos
        int n = data.length;
        // Crea una lista de listas para almacenar las probabilidades iniciales
        probabilidadInicial = new ArrayList<>(n);
        // Inicializa la matriz de probabilidades con ceros
        for (int i = 0; i < n; i++) {
            probabilidadInicial.add(new ArrayList<>(n));
            for (int j = 0; j < n; j++) {
                probabilidadInicial.get(i).add(0.0);
            }
        }
        // Calcula las probabilidades basadas en los datos de entrada
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                // Calcula la probabilidad entre el nodo i y el nodo j
                //double p = 1.0 / Math.pow(data[i][j], Parameters.GAMMA);
                double p = 1.0 / data[i][j];
                // Asigna la probabilidad calculada a las posiciones correspondientes en la matriz
                probabilidadInicial.get(i).set(j, p);
                probabilidadInicial.get(j).set(i, p);
            }
        }
        // Retorna la matriz de probabilidades calculadas
        return probabilidadInicial;
    }

    public int[] prim() {
        int ciudades = distancesMap.length;
        boolean[] visited = new boolean[ciudades];
        int[] menorDistancia = new int[ciudades];
        int[] padres = new int[ciudades];
        // lleno el arreglo con valores mas altos para poder llenarlo con los menores
        Arrays.fill(menorDistancia, Integer.MAX_VALUE);
        // lleno los padres con -1 para que esten como no visitados
        Arrays.fill(padres, -1);
        // creo la cola de prioridades para poder ordenarlos en base a la distancia
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.distance));

        // Se asume que el nodo 0 es el punto de inicio
        pq.add(new Pair(0, 0));
        menorDistancia[0] = 0;

        while (!pq.isEmpty()) {
            // Extraemos el nodo con la menor distancia de la cola de prioridad
            int u = pq.poll().node;
            // Si el nodo ya ha sido visitado, continuamos con el siguiente
            if (visited[u]) continue;
            // Marcamos el nodo como visitado
            visited[u] = true;
            // Recorremos todos los nodos adyacentes al nodo u
            for (int v = 0; v < ciudades; v++) {
                // Si v es el mismo nodo u o si el nodo v ya ha sido visitado, lo saltamos
                if (v == u || visited[v]) continue;
                // Obtenemos el peso (distancia) de la arista entre u y v
                int weight = distancesMap[u][v];
                // Si encontramos una distancia menor para llegar a v, actualizamos
                if (weight < menorDistancia[v]) {
                    // Actualizamos la distancia mínima para el nodo v
                    menorDistancia[v] = weight;
                    // Añadimos el nodo v a la cola de prioridad con la nueva distancia
                    pq.add(new Pair(weight, v));
                    // Actualizamos el padre del nodo v al nodo u
                    padres[v] = u;
                }
            }
        }
        return padres;
    }


    public int[] primBeta(){
        int ciudades = distancesMap.length;
        boolean[] visited = new boolean[ciudades];
        int[] menorDistancia = new int[ciudades];
        int[] padres = new int[ciudades];
        // lleno el arreglo con valores mas altos para poder llenarlo con los menores
        Arrays.fill(menorDistancia, Integer.MAX_VALUE);
        // lleno los padres con -1 para que esten como no visitados
        Arrays.fill(padres, -1);
        // creo la cola de prioridades para poder ordenarlos en base a la distancia
        PriorityQueue<Pair> pq = new PriorityQueue<>(Comparator.comparingInt(p -> p.distance));

        // Se asume que el nodo 1 es el punto de inicio
        pq.add(new Pair(0, 1));
        menorDistancia[1] = 0;

        while (!pq.isEmpty()) {
            // Extraemos el nodo con la menor distancia de la cola de prioridad
            int u = pq.poll().node;
            // Si el nodo ya ha sido visitado, continuamos con el siguiente
            if (visited[u]) continue;
            // Marcamos el nodo como visitado
            visited[u] = true;
            // Recorremos todos los nodos adyacentes al nodo u
            for (int v = 0; v < ciudades; v++) {
                // Si v es el mismo nodo u o si el nodo v ya ha sido visitado, lo saltamos
                if (v == u || visited[v]) continue;
                // Obtenemos el peso (distancia) de la arista entre u y v
                int weight = distancesMap[u][v];
                // Si encontramos una distancia menor para llegar a v, actualizamos
                if (weight < menorDistancia[v]) {
                    // Actualizamos la distancia mínima para el nodo v
                    menorDistancia[v] = weight;
                    // Añadimos el nodo v a la cola de prioridad con la nueva distancia
                    pq.add(new Pair(weight, v));
                    // Actualizamos el padre del nodo v al nodo u
                    padres[v] = u;
                }
            }
        }
        return padres;
    }
    public int calcularLargo(int[] padres) {
        int largo = 0;
        for (int i = 0; i < padres.length; i++) {
            int padre = padres[i];
            if (padre != -1) {
                largo += distancesMap[i][padre];
            }
        }
        return largo;
    }
    public void computarCandidatos(int TAMVEC) {
        int n = distancesMap.length;
        candidatosDeNodo = new ArrayList<>(n);

        for (int i = 0; i < n; i++) {
            candidatosDeNodo.add(new ArrayList<>());
            PriorityQueue<Pair> cola = new PriorityQueue<>(Comparator.comparingInt(Pair::getDistance));

            for (int j = 0; j < n; j++) {
                if (i == j) continue;
                cola.add(new Pair(distancesMap[i][j], j));
            }

            for (int k = 0; k < TAMVEC; k++) {
                if (!cola.isEmpty()) {
                    Pair aux = cola.poll();
                    candidatosDeNodo.get(i).add(aux.node);
                }
            }
        }
    }

    private List<List<Integer>> computeCandidates(int[][] cityDistances) {
        int n = cityDistances.length;
        List<List<Integer>> nodesCandidates = new ArrayList<>();
        // Inicializar la lista de listas para almacenar los índices de candidatos.
        for (int i = 0; i < n; i++) {
            nodesCandidates.add(new ArrayList<>());
        }
        for (int i = 0; i < n; i++) {
            // Lista para mantener los costes y los índices de cada ciudad relacionada.
            List<CostIndexPair> queue = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                queue.add(new CostIndexPair(cityDistances[i][j], j));
            }
            // Ordenar la lista de CostIndexPair basado en el coste de menor a mayor.
            Collections.sort(queue);
            // Agregar los índices de los candidatos de menor coste a nodesCandidates.
            for (int k = 0; k < Parameters.CANDIDATES_LENGTH && k < queue.size(); k++) {
                nodesCandidates.get(i).add(queue.get(k).index);
            }
        }
        return nodesCandidates;
    }
    private static class CostIndexPair implements Comparable<CostIndexPair> {

        int cost;
        int index;

        public CostIndexPair(int cost, int index) {
            this.cost = cost;
            this.index = index;
        }

        @Override
        public int compareTo(CostIndexPair other) {
            return Integer.compare(this.cost, other.cost);
        }

    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("Distances Map for ");
        builder.append(name).append(":\n");
        for (int i = 0; i < distancesMap.length; i++) {
            builder.append("\t[");
            for (int j = 0; j < distancesMap[i].length; j++) {
                builder.append(distancesMap[i][j]).append(",");
            }
            builder.append("],\n");
        }
        return builder.toString();
    }
}
