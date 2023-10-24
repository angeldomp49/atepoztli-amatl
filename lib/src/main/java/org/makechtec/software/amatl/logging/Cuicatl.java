package org.makechtec.software.amatl.logging;

import java.util.logging.Logger;

public class Cuicatl {

    private static final Logger LOG = Logger.getLogger(Cuicatl.class.getName());

    private final LevelOutputSettings settings;
    private final MessageFormatter formatter;

    private Exception originalException;


    public Cuicatl(LevelOutputSettings settings) {
        this.settings = settings;
        this.formatter = new MessageFormatter();
    }

    public void info(CharSequence message, Object... values) {
        try {
            settings.info().saveOn(formatter.cleanMessage(message, values));
        } catch (StorageException e) {
            LOG.warning("There was an error saving log message! \nTo get original exception please use getOriginalException()");
            originalException = e;
        }
    }

    public void debug(CharSequence message, Object... values) {
        try {
            settings.debug().saveOn(formatter.cleanMessage(message, values));
        } catch (StorageException e) {
            LOG.warning("There was an error saving log message! \nTo get original exception please use getOriginalException()");
            originalException = e;
        }
    }

    public void warning(CharSequence message, Object... values) {
        try {
            settings.warning().saveOn(formatter.cleanMessage(message, values));
        } catch (StorageException e) {
            LOG.warning("There was an error saving log message! \nTo get original exception please use getOriginalException()");
            originalException = e;
        }
    }

    public void severe(CharSequence message, Object... values) {
        try {
            settings.severe().saveOn(formatter.cleanMessage(message, values));
        } catch (StorageException e) {
            LOG.warning("There was an error saving log message! \nTo get original exception please use getOriginalException()");
            originalException = e;
        }
    }

    public void error(CharSequence message, Object... values) {
        try {
            settings.error().saveOn(formatter.cleanMessage(message, values));
        } catch (StorageException e) {
            LOG.warning("There was an error saving log message! \nTo get original exception please use getOriginalException()");
            originalException = e;
        }
    }

    public Exception getOriginalException() {
        return originalException;
    }

}
