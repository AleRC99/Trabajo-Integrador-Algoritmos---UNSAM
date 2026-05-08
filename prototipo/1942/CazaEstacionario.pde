class CazaEstacionario extends Enemigo {
  float timer = 0;
  float startX;

  CazaEstacionario(float x, float y) {
    super(x, y);
    this.startX = x;
    this.hp = 6;
    this.velocidad = 3;
    this.puedeDisparar = true;
  }

  @Override
  void actualizar() {
    timer++;
    
    if (y < 200 && timer < 300) {
      y += velocidad; // Desciende hasta el tercio superior
    } else if (timer < 300) {
      x = startX + sin(timer * 0.05) * 120; // Péndulo amplio e impredecible
    } else {
      y -= velocidad * 2; // Escape hacia arriba
    }
    
    if (y < -100 || y > height + 80) vivo = false;
  }

  @Override
  ProyectilEnemigo disparar() {
    // Rafaguea constante mientras flota
    if (timer > 100 && timer < 280 && timer % 20 == 0) {
       // Dispara en un cono semi-aleatorio
       return new ProyectilEnemigo(x, y + 10, random(-3, 3), 5);
    }
    return null;
  }

  @Override
  void dibujar() {
    pushMatrix();
    translate(x, y);
    
    fill(200, 180, 50); // Color oro/marfil llamativo
    noStroke();
    
    // Caza de Ala Ancha ("Interceptor Estacionario")
    triangle(0, 15, -40, -10, 40, -10); // Forma de Ala Delta enorme
    fill(100);
    rectMode(CENTER);
    rect(0, 0, 15, 30); // Eje central
    
    fill(0);
    ellipse(0, 5, 8, 8); // Ojo central / Cabina esférica
    
    popMatrix();
  }
}
