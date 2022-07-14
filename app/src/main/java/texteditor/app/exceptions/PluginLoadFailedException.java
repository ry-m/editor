package texteditor.app.exceptions;

/**
 * General class for plugin load failures.
 * @author Ryan Martin
 */
public class PluginLoadFailedException extends Exception {
    
    public PluginLoadFailedException(String msg) {
        super(msg); 
    }
}
