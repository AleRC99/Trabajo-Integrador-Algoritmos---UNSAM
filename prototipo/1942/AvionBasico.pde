class AvionBasico extends Enemigo {
  int tipo; // 1 = Zeke Loop (Tipo A), 2 = Zeke Diagonal (Tipo B), 3 = Caza Gris
  float startX;
  float t = 0;
  float dirX = 1;

  AvionBasico(float x, float y, int tipo) {
    super(x, y);
    this.tipo = tipo;
    this.startX = x;
    if (random(1) < 0.5) dirX = -1;
    this.velocidad = 3.5;
    this.hp = 1;
    if (tipo == 3) puedeDisparar = true;
  }

  // Constructor por defecto
  AvionBasico(float x, float y) {
    this(x, y, (random(1) < 0.5) ? 1 : 2); // Random entre 1 y 2 si no se pasa nada
  }

  @Override
  void actualizar() {
    t++;
    
    if (tipo == 1) { // Type A: Loop Maker
      if (t < 60) {
        y += velocidad;
      } else if (t < 130) {
        float angle = map(t, 60, 130, -HALF_PI, TWO_PI - HALF_PI);
        y += cos(angle) * 3;
        x += sin(angle) * 4;
      } else {
        y -= velocidad * 1.5; // Sale hacia arriba de regreso!
      }
    } 
    else if (tipo == 2) { // Type B: Diagonal Zigzag
      y += velocidad;
      x += dirX * 3.0;
      if (x > width - 20 || x < 20) dirX *= -1;
    } 
    else if (tipo == 3) { // Caza Gris
      y += velocidad;
    }
    
    if (y > height + 80 || y < -150) vivo = false;
  }

  @Override
  ProyectilEnemigo disparar() {
    if (tipo == 3 && y > height / 2 && shootCooldown <= 0 && random(1) < 0.05) {
      shootCooldown = 999;
      // Tratar de apuntar a la nave mas cercana (o hacia abajo por defecto)
      return new ProyectilEnemigo(x, y + 10, 0, 6);
    }
    return null;
  }

  @Override
  void dibujar() {
    pushMatrix();
    translate(x, y);
    
    // Rotamos el Zeke si está regresando por el loop
    if (tipo == 1 && t >= 130) rotate(PI); 
    
    if (tipo == 3) fill(150, 160, 150); // Caza Gris
    else fill(60, 120, 60); // Verde Zeke Militar
    
    noStroke();
    ellipse(0, 0, 12, 28);
    rectMode(CENTER);
    rect(0, 0, 36, 8);
    // Cola
    rect(0, -12, 14, 4);
    
    fill(30, 30, 50); 
    ellipse(0, 6, 6, 8); 
    
    if (tipo != 3) {
      fill(200, 40, 40);
      ellipse(-10, 0, 5, 5);
      ellipse(10, 0, 5, 5);
    }
    
    popMatrix();
  }
}
