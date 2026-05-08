class DefensaEstatica extends Enemigo {
  int tipo; // 1 = Bunker Terrestre, 2 = Torreta Naval
  Nave objetivo;

  DefensaEstatica(float x, float y, int tipo) {
    super(x, y);
    this.tipo = tipo;
    this.hp = (tipo == 1) ? 8 : 5;
    this.velocidad = 1.0; // Avance solidario con la cámara/islas
    this.puedeDisparar = true;
  }

  @Override
  void actualizar() {
    y += velocidad; // Se desplaza con la Isla o el Portaaviones
    
    // Encuentra a la nave aliada viva más cercana
    objetivo = null;
    float minDist = 9999;
    for (Nave n : listaNaves) { // Nota: asume que listaNaves es global en Main
      if (n.isVivo()) {
        float d = dist(x,y, n.getX(), n.getY());
        if (d < minDist) { 
          minDist = d; 
          objetivo = n; 
        }
      }
    }
    
    if (y > height + 80) vivo = false;
  }

  @Override
  ProyectilEnemigo disparar() {
    if (y > 0 && y < height && objetivo != null) {
      if (tipo == 1) { // Bunker
        if (random(1) < 0.015 && shootCooldown <= 0) {
          shootCooldown = 90;
          float ang = atan2(objetivo.getY() - y, objetivo.getX() - x);
          return new ProyectilEnemigo(x, y, cos(ang)*4, sin(ang)*4); 
        }
      } else if (tipo == 2) { // Torreta Naval
        if (random(1) < 0.02 && shootCooldown <= 0) {
          shootCooldown = 60;
          float ang = atan2(objetivo.getY() - y, objetivo.getX() - x);
          return new ProyectilEnemigo(x, y, cos(ang)*5, sin(ang)*5);
        }
      }
    }
    if (shootCooldown > 0) shootCooldown--;
    return null;
  }

  @Override
  void dibujar() {
    pushMatrix();
    translate(x, y);
    
    if (tipo == 1) { 
      // Bunker Terrestre Base Fija
      fill(90, 100, 90);
      ellipse(0, 0, 50, 50); // Cúpula base ancha de hormigón
      fill(50);
      ellipse(0, 0, 25, 25);
    } else { 
      // Torreta Naval Pequeña
      fill(80);
      ellipse(0, 0, 30, 30);
    }
    
    // Cañón rotativo
    stroke(30); 
    strokeWeight(4);
    if (objetivo != null) {
      float ang = atan2(objetivo.getY() - y, objetivo.getX() - x);
      line(0, 0, cos(ang)*18, sin(ang)*18);
    } else {
      line(0, 0, 0, 18);
    }
    noStroke();
    
    popMatrix();
  }
}
