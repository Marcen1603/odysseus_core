/* Generated By:JJTree: Do not edit this line. ASTStartEndPWindow.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

public
class ASTStartEndPWindow extends SimpleNode {
  public ASTStartEndPWindow(int id) {
    super(id);
  }

  public ASTStartEndPWindow(SPARQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=790471c95f74a09286338b2b1989ca7b (do not edit this line) */
