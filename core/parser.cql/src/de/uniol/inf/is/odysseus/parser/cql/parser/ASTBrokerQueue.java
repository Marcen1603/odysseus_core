/* Generated By:JJTree: Do not edit this line. ASTBrokerQueue.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public
@SuppressWarnings("all")
class ASTBrokerQueue extends SimpleNode {
  public ASTBrokerQueue(int id) {
    super(id);
  }

  public ASTBrokerQueue(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws QueryParseException {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=316b286935c38d2d40927a562f4dab9b (do not edit this line) */
