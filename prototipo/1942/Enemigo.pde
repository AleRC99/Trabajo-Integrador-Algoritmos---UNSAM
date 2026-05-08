abstract class Enemigo extends Objeto {
  boolean puedeDisparar = false;
  int shootCooldown = 0;
  int hp = 1; // Salud base
  
  Enemigo(float x, float y) {
    super(x, y);
  }

  void recibirDano() {
    hp--;
    if (hp <= 0) {
      vivo = false;
    }
  }

  Mejora soltarMejora() {
    if (random(1) < 0.2) { 
      return new Mejora(x, y);
    }
    return null;
  }
  
  ProyectilEnemigo disparar() {
    if (puedeDisparar && random(1) < 0.01 && shootCooldown <= 0) {
      shootCooldown = 60; // 1 segundo
      return new ProyectilEnemigo(x, y);
    }
    if (shootCooldown > 0) shootCooldown--;
    return null;
  }
}
