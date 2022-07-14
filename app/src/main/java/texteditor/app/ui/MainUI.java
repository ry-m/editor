package texteditor.app.ui;

import java.io.IOException;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import texteditor.api.EventHandler;
import texteditor.api.FunctionKey;
import texteditor.api.TextModificationHandler;
import texteditor.app.model.*;
import texteditor.app.model.KeyAction.*;
import texteditor.app.plugins.Editor;
import texteditor.app.plugins.PluginManager;
import texteditor.app.plugins.ScriptManager;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * Represents the main user interface for the text editor.
 * Contains all UI elements for the editor, including the text 
 * area and toolbar. 
 * 
 * Provides the API implementing class (Editor) with a reference 
 * to itself for adding buttons and registering callbacks. 
 * 
 * @author Ryan Martin, Dave Cooper. 
 */
public class MainUI {

    // List of key mappings from the keymap file. 
    private List<KeyMapping> keyMap;

    // The tool bar containing the buttons.
    private ToolBar toolBar = new ToolBar();

    // The API.
    private final Editor editor;

    // The main text area. 
    private final TextArea textArea = new TextArea();

    // Resource bundle for languages. 
    private final ResourceBundle bundle;

    // Plugin manager.
    private final PluginManager pluginManager;

    // Script manager. 
    private final ScriptManager scriptManager;

    // Locale. 
    private final Locale locale;

    // List of text modification handlers. 
    private final List<TextModificationHandler> tmHandlers = new LinkedList<>();

    // Map of function key handlers. 
    private final EnumMap<FunctionKey, EventHandler> fnHandlers = new EnumMap<>(FunctionKey.class);

    // Mutex lock for modifying UI elements. 
    private final Object mutex = new Object();

    /**
     * Main constructor 
     * 
     * @param bundle Resource bundle for languages. 
     * @param pluginManager Plugin manager.
     * @param scriptManager Script manager.
     */
    public MainUI(ResourceBundle bundle, PluginManager pluginManager, ScriptManager scriptManager) {
        // Initialise the API. 
        this.editor = new Editor(this);

        this.bundle = bundle; 
        this.pluginManager = pluginManager; 
        this.scriptManager = scriptManager;

        this.locale = bundle.getLocale();
    }

    /**
     * Initialise and display the main GUI window. 
     * 
     * @author Ryan Martin, Dave Cooper. 
     * @param stage Stage reference.
     */
    public void display(Stage stage) {
        stage.setTitle(bundle.getString("title"));
        stage.setMinWidth(800);

        // Create toolbar
        Button saveBtn = new Button(bundle.getString("save") + "..."); 
        Button loadBtn = new Button(bundle.getString("load") + "...");
        Button pluginsBtn = new Button(bundle.getString("plugins_scripts") + "...");
        toolBar = new ToolBar(loadBtn, saveBtn, new Separator(), pluginsBtn, new Separator());

        // Subtle user experience tweaks
        toolBar.setFocusTraversable(false);
        toolBar.getItems().forEach(btn -> btn.setFocusTraversable(false));
        textArea.setStyle("-fx-font-family: 'monospace'"); // Set the font
        
        // Add the main parts of the UI to the window.
        BorderPane mainBox = new BorderPane();
        mainBox.setTop(toolBar);
        mainBox.setCenter(textArea);
        Scene scene = new Scene(mainBox);

        saveBtn.setOnAction(event -> {
            try {
                new SaveFileDialog(stage, bundle).show(editor.getText());
            } catch (IOException e) {
                showError("io_err", e.getMessage());
            }
        });

        loadBtn.setOnAction(event -> {
            try {
                String s = new LoadFileDialog(stage, bundle).show(); 
                if (s != null) {
                    textArea.setText(s);
                }
            } catch (IOException e) {
                showError("io_err", e.getMessage());
            }
        });
        
        // Plugins dialog button. 
        pluginsBtn.setOnAction(event -> 
            new PluginsListDialog(bundle, this, pluginManager, scriptManager).show(editor, stage)
        );
        
        // Notify the text modification handlers. 
        textArea.textProperty().addListener((object, oldValue, newValue) -> {
            synchronized(mutex) {
                for (TextModificationHandler h : tmHandlers) {
                    h.onTextModified(oldValue, newValue);
                }
            }
        });
        
        // Handle key presses. 
        scene.setOnKeyPressed(keyEvent -> {            
            KeyCode key = keyEvent.getCode();
            boolean ctrl = keyEvent.isControlDown();
            boolean shift = keyEvent.isShiftDown();
            boolean alt = keyEvent.isAltDown();

            // Handle function keys for function key handlers. 
            if (key.isFunctionKey()) {
                // Retrieve the string representation of the key
                // and match it to the enum name. 
                FunctionKey fnKey = FunctionKey.valueOf(key.getName());
                EventHandler h = fnHandlers.get(fnKey);

                // Trigger the handler if an event handler was found.
                if (h != null) {
                    h.onEvent();
                }
            // If the key is not a function key, check if it is a letter
            // key (A-Z). 
            } else if (key.isLetterKey() && keyMap != null) {
                KeyMapping match = null; 
                // Retrieve the key's letter. 
                char c = key.toString().charAt(0);
                boolean matchFound = false; 
                int i = 0; 

                // Iterate through the key map until a match is found
                // or the end of the map is reached. 
                while (!matchFound && i < keyMap.size()) {
                    KeyMapping m = keyMap.get(i); 

                    // Check if the combination matches. 
                    if (m.getCombo().isCombo(alt, ctrl, shift, c)) {
                        match = m;
                        matchFound = true; 
                    }

                    i++;
                }

                // Perform the action if the match is found.
                if (match != null) {
                    KeyAction action = match.getAction();
                    doKeyAction(action);
                }
            }
        });
        
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();
    }

