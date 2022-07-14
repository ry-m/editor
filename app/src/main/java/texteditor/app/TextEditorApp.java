package texteditor.app;

import texteditor.KeyMapParser;
import texteditor.ParseException;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.stage.Stage;
import texteditor.app.model.KeyMapping;
import texteditor.app.plugins.PluginManager;
import texteditor.app.plugins.ScriptManager;
import texteditor.app.ui.MainUI;

/**
 * Main class.
 * @author Ryan Martin
 */
public class TextEditorApp extends Application {

    /**
     * Main. 
     * 
     * @param args Command-line arguments. 
     */
    public static void main(String[] args) {
        Application.launch(args);
    }

    /**
     * JavaFX main application.
     */
    @Override
    public void start(Stage stage) {
        String localeString = getParameters().getNamed().get("locale");

        ResourceBundle bundle;
        if (localeString == null) {
            bundle = ResourceBundle.getBundle("bundle", Locale.getDefault());
        } else {
            Locale locale;

            try {
                locale = new Locale(localeString);
                bundle = ResourceBundle.getBundle("bundle", locale); 
            } catch (MissingResourceException e) {
                // This exception should not occur, it is up to the developer to 
                // implement the resource. 
                e.printStackTrace();
                bundle = ResourceBundle.getBundle("bundle", Locale.getDefault());
            }
        }

        List<KeyMapping> keyMap = null;
        MainUI ui = new MainUI(bundle, new PluginManager(bundle), new ScriptManager()); 

        try {
            keyMap = KeyMapParser.parse(
                new FileInputStream("app/src/main/resources/keymap")
            );
        } catch (IOException e) {
            ui.showError(bundle.getString("keymap_err"), e.getMessage());
        } catch (ParseException e2) {
            ui.showError(bundle.getString("parse_err"), e2.getMessage());
        }

        ui.setKeyMapList(keyMap);
        ui.display(stage);
    }
}