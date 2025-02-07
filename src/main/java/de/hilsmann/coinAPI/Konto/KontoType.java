package de.hilsmann.coinAPI.Konto;

public enum KontoType {
    SERVER("Serverkonto"),
    PLAYER("Spielerkonto"),
    COMPANY("Firmenkonto");

    private final String displayName;

    KontoType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
