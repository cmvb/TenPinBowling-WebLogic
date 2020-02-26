package co.com.cmvb.tpb.tenpinbowling.enums;

import lombok.Getter;

public enum EAction {
    QUERY("query"),
    CREATE("create"),
    UPDATE("update");

    @Getter
    private final String nombre;

    private EAction(String nombre) {
        this.nombre = nombre;
    }
}
