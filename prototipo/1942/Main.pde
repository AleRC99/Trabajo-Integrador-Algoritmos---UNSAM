final int ESTADO_MENU = 0;
final int ESTADO_JUEGO = 1;
final int ESTADO_GAMEOVER = 2;
final int ESTADO_WIN = 3;
int estadoJuego = ESTADO_MENU;

int selCantJugadores = 1;
int selNivel = 1;
int selTipoAvionP1 = 1;
int selTipoAvionP2 = 2;

ArrayList<Nave> listaNaves;
ArrayList<Enemigo> listaEnemigos;
ArrayList<Proyectil> listaProyectiles;
ArrayList<ProyectilEnemigo> disparosEnemigos;
ArrayList<Mejora> listaMejoras;
ArrayList<Enemigo> escuadronRojoActivo;

int score = 0;
int tiempoNivel = 0;
int pausaPeligroTimer = 0; // Pausa táctica post mini-boss
boolean bossSpawned = false;

final int FASE_INICIO = 1;
final int FASE_MEDIO = 2;
final int FASE_FINAL = 3;
int faseActual = FASE_INICIO;

boolean firingP1 = false;
boolean firingP2 = false;
int cooldownP1 = 0;
int cooldownP2 = 0;

void setup() {
  size(600, 600);
  ellipseMode(CENTER);
  rectMode(CENTER);
}

void initGame() {
  score = 0;
  tiempoNivel = 0;
  pausaPeligroTimer = 0;
  bossSpawned = false;
  faseActual = FASE_INICIO;
  
  listaNaves = new ArrayList<Nave>();
  listaProyectiles = new ArrayList<Proyectil>();
  disparosEnemigos = new ArrayList<ProyectilEnemigo>();
  listaMejoras = new ArrayList<Mejora>();
  listaEnemigos = new ArrayList<Enemigo>();
  escuadronRojoActivo = new ArrayList<Enemigo>();
  
  if (selCantJugadores >= 1) listaNaves.add(new Nave(width/2 - 50, 500, 1, selTipoAvionP1));
  if (selCantJugadores == 2) listaNaves.add(new Nave(width/2 + 50, 500, 2, selTipoAvionP2));
  
  estadoJuego = ESTADO_JUEGO;
}

void draw() {
  drawBackgroundParallax();
  
  if (estadoJuego == ESTADO_MENU) {
    dibujarMenu();
  } else if (estadoJuego == ESTADO_JUEGO) {
    manejarProgresionNivel();
    manejarDisparosContínuos();
    actualizarYDibujarEntidades();
    drawUI();
    
    boolean algunaViva = false;
    for (Nave n : listaNaves) if (n.isVivo()) algunaViva = true;
    if (!algunaViva) estadoJuego = ESTADO_GAMEOVER;
    
  } else if (estadoJuego == ESTADO_GAMEOVER) {
    drawGameOver("GAME OVER", "Presiona 'R' para volver al MENÚ");
  } else if (estadoJuego == ESTADO_WIN) {
    drawGameOver("STAGE CLEAR!", "MISIÓN CUMPLIDA - Presiona 'R'");
  }
}

void drawBackgroundParallax() {
  background(15, 45, 90); 
  stroke(30, 80, 150, 100);
  for (int i = 0; i < 30; i++) {
    float x = (i * 85) % width;
    float y = (tiempoNivel * 0.5 + i * 50) % height;
    line(x, y, x + 40, y);
  }
  noStroke();
  
  if (faseActual >= FASE_MEDIO) {
    randomSeed(selNivel * 1000); 
    for(int i = 0; i < 4; i++) {
       float yOff = (tiempoNivel * 0.8 + random(1000)) % (height+800) - 400;
       float xOff = random(width);
       
       fill(30, 80, 40);
       ellipse(xOff, yOff, 180 + random(-50, 50), 120 + random(-20, 20));
       fill(180, 170, 90); 
       ellipse(xOff - 10, yOff - 10, 40, 30);
    }
  }

  fill(255, 255, 255, 50);
  for (int i = 0; i < 6; i++) {
    float nx = ((tiempoNivel * 2.5) + (i*100)) % (width + 200) - 100;
    float ny = ((tiempoNivel * 3) + (i*150)) % height;
    ellipse(nx, ny, 150, 70);
  }

  if (faseActual == FASE_FINAL) {
     if (selNivel % 2 != 0) fill(150, 50, 0, 70); 
     else fill(0, 0, 50, 80); 
     rectMode(CENTER);
     rect(width/2, height/2, width, height);
  }
}

