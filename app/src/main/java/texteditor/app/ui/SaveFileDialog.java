package texteditor.app.ui;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import texteditor.app.io.FileIO;

/**
 * Save a file in a specified encoding. 
 * @author Ryan Martin
 */
public class SaveFileDialog {

    // Owner window for the file chooser. 
    private final Window owner; 

    // Resource bundle for localisation. 
    private final ResourceBundle bundle; 

    /**
     * Constructor 
     * 
     * @param owner Owner window for the file chooser. 
     * @param bundle Resource bundle for localisation. 
     */
    public SaveFileDialog(Window owner, ResourceBundle bundle) {
        this.owner = owner; 
        this.bundle = bundle; 
    }
    
    /**
     * Display the encoding dialog, then the file chooser dialog to save the file. 
     * 
     * @param fileData File data to save. 
     * @throws IOException If an IO exception occurs. 
     */
    public void show(String fileData) throws IOException {
        String encoding = new EncodingSelectionDialog(bundle).show();
        FileChooser fc = new FileChooser(); 
        File file = fc.showSaveDialog(owner);

        if (file != null) {
            FileIO.saveFile(file.getAbsolutePath(), fileData, encoding);
        }
    }
}
