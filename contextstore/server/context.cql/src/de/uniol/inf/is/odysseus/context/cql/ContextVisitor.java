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
/** Copyright 2011 The Odysseus Team
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

package de.uniol.inf.is.odysseus.context.cql;

import java.util.List;

import de.uniol.inf.is.odysseus.context.ContextManagementException;
import de.uniol.inf.is.odysseus.context.store.ContextStoreManager;
import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.context.store.types.MultiElementStore;
import de.uniol.inf.is.odysseus.context.store.types.PartitionedMultiElementStore;
//import de.uniol.inf.is.odysseus.context.store.types.MultiElementStore;
import de.uniol.inf.is.odysseus.context.store.types.SingleElementStore;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.parser.cql.IVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinitions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTContextStoreType;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateContextStore;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDropContextStore;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIfExists;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInteger;
import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateStreamVisitor;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;
//import de.uniol.inf.is.odysseus.wrapper.ivef.contextmodel.contextstore.HistoryContextStore;

/**
 * 
 * @author Dennis Geesen Created at: 06.02.2012
 */
public class ContextVisitor implements IVisitor {

	private ISession session;
	private IDataDictionary datadictionary;
	private IMetaAttribute metaAttribute;
	private List<IExecutorCommand> commands;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Object visit(ASTCreateContextStore node, Object data) {
		String name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		ASTAttributeDefinitions definitions = (ASTAttributeDefinitions) node.jjtGetChild(1);
		ASTContextStoreType typeNode = (ASTContextStoreType) node.jjtGetChild(2);

		CreateStreamVisitor csv = new CreateStreamVisitor(session, datadictionary, commands, metaAttribute);
		csv.visit(definitions, null);
		SDFSchema schema = SDFSchemaFactory.createNewTupleSchema("ContextStore:" + name, csv.getAttributes());
		ITimeIntervalSweepArea sa;
		try {
			sa = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(DefaultTISweepArea.NAME);
		} catch (InstantiationException | IllegalAccessException e1) {
			throw new QueryParseException(e1);
		}
		
		int size = visit(typeNode, schema);
        IContextStore<Tuple<? extends ITimeInterval>> store;
        if (size == 1) {
            store = new SingleElementStore<Tuple<? extends ITimeInterval>>(name, schema);
        }
        else {
        	if(typeNode.jjtGetNumChildren() == 1)
        		store = new MultiElementStore<Tuple<? extends ITimeInterval>>(name, schema, size, sa);
        	else{
        		int partitionBy = ((ASTInteger) typeNode.jjtGetChild(1)).getValue().intValue();
        		if(partitionBy < 0 || partitionBy >= schema.size())
        			throw new QueryParseException("Partition key index is not compatiple with the schema!");
        		store = new PartitionedMultiElementStore<Tuple<? extends ITimeInterval>>(name, schema, size, partitionBy);
        	}
        }

		try {
			ContextStoreManager.addStore(name, store);
		} catch (ContextManagementException e) {
			throw new QueryParseException(e);
		}
		return null;
	}

	@Override
	public void setUser(ISession user) {
		session = user;
	}

	@Override
	public void setDataDictionary(IDataDictionary dd) {
		this.datadictionary = dd;
	}
	
	@Override
	public void setMetaAttribute(IMetaAttribute metaAttribute) {
		this.metaAttribute = metaAttribute;
	}
	
	@Override
	public void setCommands(List<IExecutorCommand> commands) {
		this.commands = commands;
	}

	public int visit(ASTContextStoreType node, Object data) throws QueryParseException {
		String typeName = node.jjtGetValue().toString().trim();
		if (typeName.equalsIgnoreCase("SINGLE")) {
			return 1;
		}
		if (typeName.equalsIgnoreCase("MULTI")) {
			return ((ASTInteger) node.jjtGetChild(0)).getValue().intValue();
		}
		throw new QueryParseException("Type for context store does not exist!");
	}

	@Override
	public Object visit(SimpleNode node, Object data, Object baseObject) {
		if (node instanceof ASTCreateContextStore) {
			return this.visit((ASTCreateContextStore) node, data);
		}
		if (node instanceof ASTDropContextStore) {
			return this.visit((ASTDropContextStore) node, data);
		}
		return null;
	}

	public Object visit(ASTDropContextStore node, Object data) throws QueryParseException {
		ASTIdentifier identifier = (ASTIdentifier) node.jjtGetChild(0);
		String name = identifier.getName();
		if(ContextStoreManager.storeExists(name)){
			ContextStoreManager.removeStore(name);
		}else{
			if(!((node.jjtGetNumChildren()>=2) && (node.jjtGetChild(1) instanceof ASTIfExists))){			
				throw new QueryParseException("There is no store named \""+name+"\"");
			}
		}		
		return null;
	}

}
