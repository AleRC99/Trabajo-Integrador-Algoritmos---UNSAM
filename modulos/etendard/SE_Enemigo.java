
import processing.core.PApplet;
import processing.core.PImage;

public class SE_Enemigo extends SE_Objeto {
  String tipo;
  int timer = 0;
  float t = 0;
  int estado = 0;
  float startX;
  float dirX = 1;
  
  boolean puedeDisparar = false;
  int shootCooldown = 0;
  int hp = 1;

  public SE_Enemigo(SE_GestorDeEntidades gestor, float x, float y, String tipo) {
    super(gestor, x, y);
    this.tipo = tipo;
    this.startX = x;
    PApplet app = gestor.gp.getApp();
    shootCooldown = (int)app.random(60, 120);

    switch (tipo) {
      case "SeaHarrier":
        velocidad = 5; hp = 2; ancho = 30; alto = 30;
        break;
      case "SeaKing":
        hp = 4; ancho = 30; alto = 30;
        break;
      case "WestlandLynx":
        velocidad = 2.5f; hp = 1; ancho = 30; alto = 30;
        break;
      case "FragataTipo21":
        velocidad = 0.5f; hp = 8; ancho = 40; alto = 120;
        break;
      case "AtlanticConveyor":
        velocidad = 0.3f; hp = 15; ancho = 60; alto = 160;
        break;
      case "HmsSheffield":
        hp = 80; ancho = 80; alto = 200;
        break;
      default:
        velocidad = 2; hp = 1; ancho = 30; alto = 30;
        break;
    }
  }

  public String getTipo() { return tipo; }

  public void recibirDano() {
    hp--;
    if (hp <= 0) vivo = false;
  }

/*
  public SE_Mejora soltarMejora() {
    PApplet app = gestor.gp.getApp();
    if (app.random(1) < 0.2f) {
      return new SE_Mejora(this.gestor, x, y, 1);
    }
    return null;
  }
*/

  public void disparar() {
    PApplet app = gestor.gp.getApp();
    if (y > 0 && y < app.height / 2) {
      if (shootCooldown <= 0) {
        shootCooldown = 150 + (int)app.random(60); 
        SE_Nave objetivo = null;
        for (SE_Nave n : this.gestor.getNaves()) {
           if (n.isVivo()) objetivo = n; 
        }
        
        if (objetivo != null) {
           float ang = PApplet.atan2(objetivo.getY() - y, objetivo.getX() - x);
           this.gestor.agregarProyectil(new SE_Proyectil(this.gestor, x, y + 10, PApplet.cos(ang)*5, PApplet.sin(ang)*5, false));
        } else {
           this.gestor.agregarProyectil(new SE_Proyectil(this.gestor, x, y + 10, 0, 5, false));
        }
      }
      shootCooldown--;
    }
  }

  public boolean colisionaConNave() { return true; }

  @Override
  public void actualizar() {
    PApplet app = gestor.gp.getApp();
    timer++;
    switch (tipo) {
      case "SeaHarrier":
        if (estado == 0) {
          velocidad = PApplet.lerp(velocidad, 0.5f, 0.05f);
          y += velocidad;
          if (velocidad < 0.8f) { estado = 1; timer = 0; }
        } else if (estado == 1) {
          if (timer > 80) { estado = 2; velocidad = 0; }
        } else if (estado == 2) {
          velocidad += 0.4f; y -= velocidad;
          if (y < -80) vivo = false;
        }
        break;
      case "SeaKing":
        x += dirX * 1.5f;
        if (x < -100 || x > app.width + 100) vivo = false;
        break;
      case "WestlandLynx":
        y += velocidad; t += 0.04f; x = startX + PApplet.sin(t) * 150;
        if (y > app.height + 50 || x < -50 || x > app.width + 50) vivo = false;
        break;
      case "FragataTipo21":
        y += velocidad; x += PApplet.sin(timer * 0.02f) * 0.3f;
        if (y > app.height + 100) vivo = false;
        break;
      case "AtlanticConveyor":
        y += velocidad; x += PApplet.sin(timer * 0.01f) * 0.1f;
        if (timer % 300 == 0 && y > -50 && y < app.height) {
          this.gestor.agregarEnemigo(new SE_Enemigo(this.gestor, x, y, "SeaHarrier"));
        }
        if (y > app.height + 200) vivo = false;
        break;
      case "HmsSheffield":
        if (y < 150) y += 1.5f;
        else x += PApplet.sin(timer * 0.005f) * 0.2f;
        if (hp <= 0) vivo = false;
        if (y > app.height + 200) vivo = false;
        break;
      default:
        y += 2;
        if (y > app.height + 50) vivo = false;
        break;
    }
  }

  @Override
  public void dibujar() {
    PApplet app = gestor.gp.getApp();
    app.pushMatrix(); 
    app.translate(x, y);
    PImage sprite = (gfx != null) ? gfx.getImagen(tipo) : null;
    
    if (sprite != null) {
      if (tipo.equals("HmsSheffield")) gfx.dibujarConBorde(sprite, 0, 0, app.color(255, 0, 0));
      else gfx.dibujarConBorde(sprite, 0, 0, app.color(255, 50, 50));
    } else {
      app.rectMode(PApplet.CENTER);
      app.fill(tipo.equals("HmsSheffield") ? app.color(180, 50, 50) : app.color(120, 120, 140));
      app.stroke(255, 0, 0);
      app.strokeWeight(1.5f);
      app.rect(0, 0, ancho, alto);
    }
    app.popMatrix();
  }
}