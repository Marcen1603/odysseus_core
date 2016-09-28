
package de.uniol.inf.is.odysseus.webservice.client;
import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;					
				

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r parameterInfo complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
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
 *         &lt;element name="possibleValueMethod" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "possibleValueMethod",
    "requirement",
    "value"
})
@SuppressWarnings(value = { "all" })
public class ParameterInfo {

    protected String dataType;
    protected boolean deprecated;
    protected String doc;
    protected ParameterInfo listDataType;
    protected boolean mandatory;
    protected ParameterInfo mapKeyDataType;
    protected ParameterInfo mapValueDataType;
    protected String name;
    protected String possibleValueMethod;
    @XmlSchemaType(name = "string")
    protected Requirement requirement;
    protected String value;

    /**
     * Ruft den Wert der dataType-Eigenschaft ab.
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
     * Legt den Wert der dataType-Eigenschaft fest.
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
     * Ruft den Wert der deprecated-Eigenschaft ab.
     * 
     */
    public boolean isDeprecated() {
        return deprecated;
    }

    /**
     * Legt den Wert der deprecated-Eigenschaft fest.
     * 
     */
    public void setDeprecated(boolean value) {
        this.deprecated = value;
    }

    /**
     * Ruft den Wert der doc-Eigenschaft ab.
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
     * Legt den Wert der doc-Eigenschaft fest.
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
     * Ruft den Wert der listDataType-Eigenschaft ab.
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
     * Legt den Wert der listDataType-Eigenschaft fest.
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
     * Ruft den Wert der mandatory-Eigenschaft ab.
     * 
     */
    public boolean isMandatory() {
        return mandatory;
    }

    /**
     * Legt den Wert der mandatory-Eigenschaft fest.
     * 
     */
    public void setMandatory(boolean value) {
        this.mandatory = value;
    }

    /**
     * Ruft den Wert der mapKeyDataType-Eigenschaft ab.
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
     * Legt den Wert der mapKeyDataType-Eigenschaft fest.
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
     * Ruft den Wert der mapValueDataType-Eigenschaft ab.
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
     * Legt den Wert der mapValueDataType-Eigenschaft fest.
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
     * Ruft den Wert der possibleValueMethod-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPossibleValueMethod() {
        return possibleValueMethod;
    }

    /**
     * Legt den Wert der possibleValueMethod-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPossibleValueMethod(String value) {
        this.possibleValueMethod = value;
    }

    /**
     * Ruft den Wert der requirement-Eigenschaft ab.
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
     * Legt den Wert der requirement-Eigenschaft fest.
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
     * Ruft den Wert der value-Eigenschaft ab.
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
     * Legt den Wert der value-Eigenschaft fest.
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
