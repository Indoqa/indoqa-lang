/*
 * Licensed to the Indoqa Software Design und Beratung GmbH (Indoqa) under
 * one or more contributor license agreements. See the NOTICE file distributed
 * with this work for additional information regarding copyright ownership.
 * Indoqa licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.indoqa.lang.xmlloader;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XmlLoader<T extends Object> {

    private Class<? extends T>[] types;

    private JAXBContext context;
    private Map<String, Object> properties = new HashMap<String, Object>();
    private DocumentBuilder documentBuilder;

    private Schema schema;

    public XmlLoader(Class<? extends T> type) {
        this(type, null);
    }

    @SuppressWarnings("unchecked")
    public XmlLoader(Class<? extends T> type, Map<String, Object> properties) {
        this(new Class[] {type});

        if (properties != null) {
            this.properties = properties;
        } else {
            this.properties.put(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        }
    }

    public XmlLoader(Class<? extends T>[] types) {
        this.types = types;
    }

    @SuppressWarnings("unchecked")
    public XmlLoader(String type) throws ClassNotFoundException {
        this((Class<T>) Class.forName(type), null);
    }

    @SuppressWarnings("unchecked")
    public XmlLoader(String type, Map<String, Object> properties) throws ClassNotFoundException {
        this((Class<T>) Class.forName(type), properties);
    }

    public Document createDocument() {
        return this.getDocumentBuilder().newDocument();
    }

    public Schema getSchema() {
        return this.schema;
    }

    public String marshal(T entity) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        this.marshal(entity, stringWriter);
        return stringWriter.toString();
    }

    public void marshal(T entity, File file) throws JAXBException {
        this.getMarshaller().marshal(entity, file);
    }

    public void marshal(T entity, Node node) throws JAXBException {
        this.getMarshaller().marshal(entity, node);
    }

    public void marshal(T entity, OutputStream outputStream) throws JAXBException {
        this.getMarshaller().marshal(entity, outputStream);
    }

    public void marshal(T entity, Writer writer) throws JAXBException {
        this.getMarshaller().marshal(entity, writer);
    }

    public void setSchema(Schema schema) {
        this.schema = schema;
    }

    @SuppressWarnings("unchecked")
    public T unmarshal(File file) throws JAXBException {
        return (T) this.getUnmarshaller().unmarshal(file);
    }

    @SuppressWarnings("unchecked")
    public T unmarshal(InputStream inputStream) throws JAXBException {
        return (T) this.getUnmarshaller().unmarshal(inputStream);
    }

    public T unmarshal(InputStream inputStream, String forcedNamespace) throws JAXBException {
        if (forcedNamespace == null) {
            return this.unmarshal(inputStream);
        }

        try {
            InputSource inputSource = new InputSource(inputStream);
            SAXSource saxSource = new SAXSource(new ForceNamespaceFilter(forcedNamespace), inputSource);

            return this.unmarshal(saxSource);
        } catch (SAXException e) {
            throw new JAXBException("Failed to apply forced namespace", e);
        }
    }

    @SuppressWarnings("unchecked")
    public T unmarshal(Node node) throws JAXBException {
        return (T) this.getUnmarshaller().unmarshal(node);
    }

    @SuppressWarnings("unchecked")
    public T unmarshal(Source source) throws JAXBException {
        return (T) this.getUnmarshaller().unmarshal(source);
    }

    @SuppressWarnings("unchecked")
    public T unmarshal(String xmlPayload) throws JAXBException {
        return (T) this.getUnmarshaller().unmarshal(new StringReader(xmlPayload));
    }

    @SuppressWarnings("unchecked")
    public T unmarshal(URL url) throws JAXBException {
        return (T) this.getUnmarshaller().unmarshal(url);
    }

    private synchronized JAXBContext getContext() throws JAXBException {
        if (this.context == null) {
            this.context = JAXBContext.newInstance(this.types);
        }

        return this.context;
    }

    private synchronized DocumentBuilder getDocumentBuilder() {
        if (this.documentBuilder == null) {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                throw new IllegalStateException("Failed to obtain a document builder", e);
            }
        }

        return this.documentBuilder;
    }

    private Marshaller getMarshaller() throws JAXBException {
        Marshaller marshaller = this.getContext().createMarshaller();
        marshaller.setSchema(this.schema);

        for (String key : this.properties.keySet()) {
            marshaller.setProperty(key, this.properties.get(key));
        }

        return marshaller;
    }

    private Unmarshaller getUnmarshaller() throws JAXBException {
        Unmarshaller unmarshaller = this.getContext().createUnmarshaller();

        unmarshaller.setSchema(this.schema);

        return unmarshaller;
    }
}
