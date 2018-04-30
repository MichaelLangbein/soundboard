package org.langbein.michael.soundboard;

import org.junit.Test;
import org.langbein.michael.soundboard.utils.Complex;
import org.langbein.michael.soundboard.utils.FFT;

import static org.junit.Assert.assertTrue;

public class FFTTest {

    @Test
    public void testFft() {
        Complex[] data = new Complex[4];
        data[0] = new Complex(0, 0);
        data[1] = new Complex(1, 0);
        data[2] = new Complex(2, 0);
        data[3] = new Complex(3, 0);

        Complex[] result = FFT.fft(data);

        Complex[] expectedResult = new Complex[4];
        expectedResult[0] = new Complex(6, 0);
        expectedResult[1] = new Complex(-2, 2);
        expectedResult[2] = new Complex(-2, 0);
        expectedResult[3] = new Complex(-2, -2);


        for(int i = 0; i<4; i++){
            assertTrue("Hm. Etwas stimmt nicht. " + result[i] + "!=" + expectedResult[i], Math.abs(result[i].getReal()  - expectedResult[i].getReal()) < 0.5 );
            assertTrue("Hm. Etwas stimmt nicht. " + result[i] + "!=" + expectedResult[i], Math.abs(result[i].getCompl() - expectedResult[i].getCompl()) < 0.5 );
        }

    }
}
