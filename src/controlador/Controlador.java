package controlador;

import modelo.Laberinto;

public class Controlador {
    private Laberinto laberinto;
    
    public Controlador(int filas, int columnas) {
        this.laberinto = new Laberinto(filas, columnas);
    }
    
    public void setCelda(int x, int y, int valor) {
        laberinto.setCelda(x, y, valor);
    }
    
    public int getCelda(int x, int y) {
        return laberinto.getCelda(x, y);
    }
}
