package zendo.games.zenlib.utils;

public class Point {

    public int x;
    public int y;

    // must be public for Json deserialization
    public Point() {
        this(0, 0);
    }

    private Point(int x, int y) {
        set(x, y);
    }

    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Point zero() {
        return new Point();
    }

    public static Point at(int x, int y) {
        return new Point(x, y);
    }

}
