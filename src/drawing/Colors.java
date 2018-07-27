package drawing;

public enum Colors {

    BLACK("Black"),
    GREY("Grey"),
    RED("Red"),
    ORANGE("Orange"),
    YELLOW("Yellow"),
    GREEN("Green"),
    BLUE("Blue"),
    PURPLE("Purple"),
    PINK("Pink");

    private String clr;

    Colors(String color) {
        clr = color;
    }

    @Override
    public String toString() { return clr; }
}
