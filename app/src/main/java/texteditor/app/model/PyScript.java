package texteditor.app.model;

import java.util.Objects;

/**
 * Represents a Python script. 
 * @author Ryan Martin
 */
public class PyScript {
    
    // Source code. 
    private final String src; 

    // Name of the script (file name). 
    private final String scriptName; 

    /**
     * Constructor 
     * 
     * @param scriptName Name of the script (file name).
     * @param src Source code. 
     */
    public PyScript(String scriptName, String src) {
        this.scriptName = scriptName;
        this.src = src; 
    }

    /**
     * 
     * @return Name of the script (file name). 
     */
    public String getScriptName() {
        return scriptName; 
    }

    /**
     * 
     * @return Source code. 
     */
    public String getSrc() {
        return src; 
    }

    @Override 
    public boolean equals(Object o) {
        if (o instanceof PyScript) {
            PyScript p = (PyScript)o;
            return Objects.equals(p.scriptName, this.scriptName) && Objects.equals(p.src, this.src);
        }

        return false; 
    }
}
