package Home;

import processing.core.PApplet;
import Contrato.ModuloJuego;
import Contrato.ContextoJuego;
import Home.ui.*;
import java.util.List;
import Aviones.etendard.SE_GestorPrincipal;

/**
 * Orquestador principal del Home/Lobby.
 * Coordina gestores, navegacion, pantallas y ciclo de vida de modulos.
 * Implementa IModuloObserver para recibir eventos de los modulos.
 */
public class HomeJuego implements IModuloObserver {

    private PApplet app;
    private GestorModulos gestorModulos;
    private GestorEstadisticas gestorEstadisticas;
    private ControladorNavegacion controladorNav;

    // Pantallas
    private PantallaInicio pantallaInicio;
    private PantallaSeleccion pantallaSeleccion;
    private PantallaEstadisticas pantallaEstadisticas;

    // Modulo actualmente en ejecucion (null si no hay ninguno)
    private ModuloJuego moduloActual;

    // Temporizador para el stub de prueba (cuanto falta para finalizar)
    private int tiempoJuegoFrames;

    public HomeJuego(PApplet app) {
        this.app = app;
        this.controladorNav = new ControladorNavegacion();

        // Crear repositorio de estadisticas en carpeta "../estadisticas/" (fuera de "out/") para evitar borrado por compilacion
        RepositorioEstadisticas repositorio = new RepositorioEstadisticasArchivo("../estadisticas");
        this.gestorEstadisticas = new GestorEstadisticas(repositorio);
        this.gestorModulos = new GestorModulos();
    }

    /** Registra un modulo en el gestor */
    public void registrarModulo(ModuloJuego modulo) {
        gestorModulos.agregarModulo(modulo);
    }

    /** Inicializa el Home y prepara las pantallas */
    public void iniciarHome() {
        pantallaInicio = new PantallaInicio(app.width, app.height);
        pantallaSeleccion = new PantallaSeleccion(
            gestorModulos.listarModulos(), app.width, app.height
        );
        pantallaEstadisticas = new PantallaEstadisticas(app.width, app.height);
    }

    /** Se llama cada frame desde draw() */
    public void dibujar() {
        switch (controladorNav.getPantallaActual()) {
            case INICIO:
                pantallaInicio.dibujar(app);
                break;
            case SELECCION:
                pantallaSeleccion.dibujar(app);
                break;
            case ESTADISTICAS:
                List<EstadisticasGenerales> stats = gestorEstadisticas.obtenerTodas();
                int total = gestorEstadisticas.calcularPuntajeTotalCurso();
                pantallaEstadisticas.dibujar(app, stats, total);
                break;
            case JUEGO:
                if (moduloActual instanceof SE_GestorPrincipal) {
                    ((SE_GestorPrincipal) moduloActual).actualizarYDibujar();
                } else {
                    dibujarJuego();
                }
                break;
        }
    }

    /** Dibuja la pantalla de juego (stub simple) */
    private void dibujarJuego() {
        app.background(0);

        if (moduloActual == null) {
            controladorNav.irSeleccionModulo();
            return;
        }

        // Pantalla de "juego" simple
        app.fill(0, 255, 0);
        app.textAlign(PApplet.CENTER, PApplet.CENTER);
        app.textSize(20);
        app.text("JUGANDO: " + moduloActual.getNombreAvion(), app.width / 2, 60);

        // Barra de progreso del tiempo de juego
        tiempoJuegoFrames--;
        float progreso = 1.0f - ((float) tiempoJuegoFrames / (5 * 60)); // 5 segundos a 60fps
        app.fill(0, 80, 0);
        app.rectMode(PApplet.CORNER);
        app.rect(100, app.height / 2 - 15, app.width - 200, 30);
        app.fill(0, 255, 0);
        app.rect(100, app.height / 2 - 15, (app.width - 200) * progreso, 30);

        app.fill(0, 200, 0);
        app.textSize(10);
        app.text("Simulacion de partida en curso...", app.width / 2, app.height / 2 + 40);

        // Mostrar controles del juego
        app.fill(0, 120, 0);
        app.textSize(8);
        app.text("W: arriba  |  A: izquierda  |  S: abajo  |  D: derecha", app.width / 2, app.height - 60);
        app.text("SPACE: disparar  |  ESC: salir al menu", app.width / 2, app.height - 40);

        // Auto-finalizar cuando se acaba el tiempo
        if (tiempoJuegoFrames <= 0) {
            finalizarModuloActual();
        }
    }

    /** Maneja clics del mouse */
    public void manejarClick(float mouseX, float mouseY) {
        switch (controladorNav.getPantallaActual()) {
            case INICIO:
                if (pantallaInicio.clicEnStart(mouseX, mouseY)) {
                    controladorNav.irSeleccionModulo();
                }
                break;
            case SELECCION:
                String nombreModulo = pantallaSeleccion.clicEnModulo(mouseX, mouseY);
                if (nombreModulo != null) {
                    seleccionarModulo(nombreModulo);
                }
                if (pantallaSeleccion.clicEnEstadisticas(mouseX, mouseY)) {
                    controladorNav.irEstadisticas();
                }
                if (pantallaSeleccion.clicEnVolver(mouseX, mouseY)) {
                    controladorNav.irHome();
                }
                break;
            case ESTADISTICAS:
                if (pantallaEstadisticas.clicEnVolver(mouseX, mouseY)) {
                    controladorNav.irSeleccionModulo();
                }
                break;
            case JUEGO:
                break;
        }
    }

