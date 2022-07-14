package texteditor.app.plugins;

import java.util.Locale;

import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import texteditor.api.EventHandler;
import texteditor.api.FunctionKey;
import texteditor.api.TextEditorAPI;
import texteditor.api.TextModificationHandler;
import texteditor.app.ui.MainUI;

/**
 * API implementation. 
 * Separates GUI access from the MainUI and the plugins. 
 * @author Ryan Martin
 */
public class Editor implements TextEditorAPI {

    // Reference to the main UI.
    // Only this class can interact directly with the GUI. 
    // Plugins only have access to API methods. 
    private final MainUI ui; 
    
    /**
     * Constructor. 
     * 
     * @param ui UI reference.
     */
    public Editor(MainUI ui) {
        this.ui = ui;
    } 
    
    @Override
    public void registerButton(String label, EventHandler callback) {
        Button btn = new Button(label); 
        btn.setOnAction((event) -> callback.onEvent());

        ui.addToolbarButton(btn);
    }

    @Override
    public void registerOnFunctionKeyEvent(FunctionKey key, EventHandler callback) {
        ui.putCallback(key, callback);
    }

    @Override
    public void registerTextModificationHandler(TextModificationHandler callback) {
        ui.putCallback(callback);
    }

    @Override
    public String promptUser(String prompt) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(prompt);

        return dialog.showAndWait().orElse(null);
    }

    @Override
    public Locale getLocale() {
        return ui.getLocale();
    }

    @Override
    public int getCaretPosition() {
        return ui.getTextArea().getCaretPosition();
    }

    @Override
    public String getText() {
        return ui.getTextArea().getText();
    }

    @Override
    public String getText(int startIdx, int endIdx) {
        return ui.getTextArea().getText(startIdx, endIdx);
    }

    @Override
    public int getTextLength() {
        return ui.getTextArea().getLength();
    }

    @Override
    public void setCaretPosition(int pos) {
        ui.getTextArea().positionCaret(pos);
    }

    @Override
    public void insertText(String text) {
        insertText(getCaretPosition(), text);
    }

    @Override
    public void insertText(int idx, String text) {
        ui.getTextArea().insertText(idx, text);
    }

    @Override
    public String deleteText(int startIdx, int endIdx) {
        int length = getTextLength();

        if (startIdx > endIdx || startIdx >= length || endIdx <= 0) {
            return null;
        } else {
            String str = getText().substring(startIdx, endIdx);
            ui.getTextArea().deleteText(startIdx, endIdx);

            return str;
        }
    }

    @Override
    public String deleteText(String text) {
        String beforeCaret = getText().substring(0, getCaretPosition());
        String afterCaret = getText().substring(getCaretPosition(), getTextLength());
        
        if (beforeCaret.endsWith(text)) {
            String result = beforeCaret.substring(0, beforeCaret.length() - text.length());
            result += afterCaret;
            ui.getTextArea().setText(result);
            return result;
        } else {
            return null;
        }
    }

    @Override
    public void highlightText(int startIdx, int endIdx) {
        int length = getTextLength();

        int s = Math.max(startIdx, 0);
        int e = Math.min(endIdx, length);

        ui.getTextArea().selectRange(s, e);
    }

    @Override
    public void replaceText(String find, String replace) {
        String text = getText();
        text = text.replace(find, replace);

        ui.getTextArea().setText(text);
    }
}
