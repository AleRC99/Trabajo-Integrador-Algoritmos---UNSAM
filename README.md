# 🛩️ Módulo 6: Super Étendard – TPI Algoritmos 1 (UNSAM 2026)

> **Módulo de juego vertical-scrolling** inspirado en la mecánica arcade de los años 80, adaptado al contexto histórico-educativo del Conflicto del Atlántico Sur. Desarrollado bajo el paradigma de Programación Orientada a Objetos, con énfasis en arquitectura limpia, patrones de diseño, pruebas automatizadas y contratos de integración escalables.

---

## 📖 Descripción del Sistema
El sistema implementa un juego arcade bidimensional donde el usuario controla una aeronave Super Étendard, enfrentando oleadas de objetivos con patrones predefinidos. Cumple con la estética pixel-art de la década del 80, gestión de estados, detección de colisiones optimizada y persistencia de estadísticas locales/globales.

🔹 **Enfoque técnico:** `POO` | `Design Patterns` (State, Strategy, Object Pool, Observer) | `Java2D/Swing` | `Maven` | `JUnit 5`  
🔹 **Integración:** Compatible con el ecosistema global `1982` mediante contratos `IModulePlugin` e `IModuleStats` validados por el comité del curso.  
🔹 **Calidad:** Código modular, documentación trazable (p1–p9), ciclo de vida gestionado con GitFlow y CI/CD.


---

## 🚀 Build, Pruebas & Ejecución
```bash
# 1. Clonar y compilar
git clone <repo-url>
cd tpi-algo1-modulo6-super-etendard
mvn clean compile

# 2. Ejecutar pruebas unitarias
mvn test

# 3. Ejecutar el módulo (autónomo o inyectado al HOME)
mvn exec:java -Dexec.mainClass="com.unsam.algo1.module6.MainLauncher"