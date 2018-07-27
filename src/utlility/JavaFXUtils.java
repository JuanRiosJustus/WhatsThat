package utlility;

import javafx.application.Platform;
import javafx.scene.control.TextArea;
import messaging.Message;
import messaging.MessageUtils;

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
}
