package org.makechtec.software.amatl.logging;

public class StorageException extends Exception{
    private final Exception originalException;

    public StorageException(Exception originalException) {
        this.originalException = originalException;
    }


    public Exception getOriginalException() {
        return originalException;
    }

}
