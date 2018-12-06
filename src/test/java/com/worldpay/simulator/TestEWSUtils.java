package com.worldpay.simulator;

import com.worldpay.simulator.exceptions.ServerFaultException;
import com.worldpay.simulator.exceptions.ClientFaultException;
import com.worldpay.simulator.utils.EWSUtils;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TestEWSUtils {
  private String reqId1;
  private String reqId2;
  private String PAN1;
  private String PAN2;
  private String PAN3;
  private String regId1;
  private String regId2;
  private String regId3;
  private String property1;
  private String property2;
  private String property3;
  private String invalidProperty;
  private String defaultPAN;
  private String defaultRegId;
  private String cvv;
  private String token;
  private AccountType CHECKING;
  private AccountType SAVINGS;
  private AccountType CORPORATE_CHECKING;
  private AccountType CORPORATE_SAVINGS;
  private VError error1;



  @Before
  public void setUp(){
    PAN1 = "123513521231";
    PAN2 = "251";
    PAN3 = "7123513521231";
    regId1 = "3153219999999471321";
    regId2 = "251";
    regId3 = "1532179999996471321";
    property1 = "123513251231";
    property2 = "251";
    property3 = "7123512531231";
    invalidProperty = "asd21132";
    defaultPAN = "3000109999988888566";
    defaultRegId = "0100030011116658";
    cvv = "768";
    token = "1252645696785667688";
    SAVINGS = AccountType.SAVINGS;
    CHECKING = AccountType.CHECKING;
    CORPORATE_CHECKING = AccountType.CORPORATE_CHECKING;
    CORPORATE_SAVINGS = AccountType.CORPORATE_SAVINGS;
    error1 = new VError();
    error1.setId(4);
    error1.setCode("INVALID_REQUEST");
    error1.setMessage("Invalid request (syntax error).");
    EWSUtils tempUtils = new EWSUtils();
  }




  @Test
  public void testRandomReqId(){
    reqId1 = EWSUtils.randomReqId();
    reqId2 = EWSUtils.randomReqId();
    assertFalse(reqId1.equals(reqId2));
  }

  @Test
  public void testGetRegId(){
    String temp = EWSUtils.getRegIdFromPAN(PAN1);
    assertEquals(regId1,temp);
    temp = EWSUtils.getRegIdFromPAN(PAN3);
    assertEquals(regId3,temp);
  }
  @Test
  public void testGetRegIdFromToken(){
    assertEquals("4625213341230348867",EWSUtils.getRegIdFromToken(token));
  }

  @Test
  public void testGenerateProperty(){
    String temp = EWSUtils.generateProperty(PAN3);
    assertEquals(property3,temp);
    temp = EWSUtils.generateProperty(PAN2);
    assertEquals(PAN2,temp);
  }

  @Test(expected = ClientFaultException.class)
  public void testGenerateProperty_with_exception(){
    EWSUtils.generateProperty("asdas");
  }

  @Test
  public void testGetToken(){
    String temp = EWSUtils.getPANToken(PAN3);
    assertEquals(property3,temp);
    temp = EWSUtils.getPANToken(PAN2);
    assertEquals(property2,temp);


  }

  @Test
  public void testGetPAN(){
    String temp = EWSUtils.getPAN(property3);
    assertEquals(PAN3,temp);
    temp = EWSUtils.getPAN(property2);
    assertEquals("3000100011118566",temp);
  }

  @Test
  public void testGenerateRandomNumber(){
    String temp1 = EWSUtils.generateRandomNumber(5);
    String temp2 = EWSUtils.generateRandomNumber(5);
    assertNotEquals(temp1,temp2);
  }

  @Test(expected = Exception.class)
  public void handleDesiredExceptions_throwException(){
    EWSUtils.handleDesiredExceptions("10033");
  }

  @Test
  public void handleDesiredExceptions_throwException_lastThreeAreNotNumber(){
    EWSUtils.handleDesiredExceptions("1003asd");
  }

  @Test
  public void handleDesiredExceptions_noException_inputTooShort(){
    EWSUtils.handleDesiredExceptions("13");
  }

  @Test
  public void handleDesiredExceptions_NoException(){
    EWSUtils.handleDesiredExceptions("1313131");
  }

  @Test(expected = ServerFaultException.class)
  public void testThrowDesiredException_ServerFaultException_1(){
    EWSUtils.throwDesiredException(1);
  }

  @Test(expected = ServerFaultException.class)
  public void testThrowDesiredException_ServerFaultException_2(){
    EWSUtils.throwDesiredException(2);
  }

  @Test(expected = ServerFaultException.class)
  public void testThrowDesiredException_ServerFaultException_3(){
    EWSUtils.throwDesiredException(3);
  }

  @Test(expected = ServerFaultException.class)
  public void testThrowDesiredException_ServerFaultException_8(){
    EWSUtils.throwDesiredException(8);
  }

  @Test(expected = ClientFaultException.class)
  public void testThrowDesiredException_ClientFaultException(){
    EWSUtils.throwDesiredException(7);
  }

  @Test
  public void testIsClientFaultError(){
    assertTrue(EWSUtils.isClientFaultError(4));
    assertFalse(EWSUtils.isClientFaultError(2));
    assertFalse(EWSUtils.isClientFaultError(99));
  }

  @Test
  public void testGetPANThroughRegId(){
    String temp = EWSUtils.getPANThroughRegId(regId1);
    assertEquals(PAN1,temp);
    temp = EWSUtils.getPANThroughRegId(regId3);
    assertEquals(PAN3,temp);
    assertEquals(defaultPAN,EWSUtils.getPANThroughRegId(defaultRegId));
  }

  @Test
  public void testGetCVVThroughToken(){
    assertEquals(cvv,EWSUtils.getCVVThroughToken(token));
    assertEquals("566",EWSUtils.getCVVThroughToken("12"));
  }

  @Test
  public void testGetAccountType(){
    assertEquals(CORPORATE_CHECKING,EWSUtils.getAccountType("120"));
    assertEquals(CORPORATE_CHECKING,EWSUtils.getAccountType("121"));
    assertEquals(CORPORATE_CHECKING,EWSUtils.getAccountType("122"));
    assertEquals(CORPORATE_CHECKING,EWSUtils.getAccountType("123"));
  }

  @Test
  public void testGetRoutingNumber(){
    assertEquals("300010999",EWSUtils.getRoutingNumber(defaultPAN));
    assertEquals("123456789",EWSUtils.getRoutingNumber("123"));
  }

  @Test
  public void testDecrypt(){
    assertEquals("132125315321",EWSUtils.decrypt(PAN1));
  }

  @Test
  public void testGenerateEcheckToken(){
      assertEquals("2876543210987654322",EWSUtils.generateEcheckToken("12345678901234567",AccountType.CORPORATE_CHECKING));
      assertEquals("87654321098765432",EWSUtils.generateEcheckToken("12345678901234567"));
  }

  @Test
  public void testGenerateEcheckAccount(){
      assertEquals("12345678901234567",EWSUtils.generateEcheckAccount("2876543210987654322"));
  }

  @Test
  public void testGetError(){
    assertNull(EWSUtils.getError("0011"));
    VError tempError = EWSUtils.getError("0044");
    assertEquals(error1.getId(),tempError.getId());
    assertEquals(error1.getCode(),tempError.getCode());
    assertEquals(error1.getMessage(),tempError.getMessage());

  }

}
