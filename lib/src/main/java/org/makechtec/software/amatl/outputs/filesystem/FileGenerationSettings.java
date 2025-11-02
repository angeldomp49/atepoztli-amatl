package org.makechtec.software.amatl.outputs.filesystem;

import org.makechtec.software.amatl.outputs.filesystem.naming.NameGenerationStrategy;
import org.makechtec.software.amatl.outputs.filesystem.naming.NameSettings;

public record FileGenerationSettings(
        int frequencyQuantity,
        FrequencyUnit frequencyUnit,
        NameGenerationStrategy nameGenerationStrategy,
        NameSettings nameSettings
) {

}
