package org.makechtec.software.amatl.built_in_writers.filesystem;

import org.makechtec.software.amatl.built_in_writers.filesystem.naming.NameGenerationStrategy;
import org.makechtec.software.amatl.built_in_writers.filesystem.naming.NameSettings;

public record FileGenerationSettings(
        int frequencyQuantity,
        FrequencyUnit frequencyUnit,
        NameGenerationStrategy nameGenerationStrategy,
        NameSettings nameSettings
) {

}
