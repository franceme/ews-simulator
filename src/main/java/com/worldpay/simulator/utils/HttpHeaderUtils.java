package com.worldpay.simulator.utils;

import org.springframework.ws.transport.context.TransportContext;
import org.springframework.ws.transport.context.TransportContextHolder;
import org.springframework.ws.transport.http.HttpServletConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpHeaderUtils {

    public static String defaultCorrelationId = "64f231d1-e122-4693-af76-5652d4e37441";
    public static String acceptInput = "gzip,deflate";
    public static String correlationHeader = "v_CorrelationId";
    public static String acceptHeader = "Accept-Encoding";

    public static void customizeHttpResponseHeader(){
        String headerValue = getHttpHeaderValue(correlationHeader);

        if(headerValue == null) {
            addResponseHttpHeader(correlationHeader,defaultCorrelationId);
        } else {
            addResponseHttpHeader(correlationHeader,headerValue);
        }
        setResponseHttpHeaderValue(acceptHeader,acceptInput);
    }

    private static HttpServletRequest getHttpServletRequest() {
        TransportContext ctx = TransportContextHolder.getTransportContext();
        return ( null != ctx ) ? ((HttpServletConnection) ctx.getConnection()).getHttpServletRequest() : null;
    }

    private static String getHttpHeaderValue( String headerName ) {
        HttpServletRequest httpServletRequest = getHttpServletRequest();
        return ( null != httpServletRequest ) ? httpServletRequest.getHeader( headerName ) : null;
    }

    private static HttpServletResponse getHttpServletResponse() {
        TransportContext ctx = TransportContextHolder.getTransportContext();
        return ( null != ctx ) ? ((HttpServletConnection) ctx.getConnection()).getHttpServletResponse() : null;
    }

    private static void addResponseHttpHeader(String headerName,String headerValue) {

        HttpServletResponse httpServletResponse = getHttpServletResponse();
        httpServletResponse.addHeader(headerName, headerValue);
    }

    private static void setResponseHttpHeaderValue(String headerName,String headerValue) {

        HttpServletResponse httpServletResponse = getHttpServletResponse();
        httpServletResponse.setHeader(headerName, headerValue);

    }
}
