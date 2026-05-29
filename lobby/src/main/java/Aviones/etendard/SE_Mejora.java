package Aviones.etendard;

import processing.core.PApplet;

public class SE_Mejora extends SE_Objeto {
  private int tipo;

  public SE_Mejora(SE_GestorDeEntidades gestor, float x, float y, int tipo) {
    super(gestor, x, y);
    this.tipo = tipo;
    this.ancho = 20;
    this.alto = 20;
  }

  @Override
  public void actualizar() {
    y += 1.5;
    if (y > gestor.gp.getApp().height + 20) vivo = false;
  }

  @Override
  public void dibujar() {
    PApplet app = gestor.gp.getApp();
    app.fill(0, 255, 0);
    app.ellipse(x, y, 20, 20);
    app.fill(0);
    app.text("P", x-5, y+5);
  }

  public int getTipo() { return tipo; }
}
