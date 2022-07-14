package texteditor.app.io;

import java.io.File;
import java.io.IOException;

import texteditor.app.model.PyScript;

/**
 * Loads Python scripts. 
 * @author Ryan Martin
 */
public class ScriptLoader {
    
    /**
     * Load a Python script from file. 
     * 
     * @param fileName The file name. 
     * @return Python script.
     * @throws IOException If an IO error occurs. 
     */
    public static PyScript loadScript(String fileName) throws IOException {
        if (!fileName.endsWith(".py")) {
            throw new IOException("Invalid Python file.");
        } 

        String src = FileIO.loadFile(fileName, "utf-8");

        return new PyScript(new File(fileName).getName(), src);
    }
}
