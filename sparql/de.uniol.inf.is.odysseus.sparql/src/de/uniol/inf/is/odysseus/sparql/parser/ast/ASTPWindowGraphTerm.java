/* Generated By:JJTree: Do not edit this line. ASTPWindowGraphTerm.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

public
class ASTPWindowGraphTerm extends SimpleNode {
  public ASTPWindowGraphTerm(int id) {
    super(id);
  }

  public ASTPWindowGraphTerm(SPARQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=40667f755050892b94f09b078cb6fe47 (do not edit this line) */
