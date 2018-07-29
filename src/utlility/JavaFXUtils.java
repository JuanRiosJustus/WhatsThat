package utlility;

import dialogs.Constants;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import messaging.Message;
import messaging.MessageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Queue;

public class JavaFXUtils {

    protected JavaFXUtils() { }

    /**
     * Given a textarea component and a string to be appended to the text area component
     * along with a new line character, safely add the appended items
     * to the given textarea.
     * @param area
     * @param str
     */
    public static void threadSafeAppendToTextArea(TextArea area, String str) {
        if (area == null || str == null) { return; }
        Platform.runLater( () -> area.appendText(str + "\n") );
    }

    /**
     * Given a textarea component and a message to be appended to the text area component
     * along with a new line character, safely add the appended items
     * to the given textarea.
     * @param area
     * @param msg
     */
    public static void threadSafeAppendToTextAreaForClient(TextArea area, Message msg) {
        if (area == null || msg == null) { return; }
        Platform.runLater( () -> area.appendText(MessageUtils.readableMessageFormatForClient(msg) + "\n") );
    }

    /**
     * Given a textarea component and a message to be appended to the text area component
     * along with a new line character, safely add the appended items
     * to the given textarea.
     * @param area
     * @param msg
     */
    public static void threadSafeAppendToTextAreaForServer(TextArea area, Message msg) {
        if (area == null || msg == null) { return; }
        Platform.runLater( () -> area.appendText(MessageUtils.readableMessageFormatForServer(msg) + "\n") );
    }

    /**
     * Given an array of strings denoting the available extensions, and the path
     * of the file to save the image on, and the canvas to receve the image from,
     * Save the file.
     * @param canv
     * @param extensions
     * @param path
     */
    public static void saveCanvasDialog(Canvas canv, String[] extensions, String path) {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(path));
        for (int i = 0; i < extensions.length; i++) {
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter(extensions[i], "*." + extensions[i].toLowerCase()));
        }
        fc.setTitle(Constants.SAVE_BUTTON_TEXT);
        File file = fc.showSaveDialog(new Stage());
        if(file != null){
            WritableImage wi = new WritableImage((int)Constants.UI_CANVAS_WIDTH,(int)Constants.UI_CANVAS_HEIGHT);
            SnapshotParameters sp;
            if (fc.getSelectedExtensionFilter().getDescription().equalsIgnoreCase("PNG")) {
                sp = new SnapshotParameters();
                sp.setFill(Color.TRANSPARENT);
            } else {
                sp = new SnapshotParameters();
            }
            BufferedImage bi = SwingFXUtils.fromFXImage(canv.snapshot(sp,wi),null);
            try {
                ImageIO.write(bi, fc.getSelectedExtensionFilter().getDescription().toLowerCase(), file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Constructs a partial stage for temporary docking
     * @param title
     * @return
     */
    public static Stage constructPartialStage(String title, boolean resizeable) {
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setWidth(Constants.UI_PROGRAM_WIDTH / 2);
        stage.setMaxWidth(Constants.UI_PROGRAM_WIDTH / 2);
        stage.setHeight(Constants.UI_PROGRAM_HEIGHT / 2);
        stage.setMaxHeight(Constants.UI_PROGRAM_HEIGHT / 2);
        return stage;
    }

    /**
     * Constructs a button
     * @param name
     * @param order
     * @return
     */
    public static Button createButton(String name, int order) {
        Button btn = new Button(name);
        btn.setMaxWidth(Constants.UI_LESSER_COMP_SIZE / order);
        btn.setMinWidth(Constants.UI_LESSER_COMP_SIZE / order);
        btn.setPrefWidth(Constants.UI_LESSER_COMP_SIZE / order);
        return btn;
    }
}
