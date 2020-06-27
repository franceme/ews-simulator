package com.worldpay.simulator.service;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Service;

import com.worldpay.simulator.RequestValidationFault;
import com.worldpay.simulator.SecurityHeaderType;
import com.worldpay.simulator.ServerFault;

@Service
public class JAXBService {

    private static final JAXBContext context;

    static {
        try {
            context = JAXBContext.newInstance(SecurityHeaderType.class, RequestValidationFault.class, ServerFault.class);
        } catch (JAXBException e) {
            throw new Error("Fatal Error: Cannot create JAXBContext");
        }
    }

    public JAXBContext getContext(){
        return context;
    }
}
