import java.util.ListArray;

class UserEvents {
  ListArray<String> listaTeclasAccionables = new ListArray<>("UP", "DOWN", "RIGHT", "LEFT", "SPACE", "ESCAPE");
 
 public UserEvents() {}
 
 public void keyPressed() {}
 
 public void keyReleased() {}
 
 public boolean getEvento() {
   return true; // Se estableció este valor predeterminado para evitar errores en el compilador Java
     }
}
