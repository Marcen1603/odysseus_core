
package de.uniol.inf.is.odysseus.webservice.client;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for parameterInfo complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="parameterInfo">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="dataType" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="deprecated" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="doc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="listDataType" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}parameterInfo" minOccurs="0"/>
 *         &lt;element name="mandatory" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="mapKeyDataType" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}parameterInfo" minOccurs="0"/>
 *         &lt;element name="mapValueDataType" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}parameterInfo" minOccurs="0"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="possibleValues" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="requirement" type="{http://webservice.server.webservice.executor.planmanagement.odysseus.is.inf.uniol.de/}requirement" minOccurs="0"/>
 *         &lt;element name="value" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "parameterInfo", propOrder = {
    "dataType",
    "deprecated",
    "doc",
    "listDataType",
    "mandatory",
    "mapKeyDataType",
    "mapValueDataType",
    "name",
    "possibleValues",
    "requirement",
    "value"
})
public class ParameterInfo {

    protected String dataType;
    protected boolean deprecated;
    protected String doc;
    protected ParameterInfo listDataType;
    protected boolean mandatory;
    protected ParameterInfo mapKeyDataType;
    protected ParameterInfo mapValueDataType;
    protected String name;
    @XmlElement(nillable = true)
    protected List<String> possibleValues;
    protected Requirement requirement;
    protected String value;

    /**
     * Gets the value of the dataType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * Sets the value of the dataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataType(String value) {
        this.dataType = value;
    }

    /**
     * Gets the value of the deprecated property.
     * 
     */
    public boolean isDeprecated() {
        return deprecated;
    }

    /**
     * Sets the value of the deprecated property.
     * 
     */
    public void setDeprecated(boolean value) {
        this.deprecated = value;
    }

    /**
     * Gets the value of the doc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDoc() {
        return doc;
    }

    /**
     * Sets the value of the doc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDoc(String value) {
        this.doc = value;
    }

    /**
     * Gets the value of the listDataType property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterInfo }
     *     
     */
    public ParameterInfo getListDataType() {
        return listDataType;
    }

    /**
     * Sets the value of the listDataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterInfo }
     *     
     */
    public void setListDataType(ParameterInfo value) {
        this.listDataType = value;
    }

    /**
     * Gets the value of the mandatory property.
     * 
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * Sets the value of the mandatory property.
     * 
     */
    public void setMandatory(boolean value) {
        this.mandatory = value;
    }

    /**
     * Gets the value of the mapKeyDataType property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterInfo }
     *     
     */
    public ParameterInfo getMapKeyDataType() {
        return mapKeyDataType;
    }

    /**
     * Sets the value of the mapKeyDataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterInfo }
     *     
     */
    public void setMapKeyDataType(ParameterInfo value) {
        this.mapKeyDataType = value;
    }

    /**
     * Gets the value of the mapValueDataType property.
     * 
     * @return
     *     possible object is
     *     {@link ParameterInfo }
     *     
     */
    public ParameterInfo getMapValueDataType() {
        return mapValueDataType;
    }

    /**
     * Sets the value of the mapValueDataType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParameterInfo }
     *     
     */
    public void setMapValueDataType(ParameterInfo value) {
        this.mapValueDataType = value;
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
     * Gets the value of the possibleValues property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the possibleValues property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPossibleValues().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getPossibleValues() {
        if (possibleValues == null) {
            possibleValues = new ArrayList<String>();
        }
        return this.possibleValues;
    }

    /**
     * Gets the value of the requirement property.
     * 
     * @return
     *     possible object is
     *     {@link Requirement }
     *     
     */
    public Requirement getRequirement() {
        return requirement;
    }

    /**
     * Sets the value of the requirement property.
     * 
     * @param value
     *     allowed object is
     *     {@link Requirement }
     *     
     */
    public void setRequirement(Requirement value) {
        this.requirement = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValue(String value) {
        this.value = value;
    }

}
