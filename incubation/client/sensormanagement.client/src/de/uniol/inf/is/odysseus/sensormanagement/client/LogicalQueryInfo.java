
package de.uniol.inf.is.odysseus.sensormanagement.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r logicalQueryInfo complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="logicalQueryInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="queryText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="containsCycles" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="notice" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parameters" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}pair" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "logicalQueryInfo", propOrder = {
    "id",
    "queryText",
    "parserID",
    "containsCycles",
    "priority",
    "name",
    "notice",
    "parameters"
})
@SuppressWarnings(value = { "all" })
public class LogicalQueryInfo {

    protected int id;
    protected String queryText;
    protected String parserID;
    protected boolean containsCycles;
    protected int priority;
    protected String name;
    protected String notice;
    @XmlElement(nillable = true)
    protected List<Pair> parameters;

    /**
     * Ruft den Wert der id-Eigenschaft ab.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Legt den Wert der id-Eigenschaft fest.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Ruft den Wert der queryText-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getQueryText() {
        return queryText;
    }

    /**
     * Legt den Wert der queryText-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setQueryText(String value) {
        this.queryText = value;
    }

    /**
     * Ruft den Wert der parserID-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getParserID() {
        return parserID;
    }

    /**
     * Legt den Wert der parserID-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setParserID(String value) {
        this.parserID = value;
    }

    /**
     * Ruft den Wert der containsCycles-Eigenschaft ab.
     * 
     */
    public boolean isContainsCycles() {
        return containsCycles;
    }

    /**
     * Legt den Wert der containsCycles-Eigenschaft fest.
     * 
     */
    public void setContainsCycles(boolean value) {
        this.containsCycles = value;
    }

    /**
     * Ruft den Wert der priority-Eigenschaft ab.
     * 
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Legt den Wert der priority-Eigenschaft fest.
     * 
     */
    public void setPriority(int value) {
        this.priority = value;
    }

    /**
     * Ruft den Wert der name-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Legt den Wert der name-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Ruft den Wert der notice-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNotice() {
        return notice;
    }

    /**
     * Legt den Wert der notice-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNotice(String value) {
        this.notice = value;
    }

    /**
     * Gets the value of the parameters property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the parameters property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getParameters().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Pair }
     * 
     * 
     */
    public List<Pair> getParameters() {
        if (parameters == null) {
            parameters = new ArrayList<Pair>();
        }
        return this.parameters;
    }

}
