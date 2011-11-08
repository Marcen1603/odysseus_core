/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.Collection;
import java.util.Map;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.unit.SDFUnit;

public abstract class SDFSchemaElement extends SDFElement {

	private static final long serialVersionUID = -6044873167369148445L;
	final private SDFDatatype datatype;
	final private Map<String, SDFDatatypeConstraint> dtConstraints;
	final private SDFUnit unit;

	
	
	public SDFSchemaElement(String sourceName, String name, SDFDatatype datatype) {
		this(sourceName,name,datatype,null, null);
	}

	public SDFSchemaElement(String sourceName, String name, SDFDatatype datatype, SDFUnit unit, Map<String, SDFDatatypeConstraint> dtConstraints) {
		super(sourceName,name);
		this.datatype = datatype;
		this.unit = unit;
		this.dtConstraints = dtConstraints;
	}
	
	public SDFSchemaElement(String URI, SDFDatatype datatype) {
		super(URI);
		this.datatype = datatype;
		unit = null;
		this.dtConstraints = null;
	}
	
	public SDFSchemaElement(SDFSchemaElement copy) {
		super(copy);
		this.datatype = copy.datatype;
		this.dtConstraints = copy.dtConstraints;
		this.unit = copy.unit;
	}

	public SDFSchemaElement(String newName, SDFSchemaElement sdfAttribute) {
		super(newName);
		this.datatype = sdfAttribute.datatype;
		this.dtConstraints = sdfAttribute.dtConstraints;
		this.unit = sdfAttribute.unit;		
	}
	
	public SDFSchemaElement(String newSourceName, String newName, SDFSchemaElement sdfAttribute) {
		super(newSourceName, newName);
		this.datatype = sdfAttribute.datatype;
		this.dtConstraints = sdfAttribute.dtConstraints;
		this.unit = sdfAttribute.unit;		
	}

	public SDFDatatype getDatatype() {
		return datatype;
	}

//	public void addDtConstraint(String uri, SDFDatatypeConstraint dtConstraint) {
//		this.dtConstraints.put(uri, dtConstraint);
//	}

	public SDFDatatypeConstraint getDtConstraint(String uri) {
		return dtConstraints.get(uri);
	}

	public Collection<SDFDatatypeConstraint> getDtConstraints() {
		return dtConstraints.values();
	}

	public SDFUnit getUnit() {
		return unit;
	}


}