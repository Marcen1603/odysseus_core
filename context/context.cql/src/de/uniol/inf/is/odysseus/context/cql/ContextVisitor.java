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

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.context.ContextManagementException;
import de.uniol.inf.is.odysseus.context.store.ContextStoreManager;
import de.uniol.inf.is.odysseus.context.store.IContextStore;
import de.uniol.inf.is.odysseus.context.store.types.ContextStoreFactory;
import de.uniol.inf.is.odysseus.context.store.types.ContextStoreType;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.cql.IVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinitions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTContextStoreType;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateContextStore;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifierList;
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
		// default type
		ContextStoreType storeType = visit(typeNode, schema);
		
		
		
		IContextStore<Tuple<? extends ITimeInterval>> store = ContextStoreFactory.createStore(name, storeType, schema);
		
		// get Type
		
		
		
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

	public ContextStoreType visit(ASTContextStoreType node, Object data) throws QueryParseException {
		SDFSchema schema = (SDFSchema)data;
		String typeName = node.jjtGetValue().toString();
		ContextStoreType type = null;
		for (ContextStoreType ct : ContextStoreType.values()) {
			if (ct.toString().equalsIgnoreCase(typeName)) {
				type = ct;
				break;
			}
		}
		if (type == null) {
			throw new QueryParseException("Type for context store does not exist!");
		}
//		
//		List<SDFAttribute> keys = new ArrayList<SDFAttribute>();
//		if((node.jjtGetNumChildren()==1) && node.jjtGetChild(0) instanceof ASTIdentifierList){
//			ASTIdentifierList definitions = (ASTIdentifierList) node.jjtGetChild(0);
//			List<String> keyNames = visit(definitions, null);
//			DirectAttributeResolver ar = new DirectAttributeResolver(schema);
//			for(String keyName : keyNames){
//				SDFAttribute a = ar.getAttribute(keyName);
//				keys.add(a);
//			}
//		}		
		
		return type;				
	}

	private List<String> visit(ASTIdentifierList definitions, Object data) {
		List<String> names = new ArrayList<String>();
		for(int i=0;i<definitions.jjtGetNumChildren();i++){
			ASTIdentifier id = (ASTIdentifier) definitions.jjtGetChild(i);
			names.add(id.getName());
		}
		return names;
	}

	@Override
	public Object visit(SimpleNode node, Object data, Object baseObject) {
		if (node instanceof ASTCreateContextStore) {
			return this.visit((ASTCreateContextStore) node, data);
		}
		return null;
	}


}
