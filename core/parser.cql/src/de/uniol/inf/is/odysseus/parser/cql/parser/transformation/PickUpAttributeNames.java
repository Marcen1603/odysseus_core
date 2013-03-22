package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRenamedExpression;

public class PickUpAttributeNames extends AbstractDefaultVisitor{
		
	private Map<String, String> aliases = new HashMap<>();
	
	@Override
	public Map<String, String> visit(ASTRenamedExpression node, Object data) throws QueryParseException {			
		if(node.hasAlias()){
			
			aliases.put(node.getAlias(), node.jjtGetChild(0).toString());			
		}
		return aliases;
	}
	
	public Map<String, String> getAliasNames(){
		return this.aliases;
	}

}
