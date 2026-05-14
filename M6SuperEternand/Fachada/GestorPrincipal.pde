class GestorPrincipal {
  String nombreModulo = "";
  String descripcion = "";
  String nombreAvion = "";
  
  public GestorPrincipal() 
  {
  }

  public String getNombreModulo() 
  {
    return this.nombreModulo;
  }
  
  public String getDescripcion() 
  {
    return this.descripcion;
  }
  
  public String getNombreAvion() {
    return this.nombreAvion;
  }
  
  public void inicializarContexto() {}
  
  public void iniciar() {}
  
  public void reanudar() {}
  
  public void finalizar() {}
  
  public void getEstado() {}
  
  public void getEstadisticasGenerales() {}
}
