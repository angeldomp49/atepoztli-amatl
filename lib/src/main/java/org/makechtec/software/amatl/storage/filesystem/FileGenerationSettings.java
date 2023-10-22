package org.makechtec.software.amatl.storage.filesystem;

import org.makechtec.software.amatl.storage.filesystem.naming.NameGenerationStrategy;
import org.makechtec.software.amatl.storage.filesystem.naming.NameSettings;

public record FileGenerationSettings(
        int frequencyQuantity,
        FrequencyUnit frequencyUnit,
        NameGenerationStrategy nameGenerationStrategy,
        NameSettings nameSettings
) {

}
