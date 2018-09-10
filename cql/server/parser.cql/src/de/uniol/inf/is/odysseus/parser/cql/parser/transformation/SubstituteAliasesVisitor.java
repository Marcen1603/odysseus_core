package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBasicPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTFromClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHavingClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectClause;

public class SubstituteAliasesVisitor extends AbstractDefaultVisitor {
	
	private Map<String, String> substitutes;
	
	public SubstituteAliasesVisitor(Map<String, String> substitutes){
		this.substitutes = substitutes;		
	}
	
	@Override
	public Object visit(ASTPredicate node, Object data) throws QueryParseException {		
		node.childrenAccept(this, data);		
		return null;
	}
	
	@Override
	public Object visit(ASTBasicPredicate node, Object data) throws QueryParseException {
		return node.childrenAccept(this, data);
	}
	
	@Override
	public Object visit(ASTSelectClause node, Object data) throws QueryParseException {
		// this is needed to omit parts in select part (e.g. projection identifiers)
		return null;
	}
	
	@Override
	public Object visit(ASTFromClause node, Object data) throws QueryParseException {
		// this is needed to omit parts in from part (e.g. source identifiers)
		return null;
	}
	
	@Override
	public Object visit(ASTHavingClause node, Object data) throws QueryParseException {
		// we omit having parts, because we don't want to substitute aggregations!		
		return null;
	}
	
	
	
	@Override
	public Object visit(ASTIdentifier node, Object data) throws QueryParseException {
		String identifier = node.getName();
		if(substitutes.containsKey(identifier)){
			String newIdent = substitutes.get(identifier); 
			node.setName(newIdent);
		}
		return null;
	}
	
	
	

}
