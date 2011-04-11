package de.uniol.inf.is.odysseus.sparql.parser.visitor;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTAdditiveExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTAggregation;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTArgList;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTAskQuery;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTBaseDecl;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTBlankNode;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTBlankNodePropertyList;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTBooleanLiteral;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTBrackettedExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTBuiltInCall;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTCSVSource;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTChannel;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTCollection;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTCompilationUnit;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTConditionalAndExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTConditionalOrExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTConstraint;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTConstructQuery;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTConstructTemplate;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTConstructTriples;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTCreateStatement;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTDatastreamClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTDescribeQuery;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTFilter;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTFromClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTFunctionCall;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGraphGraphPattern;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGraphNode;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGraphPatternNotTriples;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGraphTerm;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGroupBy;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGroupGraphPattern;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTGroupOrUnionGraphPattern;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTHost;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTIRI_REF;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTIRIref;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTIRIrefOrFunction;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTIdentifier;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTLimitClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTLimitOffsetClauses;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTMultiplicativeExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTNumericExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTNumericLiteral;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTNumericLiteralNegative;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTNumericLiteralPositive;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTNumericLiteralUnsigned;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTObject;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTObjectList;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTOffsetClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTOptionalGraphPattern;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTOrderClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTOrderCondition;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTPrefixDecl;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTPrefixedName;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTPrimaryExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTPrologue;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTPropertyList;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTPropertyListNotEmpty;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTQuery;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTRDFLiteral;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTRegexExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTRelationalExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTSelectQuery;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTSlidingTimeWindow;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTSlidingTupelWindow;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTSocket;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTSolutionModifier;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTSourceSelector;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTStreamClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTString;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTTimeunit;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTTriplesBlock;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTTriplesNode;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTTriplesSameSubject;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTUnaryExpression;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTValueLogical;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTValueSpecification;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTVar;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTVarOrIRIref;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTVarOrTerm;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTVerb;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTWhereClause;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTWindow;
import de.uniol.inf.is.odysseus.sparql.parser.ast.ASTWindowNotPWindow;
import de.uniol.inf.is.odysseus.sparql.parser.ast.SPARQLParserVisitor;
import de.uniol.inf.is.odysseus.sparql.parser.ast.SimpleNode;
import de.uniol.inf.is.odysseus.usermanagement.User;


/**
 * This visitor creates a logical SPARQL plan from a SPARQL query.
 * 
 * Andre Bolles <andre.bolles@uni-oldenburg.de>
 *
 */
public class SPARQLCreateLogicalPlanVisitor implements SPARQLParserVisitor{

	private User user;
	private IDataDictionary dd;
	
	
	public SPARQLCreateLogicalPlanVisitor(){
	}
	
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public void setDataDictionary(IDataDictionary dd){
		this.dd = dd;
	}
	
	// ========================= VISIT METHODS =====================================
	
	@Override
	public Object visit(SimpleNode node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTCompilationUnit node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTQuery node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPrologue node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBaseDecl node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPrefixDecl node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSelectQuery node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTConstructQuery node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTDescribeQuery node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAskQuery node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTFromClause node, Object data) {
		return node.childrenAccept(this, data);
	}


	@Override
	public Object visit(ASTDatastreamClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAggregation node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTWindow node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSlidingTimeWindow node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSlidingTupelWindow node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTWindowNotPWindow node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTValueSpecification node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTTimeunit node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSourceSelector node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTWhereClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGroupBy node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSolutionModifier node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTLimitOffsetClauses node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTOrderClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTOrderCondition node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTLimitClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTOffsetClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGroupGraphPattern node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTTriplesBlock node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGraphPatternNotTriples node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTOptionalGraphPattern node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGraphGraphPattern node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGroupOrUnionGraphPattern node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTFilter node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTConstraint node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTFunctionCall node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTArgList node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTConstructTemplate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTConstructTriples node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTTriplesSameSubject node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPropertyListNotEmpty node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPropertyList node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTObjectList node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTObject node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTVerb node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTTriplesNode node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBlankNodePropertyList node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTCollection node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGraphNode node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTVarOrTerm node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTVarOrIRIref node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTVar node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGraphTerm node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTConditionalOrExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTConditionalAndExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTValueLogical node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTRelationalExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNumericExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAdditiveExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTMultiplicativeExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTUnaryExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPrimaryExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBrackettedExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBuiltInCall node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTRegexExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTIRIrefOrFunction node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTRDFLiteral node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNumericLiteral node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNumericLiteralUnsigned node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNumericLiteralPositive node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNumericLiteralNegative node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBooleanLiteral node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTString node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTIRIref node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPrefixedName node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTBlankNode node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTIRI_REF node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTStreamClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTCreateStatement node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSocket node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTChannel node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTHost node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTCSVSource node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {
		return node.childrenAccept(this, data);
	}

}
