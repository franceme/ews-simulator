package ewsSimulator.ws;

import static java.lang.Thread.sleep;

import java.util.Random;
import java.util.UUID;

public class EWSUtils {

    public static String randomReqId() {

        String uuid = UUID.randomUUID().toString();
        return "uuid = " + uuid;
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

}
