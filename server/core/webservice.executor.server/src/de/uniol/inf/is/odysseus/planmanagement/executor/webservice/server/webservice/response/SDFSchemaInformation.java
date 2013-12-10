/**
 * 
 */
package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Merlin Wasmann
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sdfSchemaInformation", propOrder = {
		"uri",
		"attributes",
		"typeClass"
})
public class SDFSchemaInformation {

	public SDFSchemaInformation() {
		
	}
	
	public Collection<SDFAttributeInformation> attributes;
	public String uri;
	public Class<?> typeClass;
	
	public SDFSchemaInformation(String uri, Collection<SDFAttributeInformation> attributes, Class<?> typeClass) {
		this.uri = uri;
		this.attributes = attributes;
		this.typeClass = typeClass;
	}
}