    /** Maneja teclas: W/S navegar, ENTER seleccionar, ESC volver */
    public void manejarTecla(char key, int keyCode) {
        Pantalla actual = controladorNav.getPantallaActual();

        // ESC: volver o salir
        if (key == 27) {
            app.key = 0; // evitar que Processing cierre la ventana
            switch (actual) {
                case JUEGO:
                    if (moduloActual instanceof SE_GestorPrincipal) {
                        SE_GestorPrincipal gp = (SE_GestorPrincipal) moduloActual;
                        // Si está jugando o pausado, forzamos salida guardando estadísticas
                        // Si está en el menú o pantalla final de su módulo, dejamos que lo maneje internamente (e.g. volverAlLobby)
                        if (gp.getEstado() instanceof Contrato.EnEjecucionState || gp.getEstado() instanceof Contrato.PausadoState) {
                            finalizarModuloActual();
                        } else {
                            gp.procesarKeyPressed(key, keyCode);
                        }
                    } else {
                        finalizarModuloActual();
                    }
                    break;
                case SELECCION:
                    controladorNav.irHome();
                    break;
                case ESTADISTICAS:
                    controladorNav.irSeleccionModulo();
                    break;
                case INICIO:
                    // No hacer nada en la pantalla de inicio
                    break;
            }
            return;
        }

        // ENTER: seleccionar
        if (key == '\n' || key == '\r') {
            switch (actual) {
                case INICIO:
                    controladorNav.irSeleccionModulo();
                    break;
                case SELECCION:
                    String resultado = pantallaSeleccion.confirmarSeleccion();
                    if ("ESTADISTICAS".equals(resultado)) {
                        controladorNav.irEstadisticas();
                    } else if ("VOLVER".equals(resultado)) {
                        controladorNav.irHome();
                    } else if (resultado != null) {
                        seleccionarModulo(resultado);
                    }
                    break;
                case ESTADISTICAS:
                    controladorNav.irSeleccionModulo();
                    break;
                case JUEGO:
                    break;
            }
            return;
        }

        // W/S: navegar menus
        if (actual == Pantalla.SELECCION) {
            if (key == 'w' || key == 'W') {
                pantallaSeleccion.moverArriba();
            }
            if (key == 's' || key == 'S') {
                pantallaSeleccion.moverAbajo();
            }
        }

        // Delegar teclas generales al modulo de juego activo
        if (actual == Pantalla.JUEGO) {
            if (moduloActual instanceof SE_GestorPrincipal) {
                ((SE_GestorPrincipal) moduloActual).procesarKeyPressed(key, keyCode);
            }
        }
    }

    /** Selecciona y arranca un modulo */
    public void seleccionarModulo(String nombreModulo) {
        try {
            moduloActual = gestorModulos.buscarModulo(nombreModulo);
            moduloActual.agregarObserver(this);

            // Crear contexto e inicializar
            ContextoJuego contexto = new ContextoJuego("Jugador", app.width, app.height);
            moduloActual.inicializarContexto(contexto);
            moduloActual.iniciar();

            // Temporizador: 5 segundos a 60fps
            tiempoJuegoFrames = 5 * 60;

            controladorNav.iniciarModulo(moduloActual);
        } catch (ModuloNoEncontradoException e) {
            System.err.println(e.getMessage());
        }
    }

    /** Finaliza el modulo actual y guarda estadisticas */
    private void finalizarModuloActual() {
        if (moduloActual != null) {
            // IMPORTANTE: remover observer ANTES de finalizar
            // para evitar recursion infinita (finalizar -> notificar -> onEvento -> finalizar)
            ModuloJuego modulo = moduloActual;
            moduloActual = null;
            modulo.removerObserver(this);

            // Guardar estadisticas antes de finalizar
            EstadisticasGenerales stats = modulo.getEstadisticasGenerales();
            if (stats != null) {
                gestorEstadisticas.guardarEstadisticas(stats);
            }

            modulo.finalizar();
        }
        controladorNav.irSeleccionModulo();
    }

    /** Muestra las estadisticas generales */
    public void mostrarEstadisticasGenerales() {
        controladorNav.irEstadisticas();
    }

    /** Cierra la aplicacion */
    public void salir() {
        app.exit();
    }

    /** Maneja teclas liberadas en el modulo activo */
    public void manejarTeclaLiberada(char key, int keyCode) {
        if (controladorNav.getPantallaActual() == Pantalla.JUEGO) {
            if (moduloActual instanceof SE_GestorPrincipal) {
                ((SE_GestorPrincipal) moduloActual).procesarKeyReleased(key, keyCode);
            }
        }
    }

    // --- IModuloObserver ---

    public void onEventoModulo(ModuloEvento evento) {
        System.out.println("[Home] Evento recibido: " + evento.getTipo()
            + " de " + evento.getNombreModulo());

        if (evento.getTipo() == ModuloEvento.Tipo.FINALIZADO) {
            finalizarModuloActual();
        }
        if (evento.getTipo() == ModuloEvento.Tipo.ERROR) {
            System.err.println("[Home] Error en modulo: " + evento.getMensaje());
            finalizarModuloActual();
        }
    }
}
