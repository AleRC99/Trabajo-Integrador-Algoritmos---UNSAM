abstract class Objeto {
  protected float x, y;
  protected float velocidad;
  protected boolean vivo;

  Objeto(float x, float y) {
    this.x = x;
    this.y = y;
    this.vivo = true;
  }

  abstract void actualizar();
  abstract void dibujar();

  float getX() { return x; }
  float getY() { return y; }
  boolean isVivo() { return vivo; }
  
  void morir() {
    this.vivo = false;
  }
}
