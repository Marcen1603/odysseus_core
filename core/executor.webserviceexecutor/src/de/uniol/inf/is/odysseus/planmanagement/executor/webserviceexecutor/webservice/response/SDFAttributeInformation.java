package de.uniol.inf.is.odysseus.planmanagement.executor.webserviceexecutor.webservice.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * @author merlin
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sdfAttributeInformation", propOrder = {
	"sourcename",
	"attributename",
	"datatype"
})
public class SDFAttributeInformation {

	public SDFAttributeInformation() {
		
	}
	
	public String sourcename;
	public String attributename;
	public SDFDatatypeInformation datatype;
	
	public SDFAttributeInformation(String sourceName, String attributeName, SDFDatatypeInformation datatype) {
		this.sourcename = sourceName;
		this.attributename = attributeName;
		this.datatype = datatype;
	}
}
