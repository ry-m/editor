package texteditor.app.ui;

import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javafx.stage.FileChooser;
import javafx.stage.Window;
import texteditor.app.io.FileIO;

/**
 * Dialog window for loading a file. 
 * Displays the encoding dialog. 
 * 
 * @author Ryan Martin
 */
public class LoadFileDialog {
    
    // Owner window for file chooser.
    private final Window owner; 

    // Resource bundle for localisation.
    private final ResourceBundle bundle; 

    /**
     * Constructor. 
     * 
     * @param owner Owner window for file chooser.
     * @param bundle Resource bundle for localisation.
     */
    public LoadFileDialog(Window owner, ResourceBundle bundle) {
        this.owner = owner; 
        this.bundle = bundle; 
    }
    
    /**
     * Show the dialog and return the file data as a string. 
     * 
     * @return Encoded file string. 
     * @throws IOException If an IO error occurs. 
     */
    public String show() throws IOException {
        String data = null;

        String encoding = new EncodingSelectionDialog(bundle).show();
        FileChooser fc = new FileChooser(); 
        File file = fc.showOpenDialog(owner);

        if (file != null) {
            data = FileIO.loadFile(file.getAbsolutePath(), encoding);
        }

        return data;
    }
}
