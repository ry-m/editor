package texteditor.app.ui;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.ListView;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import texteditor.api.TextEditorAPI;
import texteditor.api.TextEditorPlugin;
import texteditor.app.exceptions.PluginLoadFailedException;
import texteditor.app.model.PyScript;
import texteditor.app.plugins.PluginManager;
import texteditor.app.plugins.ScriptManager;

/**
 * Shows the list of plugins and scripts used by the application.
 * Plugins and scripts can be added by the user using this interface. 
 * 
 * @author Ryan Martin
 */
public class PluginsListDialog {

    // Resource bundle for languages. 
    private final ResourceBundle bundle;

    // Plugin manager. 
    private final PluginManager pluginsManager;

    // Script manager.
    private final ScriptManager scriptManager;

    // Reference to the UI for plugin/script error messages. 
    private final MainUI ui;

    /**
     * Main constructor. 
     * 
     * @param bundle Resource bundle for languages. 
     * @param ui Reference to the UI for plugin/script error messages. 
     * @param pluginManager Plugin manager. 
     * @param scriptManager Script manager. 
     */
    public PluginsListDialog(
        ResourceBundle bundle, MainUI ui, PluginManager pluginManager, ScriptManager scriptManager
    ) {
        this.bundle = bundle; 
        this.pluginsManager = pluginManager; 
        this.scriptManager = scriptManager;
        this.ui = ui;
    }

    /**
     * Show the dialog. 
     * 
     * @param api API for the plugins.
     * @param parent Parent window. 
     */
    public void show(TextEditorAPI api, Window parent) {
        // Toolbar buttons. 
        Button addPluginBtn = new Button(bundle.getString("add_plugin") + "...");
        Button addScriptBtn = new Button(bundle.getString("add_script") + "...");
        ToolBar toolBar = new ToolBar(addPluginBtn, addScriptBtn);
        
        ObservableList<String> list = FXCollections.observableArrayList();
        ListView<String> listView = new ListView<>(list);   

        // Set up the handler for the 'Add plugin' button. 
        addPluginBtn.setOnAction(event -> {
            try {
                TextEditorPlugin p = new AddPluginDialog(bundle, pluginsManager).show(api);
                if (p != null) 
                    list.add(p.getPluginName(bundle.getLocale()));
            } catch (PluginLoadFailedException e) {
                ui.showError(bundle.getString("plugin_err"), e.getMessage());
            }
        });

        // Set up the event handler for the 'Add script' button. 
        // Scripts must have a unique name. 
        addScriptBtn.setOnAction(event -> {
            FileChooser dialog = new FileChooser();
            File script = dialog.showOpenDialog(parent);

            if (script != null && !list.contains(script.getName())) {
                try {
                    scriptManager.loadScript(script.getAbsolutePath(), api);
                    list.add(script.getName());
                } catch (IOException e) {
                    ui.showError(bundle.getString("script_err"), bundle.getString("io_err"));
                }
            }
        });

        // Add plugins and scripts to the list view. 

        for (TextEditorPlugin p : pluginsManager.getPlugins()) {
            list.add(p.getPluginName(bundle.getLocale()));
        }

        for (PyScript s : scriptManager.getScripts()) {
            list.add(s.getScriptName());
        }
        
        BorderPane box = new BorderPane();
        box.setTop(toolBar);
        box.setCenter(listView);
        
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle(bundle.getString("all_plugins_title"));
        dialog.getDialogPane().setContent(box);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        
        dialog.showAndWait();
    }
}