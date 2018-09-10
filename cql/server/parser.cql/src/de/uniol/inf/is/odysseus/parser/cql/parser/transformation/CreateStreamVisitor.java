/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.dd.CreateStreamCommand;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
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
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTFileSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHost;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIfNotExists;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInteger;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTLoginPassword;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPriorizedStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSilab;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSocket;

/**
 * @author Jonas Jacobi
 */
@SuppressWarnings({ "unchecked" })
public class CreateStreamVisitor extends AbstractDefaultVisitor {
	String name;
	private ISession caller;
	private IDataDictionary dd;

	List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
	ILogicalOperator operator;
	private List<IExecutorCommand> commands;
	private IMetaAttribute metaAttribute;

	public CreateStreamVisitor(ISession user, IDataDictionary dd,
			List<IExecutorCommand> commands, IMetaAttribute metaAttribute) {
		this.caller = user;
		this.dd = dd;
		this.commands = commands;
		this.metaAttribute = metaAttribute;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<SDFAttribute> getAttributes() {
		return attributes;
	}

	public ILogicalOperator getOperator() {
		return operator;
	}

	public void setOperator(ILogicalOperator operator) {
		this.operator = operator;
		((AbstractAccessAO) operator).setDataHandler(new TupleDataHandler()
				.getSupportedDataTypes().get(0));
	}

	@Override
	public Object visit(ASTCreateStatement node, Object data)
			throws QueryParseException {
		int startOtherValues = 2;
		name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		if (node.jjtGetNumChildren() > 2) {
			if (node.jjtGetChild(2) instanceof ASTIfNotExists) {
				startOtherValues = 3;
				if (this.dd.containsViewOrStream(name, caller)) {
					return data;
				}
			}
		}

		node.jjtGetChild(1).jjtAccept(this, data);
		for (int i = startOtherValues; i < node.jjtGetNumChildren(); ++i) {
			node.jjtGetChild(i).jjtAccept(this, data);
		}

		return data;
	}

	@Override
	public Object visit(ASTPriorizedStatement node, Object data)
			throws QueryParseException {
		CQLParser parser = new CQLParser();
		parser.setUser(caller);
		parser.setDataDictionary(dd);
		operator = ((List<ILogicalQuery>) parser.visit(node, null)).get(0)
				.getLogicalPlan().getRoot();
		SDFSchema otherAttributes = operator.getOutputSchema();

		if (otherAttributes.size() != this.attributes.size()) {
			throw new QueryParseException(
					"Query output does not match specified schema for: " + name);
		}
		ListIterator<SDFAttribute> li = otherAttributes.listIterator();
		for (SDFAttribute attr : this.attributes) {
			if (!li.next().getAttributeName().equals(attr.getAttributeName())) {
				throw new QueryParseException(
						"Query output does not match specified schema for: "
								+ name);
			}
		}

		CreateStreamCommand cmd = new CreateStreamCommand(name, operator,
				caller);
		commands.add(cmd);
		return cmd;
	}

	@Override
	public Object visit(ASTAttributeDefinitions node, Object data)
			throws QueryParseException {
		node.childrenAccept(this, data);
		// check attributes for consistency
		boolean hasEndTimestamp = false, hasStartTimestamp = false;
		for (SDFAttribute attr : this.attributes) {
			if (attr.getDatatype().equals(SDFDatatype.START_TIMESTAMP)) {
				if (hasStartTimestamp) {
					throw new QueryParseException(
							"multiple definitions of StartTimestamp attribute not allowed");
				}
				hasStartTimestamp = true;
			}
			if (attr.getDatatype().equals(SDFDatatype.END_TIMESTAMP)) {
				if (hasEndTimestamp) {
					throw new QueryParseException(
							"multiple definitions of EndTimestamp attribute not allowed");
				}
				hasEndTimestamp = true;
			}
			if (Collections.frequency(this.attributes, attr) > 1) {
				throw new QueryParseException(
						"ambiguous attribute definition: " + attr.toString());
			}
		}
		return null;
	}

	@Override
	public Object visit(ASTAttributeDefinition node, Object data)
			throws QueryParseException {
		String attrName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		SDFAttribute attribute = null;
		ASTAttributeType astAttrType = (ASTAttributeType) node.jjtGetChild(1);
		List<SDFConstraint> dtConstrains = new LinkedList<>();

		// we allow user defined types, so check
		// whether the defined type exists or not

		SDFDatatype attribType;
		try {
			attribType = this.dd.getDatatype(astAttrType.getType());
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e.getMessage());
		}

		if (attribType.isDate()) {
			dtConstrains.add(new SDFConstraint("format", astAttrType
					.getDateFormat()));
		}
		Resource resource = new Resource(this.caller.getUser(), this.name);
		if (attribType.isMeasurementValue()
				&& astAttrType.jjtGetNumChildren() > 0) {
			attribute = new SDFAttribute(resource.toString(), attrName,
					attribType, null, dtConstrains, (List<?>) astAttrType
							.jjtGetChild(0).jjtAccept(this, data));
		}
		if (attribute == null) {
			attribute = new SDFAttribute(resource.toString(), attrName,
					attribType, null, dtConstrains);
		}
		this.attributes.add(attribute);
		return data;
	}

