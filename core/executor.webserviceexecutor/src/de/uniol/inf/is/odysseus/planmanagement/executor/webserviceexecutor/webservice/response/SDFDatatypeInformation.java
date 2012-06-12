package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author merlin
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sdfDatatypeInformation", propOrder = {
		"uri"
})
public class SDFDatatypeInformation {

	public SDFDatatypeInformation() {
		
	}
	
	public String uri;
	
	public SDFDatatypeInformation(String uri) {
		this.uri = uri;
	}
}
