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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ElementWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PredicateWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ReceiveAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RenameAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimeWindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.WindowAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.TimeValueItem;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTComplexSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPartition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTWindow;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;

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

	private void relationalStreamingSource(ASTSimpleSource node, String srcName) {
		ILogicalOperator access;
		Resource originalName = dd.getViewOrStreamName(srcName, caller);
		String sourceName = srcName;
		try {
			access = dd.getViewOrStream(srcName, caller).getRoot();
			if (access instanceof AbstractAccessAO) {
				((AbstractAccessAO) access)
						.setDataHandler(new TupleDataHandler()
								.getSupportedDataTypes().get(0));
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
		} else {
			if (!(inputOp instanceof AccessAO)
					&& !(inputOp instanceof ReceiveAO)
					&& !(inputOp instanceof StreamAO) 
					&& !(inputOp instanceof RenameAO)) {
				for (String n : inputOp.getOutputSchema().getBaseSourceNames()) {

					try {
						this.attributeResolver.addSource(n, inputOp);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
			}
		}

		if (node.hasWindow()) {
			AbstractWindowAO window = createWindow(node.getWindow(), inputOp);
			inputOp = window;
		}
		this.attributeResolver.addSourceOriginal(originalName.toString(),
				inputOp);
		try {
			this.attributeResolver.addSource(originalName, inputOp);
			if (!sourceName.equals(originalName.toString())) {
				this.attributeResolver.addSource(sourceName, inputOp);
			}
		} catch (Exception e) {
			// ignore
			e.printStackTrace();
		}

	}

	private static AbstractWindowAO createWindow(ASTWindow windowNode,
			ILogicalOperator inputOp) {
		AbstractWindowAO window;
		switch (windowNode.getType()){
		case PREDICATE:
			window = new PredicateWindowAO();
			break;
		case TIME:
			window = new TimeWindowAO();
			break;
		case TUPLE:
			window =  new ElementWindowAO(); 
			break;
		case UNBOUNDED:
			window =  new WindowAO(); 
			break;
		default:
			window =  new WindowAO(); 
		}
		
		window.subscribeToSource(inputOp, 0, 0, inputOp.getOutputSchema());

		if (windowNode.isPartitioned()) {
			// if (containsWindow(inputOp)) {
			// throw new IllegalArgumentException(
			// "redefinition of window in subselect");
			// }
			ASTPartition partition = windowNode.getPartition();
			ArrayList<SDFAttribute> partitionAttributes = new ArrayList<SDFAttribute>();
			SDFSchema inputSchema = inputOp.getOutputSchema();
			for (int i = 0; i < partition.jjtGetNumChildren(); ++i) {
				String attributeName = partition.jjtGetChild(i).toString();
				boolean found = false;
				
				SDFAttribute pAttr = inputSchema.findAttribute(attributeName);
				if (pAttr != null){	
						partitionAttributes.add(pAttr);
						found = true;
				}
				if (!found) {
					throw new IllegalArgumentException(
							"partioning attribute "+attributeName+" not found in input "+inputOp.getName());
				}
			}
			window.setPartitionBy(partitionAttributes);
		}

		window.setWindowType(windowNode.getType());

		if (!windowNode.isUnbounded()) {
			window.setWindowSize(new TimeValueItem(windowNode.getSize(),
					TimeUnit.MILLISECONDS));
			Long advance = windowNode.getAdvance();
			long advanceValue = advance != null ? advance : 1;
			window.setWindowAdvance(new TimeValueItem(advanceValue,
					TimeUnit.MILLISECONDS));
			if (windowNode.getSlide() != null) {
				window.setWindowSlide(new TimeValueItem(windowNode.getSlide(),
						TimeUnit.MILLISECONDS));
			}
		}
		return window;
	}

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
			AbstractWindowAO window = createWindow((ASTWindow) asNode, result);
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
			SDFAttribute newAttribute = attribute.clone(alias,
					attribute.getAttributeName());
			// newAttribute.setSourceName(alias);
			attributes.add(newAttribute);
		}
		SDFSchema schema = 	SDFSchemaFactory.createNewWithAttributes(attributes, access.getOutputSchema());
		return schema;
	}

}
