package com.worldpay.simulator.utils;

import org.springframework.stereotype.Service;
import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class HttpHeaderUtils {

    public static String defaultCorrelationId = "64f231d1-e122-4693-af76-5652d4e37441";
    public static String acceptInput = "gzip,deflate";
    public static String correlationHeader = "v_CorrelationId";
    public static String acceptHeader = "Accept-Encoding";

    public void customizeHttpResponseHeader(){
        String headerValue = getHttpHeaderValue(correlationHeader);

        if(headerValue == null) {
            addResponseHttpHeader(correlationHeader,defaultCorrelationId);
        } else {
            addResponseHttpHeader(correlationHeader,headerValue);
        }
        setResponseHttpHeaderValue(acceptHeader,acceptInput);
    }

    public HttpServletRequest getHttpServletRequest() {
        TransportContext ctx = TransportContextHolder.getTransportContext();
        return ( null != ctx ) ? ((HttpServletConnection) ctx.getConnection()).getHttpServletRequest() : null;
    }

    public String getHttpHeaderValue(String headerName) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        return ( null != httpServletRequest ) ? httpServletRequest.getHeader( headerName ) : null;
    }

    public HttpServletResponse getHttpServletResponse() {
        TransportContext ctx = TransportContextHolder.getTransportContext();
        return ( null != ctx ) ? ((HttpServletConnection) ctx.getConnection()).getHttpServletResponse() : null;
    }

    public void addResponseHttpHeader(String headerName,String headerValue) {

        HttpServletResponse httpServletResponse = getHttpServletResponse();
        httpServletResponse.addHeader(headerName, headerValue);
    }

    public void setResponseHttpHeaderValue(String headerName,String headerValue) {

        HttpServletResponse httpServletResponse = getHttpServletResponse();
        httpServletResponse.setHeader(headerName, headerValue);

    }
}
