package org.makechtec.software.amatl.logging;

import java.util.Objects;
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
            settings.info().saveMessage(formatter.cleanMessage(message, values));
        } catch (StorageException e) {
            LOG.warning("There was an error saving log message! \nTo get original exception please use getOriginalException()");
            originalException = e;
        }
    }

    public void debug(CharSequence message, Object... values) {
        try {
            settings.debug().saveMessage(formatter.cleanMessage(message, values));
        } catch (StorageException e) {
            LOG.warning("There was an error saving log message! \nTo get original exception please use getOriginalException()");
            originalException = e;
        }
    }

    public void warning(CharSequence message, Object... values) {
        try {
            settings.warning().saveMessage(formatter.cleanMessage(message, values));
        } catch (StorageException e) {
            LOG.warning("There was an error saving log message! \nTo get original exception please use getOriginalException()");
            originalException = e;
        }
    }

    public void severe(CharSequence message, Object... values) {
        try {
            settings.severe().saveMessage(formatter.cleanMessage(message, values));
        } catch (StorageException e) {
            LOG.warning("There was an error saving log message! \nTo get original exception please use getOriginalException()");
            originalException = e;
        }
    }

    public void error(CharSequence message, Object... values) {
        try {
            settings.error().saveMessage(formatter.cleanMessage(message, values));
        } catch (StorageException e) {
            LOG.warning("There was an error saving log message! \nTo get original exception please use getOriginalException()");
            originalException = e;
        }
    }

    public Exception getOriginalException() {
        var originalException = this.originalException;
        this.originalException = null;
        return originalException;
    }

    public boolean hasException(){
        return Objects.nonNull(originalException);
    }

}
