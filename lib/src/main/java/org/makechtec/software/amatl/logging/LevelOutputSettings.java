package org.makechtec.software.amatl.logging;

public record LevelOutputSettings(
        Amatl info,
        Amatl debug,
        Amatl warning,
        Amatl severe,
        Amatl error
) {
}
