package com.worldpay.simulator.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.worldpay.simulator.Account;
import com.worldpay.simulator.DeregistrationResponse;
import com.worldpay.simulator.DetokenizeResponse;
import com.worldpay.simulator.ECheckToken;
import com.worldpay.simulator.OrderDeregistrationResponse;
import com.worldpay.simulator.OrderRegistrationResponse;
import com.worldpay.simulator.RegistrationResponse;
import com.worldpay.simulator.Token;
import com.worldpay.simulator.TokenRegistrationResponse;
import com.worldpay.simulator.TokenizeResponse;

@Service
public class SimulatorResponseService {
    // Key - RegId
    private Map<String, DeregistrationResponse> deregistrationResponseMap;
    // Key - Token
    private Map<String, OrderDeregistrationResponse> orderDeregistrationResponseMap;
    // Key - Token
    private Map<String, DetokenizeResponse> detokenizeResponseMap;
    // Key - PAN
    private Map<String, Token> tokenInquiryPanToTokenResponseMap;
    // Key - Token
    private Map<String, Account> eCheckDetokenizeTokenToAccountResponseMap;
    // Key - PAN
    private Map<String, ECheckToken> eCheckTokenizePanToTokenResponseMap;
    // Key - PAN
    private Map<String, ECheckToken> eCheckTokenizePanToErrorTokenResponseMap;
    // Key - Token
    private Map<String, String> batchDetokenizeTokenToPanResponseMap;
    // Key - PAN
    private Map<String, Token> batchTokenizePANToTokenResponseMap;
    // Key - Token
    private Map<String, TokenRegistrationResponse> tokenRegistrationResponseMap;
    // Key - CVV
    private Map<String, OrderRegistrationResponse> orderRegistrationResponseMap;
    // Key - PAN
    private Map<String, TokenizeResponse> tokenizeResponseMap;
    // Key - PAN
    private Map<String, RegistrationResponse> registrationResponseMap;

    // Exceptions Map (key to error id)

    // Key - RegId
    private Map<String, Integer> deregistrationExceptionMap;
    // Key - Token
    private Map<String, Integer> orderDeregistrationExceptionMap;
    // Key - Token
    private Map<String, Integer> detokenizeExceptionMap;
    // Key - PAN
    private Map<String, Integer> tokenInquiryExceptionMap;
    // Key - Token
    private Map<String, Integer> eCheckDetokenizeExceptionMap;
    // Key - PAN
    private Map<String, Integer> eCheckTokenizeExceptionMap;
    // Key - Token
    private Map<String, Integer> batchDetokenizeExceptionMap;
    // Key - PAN
    private Map<String, Integer> batchTokenizeExceptionMap;
    // Key - Token
    private Map<String, Integer> tokenRegistrationExceptionMap;
    // Key - CVV
    private Map<String, Integer> orderRegistrationExceptionMap;
    // Key - PAN
    private Map<String, Integer> tokenizeExceptionMap;
    // Key - PAN
    private Map<String, Integer> registrationExceptionMap;


    public Map<String, DeregistrationResponse> getDeregistrationResponseMap() {
        return deregistrationResponseMap;
    }

    public Map<String, OrderDeregistrationResponse> getOrderDeregistrationResponseMap() {
        return orderDeregistrationResponseMap;
    }

    public Map<String, DetokenizeResponse> getDetokenizeResponseMap() {
        return detokenizeResponseMap;
    }

    public Map<String, Token> getTokenInquiryPanToTokenResponseMap() {
        return tokenInquiryPanToTokenResponseMap;
    }

    public Map<String, Account> getECheckDetokenizeTokenToAccountResponseMap() {
        return eCheckDetokenizeTokenToAccountResponseMap;
    }

    public Map<String, ECheckToken> getECheckTokenizePanToTokenResponseMap() {
        return eCheckTokenizePanToTokenResponseMap;
    }

    public Map<String, String> getBatchDetokenizeTokenToPanResponseMap() {
        return batchDetokenizeTokenToPanResponseMap;
    }

    public Map<String, Token> getBatchTokenizePanToTokenResponseMap() {
        return batchTokenizePANToTokenResponseMap;
    }

    public Map<String, TokenRegistrationResponse> getTokenRegistrationResponseMap() {
        return tokenRegistrationResponseMap;
    }

    public Map<String, OrderRegistrationResponse> getOrderRegistrationResponseMap() {
        return orderRegistrationResponseMap;
    }

