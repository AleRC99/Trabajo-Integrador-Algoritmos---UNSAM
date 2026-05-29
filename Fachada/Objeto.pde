import java.util.ArrayList;

abstract class Objeto {
  // --- POSICIÓN ---
  private float X;
  private float Y;
  // --- ESTADO DEL OBJETO ---
  private boolean vivo;
  
  
  public Objeto(float x_, float y_) {  // CONSTRUCTOR
    this.X = x_;
    this.Y = y_;
    this.vivo = true;
  }
  
  
  public float getX() // getter Posición en X. 
  {
    return this.X;
  }
  
  public float getY() // getter Posición en Y.
  {
    return this.Y;
  }
  
  public boolean isVivo() { // getter del Estado del objeto.
    return this.vivo;
  }
  
  public void morir() { // Cambia el estado del objeto 
    this.vivo = false;
  }
  
  public void actualizar(float X_, float Y_) { // setter de la posición del objeto
    this.X = X_;
    this.Y = Y_;
  }
}
