import java.util.ListArray;

class GestorDeEntidades {
   ListArray<Objeto> listaAliados = new ListArray<>();
   
   public GestorDeEntidades() {}
   
   public void iniciarEternand(int alturaPantalla, int anchoPantalla) {
   Eternand player1 = new Eternand(0.5 * anchoPantalla, 1.1 * alturaPantalla);
   listaAliados.add(player1);
   }
   
   public void iniciarOleada() {}
   
   public void invocarJefe() {}
   
   public void disparoAliado() {}
   
   public void disparoEnemigo() {}
   
   public void actualizarColisionesAliadas() {}
   
   public void actualizarColisionesEnemigas() {}
   
   public ListArray getListaAliados() 
     {
       return this.listaAliados;
     } 
}
