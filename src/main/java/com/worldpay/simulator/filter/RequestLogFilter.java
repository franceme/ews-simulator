package com.worldpay.simulator.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;

import com.worldpay.simulator.utils.EWSUtils;

public class RequestLogFilter extends CommonsRequestLoggingFilter {
    public RequestLogFilter() {
    }

    @Override
    protected void beforeRequest(HttpServletRequest request, String message) {
        super.beforeRequest(request, request.getMethod() + " " + message);
    }

    @Override
    protected void afterRequest(HttpServletRequest request, String message) {
        super.afterRequest(request, request.getMethod() + " " + message);
    }
}
