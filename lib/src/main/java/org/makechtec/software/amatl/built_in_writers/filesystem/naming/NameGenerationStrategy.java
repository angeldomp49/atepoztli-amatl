package org.makechtec.software.amatl.built_in_writers.filesystem.naming;

@FunctionalInterface
public interface NameGenerationStrategy {

    String generateFilename(NameSettings settings);

}
