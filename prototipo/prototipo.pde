/**
 * TP Algoritmos 1 - UNSAM
 * Prototipo Arcade 1942: Super Étendard (Contexto Malvinas)
 * Bibliografía: Clean Code (Funciones con un propósito), Effective Java (Encapsulamiento)
 */

PShape avion;
float playerX, playerY;
float velocidad = 4.0;
float gridOffset = 0.0;
boolean left, right, up, down;

void setup() {
  size(600, 800);
  smooth();
  
  // Posición inicial del jugador
  playerX = width / 2;
  playerY = height - 150;
  
  // Construcción del avión usando PShape para mejor rendimiento (evita redibujar polígonos cada frame)
  avion = crearSuperEtendard();
}

void draw() {
  fondoOceano();
  moverJugador();
  
  // Renderizado del jugador
  pushMatrix();
  translate(playerX, playerY);
  shape(avion);
  animarMotor(); 
  popMatrix();
  
  // HUD simple
  dibujarHUD();
}

// ---------------------------------------------------------
// 1. GEOMETRÍA Y DISEÑO (Super Étendard ARA)
// ---------------------------------------------------------
PShape crearSuperEtendard() {
  PShape grupo = createShape(GROUP);
  grupo.noStroke();

  // 1.1 Fuselaje Principal (Color gris azulado ARA)
  PShape fuselaje = createShape();
  fuselaje.beginShape();
  fuselaje.fill(100, 110, 120);
  fuselaje.vertex(0, -50);      // Nariz
  fuselaje.vertex(14, -25);
  fuselaje.vertex(12, 30);
  fuselaje.vertex(0, 50);       // Cola
  fuselaje.vertex(-12, 30);
  fuselaje.vertex(-14, -25);
  fuselaje.endShape(CLOSE);
  grupo.addChild(fuselaje);

  // 1.2 Alas Principales (Swept Wings)
  PShape alas = createShape();
  alas.beginShape();
  alas.fill(85, 95, 105); // Ligeramente más oscuro para dar profundidad
  alas.vertex(14, -25);
  alas.vertex(45, 5);     // Punta derecha
  alas.vertex(12, 25);
  alas.vertex(-12, 25);
  alas.vertex(-45, 5);    // Punta izquierda
  alas.vertex(-14, -25);
  alas.endShape(CLOSE);
  grupo.addChild(alas);

  // 1.3 Estabilizadores Horizontales (Cola)
  PShape cola = createShape();
  cola.beginShape();
  cola.fill(80, 90, 100);
  cola.vertex(12, 30);
  cola.vertex(26, 40);
  cola.vertex(0, 46);
  cola.vertex(-26, 40);
  cola.vertex(-12, 30);
  cola.endShape(CLOSE);
  grupo.addChild(cola);

  // 1.4 Cabina / Canopy
  PShape cabina = createShape();
  cabina.beginShape();
  cabina.fill(135, 200, 255); // Azul cristal
  cabina.stroke(60);
  cabina.strokeWeight(1.5);
  cabina.vertex(0, -35);
  cabina.quadraticVertex(8, -15, 5, -10);
  cabina.vertex(-5, -10);
  cabina.quadraticVertex(-8, -15, 0, -35);
  cabina.endShape(CLOSE);
  grupo.addChild(cabina);

  // 1.5 Misiles Exocet (Histórico: 1 por ala)
  PShape mIzq = createShape();
  mIzq.beginShape();
  mIzq.fill(70);
  mIzq.vertex(-36, 8);
  mIzq.vertex(-28, 8);
  mIzq.vertex(-28, 28);
  mIzq.vertex(-32, 34);
  mIzq.vertex(-36, 28);
  mIzq.endShape(CLOSE);
  grupo.addChild(mIzq);

  PShape mDer = createShape();
  mDer.beginShape();
  mDer.fill(70);
  mDer.vertex(36, 8);
  mDer.vertex(28, 8);
  mDer.vertex(28, 28);
  mDer.vertex(32, 34);
  mDer.vertex(36, 28);
  mDer.endShape(CLOSE);
  grupo.addChild(mDer);
  
  return grupo;
}

// ---------------------------------------------------------
// 2. LÓGICA DE JUEGO (Game Loop)
// ---------------------------------------------------------

void moverJugador() {

  if (left)  playerX -= velocidad;
  if (right) playerX += velocidad;
  if (up)    playerY -= velocidad;
  if (down)  playerY += velocidad;

  // Limites
  playerX = constrain(playerX, 50, width - 50);
  playerY = constrain(playerY, 80, height - 60);
}

void keyPressed() {

  if (keyCode == LEFT || key == 'a')  left = true;
  if (keyCode == RIGHT || key == 'd') right = true;
  if (keyCode == UP || key == 'w')    up = true;
  if (keyCode == DOWN || key == 's')  down = true;
}

void keyReleased() {

  if (keyCode == LEFT || key == 'a')  left = false;
  if (keyCode == RIGHT || key == 'd') right = false;
  if (keyCode == UP || key == 'w')    up = false;
  if (keyCode == DOWN || key == 's')  down = false;
}

void animarMotor() {
  // Simula la llama del motor con oscilación
  float llameo = map(sin(frameCount * 0.25), -1, 1, 10, 35);
  fill(255, 100, 50, 180); noStroke();
  ellipse(0, 55 + llameo/2, 8, llameo);
  fill(255, 220, 100, 180);
  ellipse(0, 55 + llameo/3, 4, llameo * 0.6);
}

// ---------------------------------------------------------
// 3. ENTORNO VISUAL (Estilo 1942)
// ---------------------------------------------------------
void fondoOceano() {
  background(15, 35, 85); // Azul oscuro profundo
  
  // Grid de movimiento para simular avance
  stroke(255, 30); strokeWeight(1);
  gridOffset = (gridOffset + 3.0) % 60;
  
  for (int i = 0; i < width; i += 60) line(i, 0, i, height);
  for (int j = -60 + (int)gridOffset; j < height; j += 60) line(0, j, width, j);
}

void dibujarHUD() {
  fill(255); noStroke();
  textAlign(CENTER, TOP);
  textSize(14);
  text("SUPER ÉTENDARD - ARMADA ARGENTINA", width/2, 20);
  textSize(10); fill(200, 200, 200);
  text("TP Algoritmos 1 - UNSAM", width/2, height - 20);
}
