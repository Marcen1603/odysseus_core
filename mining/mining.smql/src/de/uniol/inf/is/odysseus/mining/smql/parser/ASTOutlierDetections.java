/* Generated By:JJTree: Do not edit this line. ASTOutlierDetections.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.mining.smql.parser;

public
class ASTOutlierDetections extends SimpleNode {
  public ASTOutlierDetections(int id) {
    super(id);
  }

  public ASTOutlierDetections(SMQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SMQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=dd1541617fe1e75b659a87c114c49285 (do not edit this line) */
