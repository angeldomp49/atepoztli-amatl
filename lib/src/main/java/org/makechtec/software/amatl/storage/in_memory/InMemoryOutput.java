package org.makechtec.software.amatl.storage.in_memory;

import org.makechtec.software.amatl.logging.Amatl;
import org.makechtec.software.amatl.logging.StorageException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class InMemoryOutput implements Amatl {

    private final List<InMemoryMessageRecord> messages;

    public InMemoryOutput() {
        this.messages = new ArrayList<>();
    }


    @Override
    public void saveMessage(CharSequence message, CharSequence metadata) throws StorageException {
        messages.add(new InMemoryMessageRecord(message.toString(), metadata.toString()));
    }
    
    public List<InMemoryMessageRecord> getMessages() {
        return Collections.unmodifiableList(messages);
    }
    
    public record InMemoryMessageRecord(
            String message,
            String metadata
    ){}

}
