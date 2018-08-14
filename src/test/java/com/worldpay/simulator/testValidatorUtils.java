package com.worldpay.simulator;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.validation.constraints.AssertTrue;

import com.worldpay.simulator.validator.Validator;
import com.worldpay.simulator.validator.ValidatorUtils;

public class testValidatorUtils {
    private String PAN1;
    private String PAN2;
    private String PAN3;
    private String PAN4;
    private String regId1;
    private String regId2;
    private String regId3;
    private String regId4;



    @Before
    public void setUp(){
        PAN1 = "";
        PAN2 = "123456";
        PAN3 = "12345678912345678912345679";
        PAN4 =  "3000100011118566";
        regId1 = "";
        regId2 = "-1";
        regId3 = "123465789123465798123465";
        regId4 = "123456789123465";

    }




    @Test
    public void testIsStringEmpty(){
        assertTrue(ValidatorUtils.isStringEmpty(null));
        assertTrue(ValidatorUtils.isStringEmpty(""));
        assertFalse(ValidatorUtils.isStringEmpty("?"));
        assertFalse(ValidatorUtils.isStringEmpty("12"));
    }

    @Test
    public void testIsStringValidInteger(){
        assertFalse(ValidatorUtils.isStringValidInteger(""));
        assertFalse(ValidatorUtils.isStringValidInteger("12as"));
        assertTrue(ValidatorUtils.isStringValidInteger("12"));
        assertFalse(ValidatorUtils.isStringValidInteger("12?12"));
    }

    @Test
    public void testIsValidPAN(){
        assertFalse(ValidatorUtils.isValidPAN(PAN1));
        assertFalse(ValidatorUtils.isValidPAN(PAN2));
        assertFalse(ValidatorUtils.isValidPAN(PAN3));
        assertTrue(ValidatorUtils.isValidPAN(PAN4));
    }

    @Test
    public void testIsValidRegId(){
        assertFalse(ValidatorUtils.isValidRegId(regId1));
        assertFalse(ValidatorUtils.isValidRegId(regId2));
        assertFalse(ValidatorUtils.isValidRegId(regId3));
        assertTrue(ValidatorUtils.isValidRegId(regId4));
    }

    @Test
    public void testIsValidRollupId(){

    }

    @Test
    public void testIsValidCVV(){

    }

    @Test
    public void testIsValidOrderLVT(){

    }

    @Test
    public void testIsValidToken(){

    }

    @Test
    public void testIsValidAccount(){

    }

    @Test
    public void testIsValidRoutingNumber(){

    }

    @Test
    public void testIsValidMerchantRefId(){

    }

    @Test
    public void testIsValidExpiryDate(){

    }

    @Test
    public void testHandleException(){

    }
}
