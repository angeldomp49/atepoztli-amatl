package org.makechtec.software.amatl.storage.streaming;

import org.makechtec.software.amatl.logging.Amatl;
import org.makechtec.software.amatl.logging.StorageException;

import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class StreamOutput implements Amatl {

    private static final Logger LOG = Logger.getLogger(StreamOutput.class.getName());

    private final OutputStream outputStream;

    public StreamOutput(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void saveOn(CharSequence message) throws StorageException {
        try {
            outputStream.write(prepareMessage(message));
        } catch (IOException e) {
            LOG.severe("Application couldn't put logs in provided stream");
            throw new StorageException(e);
        }
    }

    private byte[] prepareMessage(final CharSequence message) {

        var timeFormatter = new SimpleDateFormat(" -- hh:mm:ss - dd-MM-yyyy");

        var formattedMessage = "\n" + message.toString() + timeFormatter.format(Calendar.getInstance().getTime());

        return formattedMessage.getBytes();
    }

}
