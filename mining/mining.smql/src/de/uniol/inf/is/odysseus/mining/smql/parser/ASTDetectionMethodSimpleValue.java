/* Generated By:JJTree: Do not edit this line. ASTDetectionMethodSimpleValue.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.mining.smql.parser;

public
@SuppressWarnings("all")
class ASTDetectionMethodSimpleValue extends SimpleNode {
  public ASTDetectionMethodSimpleValue(int id) {
    super(id);
  }

  public ASTDetectionMethodSimpleValue(SMQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SMQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=ade8435af1a8cd8bc37c2088cc2aed08 (do not edit this line) */
