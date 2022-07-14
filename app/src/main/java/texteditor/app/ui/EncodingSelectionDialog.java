package texteditor.app.ui;

import java.util.ResourceBundle;

import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

/**
 * Prompt the user for a specific encoding (one of UTF-8, UTF-16 or UTF-32). 
 * @author Ryan Martin
 */
public class EncodingSelectionDialog {

    // Localisation.
    private final ResourceBundle bundle; 

    /**
     * Constructor 
     * 
     * @param bundle Resource bundle for localisation.
     */
    public EncodingSelectionDialog(ResourceBundle bundle) {
        this.bundle = bundle;
    }
    
    /**
     * Display the dialog and return the encoding. 
     * 
     * @author Dave Cooper, Ryan Martin.
     * @return Encoding string.
     */
    public String show() {
        Dialog<String> encodingDialog = new Dialog<>();

        ComboBox<String> encodingComboBox = new ComboBox<>();
        FlowPane content = new FlowPane();
        encodingDialog.setTitle(bundle.getString("encoding_dialog_title"));
        encodingDialog.getDialogPane().setContent(content);
        encodingDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);        
        encodingDialog.setResultConverter(
            btn -> (btn == ButtonType.OK) ? encodingComboBox.getValue() : null
        );
        
        content.setHgap(8.0);
        content.getChildren().setAll(new Label(bundle.getString("encoding_field")), encodingComboBox);
        
        encodingComboBox.getItems().setAll("UTF-8", "UTF-16", "UTF-32");
        encodingComboBox.setValue("UTF-8");

        return encodingDialog.showAndWait().orElse(null);
    }
}
