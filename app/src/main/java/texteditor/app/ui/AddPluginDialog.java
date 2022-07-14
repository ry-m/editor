package texteditor.app.ui;

import java.util.ResourceBundle;

import javafx.scene.control.TextInputDialog;
import texteditor.api.TextEditorAPI;
import texteditor.api.TextEditorPlugin;
import texteditor.app.exceptions.PluginLoadFailedException;
import texteditor.app.plugins.PluginManager;

/**
 * Dialog for adding a new plugin.
 * @author Ryan Martin
 */
public class AddPluginDialog {

    // Resource bundle for languages. 
    private final ResourceBundle bundle;

    // Plugin manager. 
    private final PluginManager manager;

    /**
     * Constructor. 
     * 
     * @param bundle Resource bundle for languages .
     * @param manager Plugin manager. 
     */
    public AddPluginDialog(ResourceBundle bundle, PluginManager manager) {
        this.bundle = bundle; 
        this.manager = manager; 
    }
    
    /**
     * Show the dialog. Prompts the user for the class name 
     * of the plugin, uses the plugin manager to load the plugin. 
     * 
     * @param api API for the plugins to use. 
     * @throws PluginLoadFailedException If the plugin fails to load (usually a reflection exception). 
     */
    protected TextEditorPlugin show(TextEditorAPI api) throws PluginLoadFailedException {    
        TextInputDialog dialog = new TextInputDialog();

        dialog.setTitle(bundle.getString("add_plugin_title"));
        dialog.setHeaderText(bundle.getString("plugin_prompt"));
        
        String inputStr = dialog.showAndWait().orElse(null);
        
        if(inputStr != null) {
            return manager.loadPlugin(inputStr, api);
        } else {
            return null;
        }
    }
}
