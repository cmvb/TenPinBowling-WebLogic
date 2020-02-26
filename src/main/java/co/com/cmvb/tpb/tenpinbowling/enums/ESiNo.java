package co.com.cmvb.tpb.tenpinbowling.enums;

import lombok.Getter;

public enum ESiNo {
    VACIO(" "),
    NO("NO"),
    SI("SI");

    @Getter
    private final String nombre;

    private ESiNo(String nombre) {
        this.nombre = nombre;
    }
}
