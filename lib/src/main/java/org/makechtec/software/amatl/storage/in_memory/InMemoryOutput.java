package org.makechtec.software.amatl.storage.in_memory;

import org.makechtec.software.amatl.logging.Amatl;
import org.makechtec.software.amatl.logging.StorageException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class InMemoryOutput implements Amatl {

    private final List<String> messages;

    public InMemoryOutput() {
        messages = new ArrayList<>();
    }

    @Override
    public void saveOn(CharSequence message) throws StorageException {
        messages.add(prepareMessage(message));
    }

    private String prepareMessage(final CharSequence message) {

        var timeFormatter = new SimpleDateFormat("-- hh:mm:ss - dd-MM-yyyy");

        return "\n" + message.toString() + timeFormatter.format(Calendar.getInstance().getTime());

    }

    public List<String> getMessages() {
        return Collections.unmodifiableList(messages);
    }
}
