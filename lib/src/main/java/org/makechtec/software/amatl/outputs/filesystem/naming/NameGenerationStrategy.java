package org.makechtec.software.amatl.outputs.filesystem.naming;

@FunctionalInterface
public interface NameGenerationStrategy {

    String generateFilename(NameSettings settings);

}
