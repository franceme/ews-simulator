package ewsSimulator.ws;

import static java.lang.Thread.sleep;

import java.util.Random;
import java.util.UUID;

public class EWSUtils {

    public static String randomReqId() {

        String uuid = UUID.randomUUID().toString();
        return uuid;
    }

    public static String getRegId(String primaryAccountNumber) {

        return Long.toString(Long.parseLong(primaryAccountNumber) + 99999);
    }

    public static String getToken(String primaryAccountNumber) {

        int lengthPAM = primaryAccountNumber.length();

        if(lengthPAM > 3) {
            StringBuffer sb = new StringBuffer(primaryAccountNumber.substring(0, lengthPAM - 4));
            String lastFour = primaryAccountNumber.substring(lengthPAM - 4);
            return sb.reverse().append(lastFour).toString();
        } else {
            return primaryAccountNumber;
        }
    }

    public static String getPAN(String token) {

        return getToken(token);
    }



    public static void delayInResponse(String primaryAccountNumber) throws InterruptedException {
        int lengthPAM = primaryAccountNumber.length();

        if(lengthPAM > 2 && primaryAccountNumber.substring(0, 2).equals("00")) {
            int time = Integer.parseInt(primaryAccountNumber.substring(2,3));
            sleep(time * 1000);
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

        if((cvv.charAt(0) == '?' || cvv.charAt(0) == ' ') && cvv.length() == 1)
            return true;

        if(cvv.equalsIgnoreCase(""))
            return true;

        return false;
    }

    public static boolean isSecurityCodeValid(String cvv){

        if(cvv.charAt(0) == '?' || cvv.charAt(0) == ' ')
            return true;

        if(cvv.length() < 3)
            return false;

        return true;
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

        return Long.toString(Long.parseLong(regId) - 99999);
    }

    public static String getCVVThroughToken(String token) {
        return token.substring(token.length() - 3, token.length());
    }

    public static boolean validRollupId(String rollupId){
        if(rollupId.equals("")||rollupId.equals("?")||rollupId.length()>6) return false;
        try{
            Integer.parseInt(rollupId);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean validCVV(String OrderLVT){
        if(OrderLVT.equals("")||OrderLVT.equals("?")||OrderLVT.length()!=3) return false;
        try{
            Integer.parseInt(OrderLVT);
        }
        catch (Exception e){
            return false;
        }
        return true;
    }

    public static boolean validToken(String token){
        if(token.equals("")||token.equals("?")||token.length()>50) return false;
        return true;
    }
}
