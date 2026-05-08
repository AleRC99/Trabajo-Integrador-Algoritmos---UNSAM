class Mejora extends Objeto {
  String tipo;

  Mejora(float x, float y) {
    super(x, y);
    this.velocidad = 2;
    // Por ahora solo un tipo, pero expandible
    this.tipo = "SPEED";
  }

  void actualizar() {
    y += velocidad;
    if (y > height) vivo = false;
  }

  void dibujar() {
    fill(0, 255, 0);
    ellipse(x, y, 15, 15);
    fill(0);
    textSize(10);
    textAlign(CENTER, CENTER);
    text("P", x, y);
  }

  String getTipo() { return tipo; }
}
