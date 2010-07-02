package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAS;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAdvance;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAggregateFunction;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAllPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAndPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAnyPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttrDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinitions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeType;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBasicPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerAsSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerQueue;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSelectInto;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCSVSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTChannel;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCompareOperator;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTComplexSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCovarianceRow;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateBroker;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateSensor;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateViewStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDBExecuteStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDBSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabase;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabaseOptions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDateFormat;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDefaultPriority;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDistinctExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTElementPriorities;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTElementPriority;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTExists;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTFromClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTFunctionExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTFunctionName;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTGroupByClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHavingClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHost;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInteger;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTListDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTMatrixExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTMetric;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTNotPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTNumber;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTORSchemaDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTOSGI;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTOldWindow;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTOrPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPartition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPriority;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPriorizedStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTProbabilityPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTProjectionMatrix;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTProjectionVector;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTQuantificationOperator;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTQuantificationPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRecordDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRecordEntryDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRenamedExpression;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSQL;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectAll;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSetOperator;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSilab;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimplePredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleToken;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleTuple;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSlide;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSocket;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSpatialCompareOperator;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSpatialPredicate;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTStreamSQLWindow;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTString;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSubselect;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTTimeInterval;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTTimedTuple;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTTimedTuples;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTTuple;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTTupleSet;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTWhereClause;
import de.uniol.inf.is.odysseus.parser.cql.parser.NewSQLParserVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;
import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFCompareOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate.SDFCompareOperatorFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.IAttributeResolver;
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

	public Object visit(SimpleNode node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTSelectClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTFromClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTWhereClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTGroupByClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTHavingClause node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTSimplePredicate node, Object data) {
		return node.childrenAccept(this, data);

	}

	public Object visit(ASTExpression node, Object data) {
		return node.childrenAccept(this, data);

	}

	public Object visit(ASTSimpleToken node, Object data) {
		return node.childrenAccept(this, data);

	}

	public Object visit(ASTAggregateExpression node, Object data) {
		return node.childrenAccept(this, data);

	}

	public Object visit(ASTAggregateFunction node, Object data) {
		return node.childrenAccept(this, data);

	}

	public Object visit(ASTDistinctExpression node, Object data) {
		return node.childrenAccept(this, data);

	}

	public Object visit(ASTAS node, Object data) {
		return node.childrenAccept(this, data);

	}

	public Object visit(ASTIdentifier node, Object data) {
		return node.childrenAccept(this, data);

	}

	public Object visit(ASTInteger node, Object data) {
		return node.childrenAccept(this, data);

	}

	public Object visit(ASTNumber node, Object data) {
		return node.childrenAccept(this, data);

	}

	public Object visit(ASTCompareOperator node, Object data) {
		return node.childrenAccept(this, data);

	}

	public Object visit(ASTSetOperator node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTStatement node, Object data) {
		return node.childrenAccept(this, data);
	}

	protected String getName(ASTAggregateExpression curChild,
			IAttributeResolver attributeResolver) {
		return (curChild.jjtGetChild(0)).toString()
				+ "("
				+ attributeResolver.getAttribute(curChild.jjtGetChild(1)
						.toString()) + ")";
	}

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

	public Object visit(ASTFunctionExpression node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTString node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTAdvance node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTSlide node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTAnyPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTAllPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTInPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTExists node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTTuple node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTTupleSet node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTQuantificationOperator node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTPartition node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTOldWindow node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTStreamSQLWindow node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTOrPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTAndPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTNotPredicate node, Object data) {
		return node.childrenAccept(this, data);
	}

	public Object visit(ASTCreateStatement node, Object data) {
		return null;
	}

	public Object visit(ASTAttributeDefinitions node, Object data) {
		return null;
	}

	public Object visit(ASTAttributeDefinition node, Object data) {
		return null;
	}

	public Object visit(ASTTimedTuples node, Object data) {
		return null;
	}

	public Object visit(ASTTimedTuple node, Object data) {
		return null;
	}

	public Object visit(ASTTimeInterval node, Object data) {
		return null;
	}

	public Object visit(ASTSimpleTuple node, Object data) {
		return null;
	}

	public Object visit(ASTAttributeType node, Object data) {
		return null;
	}

	public Object visit(ASTPriority node, Object data) {
		return null;
	}

	public Object visit(ASTPriorizedStatement node, Object data) {

		return null;
	}

	public Object visit(ASTComplexSelectStatement node, Object data) {

		return null;
	}

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
}
