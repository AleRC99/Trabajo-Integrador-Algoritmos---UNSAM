class Eternand extends Objeto {
  boolean mejoraActiva;
  
  
  public Eternand(int X_init, int Y_init) {
    super(X_init, Y_init);
    this.mejoraActiva = false;
  }
  
  public void disparar() {
  
  }
  
  public void mejorar() {
   this. mejoraActiva = true;
  }
  
  public void desactivarMejora() {
     this.mejoraActiva = false; 
  }
  
  private record Posicion(int x, int y) {}
  
  public Posicion getPosicion() {
    return new Posicion(this.X, this.Y);
  }
  
  public void actualizarPosicion(int sumX, int sumY) {
    this.X += sumX;
    this.Y += sumY;
  }
  

}
