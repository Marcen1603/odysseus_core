/* Generated By:JJTree: Do not edit this line. ASTCreateType.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;

public
@SuppressWarnings("all")
class ASTCreateType extends SimpleNode {
  public ASTCreateType(int id) {
    super(id);
  }

  public ASTCreateType(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. 
 * @throws QueryParseException **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws QueryParseException {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=b936123cb2f63b73f27aca0056a27a1a (do not edit this line) */
