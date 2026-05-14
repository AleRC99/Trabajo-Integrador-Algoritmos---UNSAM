class GestorSesionJuego {
  /*
  Estadísticas de la Sesión en el módulo.
  */
  int SESION_tiempoJugado;
  int SESION_enemigosDestruidos;
  int SESION_partidasJugadas;
  int SESION_partidasGanadas;
  int SESION_partidasPerdidas;
  int SESION_puntajeTotal;
  
  /* 
  Estadísticas de una única partida
  */
  int PARTIDA_tiempoJugado = 0;
  int PARTIDA_enemigosDestruidos = 0;
  int PARTIDA_puntajeTotal = 0;
  
   // Puntaje Histórico del Modulo
   int PUNTAJE_HISTORICO;
   
   // Estado de la sesión
   boolean estadoSesion;
   
  public GestorSesionJuego(int PuntajeHistorico) {
    this.PUNTAJE_HISTORICO = PuntajeHistorico;
  }
  
  public void iniciarSesion() {
  // ESTADISTICAS DE SESION
    this.SESION_tiempoJugado = 0;
    this.SESION_enemigosDestruidos = 0;
    this.SESION_partidasJugadas = 0;
    this.SESION_partidasGanadas = 0;
    this.SESION_partidasPerdidas = 0;
    this.SESION_puntajeTotal = 0;
   // ESTADO DE SESION
     this.estadoSesion = true;
   }
  
  
  public void iniciarPartida() {
    this.PARTIDA_tiempoJugado = 0;
    this.PARTIDA_enemigosDestruidos = 0;
    this.PARTIDA_puntajeTotal = 0;
}

  public void actualizarEstadistica() {}
  
  public void finalizarPartida() {}
  
  // Este método debería retornar una clase EstadisticasGenerales (que aún no ha sido implementada)
  public void cerrarSesion() {};
