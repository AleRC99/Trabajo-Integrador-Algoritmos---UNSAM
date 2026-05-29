// ============================================================
// MAIN — Punto de entrada del sistema multi-juego.
// Instancia el HomeJuego (lobby) y delega todo el control.
// Las constantes de fase siguen aquí porque son globales al juego.
// ============================================================

// Constantes de estado del juego 1942 (usadas en GestorGrafico)
final int ESTADO_GAMEOVER = 2;
final int ESTADO_WIN      = 3;

// Constantes de fase del nivel 1942
final int FASE_INICIO = 1;
final int FASE_MEDIO  = 2;
final int FASE_FINAL  = 3;

// Instancias globales accesibles por todo el sketch de Processing
HomeJuego     lobby;
SE_GestorGrafico graficos;

// Referencia de conveniencia al juego activo (usada por Pantallas internas)
// Se obtiene dinámicamente del lobby cuando se necesita.
SE_GestorPrincipal juego;

void setup() {
  size(600, 600);
  ellipseMode(CENTER);
  rectMode(CENTER);
  imageMode(CENTER);

  graficos = new SE_GestorGrafico();
  graficos.cargarSpriters();

  lobby = new HomeJuego();
  lobby.iniciarHome();

  // Guardamos la referencia al juego 1942 para que las Pantallas internas puedan acceder
  juego = (SE_GestorPrincipal) lobby.gestorModulos.buscarModulo(0);
}

void draw() {
  lobby.actualizarYDibujar();
}

void keyPressed() {
  lobby.procesarKeyPressed(key, keyCode);
}

void keyReleased() {
  lobby.procesarKeyReleased(key, keyCode);
}
