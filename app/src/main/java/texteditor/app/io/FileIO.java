package texteditor.app.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Methods for file saving and loading.
 * @author Ryan Martin
 */
public class FileIO {
    
    /**
     * Load a file with a specified encoding. 
     * 
     * @param fileName File name.
     * @param encoding Encoding string.
     * @return The string contents of the file using the encoding. 
     * @throws IOException If an IO exception occurs. 
     */
    public static String loadFile(String fileName, String encoding) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)), encoding);
    }

    /**
     * Save a file to a path using a specified encoding. 
     * 
     * @param fileName Output file name. 
     * @param data Content to write. 
     * @param encoding Encoding string. 
     * @throws IOException If an IO exception occurs. 
     */
    public static void saveFile(String fileName, String data, String encoding) throws IOException {
        if (data != null) {
            Files.write(Paths.get(fileName), data.getBytes(encoding));
        }
    }
}