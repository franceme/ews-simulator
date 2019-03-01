package com.worldpay.simulator.adapter;

import java.util.List;
import java.util.Properties;

import javax.xml.ws.soap.SOAPFaultException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.server.endpoint.SoapFaultDefinition;
import org.springframework.ws.soap.server.endpoint.SoapFaultMappingExceptionResolver;
import org.springframework.ws.soap.server.endpoint.interceptor.PayloadValidatingInterceptor;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

import com.worldpay.simulator.exceptions.ClientFaultException;
import com.worldpay.simulator.exceptions.DetailSoapFaultDefinitionExceptionResolver;
import com.worldpay.simulator.exceptions.SecurityErrorException;
import com.worldpay.simulator.exceptions.ServerFaultException;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Value("${validate.schema}")
    private boolean validateSchema;

    @Value("${simulator.endpoint}")
    private String simulatorEndpoint;

    @Autowired
    DetailSoapFaultDefinitionExceptionResolver exceptionResolver;

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        PayloadValidatingInterceptor validatingInterceptor = payloadValidatingInterceptorInstance();
        validatingInterceptor.setValidateRequest(validateSchema);
        validatingInterceptor.setValidateResponse(validateSchema);
        validatingInterceptor.setXsdSchema(encryptionSchema());

        interceptors.add(validatingInterceptor);

    }

    public PayloadValidatingInterceptor payloadValidatingInterceptorInstance() {
        return new PayloadValidatingInterceptor();
    }


    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver() {

        SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        faultDefinition.setFaultStringOrReason("Server Fault Exception");
        exceptionResolver.setDefaultFault(faultDefinition);


        Properties errorMappings = new Properties();
        errorMappings.setProperty(ServerFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(ClientFaultException.class.getName(), SoapFaultDefinition.CLIENT.toString());
        errorMappings.setProperty(SecurityErrorException.class.getName(), SoapFaultDefinition.CLIENT.toString());
        errorMappings.setProperty(SOAPFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());
        exceptionResolver.setExceptionMappings(errorMappings);
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean<>(servlet, "/"+simulatorEndpoint+"/*");
    }


    @Bean
    public XsdSchema encryptionSchema() {
        XsdSchema schema = new SimpleXsdSchema(new ClassPathResource("xsd/encryption_v4_transactions.xsd"));
        return schema;
    }

    @Bean
    public XsdSchema securitySchema() {
        XsdSchema schema = new SimpleXsdSchema(new ClassPathResource("xsd/oasis-200401-wss-wssecurity-secext-1.0.xsd"));
        return schema;
    }
}