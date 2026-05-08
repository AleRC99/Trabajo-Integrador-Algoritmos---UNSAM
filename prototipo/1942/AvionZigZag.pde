class AvionZigZag extends Enemigo {
  private float angulo = 0;

  AvionZigZag(float x, float y) {
    super(x, y);
    this.velocidad = 1.6;
  }

  @Override
  void actualizar() {
    y += velocidad;
    x += sin(angulo) * 3; // Movimiento escurridizo
    angulo += 0.05;
    
    if (y > height + 20) vivo = false;
  }

  @Override
  void dibujar() {
    pushMatrix();
    translate(x, y);
    
    fill(150, 120, 70); // Marrón claro/Camuflaje arena
    noStroke();
    ellipse(0, 0, 10, 26); // Fuselaje 
    rectMode(CENTER);
    rect(0, 2, 28, 6); // Alas un poco más adelantadas
    rect(0, -10, 12, 3); // Cola
    
    fill(50, 30, 20); // Cabina oscura
    ellipse(0, 4, 6, 6); 
    
    fill(200, 40, 40);
    ellipse(-8, 2, 4, 4);
    ellipse(8, 2, 4, 4);
    
    popMatrix();
  }
}
