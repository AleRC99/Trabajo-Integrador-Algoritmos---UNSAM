import java.utils.ListArray;

abstract class Objeto {
  private int X;
  private int Y;
  private boolean vivo;
  
  public Objeto(int x_, int y_) {
    this.X = x_;
    this.Y = y_;
    this.vivo = true;
  }
  
  public int getX() {
    return this.X;
  }
  
  public int getY() {
    return this.Y;
  }
  
  public Array getPosicion() {
    return new Array(this.X, this.Y);
  }
  
  public boolean isVivo() {
    return this.vivo;
  }
  
  public void morir() {
    this.vivo = false;
  }
  
  public void actualizar(int X_, int Y_) {
    this.X = X_;
    this.Y = Y_;
  }
}
