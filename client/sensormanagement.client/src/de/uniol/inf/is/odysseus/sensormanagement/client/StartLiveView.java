
package de.uniol.inf.is.odysseus.sensormanagement.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r startLiveView complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="startLiveView">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="securityToken" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="sensorId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="targetHost" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="targetPort" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "startLiveView", propOrder = {
    "securityToken",
    "sensorId",
    "targetHost",
    "targetPort"
})
@SuppressWarnings(value = { "all" })
public class StartLiveView {

    protected String securityToken;
    protected String sensorId;
    protected String targetHost;
    protected int targetPort;

    /**
     * Ruft den Wert der securityToken-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSecurityToken() {
        return securityToken;
    }

    /**
     * Legt den Wert der securityToken-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSecurityToken(String value) {
        this.securityToken = value;
    }

    /**
     * Ruft den Wert der sensorId-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSensorId() {
        return sensorId;
    }

    /**
     * Legt den Wert der sensorId-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSensorId(String value) {
        this.sensorId = value;
    }

    /**
     * Ruft den Wert der targetHost-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTargetHost() {
        return targetHost;
    }

    /**
     * Legt den Wert der targetHost-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTargetHost(String value) {
        this.targetHost = value;
    }

    /**
     * Ruft den Wert der targetPort-Eigenschaft ab.
     * 
     */
    public int getTargetPort() {
        return targetPort;
    }

    /**
     * Legt den Wert der targetPort-Eigenschaft fest.
     * 
     */
    public void setTargetPort(int value) {
        this.targetPort = value;
    }

}
