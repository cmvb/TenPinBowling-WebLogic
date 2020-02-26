package co.com.cmvb.tpb.tenpinbowling.mb;

import co.com.cmvb.tpb.tenpinbowling.util.UtilPropertiesMensaje;
import co.com.cmvb.tpb.tenpinbowling.constantes.DireccionesWeb;
import co.com.cmvb.tpb.tenpinbowling.dto.Sesion;
import co.com.cmvb.tpb.tenpinbowling.util.Util;
import java.io.Serializable;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import lombok.Setter;
import lombok.Getter;

@SessionScoped
@Named("sesionMB")
public class SesionMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(SesionMB.class.getName());
    public static final String NOMBRE_LLAVE_TOKEN = "app.bean.sesion.tpb";
    private static final long serialVersionUID = -2850939549135889356L;

    @Getter
    private Sesion sesion;

    @Getter
    @Setter
    private String tituloMsj;

    @Getter
    @Setter
    private String claseMsj;

    @Getter
    @Setter
    private String msj;

    @Getter
    @Setter
    private String mensajeExcepcion;

    @Getter
    private final String aplicativoNombre = UtilPropertiesMensaje.getProperty("tpb.generico.aplicativo.nombre", (Object) null);

    @Getter
    private final String versionProyecto = "Vs. 1.0.0";

    @Getter
    private final String fechaVProyecto = "13/11/2019 08:00 am";

    /**
     * Método para iniciar sesión al aplicativo
     *
     * @return
     */
    public String iniciarSesion() {
        sesion = new Sesion();

        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idUsuario", sesion.getIdUsuario());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("nombreUsuario", sesion.getNombre());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", sesion.getUsuario());
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(NOMBRE_LLAVE_TOKEN, this);

        return DireccionesWeb.INDEX + "?faces-redirect=true";
    }

    /**
     * Finaliza la sesion
     *
     * @return
     */
    public String cerrarSesion() {
        sesion = null;
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("idUsuario", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("nombreUsuario", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuario", null);
        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put(NOMBRE_LLAVE_TOKEN, null);

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Object session = externalContext.getSession(false);
        HttpSession httpSession = (HttpSession) session;
        httpSession.invalidate();

        return DireccionesWeb.LOGIN;
    }

    /**
     * Obtiene un valor de la sesion como String
     *
     * @param mensaje
     * @return
     */
    public String obtenerValorSesion(String mensaje) {
        return Util.getAtributoSesion(mensaje).toString();
    }

}
