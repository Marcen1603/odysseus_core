/* Generated By:JJTree: Do not edit this line. ASTCreateSensor.java Version 4.1 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY= */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public class ASTCreateSensor extends SimpleNode {
  public ASTCreateSensor(int id) {
    super(id);
  }

  public ASTCreateSensor(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=eee733352194247ed0795dce6c7b188b (do not edit this line) */
