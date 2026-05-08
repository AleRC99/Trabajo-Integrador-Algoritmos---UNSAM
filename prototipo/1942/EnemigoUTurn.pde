class EnemigoUTurn extends Enemigo {
  private float startY;
  private boolean returning = false;
  
  EnemigoUTurn(float x, float y) {
    super(x, y);
    this.velocidad = 3;
    this.startY = y;
    this.puedeDisparar = true; // Estos enemigos suelen disparar
  }

  @Override
  void actualizar() {
    if (!returning) {
      y += velocidad;
      if (y > startY + 300) {
        returning = true;
      }
    } else {
      y -= velocidad;
      // Curva lenta hacia afuera (izquierda o derecha dependiendo de donde apareció)
      if (x > width/2) x += velocidad / 2;
      else x -= velocidad / 2;
      
      if (y < -50 || x < -50 || x > width + 50) vivo = false;
    }
  }

  @Override
  void dibujar() {
    pushMatrix();
    translate(x, y);
    if (returning) rotate(PI); // Rotar 180 grados si está regresando
    
    // Dibujo del avión estilo Zero japonés (alas y cuerpo rectos)
    fill(100, 150, 100); // Verde oliva militar
    noStroke();
    ellipse(0, 0, 12, 30); // Fuselaje central
    rectMode(CENTER);
    rect(0, 0, 34, 8); // Alas grandes
    rect(0, -12, 16, 4); // Cola
    fill(200, 50, 50); // Detalles rojos (insignia)
    ellipse(-10, 0, 6, 6);
    ellipse(10, 0, 6, 6);
    
    popMatrix();
  }
}
