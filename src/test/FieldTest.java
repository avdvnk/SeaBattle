package test;

import model.Field;
import org.junit.Assert;
import org.junit.Test;

public class FieldTest {
    private Field field = new Field(10,10,10);
    @Test
    public void testField (){
        field.setShip();
        Assert.assertTrue(field.getNumLiveShips() == 10);
        Assert.assertTrue(field.getMaxShip() == 10);
        Assert.assertNotNull(field.getCell(4,7));
    }
}
