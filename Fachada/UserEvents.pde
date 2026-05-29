import java.util.ArrayList;


class UserEvents {
  boolean[] ListaTeclas = new boolean[256]; // Array de booleanos \\ Representa los estados si una tecla está presionada.
 
 // CONSTRUCTOR
 public UserEvents() {
 }
 
 // Método provisorio para test.
 public boolean[] getEventos() {
   return ListaTeclas; // Retorna el array de boolean's del estado de las teclas.
     }
 // Método provisorio para test.    
 public void setEventos(boolean[] new_lista) {
    this.ListaTeclas = new_lista; // Actualiza el array de booleanos de los estados de las teclas.
 }
}
