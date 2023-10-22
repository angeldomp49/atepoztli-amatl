package org.makechtec.software.amatl.storage.filesystem.buil_in_naming;

import org.makechtec.software.amatl.storage.filesystem.naming.NameGenerationStrategy;
import org.makechtec.software.amatl.storage.filesystem.naming.NameSettings;

import java.io.File;
import java.util.Calendar;

public class SimpleTimestampNameStrategy implements NameGenerationStrategy {
    @Override
    public String generateFilename(NameSettings settings) {
        return
                settings.rootDirectory() +
                        File.pathSeparator +
                        settings.prefix() +
                        Calendar.getInstance().getTimeInMillis() +
                        settings.suffix();
    }
}
