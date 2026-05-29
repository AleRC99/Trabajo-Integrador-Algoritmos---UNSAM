## AVISO
Estás en el branch Main, este contenido no está hecho para ser ejecutado en processing, se puede ejecutar en VS Code o la terminal, de acuerdo a lo indicado en el archivo COMO_EJECUTAR.md
El branch processing-ide contiene los archivos necesarios para que el juego corra en processing, al igual que instrucciones para integrar correctamente.

# Modulo 1 - Lobby del Juego
## Módulos del juego (por grupo)

| Módulo | Descripción |
|--------|-------------|
| Lobby General | Home y pantalla de inicio del juego |
| Avión Skyhawk | Armada Argentina |
| Avión Pucará | Ejército Argentino |
| Avión Aermacchi MB-339 | Piloteado por Owen Crippa |
| Avión Mirage | Fuerza Aérea Argentina |
| Avión Super Etendard | Armada Argentina |


---
## Cronograma de entregas

| Fecha | Tipo | Entregables |
|-------|------|-------------|
| 05/05/2026 | Seguimiento I | Documentación de Análisis |
| 19/05/2026 | Seguimiento II | Documentación de Diseño |
| 09/06/2026 | Final | Todos los entregables + defensa oral |

> Los seguimientos parciales no son obligatorios pero impactan en el criterio de organización del grupo.

---


### Código fuente (`src/`)

- Implementación en Java (versión 8 o superior)
- El código compila sin errores
- La ejecución no arroja excepciones inesperadas
- Documentación de uso (se puede usar Javadoc)
- Se valoran pruebas unitarias con JUnit

### Presentación (defensa oral)

Contenidos recomendados:
- Presentación de integrantes y responsabilidades
- Breve descripción del análisis del problema
- Detalle de la solución diseñada (al menos 1 caso de uso de los p5–p9)
- Problemas encontrados y cómo se resolvieron
- Posibles mejoras pendientes
- Estrategias para incorporar nueva funcionalidad
- Demostración funcional con un caso de uso
- Conclusiones

> **Todos los integrantes deben estar presentes.** Los docentes pueden preguntar a cualquier integrante sobre cualquier aspecto del trabajo.

---

## Criterios de evaluación

### Evaluación grupal

| Aspecto | Regular (1) | Bueno (2) | Muy Bueno (3) | Excelente (4) |
|---------|-------------|-----------|---------------|---------------|
| **Análisis del problema** | No se interpreta correctamente o no hay documentación | Documentación suficiente y acorde a lo solicitado | Completa y argumentada, relacionando temas de clase | Evidencia bibliografía e investigación adicional |
| **Diseño en POO** | Incompleto o no acorde al análisis | Solución básica en POO | Completo, con conceptos de diseño, extensible y mantenible | Usa patrones de diseño, reflexiona sobre principios y atributos de calidad |
| **Implementación** | No compila o arroja errores inesperados | Resuelve el problema sin errores en compilación/ejecución | Incorpora buenas prácticas y está alineado al diseño | Conocimiento profundo del lenguaje, eficiencia o pruebas unitarias |
| **Organización y presentación** | Fuera de término o formato inconsistente | Formato consistente y presentación preparada | Usa herramientas de colaboración, versionado y organización | Gestiona el ciclo de vida del trabajo |

### Evaluación individual

| Aspecto | Regular (1) | Bueno (2) | Muy Bueno (3) | Excelente (4) |
|---------|-------------|-----------|---------------|---------------|
| **Participación en el grupo** | Sin evidencia de participación | Plantea ideas o escucha a los demás | Participa activamente en discusiones y resolución | Contribuye proactivamente y fomenta la colaboración |
| **Defensa del trabajo** | No asiste o no participa | Expone su rol y aportes con claridad | Responde con seguridad las preguntas | Relaciona conceptos de la materia y argumenta decisiones de diseño |

---
## Estructura del proyecto

