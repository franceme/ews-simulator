package ewsSimulator.ws;

import java.util.List;
import java.util.Properties;

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
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@Configuration
public class WebServiceConfig extends WsConfigurerAdapter {

    @Override
    public void addInterceptors(List<EndpointInterceptor> interceptors) {
        PayloadValidatingInterceptor validatingInterceptor = new PayloadValidatingInterceptor();
//        PayloadValidatingInterceptor secutiryInterceptor = new PayloadValidatingInterceptor();
        validatingInterceptor.setValidateRequest(true);
        validatingInterceptor.setValidateResponse(true);
        validatingInterceptor.setXsdSchema(encryptionSchema());

//        secutiryInterceptor.setValidateRequest(true);
//        secutiryInterceptor.setValidateResponse(true);
//        secutiryInterceptor.setXsdSchema(encryptionSchema());

        interceptors.add(validatingInterceptor);
//        interceptors.add(secutiryInterceptor);

    }


    @Bean
    public SoapFaultMappingExceptionResolver exceptionResolver() {
        SoapFaultMappingExceptionResolver exceptionResolver = new DetailSoapFaultDefinitionExceptionResolver();

        SoapFaultDefinition faultDefinition = new SoapFaultDefinition();
        faultDefinition.setFaultCode(SoapFaultDefinition.SERVER);
        exceptionResolver.setDefaultFault(faultDefinition);

        Properties errorMappings = new Properties();
        errorMappings.setProperty(Exception.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(ServerFaultException.class.getName(), SoapFaultDefinition.SERVER.toString());
        errorMappings.setProperty(ClientFaultException.class.getName(), SoapFaultDefinition.CLIENT.toString());
        errorMappings.setProperty(SecurityErrorException.class.getName(), SoapFaultDefinition.CLIENT.toString());
        exceptionResolver.setExceptionMappings(errorMappings);
        exceptionResolver.setOrder(1);
        return exceptionResolver;
    }

    @Bean
    public ServletRegistrationBean messageDispatcherServlet(ApplicationContext applicationContext) {
        MessageDispatcherServlet servlet = new MessageDispatcherServlet();
        servlet.setApplicationContext(applicationContext);
        servlet.setTransformWsdlLocations(true);
        return new ServletRegistrationBean(servlet, "/etws/v4/*");
    }

    @Bean(name = "EWSSimulator")
    public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema encryptionSchema) {
        DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
        wsdl11Definition.setPortTypeName("EncryptionWebServiceV4");
        wsdl11Definition.setLocationUri("/etws/v4");
        wsdl11Definition.setTargetNamespace("urn:com:vantiv:types:encryption:transactions:v1");
        wsdl11Definition.setSchema(encryptionSchema);
        return wsdl11Definition;
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