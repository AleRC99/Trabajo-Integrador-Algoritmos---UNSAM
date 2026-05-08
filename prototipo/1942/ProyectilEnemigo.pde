class ProyectilEnemigo extends Objeto {
  float vx, vy;
  
  ProyectilEnemigo(float x, float y, float vx, float vy) {
    super(x, y);
    this.vx = vx;
    this.vy = vy;
  }
  
  // Constructor original retro-compatible
  ProyectilEnemigo(float x, float y) {
    super(x, y);
    this.vx = 0;
    this.vy = 5; // Siempre hacia abajo por defecto
  }

  @Override
  void actualizar() {
    x += vx;
    y += vy;
    
    // Si sale de cualquier borde de la pantalla desaparece
    if (y > height + 20 || y < -20 || x > width + 20 || x < -20) vivo = false;
  }

  @Override
  void dibujar() {
    pushMatrix();
    translate(x, y);
    fill(255, 100, 0); // Naranja reflectante
    noStroke();
    ellipse(0, 0, 8, 8);
    // Efecto de brillo de energía
    fill(255, 200, 0, 100);
    ellipse(0, 0, 12, 12);
    popMatrix();
  }
}
