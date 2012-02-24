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
/* Generated By:JJTree: Do not edit this line. ASTBasicPredicate.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public class ASTBasicPredicate extends AbstractPredicate {
  public ASTBasicPredicate(int id) {
    super(id);
  }

  public ASTBasicPredicate(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. 
 * @throws QueryParseException **/
  @Override
public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws QueryParseException {
    return visitor.visit(this, data);
  }
  
  public String getPredicate() {
	  return jjtGetChild(0).toString() + jjtGetChild(1).toString()
		+ jjtGetChild(2).toString();
  }
  
  @Override
public String toString() {
	  return getPredicate();
  }
}
/* JavaCC - OriginalChecksum=50212c02773f7976ea12aa8bd484a7db (do not edit this line) */
