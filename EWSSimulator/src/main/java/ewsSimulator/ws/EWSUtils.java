package ewsSimulator.ws;

import static java.lang.Thread.sleep;

import java.util.UUID;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.springframework.ws.soap.SoapHeaderElement;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;

public class EWSUtils {

    public static String randomReqId() {

        return UUID.randomUUID().toString();
    }

    public static String getRegId(String primaryAccountNumber) {

        return Long.toString(Long.parseLong(primaryAccountNumber) + 99999);
    }

    public static String generateProperty(String input) {

        int lengthInput = input.length();

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

        try {
            return generateProperty(token);
        } catch(NumberFormatException ex) {
            return "3000100011118566";
        }
    }

    public static void delayInResponse(String primaryAccountNumber) throws InterruptedException {
        int lengthPAM = primaryAccountNumber.length();

        if(lengthPAM > 2 && primaryAccountNumber.substring(0, 2).equals("00")) {
            int time = Integer.parseInt(primaryAccountNumber.substring(2,3));
            sleep(time * 1000);
        }
    }


    public static void getAuthentication(SoapHeaderElement header){
        try {

            JAXBContext context = JAXBContext.newInstance(SecurityHeaderType.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            JAXBElement<SecurityHeaderType> root = unmarshaller.unmarshal(header.getSource(), SecurityHeaderType.class);

            ElementNSImpl checkwhatever = (ElementNSImpl)root.getValue().getAny().get(0);
            String localName = checkwhatever.getFirstChild().getNextSibling().getLocalName();

            String hi = "";

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        //return authentication;
    }

}
