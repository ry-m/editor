package texteditor.api;

import java.util.Locale;

/**
 * Text editor plugin interface.
 */
public interface TextEditorPlugin {

    /**
     * API entry point 
     * 
     * @param api Reference to the implementing API. 
     */
    void start(TextEditorAPI api);

    /**
     * Retrieve the plugin name. 
     * Localisation recommended. 
     * 
     * @param locale Application locale. 
     * @return Plugin name. 
     */
    String getPluginName(Locale locale); 
}
