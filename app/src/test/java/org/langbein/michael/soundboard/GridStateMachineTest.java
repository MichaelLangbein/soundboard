package org.langbein.michael.soundboard;

import org.junit.Test;
import static org.junit.Assert.*;
import org.langbein.michael.soundboard.utils.GridStateMachine;
import org.langbein.michael.soundboard.utils.Vec2;

/**
 * Created by michael on 28.01.18.
 */

public class GridStateMachineTest {


    @Test
    public void testStateMachine() {
        GridStateMachine gsm = new GridStateMachine();
        for(int k = 0; k < 49; k++) {
            Vec2<Integer> pos = gsm.getCurrentPos();
            gsm.oneStepFurther();
            System.out.println("At index " + k + " we got row " + pos.x + " and col " + pos.y);
        }
        assertTrue(true);
    }
}
