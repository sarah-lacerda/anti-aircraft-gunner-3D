package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import exception.FailedToLoadResourceException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FileUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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

    public static <T> T deserializeFrom(String filePath, Class<T> targetClass) {
        try {
            return OBJECT_MAPPER.readValue(getFileFromResourceAsStream(filePath), targetClass);
        } catch (IOException e) {
            throw new FailedToLoadResourceException(targetClass.getName(), filePath, e);
        }
    }
}
