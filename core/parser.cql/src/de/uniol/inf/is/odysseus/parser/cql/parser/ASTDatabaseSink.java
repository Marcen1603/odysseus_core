/* Generated By:JJTree: Do not edit this line. ASTDatabaseSink.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public
class ASTDatabaseSink extends SimpleNode {
  public ASTDatabaseSink(int id) {
    super(id);
  }

  public ASTDatabaseSink(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=54750ef827e5df0f1b868f61fea3fe85 (do not edit this line) */
