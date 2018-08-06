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

        String token = Long.toString(Long.parseLong(primaryAccountNumber) + 77777);

        if(token.length() > 50) {

            return token.substring(0, 50);
        }

        return token;
    }


}
