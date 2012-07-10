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
/* Generated By:JJTree: Do not edit this line. ASTCreateUserStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public class ASTCreateUserStatement extends SimpleNode {

	String password;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		// Anfuehrungszeichen entfernen
		this.password = password.substring(1,password.length()-1);		
	}

	public ASTCreateUserStatement(int id) {
		super(id);
	}

	public ASTCreateUserStatement(NewSQLParser p, int id) {
		super(p, id);
	}

	/** Accept the visitor. 
	 * @throws QueryParseException **/
	@Override
	public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws QueryParseException {
		return visitor.visit(this, data);
	}
}
/*
 * JavaCC - OriginalChecksum=5af5ed8bcb69ce4c226219fe4067ef0a (do not edit this
 * line)
 */