    /**
     * Perform a key action. 
     * A key action (defined by the KeyAction class) represents
     * inserting/deleting text at the caret/start of line. 
     * Uses the API (Editor) for text modification.
     * 
     * @param action Key action.
     */
    private void doKeyAction(KeyAction action) {
        String text = action.getText();
        KeyActionType type = action.getType(); 
        KeyActionPosition pos = action.getPosition();

        switch (type) {
            case ACTION_INSERT -> {
                switch (pos) {
                    case AT_CARET -> 
                        // Simply insert the text at the current caret position. 
                        editor.insertText(text); 
                    case LINE_START -> {
                        // Retrieve the line start index and insert. 
                        int lineStart = findLineStart(); 
                        editor.insertText(lineStart, text);
                    }
                }
            } case ACTION_DELETE -> {
                switch (pos) {
                    case AT_CARET -> 
                        editor.deleteText(text);
                    case LINE_START -> {
                        int lineStart = findLineStart(); 

                        String currentText = editor.getText(); 
                        currentText = currentText.substring(lineStart);
            
                        if (currentText.startsWith(text)) {
                            editor.deleteText(lineStart, lineStart + text.length());
                        }
                    }
                }
            }
        }
    }

    /**
     * Determine the start of the line, identified by the last newline
     * sequence from the caret. 
     * 
     * @return Index of line start.
     */
    private int findLineStart() {
        String text = textArea.getText().substring(0, textArea.getCaretPosition()); 

        int start = text.lastIndexOf("\n");

        if (start <= 0) 
            return 0; 
        else 
            return start + 1; 
    }

    /**
     * Display an error message to the user. 
     * 
     * @param msg Header text (brief description).
     * @param details Details of the error.
     */
    public void showError(String msg, String details) {
        Alert alert = new Alert(AlertType.ERROR);

        alert.setHeaderText(msg);
        alert.setContentText(details);

        alert.showAndWait();
    }
    
    /**
     * Set the key map list. 
     * 
     * @param keyMap Key map list. 
     */
    public void setKeyMapList(List<KeyMapping> keyMap) {
        this.keyMap = keyMap;
    }

    /**
     * Add a toolbar button. 
     * (API required method).
     * 
     * @param btn Button to add
     */
    public void addToolbarButton(Button btn) {
        toolBar.getItems().add(btn);
    }

    /**
     * Add a function key callback. 
     * An error is displayed if the function key already has a callback 
     * mapping.
     * 
     * @param key Function key. 
     * @param callback Callback. 
     */
    public void putCallback(FunctionKey key, EventHandler callback) {
        synchronized(mutex) {
            if (!fnHandlers.containsKey(key)) {
                fnHandlers.put(key, callback);
            } else {
                showError(bundle.getString("plugin_err"), bundle.getString("fn_key_err"));
            }
        }
    }

    /**
     * Add a text modification callback. 
     * 
     * @param callback Callback.
     */
    public void putCallback(TextModificationHandler callback) {
        synchronized(mutex) {
            tmHandlers.add(callback);
        }
    }

    /**
     * 
     * @return The locale for the app.
     */
    public Locale getLocale() {
        return locale; 
    }

    /**
     * 
     * @return Reference to the text area.
     */
    public TextArea getTextArea() {
        return textArea;
    }
}
