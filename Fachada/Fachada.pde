GestorPrincipal gp;

void setup() {
  size(600, 600);
  background(250);
  gp = new GestorPrincipal(this);
}

void draw() {
  gp.actualizarFrame(); 
}



void keyPressed() {
  boolean[] UI = gp.accesoEvents().getEventos();
  
  boolean[] lista_teclas = UI;
  
  if (keyCode < 256) {lista_teclas[keyCode] = true;}
  
  gp.accesoEvents().setEventos(lista_teclas);
}

void keyReleased() {
  boolean[] UI = gp.accesoEvents().getEventos();
  
  boolean[] lista_teclas = UI;
  
  if (keyCode < 256) {lista_teclas[keyCode] = false;}
  
  gp.accesoEvents().setEventos(lista_teclas);
}
