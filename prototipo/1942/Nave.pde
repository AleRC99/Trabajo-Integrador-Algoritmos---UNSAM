class Nave extends Objeto {
  private boolean yendoDerecha, yendoIzquierda, yendoArriba, yendoAbajo;
  private float powerUpTimer = 0;
  private boolean isDoubleShot = false;
  
  private int idJugador; // 1 = Flechas, 2 = WASD
  private int tipoNave;  // 1 = P-38 Heavy, 2 = Zero Light

  Nave(float x, float y, int idJugador, int tipoNave) {
    super(x, y);
    this.idJugador = idJugador;
    this.tipoNave = tipoNave;
    
    // Stats según tipo
    if (tipoNave == 1) {
      this.velocidad = 5; // P-38 balanceado
    } else {
      this.velocidad = 7; // Caza ágil rápido
    }
  }

  @Override
  void actualizar() {
    float velActual = velocidad;
    if (powerUpTimer > 0) {
      powerUpTimer--;
      if (powerUpTimer <= 0) isDoubleShot = false; // Fin de PowerUp
    }

    if (yendoDerecha && x < width - 20) x += velActual;
    if (yendoIzquierda && x > 20) x -= velActual;
    if (yendoArriba && y > 50) y -= velActual; 
    if (yendoAbajo && y < height - 20) y += velActual;
  }

  @Override
  void dibujar() {
    pushMatrix();
    translate(x, y);
    
    if (tipoNave == 1) {
      // DIBUJO DEL P-38 LIGHTNING
      if (powerUpTimer > 0) fill(150, 255, 150); // Buff
      else if (idJugador == 1) fill(180, 200, 220); // Gris azulado (P1)
      else fill(200, 180, 150); // Marrón claro (P2)
      
      noStroke();
      ellipse(0, 0, 14, 34); // Fuselaje central
      rectMode(CENTER);
      rect(0, 2, 50, 8); // Alas
      rect(-20, 10, 6, 30); // Cola izquierda
      rect(20, 10, 6, 30);  // Cola derecha
      rect(0, 23, 46, 5);   // Unión trasera
      fill(50, 150, 255);
      ellipse(0, -5, 8, 12); // Cabina
    } 
    else {
      // DIBUJO DEL CAZA ÁGIL
      if (powerUpTimer > 0) fill(150, 255, 150); 
      else if (idJugador == 1) fill(250, 200, 50); // Amarillo P1
      else fill(50, 200, 100); // Verde P2
      
      noStroke();
      ellipse(0, 0, 10, 28); // Más delgado
      triangle(0, -15, -25, 10, 25, 10); // Alas delta para verse rápido
      rectMode(CENTER);
      rect(0, 12, 16, 4); // Cola pequeña
      fill(50, 150, 255);
      ellipse(0, -2, 6, 8); // Cabina
    }
    
    popMatrix();
  }

  void actibarMejora() {
    powerUpTimer = 600; // 10 segundos
    isDoubleShot = true;
  }
  
  boolean hasDoubleShot() { return isDoubleShot; }
  int getIdJugador() { return idJugador; }

  void setYendoDerecha(boolean v) { yendoDerecha = v; }
  void setYendoIzquierda(boolean v) { yendoIzquierda = v; }
  void setYendoArriba(boolean v) { yendoArriba = v; }
  void setYendoAbajo(boolean v) { yendoAbajo = v; }
}
