package com.worldpay.simulator.errors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestErrorIdMap {

    private int errorValue = 1;
    private int nonErrorValue = 11;




    @Before
    public void setUp(){

    }

    @Test
    public void testGetError(){


        assertNotNull(ErrorIdMap.getError(errorValue));
        Assert.assertNull(ErrorIdMap.getError(nonErrorValue));

        assertEquals(true, ErrorIdMap.getError(errorValue) instanceof EWSError);
    }

    @Test
    public void testContainsError(){

        assertTrue(ErrorIdMap.containsErrorId(errorValue));
        assertFalse(ErrorIdMap.containsErrorId(nonErrorValue));

    }
}
