
public class SE_Objeto {
  protected SE_GestorDeEntidades gestor;
  protected SE_GestorGrafico gfx;
  protected float x, y;
  protected float velocidad;
  protected boolean vivo = true;
  protected float ancho, alto;

  public SE_Objeto(SE_GestorDeEntidades gestor, float x, float y) {
    this.gestor = gestor;
    this.gfx   = gestor.gp.getGraficos();
    this.x = x;
    this.y = y;
    this.vivo = true;
  }

  public void actualizar() {}
  public void dibujar() {}
  
  public float getAncho() { return ancho; }
  public float getAlto() { return alto; }

  public float getX() { return x; }
  public float getY() { return y; }
  public boolean isVivo() { return vivo; }
  
  public void morir() {
    this.vivo = false;
  }
}