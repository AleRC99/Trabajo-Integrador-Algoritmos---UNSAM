# Casos de Uso: Módulo 1942

A continuación se detallan los Casos de Uso (CU) principales que definen las interacciones del jugador (Actor) con el sistema del juego. Este listado es ideal para incluir en la documentación técnica del proyecto.

## Actor Principal: Jugador
## Actor Secundario: Sistema (Lobby Integrador / Gestor Principal)

---

### CU-01: Iniciar Partida
* **Actor:** Jugador
* **Descripción:** El jugador selecciona la cantidad de jugadores (1 o 2) y el nivel de dificultad en la pantalla de inicio, y comienza a jugar.
* **Flujo Principal:** El sistema genera el contexto de la partida, limpia las listas de entidades, posiciona la/las nave/s en el punto de partida y comienza el ciclo de oleadas de enemigos.

### CU-02: Controlar Nave (Movimiento)
* **Actor:** Jugador
* **Descripción:** El jugador desplaza su nave por el eje X e Y de la pantalla.
* **Flujo Principal:** El jugador presiona las teclas de dirección (Flechas o WASD). El sistema actualiza las coordenadas de la HitBox de la nave respetando los límites (bordes) de la pantalla.

### CU-03: Disparar Proyectil
* **Actor:** Jugador
* **Descripción:** El jugador ejecuta un ataque frontal para dañar a los enemigos.
* **Flujo Principal:** El jugador presiona la tecla de acción (Espacio o 'F'). El sistema instancia un `Proyectil` aliado en la posición actual de la nave, el cual se desplaza verticalmente hacia arriba.

### CU-04: Recolectar Mejora (Power-Up)
* **Actor:** Jugador
* **Descripción:** El jugador adquiere una ventaja táctica durante la partida.
* **Flujo Principal:** La nave del jugador colisiona con el HitBox de un objeto `Mejora`. El sistema elimina la mejora de la pantalla, aplica el efecto correspondiente a la nave (ej. disparo doble) y suma puntos extra al score.

### CU-05: Destruir Enemigo
* **Actor:** Jugador / Sistema
* **Descripción:** Un proyectil aliado impacta contra un enemigo, reduciendo su salud.
* **Flujo Principal:** El sistema detecta colisión entre un proyectil y un enemigo. Reduce el HP del enemigo. Si el HP llega a 0, el sistema registra la muerte, suma los puntos al contador de sesión y, bajo ciertas probabilidades, genera una `Mejora` en la posición donde murió el enemigo.

### CU-06: Recibir Daño (Perder Partida)
* **Actor:** Sistema
* **Descripción:** La nave del jugador sufre daño letal al impactar contra un enemigo o un proyectil enemigo.
* **Flujo Principal:** El sistema detecta la colisión, destruye la nave aliada. Si no quedan naves vivas, el sistema detiene el juego, cambia el estado a `FinalizadoState` (Derrota) y muestra la pantalla de Game Over.

### CU-07: Pausar y Reanudar Juego
* **Actor:** Jugador
* **Descripción:** El jugador interrumpe temporalmente la partida.
* **Flujo Principal:** El jugador presiona la tecla 'P'. El sistema congela las actualizaciones físicas (`procesar(true)`), dibuja un filtro translúcido sobre la pantalla y espera a que el jugador vuelva a presionar 'P' para reanudar el ciclo lógico normal.

### CU-08: Completar Nivel (Ganar Partida)
* **Actor:** Sistema
* **Descripción:** El jugador sobrevive a todas las oleadas de enemigos y derrota al Jefe Final.
* **Flujo Principal:** El Gestor de Oleadas termina su cronograma. El sistema detecta que el Jefe ha sido destruido y no quedan enemigos vivos. Se declara la victoria, se consolidan las estadísticas y se pasa a la pantalla de victoria (Stage Clear).

### CU-09: Consolidar Estadísticas
* **Actor:** Sistema (Automático)
* **Descripción:** Al finalizar una partida (por victoria o derrota), el sistema archiva los resultados.
* **Flujo Principal:** El `GestorPrincipal` toma el *Score* y el tiempo acumulado y se lo pasa al objeto `EstadisticasGenerales`. Si el Score supera al "Mejor Puntaje" histórico, se registra un nuevo récord para informar al Lobby.

### CU-10: Salir al Menú Principal (Lobby)
* **Actor:** Jugador
* **Descripción:** El jugador abandona el módulo 1942 para regresar a la interfaz principal de la materia.
* **Flujo Principal:** El jugador presiona 'Q' o 'ESC' (o 'R' estando en Game Over para volver al menú de inicio). El sistema cede el control lógico al `ControladorNavegacion` del Lobby.
