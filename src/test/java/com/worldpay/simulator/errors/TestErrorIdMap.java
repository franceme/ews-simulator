package com.worldpay.simulator.errors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestErrorIdMap {

    private int errorValue = 1;
    private int nonErrorValue = 11;
    private ErrorIdMap errorIdMap;




    @Before
    public void setUp(){
        errorIdMap = new ErrorIdMap();

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
