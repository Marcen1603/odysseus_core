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
  public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
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
