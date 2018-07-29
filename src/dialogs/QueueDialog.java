package dialogs;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import utlility.JavaFXUtils;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.util.LinkedList;
import java.util.Queue;

public class QueueDialog {

    private Queue<String> queue;
    private ImageView img;
    private Stage currentStage;
    private boolean suspended = true;

    public void createImagesDialog(TextArea ta, String url) {
        if (url == null || url.length() < 1) { return; }
        // if currentStage != null, then the current stage is open
        if (currentStage != null) {
            currentStage.close();
            currentStage = null;
        }
        // create the stage, and get url
        try {
            // try to ge the url
            Document doc = Jsoup.connect(url).get();
            currentStage = JavaFXUtils.constructPartialStage(doc.title(), true);
            Elements ele = doc.getElementsByTag("img");
            queue = new LinkedList<>();
            // add all elements to the queue
            for (int i = 0; i < ele.size(); i++) { queue.add(ele.get(i).absUrl("src")); }
            currentStage.setScene(constructImageViewScene(ta));
            currentStage.show();
        } catch (Exception ex) {
            // idk
            currentStage = null;
            return;
        }
    }

    /**
     * Constructs an image queue that allows user to traverse
     * all images in the queue.
     * @param ta
     * @return
     */
    private Scene constructImageViewScene(TextArea ta) {
        FlowPane fp = new FlowPane();
        // button to pop
        Button btn = JavaFXUtils.createButton("Dequeue", 2);
        Button btn1 = JavaFXUtils.createButton("Enqueued: " + queue.size(), 2);
        // adds the current url to the system clipboard
        Button btn2 = JavaFXUtils.createButton("Details", 2);
        btn2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (img == null) { return; }
                StringSelection sel = new StringSelection(img.getImage().getUrl());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(sel, null);
                btn2.setText("Img copied");
            }
        });
        // if suspended, the current image will not be removed from the dialog
        // when applying to the canvas.
        Button btn3 = JavaFXUtils.createButton("Suspended", 2);
        btn3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (suspended) {
                    suspended = false;
                    btn3.setText("Unsuspended");
                } else {
                    suspended = true;
                }
            }
        });
        // constructs the queue behavior
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (queue.isEmpty()) { return; }
                if (queue.peek().equalsIgnoreCase("")) {
                    queue.poll();
                    return;
                }
                img = new ImageView(queue.poll());

                if (fp.getChildren().size() < 5) {
                    fp.getChildren().add(img);
                } else {
                    fp.getChildren().set(4, img);
                }
                btn1.setText("Enqueued: " + queue.size());
                btn2.setText("W: " + img.getImage().getWidth() + " H: " + img.getImage().getHeight());
            }
        });
        fp.getChildren().add(btn);
        fp.getChildren().add(btn1);
        fp.getChildren().add(btn2);
        fp.getChildren().add(btn3);
        return new Scene(fp, Constants.UI_PROGRAM_WIDTH / 2, Constants.UI_PROGRAM_HEIGHT / 2);
    }

    public boolean hasImage() { return img != null; }
    public String extractImage() {
        if (img == null) { return Constants.EMPTY_STRING; }
        String ret = img.getImage().getUrl();
        if (suspended == false) {
            img = null;
        }
        return ret;
    }
}