    public Map<String, TokenizeResponse> getTokenizeResponseMap() {
        return tokenizeResponseMap;
    }

    public Map<String, RegistrationResponse> getRegistrationResponseMap() {
        return registrationResponseMap;
    }

    public Map<String, Integer> getDeregistrationExceptionMap() {
        return deregistrationExceptionMap;
    }

    public Map<String, Integer> getOrderDeregistrationExceptionMap() {
        return orderDeregistrationExceptionMap;
    }

    public Map<String, Integer> getDetokenizeExceptionMap() {
        return detokenizeExceptionMap;
    }

    public Map<String, Integer> getTokenInquiryExceptionMap() {
        return tokenInquiryExceptionMap;
    }

    public Map<String, Integer> getECheckDetokenizeExceptionMap() {
        return eCheckDetokenizeExceptionMap;
    }

    public Map<String, Integer> getECheckTokenizeExceptionMap() {
        return eCheckTokenizeExceptionMap;
    }

    public Map<String, ECheckToken> geteCheckTokenizePanToErrorTokenResponseMap() {
        return eCheckTokenizePanToErrorTokenResponseMap;
    }

    public Map<String, Integer> getBatchDetokenizeExceptionMap() {
        return batchDetokenizeExceptionMap;
    }

    public Map<String, Integer> getBatchTokenizeExceptionMap() {
        return batchTokenizeExceptionMap;
    }

    public Map<String, Integer> getTokenRegistrationExceptionMap() {
        return tokenRegistrationExceptionMap;
    }

    public Map<String, Integer> getOrderRegistrationExceptionMap() {
        return orderRegistrationExceptionMap;
    }

    public Map<String, Integer> getTokenizeExceptionMap() {
        return tokenizeExceptionMap;
    }

    public Map<String, Integer> getRegistrationExceptionMap() {
        return registrationExceptionMap;
    }

    public void clearAllResponses() {
        clearDeregistrationMap();
        clearOrderDeregistrationMap();
        clearDetokenizeMap();
        clearTokenInquiryMap();
        clearECheckDetokenizeMap();
        clearECheckTokenizeMap();
        clearBatchDetokenizeMap();
        clearBatchTokenizeMap();
        clearTokenRegistrationMap();
        clearOrderRegistrationMap();
        clearTokenizeMap();
        clearRegistrationMap();
    }

    public void clearDeregistrationMap() {
        if (deregistrationResponseMap != null) {
            deregistrationResponseMap.clear();
        }
        if (deregistrationExceptionMap != null) {
            deregistrationExceptionMap.clear();
        }
    }

    public void clearOrderDeregistrationMap() {
        if (orderDeregistrationResponseMap != null) {
            orderDeregistrationResponseMap.clear();
        }
        if (orderDeregistrationExceptionMap != null) {
            orderDeregistrationExceptionMap.clear();
        }
    }

    public void clearDetokenizeMap() {
        if (detokenizeResponseMap != null) {
            detokenizeResponseMap.clear();
        }
        if (detokenizeExceptionMap != null) {
            detokenizeExceptionMap.clear();
        }
    }

    public void clearTokenInquiryMap() {
        if (tokenInquiryPanToTokenResponseMap != null) {
            tokenInquiryPanToTokenResponseMap.clear();
        }
        if (tokenInquiryExceptionMap != null) {
            tokenInquiryExceptionMap.clear();
        }
    }

    public void clearECheckDetokenizeMap() {
        if (eCheckDetokenizeTokenToAccountResponseMap != null) {
            eCheckDetokenizeTokenToAccountResponseMap.clear();
        }
        if (eCheckDetokenizeExceptionMap != null) {
            eCheckDetokenizeExceptionMap.clear();
        }
    }

    public void clearECheckTokenizeMap() {
        if (eCheckTokenizePanToTokenResponseMap != null) {
            eCheckTokenizePanToTokenResponseMap.clear();
        }
        if (eCheckTokenizeExceptionMap != null) {
            eCheckTokenizeExceptionMap.clear();
        }
        if (eCheckTokenizePanToErrorTokenResponseMap != null) {
            eCheckTokenizePanToErrorTokenResponseMap.clear();
        }
    }

    public void clearBatchDetokenizeMap() {
        if (batchDetokenizeTokenToPanResponseMap != null) {
            batchDetokenizeTokenToPanResponseMap.clear();
        }
        if (batchDetokenizeExceptionMap != null) {
            batchDetokenizeExceptionMap.clear();
        }
    }

