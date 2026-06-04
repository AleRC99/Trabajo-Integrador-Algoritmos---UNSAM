import java.util.ArrayList;

public class SE_GestorDeEntidades {
  public SE_GestorPrincipal gp;
  private ArrayList<SE_Nave>     listaNaves;
  private ArrayList<SE_Enemigo>  listaEnemigos;
  private ArrayList<SE_Proyectil> listaProyectiles;

  public SE_GestorDeEntidades(SE_GestorPrincipal gp) {
    this.gp = gp;
    listaNaves        = new ArrayList<SE_Nave>();
    listaProyectiles  = new ArrayList<SE_Proyectil>();
    listaEnemigos     = new ArrayList<SE_Enemigo>();
  }

  public void vaciarTodo() {
    listaNaves.clear();
    listaProyectiles.clear();
    listaEnemigos.clear();
  }

  public void agregarNave(SE_Nave n)        { listaNaves.add(n); }
  public void agregarEnemigo(SE_Enemigo e)  { listaEnemigos.add(e); }
  public void agregarProyectil(SE_Proyectil p) { listaProyectiles.add(p); }
  public void vaciarEnemigos() { listaEnemigos.clear(); }
  
  public ArrayList<SE_Nave>    getNaves()    { return listaNaves; }
  public int cantEnemigos()               { return listaEnemigos.size(); }
  
  public SE_Nave getPrimeraNaveViva() {
    for (SE_Nave n : listaNaves) if (n.vivo) return n;
    return null;
  }

  public boolean hayNavesVivas() {
    for (SE_Nave n : listaNaves) if (n.vivo) return true;
    return false;
  }

  public void procesar(boolean pausado) {
    for (int i = listaEnemigos.size() - 1; i >= 0; i--) {
      SE_Enemigo e = listaEnemigos.get(i);
      if (!pausado) {
          e.actualizar();
          // e.disparar();
      }
      e.dibujar();
      
      // if (e.colisionaConNave()) {
      //   for (SE_Nave n : listaNaves) {
      //     if (n.vivo && hayColision(e, n)) {
      //       n.morir();
      //       e.morir();
      //     }
      //   }
      // }
      
      for (int j = listaProyectiles.size() - 1; j >= 0; j--) {
        SE_Proyectil p = listaProyectiles.get(j);
        if (!p.esAliado) continue; 
        boolean hit = hayColision(e, p);
        if (hit) {
          e.morir();
          listaProyectiles.remove(j);
          if (!e.vivo) manejarMuerteEnemigo(e);
          break; 
        }
      }
      
      if (!e.vivo) listaEnemigos.remove(i);
    }
    
    for (int i = listaProyectiles.size() - 1; i >= 0; i--) {
      SE_Proyectil p = listaProyectiles.get(i);
      if (!pausado) p.actualizar();
      p.dibujar();
      
      if (!p.esAliado) {
        for (SE_Nave n : listaNaves) {
          if (n.vivo && hayColision(p, n)) {
            n.morir();
            p.morir();
          }
        }
      }
      if (!p.vivo) listaProyectiles.remove(i);
    }

    for (SE_Nave n : listaNaves) {
      if (n.vivo) { 
        if (!pausado) n.actualizar(); 
        n.dibujar(); 
      }
    }
  }

  void manejarMuerteEnemigo(SE_Enemigo e) {
    if (gp == null) return;
    SE_EstadisticasPartida stats = gp.getHistorial().getPartidaActual();
    if (stats != null) {
        stats.registrarMuerte(e);
        stats.addScore(e.puntajeAlMorir);
    }
    
    if (e.isBoss) {
       gp.registrarVictoria();
    }
  }

  private boolean hayColision(SE_Objeto a, SE_Objeto b) {
    if (a == null || b == null) return false;
    float aLeft = a.x - a.ancho / 2;
    float aRight = a.x + a.ancho / 2;
    float aTop = a.y - a.alto / 2;
    float aBottom = a.y + a.alto / 2;
    
    float bLeft = b.x - b.ancho / 2;
    float bRight = b.x + b.ancho / 2;
    float bTop = b.y - b.alto / 2;
    float bBottom = b.y + b.alto / 2;
    
    return !(aRight < bLeft || aLeft > bRight || aBottom < bTop || aTop > bBottom);
  }
}