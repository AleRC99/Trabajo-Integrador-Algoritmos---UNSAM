
import processing.core.PApplet;
import processing.core.PImage;

public class SE_Nave extends SE_Objeto {
  protected boolean yendoDerecha, yendoIzquierda, yendoArriba, yendoAbajo;
  // protected float powerUpTimer = 0;
  // protected boolean isDoubleShot = false;
  protected boolean disparando = false;
  protected int cooldown = 0;
  
  public SE_Nave(SE_GestorDeEntidades gestor, float x, float y) {
    super(gestor, x, y);
    this.velocidad = 5.0f; // Velocidad estándar del Super Étendard
    this.ancho = 30;
    this.alto = 30;
  }

/*
  public void activarMejora() {
    powerUpTimer = 600; // 10 segundos
    isDoubleShot = true;
  }
  
  public boolean hasDoubleShot() { return isDoubleShot; }
*/
  public void setDisparando(boolean v) { disparando = v; }

  public void setYendoDerecha(boolean v) { yendoDerecha = v; }
  public void setYendoIzquierda(boolean v) { yendoIzquierda = v; }
  public void setYendoArriba(boolean v) { yendoArriba = v; }
  public void setYendoAbajo(boolean v) { yendoAbajo = v; }

  @Override
  public void actualizar() {
    PApplet app = gestor.gp.getApp();
    float velActual = velocidad;
/*
    if (powerUpTimer > 0) {
      powerUpTimer--;
      if (powerUpTimer <= 0) isDoubleShot = false; // Fin de PowerUp
    }
*/

    if (yendoDerecha && x < app.width - 20) x += velActual;
    if (yendoIzquierda && x > 20) x -= velActual;
    if (yendoArriba && y > 50) y -= velActual; 
    if (yendoAbajo && y < app.height - 20) y += velActual;
    
    x = app.constrain(x, 20, app.width - 20);
    y = app.constrain(y, 20, app.height - 20);
    
    if (cooldown > 0) cooldown--;
    
    disparar();
  }

  public void disparar() {
    if (disparando && cooldown == 0) {
      cooldown = 10;
/*
      if (isDoubleShot) {
        gestor.agregarProyectil(new SE_Proyectil(gestor, x - 12, y - 10, true));
        gestor.agregarProyectil(new SE_Proyectil(gestor, x + 12, y - 10, true));
      } else {
*/
        gestor.agregarProyectil(new SE_Proyectil(gestor, x, y - 10, true));
//      }
    }
  }

  @Override
  public void dibujar() {
    PApplet app = gestor.gp.getApp();
    app.pushMatrix();
    app.translate(x, y);
    PImage sprite = (gfx != null) ? gfx.getImagen("Nave") : null;
    if (sprite != null) {
      // if (powerUpTimer > 0) app.tint(150, 255, 150);
      // else app.tint(200, 220, 255);
      app.tint(200, 220, 255);
      
      gfx.dibujarConBorde(sprite, 0, 0, app.color(255));
      app.noTint();
    } else {
      app.fill(app.color(100, 150, 255));
      // if (powerUpTimer > 0) app.fill(150, 255, 150);
      app.rect(-15, -15, 30, 30);
    }
    app.popMatrix();
  }
}