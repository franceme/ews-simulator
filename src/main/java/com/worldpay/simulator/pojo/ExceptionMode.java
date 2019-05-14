package com.worldpay.simulator.pojo;

public class ExceptionMode {

    private boolean randomExceptionsEnabled;
    private int avgErrorsPer200Requests;
    private int[] enabledRandomErrorCodes;

    public ExceptionMode(boolean randomExceptionsEnabled, int avgErrorsPer200Requests, int[] enabledRandomErrorCodes) {
        this.randomExceptionsEnabled = randomExceptionsEnabled;
        if (avgErrorsPer200Requests < 0) {
            throw new IllegalArgumentException("Error rate per 200 requests cannot be lower than 0");
        }
        if (avgErrorsPer200Requests > 200) {
            throw new IllegalArgumentException("Error rate per 200 requests cannot be higher than 200");
        }
        this.avgErrorsPer200Requests = avgErrorsPer200Requests;
        this.enabledRandomErrorCodes = enabledRandomErrorCodes;
    }

    public boolean isRandomExceptionsEnabled() {
        return randomExceptionsEnabled;
    }

    public void setRandomExceptionsEnabled(boolean randomExceptionsEnabled) {
        this.randomExceptionsEnabled = randomExceptionsEnabled;
    }

    public int getAvgErrorsPer200Requests() {
        return avgErrorsPer200Requests;
    }

    public void setAvgErrorsPer200Requests(int avgErrorsPer200Requests) {
        this.avgErrorsPer200Requests = avgErrorsPer200Requests;
    }

    public int[] getEnabledRandomErrorCodes() {
        return enabledRandomErrorCodes;
    }

    public void setEnabledRandomErrorCodes(int[] enabledRandomErrorCodes) {
        this.enabledRandomErrorCodes = enabledRandomErrorCodes;
    }
}
