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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.QueryAccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTComplexSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDBSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPartition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTWindow;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class CreateAccessAOVisitor extends AbstractDefaultVisitor {

	private AttributeResolver attributeResolver;

	private Map<SDFSource, ILogicalOperator> sources;

	private User caller;
	private IDataDictionary dd;

	public CreateAccessAOVisitor(User user, IDataDictionary dd) {
		super();
		init();
		this.caller = user;
		this.dd = dd;
	}

	public final void init() {
		this.attributeResolver = new AttributeResolver();
		this.sources = new HashMap<SDFSource, ILogicalOperator>();
	}

	public AttributeResolver getAttributeResolver() {
		return this.attributeResolver;
	}

	@Override
	public Object visit(ASTSimpleSource node, Object data) throws QueryParseException {
		Node childNode = node.jjtGetChild(0);
		String sourceString = ((ASTIdentifier) childNode).getName();
		SDFSource source = dd.createSDFSource(sourceString);
		if (source.getSourceType().equals("RelationalStreaming")) {
			relationalStreamingSource(node, source, sourceString);
			return null;
		} else if (source.getSourceType().equals("brokerStreaming")) {
			brokerStreamingSource(node, data);
			return null;
		} else if (source.getSourceType().equals("ObjectRelationalStreaming")) {
			relationalStreamingSource(node, source, sourceString);
			return null;
		} else {
			throw new QueryParseException("unknown type of source '"
					+ source.getSourceType() + "' for source: " + sourceString);
		}

		// case Relational:
		// relationalSource(node, source, sourceString);
		// return null;
		// case OSGI:
		// WrapperArchitectureAO access = (WrapperArchitectureAO)
		// sources.get(source);
		// this.attributeResolver.addSource(node.getAlias(), access);
		// return null;
		// default:

	}

	// private void sensorStreamSource(ASTSimpleSource node, SDFSource source,
	// String sourceName) {
	// ILogicalOperator access = this.sources.get(source);
	// if (access == null) {
	// access = new AccessAO(source);
	// SDFEntity entity = null;
	// try {
	// entity = DataDictionary.getInstance().getEntity(sourceName);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// ((AccessAO) access).setOutputSchema(entity.getAttributes());
	//
	// this.sources.put(source, access);
	// }
	//
	// ILogicalOperator inputOp = access;
	// if (node.hasAlias()) {
	// inputOp = new RenameAO();
	// inputOp.subscribeToSource(access, 0, 0, access.getOutputSchema());
	// ((RenameAO) inputOp).setOutputSchema(createAliasSchema(node.getAlias(),
	// access));
	// sourceName = node.getAlias();
	// }
	//
	// if (node.hasWindow()) {
	// WindowAO window = createWindow(node.getWindow(), inputOp);
	// inputOp = window;
	// }
	// this.attributeResolver.addSource(sourceName, inputOp);
	// }

	// FIXME funktioniert nicht mehr, da datadictionary nicht mehr auf db basis
	// arbeitet
	// und noch keine neue repraesentation von relationsinfos da ist
	@SuppressWarnings("unused")
	private void relationalSource(ASTSimpleSource node, SDFSource source,
			String sourceString) {
		QueryAccessAO access = (QueryAccessAO) this.sources.get(source);
		if (access == null) {
			access = new QueryAccessAO(source);
			// SDFAttributeList attributes = DataDictionary.getInstance()
			// .attributesOfRelation(sourceString);
			// access.setOutputSchema(attributes);
			this.sources.put(source, access);
		}

		AbstractLogicalOperator operator = access;
		if (node.hasAlias()) {
			sourceString = node.getAlias();
			operator = new RenameAO();
			SDFAttributeList newSchema = createAliasSchema(node.getAlias(),
					access);
			operator.subscribeToSource(access, 0, 0, newSchema);
		}

		this.attributeResolver.addSource(sourceString, operator);
	}

	private void relationalStreamingSource(ASTSimpleSource node,
			SDFSource source, String sourceName) {
		ILogicalOperator access = dd.getViewOrStream(sourceName, caller);
		

		ILogicalOperator inputOp = access;
		if (node.hasAlias()) {
			inputOp = new RenameAO();
			inputOp.subscribeToSource(access, 0, 0, access.getOutputSchema());
			((RenameAO) inputOp).setOutputSchema(createAliasSchema(node
					.getAlias(), access));
			sourceName = node.getAlias();
		}

		if (node.hasWindow()) {
			WindowAO window = createWindow(node.getWindow(), inputOp);
			inputOp = window;
		}
		this.attributeResolver.addSource(sourceName, inputOp);
	}

	private WindowAO createWindow(ASTWindow windowNode, ILogicalOperator inputOp) {
		WindowAO window = new WindowAO();
		window.subscribeToSource(inputOp, 0, 0, inputOp.getOutputSchema());

		if (windowNode.isPartitioned()) {
			// if (containsWindow(inputOp)) {
			// throw new IllegalArgumentException(
			// "redefinition of window in subselect");
			// }
			ASTPartition partition = windowNode.getPartition();
			ArrayList<SDFAttribute> partitionAttributes = new ArrayList<SDFAttribute>();
			for (int i = 0; i < partition.jjtGetNumChildren(); ++i) {
				String attributeName = partition.jjtGetChild(i).toString();
				boolean found = false;
				for (SDFAttribute curAttribute : inputOp.getOutputSchema()) {
					if (curAttribute.getPointURI().equals(attributeName)) {
						partitionAttributes.add(curAttribute);
						found = true;
						break;
					}
				}
				if (!found) {
					throw new IllegalArgumentException(
							"invalid partioning attribute");
				}
			}
			window.setPartitionBy(partitionAttributes);
		}

		window.setWindowType(windowNode.getType());
		
		if (!windowNode.isUnbounded()) {
			window.setWindowSize(windowNode.getSize());
			Long advance = windowNode.getAdvance();
			window.setWindowAdvance(advance != null ? advance : 1);
			if (windowNode.getSlide() != null) {
				window.setWindowSlide(windowNode.getSlide());
			}
		}
		return window;
	}

	// private boolean containsWindow(ILogicalOperator inputOp) {
	// if (inputOp instanceof WindowAO) {
	// return true;
	// }
	// int numberOfInputs = inputOp.getSubscribedToSource().size();
	// if (inputOp instanceof ExistenceAO) {
	// numberOfInputs = 1;// don't check subselects in existenceaos
	// }
	// for (int i = 0; i < numberOfInputs; ++i) {
	// if (containsWindow(inputOp.getSubscribedToSource(i).getTarget())) {
	// return true;
	// }
	// }
	// return false;
	// }

	@Override
	public Object visit(ASTSubselect node, Object data) throws QueryParseException {
		ASTComplexSelectStatement childNode = (ASTComplexSelectStatement) node
				.jjtGetChild(0);
		CQLParser v = new CQLParser();
		v.setUser(caller);
		v.setDataDictionary(dd);
		AbstractLogicalOperator result = (AbstractLogicalOperator) v.visit(
				childNode, null);

		Node asNode = node.jjtGetChild(1);
		if (asNode instanceof ASTWindow) {
			WindowAO window = createWindow((ASTWindow) asNode, result);
			result = window;
			asNode = node.jjtGetChild(2);
		}
		ASTIdentifier asIdentifier = (ASTIdentifier) asNode.jjtGetChild(0);
		RenameAO rename = new RenameAO();
		rename.subscribeToSource(result, 0, 0, result.getOutputSchema());
		rename
				.setOutputSchema(createAliasSchema(asIdentifier.getName(),
						result));
		this.attributeResolver.addSource(asIdentifier.getName(), rename);
		return null;
	}

	private SDFAttributeList createAliasSchema(String alias,
			ILogicalOperator access) {
		SDFAttributeList attributes = new SDFAttributeList();
		for (SDFAttribute attribute : access.getOutputSchema()) {
			SDFAttribute newAttribute = (SDFAttribute) attribute.clone();
			newAttribute.setSourceName(alias);
			attributes.add(newAttribute);
		}
		return attributes;
	}

	@Override
	public Object visit(ASTDBSelectStatement node, Object data) throws QueryParseException {
		Class<?> dbClass;
		try {
			dbClass = Class
					.forName("de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateDatabaseAOVisitor");
			IDatabaseAOVisitor dbVisitor = (IDatabaseAOVisitor) dbClass
					.newInstance();
			
			Method m = dbClass.getDeclaredMethod("setUser", User.class);
			m.invoke(dbVisitor, caller);
			m = dbClass.getDeclaredMethod("setDataDictionary", IDataDictionary.class);
			m.invoke(dbVisitor, dd);

			
			AbstractLogicalOperator dbOp = (AbstractLogicalOperator) dbVisitor
					.visit(node, data);
			this.attributeResolver.addSource(dbVisitor.getAlias(), dbOp);

		} catch (Exception e) {
			throw new QueryParseException("missing database plugin for cql parser");
		}
		return null;
	}

	@Override
	public Object visit(ASTBrokerSource node, Object data) throws QueryParseException {
		try {
			Class<?> brokerSourceVisitor = Class
					.forName("de.uniol.inf.is.odysseus.broker.parser.cql.BrokerVisitor");
			Object bsv = brokerSourceVisitor.newInstance();
			Method m = brokerSourceVisitor.getDeclaredMethod("setUser", User.class);
			m.invoke(bsv, caller);
			m = brokerSourceVisitor.getDeclaredMethod("setDataDictionary", IDataDictionary.class);
			m.invoke(bsv, dd);

			m = brokerSourceVisitor.getDeclaredMethod("visit",
					ASTBrokerSource.class, Object.class);
			AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m
					.invoke(bsv, node, data);
			Node child = node.jjtGetChild(0);
			ASTIdentifier ident = (ASTIdentifier) child.jjtGetChild(child
					.jjtGetNumChildren() - 1);
			String name = ident.getName();
			this.attributeResolver.addSource(name, sourceOp);
		} catch (ClassNotFoundException ex) {
			throw new QueryParseException(
					"Brokerplugin is missing in CQL parser.", ex.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new QueryParseException(
					"Error while accessing broker as source.", e.getCause());
		}

		return null;
	}

	private Object brokerStreamingSource(ASTSimpleSource node, Object data) throws QueryParseException {
		try {
			Class<?> brokerSourceVisitor = Class
					.forName("de.uniol.inf.is.odysseus.broker.parser.cql.BrokerVisitor");
			Object bsv = brokerSourceVisitor.newInstance();
			Method m = brokerSourceVisitor.getDeclaredMethod("setUser", User.class);
			m.invoke(bsv, caller);
			m = brokerSourceVisitor.getDeclaredMethod("setDataDictionary", IDataDictionary.class);
			m.invoke(bsv, dd);
			
			m = brokerSourceVisitor.getDeclaredMethod("visit",
					ASTSimpleSource.class, Object.class);
			AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m
					.invoke(bsv, node, data);

			ASTIdentifier ident = (ASTIdentifier) node.jjtGetChild(0);
			String name = ident.getName();
			this.attributeResolver.addSource(name, sourceOp);
		} catch (ClassNotFoundException ex) {
			throw new QueryParseException(
					"Brokerplugin is missing in CQL parser.", ex.getCause());
		} catch (Exception e) {
			throw new QueryParseException(
					"Error while creating broker as source.", e.getCause());
		}

		return null;
	}
}
