/* Generated By:JJTree: Do not edit this line. ASTCleanPhase.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.mining.smql.parser;

public
class ASTCleanPhase extends SimpleNode {
  public ASTCleanPhase(int id) {
    super(id);
  }

  public ASTCleanPhase(SMQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SMQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=1db6ff2cd640662f18e4a8ce8a8fd0b4 (do not edit this line) */
