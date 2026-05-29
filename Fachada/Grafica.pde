import java.util.ArrayList;

class Grafica {  
  PApplet sketch; // Objeto necesario para trabajar con las funcionalidades gráficas de processing. (Naturalmente, para las funciones del sketch principal)
  
  // CONSTRUCTOR
  public Grafica(PApplet app) {
    this.sketch = app; // Sketch Principal
  }
  
  // ----- DIBUJAR TODAS LAS ENTIDADES -----
  public void dibujarEntidades(ArrayList<Objeto> listaEntidades) { 
  for (Objeto n_obj: listaEntidades) { // Itera en cada una de las entidades del ArrayList
    this.sketch.fill(120, 120, 255); // Fija el color de la figura.
    this.sketch.ellipse(n_obj.getX(), n_obj.getY(), 20, 20); // Imprime una elipse de 20px*20px
    }
  }
  
  public void dibujarFondo() {
    // Este versión no cuenta con un fondo dinámico.
    this.sketch.background(0); // Dibuja un fondo negro.
  }

}
