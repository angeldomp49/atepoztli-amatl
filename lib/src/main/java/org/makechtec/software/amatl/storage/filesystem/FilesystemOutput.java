package org.makechtec.software.amatl.storage.filesystem;

import org.makechtec.software.amatl.logging.Amatl;
import org.makechtec.software.amatl.logging.StorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Logger;

public class FilesystemOutput implements Amatl {

    private static final Logger LOG = Logger.getLogger(FilesystemOutput.class.getName());

    private final FileGenerationSettings settings;
    private Calendar lastCalendarSnapshot;
    private String filename;

    public FilesystemOutput(FileGenerationSettings settings) {
        this.settings = settings;
        this.lastCalendarSnapshot = Calendar.getInstance();
        changeFilename();
    }

    @Override
    public void saveOn(final CharSequence message) throws StorageException {

        if (hasToChangeOutputFile()) {
            changeFilename();
        }

        var filePath = Paths.get(filename);
        var rootDirPath = Paths.get(settings.nameSettings().rootDirectory());

        try {

            LOG.info("creating log file with next filename: " + filename);

            if(!Files.exists(rootDirPath)){
                Files.createDirectories(rootDirPath);
            }

            if(Files.notExists(filePath)){
                Files.createFile(filePath);
            }

            Files.write(filePath, prepareMessage(message), StandardOpenOption.APPEND);
        } catch (IOException e) {
            LOG.severe("FileSystemOutput has had a problem dealing with filesystem making logging");
            throw new StorageException(e);
        }

    }

    private boolean hasToChangeOutputFile() {
        var currentCalendar = Calendar.getInstance();
        var temporaryCalendar = this.createComparableCalendar();
        var comparationResult = currentCalendar.compareTo(temporaryCalendar);

        if (comparationResult >= 0) {
            lastCalendarSnapshot = currentCalendar;
            return true;
        }

        return false;
    }

    private Calendar createComparableCalendar() {
        var temporaryCalendar = Calendar.getInstance();

        temporaryCalendar.setTimeInMillis(lastCalendarSnapshot.getTimeInMillis());

        switch (settings.frequencyUnit()) {
            case HOUR -> temporaryCalendar.add(Calendar.HOUR, settings.frequencyQuantity());
            case DAY -> temporaryCalendar.add(Calendar.DAY_OF_MONTH, settings.frequencyQuantity());
            case WEEK -> temporaryCalendar.add(Calendar.DAY_OF_MONTH, settings.frequencyQuantity() * 7);
            case MONTH -> temporaryCalendar.add(Calendar.MONTH, settings.frequencyQuantity());
            case YEAR -> temporaryCalendar.add(Calendar.YEAR, settings.frequencyQuantity());
        }

        return temporaryCalendar;
    }

    private void changeFilename() {
        filename = settings.nameGenerationStrategy().generateFilename(settings.nameSettings());
    }

    private byte[] prepareMessage(final CharSequence message) {

        var timeFormatter = new SimpleDateFormat(" -- hh:mm:ss - dd-MM-yyyy");

        var formattedMessage = "\n" + message.toString() + timeFormatter.format(Calendar.getInstance().getTime());

        return formattedMessage.getBytes();
    }

}
