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
/* Generated By:JJTree: Do not edit this line. ASTConditionalAndExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

public
class ASTConditionalAndExpression extends SimpleNode {
  public ASTConditionalAndExpression(int id) {
    super(id);
  }

  public ASTConditionalAndExpression(SPARQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  @Override
public String toString(){
	  String str = "";
	  str += this.jjtGetChild(0).toString();
	  for(int i = 1; i<this.jjtGetNumChildren(); i++){
		  str += " && " + this.jjtGetChild(i);
	  }
	  
	  return str;
  }
}
/* JavaCC - OriginalChecksum=4f518aae9e4e086ec5cdb0ddefe363f9 (do not edit this line) */
