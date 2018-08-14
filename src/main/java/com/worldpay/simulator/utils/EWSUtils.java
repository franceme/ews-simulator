package com.worldpay.simulator.utils;

import static com.worldpay.simulator.validator.ValidatorUtils.isValidAccount;
import static com.worldpay.simulator.validator.ValidatorUtils.isValidPAN;
import static com.worldpay.simulator.validator.ValidatorUtils.isValidToken;
import static java.lang.Thread.sleep;

import java.util.Random;
import java.util.UUID;

import com.worldpay.simulator.AccountType;
import com.worldpay.simulator.errors.EWSError;
import com.worldpay.simulator.errors.ErrorIdMap;
import com.worldpay.simulator.VError;
import com.worldpay.simulator.exceptions.ClientFaultException;
import com.worldpay.simulator.exceptions.ServerFaultException;

public class EWSUtils {

    public static String randomReqId() {
        return UUID.randomUUID().toString();
    }

    public static String getRegIdFromPAN(String input) {
        int lengthInput = input.length();

        if(lengthInput > 3) {
            StringBuilder sb = new StringBuilder(input.substring(0, lengthInput - 4));
            StringBuilder lastFour = new StringBuilder(input.substring(lengthInput - 4));
            return sb.append(lastFour.reverse().toString()).toString();
        } else {
            return input;
        }
    }


    public static String getRegIdFromToken(String input) {
        int lengthInput = input.length();

        if(lengthInput > 3) {
            StringBuilder sb = new StringBuilder(input.substring(0, lengthInput - 4));
            StringBuilder lastFour = new StringBuilder(input.substring(lengthInput - 4));
            return sb.reverse().append(lastFour.reverse().toString()).toString();
        } else {
            return input;
        }
    }


    public static String generateProperty(String input) {

        int lengthInput = input.length();

        try{
            Long temp = Long.parseLong(input);
        }catch (Exception e){
            throw new NumberFormatException();
        }

        if(lengthInput > 3) {
            StringBuffer sb = new StringBuffer(input.substring(0, lengthInput - 4));
            String lastFour = input.substring(lengthInput - 4);
            return sb.reverse().append(lastFour).toString();
        } else {
            return input;
        }
    }

    public static String getToken(String primaryAccountNumber) {
        return generateProperty(primaryAccountNumber);
    }

    public static String getPAN(String token) {

        if(!isValidPAN(generateProperty(token)))
            return "3000100011118566";
        return generateProperty(token);
    }

    public static String getAccountNumber(String token){
        if(!isValidAccount(getPAN(token)))
            return "3000100011118566";
        return getPAN(token);
    }


    public static void delayInResponse(String primaryAccountNumber) throws InterruptedException {
        if(primaryAccountNumber != null) {
            int lengthPAM = primaryAccountNumber.length();

            if (lengthPAM > 2 && primaryAccountNumber.substring(0, 2).equals("00")) {
                int time = Integer.parseInt(primaryAccountNumber.substring(2, 3));
                sleep(time * 1000);
            }
        }
    }


    public static String generateRandomNumber(int length){
        Random rnd = new Random();
        StringBuilder sb = new StringBuilder(length);
        for(int i=0;i<length;i++)
            sb.append((char)('0'+rnd.nextInt(10)));
        return sb.toString();
    }


    public static boolean isSecurityCodeEmpty(String cvv){

        if(cvv == null)
            return true;

        if(cvv.length() == 1 && (cvv.charAt(0) == '?' || cvv.charAt(0) == ' ') )
            return true;

        if(cvv.equalsIgnoreCase(""))
            return true;

        return false;
    }

    public static boolean isSecurityCodeValid(String cvv){
        if(cvv == null || cvv.length() < 3)
            return false;

        if(cvv.charAt(0) == '?' || cvv.charAt(0) == ' ')
            return true;



        return true;
    }

    public static VError getError(String PAN){
        int last3Digits = Integer.parseInt(PAN.substring(PAN.length()-3,PAN.length()));

        if(isServerFaultError(last3Digits))
            return null;

        EWSError validError = ErrorIdMap.getError(last3Digits);

        if(validError != null){
            VError error = new VError();
            error.setId(last3Digits);
            error.setCode(validError.getErrorCode());
            error.setMessage(validError.getErrorMessage());

            return error;
        }

        return null;

    }

    public static void handleDesiredExceptions(String input) {
        String inputLast3;
        if (input.length() >= 3) {
            int errorId = 0;
            inputLast3 = input.substring(input.length() - 3);
            try {
                errorId = Integer.parseInt(inputLast3);
            } catch (NumberFormatException ex){
                // Do Nothing
            }

            if (errorId > 0)
                throwDesiredException(errorId);
        }
    }

    public static void throwDesiredException(int errorId) {
        if (isServerFaultError(errorId)) {
            throw new ServerFaultException(errorId);
        } else if (isClientFaultError(errorId)) {
            throw new ClientFaultException(errorId);
        }
    }

    public static boolean isServerFaultError(int errorId) {
        return errorId == 1 || errorId == 2 || errorId == 3 || errorId == 8;
    }

    public static boolean isClientFaultError(int errorId) {
        return ErrorIdMap.containsErrorId(errorId) && !isServerFaultError(errorId);
    }


    public static String getPANThroughRegId(String regId) {
        int lengthInput = regId.length();

        if(lengthInput > 3) {
            StringBuilder sb = new StringBuilder(regId.substring(0, lengthInput - 4));
            StringBuilder lastFour = new StringBuilder(regId.substring(lengthInput - 4));
            return sb.append(lastFour.reverse().toString()).toString();
        } else {
            return regId;
        }
    }

    public static String getCVVThroughToken(String token) {
        if(token.length() < 3){
            return "566";
        }
        return token.substring(token.length() - 3, token.length());
    }


    public static AccountType getAccountType(String AccNum){
        int last2Digits = Integer.parseInt(AccNum.substring(AccNum.length()-2,AccNum.length()));
        switch (last2Digits % 4){
            case 0:
                return AccountType.CHECKING;
            case 1:
                return AccountType.SAVINGS;
            case 2:
                return AccountType.CORPORATE_CHECKING;
            default:
                return AccountType.CORPORATE_SAVINGS;
        }
    }

    public static String getRoutingNumber(String AccNum) {
        if(AccNum.length() < 6)
            return "123456";
        return AccNum.substring(0, 6);
    }


    public static String decrypt(String input) {
        StringBuilder sb = new StringBuilder(input);
        return sb.reverse().toString();
    }
}
