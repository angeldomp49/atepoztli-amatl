package org.makechtec.software.amatl.logging;

public class Cuicatl {

    private final LevelOutputSettings settings;
    private final MessageFormatter formatter;


    public Cuicatl(LevelOutputSettings settings) {
        this.settings = settings;
        this.formatter = new MessageFormatter();
    }

    public void info(CharSequence message, Object... values) throws StorageException {
        settings.info().saveOn(formatter.cleanMessage(message, values));
    }

    public void debug(CharSequence message, Object... values) throws StorageException {
        settings.debug().saveOn(formatter.cleanMessage(message, values));
    }

    public void warning(CharSequence message, Object... values) throws StorageException {
        settings.warning().saveOn(formatter.cleanMessage(message, values));
    }

    public void severe(CharSequence message, Object... values) throws StorageException {
        settings.severe().saveOn(formatter.cleanMessage(message, values));
    }

    public void error(CharSequence message, Object... values) throws StorageException {
        settings.error().saveOn(formatter.cleanMessage(message, values));
    }

}
