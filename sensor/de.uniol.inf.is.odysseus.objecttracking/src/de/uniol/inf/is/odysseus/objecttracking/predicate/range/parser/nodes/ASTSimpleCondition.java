/* Generated By:JJTree: Do not edit this line. ASTSimpleCondition.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes;

import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.*;

public
@SuppressWarnings("all")
class ASTSimpleCondition extends SimpleNode {
  public ASTSimpleCondition(int id) {
    super(id);
  }

  public ASTSimpleCondition(MapleResultParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MapleResultParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
  
  public String getCondition() {
	  return jjtGetChild(0).toString() + jjtGetChild(1).toString()
		+ jjtGetChild(2).toString();
  }
  
  @Override
public String toString() {
	  return getCondition();
  }
}
/* JavaCC - OriginalChecksum=7f72f856a90786d55bd9f0643b35401d (do not edit this line) */
