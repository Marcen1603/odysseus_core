/* Generated By:JJTree: Do not edit this line. ASTProjectionVector.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTProjectionVector extends SimpleNode {
  public ASTProjectionVector(int id) {
    super(id);
  }

  public ASTProjectionVector(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  @Override
public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=2ee67c6c150461dccb858f627efce511 (do not edit this line) */
