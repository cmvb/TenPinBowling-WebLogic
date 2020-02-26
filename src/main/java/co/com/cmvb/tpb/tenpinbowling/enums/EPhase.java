package co.com.cmvb.tpb.tenpinbowling.enums;

import lombok.Getter;

public enum EPhase {
    QUERY("query"),
    EDIT("edit");

    @Getter
    private final String nombre;

    private EPhase(String nombre) {
        this.nombre = nombre;
    }
}