    public void clearBatchTokenizeMap() {
        if (batchTokenizePANToTokenResponseMap != null) {
            batchTokenizePANToTokenResponseMap.clear();
        }
        if (batchTokenizeExceptionMap != null) {
            batchTokenizeExceptionMap.clear();
        }
    }

    public void clearTokenRegistrationMap() {
        if (tokenRegistrationResponseMap != null) {
            tokenRegistrationResponseMap.clear();
        }
        if (tokenRegistrationExceptionMap != null) {
            tokenRegistrationExceptionMap.clear();
        }
    }

    public void clearOrderRegistrationMap() {
        if (orderRegistrationResponseMap != null) {
            orderRegistrationResponseMap.clear();
        }
        if (orderRegistrationExceptionMap != null) {
            orderRegistrationExceptionMap.clear();
        }
    }

    public void clearTokenizeMap() {
        if (tokenizeResponseMap != null) {
            tokenizeResponseMap.clear();
        }
        if (tokenizeExceptionMap != null) {
            tokenizeExceptionMap.clear();
        }
    }

    public void clearRegistrationMap() {
        if (registrationResponseMap != null) {
            registrationResponseMap.clear();
        }
        if (registrationExceptionMap != null) {
            registrationExceptionMap.clear();
        }
    }

    public void addDeregistrationResponseToMap(String key, DeregistrationResponse deregistrationResponse) {
        if (deregistrationResponseMap == null) {
            deregistrationResponseMap = new HashMap<>();
        }
        deregistrationResponseMap.put(key, deregistrationResponse);
    }

    public void addOrderDeregistrationResponseToMap(String key, OrderDeregistrationResponse orderDeregistrationResponse) {
        if (orderDeregistrationResponseMap == null) {
            orderDeregistrationResponseMap = new HashMap<>();
        }
        orderDeregistrationResponseMap.put(key, orderDeregistrationResponse);
    }

    public void addDetokenizeResponseToMap(String key, DetokenizeResponse detokenizeResponse) {
        if (detokenizeResponseMap == null) {
            detokenizeResponseMap = new HashMap<>();
        }
        detokenizeResponseMap.put(key, detokenizeResponse);
    }

    public void addTokenInquiryPanToTokenResponseToMap(String key, Token token) {
        if (tokenInquiryPanToTokenResponseMap == null) {
            tokenInquiryPanToTokenResponseMap = new HashMap<>();
        }
        tokenInquiryPanToTokenResponseMap.put(key, token);
    }

    public void addECheckDetokenizeTokenToAccountResponseToMap(String key, Account account) {
        if (eCheckDetokenizeTokenToAccountResponseMap == null) {
            eCheckDetokenizeTokenToAccountResponseMap = new HashMap<>();
        }
        eCheckDetokenizeTokenToAccountResponseMap.put(key, account);
    }

    public void addECheckTokenizePanToTokenResponseToMap(String key, ECheckToken token) {
        if (eCheckTokenizePanToTokenResponseMap == null) {
            eCheckTokenizePanToTokenResponseMap = new HashMap<>();
        }
        eCheckTokenizePanToTokenResponseMap.put(key, token);
    }

    public void addECheckTokenizePanToErrorTokenResponseToMap(String key, ECheckToken token) {
        if (eCheckTokenizePanToErrorTokenResponseMap == null) {
            eCheckTokenizePanToErrorTokenResponseMap = new HashMap<>();
        }
        eCheckTokenizePanToErrorTokenResponseMap.put(key, token);
    }

    public void addBatchDetokenizeTokenToPanResponseToMap(String token, String pan) {
        if (batchDetokenizeTokenToPanResponseMap == null) {
            batchDetokenizeTokenToPanResponseMap = new HashMap<>();
        }
        batchDetokenizeTokenToPanResponseMap.put(token, pan);
    }

    public void addBatchTokenizePanToTokenResponseToMap(String key, Token token) {
        if (batchTokenizePANToTokenResponseMap == null) {
            batchTokenizePANToTokenResponseMap = new HashMap<>();
        }
        batchTokenizePANToTokenResponseMap.put(key, token);
    }

    public void addTokenRegistrationResponseToMap(String key, TokenRegistrationResponse tokenRegistrationResponse) {
        if (tokenRegistrationResponseMap == null) {
            tokenRegistrationResponseMap = new HashMap<>();
        }
        tokenRegistrationResponseMap.put(key, tokenRegistrationResponse);
    }

