package texteditor;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

import texteditor.api.TextEditorAPI;
import texteditor.api.TextEditorPlugin;

/**
 * Date plugin.
 * @author Ryan Martin
 */
public class DatePlugin implements TextEditorPlugin {

    /**
     * API start. 
     */
    @Override
    public void start(TextEditorAPI api) {
        // Register the date button. 
        api.registerButton("Date", () -> {
            Locale locale = api.getLocale();

            ZonedDateTime dateTime = ZonedDateTime.now(); 
            DateTimeFormatter formatter = DateTimeFormatter
                .ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM)
                .withLocale(locale);

            api.insertText(dateTime.format(formatter));
        }); 
    } 

    /**
     * Localised plugin name. 
     */
    @Override
    public String getPluginName(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("dateplugin", locale);
        return bundle.getString("name"); 
    }
}