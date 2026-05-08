class AvionKamikaze extends Enemigo {
  AvionKamikaze(float x, float y) {
    super(x, y);
    this.velocidad = 4.5;
  }

  @Override
  void actualizar() {
    y += velocidad;
    
    // Perseguir a la nave más cercana si la lista es global o pasamos el objetivo (mejor usamos alcance global en Processing)
    Nave objetivo = null;
    float distMinima = 9999;
    
    for (Nave nave : listaNaves) {
      if (nave.isVivo()) {
        float d = dist(x, y, nave.getX(), nave.getY());
        if (d < distMinima) {
          distMinima = d;
          objetivo = nave;
        }
      }
    }
    
    if (objetivo != null) {
      if (x < objetivo.getX()) x += 1.8;
      if (x > objetivo.getX()) x -= 1.8;
    }
    
    if (y > height + 20) vivo = false;
  }

  @Override
  void dibujar() {
    pushMatrix();
    translate(x, y);
    
    fill(200, 60, 60); // Rojo agresivo
    noStroke();
    triangle(0, 18, -10, -12, 10, -12); // Morro afilado
    rectMode(CENTER);
    rect(0, -2, 24, 6); // Alas delta
    
    fill(0);
    ellipse(0, 4, 4, 8); // Cabina
    
    popMatrix();
  }
}
