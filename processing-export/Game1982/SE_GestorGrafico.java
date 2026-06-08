import processing.core.*;
import java.util.HashMap;

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
    cargarImagen("Nave", "Super Etendard.png", 40, 0);
    cargarImagen("SeaHarrier", "Sea Harrier FRS.1.png", 40, 0);
    cargarImagen("WestlandLynx", "Westland Lynx.png", 40, 0);
    cargarImagen("SeaKing", "Sea King HAS.5.png", 60, 0);
    cargarImagen("FragataTipo21", "Fragata Tipo 21 Clase Amazon.png", 40, 0);
    cargarImagen("AtlanticConveyor", "Atlantic Conveyor.png", 60, 0);
    cargarImagen("HmsSheffield", "HMS Sheffield.png", 80, 0);

    // Fondos y decoraciones
    // Fondo: resize solo el ancho; Processing mantiene la proporción de alto (imagen 3x pantalla)
    cargarImagen("Fondo", "Fondo.jpg", app.width, 0);
    cargarImagen("Menu", "menu.png", app.width, app.height);
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
    if (img == null)
      return;
    app.pushStyle();
    app.imageMode(PApplet.CENTER);
    app.tint(c);
    app.image(img, px - 2, py);
    app.image(img, px + 2, py);
    app.image(img, px, py - 2);
    app.image(img, px, py + 2);
    app.noTint();
    app.image(img, px, py);
    app.popStyle();
  }

  public void dibujarFondo(int tiempoNivel) {
    PImage fondo = getImagen("Fondo");
    if (fondo == null)
      return;

    // La imagen mide 3x la pantalla: usamos su altura real como período del loop
    int imgH = fondo.height;
    float offset = (tiempoNivel * 1.5f) % imgH;

    // Empezar desde el final de la imagen y avanzar hacia el inicio (scroll hacia abajo)
    // y=0 → muestra el fondo de la imagen; a medida que pasa el tiempo sube hacia el inicio
    float y = -(imgH - app.height) + offset;

    app.pushStyle();
    app.imageMode(PApplet.CORNER);
    app.image(fondo, 0, y);
    // Segunda copia arriba para el loop continuo
    app.image(fondo, 0, y - imgH);
    app.popStyle();
  }

  public void dibujarMenu() {
    PImage imgMenu = getImagen("Menu");
    if (imgMenu != null) {
      app.imageMode(PApplet.CENTER);
      app.image(imgMenu, app.width / 2.0f, app.height / 2.0f);
    } else {
      app.fill(0);
      app.rect(app.width / 2.0f, app.height / 2.0f, app.width, app.height);
    }

    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    float baseY = app.height * 0.8f;

    app.rectMode(PApplet.CENTER);
    app.fill(0, 180);
    app.noStroke();
    app.rect(app.width / 2.0f, baseY + 50, app.width, 180);

    app.fill(255, 50, 50);
    app.textSize(18);
    app.text("PULSA ESPACIO PARA INICIAR", app.width / 2.0f, baseY + 30);

    app.fill(180, 180, 100);
    app.textSize(13);
    app.text("[ESC] o [Q]  Volver al Lobby Principal", app.width / 2.0f, baseY + 60);
  }

  public void dibujarUI(int score) {
    app.fill(255);
    app.textSize(20);
    app.textAlign(PApplet.LEFT, PApplet.TOP);
    app.text("SCORE: " + score, 10, 10);
  }

  public void dibujarPantallaPausa() {
    app.fill(0, 150);
    app.rect(app.width / 2.0f, app.height / 2.0f, app.width, app.height);
    app.fill(255);
    app.textSize(50);
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.text("PAUSA", app.width / 2.0f, app.height / 2.0f - 20);
    app.textSize(20);
    app.text("Presiona 'P' para continuar", app.width / 2.0f, app.height / 2.0f + 30);
  }

  public void dibujarGameOver(String title, String subtitle, int estadoJuego, SE_GestorPrincipal sesion,
      EstadisticasGenerales eg) {
    app.fill(0, 180);
    app.rect(app.width / 2.0f, app.height / 2.0f, app.width, app.height);
    if (estadoJuego == 3)
      app.fill(0, 255, 100);
    else
      app.fill(255, 0, 0);

    app.textSize(50);
    app.textAlign(PApplet.CENTER, PApplet.CENTER);
    app.text(title, app.width / 2.0f, app.height / 2.0f - 150);

    SE_EstadisticasPartida stats = sesion.getHistorial().getPartidaActual();
    int score = stats != null ? stats.getScore() : 0;
    int destruidos = stats != null ? stats.getTotalEnemigosDestruidos() : 0;

    app.fill(255);
    app.textSize(20);
    app.text("Puntaje Partida: " + score, app.width / 2.0f, app.height / 2.0f - 90);
    app.text("Enemigos Destruidos: " + destruidos, app.width / 2.0f, app.height / 2.0f - 60);

    app.textSize(15);
    int yOff = app.height / 2 - 20;
    if (stats != null && stats.getDetalleEnemigos() != null) {
      for (String key : stats.getDetalleEnemigos().keyArray()) {
        app.text(key + ": " + stats.getDetalleEnemigos().get(key), app.width / 2.0f, yOff);
        yOff += 20;
      }
    }

    app.fill(255, 255, 0);
    app.textSize(18);
    app.text(subtitle, app.width / 2.0f, app.height - 60);

    app.fill(200);
    app.textSize(14);
    app.text("Mejor Puntaje: " + eg.getPuntajeTotal() + " | Ganadas: " + eg.getPartidasGanadas() + " | Enemigos: "
        + eg.getEnemigosDestruidos(), app.width / 2.0f, app.height - 30);
  }

  /**
   * Despachador de Dibujo y Físicas.
   * Renderiza la pantalla correspondiente según el estado del contrato de juego
   * y avanza la actualización del nivel y procesamiento de entidades de forma
   * síncrona.
   */
  public void renderizarSegunEstado(EstadoJuego estado) {
    // Limpiar el canvas antes de cada frame para evitar que se acumulen
    app.background(0);

    String nombre = estado.getNombre();
    switch (nombre) {
      case "INICIANDO":
        dibujarFondo(0);
        dibujarMenu();
        break;

      case "EN_EJECUCION":
        dibujarFondo(gp.getNivel().getTiempoNivel());

        boolean naveViva = gp.entidades.hayNavesVivas();
        boolean jefeDerrotado = gp.getNivel().isBossSpawned() && gp.entidades.cantEnemigos() == 0;

        if (naveViva && !jefeDerrotado) {
          gp.getNivel().actualizar(gp, gp.entidades);
          gp.entidades.procesar(false);
        } else {
          gp.entidades.procesar(true); // Pausar entidades
          app.textAlign(PApplet.CENTER, PApplet.CENTER);
          if (!naveViva) {
            app.fill(255, 50, 50);
            app.textSize(40);
            app.text("GAME OVER", app.width / 2.0f, app.height / 2.0f - 20);
            app.textSize(20);
            app.fill(255);
            app.text("Presiona Q para salir", app.width / 2.0f, app.height / 2.0f + 30);
          } else if (jefeDerrotado) {
            app.fill(50, 255, 50);
            app.textSize(40);
            app.text("VICTORIA", app.width / 2.0f, app.height / 2.0f - 20);
            app.textSize(20);
            app.fill(255);
            app.text("Presiona Q para salir", app.width / 2.0f, app.height / 2.0f + 30);

            if (!gp.victoriaRegistrada) {
              gp.registrarVictoria();
            }
          }
        }

        SE_EstadisticasPartida p1 = gp.getHistorial().getPartidaActual();
        dibujarUI(p1 != null ? p1.getScore() : 0);
        break;

      case "PAUSADO":
        dibujarFondo(gp.getNivel().getTiempoNivel());
        gp.entidades.procesar(true);
        SE_EstadisticasPartida p2 = gp.getHistorial().getPartidaActual();
        dibujarUI(p2 != null ? p2.getScore() : 0);
        dibujarPantallaPausa();
        break;

      case "FINALIZADO":
        dibujarFondo(gp.getNivel().getTiempoNivel());
        boolean gano = (gp.getHistorial().getPartidaActual() != null
            && gp.getHistorial().getPartidaActual().isVictoria());
        int est = gano ? SE_GestorPrincipal.ESTADO_WIN : SE_GestorPrincipal.ESTADO_GAMEOVER;
        String tit = gano ? "STAGE CLEAR!" : "GAME OVER";
        String sub = gano ? "MISION CUMPLIDA - Presiona 'R'" : "Presiona 'R' para volver al MENU";

        dibujarGameOver(tit, sub, est, gp, gp.statsGenerales);
        break;
    }
  }
}