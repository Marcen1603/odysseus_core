/* Generated By:JJTree: Do not edit this line. ASTSocketSink.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public
class ASTSocketSink extends SimpleNode {
  public ASTSocketSink(int id) {
    super(id);
  }

  public ASTSocketSink(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws de.uniol.inf.is.odysseus.planmanagement.QueryParseException {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=cb8258b27d002fc74c7c7fc6168e9b8f (do not edit this line) */
