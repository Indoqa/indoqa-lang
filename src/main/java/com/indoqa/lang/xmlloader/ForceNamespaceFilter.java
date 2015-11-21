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

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.XMLFilterImpl;
import org.xml.sax.helpers.XMLReaderFactory;

public class ForceNamespaceFilter extends XMLFilterImpl {

    private String targetNamespace;
    private boolean namespaceSet;

    public ForceNamespaceFilter(String namespaceUri) throws SAXException {
        super();

        this.targetNamespace = namespaceUri;

        this.setParent(XMLReaderFactory.createXMLReader());
    }

    @Override
    public void endElement(String uri, String localname, String qName) throws SAXException {
        super.endElement(this.targetNamespace, localname, qName);
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();

        this.startControlledPrefixMapping();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        super.startElement(this.targetNamespace, localName, qName, atts);
    }

    @Override
    public void startPrefixMapping(String prefix, String url) throws SAXException {
        // do nothing
    }

    private void startControlledPrefixMapping() throws SAXException {
        if (!this.namespaceSet) {
            super.startPrefixMapping("", this.targetNamespace);

            this.namespaceSet = true;
        }
    }
}
