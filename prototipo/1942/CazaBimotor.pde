class CazaBimotor extends Enemigo {
  float timer = 0;

  CazaBimotor(float x, float y) {
    super(x, y); 
    // y debe inicializarse como height + 50 (desde abajo)
    this.velocidad = 2.0; 
    this.hp = 5; // Semi-resistente
    this.puedeDisparar = true;
  }

  @Override
  void actualizar() {
    timer++;
    
    if (timer < 120) {
      y -= velocidad; // Entra de abajo lentamente por detrás
    } else if (timer < 180) {
      y -= velocidad * 0.3; // Frena, preparando disparos
    } else {
      y -= velocidad * 4; // Acelera bruscamente (Warp escape rápido)
    }
    
    if (y < -100) vivo = false;
  }

  @Override
  ProyectilEnemigo disparar() {
    // Ráfaga doble alrededor de un punto de ataque
    if (timer == 140) {
       return new ProyectilEnemigo(x - 15, y - 20, 0, -8); // Dispara HACIA ARRIBA
    }
    if (timer == 150) {
       return new ProyectilEnemigo(x + 15, y - 20, 0, -8);
    }
    return null;
  }

  @Override
  void dibujar() {
    pushMatrix();
    translate(x, y);
    
    fill(130, 140, 100); // Verde oliva apagado
    noStroke();
    
    ellipse(0, 0, 16, 40); // Fuselaje central grande
    rectMode(CENTER);
    rect(0, 5, 60, 12); // Alas robustas atrasadas
    
    // Bimotores gruesos
    fill(100);
    rect(-15, 5, 10, 25);
    rect(15, 5, 10, 25);
    
    // Cola
    rect(0, 15, 25, 6); // La cola está "abajo" visualmente, porque va hacia arriba
    
    fill(50, 150, 255);
    ellipse(0, -10, 8, 12); // Cabina frontal
    
    popMatrix();
  }
}
