/* Generated By:JJTree: Do not edit this line. ASTCreateContextStore.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public
class ASTCreateContextStore extends SimpleNode {
  public ASTCreateContextStore(int id) {
    super(id);
  }

  public ASTCreateContextStore(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws de.uniol.inf.is.odysseus.planmanagement.QueryParseException {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=5a531011e71b9d194b2883c26042e3da (do not edit this line) */
