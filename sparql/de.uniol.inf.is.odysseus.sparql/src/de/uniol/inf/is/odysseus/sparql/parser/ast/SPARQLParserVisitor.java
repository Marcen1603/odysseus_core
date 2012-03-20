/* Generated By:JavaCC: Do not edit this line. SPARQLParserVisitor.java Version 5.0 */
package de.uniol.inf.is.odysseus.sparql.parser.ast;

@SuppressWarnings("all")
public interface SPARQLParserVisitor
{
  public Object visit(SimpleNode node, Object data);
  public Object visit(ASTCompilationUnit node, Object data);
  public Object visit(ASTQuery node, Object data);
  public Object visit(ASTCreateStatement node, Object data);
  public Object visit(ASTSocket node, Object data);
  public Object visit(ASTChannel node, Object data);
  public Object visit(ASTHost node, Object data);
  public Object visit(ASTCSVSource node, Object data);
  public Object visit(ASTIdentifier node, Object data);
  public Object visit(ASTPrologue node, Object data);
  public Object visit(ASTBaseDecl node, Object data);
  public Object visit(ASTPrefixDecl node, Object data);
  public Object visit(ASTSelectQuery node, Object data);
  public Object visit(ASTConstructQuery node, Object data);
  public Object visit(ASTDescribeQuery node, Object data);
  public Object visit(ASTAskQuery node, Object data);
  public Object visit(ASTFromClause node, Object data);
  public Object visit(ASTDatastreamClause node, Object data);
  public Object visit(ASTStreamClause node, Object data);
  public Object visit(ASTAggregation node, Object data);
  public Object visit(ASTWindow node, Object data);
  public Object visit(ASTSlidingTimeWindow node, Object data);
  public Object visit(ASTSlidingTupelWindow node, Object data);
  public Object visit(ASTWindowNotPWindow node, Object data);
  public Object visit(ASTValueSpecification node, Object data);
  public Object visit(ASTTimeunit node, Object data);
  public Object visit(ASTSourceSelector node, Object data);
  public Object visit(ASTWhereClause node, Object data);
  public Object visit(ASTGroupBy node, Object data);
  public Object visit(ASTSolutionModifier node, Object data);
  public Object visit(ASTLimitOffsetClauses node, Object data);
  public Object visit(ASTOrderClause node, Object data);
  public Object visit(ASTOrderCondition node, Object data);
  public Object visit(ASTLimitClause node, Object data);
  public Object visit(ASTOffsetClause node, Object data);
  public Object visit(ASTGroupGraphPattern node, Object data);
  public Object visit(ASTTriplesBlock node, Object data);
  public Object visit(ASTGraphPatternNotTriples node, Object data);
  public Object visit(ASTOptionalGraphPattern node, Object data);
  public Object visit(ASTGraphGraphPattern node, Object data);
  public Object visit(ASTGroupOrUnionGraphPattern node, Object data);
  public Object visit(ASTFilter node, Object data);
  public Object visit(ASTConstraint node, Object data);
  public Object visit(ASTFunctionCall node, Object data);
  public Object visit(ASTArgList node, Object data);
  public Object visit(ASTConstructTemplate node, Object data);
  public Object visit(ASTConstructTriples node, Object data);
  public Object visit(ASTTriplesSameSubject node, Object data);
  public Object visit(ASTPropertyListNotEmpty node, Object data);
  public Object visit(ASTPropertyList node, Object data);
  public Object visit(ASTObjectList node, Object data);
  public Object visit(ASTObject node, Object data);
  public Object visit(ASTVerb node, Object data);
  public Object visit(ASTTriplesNode node, Object data);
  public Object visit(ASTBlankNodePropertyList node, Object data);
  public Object visit(ASTCollection node, Object data);
  public Object visit(ASTGraphNode node, Object data);
  public Object visit(ASTVarOrTerm node, Object data);
  public Object visit(ASTVarOrIRIref node, Object data);
  public Object visit(ASTVar node, Object data);
  public Object visit(ASTGraphTerm node, Object data);
  public Object visit(ASTExpression node, Object data);
  public Object visit(ASTConditionalOrExpression node, Object data);
  public Object visit(ASTConditionalAndExpression node, Object data);
  public Object visit(ASTValueLogical node, Object data);
  public Object visit(ASTRelationalExpression node, Object data);
  public Object visit(ASTNumericExpression node, Object data);
  public Object visit(ASTAdditiveExpression node, Object data);
  public Object visit(ASTMultiplicativeExpression node, Object data);
  public Object visit(ASTUnaryExpression node, Object data);
  public Object visit(ASTPrimaryExpression node, Object data);
  public Object visit(ASTBrackettedExpression node, Object data);
  public Object visit(ASTBuiltInCall node, Object data);
  public Object visit(ASTRegexExpression node, Object data);
  public Object visit(ASTIRIrefOrFunction node, Object data);
  public Object visit(ASTRDFLiteral node, Object data);
  public Object visit(ASTNumericLiteral node, Object data);
  public Object visit(ASTNumericLiteralUnsigned node, Object data);
  public Object visit(ASTNumericLiteralPositive node, Object data);
  public Object visit(ASTNumericLiteralNegative node, Object data);
  public Object visit(ASTBooleanLiteral node, Object data);
  public Object visit(ASTString node, Object data);
  public Object visit(ASTIRIref node, Object data);
  public Object visit(ASTPrefixedName node, Object data);
  public Object visit(ASTBlankNode node, Object data);
  public Object visit(ASTIRI_REF node, Object data);
}
/* JavaCC - OriginalChecksum=fa3c5e1b4a60d3f2df36e55d6272b3ad (do not edit this line) */
