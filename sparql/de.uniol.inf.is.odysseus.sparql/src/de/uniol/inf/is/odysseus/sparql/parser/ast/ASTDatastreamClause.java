/* Generated By:JJTree: Do not edit this line. ASTDatastreamClause.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

public
class ASTDatastreamClause extends SimpleNode {
  public ASTDatastreamClause(int id) {
    super(id);
  }

  public ASTDatastreamClause(SPARQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(SPARQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=860bb76f76b93f22933616d1d825cd70 (do not edit this line) */
