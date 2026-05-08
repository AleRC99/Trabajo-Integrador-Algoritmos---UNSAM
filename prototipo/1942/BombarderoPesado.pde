class BombarderoPesado extends Enemigo {
  BombarderoPesado(float x, float y) {
    super(x, y);
    this.velocidad = 0.8; // Movimiento muy lento
    this.hp = 15; // Requiere muchísimos impactos
    this.puedeDisparar = true;
  }

  @Override
  void actualizar() {
    y += velocidad;
    if (y > height + 100) vivo = false;
  }
  
  @Override
  ProyectilEnemigo disparar() {
    if (random(1) < 0.02 && shootCooldown <= 0) {
      shootCooldown = 90; // Disparo más pesado
      return new ProyectilEnemigo(x, y + 20);
    }
    if (shootCooldown > 0) shootCooldown--;
    return null;
  }

  @Override
  void dibujar() {
    pushMatrix();
    translate(x, y);
    
    fill(90, 100, 110); // Gris Oscuro/Azulado masivo
    noStroke();
    
    // Fuselaje enorme
    ellipse(0, 0, 30, 80); 
    
    // Alas que ocupan el 25% de la pantalla (aprox 150px)
    rectMode(CENTER);
    rect(0, -5, 150, 20); 
    
    // Motores (Cuatrimotor)
    fill(70);
    rect(-40, -5, 12, 30);
    rect(-20, -5, 12, 30);
    rect(40, -5, 12, 30);
    rect(20, -5, 12, 30);
    
    // Cola
    rect(0, -35, 60, 15);
    
    // Si tiene HP bajo, emite humo procedural
    if (hp <= 7) {
      fill(50, 150); // Humo transparente oscuro
      ellipse(random(-15, 15), random(-30, 0), random(10, 20), random(10, 20));
      ellipse(random(-15, 15), random(-30, 0), random(10, 20), random(10, 20));
      fill(255, 100, 0, 150); // Llamas ocasionales
      if (random(1) < 0.3) ellipse(random(-10, 10), random(-10, 10), 10, 10);
    }
    
    popMatrix();
  }
  
  @Override
  Mejora soltarMejora() {
    // El bombardero tiene 100% chance de soltar mejora al morir por su dureza
    return new Mejora(x, y);
  }
}
