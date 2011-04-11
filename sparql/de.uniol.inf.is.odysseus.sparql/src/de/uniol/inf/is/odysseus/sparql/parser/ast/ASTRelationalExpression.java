/* Generated By:JJTree: Do not edit this line. ASTRelationalExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

public
class ASTRelationalExpression extends SimpleNode {
  
	private String compareOperator;
	
	public ASTRelationalExpression(int id) {
    super(id);
  }

  public ASTRelationalExpression(SPARQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  public String toString(){
	  if(this.compareOperator == null){
		  return this.jjtGetChild(0).toString();
	  }
	  else{
		  return this.jjtGetChild(0) + " " + this.compareOperator + " " + this.jjtGetChild(1);
	  }
  }
  
  public void setCompareOperator(String compareOperator){
	  this.compareOperator = compareOperator;
  }
}
/* JavaCC - OriginalChecksum=596e5b2eb6388862122f783c1325f503 (do not edit this line) */
