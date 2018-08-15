package com.worldpay.simulator.validator;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import javax.validation.constraints.AssertTrue;

import com.worldpay.simulator.exceptions.ClientFaultException;
import com.worldpay.simulator.validator.Validator;
import com.worldpay.simulator.validator.ValidatorUtils;

public class TestValidatorUtils {
    private String PAN1;
    private String PAN2;
    private String PAN3;
    private String PAN4;
    private String regId2;
    private String regId3;
    private String regId4;
    private String rollUpId1;
    private String rollUpId2;
    private String CVV1;
    private String CVV2;
    private String CVV3;
    private String LVT1;
    private String LVT2;
    private String token;
    private String account;



    @Before
    public void setUp(){
        PAN1 = "";
        PAN2 = "123456";
        PAN3 = "12345678912345678912345679";
        PAN4 =  "3000100011118566";
        regId2 = "-1";
        regId3 = "123465789123465798123465";
        regId4 = "123456789123465";
        rollUpId1 = "123456";
        rollUpId2 = "123456789";
        CVV1 = "123";
        CVV2 = "1234";
        CVV3 = "12";
        LVT1 = "123456789123456789";
        LVT2 = "321456789123456789";
        token = "123456789012345678901234567890123456789012345678901";
        account = "1234567890123456";

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
        assertFalse(ValidatorUtils.isValidRegId(PAN1));
        assertFalse(ValidatorUtils.isValidRegId(regId2));
        assertFalse(ValidatorUtils.isValidRegId(regId3));
        assertTrue(ValidatorUtils.isValidRegId(regId4));
    }

    @Test
    public void testIsValidRollupId(){
        assertFalse(ValidatorUtils.isValidRollupId(PAN1));
        assertTrue(ValidatorUtils.isValidRollupId(rollUpId1));
        assertFalse(ValidatorUtils.isValidRollupId(rollUpId2));
    }

    @Test
    public void testIsValidCVV(){
        assertFalse(ValidatorUtils.isValidCVV(PAN1));
        assertTrue(ValidatorUtils.isValidCVV(CVV1));
        assertTrue(ValidatorUtils.isValidCVV(CVV2));
        assertFalse(ValidatorUtils.isValidCVV(CVV3));
    }

    @Test
    public void testIsValidOrderLVT(){
        assertFalse(ValidatorUtils.isValidOrderLVT(PAN1));
        assertFalse(ValidatorUtils.isValidOrderLVT(PAN2));
        assertFalse(ValidatorUtils.isValidOrderLVT(LVT1));
        assertTrue(ValidatorUtils.isValidOrderLVT(LVT2));
    }

    @Test
    public void testIsValidToken(){
        assertFalse(ValidatorUtils.isValidToken(PAN1));
        assertFalse(ValidatorUtils.isValidToken(token));
        assertTrue(ValidatorUtils.isValidToken(PAN2));
    }

    @Test
    public void testIsValidAccount(){
        assertFalse(ValidatorUtils.isValidAccount(PAN1));
        assertFalse(ValidatorUtils.isValidAccount(LVT1));
        assertFalse(ValidatorUtils.isValidAccount(CVV1));
        assertTrue(ValidatorUtils.isValidAccount(account));
    }

    @Test
    public void testIsValidRoutingNumber(){
        assertFalse(ValidatorUtils.isValidRoutingNumber(PAN1));
        assertTrue(ValidatorUtils.isValidRoutingNumber(rollUpId2));
        assertFalse(ValidatorUtils.isValidRoutingNumber(PAN2));
    }

    @Test
    public void testIsValidMerchantRefId(){
        assertTrue(ValidatorUtils.isValidMerchantRefId(null));
        assertTrue(ValidatorUtils.isValidMerchantRefId(PAN1));
        assertTrue(ValidatorUtils.isValidMerchantRefId(token));
        assertFalse(ValidatorUtils.isValidMerchantRefId(PAN2));
    }

    @Test
    public void testIsValidExpiryDate(){
        assertTrue(ValidatorUtils.isValidExpiryDate(CVV2));
        assertFalse(ValidatorUtils.isValidExpiryDate(PAN1));
        assertFalse(ValidatorUtils.isValidExpiryDate(PAN2));
    }

    @Test(expected = ClientFaultException.class)
    public void testHandleException_throw_exception(){
        ValidatorUtils.handleException(4,"Invalid request (syntax error).");
    }

}
