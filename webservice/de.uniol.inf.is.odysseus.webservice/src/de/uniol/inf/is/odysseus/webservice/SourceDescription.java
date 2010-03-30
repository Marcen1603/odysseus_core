
package de.uniol.inf.is.odysseus.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sourceDescription complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sourceDescription">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="sourceString" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sourceSchema" type="{http://de.uni.ol.inf.is.odysseus/OdysseusWS/}SourceSchema"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sourceDescription", propOrder = {
    "sourceString",
    "sourceSchema"
})
public class SourceDescription {

    protected String sourceString;
    protected SourceSchema sourceSchema;

    /**
     * Gets the value of the sourceString property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSourceString() {
        return sourceString;
    }

    /**
     * Sets the value of the sourceString property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSourceString(String value) {
        this.sourceString = value;
    }

    /**
     * Gets the value of the sourceSchema property.
     * 
     * @return
     *     possible object is
     *     {@link SourceSchema }
     *     
     */
    public SourceSchema getSourceSchema() {
        return sourceSchema;
    }

    /**
     * Sets the value of the sourceSchema property.
     * 
     * @param value
     *     allowed object is
     *     {@link SourceSchema }
     *     
     */
    public void setSourceSchema(SourceSchema value) {
        this.sourceSchema = value;
    }

}
