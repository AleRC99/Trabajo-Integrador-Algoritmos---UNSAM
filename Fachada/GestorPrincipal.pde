class GestorPrincipal {
  private PApplet app; // Sketch principal
  private GestorDeEntidades ge; // Gestor de Entidades
  private UserEvents events; // Gestor de Eventos de Teclado
  private Grafica grafica; // Graficador
  private Eternand eternand; // Jugador
  
  public GestorPrincipal(PApplet app) 
  { 
    this.app = app; // Sketch Principal
    
    ge = new GestorDeEntidades(this.app.height, this.app.width); // Inicia el Gestor de Entidades (con dimensiones de la pantalla)
    events = new UserEvents(); // Inicia el gestor de eventos
    grafica = new Grafica(this.app); // Inicia Gráfica (con acceso a funciones del sketch principal)
    
    eternand = ge.getEternand(); // Almacena el Avión ETERNAND del jugador.
  }
  
  public void actualizarFrame() { // Realiza las acciones que deben realizarse frame a frame
   
   boolean[] listaEventos = this.events.getEventos(); // Obtenes la lista de teclas presionadas ↓↓↓
   
   eternand.actualizarPosicion(listaEventos);         // → Actualizas la posición del jugador   ←←←
   
   grafica.dibujarFondo();     // Dibuja el fondo
   grafica.dibujarEntidades(ge.getListaAliados()); // Dibuja las entidades
  }
  
  // Provisoriamente: --- RESUELVE LOS PROBLEMAS DE GESTIÓN DE EVENTOS DE TECLADO---
  public UserEvents accesoEvents() {return this.events;}
}
