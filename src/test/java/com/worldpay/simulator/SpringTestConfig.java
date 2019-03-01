package com.worldpay.simulator;

import com.worldpay.simulator.controller.EWSSimulatorEndpoint;
import com.worldpay.simulator.exceptions.DetailSoapFaultDefinitionExceptionResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;

import com.worldpay.simulator.filter.FilterConfiguration;
import com.worldpay.simulator.service.JAXBService;
import com.worldpay.simulator.utils.HttpHeaderUtils;
import com.worldpay.simulator.service.RequestValidator;
import com.worldpay.simulator.service.ValidatorService;

@ContextConfiguration
public class SpringTestConfig {

    @Bean(name = "testHttpHeaders")
    public HttpHeaderUtils httpHeaderUtils(){
        HttpHeaderUtils httpHeaderUtils = new HttpHeaderUtils();
        return httpHeaderUtils;
    }

    @Bean(name = "testEWSSimulator")
    public EWSSimulatorEndpoint eWSSimulatorEndpoint(){
        EWSSimulatorEndpoint eWSSimulatorEndpoint = new EWSSimulatorEndpoint();
        return eWSSimulatorEndpoint;
    }

    @Bean(name = "testFilterConfig")
    public FilterConfiguration filterConfiguration() {
        FilterConfiguration testFilterConfiguration = new FilterConfiguration();
        return testFilterConfiguration;
    }

    @Bean(name = "testValidatorService")
    public ValidatorService validatorService() {
        ValidatorService validatorService = new ValidatorService();
        return validatorService;
    }

    @Bean(name = "testRequestValidator")
    public RequestValidator requestValidator() {
        RequestValidator requestValidator = new RequestValidator();
        return requestValidator;
    }

    @Bean
    public JAXBService jaxbService() {
        return new JAXBService();
    }

    @Bean
    public DetailSoapFaultDefinitionExceptionResolver exceptionResolver(){
        return new DetailSoapFaultDefinitionExceptionResolver();
    }
}
