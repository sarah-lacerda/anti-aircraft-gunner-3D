package util;

import exception.FailedToLoadResourceException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {

    private FileUtils() {
    }

    public static InputStream getFileFromResourceAsStream(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("Error while accessing resource, file path is blank");
        }
        InputStream inputStream = FileUtils.class.getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new FailedToLoadResourceException("", filePath, "File not found!");
        }
        return inputStream;
    }

    public static BufferedReader getBufferedReaderForFile(String filePath) {
        return new BufferedReader(new InputStreamReader(getFileFromResourceAsStream(filePath)));
    }
}
