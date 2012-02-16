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
package de.uniol.inf.is.odysseus.objecttracking.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFSchemaExtended;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttrDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeType;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTChannel;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateSensor;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHost;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInteger;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTListDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTORSchemaDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRecordDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRecordEntryDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AbstractDefaultVisitor;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.scars.operator.jdveaccess.ao.JDVEAccessMVAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeConstraint;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.unit.SDFUnit;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

public class CreateSensorVisitor extends AbstractDefaultVisitor {

	private String name;
	private String host;
	private Long port;

	private ISession user;
	private IDataDictionary dd;

	public void setUser(ISession user) {
		this.user = user;
	}

	public void setDataDictionary(IDataDictionary dd) {
		this.dd = dd;
	}

	@Override
	public Object visit(ASTCreateSensor node, Object data) {
		// System.out.println("Visit ASTCreateSensor(" + node + "," + data +
		// ")");
		this.name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		// System.out.println("Sensorname = " + name);

		data = null;
		SDFAttribute rootAttribute = (SDFAttribute) node.jjtGetChild(1)
				.jjtAccept(this, data);

		String objectListPath = ((ASTIdentifier) node.jjtGetChild(2)).getName();

		node.jjtGetChild(3).jjtAccept(this, data);

		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		attrs.add(rootAttribute);
		SDFSchemaExtended ex = new SDFSchemaExtended(attrs);

		SDFSchema schema = new SDFSchema(name, ex);

		dd.addSourceType(name, "ObjectRelationalStreaming");
		dd.addEntitySchema(name, schema, user);

		// TODO: rekursiv ausgeben, was in der SDFSchemaExtended ist
		// (extra Klasse oder so)
		// accessao bauen
		JDVEAccessMVAO source = new JDVEAccessMVAO(new SDFSource(name,
				"JDVEAccessMVPO"));
		source.setPort(port.intValue());
		source.setHost(host);
		source.setOutputSchema(ex);
		source.setObjectListPath(objectListPath);
		try {
			dd.setView(name, source, user);
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e.getMessage());
		}
		return null;
	}

	@Override
	public Object visit(ASTORSchemaDefinition node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	@Override
	public Object visit(ASTRecordDefinition node, Object data) {
		String attrName = ((ASTIdentifier) node.jjtGetChild(0)).getName();

		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		for (int i = 1; i < node.jjtGetNumChildren(); i++) {
			SDFAttribute attr = (SDFAttribute) node.jjtGetChild(i).jjtAccept(
					this, data);
			attrs.add(attr);
		}
		SDFSchema complexAttrSchema = new SDFSchema("", attrs);

		SDFDatatype recordType = new SDFDatatype(this.name + "." + attrName,
				SDFDatatype.KindOfDatatype.TUPLE, complexAttrSchema);
		try {
			dd.addDatatype(this.name + "." + attrName, recordType);
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e.getMessage());
		}

		SDFAttribute recordAttribute = new SDFAttribute(this.name, attrName,
				recordType);

		return recordAttribute;
	}

	@Override
	public Object visit(ASTRecordEntryDefinition node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	@Override
	public Object visit(ASTListDefinition node, Object data) {
		String attrName = ((ASTIdentifier) node.jjtGetChild(0)).getName();

		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		for (int i = 1; i < node.jjtGetNumChildren(); i++) {
			SDFAttribute listedAttribute = (SDFAttribute) node.jjtGetChild(i)
					.jjtAccept(this, data);
			attrs.add(listedAttribute);
		}
		SDFSchema complexAttrSchema = new SDFSchema("", attrs);

		SDFDatatype listType = new SDFDatatype(this.name + "." + attrName,
				SDFDatatype.KindOfDatatype.MULTI_VALUE, complexAttrSchema);
		try {
			dd.addDatatype(this.name + "." + attrName, listType);
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e.getMessage());
		}

		SDFAttribute attribute = new SDFAttribute(this.name, attrName, listType);

		return attribute;
	}

	@Override
	public Object visit(ASTAttrDefinition node, Object data) {
		String attrName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		ASTAttributeType astAttrType = (ASTAttributeType) node.jjtGetChild(1);

		SDFDatatype datatype = null;
		List<?> cov = null;
		SDFUnit unit = null;
		Map<String, SDFDatatypeConstraint> constraints = new HashMap<String, SDFDatatypeConstraint>();

		try {
			datatype = dd.getDatatype(astAttrType.getType());
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e.getMessage());
		}

		if (datatype.isMeasurementValue()
				&& astAttrType.jjtGetNumChildren() > 0) {
			cov = (List<?>) astAttrType.jjtGetChild(0).jjtAccept(this, data);

		}

		if (datatype.isDate())
			constraints.put("format", astAttrType.getDateFormat());

		SDFAttribute attribute = new SDFAttribute(this.name, attrName,
				datatype, unit, constraints, cov);

		return attribute;
	}

	@Override
	public Object visit(ASTHost node, Object data) {
		return node.getValue();
	}

	@Override
	public Object visit(ASTChannel node, Object data) {
		this.host = (String) node.jjtGetChild(0).jjtAccept(this, data);
		this.port = ((ASTInteger) node.jjtGetChild(1)).getValue();
		return super.visit(node, data);
	}

}
