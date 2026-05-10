🎯 PUNTO 1: Sistema de Colisiones (AABB + Grid Espacial)
🧩 Analogía sencilla
Imagina que estás en una sala oscura con 100 personas flotando. Para saber si dos chocaron, ¿revisarías a cada persona contra las otras 99 una por una? Eso serían 4,950 comparaciones por segundo. Si la sala se mueve, el cerebro se colapsa.
La solución real: Divide la sala en cuadrantes (una grilla). Solo preguntas: "¿Hay alguien en mi cuadrante o en los vecinos?". Pasas de revisar miles de pares a revisar solo 2 o 3.
💻 Implementación técnica (Java 17+)

/**
 * Detector de colisiones AABB optimizado con partición espacial uniforme.
 * Cumple SRP: solo gestiona geometría, no lógica de juego.
 * 
 * @see CLRS Cap. 33 (Geometric Searching)
 * @see TPI RNF-03 (Rendimiento estable a 60 FPS)
 */
public final class CollisionDetector {
    private final int cellSize;
    private final Map<Point, List<GameObject>> grid = new HashMap<>();

    public CollisionDetector(int cellSize) {
        this.cellSize = cellSize;
    }

    /** Inserta entidades en la grilla (complejidad O(n)) */
    public void rebuild(List<GameObject> activeEntities) {
        grid.clear();
        for (GameObject obj : activeEntities) {
            Point cell = getCellKey(obj);
            grid.computeIfAbsent(cell, k -> new ArrayList<>()).add(obj);
        }
    }

    /** Retorna pares en colisión sin duplicados ni autocolisiones */
    public List<Pair<GameObject>> detect() {
        List<Pair<GameObject>> collisions = new ArrayList<>();
        Set<Long> checked = new HashSet<>(); // evita doble chequeo (A,B) y (B,A)

        for (Map.Entry<Point, List<GameObject>> entry : grid.entrySet()) {
            Point cell = entry.getKey();
            List<GameObject> currentCell = entry.getValue();

            // 1. Colisiones internas de la misma celda
            for (int i = 0; i < currentCell.size(); i++) {
                for (int j = i + 1; j < currentCell.size(); j++) {
                    addIfColliding(currentCell.get(i), currentCell.get(j), collisions, checked);
                }
            }

            // 2. Colisiones con celdas vecinas (8 vecinos + centro)
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue;
                    Point neighbor = new Point(cell.x + dx, cell.y + dy);
                    List<GameObject> neighborCell = grid.get(neighbor);
                    if (neighborCell == null) continue;

                    for (GameObject a : currentCell) {
                        for (GameObject b : neighborCell) {
                            addIfColliding(a, b, collisions, checked);
                        }
                    }
                }
            }
        }
        return collisions;
    }

    private void addIfColliding(GameObject a, GameObject b, List<Pair<GameObject>> collisions, Set<Long> checked) {
        long key = Math.min(a.hashCode(), b.hashCode()) * 31L + Math.max(a.hashCode(), b.hashCode());
        if (checked.contains(key)) return;
        checked.add(key);

        if (a.getBounds().intersects(b.getBounds())) {
            collisions.add(new Pair<>(a, b));
        }
    }

    private Point getCellKey(GameObject obj) {
        return new Point(obj.getX() / cellSize, obj.getY() / cellSize);
    }

    public record Pair<T>(T first, T second) {}
}

🔄 PUNTO 2: Object Pool (Reutilización de Entidades)
🧩 Analogía sencilla
En un restaurante de alta rotación, no compras un plato nuevo para cada cliente. Los lavas y los reutilizas.
En Java, cada vez que haces new Bala(), el sistema reserva memoria. Cuando la bala sale de pantalla, la dejas morir y el Garbage Collector (GC) debe limpiarla. Si disparas 10 veces por segundo, el GC se activa constantemente y el juego se traba 1-2 frames (lag visible).
El Object Pool es el "lavaplatos": al inicio crea 100 balas vacías. Cuando disparas, toma una inactiva, la activa y la coloca en pantalla. Cuando choca o sale, la desactiva y la devuelve al pool. Cero new durante el juego.
💻 Implementación técnica (Java 17+)

package com.unsam.algo1.module6.core;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.function.Supplier;

/**
 * Pool genérico de objetos reutilizables. Evita allocation/GC en el game loop.
 * 
 * @see Effective Java Ítem 59 (Avoid unnecessary object creation)
 * @see CLRS Cap. 10 (Queues, O(1) poll/offer)
 */
public final class ObjectPool<T extends Poolable> {
    private final Queue<T> available;
    private final Supplier<T> factory;

    public ObjectPool(int initialCapacity, Supplier<T> factory) {
        this.factory = factory;
        this.available = new ArrayDeque<>(initialCapacity);
        // Pre-allocación en init()
        for (int i = 0; i < initialCapacity; i++) {
            available.add(factory.get());
        }
    }

    /** Retorna un objeto inactivo listo para usar. Si no hay, crea uno nuevo (seguridad) */
    public T acquire() {
        T obj = available.poll();
        if (obj == null) obj = factory.get(); // fallback seguro
        obj.activate();
        return obj;
    }

    /** Devuelve el objeto al pool tras su uso */
    public void release(T obj) {
        if (obj.isActive()) { // solo recicla si sigue vivo
            obj.reset();
            available.offer(obj);
        }
    }

    /** Interfaz que deben cumplir los objetos poolables */
    public interface Poolable {
        void activate();
        void reset();
        boolean isActive();
    }
}

Uso práctico en tu módulo:
// En init() del módulo:
ObjectPool<Projectile> projectilePool = new ObjectPool<>(100, Projectile::new);

// Al disparar:
Projectile p = projectilePool.acquire();
p.setPosition(player.getX(), player.getY());
p.setVelocity(0, -10);

// En el game loop, cuando sale de pantalla o choca:
if (!p.isVisible()) projectilePool.release(p);