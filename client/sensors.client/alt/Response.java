
package de.uniol.inf.is.odysseus.sensors.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse für response complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="response">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="successful" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "response", propOrder = {
    "successful"
})
@XmlSeeAlso({
    StringResponse.class
})
public class Response {

    protected boolean successful;

    /**
     * Ruft den Wert der successful-Eigenschaft ab.
     * 
     */
    public boolean isSuccessful() {
        return successful;
    }

    /**
     * Legt den Wert der successful-Eigenschaft fest.
     * 
     */
    public void setSuccessful(boolean value) {
        this.successful = value;
    }

}