void manejarProgresionNivel() {
  tiempoNivel++;
  
  if (pausaPeligroTimer > 0) {
    pausaPeligroTimer--;
    return; // Pausa táctica
  }

  int paseMedio = 1500 + (selNivel * 300);
  int paseFinal = 3000 + (selNivel * 600);

  if (tiempoNivel < paseMedio) faseActual = FASE_INICIO;
  else if (tiempoNivel < paseFinal) faseActual = FASE_MEDIO;
  else faseActual = FASE_FINAL;
  
  if (faseActual != FASE_FINAL) {
    int m = (selNivel * 12); // Modificador
    
    // Zekes (AvionBasico) con tres IA
    if (tiempoNivel % (90 - m) == 0) {
      int tipoZeke = (random(1) < 0.6) ? 1 : (random(1) < 0.5 ? 2 : 3);
      listaEnemigos.add(new AvionBasico(random(50, width-50), -30, tipoZeke));
    }
    
    // Escuadrón Rojo (Bono) y Trampa
    if (tiempoNivel % 500 == 0 && escuadronRojoActivo.isEmpty()) {
       float xR = random(100, width - 100);
       for (int i = 0; i < 6; i++) {
         AvionEscuadronRojo aer = new AvionEscuadronRojo(xR, -30 - (i * 40));
         listaEnemigos.add(aer);
         escuadronRojoActivo.add(aer);
       }
       // TRAMPA: Spawnea un Bimotor justo detrás para obligar a elegir
       listaEnemigos.add(new CazaBimotor(random(50, width-50), height + 50));
    }
    
    // Interceptor Flotante
    if (tiempoNivel % 250 == 0) {
      listaEnemigos.add(new CazaEstacionario(random(100, width-100), -40));
    }
  }
  
  if (faseActual >= FASE_MEDIO && faseActual != FASE_FINAL) {
     if (tiempoNivel % 500 == 0) {
       listaEnemigos.add(new BombarderoPesado(width/2 + random(-150, 150), -80));
     }
     
     if (tiempoNivel % 300 == 0) {
       int tipoFijo = (random(1) < 0.5) ? 1 : 2;
       listaEnemigos.add(new DefensaEstatica(random(50, width-50), -50, tipoFijo));
     }
  }
  
  if (faseActual == FASE_FINAL && !bossSpawned) {
    if (listaEnemigos.isEmpty()) { 
      listaEnemigos.add(new JefeColosal(width/2, -150));
      bossSpawned = true;
    }
  }
}

void revisarEscapesRojos() {
  for (int i = escuadronRojoActivo.size() - 1; i >= 0; i--) {
     if (escuadronRojoActivo.get(i).getY() > height) {
        escuadronRojoActivo.clear();
        break;
     }
  }
}

void manejarDisparosContínuos() {
  if (cooldownP1 > 0) cooldownP1--;
  if (cooldownP2 > 0) cooldownP2--;
  
  for (Nave n : listaNaves) {
    if (!n.isVivo()) continue;
    if (n.getIdJugador() == 1 && firingP1 && cooldownP1 == 0) { crearProyectil(n); cooldownP1 = 10; }
    if (n.getIdJugador() == 2 && firingP2 && cooldownP2 == 0) { crearProyectil(n); cooldownP2 = 10; }
  }
}

void crearProyectil(Nave n) {
  if (n.hasDoubleShot()) {
    listaProyectiles.add(new Proyectil(n.getX() - 12, n.getY() - 10));
    listaProyectiles.add(new Proyectil(n.getX() + 12, n.getY() - 10));
  } else {
    listaProyectiles.add(new Proyectil(n.getX(), n.getY() - 10));
  }
}

