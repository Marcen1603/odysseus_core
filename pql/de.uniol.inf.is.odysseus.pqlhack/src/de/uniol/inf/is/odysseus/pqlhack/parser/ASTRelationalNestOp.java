/* Generated By:JJTree: Do not edit this line. ASTRelationalNestOp.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.pqlhack.parser;

public class ASTRelationalNestOp extends SimpleNode {
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
/* JavaCC - OriginalChecksum=ca294436842ecd41e0cf5e5659e73296 (do not edit this line) */
