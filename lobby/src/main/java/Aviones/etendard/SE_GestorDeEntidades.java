package Aviones.etendard;

import java.util.ArrayList;

public class SE_GestorDeEntidades {
  public SE_GestorPrincipal gp;
  private ArrayList<SE_Nave>     listaNaves;
  private ArrayList<SE_Enemigo>  listaEnemigos;
  private ArrayList<SE_Proyectil> listaProyectiles;
  // private ArrayList<SE_Mejora>   listaMejoras;
  private ArrayList<SE_Enemigo>  escuadronRojoActivo;

  public SE_GestorDeEntidades(SE_GestorPrincipal gp) {
    this.gp = gp;
    listaNaves        = new ArrayList<SE_Nave>();
    listaProyectiles  = new ArrayList<SE_Proyectil>();
    // listaMejoras      = new ArrayList<SE_Mejora>();
    listaEnemigos     = new ArrayList<SE_Enemigo>();
    escuadronRojoActivo = new ArrayList<SE_Enemigo>();
  }

  public void vaciarTodo() {
    listaNaves.clear();
    listaProyectiles.clear();
    // listaMejoras.clear();
    listaEnemigos.clear();
    escuadronRojoActivo.clear();
  }

  public void agregarNave(SE_Nave n)        { listaNaves.add(n); }
  public void agregarEnemigo(SE_Enemigo e)  { listaEnemigos.add(e); }
  public void agregarProyectil(SE_Proyectil p) { listaProyectiles.add(p); }
  public void agregarEscuadronRojo(SE_Enemigo e) { escuadronRojoActivo.add(e); listaEnemigos.add(e); }
  public void vaciarEnemigos() { listaEnemigos.clear(); escuadronRojoActivo.clear(); }
  
  public ArrayList<SE_Nave>    getNaves()    { return listaNaves; }
  public boolean hayEscuadronRojo()       { return !escuadronRojoActivo.isEmpty(); }
  public int cantEnemigos()               { return listaEnemigos.size(); }
  
  public SE_Nave getPrimeraNaveViva() {
    for (SE_Nave n : listaNaves) if (n.isVivo()) return n;
    return null;
  }
  public boolean hayNavesVivas() {
    for (SE_Nave n : listaNaves) if (n.isVivo()) return true;
    return false;
  }

  public void procesar(boolean pausado) {
    revisarEscapesRojos();

    for (int i = listaEnemigos.size() - 1; i >= 0; i--) {
      SE_Enemigo e = listaEnemigos.get(i);
      if (!pausado) e.actualizar();
      e.dibujar();
      
      if (!pausado) e.disparar();

      if (e.colisionaConNave()) {
        for (SE_Nave n : listaNaves) {
          if (n.isVivo() && hayColision(e, n)) {
            n.morir();
            e.recibirDano();
          }
        }
      }
      
      for (int j = listaProyectiles.size() - 1; j >= 0; j--) {
        SE_Proyectil p = listaProyectiles.get(j);
        if (!p.esAliado) continue; 
        boolean hit = hayColision(e, p);
        if (hit) {
          e.recibirDano();
          listaProyectiles.remove(j);
          if (!e.isVivo()) manejarMuerteEnemigo(e);
          break; 
        }
      }
      
      if (!e.isVivo()) listaEnemigos.remove(i);
    }
    
    for (int i = listaProyectiles.size() - 1; i >= 0; i--) {
      SE_Proyectil p = listaProyectiles.get(i);
      if (!pausado) p.actualizar();
      p.dibujar();
      
      if (!p.esAliado) {
        for (SE_Nave n : listaNaves) {
          if (n.isVivo() && hayColision(p, n)) {
            n.morir();
            p.morir();
          }
        }
      }
      if (!p.isVivo()) listaProyectiles.remove(i);
    }

/*
    for (int i = listaMejoras.size() - 1; i >= 0; i--) {
      SE_Mejora m = listaMejoras.get(i);
      if (!pausado) m.actualizar();
      m.dibujar();
      for (SE_Nave n : listaNaves) {
        if (n.isVivo() && hayColision(m, n)) {
          n.activarMejora();
          m.morir();
          if (gp != null) gp.addScore(500);
        }
      }
      if (!m.isVivo()) listaMejoras.remove(i);
    }
*/

    for (SE_Nave n : listaNaves) {
      if (n.isVivo()) { 
        if (!pausado) n.actualizar(); 
        n.dibujar(); 
      }
    }
  }

  void manejarMuerteEnemigo(SE_Enemigo e) {
    if (gp == null) return;
    gp.registrarMuerte(e);
    gp.addScore(100 * gp.getDificultad());
    
    if (e.getTipo().equals("AtlanticConveyor")) {
       gp.addScore(2000);
       gp.getNivel().setPausaPeligroTimer(150);
    }
    if (e.getTipo().equals("HmsSheffield")) {
       gp.addScore(50000);
       gp.ganarPartida();
    }
    
    // SE_Mejora m = e.soltarMejora();
    // if (m != null) listaMejoras.add(m);
    
    if (escuadronRojoActivo.contains(e)) {
       escuadronRojoActivo.remove(e);
       if (escuadronRojoActivo.isEmpty()) {
          // listaMejoras.add(new SE_Mejora(this, e.getX(), e.getY(), 1));
          if (gp != null) gp.addScore(500);
       }
    }
  }

  void revisarEscapesRojos() {
    for (int i = escuadronRojoActivo.size() - 1; i >= 0; i--) {
       if (escuadronRojoActivo.get(i).getY() > gp.getApp().height) {
          escuadronRojoActivo.clear();
          break;
       }
    }
  }

  private boolean hayColision(SE_Objeto a, SE_Objeto b) {
    if (a == null || b == null) return false;
    float aLeft = a.getX() - a.getAncho() / 2;
    float aRight = a.getX() + a.getAncho() / 2;
    float aTop = a.getY() - a.getAlto() / 2;
    float aBottom = a.getY() + a.getAlto() / 2;
    
    float bLeft = b.getX() - b.getAncho() / 2;
    float bRight = b.getX() + b.getAncho() / 2;
    float bTop = b.getY() - b.getAlto() / 2;
    float bBottom = b.getY() + b.getAlto() / 2;
    
    return !(aRight < bLeft || aLeft > bRight || aBottom < bTop || aTop > bBottom);
  }
}
