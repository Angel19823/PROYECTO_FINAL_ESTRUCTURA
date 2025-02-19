package controlador;

import modelo.Laberinto;
import modelo.Resultado;
import java.util.List;

public class Controlador {
    private Laberinto laberinto;

    public Controlador(int filas, int columnas) {
        this.laberinto = new Laberinto(filas, columnas);
    }

    public int getFilas() {
        return laberinto.getFilas();
    }

    public int getColumnas() {
        return laberinto.getColumnas();
    }

    public void setCelda(int x, int y, int valor) {
        laberinto.setCelda(x, y, valor);
    }

    public int getCelda(int x, int y) {
        return laberinto.getCelda(x, y);
    }

    // Métodos sin animación
    public Resultado bfsNoAnim(int startX, int startY, int endX, int endY) {
        return laberinto.bfsNoAnim(startX, startY, endX, endY);
    }
    public Resultado dfsNoAnim(int startX, int startY, int endX, int endY) {
        return laberinto.dfsNoAnim(startX, startY, endX, endY);
    }
    public Resultado recursivoSimple(int startX, int startY, int endX, int endY) {
        return laberinto.recursivoSimple(startX, startY, endX, endY);
    }
    public Resultado dinamico(int startX, int startY, int endX, int endY) {
        return laberinto.dinamico(startX, startY, endX, endY);
    }

    // Métodos animados
    public boolean recursivoAnimado(int startX, int startY, int endX, int endY,
                                    List<int[]> order, List<int[]> finalPath) {
        return laberinto.recursivoAnimado(startX, startY, endX, endY, order, finalPath);
    }
    public boolean dinamicoAnimado(int startX, int startY, int endX, int endY,
                                   List<int[]> order, List<int[]> finalPath) {
        return laberinto.dinamicoAnimado(startX, startY, endX, endY, order, finalPath);
    }
}
