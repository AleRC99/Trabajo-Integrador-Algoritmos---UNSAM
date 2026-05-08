class Proyectil extends Objeto {
  
  Proyectil(float x, float y) {
    super(x, y);
    this.velocidad = 7;
  }

  @Override
  void actualizar() {
    y -= velocidad;
    if (y < 0) vivo = false;
  }

  @Override
  void dibujar() {
    fill(255, 255, 0);
    rect(x, y, 5, 15);
  }
}
