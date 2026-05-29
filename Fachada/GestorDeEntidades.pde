import java.util.ArrayList;

class GestorDeEntidades {
   ArrayList<Objeto> listaAliados = new ArrayList<>(); // LISTA DE ALIADOS
   Eternand p1; // AVIÓN ETERNAND DEL JUGADOR
   
   public GestorDeEntidades(int height_, int width_) { // Necesita las dimensiones de la pantalla
   iniciarEternand(width_, height_); // Inicia el objeto gráfico de Eternand
   }
   
   public void iniciarEternand(int alturaPantalla, int anchoPantalla) { 
   Eternand player1 = new Eternand(0.5 * anchoPantalla, 0.9 * alturaPantalla); // Lo inicia en el centro inferior de la pantalla.
   listaAliados.add(player1); // Lo añade a LISTA DE ALIADOS
   p1 = player1; // Almacena el objeto Eternand
   }
      
   public ArrayList getListaAliados() // Retorna la LISTA DE ALIADOS
     {
       return this.listaAliados;
     } 
    
   public Eternand getEternand() { // Retorna el objeto ETERNAND que maneja el jugador
     return this.p1;
   }
  

}
