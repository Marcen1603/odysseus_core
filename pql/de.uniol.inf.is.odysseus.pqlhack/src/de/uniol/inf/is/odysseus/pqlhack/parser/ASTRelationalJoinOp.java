/* Generated By:JJTree: Do not edit this line. ASTRelationalJoinOp.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.pqlhack.parser;

public class ASTRelationalJoinOp extends SimpleNode {
  public ASTRelationalJoinOp(int id) {
    super(id);
  }

  public ASTRelationalJoinOp(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=f6d16bb2bf39b5d56054319a2fdbada3 (do not edit this line) */
