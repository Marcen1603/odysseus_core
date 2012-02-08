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
package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinitions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeType;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateType;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeConstraint;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

/**
 * @author André Bolles
 *
 */
public class CreateTypeVisitor extends AbstractDefaultVisitor {

	String name;
	private ISession caller;
	private IDataDictionary dd;
	SDFAttributeList attributes;

	public CreateTypeVisitor(ISession user, IDataDictionary dd) {
		this.caller = user;
		this.dd = dd;
	}
	
	@Override
	public Object visit(ASTCreateType node, Object data) throws QueryParseException {
		name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		attributes = new SDFAttributeList(name);
		node.jjtGetChild(1).jjtAccept(this, data); // ASTAttributeDefinitions
		
		SDFDatatype newType = new SDFDatatype(name, SDFDatatype.KindOfDatatype.TUPLE, attributes);
		dd.addDatatype(name, newType);

		return data;
	}
	
	@Override
	public Object visit(ASTAttributeDefinitions node, Object data) throws QueryParseException {
		node.childrenAccept(this, data); // each single attribute definition
		// check attributes for consistency
		boolean hasEndTimestamp = false, hasStartTimestamp = false;
		for (SDFAttribute attr : this.attributes) {
			if (attr.getDatatype().isStartTimestamp()) {
				if (hasStartTimestamp) {
					throw new QueryParseException("multiple definitions of StartTimestamp attribute not allowed");
				}
				hasStartTimestamp = true;
			}
			if (attr.getDatatype().isEndTimestamp()) {
				if (hasEndTimestamp) {
					throw new QueryParseException("multiple definitions of EndTimestamp attribute not allowed");
				}
				hasEndTimestamp = true;
			}
			if (Collections.frequency(this.attributes, attr) > 1) {
				throw new QueryParseException("ambiguous attribute definition: " + attr.toString());
			}
		}
		return null;
	}
	
	@Override
	public Object visit(ASTAttributeDefinition node, Object data) throws QueryParseException {
		String attrName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		SDFAttribute attribute = null;
		ASTAttributeType astAttrType = (ASTAttributeType) node.jjtGetChild(1);
		Map<String, SDFDatatypeConstraint> dtConstrains = new HashMap<String, SDFDatatypeConstraint>();
		
		// we allow user defined types, so check
		// whether the defined type exists or not
		
		if(this.dd.existsDatatype(astAttrType.getType())){
			
			SDFDatatype attribType = this.dd.getDatatype(astAttrType.getType());
			
			if (attribType.isDate()) {
				dtConstrains.put("format", astAttrType.getDateFormat());
			}
			
			if (attribType.isMeasurementValue()
					&& astAttrType.jjtGetNumChildren() > 0) {
				attribute = new SDFAttribute(this.name, attrName,attribType, null, dtConstrains, (List<?>) astAttrType.jjtGetChild(0).jjtAccept(this, data));	
			}
			if (attribute == null){
				attribute = new SDFAttribute(this.name, attrName,attribType, null, dtConstrains);
			}
		}
		// the corresponding type (used as type for an attribute of this newly defined type) has not been defined
		else{
			throw new QueryParseException("Type " + astAttrType.getType() + " has not been defined.");
		}
		
		this.attributes.add(attribute);
		return data;
	}
}
