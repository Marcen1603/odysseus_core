
package de.uniol.inf.is.odysseus.sensormanagement.client;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java-Klasse f�r logicalOperatorInformation complex type.
 * 
 * <p>Das folgende Schemafragment gibt den erwarteten Content an, der in dieser Klasse enthalten ist.
 * 
 * <pre>
 * &lt;complexType name="logicalOperatorInformation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="categories" type="{http://www.w3.org/2001/XMLSchema}string" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="deprecated" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="doc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="hidden" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="maxPorts" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="minPorts" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="operatorName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="url" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "logicalOperatorInformation", propOrder = {
    "categories",
    "deprecated",
    "doc",
    "hidden",
    "maxPorts",
    "minPorts",
    "operatorName",
    "url"
})
@SuppressWarnings(value = { "all" })
public class LogicalOperatorInformation {

    @XmlElement(nillable = true)
    protected List<String> categories;
    protected boolean deprecated;
    protected String doc;
    protected boolean hidden;
    protected int maxPorts;
    protected int minPorts;
    protected String operatorName;
    protected String url;

    /**
     * Gets the value of the categories property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the categories property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCategories().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getCategories() {
        if (categories == null) {
            categories = new ArrayList<String>();
        }
        return this.categories;
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
     * Ruft den Wert der hidden-Eigenschaft ab.
     * 
     */
    public boolean isHidden() {
        return hidden;
    }

    /**
     * Legt den Wert der hidden-Eigenschaft fest.
     * 
     */
    public void setHidden(boolean value) {
        this.hidden = value;
    }

    /**
     * Ruft den Wert der maxPorts-Eigenschaft ab.
     * 
     */
    public int getMaxPorts() {
        return maxPorts;
    }

    /**
     * Legt den Wert der maxPorts-Eigenschaft fest.
     * 
     */
    public void setMaxPorts(int value) {
        this.maxPorts = value;
    }

    /**
     * Ruft den Wert der minPorts-Eigenschaft ab.
     * 
     */
    public int getMinPorts() {
        return minPorts;
    }

    /**
     * Legt den Wert der minPorts-Eigenschaft fest.
     * 
     */
    public void setMinPorts(int value) {
        this.minPorts = value;
    }

    /**
     * Ruft den Wert der operatorName-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * Legt den Wert der operatorName-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOperatorName(String value) {
        this.operatorName = value;
    }

    /**
     * Ruft den Wert der url-Eigenschaft ab.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUrl() {
        return url;
    }

    /**
     * Legt den Wert der url-Eigenschaft fest.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUrl(String value) {
        this.url = value;
    }

}
