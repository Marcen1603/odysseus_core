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
/* Generated By:JJTree: Do not edit this line. ASTSimpleSource.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public class ASTSimpleSource extends SimpleNode {
  public ASTSimpleSource(int id) {
    super(id);
  }

  public ASTSimpleSource(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. 
 * @throws QueryParseException **/
  @Override
public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws QueryParseException {
    return visitor.visit(this, data);
  }
  
  public String getAlias() {
		switch (jjtGetNumChildren()) {
		case 2:
			return jjtGetChild(1) instanceof ASTAS ? jjtGetChild(1)
					.jjtGetChild(0).toString() : null;
		case 3:
			return jjtGetChild(2).jjtGetChild(0).toString();
		default:
			return null;
		}
	}

	public boolean hasAlias() {
		return getAlias() != null;
	}

//	/**
//	 * @return true, if a window is defined and it is not unbounded.
//	 */
	public boolean hasWindow() {
		return  jjtGetNumChildren() > 1
				&& jjtGetChild(1) instanceof ASTWindow;
	}

	public ASTWindow getWindow() {
		if (hasWindow()) {
			return (ASTWindow) jjtGetChild(1);
		}
		return null;
	}

}
/* JavaCC - OriginalChecksum=7bb44f3efa4029c86f6236c203310cb1 (do not edit this line) */
