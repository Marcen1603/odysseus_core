/**
 *
 */
package de.uniol.inf.is.odysseus.core.sdf;

import java.util.ArrayList;
import java.util.Collection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

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

	static public SDFSchemaInformation createSchemaInformation(SDFSchema schema) {
		if (schema == null){
			return null;
		}
		Collection<SDFAttribute> attributes = schema.getAttributes();
		Collection<SDFAttributeInformation> attributeInfos = new ArrayList<SDFAttributeInformation>();
		for (SDFAttribute attribute : attributes) {
			attributeInfos.add(SDFAttributeInformation.createAttributeInformation(attribute));
		}
		SDFSchemaInformation schemaInfo = new SDFSchemaInformation(schema.getURI(), attributeInfos, schema.getType());
		return schemaInfo;
	}
}
