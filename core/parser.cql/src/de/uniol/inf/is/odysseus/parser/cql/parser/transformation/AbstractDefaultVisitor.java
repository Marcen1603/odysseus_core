package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.parser.cql.parser.*;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFCompareOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFCompareOperatorFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.AmgigiousAttributeException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.NoSuchAttributeException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFPredicates;

public class AbstractDefaultVisitor implements NewSQLParserVisitor {

	public static SDFCompareOperator toCompareOperator(String string) {
		if (string.equals("=")) {
			return SDFCompareOperatorFactory
					.getCompareOperator(SDFPredicates.Equal);
		}
		if (string.equals("<>")) {
			return SDFCompareOperatorFactory
					.getCompareOperator(SDFPredicates.Unequal);
		}
		if (string.equals(">=")) {
			return SDFCompareOperatorFactory
					.getCompareOperator(SDFPredicates.GreaterOrEqualThan);
		}
		if (string.equals("<=")) {
			return SDFCompareOperatorFactory
					.getCompareOperator(SDFPredicates.LowerOrEqualThan);
		}
		if (string.equals(">")) {
			return SDFCompareOperatorFactory
					.getCompareOperator(SDFPredicates.GreaterThan);
		}
		if (string.equals("<")) {
			return SDFCompareOperatorFactory
					.getCompareOperator(SDFPredicates.LowerThan);
		}
		return null;
	}

	public static SDFCompareOperator toInverseCompareOperator(String string) {
		if (string.equals("<>")) {
			return SDFCompareOperatorFactory
					.getCompareOperator(SDFPredicates.Equal);
		}
		if (string.equals("=")) {
			return SDFCompareOperatorFactory
					.getCompareOperator(SDFPredicates.Unequal);
		}
		if (string.equals("<")) {
			return SDFCompareOperatorFactory
					.getCompareOperator(SDFPredicates.GreaterOrEqualThan);
		}
		if (string.equals(">")) {
			return SDFCompareOperatorFactory
					.getCompareOperator(SDFPredicates.LowerOrEqualThan);
		}
		if (string.equals("<=")) {
			return SDFCompareOperatorFactory
					.getCompareOperator(SDFPredicates.GreaterThan);
		}
		if (string.equals(">=")) {
			return SDFCompareOperatorFactory
					.getCompareOperator(SDFPredicates.LowerThan);
		}
		return null;
	}

	@Override
	public Object visit(SimpleNode node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSelectClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTFromClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTWhereClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTGroupByClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTHavingClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSimplePredicate node, Object data) {
		return node.childrenAccept(this, data);

	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		return node.childrenAccept(this, data);

	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) {
		return node.childrenAccept(this, data);

	}

	@Override
	public Object visit(ASTAggregateExpression node, Object data) {
		return node.childrenAccept(this, data);

	}

	@Override
	public Object visit(ASTAggregateFunction node, Object data) {
		return node.childrenAccept(this, data);

	}

	@Override
	public Object visit(ASTDistinctExpression node, Object data) {
		return node.childrenAccept(this, data);

	}

	@Override
	public Object visit(ASTAS node, Object data) {
		return node.childrenAccept(this, data);

	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {
		return node.childrenAccept(this, data);

	}

	@Override
	public Object visit(ASTInteger node, Object data) {
		return node.childrenAccept(this, data);

	}

	@Override
	public Object visit(ASTNumber node, Object data) {
		return node.childrenAccept(this, data);

	}

	@Override
	public Object visit(ASTCompareOperator node, Object data) {
		return node.childrenAccept(this, data);

	}

	@Override
	public Object visit(ASTSetOperator node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTStatement node, Object data) {
		return node.childrenAccept(this, data);
	}

	protected String getName(ASTAggregateExpression curChild,
			IAttributeResolver attributeResolver) {

		try {
			return (curChild.jjtGetChild(0)).toString()
					+ "("
					+ attributeResolver.getAttribute(curChild.jjtGetChild(1)
							.toString()) + ")";
		} catch (AmgigiousAttributeException e) {			
			e.printStackTrace();
		} catch (NoSuchAttributeException e) {
			e.printStackTrace();
		}
		return "";

	}

	@Override
	public Object visit(ASTRenamedExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	protected Node getNode(ASTExpression expression) {
		if (expression.jjtGetNumChildren() != 1
				|| (expression.jjtGetChild(0) instanceof ASTFunctionExpression)) {
			return null;
		}
		Node childNode = expression.jjtGetChild(0).jjtGetChild(0);
		if (childNode instanceof ASTExpression) {
			return getNode((ASTExpression) childNode);
		}
		return childNode;
	}

	@Override
	public Object visit(ASTFunctionExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTString node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAdvance node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTSlide node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAnyPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAllPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTInPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTExists node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTTuple node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTTupleSet node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTQuantificationOperator node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTPartition node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTStreamSQLWindow node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTOrPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAndPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTNotPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTCreateStatement node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTAttributeDefinitions node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTAttributeDefinition node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTTimedTuples node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTTimedTuple node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTTimeInterval node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTSimpleTuple node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTAttributeType node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTPriority node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTPriorizedStatement node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTComplexSelectStatement node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTSelectStatement node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTElementPriorities node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTElementPriority node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTDefaultPriority node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTSocket node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTHost node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTChannel node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTSpatialCompareOperator node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTCovarianceRow node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTMatrixExpression node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTProbabilityPredicate node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTDateFormat node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTSpatialPredicate node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTSimpleSource node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTSubselect node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTBasicPredicate node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTQuantificationPredicate node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTSelectAll node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTProjectionMatrix node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTProjectionVector node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTFunctionName node, Object data) {

		return null;
	}

	@Override
	public Object visit(ASTOSGI node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTCSVSource node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTSilab node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTDBExecuteStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTDatabase node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTSQL node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTDBSelectStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTDatabaseOptions node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCreateBroker node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTBrokerSource node, Object data) {	
		return null;
	}

	@Override
	public Object visit(ASTBrokerSelectInto node, Object data) {		
		return null;
	}

	@Override
	public Object visit(ASTBrokerAsSource node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTBrokerSimpleSource node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTBrokerQueue node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTMetric node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTCreateSensor node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTORSchemaDefinition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTRecordDefinition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTRecordEntryDefinition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTListDefinition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTAttrDefinition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCreateViewStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCreateUserStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTAlterUserStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTDropStreamStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTDropViewStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCreateTenantStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTAddUserToTenantStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTRemoveUserFromTenantStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCreateSLAStatement node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTPercentileConstraint node, Object data) {
		return null;
	}	

	@Override
	public Object visit(ASTCreateFromDatabase node, Object data) {	
		return null;
	}

	@Override
	public Object visit(ASTJdbcIdentifier node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTDatabaseTimeSensitiv node, Object data) {	
		return null;
	}

	@Override
	public Object visit(ASTInsertIntoStatement node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTSaveMetaData node, Object data) {		
		return null;
	}

	@Override
	public Object visit(ASTDatabaseTableOptions node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTDatabaseCreateOption node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTDatabaseTruncateOption node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTIfNotExists node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTDropUserStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTCreateRoleStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTDropRoleStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTGrantStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Object visit(ASTRevokeStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTIdentifierList node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTGrantRoleStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTRevokeRoleStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}
}
