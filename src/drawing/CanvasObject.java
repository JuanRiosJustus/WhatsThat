package drawing;

import dialogs.Constants;

public class CanvasObject {
    // There will be 3 canvas objects which sends an object that
    // will represent the point and the item at that point
    // if canvas object is a picture, sends picture at reduced size at point
    // if canvas object is color, sends point with a color
    // if canvas object is text, sends point, color, text
    //

    private String data;
    private Point point;
    private boolean mutable;

    public CanvasObject() { mutable = true; }

    public CanvasObject(Point pt, String dta) {
        point = pt;
        data = dta;
        mutable = false;
    }

    public CanvasObject(Point pt, String dta, boolean mutble) {
        point = pt;
        data = dta;
        mutable = mutble;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        if (mutable == false) { return; }
        this.data = data;
    }

    public void setPoint(Point point) {
        if (mutable == false) { return; }
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public boolean isMutable() {
        return mutable;
    }

    public boolean isConsumable() {
        if (data.equalsIgnoreCase(Constants.EMPTY_STRING) || data.equalsIgnoreCase("null")) {
            return false;
        }
        return true;
    }
    @Override
    public String toString() {
        return "(" + point.toString() + Constants.CANVAS_OBJECT_DELIMITER + "[" + data + "])";
    }
}
