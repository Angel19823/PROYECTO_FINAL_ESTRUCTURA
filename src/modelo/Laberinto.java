package modelo;

public class Laberinto {
    private int[][] grid;
    private int filas, columnas;
    
    public Laberinto(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.grid = new int[filas][columnas];
    }
    
    public int getFilas() {
        return filas;
    }
    
    public int getColumnas() {
        return columnas;
    }
    
    public void setCelda(int x, int y, int valor) {
        grid[x][y] = valor;
    }
    
    public int getCelda(int x, int y) {
        return grid[x][y];
    }
    
}
