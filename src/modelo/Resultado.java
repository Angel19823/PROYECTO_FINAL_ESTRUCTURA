package modelo;

import java.util.List;

public class Resultado {
    private List<int[]> path;
    private double timeMs;  // Tiempo en milisegundos

    public Resultado(List<int[]> path, double timeMs) {
        this.path = path;
        this.timeMs = timeMs;
    }

    public List<int[]> getPath() {
        return path;
    }

    public double getTimeMs() {
        return timeMs;
    }
}
