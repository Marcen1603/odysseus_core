/* Generated By:JJTree: Do not edit this line. ASTPriorizedStatement.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTPriorizedStatement extends SimpleNode {
  public ASTPriorizedStatement(int id) {
    super(id);
  }

  public ASTPriorizedStatement(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=6eeca9f4cec1c83dba9b6a15dd95a1d7 (do not edit this line) */
