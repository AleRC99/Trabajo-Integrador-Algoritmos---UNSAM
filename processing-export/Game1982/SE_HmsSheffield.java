import processing.core.PApplet;

public class SE_HmsSheffield extends SE_Enemigo {
  private int timer = 0;

  public SE_HmsSheffield(SE_GestorDeEntidades gestor, float x, float y) {
    super(gestor, x, y, "HmsSheffield", 2500, true);
    this.velocidad = 0.5f;
    this.ancho = 120;
    this.alto = 60;
  }

  @Override
  public void actualizar() {
    PApplet app = gestor.gp.getApp();
    if (y < 150) {
      y += velocidad * 2; 
    } else {
      x += PApplet.sin(timer * 0.02f) * velocidad;
    }
    
    timer++;
    if (timer % 90 == 0) {
      gestor.agregarProyectil(new SE_Proyectil(gestor, x - 20, y + 20, -1, 4, false));
      gestor.agregarProyectil(new SE_Proyectil(gestor, x + 20, y + 20, 1, 4, false));
      gestor.agregarProyectil(new SE_Proyectil(gestor, x, y + 30, 0, 5, false));
    }
  }
}
