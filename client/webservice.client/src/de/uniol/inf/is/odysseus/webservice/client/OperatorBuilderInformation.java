
package de.uniol.inf.is.odysseus.webservice.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for operatorBuilderInformation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="operatorBuilderInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="maxInputOperatorCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="minInputOperatorCount" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="parameters" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}parameterInfo" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "operatorBuilderInformation", propOrder = {
    "maxInputOperatorCount",
    "minInputOperatorCount",
    "name",
    "parameters"
})
public class OperatorBuilderInformation {

    protected int maxInputOperatorCount;
    protected int minInputOperatorCount;
    protected String name;
    @XmlElement(nillable = true)
    protected List<ParameterInfo> parameters;

    /**
     * Gets the value of the maxInputOperatorCount property.
     * 
     */
    public int getMaxInputOperatorCount() {
        return maxInputOperatorCount;
    }

    /**
     * Sets the value of the maxInputOperatorCount property.
     * 
     */
    public void setMaxInputOperatorCount(int value) {
        this.maxInputOperatorCount = value;
    }

    /**
     * Gets the value of the minInputOperatorCount property.
     * 
     */
    public int getMinInputOperatorCount() {
        return minInputOperatorCount;
    }

    /**
     * Sets the value of the minInputOperatorCount property.
     * 
     */
    public void setMinInputOperatorCount(int value) {
        this.minInputOperatorCount = value;
    }

    /**
     * Gets the value of the name property.
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
     * Sets the value of the name property.
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
     * {@link ParameterInfo }
     * 
     * 
     */
    public List<ParameterInfo> getParameters() {
        if (parameters == null) {
            parameters = new ArrayList<ParameterInfo>();
        }
        return this.parameters;
    }

}