	@Override
	public Object visit(ASTSocket node, Object data) throws QueryParseException {
		throw new QueryParseException(
				"SOCKET is no longer supported by CQL. Use PQL instead to create such sources!");

	}

	@Override
	public Object visit(ASTChannel node, Object data)
			throws QueryParseException {
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
		String wrapperName = Constants.GENERIC_PUSH;
		OptionMap options = new OptionMap();
		options.setOption("host", host);
		options.setOption("port", new Integer(port));
		options.setOption("autoconnect", new Boolean(autoReconnect));
		Resource resource = new Resource(caller.getUser(), name);
		AccessAO source = new AccessAO(resource, wrapperName, "NonBlockingTcp",
				"SizeByteBuffer", new TupleDataHandler()
						.getSupportedDataTypes().get(0), options);
		SDFSchema outputSchema = createOutputSchema(resource);
		source.setOutputSchema(outputSchema);
		source.setLocalMetaAttribute(metaAttribute);
		CreateStreamCommand cmd = new CreateStreamCommand(name, source, caller);
		commands.add(cmd);
		return cmd;
	}

	@Override
	public Object visit(ASTFileSource node, Object data)
			throws QueryParseException {
		String filename = node.getFilename();
		String type = "csv";
		if (node.jjtGetNumChildren() > 0) {
			type = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		}
		String wrapperName = Constants.GENERIC_PULL;
		OptionMap options = new OptionMap();
		options.setOption("filename", filename);

		// TODO: read delimiter
		options.setOption("delimiter", ";");
		Resource resource = new Resource(caller.getUser(), name);
		AccessAO source = new AccessAO(resource, wrapperName, "File", type,
				new TupleDataHandler().getSupportedDataTypes().get(0), options);
		SDFSchema outputSchema = createOutputSchema(resource);
		source.setOutputSchema(outputSchema);
		source.setLocalMetaAttribute(metaAttribute);
		CreateStreamCommand cmd = new CreateStreamCommand(name, source, caller);
		commands.add(cmd);
		return cmd;

	}

	private SDFSchema createOutputSchema(Resource resource) {
		SDFSchema outputSchema = SDFSchemaFactory.createNewTupleSchema(
				resource.toString(), this.attributes);
		if (metaAttribute != null) {
			outputSchema = SDFSchemaFactory.createNewWithMetaSchema(
					outputSchema, metaAttribute.getSchema());
		}
		return outputSchema;
	}

	private static boolean hasAutoReconnect(ASTChannel node) {
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			if (node.jjtGetChild(i) instanceof ASTAutoReconnect) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object visit(ASTLoginPassword node, Object data)
			throws QueryParseException {
		// TODO: reacivate
		// String user = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		// String password = ((ASTIdentifier) node.jjtGetChild(1)).getName();
		//
		// try {
		// // TODO:
		// //dd.getStream(name, caller).setLoginInfo(user, password);
		// } catch (DataDictionaryException e) {
		// throw new QueryParseException(e.getMessage());
		// }

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
	public Object visit(ASTCreateFromDatabase node, Object data)
			throws QueryParseException {
		CreateStreamCommand cmd = (CreateStreamCommand) invokeDatabaseVisitor(
				ASTCreateFromDatabase.class, node, name);
		cmd.setOutputSchema(SDFSchemaFactory.createNewTupleSchema(name,
				attributes));

		commands.add(cmd);
		// return addTimestampAO((ILogicalOperator) ao);
		// return ao;
		return null;
	}

	private Object invokeDatabaseVisitor(Class<?> nodeclass, Object node,
			Object data) throws QueryParseException {
		try {
			Class<?> visitor = Class
					.forName("de.uniol.inf.is.odysseus.database.cql.DatabaseVisitor");
			Object v = visitor.newInstance();
			Method m = visitor.getDeclaredMethod("setUser", ISession.class);
			m.invoke(v, caller);
			m = visitor.getDeclaredMethod("setDataDictionary",
					IDataDictionary.class);
			m.invoke(v, dd);
			m = visitor.getDeclaredMethod("setMetaAttribute",
					IMetaAttribute.class);
			m.invoke(v, metaAttribute);
			m = visitor.getDeclaredMethod("visit", nodeclass, Object.class);
			return m.invoke(v, node, data);
		} catch (ClassNotFoundException e) {
			throw new QueryParseException(
					"Database plugin is missing in CQL parser.", e.getCause());
		} catch (NoSuchMethodException e) {
			throw new QueryParseException(
					"Method in database plugin is missing.", e.getCause());
		} catch (SecurityException e) {
			throw new QueryParseException(
					"Database plugin is missing in CQL parser.", e.getCause());
		} catch (IllegalAccessException e) {
			throw new QueryParseException(
					"Database plugin is missing in CQL parser.", e.getCause());
		} catch (IllegalArgumentException e) {
			throw new QueryParseException(
					"Database plugin is missing in CQL parser.", e.getCause());
		} catch (InvocationTargetException e) {
			throw new QueryParseException(e.getTargetException()
					.getLocalizedMessage());
		} catch (InstantiationException e) {
			throw new QueryParseException(
					"Cannot create instance of database plugin.", e.getCause());
		}
	}

}
