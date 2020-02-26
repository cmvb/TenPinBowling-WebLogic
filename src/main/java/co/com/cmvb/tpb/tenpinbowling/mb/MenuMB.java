package co.com.cmvb.tpb.tenpinbowling.mb;

import co.com.cmvb.tpb.tenpinbowling.enums.ESeveridadMensaje;
import co.com.cmvb.tpb.tenpinbowling.util.Util;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@ViewScoped
@Named("menuMB")
public class MenuMB implements Serializable {

    private static final long serialVersionUID = -3735496683870227099L;

    /**
     * Accede a la pantalla de inicio de la aplicación
     *
     * @return ruta de inicio de TPB
     */
    public String irPartida() {
        Util.guardarNotificacionUsuarioSesion(null, Util.getSeveridadMensaje(ESeveridadMensaje.DEFAULT));
        return "/pages/dashboard/inicio.xhtml?faces-redirect=true";
    }
}
