/* Generated By:JJTree: Do not edit this line. ASTRelationalNestOp.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.pqlhack.parser;

public
class ASTRelationalNestOp extends SimpleNode {
  public ASTRelationalNestOp(int id) {
    super(id);
  }

  public ASTRelationalNestOp(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  public String getAlias() {
	  return ((ASTIdentifier) this.jjtGetChild(2)).getName();
  }
}
/* JavaCC - OriginalChecksum=bc7e7f7fb2866cb2b6f50d69540145c5 (do not edit this line) */
