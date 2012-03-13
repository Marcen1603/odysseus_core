
package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for logicalQuery complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="logicalQuery">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="queryText" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parserID" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="containsCycles" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="priority" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="penalty" type="{http://www.w3.org/2001/XMLSchema}double"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "logicalQuery", propOrder = {
    "id",
    "queryText",
    "parserID",
    "containsCycles",
    "priority",
    "penalty"
})
public class LogicalQuery {

    protected int id;
    protected String queryText;
    protected String parserID;
    protected boolean containsCycles;
    protected int priority;
    protected double penalty;

    /**
     * Gets the value of the id property.
     * 
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(int value) {
        this.id = value;
    }

    /**
     * Gets the value of the queryText property.
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
     * Sets the value of the queryText property.
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
     * Gets the value of the parserID property.
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
     * Sets the value of the parserID property.
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
     * Gets the value of the containsCycles property.
     * 
     */
    public boolean isContainsCycles() {
        return containsCycles;
    }

    /**
     * Sets the value of the containsCycles property.
     * 
     */
    public void setContainsCycles(boolean value) {
        this.containsCycles = value;
    }

    /**
     * Gets the value of the priority property.
     * 
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets the value of the priority property.
     * 
     */
    public void setPriority(int value) {
        this.priority = value;
    }

    /**
     * Gets the value of the penalty property.
     * 
     */
    public double getPenalty() {
        return penalty;
    }

    /**
     * Sets the value of the penalty property.
     * 
     */
    public void setPenalty(double value) {
        this.penalty = value;
    }

}