void actualizarYDibujarEntidades() {
  revisarEscapesRojos();

  for (int i = listaEnemigos.size() - 1; i >= 0; i--) {
    Enemigo e = listaEnemigos.get(i);
    e.actualizar();
    e.dibujar();
    
    ProyectilEnemigo pe = e.disparar();
    if (pe != null) disparosEnemigos.add(pe);

    if (!(e instanceof JefeColosal) && !(e instanceof DefensaEstatica)) {
      for (Nave n : listaNaves) {
        if (n.isVivo() && dist(e.getX(), e.getY(), n.getX(), n.getY()) < 30) {
          n.morir();
          e.recibirDano();
        }
      }
    }
    
    for (int j = listaProyectiles.size() - 1; j >= 0; j--) {
      Proyectil p = listaProyectiles.get(j);
      boolean hit = false;
      
      if (e instanceof JefeColosal) {
        hit = ((JefeColosal)e).verificarTiro(p.getX(), p.getY());
      } else {
        if (dist(e.getX(), e.getY(), p.getX(), p.getY()) < 25) {
          e.recibirDano();
          hit = true;
        }
      }
      
      if (hit) {
        listaProyectiles.remove(j);
        
        if (!e.isVivo()) {
          score += 100 * selNivel;
          
          if (e instanceof BombarderoPesado) {
             score += 1000;
             pausaPeligroTimer = 150; // Micro-pausa!
          }
          if (e instanceof JefeColosal) {
             score += 50000;
             estadoJuego = ESTADO_WIN;
          }
          
          Mejora m = e.soltarMejora();
          if (m != null) listaMejoras.add(m);
          
          if (escuadronRojoActivo.contains(e)) {
             escuadronRojoActivo.remove(e);
             if (escuadronRojoActivo.isEmpty()) {
                listaMejoras.add(new Mejora(e.getX(), e.getY()));
                score += 500;
             }
          }
        }
        break; 
      }
    }
    
    if (!e.isVivo()) listaEnemigos.remove(i);
  }
  
  for (int i = disparosEnemigos.size() - 1; i >= 0; i--) {
    ProyectilEnemigo pe = disparosEnemigos.get(i);
    pe.actualizar();
    pe.dibujar();
    for (Nave n : listaNaves) {
      if (n.isVivo() && dist(pe.getX(), pe.getY(), n.getX(), n.getY()) < 15) {
        n.morir();
        pe.morir();
      }
    }
    if (!pe.isVivo()) disparosEnemigos.remove(i);
  }

  for (int i = listaMejoras.size() - 1; i >= 0; i--) {
    Mejora m = listaMejoras.get(i);
    m.actualizar();
    m.dibujar();
    for (Nave n : listaNaves) {
      if (n.isVivo() && dist(m.getX(), m.getY(), n.getX(), n.getY()) < 30) {
        n.actibarMejora();
        m.morir();
        score += 500;
      }
    }
    if (!m.isVivo()) listaMejoras.remove(i);
  }

  for (Nave n : listaNaves) if (n.isVivo()) { n.actualizar(); n.dibujar(); }
  for (int i = listaProyectiles.size() - 1; i >= 0; i--) {
    Proyectil p = listaProyectiles.get(i); p.actualizar(); p.dibujar();
    if (!p.isVivo()) listaProyectiles.remove(i);
  }
}

// Interfaz Menus y Teclas iguales que antes

void dibujarMenu() {
  fill(0, 150);
  rect(width/2, height/2, width, height);
  fill(255);
  textSize(40);
  textAlign(CENTER, CENTER);
  text("1942: ULTIMATE", width/2, 80);
  
  textSize(20);
  text("MODO DE JUEGO", width/2, 170);
  fill(selCantJugadores == 1 ? color(0,255,0) : 255); text("[1] UN JUGADOR", width/2, 200);
  fill(selCantJugadores == 2 ? color(0,255,0) : 255); text("[2] DOS JUGADORES", width/2, 230);
  
  fill(255); text("DIFICULTAD", width/2, 290);
  fill(selNivel == 1 ? color(0,255,0) : 255); text("[Q] Nivel 1 (Claro)", width/2, 320);
  fill(selNivel == 2 ? color(0,255,0) : 255); text("[W] Nivel 2 (Archipiélago)", width/2, 350);
  fill(selNivel == 3 ? color(0,255,0) : 255); text("[E] Nivel 3 (Tormenta)", width/2, 380);
  
  fill(255); text("AVIONES", width/2, 440);
  fill(200, 200, 255);
  text("P1 (Flechas): " + (selTipoAvionP1 == 1 ? "P-38 LIGHTNING" : "CAZA ÁGIL") + " ->[Z]", width/2, 470);
  text("P2 (WASD)   : " + (selTipoAvionP2 == 1 ? "P-38 LIGHTNING" : "CAZA ÁGIL") + " ->[X]", width/2, 500);
  
  fill(255, 255, 0); text("¡PULSA ESPACIO PARA INICIAR VUELO!", width/2, 550);
}

