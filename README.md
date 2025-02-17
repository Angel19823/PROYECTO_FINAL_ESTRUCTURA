Informe Técnico: Implementación de Algoritmos para Resolver Laberintos 

1. Introducción 

Este informe documenta el desarrollo de una aplicación para encontrar la ruta óptima en un laberinto utilizando diferentes enfoques algorítmicos. El proyecto está basado en la materia "Estructura de Datos" y aplica conceptos de programación dinámica, estructuras de datos lineales y no lineales. 

2. Objetivo 

Desarrollar una aplicación en Java que implemente y compare distintos algoritmos para la búsqueda de caminos en un laberinto desde un punto inicial hasta un punto final. Se han utilizado estrategias como BFS, programación dinámica y recursión para evaluar su eficiencia y rendimiento. 

3. Marco Teórico 

3.1 Algoritmos de Búsqueda 

BFS (Breadth-First Search): Explora los vecinos de cada nodo antes de avanzar más profundo en el laberinto. Garantiza encontrar la ruta más corta. 

Programación Dinámica: Utiliza almacenamiento intermedio (memoización) para evitar cálculos redundantes. 

Búsqueda Recursiva: Explora todas las posibilidades utilizando llamadas recursivas hasta encontrar la solución. 

3.2 Representación del Laberinto 

El laberinto se modela como una matriz de celdas transitables y no transitables. Se emplea la clase Cell para representar cada celda del laberinto. 

4. Descripción de la Solución 

4.1 Estructura del Proyecto 

Modelo (models): Contiene las clases Maze y Cell. 

Controladores (controllers): Contiene las clases de los algoritmos de resolución de laberintos. 

Interfaz (interfaces): Define la interfaz MazeSolve para estandarizar las implementaciones. 

Clase Principal (App.java): Orquesta la ejecución de los algoritmos y la visualización del laberinto. 

4.2 Implementación de Algoritmos 

Cada algoritmo se implementa en una clase distinta: 

MazeSolverRecursivo.java: Implementa una solución recursiva para recorrer el laberinto. 

MazeSolveBFS.java: Usa BFS para encontrar la ruta más corta. 

MazeSolverDPB.java: Aplica programación dinámica para mejorar la eficiencia. 

4.3 Entrada y Salida 

Entrada: Matriz booleana que representa el laberinto, celdas de inicio y destino. 

Salida: Lista de celdas que representan el camino encontrado. 

5. Resultados y Comparación 

Se ejecutaron pruebas con diferentes tamaños de laberintos y configuraciones. Se obtuvieron los siguientes resultados:


Algoritmo     Éxito     Pasos Recorridos     Tiempo de Ejecución 

BFS             ✓           Mínimo                Rápido 

Recursivo       ✓          Variable               Medio 

DPB             ✓         Optimizado              Medio 

6. Conclusiones 

BFS es la mejor estrategia para encontrar el camino más corto. 

La programación dinámica optimiza la búsqueda reduciendo cálculos redundantes. 

La búsqueda recursiva es menos eficiente en laberintos grandes debido a la pila de llamadas. 

7. Recomendaciones 

Mejorar la interfaz de usuario para facilitar la configuración del laberinto. 

Implementar visualizaciones gráficas para mostrar la exploración del laberinto en tiempo real. 

Explorar algoritmos heurísticos como A* para mejorar la eficiencia. 