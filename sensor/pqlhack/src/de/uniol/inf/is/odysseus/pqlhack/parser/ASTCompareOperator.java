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
/* Generated By:JJTree: Do not edit this line. ASTCompareOperator.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.pqlhack.parser;

public
class ASTCompareOperator extends SimpleNode {
  public ASTCompareOperator(int id) {
    super(id);
  }

  public ASTCompareOperator(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
	private String operator;
	  
	  public void setOperator(String operator){
			if (operator.equals("=")) {
				this.operator = "==";
				return;
			}
			if (operator.equals("<>")) {
				this.operator = "!=";
				return;
			}
			this.operator = operator;
		  
	  }
	  
	  public String getOperator(){
		  return this.operator;
	  }
	  
	  @Override
	public String toString(){
		  return this.operator;
	  }
}
/* JavaCC - OriginalChecksum=b585fc3dbb00e7c70eb4dcb896ca35b3 (do not edit this line) */
