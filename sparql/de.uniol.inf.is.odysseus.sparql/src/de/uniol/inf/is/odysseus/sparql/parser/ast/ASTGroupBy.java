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
/* Generated By:JJTree: Do not edit this line. ASTGroupBy.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.sparql.parser.helper.Variable;

public
class ASTGroupBy extends SimpleNode {
  
	private List<Variable> variables;
	
	public ASTGroupBy(int id) {
    super(id);
    this.variables = new ArrayList<Variable>();
  }

  public ASTGroupBy(SPARQLParser p, int id) {
    super(p, id);
    this.variables = new ArrayList<Variable>();
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  public void addVariable(Variable v){
	  this.variables.add(v);
  }
  
  public List<Variable> getVariables(){
	  return this.variables;
  }
  
}
/* JavaCC - OriginalChecksum=caa36fda60a813fd49cfcaa3d6fbb262 (do not edit this line) */
