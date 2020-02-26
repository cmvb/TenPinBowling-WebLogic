package co.com.cmvb.tpb.tenpinbowling.mb;

import co.com.cmvb.tpb.tenpinbowling.enums.EEstado;
import java.io.Serializable;
import javax.inject.Named;
import javax.faces.view.ViewScoped;

@ViewScoped
@Named("enumMB")
public class EnumMB implements Serializable {

    private static final long serialVersionUID = 1068952186172152584L;

    /**
     * devuelve los estados del registro
     *
     * @return
     */
    public EEstado[] getEstados() {
        return EEstado.values();
    }
}
