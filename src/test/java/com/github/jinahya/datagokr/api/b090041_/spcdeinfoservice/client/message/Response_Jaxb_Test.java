package com.github.jinahya.datagokr.api.b090041_.spcdeinfoservice.client.message;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Slf4j
class Response_Jaxb_Test {

    /**
     * Prints XML Schema to the console.
     *
     * @throws JAXBException if a JAXB error occurs.
     * @throws IOException   if an I/O error occurs.
     */
    @Test
    void _Schema_() throws JAXBException, IOException {
        final JAXBContext context = JAXBContext.newInstance(Response.class);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        context.generateSchema(new SchemaOutputResolver() {
            @Override
            public Result createOutput(final String namespaceUri, final String suggestedFileName) throws IOException {
                return new StreamResult(baos) {
                    @Override
                    public String getSystemId() {
                        return "noid";
                    }
                };
            }
        });
        System.out.println(baos.toString());
    }
}