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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTComplexSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPartition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTWindow;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;

import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;

public class CreateAccessAOVisitor extends AbstractDefaultVisitor {

	private AttributeResolver attributeResolver;

	private ISession caller;
	private IDataDictionary dd;

	public CreateAccessAOVisitor(ISession user, IDataDictionary dd) {
		super();
		init();
		this.caller = user;
		this.dd = dd;
	}

	public final void init() {
		this.attributeResolver = new AttributeResolver();
	}

	public AttributeResolver getAttributeResolver() {
		return this.attributeResolver;
	}

	@Override
	public Object visit(ASTSimpleSource node, Object data)
			throws QueryParseException {
		Node childNode = node.jjtGetChild(0);
		String sourceString = ((ASTIdentifier) childNode).getName();
		if (dd.containsViewOrStream(sourceString, caller)) {
			relationalStreamingSource(node, sourceString);
			return null;
		} else {
			throw new QueryParseException("Unkown Source " + sourceString);
		}

	}

	private void relationalStreamingSource(ASTSimpleSource node,
			String sourceName) {
		ILogicalOperator access;
		try {
			access = dd.getViewOrStream(sourceName, caller);
			if (access instanceof AccessAO) {
				((AccessAO) access).setDataHandler(new TupleDataHandler()
						.getSupportedDataTypes().get(0));
				;
			}
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e.getMessage());
		}

		ILogicalOperator inputOp = access;
		if (node.hasAlias()) {
			inputOp = new RenameAO();
			inputOp.subscribeToSource(access, 0, 0, access.getOutputSchema());
			((RenameAO) inputOp).setOutputSchema(createAliasSchema(
					node.getAlias(), access));
			sourceName = node.getAlias();
		}

		if (node.hasWindow()) {
			WindowAO window = createWindow(node.getWindow(), inputOp);
			inputOp = window;
		}
		this.attributeResolver.addSource(sourceName, inputOp);
	}

	private static WindowAO createWindow(ASTWindow windowNode,
			ILogicalOperator inputOp) {
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
					if (curAttribute.getURI().equals(attributeName)) {
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
	public Object visit(ASTSubselect node, Object data)
			throws QueryParseException {
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
		rename.setOutputSchema(createAliasSchema(asIdentifier.getName(), result));
		this.attributeResolver.addSource(asIdentifier.getName(), rename);
		return null;
	}

	private static SDFSchema createAliasSchema(String alias,
			ILogicalOperator access) {
		// Keep the original Type not the alias
		List<SDFAttribute> attributes = new ArrayList<SDFAttribute>();
		for (SDFAttribute attribute : access.getOutputSchema()) {
			SDFAttribute newAttribute = (SDFAttribute) attribute.clone(alias,
					attribute.getAttributeName());
			// newAttribute.setSourceName(alias);
			attributes.add(newAttribute);
		}
		SDFSchema schema = new SDFSchema(access.getOutputSchema().getURI(),
				attributes);
		return schema;
	}

	@Override
	public Object visit(ASTBrokerSource node, Object data)
			throws QueryParseException {
		try {
			Class<?> brokerSourceVisitor = Class
					.forName("de.uniol.inf.is.odysseus.broker.parser.cql.BrokerVisitor");
			Object bsv = brokerSourceVisitor.newInstance();
			Method m = brokerSourceVisitor.getDeclaredMethod("setUser",
					ISession.class);
			m.invoke(bsv, caller);
			m = brokerSourceVisitor.getDeclaredMethod("setDataDictionary",
					IDataDictionary.class);
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

}
