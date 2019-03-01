package com.worldpay.simulator.filter;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class FilterConfiguration {

    @Bean
    public RequestLogFilter requestLoggingFilter() {
        RequestLogFilter loggingFilter = loggingFilterInstance();
        loggingFilter.setIncludeClientInfo(true);
        loggingFilter.setIncludeQueryString(true);
        loggingFilter.setIncludeHeaders(true);
        return loggingFilter;
    }

    public RequestLogFilter loggingFilterInstance() {
        return new RequestLogFilter();
    }
}
