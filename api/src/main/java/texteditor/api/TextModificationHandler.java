package texteditor.api;

/**
 * Handle a text modification.
 */
public interface TextModificationHandler {

    /**
     * Should be called on an update to the text area. 
     * 
     * @param prev The previous text in the editor region. 
     * @param current The current text in the editor region.
     */
    void onTextModified(String prev, String current); 
}
