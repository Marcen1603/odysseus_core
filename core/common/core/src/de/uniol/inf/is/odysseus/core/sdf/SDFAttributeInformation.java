/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.core.sdf;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * @author Merlin Wasmann
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sdfAttributeInformation", propOrder = {
	"sourcename",
	"attributename",
	"datatype",
	"subschema"
})
public class SDFAttributeInformation {

	public SDFAttributeInformation() {

	}

	public String sourcename;
	public String attributename;
	public SDFDatatypeInformation datatype;
	public SDFSchemaInformation subschema;

	public SDFAttributeInformation(String sourceName, String attributeName, SDFDatatypeInformation datatype, SDFSchemaInformation subschema) {
		this.sourcename = sourceName;
		this.attributename = attributeName;
		this.datatype = datatype;
		// I think, this should not be here, but as part of data type --> needs some work to refactor
		this.subschema = subschema;
	}

	public static SDFAttributeInformation createAttributeInformation(SDFAttribute attribute) {
		SDFDatatypeInformation dataTypeInformation = SDFDatatypeInformation.createDatatypeInformation(attribute.getDatatype());
		SDFAttributeInformation attrInfo = new SDFAttributeInformation(attribute.getSourceName(), attribute.getAttributeName(),
				dataTypeInformation, SDFSchemaInformation.createSchemaInformation(attribute.getDatatype().getSchema()));
		return attrInfo;
	}
}
