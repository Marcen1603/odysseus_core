/* Generated By:JJTree: Do not edit this line. ASTRemoveUserFromTenantStatement.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;

public
@SuppressWarnings("all")
class ASTRemoveUserFromTenantStatement extends SimpleNode {
  public ASTRemoveUserFromTenantStatement(int id) {
    super(id);
  }

  public ASTRemoveUserFromTenantStatement(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. 
 * @throws QueryParseException **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws QueryParseException {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=d7af7dc56906598f2b0e2762ff5aa772 (do not edit this line) */
