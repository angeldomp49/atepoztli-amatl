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
    public void saveMessage(CharSequence message, CharSequence metadata) throws StorageException {
        try {
            
            var formattedMessage = message.toString() + "--" + metadata.toString();
            outputStream.write(formattedMessage.getBytes());
        } catch (IOException e) {
            LOG.severe("Application couldn't put logs in provided stream");
            throw new StorageException(e);
        }
    }

}
