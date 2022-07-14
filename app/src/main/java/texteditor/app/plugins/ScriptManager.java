package texteditor.app.plugins;

import org.python.util.PythonInterpreter;

import java.util.List;
import java.io.IOException;
import java.util.LinkedList;

import texteditor.api.TextEditorAPI;
import texteditor.app.io.ScriptLoader;
import texteditor.app.model.PyScript;

/**
 * Loads and contains scripts. 
 * @author Ryan Martin
 */
public class ScriptManager {

    // List of Python scripts. 
    private final List<PyScript> scripts = new LinkedList<>();
    
    public ScriptManager() { }

    /**
     * Load a script and run it immediately. 
     * 
     * @param fileName Name of the script file. 
     * @param api API for the script to use. 
     * @throws IOException If the script fails to load. 
     */
    public void loadScript(String fileName, TextEditorAPI api) throws IOException {
        PyScript script = ScriptLoader.loadScript(fileName); 
        if (script != null) {
            new Thread(() -> {
                if (!scripts.contains(script)) {
                    scripts.add(script);
                    runScript(script, api);
                }
            }, "script-loader-" + script.getScriptName()).start();
        }
    }

    /**
     * 
     * @return List of all running scripts. 
     */
    public List<PyScript> getScripts() {
        return scripts;
    }

    /**
     * Run the script using the Python interpreter. 
     * 
     * @param pyScript Script
     * @param api API for the script to use. 
     */
    private void runScript(PyScript pyScript, TextEditorAPI api) {
        try (PythonInterpreter interpreter = new PythonInterpreter()) {
            interpreter.set("api", api);
            interpreter.exec(pyScript.getSrc());
        }
    }
}
