package test;

import model.Cell;
import org.junit.Assert;
import org.junit.Test;

public class CellTest {
    private Cell cell = new Cell(0,0);
    @Test
    public void test(){
        cell.setState(Cell.CELL_INJURED);
        Assert.assertTrue(cell.getState() == Cell.CELL_INJURED);
        cell.setMark(true);
        Assert.assertTrue(cell.isMark());
    }
}
