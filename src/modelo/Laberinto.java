package modelo;

import java.util.*;

public class Laberinto {
    private int[][] grid;
    private int filas, columnas;
    // Direcciones: arriba, abajo, izquierda, derecha
    private final int[] dX = {-1, 1, 0, 0};
    private final int[] dY = {0, 0, -1, 1};

    public Laberinto(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.grid = new int[filas][columnas];
    }

    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }
    public void setCelda(int x, int y, int valor) { grid[x][y] = valor; }
    public int getCelda(int x, int y) { return grid[x][y]; }

    // Método auxiliar: valida si la celda es transitable y no visitada
    private boolean esValido(int x, int y, boolean[][] visited) {
        return x >= 0 && x < filas &&
               y >= 0 && y < columnas &&
               grid[x][y] == 0 &&
               !visited[x][y];
    }

    // Reconstruir ruta a partir de arrays de padres (usado en BFS y DFS sin animación)
    private List<int[]> reconstruirRuta(int[][] parentX, int[][] parentY,
                                        int sx, int sy, int ex, int ey) {
        List<int[]> path = new ArrayList<>();
        int cx = ex, cy = ey;
        while (cx != -1 && cy != -1) {
            path.add(0, new int[]{cx, cy});
            if (cx == sx && cy == sy) break;
            int px = parentX[cx][cy];
            int py = parentY[cx][cy];
            cx = px; cy = py;
        }
        if (path.isEmpty() || (path.get(0)[0] != sx || path.get(0)[1] != sy)) {
            return null;
        }
        return path;
    }

    // ──────────────────────────────────────────────
    // 1) BFS SIN ANIMACIÓN
    // ──────────────────────────────────────────────
    public Resultado bfsNoAnim(int startX, int startY, int endX, int endY) {
        long startTime = System.nanoTime();
        boolean[][] visited = new boolean[filas][columnas];
        int[][] parentX = new int[filas][columnas];
        int[][] parentY = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            Arrays.fill(parentX[i], -1);
            Arrays.fill(parentY[i], -1);
        }
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int cx = current[0], cy = current[1];
            if (cx == endX && cy == endY) {
                long endTime = System.nanoTime();
                List<int[]> path = reconstruirRuta(parentX, parentY, startX, startY, endX, endY);
                double timeMs = (endTime - startTime) / 1_000_000.0;
                return new Resultado(path, timeMs);
            }
            for (int i = 0; i < 4; i++) {
                int nx = cx + dX[i];
                int ny = cy + dY[i];
                if (esValido(nx, ny, visited)) {
                    visited[nx][ny] = true;
                    parentX[nx][ny] = cx;
                    parentY[nx][ny] = cy;
                    queue.add(new int[]{nx, ny});
                }
            }
        }
        long endTime = System.nanoTime();
        return new Resultado(null, (endTime - startTime) / 1_000_000.0);
    }

    // ──────────────────────────────────────────────
    // 2) DFS SIN ANIMACIÓN
    // ──────────────────────────────────────────────
    public Resultado dfsNoAnim(int startX, int startY, int endX, int endY) {
        long startTime = System.nanoTime();
        boolean[][] visited = new boolean[filas][columnas];
        int[][] parentX = new int[filas][columnas];
        int[][] parentY = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            Arrays.fill(parentX[i], -1);
            Arrays.fill(parentY[i], -1);
        }
        Deque<int[]> stack = new ArrayDeque<>();
        stack.push(new int[]{startX, startY});
        visited[startX][startY] = true;
        while (!stack.isEmpty()) {
            int[] current = stack.pop();
            int cx = current[0], cy = current[1];
            if (cx == endX && cy == endY) {
                long endTime = System.nanoTime();
                List<int[]> path = reconstruirRuta(parentX, parentY, startX, startY, endX, endY);
                double timeMs = (endTime - startTime) / 1_000_000.0;
                return new Resultado(path, timeMs);
            }
            for (int i = 0; i < 4; i++) {
                int nx = cx + dX[i];
                int ny = cy + dY[i];
                if (esValido(nx, ny, visited)) {
                    visited[nx][ny] = true;
                    parentX[nx][ny] = cx;
                    parentY[nx][ny] = cy;
                    stack.push(new int[]{nx, ny});
                }
            }
        }
        long endTime = System.nanoTime();
        return new Resultado(null, (endTime - startTime) / 1_000_000.0);
    }

    // ──────────────────────────────────────────────
    // 3) RECURSIVO SIMPLE SIN ANIMACIÓN
    // ──────────────────────────────────────────────
    public Resultado recursivoSimple(int startX, int startY, int endX, int endY) {
        long startTime = System.nanoTime();
        boolean[][] visited = new boolean[filas][columnas];
        List<int[]> path = new ArrayList<>();
        boolean found = backtrack(startX, startY, endX, endY, visited, path);
        long endTime = System.nanoTime();
        double timeMs = (endTime - startTime) / 1_000_000.0;
        return new Resultado(found ? path : null, timeMs);
    }
    private boolean backtrack(int x, int y, int endX, int endY,
                              boolean[][] visited, List<int[]> path) {
        if (x < 0 || x >= filas || y < 0 || y >= columnas) return false;
        if (grid[x][y] == 1 || visited[x][y]) return false;
        visited[x][y] = true;
        path.add(new int[]{x, y});
        if (x == endX && y == endY) return true;
        for (int i = 0; i < 4; i++) {
            int nx = x + dX[i], ny = y + dY[i];
            if (backtrack(nx, ny, endX, endY, visited, path)) return true;
        }
        path.remove(path.size() - 1);
        return false;
    }

    // ──────────────────────────────────────────────
    // 4) DINÁMICO (CACHE) SIN ANIMACIÓN
    // ──────────────────────────────────────────────
    public Resultado dinamico(int startX, int startY, int endX, int endY) {
        long startTime = System.nanoTime();
        int[][] memo = new int[filas][columnas]; // 0: desconocido, 1: no ruta, 2: hay ruta
        List<int[]> path = new ArrayList<>();
        boolean found = dpSearch(startX, startY, endX, endY, memo, path, new boolean[filas][columnas]);
        long endTime = System.nanoTime();
        double timeMs = (endTime - startTime) / 1_000_000.0;
        return new Resultado(found ? path : null, timeMs);
    }
    private boolean dpSearch(int x, int y, int endX, int endY,
                             int[][] memo, List<int[]> path, boolean[][] visited) {
        if (x < 0 || x >= filas || y < 0 || y >= columnas) return false;
        if (grid[x][y] == 1) return false;
        if (visited[x][y]) return false;
        if (memo[x][y] == 1) return false;
        if (memo[x][y] == 2) { }
        visited[x][y] = true;
        path.add(new int[]{x, y});
        if (x == endX && y == endY) {
            memo[x][y] = 2;
            return true;
        }
        for (int i = 0; i < 4; i++) {
            int nx = x + dX[i], ny = y + dY[i];
            if (dpSearch(nx, ny, endX, endY, memo, path, visited)) {
                memo[x][y] = 2;
                return true;
            }
        }
        path.remove(path.size() - 1);
        visited[x][y] = false;
        memo[x][y] = 1;
        return false;
    }

    // ──────────────────────────────────────────────
    // MÉTODOS ANIMADOS: Recursivo y Dinámico
    // Estos métodos registran el “orden de exploración” y el “camino final”
    // para ser animados en la Vista con el mismo estilo que BFS.
    // ──────────────────────────────────────────────

    // Recursivo animado
    public boolean recursivoAnimado(int startX, int startY, int endX, int endY,
                                    List<int[]> order, List<int[]> finalPath) {
        boolean[][] visited = new boolean[filas][columnas];
        List<int[]> path = new ArrayList<>();
        boolean found = backtrackAnimado(startX, startY, endX, endY, visited, path, order);
        if (found) {
            finalPath.addAll(path);
        }
        return found;
    }
    private boolean backtrackAnimado(int x, int y, int endX, int endY,
                                     boolean[][] visited, List<int[]> path, List<int[]> order) {
        if (x < 0 || x >= filas || y < 0 || y >= columnas) return false;
        if (grid[x][y] == 1 || visited[x][y]) return false;
        visited[x][y] = true;
        order.add(new int[]{x, y});
        path.add(new int[]{x, y});
        if (x == endX && y == endY) return true;
        for (int i = 0; i < 4; i++) {
            int nx = x + dX[i], ny = y + dY[i];
            if (backtrackAnimado(nx, ny, endX, endY, visited, path, order)) return true;
        }
        path.remove(path.size() - 1);
        return false;
    }

    // Dinámico animado
    public boolean dinamicoAnimado(int startX, int startY, int endX, int endY,
                                   List<int[]> order, List<int[]> finalPath) {
        int[][] memo = new int[filas][columnas];
        boolean[][] visited = new boolean[filas][columnas];
        List<int[]> path = new ArrayList<>();
        boolean found = dpSearchAnimado(startX, startY, endX, endY, memo, path, visited, order);
        if (found) {
            finalPath.addAll(path);
        }
        return found;
    }
    private boolean dpSearchAnimado(int x, int y, int endX, int endY,
                                    int[][] memo, List<int[]> path, boolean[][] visited, List<int[]> order) {
        if (x < 0 || x >= filas || y < 0 || y >= columnas) return false;
        if (grid[x][y] == 1) return false;
        if (visited[x][y]) return false;
        if (memo[x][y] == 1) return false;
        if (memo[x][y] == 2) { }
        order.add(new int[]{x, y});
        visited[x][y] = true;
        path.add(new int[]{x, y});
        if (x == endX && y == endY) {
            memo[x][y] = 2;
            return true;
        }
        for (int i = 0; i < 4; i++) {
            int nx = x + dX[i], ny = y + dY[i];
            if (dpSearchAnimado(nx, ny, endX, endY, memo, path, visited, order)) {
                memo[x][y] = 2;
                return true;
            }
        }
        path.remove(path.size() - 1);
        visited[x][y] = false;
        memo[x][y] = 1;
        return false;
    }
}
