package com.worldpay.simulator.filter;

import org.springframework.web.filter.CommonsRequestLoggingFilter;
import javax.servlet.http.HttpServletRequest;


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
