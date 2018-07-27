package dialogs;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.web.WebView;

public class MainComponent {

    private static Canvas canvasInstance;
    private static WebView browserInstance;

    /**
     * returns the main canvas instance
     */
    public static Canvas getCanvasInstance() {
        if (canvasInstance == null) {
            canvasInstance = constructCanvasComponent();
        }
        return canvasInstance;
    }

    /**
     * Returns the main browser instance
     */
    public static WebView getBrowserInstance() {
        if (browserInstance == null) {
            browserInstance = constructBrowerInstance();
        }
        return browserInstance;
    }

    /**
     * Constructs the main graphics component for the client.
     * @return the graphical component.
     */
    private static Canvas constructCanvasComponent() {
        Canvas tableCanvas = new Canvas(Constants.UI_CANVAS_WIDTH, Constants.UI_CANVAS_HEIGHT);
        tableCanvas.getGraphicsContext2D().setFill(Paint.valueOf("White"));
        tableCanvas.getGraphicsContext2D().fillRect(0, 0, Constants.UI_CANVAS_WIDTH, Constants.UI_CANVAS_HEIGHT);
        return tableCanvas;
    }

    /**
     * Constructs the main browser instance view
     * @return the webview instance
     */
    private static WebView constructBrowerInstance() {
        WebView browser = new WebView();
        browser.setMaxWidth(Constants.UI_CANVAS_WIDTH);
        browser.setMinWidth(Constants.UI_CANVAS_WIDTH);
        browser.setPrefWidth(Constants.UI_CANVAS_WIDTH);
        browser.setMaxHeight(Constants.UI_CANVAS_HEIGHT);
        browser.setMinHeight(Constants.UI_CANVAS_HEIGHT);
        browser.prefHeight(Constants.UI_CANVAS_HEIGHT);

        return browser;
    }

    /**
     * Sets the canvas instance to white / default
     */
    public static void clearCanvas() {
        canvasInstance.getGraphicsContext2D().setFill(Paint.valueOf("White"));
        canvasInstance.getGraphicsContext2D().fillRect(0, 0, Constants.UI_CANVAS_WIDTH, Constants.UI_CANVAS_HEIGHT);
    }

    public static GraphicsContext getContext() { return canvasInstance.getGraphicsContext2D(); }

}
