/* Generated By:JJTree: Do not edit this line. ASTBrokerAsSource.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;

public
@SuppressWarnings("all")
class ASTBrokerAsSource extends SimpleNode {
  public ASTBrokerAsSource(int id) {
    super(id);
  }

  public ASTBrokerAsSource(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. 
 * @throws QueryParseException **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws QueryParseException {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=b246e417b981e0894d742bbd6d8b5ea4 (do not edit this line) */
