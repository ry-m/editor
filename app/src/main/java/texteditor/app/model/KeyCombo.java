package texteditor.app.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a key combination, which consists of a single 
 * character (a-z) and a set of modifiers. 
 * 
 * The modifiers are defined in the KeyModifier class, and represent one 
 * of CTRL, ALT, SHIFT. 
 * 
 * Only one of each modifier is allowed, and one modifier must be 
 * specified at minimum.
 * 
 * @author Ryan Martin
 */
public class KeyCombo {

    // The letter of the combo. 
    private char letter; 

    // List of modifiers (no duplicates allowed as per constructor).
    // Modifiers are one of CTRL, ALT and SHIFT. 
    private final List<KeyModifier> modifiers = new ArrayList<>(); 

    /**
     * Constructs a key combo, consisting of a single character and 
     * at least one of CTRL, ALT, SHIFT. 
     * 
     * No duplicate modifiers allowed. 
     * 
     * @param letter Character. 
     * @param modifiers List of modifiers. 
     */
    public KeyCombo(char letter, KeyModifier... modifiers) {
        this.letter = letter; 

        // No duplicate modifiers are allowed. 
        for (KeyModifier m : modifiers) {
            addModifier(m);
        }
    }

    /**
     * Check if the conditions of a key event match this combo. 
     * 
     * @param altDown Is the ALT key down?
     * @param ctrlDown Is the CTRL key down?
     * @param shiftDown Is the SHIFT key down?
     * @param c The main character. 
     * @return True if the conditions match the combination. 
     */
    public boolean isCombo(boolean altDown, boolean ctrlDown, boolean shiftDown, char c) {
        boolean charMatch = Character.toUpperCase(c) == Character.toUpperCase(letter);
        boolean altMatch = altDown == modifiers.contains(KeyModifier.KEY_ALT); 
        boolean ctrlMatch = ctrlDown == modifiers.contains(KeyModifier.KEY_CTRL);
        boolean shiftMatch = shiftDown == modifiers.contains(KeyModifier.KEY_SHIFT);

        return (charMatch && altMatch && ctrlMatch && shiftMatch);
    }

    /**
     * Add a key modifier to this combination.
     * 
     * @param m Modifier. 
     */
    public void addModifier(KeyModifier m) {
        if (!this.modifiers.contains(m)) {
            this.modifiers.add(m); 
        }
    }

    /**
     * Set the letter for the combination. 
     * 
     * @param c Letter.
     */
    public void setLetter(char c) {
        this.letter = c; 
    }

    /**
     * String representation of the combination.
     */
    @Override 
    public String toString() {
        String s = ""; 

        if (modifiers.contains(KeyModifier.KEY_CTRL)) s += "CTRL+";
        if (modifiers.contains(KeyModifier.KEY_ALT)) s += "ALT+"; 
        if (modifiers.contains(KeyModifier.KEY_SHIFT)) s += "SHIFT+"; 

        s += letter; 

        return s; 
    }
}
