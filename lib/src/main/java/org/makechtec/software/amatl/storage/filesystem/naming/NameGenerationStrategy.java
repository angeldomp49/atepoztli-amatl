package org.makechtec.software.amatl.storage.filesystem.naming;

@FunctionalInterface
public interface NameGenerationStrategy {

    String generateFilename(NameSettings settings);

}
