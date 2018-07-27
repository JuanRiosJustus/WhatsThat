package drawing;

import dialogs.Constants;
import utlility.StringUtils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class CanvasObjectInterpreter {

    /**
     * Given a string that is a represents a point
     * such as ([5,34,Green]~[someString]) returns the object representing
     * the point with the given value.
     * @param str the string that represents the point to create
     * @return a new point representing the given string value
     */
    public CanvasObject extractCanvasObject(String str) {
        StringBuilder sb = new StringBuilder(str);
        String[] conts = str.substring(sb.indexOf("[") + 1, sb.indexOf("]")).split(",");
        int x = Integer.valueOf(conts[0]);//Integer.valueOf(sb.substring(2, sb.indexOf(",")));
        int y = Integer.valueOf(conts[1]);//Integer.valueOf(sb.substring(sb.indexOf(",") + 1, sb.lastIndexOf(",")));
        String color = conts[2];//sb.substring(sb.lastIndexOf(",") + 1, sb.indexOf("]"));
        Point p = new Point(x, y, color);
        String contents = sb.substring(sb.lastIndexOf("[") + 1, sb.lastIndexOf("]"));
        return new CanvasObject(p, contents);
    }

    /**
     * Extracts all the given canvasObjects from the string
     * add everything to stack untill closed brack is retrieved.
     * @param str
     * @return
     */
    public CanvasObject[] extractCanvasObjects(String str) {
        StringBuilder sb = new StringBuilder(str);
        StringBuilder result = new StringBuilder();
        CanvasObject[] objs = new CanvasObject[StringUtils.numberOf(str, '~')];
        int i = 0;
        // find canvasObjects and then the inner info
        while (sb.length() > 0) {
            // get the whole canvas object, which is simply the second occurence of ')'
            objs[i] = extractCanvasObject(sb.substring(0, sb.indexOf(")")));
            i++;
            sb.delete(0, sb.indexOf(")") + 1);
            // get the text of the point
        }
        return objs;
    }

    public boolean isOnlyForDrawing(CanvasObject ca) {
        return ca.isConsumable();
    }
}
