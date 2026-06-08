import processing.core.PApplet;

public class SE_HmsSheffield extends SE_EnemigoNaval {
  private int timer = 0;

  public SE_HmsSheffield(SE_GestorDeEntidades gestor, float x, float y) {
    super(gestor, x, y, "HmsSheffield", 2500, true);
    this.velocidad = 1.5f;
    this.ancho = 120;
    this.alto = 60;
    this.vida = 10;
  }

  @Override
  public void actualizar() {
    PApplet app = gestor.gp.getApp();
    // if (y < 150) {
    // y += velocidad * 2;
    // } else
    {
      y += velocidad * 0.08f; // Descenso muy lento (ej. ~0.12 píxeles por frame)
      x += PApplet.sin(timer * 0.02f) * velocidad;

      // Restringir el movimiento lateral al área del agua
      float limiteIzquierdo = app.width * 0.25f;
      float limiteDerecho = app.width * 0.75f;
      x = app.constrain(x, limiteIzquierdo + ancho / 2.0f, limiteDerecho - ancho / 2.0f);
    }

    timer++;
    if (timer % 90 == 0) {
      gestor.agregarProyectil(new SE_Proyectil(gestor, x - 20, y + 20, -1, 4, false));
      gestor.agregarProyectil(new SE_Proyectil(gestor, x + 20, y + 20, 1, 4, false));
      gestor.agregarProyectil(new SE_Proyectil(gestor, x, y + 30, 0, 5, false));
    }
  }
}
