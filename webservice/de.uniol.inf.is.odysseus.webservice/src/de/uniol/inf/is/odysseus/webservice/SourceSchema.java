
package de.uniol.inf.is.odysseus.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SourceSchema complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="SourceSchema">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attributeSchema" type="{http://de.uni.ol.inf.is.odysseus/OdysseusWS/}SchemaArray"/>
 *         &lt;element name="streamName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="channel" type="{http://de.uni.ol.inf.is.odysseus/OdysseusWS/}Channel"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SourceSchema", propOrder = {
    "attributeSchema",
    "streamName",
    "channel"
})
public class SourceSchema {

    @XmlElement(required = true)
    protected SchemaArray attributeSchema;
    @XmlElement(required = true)
    protected String streamName;
    @XmlElement(required = true)
    protected Channel channel;

    /**
     * Gets the value of the attributeSchema property.
     * 
     * @return
     *     possible object is
     *     {@link SchemaArray }
     *     
     */
    public SchemaArray getAttributeSchema() {
        return attributeSchema;
    }

    /**
     * Sets the value of the attributeSchema property.
     * 
     * @param value
     *     allowed object is
     *     {@link SchemaArray }
     *     
     */
    public void setAttributeSchema(SchemaArray value) {
        this.attributeSchema = value;
    }

    /**
     * Gets the value of the streamName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStreamName() {
        return streamName;
    }

    /**
     * Sets the value of the streamName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStreamName(String value) {
        this.streamName = value;
    }

    /**
     * Gets the value of the channel property.
     * 
     * @return
     *     possible object is
     *     {@link Channel }
     *     
     */
    public Channel getChannel() {
        return channel;
    }

    /**
     * Sets the value of the channel property.
     * 
     * @param value
     *     allowed object is
     *     {@link Channel }
     *     
     */
    public void setChannel(Channel value) {
        this.channel = value;
    }

}
