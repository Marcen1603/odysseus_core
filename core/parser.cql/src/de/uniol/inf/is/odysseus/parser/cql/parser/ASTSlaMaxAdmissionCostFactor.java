/* Generated By:JJTree: Do not edit this line. ASTSlaMaxAdmissionCostFactor.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

public
class ASTSlaMaxAdmissionCostFactor extends SimpleNode {
  public ASTSlaMaxAdmissionCostFactor(int id) {
    super(id);
  }

  public ASTSlaMaxAdmissionCostFactor(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=7bd038cfb0d485633a67fb00f37285c2 (do not edit this line) */
