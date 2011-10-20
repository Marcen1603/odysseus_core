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

package de.uniol.inf.is.odysseus.sink.database.cql;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabaseSink;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabaseSinkOptions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHost;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInteger;
import de.uniol.inf.is.odysseus.sink.database.logicaloperator.DatabaseSinkAO;
import de.uniol.inf.is.odysseus.usermanagement.User;

/**
 * 
 * @author Dennis Geesen Created at: 20.10.2011
 */
public class DatabaseVisitor extends CQLParser {

	@Override
	public Object visit(ASTDatabaseSink node, Object data) {
		String name = (String) data;

		String databasetype = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		String databasename = ((ASTIdentifier) node.jjtGetChild(1)).getName();
		int offset = 0;
		boolean drop = false;
		boolean truncate = false;
		if(node.jjtGetChild(2) instanceof ASTDatabaseSinkOptions){
			offset = 1;
			ASTDatabaseSinkOptions options = (ASTDatabaseSinkOptions)node.jjtGetChild(2);
			String value = (String)options.jjtGetValue();
			if(value.trim().toUpperCase().equals("DROP")){
				drop = true;
			}
			if(value.trim().toUpperCase().equals("TRUNCATE")){
				truncate = true;
			}
			
		}
		
		String tablename = ((ASTIdentifier) node.jjtGetChild(2+offset)).getName();
		String user = "";
		String pass = "";
		String host = "localhost";
		int port = -1;
		if (node.jjtGetNumChildren() > (3+offset)) {
			if (node.jjtGetChild(3+offset) instanceof ASTHost) {
				host = ((ASTHost)node.jjtGetChild(3+offset)).getValue();
				port = ((ASTInteger) node.jjtGetChild(4+offset)).getValue().intValue();
				if (node.jjtGetNumChildren() > (6+offset)) {
					user = ((ASTIdentifier) node.jjtGetChild(5+offset)).getName();
					pass = ((ASTIdentifier) node.jjtGetChild(6+offset)).getName();
				}
			} else {
				user = ((ASTIdentifier) node.jjtGetChild(3)).getName();
				pass = ((ASTIdentifier) node.jjtGetChild(4)).getName();
			}
		}				
		DatabaseSinkAO sinkAO = new DatabaseSinkAO(name, databasetype, host, port, databasename, tablename, user, pass, drop, truncate);
		return sinkAO;
	}

	@Override
	public void setUser(User user) {
		super.setUser(user);
	}

	@Override
	public void setDataDictionary(IDataDictionary dataDictionary) {
		super.setDataDictionary(dataDictionary);
	}

}
