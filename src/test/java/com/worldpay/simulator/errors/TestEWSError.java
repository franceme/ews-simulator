package com.worldpay.simulator.errors;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestEWSError {

    private EWSError ewsError;
    private String errCode;
    private String errMessage;

    @Before
    public void setUp(){
        errCode = "1";
        errMessage = "error";
        ewsError = new EWSError(errCode,errMessage);
    }

    @Test
    public void testGetErrorCode(){
        assertEquals(errCode,ewsError.getErrorCode());
    }

    @Test
    public void testGetErrorMessage(){
        assertEquals(errMessage,ewsError.getErrorMessage());
    }
}
