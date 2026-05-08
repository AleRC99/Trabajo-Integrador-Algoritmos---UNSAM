class Torreta {
  float offsetX, offsetY;
  boolean viva = true;
  int hp = 15; // Salud de cada torreta
  
  Torreta(float ox, float oy) {
    this.offsetX = ox; 
    this.offsetY = oy;
  }
  
  void recibirDano() {
    hp--;
    if (hp <= 0) viva = false;
  }
  
  void dibujar(float bossX, float bossY) {
    if (!viva) return;
    pushMatrix();
    translate(bossX + offsetX, bossY + offsetY);
    fill(60);
    ellipse(0, 0, 20, 20); // Base torreta
    fill(30);
    rectMode(CENTER);
    rect(0, 5, 6, 15); // Cañón apuntando abajo
    // Luz indicativa
    fill(255, 0, 0);
    ellipse(0, -3, 4, 4);
    popMatrix();
  }
}

class JefeColosal extends Enemigo {
  ArrayList<Torreta> torretas;
  float dirX = 1;

  JefeColosal(float x, float y) {
    super(x, y);
    this.velocidad = 0.3; // Extremadamente lento
    this.hp = 40; // Núcleo central durísimo
    
    torretas = new ArrayList<Torreta>();
    // Añadimos 4 torretas destructibles
    torretas.add(new Torreta(-120, -10));
    torretas.add(new Torreta(120, -10));
    torretas.add(new Torreta(-60, 20));
    torretas.add(new Torreta(60, 20));
  }

  @Override
  void actualizar() {
    // Entra a la pantalla y se detiene bajando muy poco
    if (y < 120) {
      y += 1;
    } else {
      // Movimiento lateral pendular
      x += 1.5 * dirX;
      if (x > width - 150 || x < 150) {
        dirX *= -1;
      }
      y += 0.05; // Baja hiper lento
    }
    
    // Boss over?
    if (hp <= 0) vivo = false;
  }

  ArrayList<ProyectilEnemigo> dispararTorretas() {
    ArrayList<ProyectilEnemigo> balas = new ArrayList<ProyectilEnemigo>();
    if (y < 50) return balas; // No dispara si no entró bien
    
    for (Torreta t : torretas) {
      if (t.viva && random(1) < 0.015) { // Torretas agresivas
        balas.add(new ProyectilEnemigo(x + t.offsetX, y + t.offsetY + 10));
      }
    }
    // El núcleo también dispara a veces si el boss está bajo asedio
    if (random(1) < 0.005) {
      balas.add(new ProyectilEnemigo(x, y + 40));
    }
    return balas;
  }

  boolean verificarTiro(float px, float py) {
    // Primero probar impacto en las torretas
    for (Torreta t : torretas) {
      if (t.viva) {
        float d = dist(px, py, x + t.offsetX, y + t.offsetY);
        if (d < 15) {
          t.recibirDano();
          return true; // Balas absorbidas por la torreta
        }
      }
    }
    // Si no golpeó torretas, probar impacto al núcleo (si está en su área central)
    if (dist(px, py, x, y) < 40) {
      this.recibirDano();
      return true;
    }
    return false; // Falló el tiro
  }

  @Override
  void dibujar() {
    pushMatrix();
    translate(x, y);
    
    fill(60, 70, 70); // Coloso verde oliva hiper oscuro / diésel punk
    noStroke();
    
    // Cuerpo inmenso 1 (Ancho de la pantalla casi)
    rectMode(CENTER);
    rect(0, 0, 360, 40); // Alas colosales
    
    // Motores (Múltiples hélices representadas)
    fill(40);
    for (int i = -140; i <= 140; i += 40) {
      if (i == -20 || i == 20) continue; 
      rect(i, 20, 15, 30);
    }
    
    // Núcleo
    fill(80, 90, 90);
    ellipse(0, 0, 90, 100);
    
    // Cabina acorazada principal
    fill(255, 200, 0); // Ojos amarillos brillantes que imponen
    rect(-15, 30, 10, 5);
    rect(15, 30, 10, 5);
    
    // Efectos de daño global del boss (Humo inmenso)
    if (hp <= 15) {
      fill(50, 200);
      ellipse(-30, -20, 40, 40);
      ellipse(30, -20, 50, 40);
    }
    
    popMatrix();
    
    // Por último dibujamos las torretas anexas
    for (Torreta t : torretas) {
      t.dibujar(x, y);
    }
  }
}
