/* Generated By:JJTree: Do not edit this line. ASTProcessPhases.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.mining.smql.parser;

public
@SuppressWarnings("all")
class ASTProcessPhases extends SimpleNode {
  public ASTProcessPhases(int id) {
    super(id);
  }

  public ASTProcessPhases(SMQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SMQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=79e31923c68f1cba2eae223e75cfe17a (do not edit this line) */
