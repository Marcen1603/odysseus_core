/* Generated By:JJTree: Do not edit this line. ASTNumber.java Version 4.3 */
/* JavaCCOptions:MULTI=true,NODE_USES_PARSER=false,VISITOR=true,TRACK_TOKENS=false,NODE_PREFIX=AST,NODE_EXTENDS=,NODE_FACTORY=,SUPPORT_CLASS_VISIBILITY_PUBLIC=true */
package de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.nodes;

import de.uniol.inf.is.odysseus.objecttracking.predicate.range.parser.*;

public
@SuppressWarnings("all")
class ASTNumber extends SimpleNode {
  
	public String value;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public ASTNumber(int id) {
    super(id);
  }

  public ASTNumber(MapleResultParser p, int id) {
    super(p, id);
  }


  /** Accept the visitor. **/
  public Object jjtAccept(MapleResultParserVisitor visitor, Object data) {
    return visitor.visit(this, data);
  }
}
/* JavaCC - OriginalChecksum=3b761e663145ce14ae6ed10bb8d29022 (do not edit this line) */
