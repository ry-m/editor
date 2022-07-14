package texteditor.app.plugins;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import texteditor.api.TextEditorAPI;
import texteditor.api.TextEditorPlugin;
import texteditor.app.exceptions.PluginLoadFailedException;

/**
 * Loads plugins. 
 * @author Ryan Martin
 */
public class PluginManager {

    // List of currently loaded/enabled plugins. 
    private final List<TextEditorPlugin> plugins = new LinkedList<>();

    // Keep track of installed plugins. 
    private final List<String> pluginClasses = new LinkedList<>();

    // Resource bundle for languages. 
    private final ResourceBundle bundle; 

    /**
     * Constructor 
     * 
     * @param bundle Resource bundle for languages. 
     */
    public PluginManager(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    /**
     * 
     * @return List of currently loaded/enabled plugins. 
     */
    public List<TextEditorPlugin> getPlugins() {
        return plugins; 
    }

    /**
     * Load and start a plugin. 
     * 
     * @param className Class name of the plugin. 
     * @param api API for the plugin to use. 
     * @throws PluginLoadFailedException If the plugin fails to load. 
     */
    public TextEditorPlugin loadPlugin(String className, TextEditorAPI api) throws PluginLoadFailedException {
        try {
            Class<?> pluginClass = Class.forName(className); 
            TextEditorPlugin plugin = (TextEditorPlugin)pluginClass.getConstructor().newInstance();

            if (!pluginClasses.contains(className)) {
                plugins.add(plugin);
                pluginClasses.add(className);

                plugin.start(api);
                
                return plugin;
            }
        } catch (InstantiationException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
            throw new PluginLoadFailedException(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new PluginLoadFailedException(bundle.getString("plugin_not_found_err"));
        } catch (IllegalAccessException | SecurityException e) {
            throw new PluginLoadFailedException(bundle.getString("security_err"));
        } catch (ClassCastException e) {
            throw new PluginLoadFailedException(bundle.getString("class_cast_err"));
        }

        return null;
    }
}
