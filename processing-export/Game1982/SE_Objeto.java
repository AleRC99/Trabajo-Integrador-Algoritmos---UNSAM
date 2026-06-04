public class SE_Objeto {
  public SE_GestorDeEntidades gestor;
  public SE_GestorGrafico gfx;
  public float velocidad;
  public boolean vivo = true;
  public float x, y, ancho, alto;

  public SE_Objeto(SE_GestorDeEntidades gestor, float x, float y) {
    this.gestor = gestor;
    if (gestor != null) {
      this.gfx = gestor.gp.getGraficos();
    }
    this.x = x;
    this.y = y;
  }

  public void actualizar() {}
  public void dibujar() {}
  
  public void morir() {
    this.vivo = false;
  }
}