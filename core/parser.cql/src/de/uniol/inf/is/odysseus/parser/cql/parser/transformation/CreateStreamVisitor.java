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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.OutputSchemaSettable;
import de.uniol.inf.is.odysseus.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.logicaloperator.relational.FixedSetAccessAO;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.IVisitor;
import de.uniol.inf.is.odysseus.parser.cql.VisitorFactory;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinitions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeType;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAutoReconnect;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTChannel;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateFromDatabase;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHost;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInteger;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTLoginPassword;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPriorizedStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSilab;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSocket;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTTimedTuples;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.relational.base.RelationalAccessSourceTypes;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * @author Jonas Jacobi
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class CreateStreamVisitor extends AbstractDefaultVisitor {
	String name;
	private User caller;
	private IDataDictionary dd;

	public CreateStreamVisitor(User user, IDataDictionary dd) {
		this.caller = user;
		this.dd = dd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SDFAttributeList getAttributes() {
		return attributes;
	}

	public void setAttributes(SDFAttributeList attributes) {
		this.attributes = attributes;
	}

	public ILogicalOperator getOperator() {
		return operator;
	}

	public void setOperator(ILogicalOperator operator) {
		this.operator = operator;
	}

	SDFAttributeList attributes;
	ILogicalOperator operator;

	@Override
	public Object visit(ASTCreateStatement node, Object data) throws QueryParseException {
		attributes = new SDFAttributeList();
		name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		node.jjtGetChild(1).jjtAccept(this, data);
		SDFEntity entity = new SDFEntity(name);
		entity.setAttributes(attributes);
		dd.addSourceType(name, "RelationalStreaming");
		dd.addEntity(name, entity, caller);

		for (int i = 2; i < node.jjtGetNumChildren(); ++i) {
			node.jjtGetChild(i).jjtAccept(this, data);
		}

		return data;
	}

	@Override
	public Object visit(ASTTimedTuples node, Object data) throws QueryParseException {
		FixedSetAccessAO newPO = new FixedSetAccessAO(dd.createSDFSource(name), node.getTuples(attributes));
		newPO.setOutputSchema(attributes);
		dd.setStream(name, newPO, caller);
		return null;
	}

	@Override
	public Object visit(ASTPriorizedStatement node, Object data) throws QueryParseException {
		CQLParser parser = new CQLParser();
		parser.setUser(caller);
		parser.setDataDictionary(dd);
		operator = ((List<IQuery>) parser.visit(node, null)).get(0).getLogicalPlan();
		SDFAttributeList otherAttributes = operator.getOutputSchema();

		if (otherAttributes.size() != this.attributes.size()) {
			throw new QueryParseException("Query output does not match specified schema for: " + name);
		}
		ListIterator<SDFAttribute> li = otherAttributes.listIterator();
		for (SDFAttribute attr : this.attributes) {
			if (!((SDFAttribute) li.next()).getAttributeName().equals(((SDFAttribute) attr).getAttributeName())) {
				throw new QueryParseException("Query output does not match specified schema for: " + name);
			}
		}

		operator = addTimestampAO(operator);

		dd.setStream(name, operator, caller);
		return null;
	}

	private ILogicalOperator addTimestampAO(ILogicalOperator operator) {
		TimestampAO timestampAO = new TimestampAO();
		for (SDFAttribute attr : this.attributes) {
			if (attr.getDatatype().getURI().equals("StartTimestamp")) {
				timestampAO.setStartTimestamp(attr);
			}

			if (attr.getDatatype().getURI().equals("EndTimestamp")) {
				timestampAO.setEndTimestamp(attr);
			}
		}

		timestampAO.subscribeTo(operator, operator.getOutputSchema());
		return timestampAO;
	}

	@Override
	public Object visit(ASTAttributeDefinitions node, Object data) throws QueryParseException {
		node.childrenAccept(this, data);
		// check attributes for consistency
		boolean hasEndTimestamp = false, hasStartTimestamp = false;
		for (SDFAttribute attr : this.attributes) {
			if (attr.getDatatype().equals("StartTimestamp")) {
				if (hasStartTimestamp) {
					throw new QueryParseException("multiple definitions of StartTimestamp attribute not allowed");
				}
				hasStartTimestamp = true;
			}
			if (attr.getDatatype().equals("EndTimestamp")) {
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

		// we allow user defined types, so check
		// whether the defined type exists or not

		if (this.dd.existsDatatype(astAttrType.getType())) {

			attribute = new SDFAttribute(this.name, attrName, this.dd.getDatatype(astAttrType.getType()));
			if (attribute.getDatatype().isDate()) {
				attribute.addDtConstraint("format", astAttrType.getDateFormat());
			}
			if (attribute.getDatatype().isMeasurementValue() && astAttrType.jjtGetNumChildren() > 0) {
				attribute.setCovariance((List<?>) astAttrType.jjtGetChild(0).jjtAccept(this, data));

			}
		} else {
			throw new QueryParseException("illigal datatype:" + astAttrType.getType());
		}
		this.attributes.add(attribute);
		return data;
	}

	@Override
	public Object visit(ASTSocket node, Object data) throws QueryParseException {
		String host = ((ASTHost) node.jjtGetChild(0)).getValue();
		int port = -1;
		if (node.jjtGetNumChildren() >= 2) {
			// sollte ASTInteger sein
			port = ((ASTInteger) node.jjtGetChild(1)).getValue().intValue();

		} else {
			if (host.contains(":")) {
				String[] parts = host.split(":");
				host = parts[0];
				port = Integer.parseInt(parts[1]);
			}
		}
		AccessAO source = null;
		if (node.useTupleMode()) {
			source = new AccessAO(new SDFSource(name, "RelationalInputStreamAccessPO"));
		} else if (node.useMVMode()) {
			source = new AccessAO(new SDFSource(name, "RelationalAtomicDataInputStreamAccessMVPO"));
		} else {
			source = new AccessAO(new SDFSource(name, RelationalAccessSourceTypes.RELATIONAL_ATOMIC_DATA_INPUT_STREAM_ACCESS));
		}
		initSource(source, host, port);
		ILogicalOperator op = addTimestampAO(source);
		dd.setStream(name, op, caller);
		return data;
	}

	private void initSource(AccessAO source, String host, int port) {
		source.setPort(port);
		source.setHost(host);
		source.setOutputSchema(this.attributes);
	}

	@Override
	public Object visit(ASTChannel node, Object data) throws QueryParseException {
		String host = ((ASTHost) node.jjtGetChild(0)).getValue();
		boolean autoReconnect = hasAutoReconnect(node);
		int port = -1;
		if (node.jjtGetNumChildren() >= 2) {
			if (node.jjtGetChild(1) instanceof ASTInteger) {
				port = ((ASTInteger) node.jjtGetChild(1)).getValue().intValue();
			}

		} else {
			if (host.contains(":")) {
				String[] parts = host.split(":");
				host = parts[0];
				port = Integer.parseInt(parts[1]);
			}
		}
		AccessAO source = new AccessAO(new SDFSource(name, "RelationalByteBufferAccessPO"));
		source.setAutoReconnectEnabled(autoReconnect);
		initSource(source, host, port);
		ILogicalOperator op = addTimestampAO(source);
		dd.setStream(name, op, caller);
		return data;
	}

	private boolean hasAutoReconnect(ASTChannel node) {
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			if (node.jjtGetChild(i) instanceof ASTAutoReconnect) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object visit(ASTLoginPassword node, Object data) throws QueryParseException {
		String user = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		String password = ((ASTIdentifier) node.jjtGetChild(1)).getName();

		dd.getStream(name, caller).setLoginInfo(user, password);

		return null;
	}

	@Override
	public Object visit(ASTSilab node, Object data) throws QueryParseException {
		try {
			Class.forName("de.uniol.inf.is.odysseus.objecttracking.parser.SILABVisitor");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		IVisitor v = VisitorFactory.getInstance().getVisitor("Silab");
		v.setUser(caller);
		v.setDataDictionary(dd);
		return v.visit(node, data, this);
	}

	@Override
	public Object visit(ASTCreateFromDatabase node, Object data) throws QueryParseException {
		OutputSchemaSettable ao = (OutputSchemaSettable) invokeDatabaseVisitor(ASTCreateFromDatabase.class, node, name);
		ao.setOutputSchema(this.attributes);
		return addTimestampAO((ILogicalOperator) ao);
	}

	private Object invokeDatabaseVisitor(Class<?> nodeclass, Object node, Object data) throws QueryParseException {
		try {
			Class<?> visitor = Class.forName("de.uniol.inf.is.odysseus.database.cql.DatabaseVisitor");
			Object v = visitor.newInstance();
			Method m = visitor.getDeclaredMethod("setUser", User.class);
			m.invoke(v, caller);
			m = visitor.getDeclaredMethod("setDataDictionary", IDataDictionary.class);
			m.invoke(v, dd);
			m = visitor.getDeclaredMethod("visit", nodeclass, Object.class);
			return (AbstractLogicalOperator) m.invoke(v, node, data);
		} catch (ClassNotFoundException e) {
			throw new QueryParseException("Database plugin is missing in CQL parser.", e.getCause());
		} catch (NoSuchMethodException e) {
			throw new QueryParseException("Method in database plugin is missing.", e.getCause());
		} catch (SecurityException e) {
			throw new QueryParseException("Database plugin is missing in CQL parser.", e.getCause());
		} catch (IllegalAccessException e) {
			throw new QueryParseException("Database plugin is missing in CQL parser.", e.getCause());
		} catch (IllegalArgumentException e) {
			throw new QueryParseException("Database plugin is missing in CQL parser.", e.getCause());
		} catch (InvocationTargetException e) {
			throw new QueryParseException(e.getTargetException().getLocalizedMessage());
		} catch (InstantiationException e) {
			throw new QueryParseException("Cannot create instance of database plugin.", e.getCause());
		}
	}

}
