package vista;

import controlador.Controlador;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;

public class LaberintoGUI extends JFrame {
    private Controlador controlador;
    private JButton[][] botones;
    private int filas = 10, columnas = 10;
    private int startX = -1, startY = -1, endX = -1, endY = -1;
    private JComboBox<String> cbMetodo;
    // Direcciones: arriba, abajo, izquierda, derecha
    private final int[] dX = {-1, 1, 0, 0};
    private final int[] dY = {0, 0, -1, 1};
    
    public LaberintoGUI() {
        controlador = new Controlador(filas, columnas);
        setTitle("Laberinto");
        setSize(700, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Panel del laberinto
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
        cbMetodo = new JComboBox<>(new String[] { "BFS", "DFS" });
        JButton buscarBtn = new JButton("Buscar Ruta");
        buscarBtn.addActionListener(e -> {
            String metodo = (String) cbMetodo.getSelectedItem();
            if (startX == -1 || endX == -1) {
                JOptionPane.showMessageDialog(null, "Debe seleccionar punto de inicio y fin.");
                return;
            }
            if(metodo.equals("BFS")) {
                animateBFS();
            } else {
                animateDFS();
            }
        });
        
        JButton resetBtn = new JButton("Resetear");
        resetBtn.addActionListener(e -> resetLaberinto());
        
        controlPanel.add(new JLabel("Método: "));
        controlPanel.add(cbMetodo);
        controlPanel.add(buscarBtn);
        controlPanel.add(resetBtn);
        
        add(controlPanel, BorderLayout.SOUTH);
        add(gridPanel, BorderLayout.CENTER);
    }
    
    // Al hacer clic en una celda se asigna el inicio, fin o se coloca un obstáculo.
    private void cellClicked(int x, int y) {
        if (startX == -1 && startY == -1) {
            startX = x;
            startY = y;
            botones[x][y].setBackground(Color.BLUE);
        } else if (endX == -1 && endY == -1 && !(x == startX && y == startY)) {
            endX = x;
            endY = y;
            botones[x][y].setBackground(Color.RED);
        } else {
            // Si se hace clic sobre una celda que no es inicio ni fin, se alterna entre libre y obstáculo.
            if ((x == startX && y == startY) || (x == endX && y == endY)) return;
            int current = controlador.getCelda(x, y);
            int nuevo = (current == 0) ? 1 : 0;
            controlador.setCelda(x, y, nuevo);
            botones[x][y].setBackground(nuevo == 1 ? Color.BLACK : Color.WHITE);
        }
    }
    
    // Reinicia el laberinto
    private void resetLaberinto() {
        startX = startY = endX = endY = -1;
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                controlador.setCelda(i, j, 0);
                botones[i][j].setBackground(Color.WHITE);
            }
        }
    }
    
    // Animación paso a paso con BFS
    private void animateBFS() {
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
        
        final Timer timer = new Timer(150, null);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (queue.isEmpty()) {
                    timer.stop();
                    JOptionPane.showMessageDialog(null, "No se encontró ruta.");
                    return;
                }
                int[] current = queue.poll();
                int cx = current[0], cy = current[1];
                
                // Marcar celda procesada
                if (!(cx == startX && cy == startY) && !(cx == endX && cy == endY)) {
                    botones[cx][cy].setBackground(Color.YELLOW);
                }
                
                if (cx == endX && cy == endY) {
                    timer.stop();
                    reconstructPath(parentX, parentY);
                    return;
                }
                
                // Explorar vecinos
                for (int i = 0; i < 4; i++) {
                    int newX = cx + dX[i];
                    int newY = cy + dY[i];
                    if (newX >= 0 && newX < filas && newY >= 0 && newY < columnas &&
                        !visited[newX][newY] && controlador.getCelda(newX, newY) == 0) {
                        queue.add(new int[]{newX, newY});
                        visited[newX][newY] = true;
                        parentX[newX][newY] = cx;
                        parentY[newX][newY] = cy;
                        if (!(newX == endX && newY == endY)) {
                            botones[newX][newY].setBackground(Color.ORANGE);
                        }
                    }
                }
            }
        });
        timer.start();
    }
    
    // Animación paso a paso con DFS (iterativo)
    private void animateDFS() {
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
        
        final Timer timer = new Timer(150, null);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (stack.isEmpty()) {
                    timer.stop();
                    JOptionPane.showMessageDialog(null, "No se encontró ruta.");
                    return;
                }
                int[] current = stack.pop();
                int cx = current[0], cy = current[1];
                if (!(cx == startX && cy == startY) && !(cx == endX && cy == endY)) {
                    botones[cx][cy].setBackground(Color.YELLOW);
                }
                if (cx == endX && cy == endY) {
                    timer.stop();
                    reconstructPath(parentX, parentY);
                    return;
                }
                for (int i = 0; i < 4; i++) {
                    int newX = cx + dX[i];
                    int newY = cy + dY[i];
                    if (newX >= 0 && newX < filas && newY >= 0 && newY < columnas &&
                        !visited[newX][newY] && controlador.getCelda(newX, newY) == 0) {
                        stack.push(new int[]{newX, newY});
                        visited[newX][newY] = true;
                        parentX[newX][newY] = cx;
                        parentY[newX][newY] = cy;
                        if (!(newX == endX && newY == endY)) {
                            botones[newX][newY].setBackground(Color.ORANGE);
                        }
                    }
                }
            }
        });
        timer.start();
    }
    
    // Reconstruye y resalta la ruta encontrada (camino óptimo en MAGENTA)
    private void reconstructPath(int[][] parentX, int[][] parentY) {
        int cx = endX, cy = endY;
        while (!(cx == startX && cy == startY)) {
            botones[cx][cy].setBackground(Color.MAGENTA);
            int px = parentX[cx][cy];
            int py = parentY[cx][cy];
            cx = px;
            cy = py;
        }
        botones[startX][startY].setBackground(Color.MAGENTA);
    }
}
