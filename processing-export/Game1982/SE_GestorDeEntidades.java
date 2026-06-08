import java.util.ArrayList;

public class SE_GestorDeEntidades {
  public SE_GestorPrincipal gp;
  private ArrayList<SE_Nave>     listaNaves;
  private ArrayList<SE_Enemigo>  listaEnemigos;
  private ArrayList<SE_Explosion> listaExplosiones; // explosion effects
  private ArrayList<SE_Proyectil> listaProyectiles;

  public SE_GestorDeEntidades(SE_GestorPrincipal gp) {
    this.gp = gp;
    listaNaves        = new ArrayList<SE_Nave>();
    listaProyectiles  = new ArrayList<SE_Proyectil>();
    listaEnemigos     = new ArrayList<SE_Enemigo>();
    listaExplosiones = new ArrayList<SE_Explosion>();
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
      
      if (e.puedeColisionarConNave()) {
        for (SE_Nave n : listaNaves) {
            if (n.vivo && hayColision(e, n)) {
              n.recibirDaño(1);
              e.recibirDaño(1);
            }
        }
      }
      
      // Control de salida de pantalla y derrota por escape del jefe
      if (!pausado) {
        processing.core.PApplet app = gp.getApp();
        if (!e.isBoss) {
          if (e.y > app.height + 100) {
            e.morir();
          }
        } else {
          if (e.y > app.height + e.alto / 2.0f) {
            for (SE_Nave n : listaNaves) {
              n.morir();
            }
            e.morir();
          }
        }
      }
      
      for (int j = listaProyectiles.size() - 1; j >= 0; j--) {
        SE_Proyectil p = listaProyectiles.get(j);
        if (!p.esAliado) continue; 
        boolean hit = hayColision(e, p);
          if (hit) {
            e.recibirDaño(1);
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
              n.recibirDaño(1);
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
      // Update and draw explosions
    if (listaExplosiones != null) {
        for (int i = listaExplosiones.size() - 1; i >= 0; i--) {
            SE_Explosion exp = listaExplosiones.get(i);
            exp.actualizar();
            exp.dibujar();
            if (!exp.estaViva()) {
                listaExplosiones.remove(i);
            }
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

    // Create an explosion effect at given coordinates
    public void crearExplosion(float x, float y) {
        if (listaExplosiones == null) {
            listaExplosiones = new ArrayList<SE_Explosion>();
        }
        listaExplosiones.add(new SE_Explosion(this, x, y));
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