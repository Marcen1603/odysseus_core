/* Generated By:JJTree: Do not edit this line. ASTListDefinition.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.parser.cql.parser;

import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;

public
@SuppressWarnings("all")
class ASTListDefinition extends SimpleNode {
  public ASTListDefinition(int id) {
    super(id);
  }

  public ASTListDefinition(NewSQLParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. 
 * @throws QueryParseException **/
  public Object jjtAccept(NewSQLParserVisitor visitor, Object data) throws QueryParseException {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=6dc962830235aa5aad67c9fd9fa1b282 (do not edit this line) */
