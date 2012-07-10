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
/* Generated By:JJTree: Do not edit this line. ASTSimpleToken.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes;

import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.*;

public
@SuppressWarnings("all")
class ASTSimpleToken extends SimpleNode {
  
	private boolean hasMinus;
	
	public ASTSimpleToken(int id) {
    super(id);
  }

  public ASTSimpleToken(MapleResultParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MapleResultParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  public void setMinus(boolean hasMinus){
	  this.hasMinus = hasMinus;
  }
  
	@Override
	public String toString() {
		Node node = jjtGetChild(0);
		// only set the braces "(" and ")", if there is a "-" sign, because
		// we want to test against special variables like t for example.
		return "(" + (this.hasMinus ? "(-" + node.toString() + ")" : node.toString()) + ")";
	}
}
/* JavaCC - OriginalChecksum=ea5958b49ff19b3e0192ad9762dd5f30 (do not edit this line) */
