/* Generated By:JJTree: Do not edit this line. ASTNamedGraphClause.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

public
class ASTNamedGraphClause extends SimpleNode {
  public ASTNamedGraphClause(int id) {
    super(id);
  }

  public ASTNamedGraphClause(SPARQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=abbf1c72e642950862cd436fdb6d4582 (do not edit this line) */
