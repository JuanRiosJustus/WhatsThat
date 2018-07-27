package drawing;

public class Point {
	
	private double x;
	private double y;
	private String color;
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
		color = "Black";
	}

	public Point(double x, double y, String clr) {
		this.x = x;
		this.y = y;
		color = clr;
	}
	
	public double getX() { return x; }
	public double getY() { return y; }
	public String getColor() { return color; }
	public void normalize() { x = (int)x; y = (int) y; }

    public boolean isEqualTo(Point p) {
	    normalize();
	    p.normalize();
	    if (p.getY() == y && p.getX() == x) {
	        return true;
        } else {
	        return false;
        }
    }

	@Override
	public int hashCode() {
		return Integer.valueOf(String.valueOf((int)x) + String.valueOf((int)y));
	}
	@Override
	public String toString() {

	    return "[" + ((int)x) + "," + ((int)y) + "," + color + "]";
    }
}
