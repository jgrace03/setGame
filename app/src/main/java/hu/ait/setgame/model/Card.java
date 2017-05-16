package hu.ait.setgame.model;

public class Card {

    public static final short RED = 0;
    public static final short PURPLE = 1;
    public static final short GREEN = 2;

    public static final short OVAL = 0;
    public static final short SQUIGGLE = 1;
    public static final short DIAMOND = 2;

    public static final short SOLID = 0;
    public static final short STRIPED = 1;
    public static final short OUTLINED = 2;

    private short color;
    private short shape;
    private short shading;
    private short number;

    public Card(short color, short shape, short shading, short number) {
        this.color = color;
        this.shape = shape;
        this.shading = shading;
        this.number = number;
    }

    public short getColor() {
        return color;
    }

    public short getShape() {
        return shape;
    }

    public short getShading() {
        return shading;
    }

    public short getNumber() {
        return number;
    }

    public void setNumber(short number) {
        this.number = number;
    }

    public void setColor(short color) {
        this.color = color;
    }

    public void setShape(short shape) {
        this.shape = shape;
    }

    public void setShading(short shading) {
        this.shading = shading;
    }
}
