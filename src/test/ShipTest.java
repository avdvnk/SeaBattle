package test;

import model.Field;
import model.Ship;
import org.junit.Assert;
import org.junit.Test;

public class ShipTest {
    private Ship ship;
    private Field field;
    @Test
    public void testShip (){
        field = new Field(10,10,10);
        ship = new Ship(field, 4);
        Assert.assertTrue(ship.getSize() == 4);
        Assert.assertNotNull(ship);
        Assert.assertTrue(ship.getState() == 1);
    }
}
