/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for addQuery complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="addQuery">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="securitytoken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parser" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="query" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="transformationconfig" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addQuery", propOrder = {
    "securitytoken",
    "parser",
    "query",
    "transformationconfig"
})
public class AddQuery {

    protected String securitytoken;
    protected String parser;
    protected String query;
    protected String transformationconfig;

    /**
     * Gets the value of the securitytoken property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecuritytoken() {
        return securitytoken;
    }

    /**
     * Sets the value of the securitytoken property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecuritytoken(String value) {
        this.securitytoken = value;
    }

    /**
     * Gets the value of the parser property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParser() {
        return parser;
    }

    /**
     * Sets the value of the parser property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParser(String value) {
        this.parser = value;
    }

    /**
     * Gets the value of the query property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQuery() {
        return query;
    }

    /**
     * Sets the value of the query property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQuery(String value) {
        this.query = value;
    }

    /**
     * Gets the value of the transformationconfig property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTransformationconfig() {
        return transformationconfig;
    }

    /**
     * Sets the value of the transformationconfig property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTransformationconfig(String value) {
        this.transformationconfig = value;
    }

}
