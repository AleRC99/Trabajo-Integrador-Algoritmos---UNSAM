class AvionEscuadronRojo extends Enemigo {
  boolean formaParteDeEscuadronFormacion = true; // Para lógica de Mejora
  
  AvionEscuadronRojo(float x, float y) {
    super(x, y);
    this.velocidad = 5.5; // Muy rápidos en línea recta
    this.hp = 1;
  }

  @Override
  void actualizar() {
    y += velocidad;
    if (y > height + 20) vivo = false;
  }

  @Override
  void dibujar() {
    pushMatrix();
    translate(x, y);
    
    fill(250, 40, 40); // Rojo Brillante
    noStroke();
    ellipse(0, 0, 10, 24); // Fuselaje central
    rectMode(CENTER);
    rect(0, 2, 26, 6); // Alas 
    rect(0, -10, 10, 4); // Cola
    
    fill(255); // Cabina de as
    ellipse(0, 4, 4, 6);
    
    popMatrix();
  }
}