    public void addOrderRegistrationResponseToMap(String key, OrderRegistrationResponse orderRegistrationResponse) {
        if (orderRegistrationResponseMap == null) {
            orderRegistrationResponseMap = new HashMap<>();
        }
        orderRegistrationResponseMap.put(key, orderRegistrationResponse);
    }

    public void addTokenizeResponseToMap(String key, TokenizeResponse tokenizeResponse) {
        if (tokenizeResponseMap == null) {
            tokenizeResponseMap = new HashMap<>();
        }
        tokenizeResponseMap.put(key, tokenizeResponse);
    }

    public void addRegistrationResponseToMap(String key, RegistrationResponse registrationResponse) {
        if (registrationResponseMap == null) {
            registrationResponseMap = new HashMap<>();
        }
        registrationResponseMap.put(key, registrationResponse);
    }

    public void addDeregistrationExceptionToMap(String key, Integer deregistrationException) {
        if (deregistrationExceptionMap == null) {
            deregistrationExceptionMap = new HashMap<>();
        }
        deregistrationExceptionMap.put(key, deregistrationException);
    }

    public void addOrderDeregistrationExceptionToMap(String key, Integer orderDeregistrationException) {
        if (orderDeregistrationExceptionMap == null) {
            orderDeregistrationExceptionMap = new HashMap<>();
        }
        orderDeregistrationExceptionMap.put(key, orderDeregistrationException);
    }

    public void addDetokenizeExceptionToMap(String key, Integer detokenizeException) {
        if (detokenizeExceptionMap == null) {
            detokenizeExceptionMap = new HashMap<>();
        }
        detokenizeExceptionMap.put(key, detokenizeException);
    }

    public void addTokenInquiryExceptionToMap(String key, Integer tokenInquiryException) {
        if (tokenInquiryExceptionMap == null) {
            tokenInquiryExceptionMap = new HashMap<>();
        }
        tokenInquiryExceptionMap.put(key, tokenInquiryException);
    }

    public void addECheckDetokenizeExceptionToMap(String key, Integer eCheckDetokenizeException) {
        if (eCheckDetokenizeExceptionMap == null) {
            eCheckDetokenizeExceptionMap = new HashMap<>();
        }
        eCheckDetokenizeExceptionMap.put(key, eCheckDetokenizeException);
    }

    public void addECheckTokenizeExceptionToMap(String key, Integer eCheckTokenizeException) {
        if (eCheckTokenizeExceptionMap == null) {
            eCheckTokenizeExceptionMap = new HashMap<>();
        }
        eCheckTokenizeExceptionMap.put(key, eCheckTokenizeException);
    }

    public void addBatchDetokenizeExceptionToMap(String key, Integer batchDetokenizeException) {
        if (batchDetokenizeExceptionMap == null) {
            batchDetokenizeExceptionMap = new HashMap<>();
        }
        batchDetokenizeExceptionMap.put(key, batchDetokenizeException);
    }

    public void addBatchTokenizeExceptionToMap(String key, Integer batchTokenizeException) {
        if (batchTokenizeExceptionMap == null) {
            batchTokenizeExceptionMap = new HashMap<>();
        }
        batchTokenizeExceptionMap.put(key, batchTokenizeException);
    }

    public void addTokenRegistrationExceptionToMap(String key, Integer tokenRegistrationException) {
        if (tokenRegistrationExceptionMap == null) {
            tokenRegistrationExceptionMap = new HashMap<>();
        }
        tokenRegistrationExceptionMap.put(key, tokenRegistrationException);
    }

    public void addOrderRegistrationExceptionToMap(String key, Integer orderRegistrationException) {
        if (orderRegistrationExceptionMap == null) {
            orderRegistrationExceptionMap = new HashMap<>();
        }
        orderRegistrationExceptionMap.put(key, orderRegistrationException);
    }

    public void addTokenizeExceptionToMap(String key, Integer tokenizeException) {
        if (tokenizeExceptionMap == null) {
            tokenizeExceptionMap = new HashMap<>();
        }
        tokenizeExceptionMap.put(key, tokenizeException);
    }

    public void addRegistrationExceptionToMap(String key, Integer registrationException) {
        if (registrationExceptionMap == null) {
            registrationExceptionMap = new HashMap<>();
        }
        registrationExceptionMap.put(key, registrationException);
    }

    public Integer getRegistrationExceptionSavedIfAny(String pan) {
        if (registrationExceptionMap == null) {
            return null;
        }
        Integer errorId = registrationExceptionMap.get(pan);
        return errorId == null ? registrationExceptionMap.get("*") : errorId;
    }

