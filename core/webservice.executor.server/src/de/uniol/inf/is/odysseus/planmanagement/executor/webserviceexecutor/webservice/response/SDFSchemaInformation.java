/**
 * 
 */
package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.response;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author merlin
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sdfSchemaInformation", propOrder = {
		"uri",
		"attributes"
})
public class SDFSchemaInformation {

	public SDFSchemaInformation() {
		
	}
	
	public Collection<SDFAttributeInformation> attributes;
	public String uri;
	
	public SDFSchemaInformation(String uri, Collection<SDFAttributeInformation> attributes) {
		this.uri = uri;
		this.attributes = attributes;
	}
}
