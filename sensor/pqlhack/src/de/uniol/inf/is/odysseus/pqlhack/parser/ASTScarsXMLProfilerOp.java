/* Generated By:JJTree: Do not edit this line. ASTScarsXMLProfilerOp.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.pqlhack.parser;

public
class ASTScarsXMLProfilerOp extends SimpleNode {
  public ASTScarsXMLProfilerOp(int id) {
    super(id);
  }

  public ASTScarsXMLProfilerOp(ProceduralExpressionParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(ProceduralExpressionParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=c0fbbd8a858acfddc7b3c048ed416472 (do not edit this line) */
