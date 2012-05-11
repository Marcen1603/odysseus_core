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

import de.uniol.inf.is.odysseus.context.ContextManagementException;
import de.uniol.inf.is.odysseus.context.store.ContextStoreManager;
import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.context.store.types.MultiElementStore;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
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
import de.uniol.inf.is.odysseus.relational.base.Tuple;

/**
 * 
 * @author Dennis Geesen Created at: 06.02.2012
 */
public class ContextVisitor implements IVisitor {

	private ISession session;
	private IDataDictionary datadictionary;

	public Object visit(ASTCreateContextStore node, Object data) {
		String name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		ASTAttributeDefinitions definitions = (ASTAttributeDefinitions) node.jjtGetChild(1);
		ASTContextStoreType typeNode = (ASTContextStoreType) node.jjtGetChild(2);

		CreateStreamVisitor csv = new CreateStreamVisitor(session, datadictionary);
		csv.visit(definitions, null);
		SDFSchema schema = new SDFSchema("ContextStore:" + name, csv.getAttributes());

		int size = visit(typeNode, schema);
		IContextStore<Tuple<? extends ITimeInterval>> store = new MultiElementStore<Tuple<? extends ITimeInterval>>(name, schema, size);

		try {
			ContextStoreManager.addStore(name, store);
		} catch (ContextManagementException e) {
			e.printStackTrace();
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
