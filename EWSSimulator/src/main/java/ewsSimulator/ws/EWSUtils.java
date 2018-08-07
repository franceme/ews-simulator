package ewsSimulator.ws;

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


}
