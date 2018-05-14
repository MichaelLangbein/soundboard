package org.langbein.michael.soundboard;


import org.junit.Test;
import org.langbein.michael.soundboard.utils.ArrayUtils;
import static org.junit.Assert.assertTrue;

public class ArrayUtilsTest {

    @Test
    public void testHighestN () {
        double[] data = new double[5];
        data[0] = 1.1;
        data[1] = 2.1;
        data[2] = - 3.1;
        data[3] = 4.1;
        data[4] = 1.5;

        int[] indices = ArrayUtils.highestN(data, 3);

        assertTrue("Highest index isn't 3", indices[0] == 3);
        assertTrue("Second - highest index isn't 1",indices[1] == 1);
        assertTrue("Third - highest index isn't 4",indices[2] == 4);
    }
}
