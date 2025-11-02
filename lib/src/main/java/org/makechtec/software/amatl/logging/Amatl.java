package org.makechtec.software.amatl.logging;

public interface Amatl {

    void saveMessage(final CharSequence message, CharSequence metadata) throws StorageException;

}
