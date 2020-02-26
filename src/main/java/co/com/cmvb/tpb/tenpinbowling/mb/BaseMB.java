package co.com.cmvb.tpb.tenpinbowling.mb;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.UploadedFile;

public abstract class BaseMB<T> implements Serializable {

    private static final long serialVersionUID = 315259619797071301L;

    private final Class<T> clazz = null;

    @Getter
    @Setter
    private T partidaActual = null;

    @Getter
    @Setter
    private T partidaSeleccionada = null;

    @Getter
    @Setter
    private List<T> partidasHistorial = null;

    @Getter
    @Setter
    private UploadedFile archivo;

}
