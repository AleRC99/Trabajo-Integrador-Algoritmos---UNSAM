class Eternand extends Objeto {
  
  // Constructor
  public Eternand(float X_init, float Y_init) {
    super(X_init, Y_init);
    
  }

  // ----- MÉTODO PARA ACTUALIZAR LA POSICIÓN -----
  public void actualizarPosicion(boolean[] listaTeclas_) {
    /* Lista con índices de "W", "A", "S" y "D", respectivamente.
    "W" = 87; "A" = 65; "S" = 83; "D" = 68.
    */
    
    // ------- ARRIBA / ABAJO ---------
    if (listaTeclas_[UP] || listaTeclas_[87]) {this.actualizar(this.getX(), this.getY() - 1 );} //Movimiento hacia arriba
    if (listaTeclas_[DOWN] || listaTeclas_[83]) {this.actualizar(this.getX(), this.getY() + 1);} //Movimiento hacia abajo
    
    // ---------- IZQUIERDA / DERECHA ----------
    if (listaTeclas_[RIGHT] || listaTeclas_[68]) {this.actualizar(this.getX() + 1, this.getY());} //Movimiento hacia arriba
    if (listaTeclas_[LEFT] || listaTeclas_[65]) {this.actualizar(this.getX() - 1, this.getY());} //Movimiento hacia arriba
  }
  

}