```
1982/                                 ← RAIZ del repositorio
│
├── README.md                               ← Explicación del proyecto para todos
├── .gitignore                              ← Archivos que NO se suben a GitHub
│
├── docs/                                   ← 📄 DOCUMENTACIÓN
│   ├── analisis/
│   │
│   └── manuales/
│       ├── Manual_Integracion_Grupos.pdf   ← Guía para que los grupos integren su avión
│       └── Contrato_Integracion.pdf        ← El contrato que deben firmar los grupos
│
├── lib/                                    ← 📚 LIBRERÍAS
│   └── processing-core.jar                 ← Librería de Processing (para dibujar ventanas)
│
└── src/                                    ← 💻 CÓDIGO FUENTE
    │
    └── main/
        │
        └── java/
            │
            ├── contrato/                   ← 🔗 CONTRATO (interfaz común que TODOS usan)
            │   │
            │   ├── ModuloJuego.java
            │   │   ← INTERFAZ PRINCIPAL. Contrato que TODOS los
            │   │     aviones deben implementar. Define los métodos
            │   │     obligatorios: iniciar(), pausar(), etc.
            │   │
            │   ├── EstadoJuego.java
            │   │   ← INTERFAZ para el Patrón State. Define los
            │   │     métodos que cada estado debe tener.
            │   │
            │   ├── NoIniciadoState.java
            │   │   ← ESTADO: El juego fue creado pero aún no
            │   │     se inició. Solo se puede llamar a iniciar().
            │   │
            │   ├── IniciandoState.java
            │   │   ← ESTADO: El juego se está iniciando
            │   │     (cargando imágenes, sonidos, etc.)
            │   │
            │   ├── EnEjecucionState.java
            │   │   ← ESTADO: El juego está corriendo normalmente.
            │   │     Desde aquí se puede pausar o finalizar.
            │   │
            │   ├── PausadoState.java
            │   │   ← ESTADO: El juego está pausado.
            │   │     Desde aquí se puede reanudar o finalizar.
            │   │
            │   ├── FinalizadoState.java
            │   │   ← ESTADO: El juego terminó (ganó o perdió).
            │   │     Es un estado terminal, no se puede hacer nada.
            │   │
            │   ├── ErrorState.java
            │   │   ← ESTADO: Ocurrió un error en el juego.
            │   │     El Home lo detecta y vuelve al menú.
            │   │
            │   ├── ContextoJuego.java
            │   │   ← DATOS que el Home le pasa al avión.
            │   │     Contiene: nombre del jugador, ancho y alto
            │   │     de la pantalla. Es INMUTABLE (solo getters).
            │   │
            │   ├── EstadisticasGenerales.java
            │   │   ← OBJETO que el avión le DEVUELVE al Home
            │   │     al finalizar la partida. Contiene puntaje,
            │   │     enemigos destruidos, tiempo jugado, etc.
            │   │
            │   └── JuegoException.java
            │       ← Excepción BASE del juego. Todas las demás
            │         excepciones heredan de esta.
            │
            ├── home/                       ← 🏠 HOME/LOBBY
            │   │
            │   ├── GameApp.java
            │   │   ← PUNTO DE ENTRADA. Extiende PApplet de
            │   │     Processing. Tiene main(), setup(), draw().
            │   │     Es lo primero que se ejecuta.
            │   │
            │   ├── HomeJuego.java
            │   │   ← ORQUESTADOR PRINCIPAL. Coordina todo el Home.
            │   │     Maneja las pantallas, los gestores, y el
            │   │     ciclo de vida de las partidas.
            │   │
            │   ├── GestorModulos.java
            │   │   ← ADMINISTRA los aviones registrados.
            │   │     Permite agregar, buscar y listar módulos.
            │   │
            │   ├── GestorEstadisticas.java
            │   │   ← PROCESA las estadísticas. Acumula puntajes,
            │   │     guarda en archivo, y calcula totales.
            │   │
            │   ├── ControladorNavegacion.java
            │   │   ← MANEJA LA NAVEGACIÓN entre pantallas.
            │   │     Home → Selección → Estadísticas → Juego.
            │   │
            │   ├── RepositorioEstadisticas.java
            │   │   ← PERSISTENCIA. Lee y escribe estadísticas
            │   │     en archivos .dat con serialización.
            │   │
            │   └── ui/                     ← INTERFAZ GRÁFICA del Home
            │       │
            │       ├── Pantalla.java
            │       │   ← ENUM con las pantallas: INICIO,
            │       │     SELECCION, ESTADISTICAS, JUEGO.
            │       │
            │       ├── Boton.java
            │       │   ← CLASE REUTILIZABLE para botones.
            │       │     Detecta hover, clics y dibuja estilo.
            │       │
            │       ├── PantallaInicio.java
            │       │   ← PANTALLA DE INICIO. Muestra "1982"
            │       │     y el botón START.
            │       │
            │       ├── PantallaSeleccion.java
            │       │   ← PANTALLA DE SELECCIÓN. Muestra
            │       │     botones de aviones y estadísticas.
            │       │
            │       └── PantallaEstadisticas.java
            │           ← PANTALLA DE ESTADÍSTICAS.
            │             Muestra ranking y puntajes.
            │
            ├── aviones/                    ← ✈️ AVIONES
            │   │
            │   ├── mirage/
            │   │   ├── AvionMirage.java
            │   │   └── recursos/
            │   │       ├── imagenes/
            │   │       └── sonidos/
            │   │
            │   ├── pucara/
            │   │   ├── AvionPucara.java
            │   │   └── recursos/
            │   │
            │   ├── skyhawk/
            │   │   ├── AvionSkyhawk.java
            │   │   └── recursos/
            │   │
            │   ├── etendard/
            │   │   ├── AvionEtendard.java
            │   │   └── recursos/
            │   │
            │   └── aermacchi/
            │       ├── AvionAermacchi.java
            │       └── recursos/
            │
            └── resources/                  ← 🖼️ RECURSOS COMPARTIDOS
                │
                ├── imagenes/
                │   ├── 1982.png
                │   ├── fondo_inicio.png
                │   ├── fondo_seleccion.png
                │   └── iconos/
                │       ├── mirage_icon.png
                │       ├── pucara_icon.png
                │       └── ...
                │
                └── fonts/
                    └── PressStart2P-Regular.ttf
```

