
package de.uniol.inf.is.odysseus.service.sensor.webservice;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sensorSchema complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sensorSchema">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="attributes" type="{http://sensorregistry.odysseus.is.inf.uniol.de/}sensorAttribute" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sensorSchema", propOrder = {
    "attributes"
})
public class SensorSchema {

    @XmlElement(nillable = true)
    protected List<SensorAttribute> attributes;

    /**
     * Gets the value of the attributes property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the attributes property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAttributes().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SensorAttribute }
     * 
     * 
     */
    public List<SensorAttribute> getAttributes() {
        if (attributes == null) {
            attributes = new ArrayList<SensorAttribute>();
        }
        return this.attributes;
    }
    
    public void addAttribute(SensorAttribute sa){
    	if (attributes == null) {
            attributes = new ArrayList<SensorAttribute>();
        }
    	this.attributes.add(sa);
    }

}
