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
import de.uniol.inf.is.odysseus.context.store.ContextStore;
import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.parser.cql.IVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinitions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateContextStore;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateStreamVisitor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

/**
 * 
 * @author Dennis Geesen Created at: 06.02.2012
 */
public class ContextVisitor implements IVisitor {

	private ISession session;
	private IDataDictionary datadictionary;

	public Object visit(ASTCreateContextStore node, Object data) {
		String name = ((ASTIdentifier)node.jjtGetChild(0)).getName();
		System.out.println("Ctore name:" +name);
		ASTAttributeDefinitions definitions = (ASTAttributeDefinitions) node.jjtGetChild(1);
		
		CreateStreamVisitor csv = new CreateStreamVisitor(session, datadictionary);
		csv.visit(definitions, null);
		SDFSchema schema = new SDFSchema("ContextStore."+name,csv.getAttributes());
		try {
			ContextStore.getInstance().createStore(name, schema);			
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

	@Override
	public Object visit(SimpleNode node, Object data, Object baseObject) {
		if (node instanceof ASTCreateContextStore) {
			return this.visit((ASTCreateContextStore)node, data);
		}
		return null;
	}

}
