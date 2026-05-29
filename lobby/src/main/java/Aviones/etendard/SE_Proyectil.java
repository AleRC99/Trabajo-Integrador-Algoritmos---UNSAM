package Aviones.etendard;

import processing.core.PApplet;

public class SE_Proyectil extends SE_Objeto {
  boolean esAliado;
  float vx, vy;
  
  // Constructor original (movimiento vertical)
  public SE_Proyectil(SE_GestorDeEntidades gestor, float x, float y, boolean esAliado) {
    super(gestor, x, y);
    this.esAliado = esAliado;
    this.vx = 0;
    this.vy = esAliado ? -7 : 5; // Aliado sube rápido, enemigo baja más lento
    this.ancho = 4;
    this.alto = 15;
  }
  
  // Constructor para disparo angulado (Enemigos)
  public SE_Proyectil(SE_GestorDeEntidades gestor, float x, float y, float vx, float vy, boolean esAliado) {
    super(gestor, x, y);
    this.esAliado = esAliado;
    this.vx = vx;
    this.vy = vy;
    this.ancho = 8;
    this.alto = 8;
  }

  @Override
  public void actualizar() {
    PApplet app = gestor.gp.getApp();
    x += vx;
    y += vy;
    if (y > app.height + 20 || y < -20 || x > app.width + 20 || x < -20) vivo = false;
  }

  @Override
  public void dibujar() {
    PApplet app = gestor.gp.getApp();
    app.pushMatrix();
    app.translate(x, y);
    if (esAliado) {
      app.fill(255, 255, 0); // Amarillo aliado
      app.rectMode(PApplet.CENTER);
      app.rect(0, 0, 5, 15);
    } else {
      app.fill(255, 100, 0); // Naranja enemigo
      app.noStroke();
      app.ellipseMode(PApplet.CENTER);
      app.ellipse(0, 0, 8, 8);
      app.fill(255, 200, 0, 100);
      app.ellipse(0, 0, 12, 12);
    }
    app.popMatrix();
  }
}
