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
package de.uniol.inf.is.odysseus.planmanagement.executor.webservice.server.webservice.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype.KindOfDatatype;

/**
 * @author Merlin Wasmann
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sdfDatatypeInformation", propOrder = {
		"uri", "type", "subtype", "subSchema"
})
public class SDFDatatypeInformation {
	
	public String uri;
	public KindOfDatatype type;
	public SDFDatatypeInformation subtype;
	public SDFSchemaInformation subSchema;

	public SDFDatatypeInformation(String uri, KindOfDatatype type, SDFDatatypeInformation subtype, SDFSchemaInformation subSchema) {
		this.uri = uri;
		this.type = type;
		this.subtype = subtype;
		this.subSchema = subSchema;
	}
	
}
