package drawing;

import dialogs.Constants;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import utlility.NetworkUtils;

import java.util.LinkedList;
import java.util.Queue;


public class CanvasInterpreter {

    private Point lastUserCreatedPoint;
    private GraphicsContext canvas;
    private String color;
    private StringBuilder builder;
    private Queue<Point> pointQueue;
    private CanvasObject mutableCanvasObject;
    private CanvasObject mediaObject;

    public CanvasInterpreter(GraphicsContext context) {
        canvas = context;
        color = "Black";
        pointQueue = new LinkedList<>();
        mutableCanvasObject = new CanvasObject();
        builder = new StringBuilder();
        mediaObject = new CanvasObject();
    }

    /**
     * Given a new point and an old point, draw all canvasObjects between
     * @param  canvas
     * @param p
     */
    private void draw(GraphicsContext canvas, Point p) {
        if (lastUserCreatedPoint == null) {
            lastUserCreatedPoint = p;
            pointQueue.add(p);
            canvas.fillOval(p.getX(), p.getY(), Constants.PEN_SIZE, Constants.PEN_SIZE);
        } else {
            if (getDifferenceBetweenPoints(p, lastUserCreatedPoint) > 10) {
                lastUserCreatedPoint = p;
                pointQueue.add(p);
            }
            //  canvasObjects between the previous and current canvasObjects
            Point temp = p;
            while (temp.isEqualTo(lastUserCreatedPoint) == false) {
                canvas.fillOval(temp.getX(), temp.getY(), Constants.PEN_SIZE, Constants.PEN_SIZE);
                temp = nextPointTo(temp, lastUserCreatedPoint);
                pointQueue.add(temp);
            }
            lastUserCreatedPoint = p;
        }
    }

    /**
     * Given a point, and a point to get to,
     * construct the next node to in the path to get to the desired point
     * @param from starting point
     * @param to ending point
     * @return the next point in the path to get to the next point.
     */
    private Point nextPointTo(Point from, Point to) {
        double newX = from.getX();
        double newY = from.getY();
        if (from.getY() > to.getY()) { newY--; }
        if (from.getY() < to.getY()) { newY++; }
        if (from.getX() < to.getX()) { newX++; }
        if (from.getX() > to.getX()) { newX--; }
        return new Point(newX, newY, from.getColor());
    }

    /**
     * Retrieves the difference between two canvasObjects
     */
    private double getDifferenceBetweenPoints(Point p1, Point p2) {
        double diffX = Math.pow(p1.getX() - p2.getX(), 2);
        double diffY = Math.pow(p1.getY() - p2.getY(), 2);
        return Math.sqrt(diffX + diffY);
    }

    /**
     * Handles all the events for mouse clicks to the canvas.
     * @param me the current mouse event occurring
     * @param inputField the field the user writes to
     */
    public void handleEvent(MouseEvent me, TextField inputField, String color) {
        canvas.setFill(Paint.valueOf(color));
        Point p = new Point(me.getSceneX(), me.getSceneY(), color);

        if (me.isPrimaryButtonDown()) {
            // draw a dots
            draw(canvas, p);
            //g.fillOval(p.getX(), p.getY(), Constants.PEN_SIZE, Constants.PEN_SIZE);
        } else if (me.isSecondaryButtonDown()) {
            // write text or image to screen
            mediaObject = new CanvasObject(p, inputField.getText());
            if (NetworkUtils.isValidImageUrl(mediaObject.getData())) {
                canvas.fillText(inputField.getText(), p.getX(), p.getY());
            }
            inputField.clear();
        }
    }
    public String constructStringFromCanvasObjects() {
        mutableCanvasObject.setData("null");
        while(pointQueue.isEmpty() == false) {
            mutableCanvasObject.setPoint(pointQueue.poll());
            builder.append(mutableCanvasObject.toString());
        }
        String ret = builder.toString();
        builder.setLength(0);
        return ret;
    }

    public String constructConsumableCanvasObject() {
        if (mediaObject == null || mediaObject.getData() == null || mediaObject.getData().length() < 1) {
            return "";
        } else {
            String ret = mediaObject.toString();
            mediaObject = null;
            return ret;
        }
    }
    public void SetColor(String clr) { color = clr; }
    public int points() { return pointQueue.size(); }
}
