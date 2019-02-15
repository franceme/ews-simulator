package com.worldpay.simulator.errors;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ErrorIdMap {

    private static final Map<Integer, EWSError> errorIdMap = new ConcurrentHashMap<>();

    static {
        errorIdMap.put(101, new EWSError("SERVICE_NOT_IMPLEMENTED", "The service you requested is not implemented."));
        errorIdMap.put(102, new EWSError("UNKNOWN_ERROR", "an unspecified error occurred."));
        errorIdMap.put(103, new EWSError("INTERNAL_ERROR", "Internal error."));
        errorIdMap.put(104, new EWSError("INVALID_REQUEST", "Invalid request (syntax error)."));
        errorIdMap.put(105, new EWSError("DETOKENIZATION_PAN_INVALID", "The De-tokenized PAN is invalid (legacy)."));
        errorIdMap.put(106, new EWSError("DECRYPTION_TOKEN_FAILED", "Token not found"));
        errorIdMap.put(107, new EWSError("TOKEN_OR_REG_ID_GENERATION_FAILED", "Token or registration ID generation failed."));
        errorIdMap.put(108, new EWSError("SERVICE_TIMED_OUT", "A back-end service/server timed out unexpectedly."));
        errorIdMap.put(109, new EWSError("TOKEN_ALREADY_EXISTS", "The Token already exists in the token vault."));
        errorIdMap.put(110, new EWSError("PAN_ALREADY_EXISTS", "The PAN already exists in the token vault"));
        errorIdMap.put(999, new EWSError("EWS_IS_DOWN", "EWS is down"));
    }

    public static EWSError getError(int id) {
        return errorIdMap.get(id);
    }

    public static boolean containsErrorId(int id) {
        return errorIdMap.containsKey(id);
    }
}
