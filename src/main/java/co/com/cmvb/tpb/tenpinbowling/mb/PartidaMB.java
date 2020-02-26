package co.com.cmvb.tpb.tenpinbowling.mb;

import co.com.cmvb.tpb.tenpinbowling.configuracion.CoreException;
import co.com.cmvb.tpb.tenpinbowling.dto.JuegoDto;
import co.com.cmvb.tpb.tenpinbowling.dto.RondaDto;
import co.com.cmvb.tpb.tenpinbowling.service.PartidaSBLocal;
import co.com.cmvb.tpb.tenpinbowling.util.Util;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;

@ViewScoped
@Named("partidaMB")
public class PartidaMB extends BaseMB<JuegoDto> implements Serializable {

    private static final long serialVersionUID = -5098079246066702823L;

    @EJB
    private PartidaSBLocal partidaSB;

    @Getter
    @Setter
    private Map<String, List<RondaDto>> mapaJugadoresPuntajes;

    @Getter
    @Setter
    private boolean mostrarResultados;

    @Getter
    @Setter
    private boolean disabledJugar;

    @Getter
    @Setter
    private int contadorPartidas;

    @PostConstruct
    public void init() {
        this.mostrarResultados = false;
        this.disabledJugar = false;
        this.contadorPartidas = 1;
        this.setPartidaActual(new JuegoDto());
        this.setPartidaSeleccionada(new JuegoDto());
        this.setPartidasHistorial(new ArrayList<JuegoDto>());
        this.mapaJugadoresPuntajes = new HashMap<>();
    }

    /**
     * Método que procesa el archivo de entrada
     */
    public void procesarArchivo() {
        this.mostrarResultados = false;
        if (this.getArchivo() != null) {
            if (validaciones()) {
                for (String jugador : this.mapaJugadoresPuntajes.keySet()) {
                    System.out.println("---------------------------------------------------");
                    System.out.println("JUGADOR: " + jugador);
                    for (RondaDto ronda : this.mapaJugadoresPuntajes.get(jugador)) {
                        System.out.println("Ronda: " + ronda.getId());
                        System.out.println("Puntajes: " + ronda.getListaPuntajes());
                        System.out.println("PuntajesTexto: " + ronda.getListaPuntajesTexto());
                        System.out.println("PuntajeTotal: " + ronda.getPuntajeTotal());
                    }
                    System.out.println("RONDAS: " + this.mapaJugadoresPuntajes.get(jugador).size());
                    System.out.println("---------------------------------------------------");
                }
                FacesMessage message = new FacesMessage("Información", this.getArchivo().getFileName() + " Ha sido procesado correctamente.");
                FacesContext.getCurrentInstance().addMessage("INFORMATION", message);
                this.disabledJugar = true;
            } else {
                this.disabledJugar = false;
            }
            this.setArchivo(null);
        } else {
            FacesMessage message = new FacesMessage("Advertencia", "Debe seleccionar un archivo para procesar.");
            FacesContext.getCurrentInstance().addMessage("WARNING", message);
        }
    }

    /**
     * Jugar Ten Pin-Bowling
     */
    public void jugar() {
        try {
            this.setPartidaActual(new JuegoDto());
            this.getPartidaActual().setId(contadorPartidas);
            this.getPartidaActual().setRondasPorJugador(this.mapaJugadoresPuntajes);

            JuegoDto partida = this.partidaSB.jugarPartida(this.getPartidaActual());
            FacesMessage message = new FacesMessage("Información", "El ganador es: " + partida.getGanador().getNombre() + " con " + partida.getPuntajeMaximo() + " puntos.");
            FacesContext.getCurrentInstance().addMessage("INFORMATION", message);
            this.mostrarResultados = true;
            this.mapaJugadoresPuntajes = new HashMap<>();
            this.mapaJugadoresPuntajes = partida.getRondasPorJugador();

            this.getPartidasHistorial().add(partida);
            this.contadorPartidas++;
        } catch (CoreException ex) {
            Util.obtenerSesionActual().setMensajeExcepcion(String.format(ex.getMessage(), ex.getMessage()));
            Util.enviarPantallaException(ex);
        }
    }

    /**
     * Método de validación
     *
     * @return
     */
    public boolean validaciones() {
        this.mapaJugadoresPuntajes = new HashMap<>();
        boolean esEstructuraValida = this.validarEstructuraArchivo();
        boolean esContenidoValido = esEstructuraValida ? this.validarContenidoArchivo() : false;

        return esEstructuraValida && esContenidoValido;
    }

    /**
     * Método que permite saber si la estructura del archivo de lectura es
     * correcta
     *
     * @return
     */
    public boolean validarEstructuraArchivo() {
        boolean result = false;

        try {
            if (Util.validarExtensionArchivo(this.getArchivo().getFileName(), Util.EXTENSION_TXT)) {
                List<String> registrosArchivo = Util.listaCargueArchivos(this.getArchivo().getInputstream());
                if (registrosArchivo != null && !registrosArchivo.isEmpty()) {
                    result = this.partidaSB.validarEstructuraArchivo(registrosArchivo);
                } else {
                    FacesMessage message = new FacesMessage("Advertencia", "El archivo está vacío.");
                    FacesContext.getCurrentInstance().addMessage("WARNING", message);
                }
            } else {
                FacesMessage message = new FacesMessage("Advertencia", "La extensión del archivo no es correcta. La extensión permitida es: (TXT).");
                FacesContext.getCurrentInstance().addMessage("WARNING", message);
            }
        } catch (IOException | CoreException ex) {
            Util.obtenerSesionActual().setMensajeExcepcion(String.format(ex.getMessage(), ex.getMessage()));
            Util.enviarPantallaException(new CoreException(ex));
        }

        return result;
    }

    /**
     * Método que permite saber si el contenido del archivo de lectura es
     * correcto
     *
     * @return
     */
    public boolean validarContenidoArchivo() {
        boolean result = false;

        try {
            List<String> registrosArchivo = Util.listaCargueArchivos(this.getArchivo().getInputstream());
            this.mapaJugadoresPuntajes = this.partidaSB.validarContenidoArchivo(registrosArchivo);

            result = this.mapaJugadoresPuntajes != null && !this.mapaJugadoresPuntajes.isEmpty();
        } catch (IOException | CoreException ex) {
            Util.obtenerSesionActual().setMensajeExcepcion(String.format(ex.getMessage(), ex.getMessage()));
            Util.enviarPantallaException(new CoreException(ex));
        }

        return result;
    }

    /**
     * método para visualizar un juego previo
     *
     * @param juego
     */
    public void mostrarJuegoPrevio(JuegoDto juego) {
        this.setPartidaActual(juego);
        this.mapaJugadoresPuntajes = juego.getRondasPorJugador();
    }

}
