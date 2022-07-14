package texteditor.api;

import java.util.Locale;

/**
 * Text editor API.
 */
public interface TextEditorAPI {

    // Callback registers. 

    /**
     * Register a button in the toolbar and its action. 
     * 
     * @param label The text to be displayed as the button label. 
     * @param callback The event handler. 
     */
    void registerButton(String label, EventHandler callback); 

    /**
     * Register a function key callback.
     * Duplicate keys are not allowed. If a duplicate is found,
     * the event will not be added and the user is prompted. 
     * 
     * @param key The specified function key. 
     * @param callback The event handler. 
     */
    void registerOnFunctionKeyEvent(FunctionKey key, EventHandler callback);

    /**
     * Register a text modification callback. 
     * 
     * @param callback The event handler. 
     */
    void registerTextModificationHandler(TextModificationHandler callback);

    // API functions. 

    /**
     * Prompt the user for a string given a message in a dialog window. 
     * 
     * @param prompt The prompt text to be displayed in the dialog window. 
     * @return The user's input. 
     */
    String promptUser(String prompt); 

    /**
     * Get the locale used by the program. 
     * 
     * @return The locale. 
     */
    Locale getLocale(); 

    /**
     * Get the current position of the caret. 
     * 
     * @return Caret position between 0 and length of text. 
     */
    int getCaretPosition(); 

    /**
     * Get all the text in the text area. 
     * 
     * @return The contents of the text area. 
     */
    String getText(); 

    /**
     * Get text from a specific range. If the range is out of bounds, 
     * the index will be set to 0 or the length of the text area
     * for the start and end index respectively.
     * 
     * @param startIdx The start index. 
     * @param endIdx The end index. 
     * @return The value of the string from the start index to the end index. 
     */
    String getText(int startIdx, int endIdx);

    /**
     * Get the length of the text area. 
     * 
     * @return Length of the text area.
     */
    int getTextLength(); 

    /**
     * Modify the position of the caret. 
     * If the position is less than 0, it is set to 0 (the start).
     * If the position is greater than the length of the text area, 
     * it is set the the length of the text area (the end).
     * 
     * @param pos The new position.
     */
    void setCaretPosition(int pos);

    /**
     * Insert text at the caret position. 
     * 
     * @param text The text to enter. 
     */
    void insertText(String text); 

    /**
     * Insert text at a specific index. 
     * 
     * @param idx The index. 
     * @param text The text to enter. 
     */
    void insertText(int idx, String text);

    /**
     * Delete text in an index range. 
     * If the index range is invalid (i.e., endIdx < startIdx), no text is deleted. 
     * If the range is out of bounds, the index will be set to 0 or the length of 
     * the text area for the start and end index respectively.
     * 
     * @param startIdx The start index. 
     * @param endIdx The end index. 
     * @return The string that was deleted from the text area.
     */
    String deleteText(int startIdx, int endIdx);

    /**
     * Delete the specified text if it occurs before the caret 
     * position. 
     * 
     * @param text The text to look for. 
     * @return The deleted text, or null if the text was not found before the caret.
     */
    String deleteText(String text);

    /**
     * Replace the specified text with another string. 
     * 
     * @param find Text to find.
     * @param replace Text in place of found string.
     */
    void replaceText(String find, String replace);

    /**
     * Select text in a range 
     * 
     * @param startIdx Start index.
     * @param endIdx End index. 
     */
    void highlightText(int startIdx, int endIdx); 
}
