package Aviones.etendard;

import processing.core.*;
import java.util.HashMap;
import Contrato.EstadoJuego;

public class SE_GestorGrafico {
  private SE_GestorPrincipal gp;
  private PApplet app;
  
  HashMap<String, PImage> imagenes = new HashMap<String, PImage>();
  
  public SE_GestorGrafico(SE_GestorPrincipal gp) {
    this.gp = gp;
    this.app = gp.getApp();
  }

  public void cargarRecursos() {
    // Sprites de naves y enemigos
    cargarImagen("Nave", "Imagenes/Super Etendard.png", 40, 0);
    cargarImagen("SeaHarrier", "Imagenes/Sea Harrier FRS.1.png", 40, 0);
    cargarImagen("WestlandLynx", "Imagenes/Westland Lynx.png", 40, 0);
    cargarImagen("SeaKing", "Imagenes/Sea King HAS.5.png", 60, 0);
    cargarImagen("FragataTipo21", "Imagenes/Fragata Tipo 21 Clase Amazon.png", 40, 0);
    cargarImagen("AtlanticConveyor", "Imagenes/Atlantic Conveyor.png", 60, 0);
    cargarImagen("HmsSheffield", "Imagenes/HMS Sheffield.png", 80, 0);

    // Fondos y decoraciones
    cargarImagen("Fondo0", "Imagenes/Water.png", app.width, app.height);
    cargarImagen("Fondo2", "Imagenes/Costa.png", app.width, app.height);
    cargarImagen("Fondo3", "Imagenes/Costa2.png", app.width, app.height);
    cargarImagen("Menu", "Imagenes/menu.png", app.width, app.height);
  }

  public void cargarImagen(String id, String path, int resizeW, int resizeH) {
    if (!imagenes.containsKey(id)) {
      PImage img = app.loadImage(path);
      if (img != null) {
        if (resizeW != 0 || resizeH != 0) {
          img.resize(resizeW, resizeH);
        }
        imagenes.put(id, img);
      } else {
        System.out.println("[GestorGrafico] Error: no se encontró " + path);
      }
    }
  }

  public PImage getImagen(String id) {
    return imagenes.get(id);
  }
  
  public void dibujarConBorde(PImage img, float px, float py, int c) {
    if (img == null) return;
    app.pushStyle();
    app.imageMode(PApplet.CENTER);
    app.tint(c);
    app.image(img, px - 2, py); app.image(img, px + 2, py);
    app.image(img, px, py - 2); app.image(img, px, py + 2);
    app.noTint();
    app.image(img, px, py);
    app.popStyle();
  }

  public void dibujarFondo(int tiempoNivel, int selNivel, int faseActual) {
    float scroll = tiempoNivel * 1.5f;
    int firstTileIndex = (int)(scroll / app.height);
    float firstTileY = scroll - firstTileIndex * app.height;
    
    app.pushStyle();
    app.imageMode(PApplet.CORNER);
    
    PImage tile1 = getTileImage(firstTileIndex);
    PImage tile2 = getTileImage(firstTileIndex + 1);
    
    if (tile1 != null) app.image(tile1, 0, firstTileY);
    if (tile2 != null) app.image(tile2, 0, firstTileY - app.height);
    
    app.popStyle();
  }

  public PImage getTileImage(int index) {
    if (index == 0 || index == 1) return getImagen("Fondo0");
    if (index == 2) return getImagen("Fondo2");
    if (index == 3) return getImagen("Fondo3");
    return getImagen("Fondo0");
  }

  public void dibujarMenu(int selNivel) {
    PImage imgMenu = getImagen("Menu");
    if (imgMenu != null) {
      app.imageMode(PApplet.CENTER);
      app.image(imgMenu, app.width/2.0f, app.height/2.0f);
    } else {
      app.fill(0);
      app.rect(app.width/2.0f, app.height/2.0f, app.width, app.height);
    }
    
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    float baseY = app.height * 0.8f;
    
    app.rectMode(PApplet.CENTER);
    app.fill(0, 180);
    app.noStroke();
    app.rect(app.width/2.0f, baseY + 50, app.width, 180);
    
    app.textSize(16);
    app.fill(255);
    app.text("DIFICULTAD", app.width/2.0f, baseY);
    
    app.fill(selNivel == 2 ? app.color(0, 255, 255) : 150);
    app.text("[W] INTERMEDIO", app.width/2.0f, baseY + 30);
    
    app.fill(selNivel == 3 ? app.color(0, 255, 255) : 150);
    app.text("[E] DIFICIL", app.width/2.0f, baseY + 60);
    
    app.fill(255, 50, 50);
    app.textSize(18);
    app.text("PULSA ESPACIO PARA INICIAR", app.width/2.0f, baseY + 95);
    
    app.fill(180, 180, 100);
    app.textSize(13);
    app.text("[ESC] o [Q]  Volver al Lobby Principal", app.width/2.0f, baseY + 125);
  }

