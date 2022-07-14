package texteditor.app.model;

/**
 * Represents a key action for the text editor, i.e., one of:
 *      INSERT [string] AT START OF LINE 
 *      INSERT [string] AT CARET
 *      DELETE [string] AT START OF LINE 
 *      DELETE [string] AT CARET
 * @author Ryan Martin
 */
public class KeyAction {

    /**
     * Type of operation (insert or delete). 
     */
    public enum KeyActionType {
        ACTION_DELETE, ACTION_INSERT
    }

    /**
     * Where the operation occurs (at line start or at caret). 
     */
    public enum KeyActionPosition {
        LINE_START, AT_CARET
    }

    // Key action type. 
    private final KeyActionType type; 

    // Key action position.
    private final KeyActionPosition pos; 

    // The text being inserted/deleted. 
    private final String text; 

    /**
     * Main constructor 
     * 
     * @param type Action type
     * @param pos Action position 
     * @param text Text being inserted or deleted. 
     */
    public KeyAction(KeyActionType type, KeyActionPosition pos, String text) {
        this.type = type; 
        this.pos = pos; 
        this.text = text; 
    }

    /**
     * 
     * @return Key action type. 
     */
    public KeyActionType getType() {
        return type; 
    }

    /**
     * 
     * @return Key action position. 
     */
    public KeyActionPosition getPosition() {
        return pos;
    }

    /**
     * 
     * @return Desired text to insert/delete. 
     */
    public String getText() {
        return text; 
    }

    @Override
    public String toString() {
        String s = ""; 

        switch (type) {
            case ACTION_DELETE -> s += "delete ";
            case ACTION_INSERT -> s += "insert ";
        }

        s += "\"" + text + "\" at ";

        switch (pos) {
            case AT_CARET -> s += "caret";
            case LINE_START -> s += "start of line"; 
        }

        return s; 
    }
}