void drawUI() {
  fill(255);
  textSize(20);
  textAlign(LEFT, TOP);
  text("SCORE: " + score, 10, 10);
  textAlign(RIGHT, TOP);
  text("PHASE: " + faseActual, width - 10, 10);
}

void drawGameOver(String title, String subtitle) {
  fill(0, 180);
  rect(width/2, height/2, width, height);
  if (estadoJuego == ESTADO_WIN) fill(0, 255, 100);
  else fill(255, 0, 0);
  
  textSize(60);
  textAlign(CENTER, CENTER);
  text(title, width/2, height/2 - 40);
  textSize(25);
  fill(255);
  text("Puntaje Total: " + score, width/2, height/2 + 30);
  fill(255, 255, 0);
  text(subtitle, width/2, height/2 + 80);
}

void keyPressed() {
  if (estadoJuego == ESTADO_MENU) {
    if (key == '1') selCantJugadores = 1; if (key == '2') selCantJugadores = 2;
    if (key == 'q' || key == 'Q') selNivel = 1; if (key == 'w' || key == 'W') selNivel = 2; if (key == 'e' || key == 'E') selNivel = 3;
    if (key == 'z' || key == 'Z') selTipoAvionP1 = (selTipoAvionP1 == 1) ? 2 : 1;
    if (key == 'x' || key == 'X') selTipoAvionP2 = (selTipoAvionP2 == 1) ? 2 : 1;
    if (key == ' ') initGame();
  } 
  else if (estadoJuego == ESTADO_JUEGO) {
    for (Nave n : listaNaves) {
      if (!n.isVivo()) continue;
      if (n.getIdJugador() == 1) {
        if (keyCode == RIGHT) n.setYendoDerecha(true); if (keyCode == LEFT) n.setYendoIzquierda(true);
        if (keyCode == UP) n.setYendoArriba(true); if (keyCode == DOWN) n.setYendoAbajo(true);
        if (key == ' ') firingP1 = true;
      }
      if (n.getIdJugador() == 2) {
        if (key == 'd' || key == 'D') n.setYendoDerecha(true); if (key == 'a' || key == 'A') n.setYendoIzquierda(true);
        if (key == 'w' || key == 'W') n.setYendoArriba(true); if (key == 's' || key == 'S') n.setYendoAbajo(true);
        if (key == 'f' || key == 'F') firingP2 = true;
      }
    }
  }
  else if (estadoJuego == ESTADO_GAMEOVER || estadoJuego == ESTADO_WIN) {
    if (key == 'r' || key == 'R') estadoJuego = ESTADO_MENU;
  }
}

void keyReleased() {
  if (estadoJuego == ESTADO_JUEGO) {
    for (Nave n : listaNaves) {
      if (n.getIdJugador() == 1) {
        if (keyCode == RIGHT) n.setYendoDerecha(false); if (keyCode == LEFT) n.setYendoIzquierda(false);
        if (keyCode == UP) n.setYendoArriba(false); if (keyCode == DOWN) n.setYendoAbajo(false);
        if (key == ' ') firingP1 = false;
      }
      if (n.getIdJugador() == 2) {
        if (key == 'd' || key == 'D') n.setYendoDerecha(false); if (key == 'a' || key == 'A') n.setYendoIzquierda(false);
        if (key == 'w' || key == 'W') n.setYendoArriba(false); if (key == 's' || key == 'S') n.setYendoAbajo(false);
        if (key == 'f' || key == 'F') firingP2 = false;
      }
    }
  }
}
