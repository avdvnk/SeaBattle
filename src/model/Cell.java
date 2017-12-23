package model;

import java.io.Serializable;

public class Cell implements Serializable{
    public final static int CELL_WATER = 1;
    public final static int CELL_BORDER = 2;
    public final static int CELL_WELL = 3;
    public final static int CELL_INJURED = 4;
    public final static int CELL_KILLED = 5;
    public final static int CELL_MISSED = 6;
    public int x;
    public int y;
    private int state;
    private Ship ship;
    private boolean mark;
    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        this.state = CELL_WATER;
        this.mark = false;
    }
    public void setState(int state) {
        this.state = state;
    }
    public int getState() {
        return state;
    }
    public boolean isMark() {
        return mark;
    }
    public void setMark(boolean mark) {
        this.mark = mark;
    }
    public Ship getShip() {
        return ship;
    }
    public void setShip(Ship ship) {
        this.ship = ship;
    }
    public int doShot() {
        setMark(true);
        if (state == CELL_WELL) {
            setState(CELL_INJURED);
            return ship.doShot();
        } else {
            if ( (state == CELL_BORDER) || (state == CELL_WATER)) {
                setState(CELL_MISSED);
            }
        }
        return Field.SHUT_MISSED;
    }
}