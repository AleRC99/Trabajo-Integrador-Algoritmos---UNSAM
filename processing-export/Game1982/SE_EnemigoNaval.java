public class SE_EnemigoNaval extends SE_Enemigo {

  private static final String[] SPRITES = { "FragataTipo21", "AtlanticConveyor" };

  public SE_EnemigoNaval(SE_GestorDeEntidades gestor, float x, float y) {
    // String sprite, int puntaje, boolean boss
    super(gestor, x, y, SPRITES[(int) (Math.random() * SPRITES.length)], 150, false);
    this.velocidad = 1.0f; // Más lentos que los aviones
    this.ancho = 50;
    this.alto = 30;
  }

  public SE_EnemigoNaval(SE_GestorDeEntidades gestor, float x, float y, String sprite, int puntaje, boolean boss) {
    super(gestor, x, y, sprite, puntaje, boss);
  }

  @Override
  public void actualizar() {
    // Los barcos bajan lentamente con el agua (scroll)
    y += velocidad;

    // Disparo ocasional hacia abajo
    if (Math.random() < 0.01) {
      gestor.agregarProyectil(new SE_Proyectil(gestor, x, y + 15, 0, 4, false));
    }
  }

  @Override
  public boolean puedeColisionarConNave() {
    return false;
  }
}