  public void dibujarUI(int score, int faseActual) {
    app.fill(255);
    app.textSize(20);
    app.textAlign(PApplet.LEFT, PApplet.TOP);
    app.text("SCORE: " + score, 10, 10);
    app.textAlign(PApplet.RIGHT, PApplet.TOP);
    app.text("PHASE: " + faseActual, app.width - 10, 10);
    
    if (gp != null && gp.entidades != null) {
      SE_Nave n = (SE_Nave) gp.entidades.getPrimeraNaveViva();
      if (n != null && n.hasDoubleShot()) {
        app.textAlign(PApplet.CENTER, PApplet.TOP);
        app.fill(150, 255, 150);
        app.text("POWER UP!", app.width / 2.0f, 10);
      }
    }
  }

  public void dibujarPantallaPausa() {
    app.fill(0, 150);
    app.rect(app.width/2.0f, app.height/2.0f, app.width, app.height);
    app.fill(255);
    app.textSize(50);
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.text("PAUSA", app.width/2.0f, app.height/2.0f - 20);
    app.textSize(20);
    app.text("Presiona 'P' para continuar", app.width/2.0f, app.height/2.0f + 30);
  }

  public void dibujarGameOver(String title, String subtitle, int estadoJuego, SE_GestorPrincipal sesion, Home.EstadisticasGenerales eg) {
    app.fill(0, 180);
    app.rect(app.width/2.0f, app.height/2.0f, app.width, app.height);
    if (estadoJuego == 3) app.fill(0, 255, 100);
    else app.fill(255, 0, 0);
    
    app.textSize(50);
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.text(title, app.width/2.0f, app.height/2.0f - 150);
    
    app.fill(255);
    app.textSize(20);
    app.text("Puntaje Partida: " + sesion.getScore(), app.width/2.0f, app.height/2.0f - 90);
    app.text("Enemigos Destruidos: " + sesion.getTotalEnemigosDestruidos(), app.width/2.0f, app.height/2.0f - 60);
    
    app.textSize(15);
    int yOff = app.height/2 - 20;
    for (String key : sesion.getDetalleEnemigos().keyArray()) {
      app.text(key + ": " + sesion.getDetalleEnemigos().get(key), app.width/2.0f, yOff);
      yOff += 20;
    }
    
    app.fill(255, 255, 0);
    app.textSize(18);
    app.text(subtitle, app.width/2.0f, app.height - 60);
    
    app.fill(200);
    app.textSize(14);
    app.text("Mejor Puntaje: " + eg.getPuntajeTotal() + " | Ganadas: " + eg.getPartidasGanadas() + " | Enemigos: " + eg.getEnemigosDestruidos(), app.width/2.0f, app.height - 30);
  }

  /**
   * Despachador de Dibujo y Físicas.
   * Renderiza la pantalla correspondiente según el estado del contrato de juego
   * y avanza la actualización del nivel y procesamiento de entidades de forma síncrona.
   */
  public void renderizarSegunEstado(EstadoJuego estado) {
    String nombre = estado.getNombre();
    switch (nombre) {
      case "NO_INICIADO":
        dibujarFondo(0, gp.getDificultad(), SE_GestorPrincipal.FASE_INICIO);
        dibujarMenu(gp.getDificultad());
        break;

      case "EN_EJECUCION":
        dibujarFondo(gp.getNivel().getTiempoNivel(), gp.getDificultad(), gp.getNivel().getFaseActual());
        gp.getNivel().actualizar(gp, gp.entidades);
        gp.entidades.procesar(false);
        dibujarUI(gp.getScore(), gp.getNivel().getFaseActual());
        
        if (!gp.entidades.hayNavesVivas()) gp.perderPartida();
        if (gp.getNivel().isBossSpawned() && gp.entidades.cantEnemigos() == 0) gp.ganarPartida();
        break;

      case "PAUSADO":
        dibujarFondo(gp.getNivel().getTiempoNivel(), gp.getDificultad(), gp.getNivel().getFaseActual());
        gp.entidades.procesar(true); // true = congelado
        dibujarUI(gp.getScore(), gp.getNivel().getFaseActual());
        dibujarPantallaPausa();
        break;

      case "FINALIZADO":
        dibujarFondo(gp.getNivel().getTiempoNivel(), gp.getDificultad(), gp.getNivel().getFaseActual());
        boolean gano = (gp.getHistorial().getPartidaActual() != null && gp.getHistorial().getPartidaActual().isVictoria());
        int est = gano ? SE_GestorPrincipal.ESTADO_WIN : SE_GestorPrincipal.ESTADO_GAMEOVER;
        String tit = gano ? "STAGE CLEAR!" : "GAME OVER";
        String sub = gano ? "MISION CUMPLIDA - Presiona 'R'" : "Presiona 'R' para volver al MENU";
        
        dibujarGameOver(tit, sub, est, gp, gp.statsGenerales);
        break;
    }
  }
}
