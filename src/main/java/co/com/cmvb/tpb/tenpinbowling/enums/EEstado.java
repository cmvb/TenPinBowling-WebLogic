package co.com.cmvb.tpb.tenpinbowling.enums;

import lombok.Getter;

public enum EEstado {
    ACTIVO("ACTIVO"),
    INACTIVO("INACTIVO");

    @Getter
    private final String nombre;

    private EEstado(String nombre) {
        this.nombre = nombre;
    }
}
