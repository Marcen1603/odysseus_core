/* Generated By:JJTree: Do not edit this line. ASTExpression.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

public
class ASTExpression extends SimpleNode {
  public ASTExpression(int id) {
    super(id);
  }

  public ASTExpression(SPARQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  public String toString(){
	  return this.jjtGetChild(0).toString();
  }
}
/* JavaCC - OriginalChecksum=9b16c50765fa13e6a2f655628d338029 (do not edit this line) */
