package texteditor.app.model; 

/**
 * Represents a key mapping, consisting of a key combination (KeyCombo)
 * and an action for when that combination occurs (KeyAction).
 * @author Ryan Martin
 */
public class KeyMapping {

    // Key combo. 
    private final KeyCombo combo; 

    // Key action.
    private final KeyAction action; 

    /**
     * Constructor
     * 
     * @param combo Key combo.
     * @param action Key action.
     */
    public KeyMapping(KeyCombo combo, KeyAction action) {
        this.combo = combo; 
        this.action = action; 
    }

    /**
     * 
     * @return Key combo.
     */
    public KeyCombo getCombo() {
        return combo; 
    }

    /**
     * 
     * @return Key action.
     */
    public KeyAction getAction() {
        return action;
    }

    /**
     * String representation of the key mapping.
     */
    @Override
    public String toString() {
        return combo.toString() + " --> " + action.toString();
    }
}