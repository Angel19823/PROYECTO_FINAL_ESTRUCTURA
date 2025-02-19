package vista;

import controlador.Controlador;
import modelo.Resultado;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class LaberintoGUI extends JFrame {
    private Controlador controlador;
    private JButton[][] botones;
    private int filas = 10, columnas = 10;
    private int startX = -1, startY = -1, endX = -1, endY = -1;
    private final int[] dX = {-1, 1, 0, 0};
    private final int[] dY = {0, 0, -1, 1};
    private JComboBox<String> cbMetodo;

    public LaberintoGUI() {
        controlador = new Controlador(filas, columnas);
        setTitle("Laberinto");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel del grid
        JPanel gridPanel = new JPanel(new GridLayout(filas, columnas));
        botones = new JButton[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                botones[i][j] = new JButton();
                botones[i][j].setBackground(Color.WHITE);
                final int x = i, y = j;
                botones[i][j].addActionListener(e -> cellClicked(x, y));
                gridPanel.add(botones[i][j]);
            }
        }

        // Panel de control
        JPanel controlPanel = new JPanel();
        cbMetodo = new JComboBox<>(new String[] { "BFS", "DFS", "Recursivo", "Dinámico" });

        JButton buscarBtn = new JButton("Buscar Ruta (Individual)");
        buscarBtn.addActionListener(e -> buscarIndividual());

        JButton compararBtn = new JButton("Comparar 4 Métodos");
        compararBtn.addActionListener(e -> {
            if (startX == -1 || endX == -1) {
                JOptionPane.showMessageDialog(this, "Selecciona inicio y fin primero.");
                return;
            }
            compararMetodos();
        });

        JButton resetBtn = new JButton("Resetear");
        resetBtn.addActionListener(e -> resetLaberinto());

        controlPanel.add(new JLabel("Método: "));
        controlPanel.add(cbMetodo);
        controlPanel.add(buscarBtn);
        controlPanel.add(compararBtn);
        controlPanel.add(resetBtn);

        add(controlPanel, BorderLayout.SOUTH);
        add(gridPanel, BorderLayout.CENTER);
    }

    // Al hacer clic: primer clic = inicio (azul), segundo = fin (rojo), luego alterna obstáculo
    private void cellClicked(int x, int y) {
        if (startX == -1 && startY == -1) {
            startX = x; startY = y;
            botones[x][y].setBackground(Color.BLUE);
        } else if (endX == -1 && endY == -1 && !(x == startX && y == startY)) {
            endX = x; endY = y;
            botones[x][y].setBackground(Color.RED);
        } else {
            if ((x == startX && y == startY) || (x == endX && y == endY)) return;
            int current = controlador.getCelda(x, y);
            int nuevo = (current == 0) ? 1 : 0;
            controlador.setCelda(x, y, nuevo);
            botones[x][y].setBackground(nuevo == 1 ? Color.BLACK : Color.WHITE);
        }
    }

    // Restablece el laberinto
    private void resetLaberinto() {
        startX = startY = endX = endY = -1;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                controlador.setCelda(i, j, 0);
                botones[i][j].setBackground(Color.WHITE);
            }
        }
    }

    // Buscar individual: según la opción del combo, se anima el recorrido
    private void buscarIndividual() {
        if (startX == -1 || endX == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona inicio y fin primero.");
            return;
        }
        limpiarColores();
        botones[startX][startY].setBackground(Color.BLUE);
        botones[endX][endY].setBackground(Color.RED);
        String metodo = (String) cbMetodo.getSelectedItem();
        if (metodo.equals("BFS")) {
            animateBFS();
        } else if (metodo.equals("DFS")) {
            animateDFS();
        } else if (metodo.equals("Recursivo")) {
            animateRecursivo();
        } else if (metodo.equals("Dinámico")) {
            animateDinámico();
        }
    }

    // ──────────────────────────────────────────────
    // Animación para BFS (ya existente)
    private void animateBFS() {
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
        final javax.swing.Timer timer = new javax.swing.Timer(150, null);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (queue.isEmpty()) {
                    timer.stop();
                    long endTime = System.nanoTime();
                    double totalMs = (endTime - startTime) / 1_000_000.0;
                    JOptionPane.showMessageDialog(null,
                        "No se encontró ruta (BFS)\nTiempo: " + totalMs + " ms");
                    return;
                }
                int[] current = queue.poll();
                int cx = current[0], cy = current[1];
                if (!(cx == startX && cy == startY) && !(cx == endX && cy == endY)) {
                    botones[cx][cy].setBackground(Color.YELLOW);
                }
                if (cx == endX && cy == endY) {
                    timer.stop();
                    long endTime = System.nanoTime();
                    double totalMs = (endTime - startTime) / 1_000_000.0;
                    reconstructPath(parentX, parentY, Color.MAGENTA);
                    JOptionPane.showMessageDialog(null,
                        "Ruta BFS encontrada.\nTiempo: " + totalMs + " ms");
                    return;
                }
                for (int i = 0; i < 4; i++) {
                    int nx = cx + dX[i];
                    int ny = cy + dY[i];
                    if (nx >= 0 && nx < filas && ny >= 0 && ny < columnas &&
                        !visited[nx][ny] && controlador.getCelda(nx, ny) == 0) {
                        visited[nx][ny] = true;
                        parentX[nx][ny] = cx;
                        parentY[nx][ny] = cy;
                        queue.add(new int[]{nx, ny});
                        if (!(nx == endX && ny == endY)) {
                            botones[nx][ny].setBackground(Color.ORANGE);
                        }
                    }
                }
            }
        });
        timer.start();
    }

    // ──────────────────────────────────────────────
    // Animación para DFS (modificada para que, al finalizar, se pinte la ruta final en MAGENTA)
    private void animateDFS() {
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
        final javax.swing.Timer timer = new javax.swing.Timer(150, null);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (stack.isEmpty()) {
                    timer.stop();
                    long endTime = System.nanoTime();
                    double totalMs = (endTime - startTime) / 1_000_000.0;
                    JOptionPane.showMessageDialog(null,
                        "No se encontró ruta (DFS)\nTiempo: " + totalMs + " ms");
                    return;
                }
                int[] current = stack.pop();
                int cx = current[0], cy = current[1];
                if (!(cx == startX && cy == startY) && !(cx == endX && cy == endY)) {
                    botones[cx][cy].setBackground(Color.YELLOW);
                }
                if (cx == endX && cy == endY) {
                    timer.stop();
                    long endTime = System.nanoTime();
                    double totalMs = (endTime - startTime) / 1_000_000.0;
                    // Se utiliza MAGENTA para pintar el camino final en DFS.
                    reconstructPath(parentX, parentY, Color.MAGENTA);
                    JOptionPane.showMessageDialog(null,
                        "Ruta DFS encontrada.\nTiempo: " + totalMs + " ms");
                    return;
                }
                for (int i = 0; i < 4; i++) {
                    int nx = cx + dX[i];
                    int ny = cy + dY[i];
                    if (nx >= 0 && nx < filas && ny >= 0 && ny < columnas &&
                        !visited[nx][ny] && controlador.getCelda(nx, ny) == 0) {
                        visited[nx][ny] = true;
                        parentX[nx][ny] = cx;
                        parentY[nx][ny] = cy;
                        stack.push(new int[]{nx, ny});
                        if (!(nx == endX && ny == endY)) {
                            botones[nx][ny].setBackground(Color.ORANGE);
                        }
                    }
                }
            }
        });
        timer.start();
    }

    // ──────────────────────────────────────────────
    // Animación para método Recursivo (con estilo similar a BFS)
    private void animateRecursivo() {
        List<int[]> order = new ArrayList<>();
        List<int[]> finalPath = new ArrayList<>();
        boolean found = controlador.recursivoAnimado(startX, startY, endX, endY, order, finalPath);
        final int[] index = {0};
        final javax.swing.Timer timer = new javax.swing.Timer(150, null);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (index[0] < order.size()) {
                    int[] cell = order.get(index[0]);
                    int cx = cell[0], cy = cell[1];
                    if (!(cx == startX && cy == startY) && !(cx == endX && cy == endY)) {
                        botones[cx][cy].setBackground(Color.YELLOW);
                    }
                    index[0]++;
                } else {
                    timer.stop();
                    for (int[] c : finalPath) {
                        int rx = c[0], ry = c[1];
                        if (!(rx == startX && ry == startY) && !(rx == endX && ry == endY)) {
                            botones[rx][ry].setBackground(Color.MAGENTA);
                        }
                    }
                }
            }
        });
        timer.start();
    }

    // ──────────────────────────────────────────────
    // Animación para método Dinámico (con estilo similar a BFS)
    private void animateDinámico() {
        List<int[]> order = new ArrayList<>();
        List<int[]> finalPath = new ArrayList<>();
        boolean found = controlador.dinamicoAnimado(startX, startY, endX, endY, order, finalPath);
        final int[] index = {0};
        final javax.swing.Timer timer = new javax.swing.Timer(150, null);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (index[0] < order.size()) {
                    int[] cell = order.get(index[0]);
                    int cx = cell[0], cy = cell[1];
                    if (!(cx == startX && cy == startY) && !(cx == endX && cy == endY)) {
                        botones[cx][cy].setBackground(Color.YELLOW);
                    }
                    index[0]++;
                } else {
                    timer.stop();
                    for (int[] c : finalPath) {
                        int rx = c[0], ry = c[1];
                        if (!(rx == startX && ry == startY) && !(rx == endX && ry == endY)) {
                            botones[rx][ry].setBackground(Color.MAGENTA);
                        }
                    }
                }
            }
        });
        timer.start();
    }

    // ──────────────────────────────────────────────
    // Comparar los 4 métodos sin animación y mostrar los tiempos en un JOptionPane
    private void compararMetodos() {
        limpiarColores();
        botones[startX][startY].setBackground(Color.BLUE);
        botones[endX][endY].setBackground(Color.RED);
        Resultado bfsRes = controlador.bfsNoAnim(startX, startY, endX, endY);
        Resultado dfsRes = controlador.dfsNoAnim(startX, startY, endX, endY);
        Resultado recRes = controlador.recursivoSimple(startX, startY, endX, endY);
        Resultado dinRes = controlador.dinamico(startX, startY, endX, endY);
        pintarRuta(bfsRes.getPath(), Color.GREEN);
        pintarRuta(dfsRes.getPath(), Color.ORANGE);
        pintarRuta(recRes.getPath(), Color.MAGENTA);
        pintarRuta(dinRes.getPath(), Color.CYAN);
        StringBuilder sb = new StringBuilder();
        sb.append("BFS: ").append(bfsRes.getPath() != null ? "Ruta encontrada, " + bfsRes.getTimeMs() + " ms" : "Sin ruta, " + bfsRes.getTimeMs() + " ms").append("\n");
        sb.append("DFS: ").append(dfsRes.getPath() != null ? "Ruta encontrada, " + dfsRes.getTimeMs() + " ms" : "Sin ruta, " + dfsRes.getTimeMs() + " ms").append("\n");
        sb.append("Recursivo: ").append(recRes.getPath() != null ? "Ruta encontrada, " + recRes.getTimeMs() + " ms" : "Sin ruta, " + recRes.getTimeMs() + " ms").append("\n");
        sb.append("Dinámico: ").append(dinRes.getPath() != null ? "Ruta encontrada, " + dinRes.getTimeMs() + " ms" : "Sin ruta, " + dinRes.getTimeMs() + " ms").append("\n");
        JOptionPane.showMessageDialog(this, sb.toString(), "Comparación de métodos", JOptionPane.INFORMATION_MESSAGE);
    }

    // Pinta una ruta de un color dado
    private void pintarRuta(List<int[]> ruta, Color color) {
        if (ruta == null) return;
        for (int[] c : ruta) {
            int rx = c[0], ry = c[1];
            if (!(rx == startX && ry == startY) && !(rx == endX && ry == endY)) {
                botones[rx][ry].setBackground(color);
            }
        }
    }

    // Limpia los colores de las celdas (manteniendo obstáculos, inicio y fin)
    private void limpiarColores() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (controlador.getCelda(i, j) == 0) {
                    botones[i][j].setBackground(Color.WHITE);
                } else {
                    botones[i][j].setBackground(Color.BLACK);
                }
            }
        }
        if (startX != -1 && startY != -1) {
            botones[startX][startY].setBackground(Color.BLUE);
        }
        if (endX != -1 && endY != -1) {
            botones[endX][endY].setBackground(Color.RED);
        }
    }

    // Reconstruye la ruta para BFS/DFS animados (pinta con el color indicado)
    private void reconstructPath(int[][] parentX, int[][] parentY, Color color) {
        int cx = endX, cy = endY;
        while (!(cx == startX && cy == startY)) {
            botones[cx][cy].setBackground(color);
            int px = parentX[cx][cy];
            int py = parentY[cx][cy];
            cx = px; cy = py;
        }
        botones[startX][startY].setBackground(color);
    }
}
