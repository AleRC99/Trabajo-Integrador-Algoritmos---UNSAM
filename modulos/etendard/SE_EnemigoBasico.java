public class SE_EnemigoBasico extends SE_Enemigo {
  
  private static final String[] SPRITES = {"SeaHarrier", "WestlandLynx", "SeaKing", "FragataTipo21", "AtlanticConveyor"};

  public SE_EnemigoBasico(SE_GestorDeEntidades gestor, float x, float y) {
    // String sprite, int puntaje, boolean boss
    super(gestor, x, y, SPRITES[(int)(Math.random() * SPRITES.length)], 100, false);
    this.velocidad = 2.0f;
    this.ancho = 40;
    this.alto = 40;
  }

  @Override
  public void actualizar() {
    comportarseComoBuscador();
    if (Math.random() < 0.01) {
       gestor.agregarProyectil(new SE_Proyectil(gestor, x, y + 20, 0, 5, false));
    }
  }
}
