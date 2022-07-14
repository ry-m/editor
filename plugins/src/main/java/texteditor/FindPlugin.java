package texteditor;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.Locale;
import java.util.ResourceBundle;

import texteditor.api.EventHandler;
import texteditor.api.FunctionKey;
import texteditor.api.TextEditorAPI;
import texteditor.api.TextEditorPlugin;

/**
 * Implementation of the find plugin. 
 * @author Ryan Martin
 */
public class FindPlugin implements TextEditorPlugin {

    /**
     * Register the button and event handler. 
     */
    @Override
    public void start(TextEditorAPI api) {
        Locale locale = api.getLocale();
        ResourceBundle bundle = ResourceBundle.getBundle("findplugin", locale);
        
        EventHandler handler = () -> {
            // Prompt the user for a string via the API. 
            String toFind = api.promptUser(bundle.getString("enter_term"));

            // Normalise the text after the caret. 
            int caretPos = api.getCaretPosition(); 
            String text = api.getText(caretPos, api.getTextLength());
            String normalisedText = Normalizer.normalize(text, Form.NFC);

            if (toFind != null) {
                // Normalise the input. 
                String normalisedToFind = Normalizer.normalize(toFind, Form.NFC);

                // Find the first index of the occurrance. 
                int idx = normalisedText.indexOf(normalisedToFind); 

                // Highlight the text. 
                if (idx != -1) {
                    api.highlightText(idx, idx + toFind.length());
                }
            }
        };

        // Register the button using the API.
        api.registerButton(bundle.getString("find_btn") + "...", handler);
        // Register the function key. 
        api.registerOnFunctionKeyEvent(FunctionKey.F3, handler);
    }

    /**
     * Get the localised plugin name. 
     */
    @Override
    public String getPluginName(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("findplugin", locale);
        return bundle.getString("name");
    }
}