    public RegistrationResponse getRegistrationResponseSavedIfAny(String pan) {
        if (registrationResponseMap == null) {
            return null;
        }
        RegistrationResponse response = registrationResponseMap.get(pan);
        return response == null ? registrationResponseMap.get("*") : response;
    }

    public Integer getTokenizeExceptionSavedIfAny(String pan) {
        if (tokenizeExceptionMap == null) {
            return null;
        }
        Integer errorId = tokenizeExceptionMap.get(pan);
        return errorId == null ? tokenizeExceptionMap.get("*") : errorId;
    }

    public TokenizeResponse getTokenizeResponseSavedIfAny(String pan) {
        if (tokenizeResponseMap == null) {
            return null;
        }
        TokenizeResponse response = tokenizeResponseMap.get(pan);
        return response == null ? tokenizeResponseMap.get("*") : response;
    }

    public Integer getOrderRegistrationExceptionSavedIfAny(String cvv) {
        if (orderRegistrationExceptionMap == null) {
            return null;
        }
        Integer errorId = orderRegistrationExceptionMap.get(cvv);
        return errorId == null ? orderRegistrationExceptionMap.get("*") : errorId;
    }

    public OrderRegistrationResponse getOrderRegistrationResponseSavedIfAny(String cvv) {
        if (orderRegistrationResponseMap == null) {
            return null;
        }
        OrderRegistrationResponse response = orderRegistrationResponseMap.get(cvv);
        return response == null ? orderRegistrationResponseMap.get("*") : response;
    }

    public Integer getTokenRegistrationExceptionSavedIfAny(String token) {
        if (tokenRegistrationExceptionMap == null) {
            return null;
        }
        Integer errorId = tokenRegistrationExceptionMap.get(token);
        return errorId == null ? tokenRegistrationExceptionMap.get("*") : errorId;
    }

    public TokenRegistrationResponse getTokenRegistrationResponseSavedIfAny(String token) {
        if (tokenRegistrationResponseMap == null) {
            return null;
        }
        TokenRegistrationResponse response = tokenRegistrationResponseMap.get(token);
        return response == null ? tokenRegistrationResponseMap.get("*") : response;
    }

    public Integer getDetokenizeExceptionSavedIfAny(String token) {
        if (detokenizeExceptionMap == null) {
            return null;
        }
        Integer errorId = detokenizeExceptionMap.get(token);
        return errorId == null ? detokenizeExceptionMap.get("*") : errorId;
    }

    public DetokenizeResponse getDetokenizeResponseSavedIfAny(String token) {
        if (detokenizeResponseMap == null) {
            return null;
        }
        DetokenizeResponse response = detokenizeResponseMap.get(token);
        return response == null ? detokenizeResponseMap.get("*") : response;
    }

    public Integer getDeregistrationExceptionSavedIfAny(String regId) {
        if (deregistrationExceptionMap == null) {
            return null;
        }
        Integer errorId = deregistrationExceptionMap.get(regId);
        return errorId == null ? deregistrationExceptionMap.get("*") : errorId;
    }

    public DeregistrationResponse getDeregistrationResponseSavedIfAny(String regId) {
        if (deregistrationResponseMap == null) {
            return null;
        }
        DeregistrationResponse response = deregistrationResponseMap.get(regId);
        return response == null ? deregistrationResponseMap.get("*") : response;
    }

    public Integer getEcheckTokenizeExceptionSavedIfAny(String pan) {
        if (eCheckTokenizeExceptionMap == null) {
            return null;
        }
        Integer errorId = eCheckTokenizeExceptionMap.get(pan);
        return errorId == null ? eCheckTokenizeExceptionMap.get("*") : errorId;
    }

    public ECheckToken getEcheckTokenizeTokenResponseSavedIfAny(String pan) {
        if (eCheckTokenizePanToTokenResponseMap == null) {
            return null;
        }
        ECheckToken response = eCheckTokenizePanToTokenResponseMap.get(pan);
        return response == null ? eCheckTokenizePanToTokenResponseMap.get("*") : response;
    }

    public ECheckToken getEcheckTokenizeErrorTokenSavedIfAny(String pan) {
        if (eCheckTokenizePanToErrorTokenResponseMap == null) {
            return null;
        }
        ECheckToken response = eCheckTokenizePanToErrorTokenResponseMap.get(pan);
        return response == null ? eCheckTokenizePanToErrorTokenResponseMap.get("*") : response;
    }
}
