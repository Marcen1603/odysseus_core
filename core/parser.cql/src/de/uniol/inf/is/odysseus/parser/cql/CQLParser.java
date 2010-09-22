package de.uniol.inf.is.odysseus.parser.cql;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.base.IQueryParser;
import de.uniol.inf.is.odysseus.base.QueryParseException;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.base.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.base.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.base.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.base.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.DifferenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.IntersectionAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnionAO;
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
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateUserStatement;
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
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSQLStatement;
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
import de.uniol.inf.is.odysseus.parser.cql.parser.NewSQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.NewSQLParserVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.SimpleNode;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AttributeResolver;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CheckAttributes;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CheckGroupBy;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CheckHaving;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateAccessAOVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateAggregationVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateJoinAOVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreatePriorityAOVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateProjectionVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateStreamVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateViewVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.IDatabaseAOVisitor;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class CQLParser implements NewSQLParserVisitor, IQueryParser {

	private List<IQuery> plans = new ArrayList<IQuery>();
	private static CQLParser instance = null;
	private static NewSQLParser parser;

	public static synchronized IQueryParser getInstance() {
		if (instance == null) {
			instance = new CQLParser();
		}
		return instance;
	}

	@Override
	public String getLanguage() {
		return "CQL";
	}

	public synchronized List<IQuery> parse(String query)
			throws QueryParseException {
		return parse(new StringReader(query));
	}

	public synchronized List<IQuery> parse(Reader reader)
			throws QueryParseException {
		try {
			if (parser == null) {
				parser = new NewSQLParser(reader);
			} else {
				NewSQLParser.ReInit(reader);
			}
			ASTStatement statement = NewSQLParser.Statement();
			CQLParser cqlParser = new CQLParser();
			cqlParser.visit(statement, null);
			return cqlParser.plans;
		} catch (NoClassDefFoundError e) {
			throw new QueryParseException(
					"parse error: missing plugin for language feature", e
							.getCause());
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
	}

	public Object visit(ASTStatement node, Object data) {
		return node.childrenAccept(this, null);
	}

	public Object visit(ASTPriorizedStatement node, Object data) {
		AbstractLogicalOperator op;
		Integer priority = 0;
		if (node.jjtGetChild(0) instanceof ASTDBExecuteStatement) {
			try {
				Class<?> dbClass = Class
						.forName("de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateDatabaseAOVisitor");
				IDatabaseAOVisitor dbVisitor = (IDatabaseAOVisitor) dbClass
						.newInstance();

				op = (AbstractLogicalOperator) node.jjtGetChild(0).jjtAccept(
						dbVisitor, data);
				AbstractLogicalOperator dsOp = (AbstractLogicalOperator) node
						.jjtGetChild(1).jjtAccept(this, data);
				// op.setInputAO(0, dsOp);
				op.subscribeToSource(dsOp, 0, 0, dsOp.getOutputSchema());
				// hat nun 3 statt 2 kinder
				if (node.jjtGetNumChildren() == 3) {
					priority = (Integer) node.jjtGetChild(2).jjtAccept(this,
							data);
				}
			} catch (Exception e) {
				throw new RuntimeException(
						"missing database plugin for cql parser");
			}

		} else {
			op = (AbstractLogicalOperator) node.jjtGetChild(0).jjtAccept(this,
					data);
			if (node.jjtGetNumChildren() == 2) {
				if (node.jjtGetChild(1) instanceof ASTPriority) {
					priority = (Integer) node.jjtGetChild(1).jjtAccept(this,
							data);
				} else {
					if (node.jjtGetChild(1) instanceof ASTMetric) {
						op = (AbstractLogicalOperator) node.jjtGetChild(1)
								.jjtAccept(this, op);
					}
				}
			}
		}

		Query query = new Query();
		query.setParserId(getLanguage());
		query.setPriority(priority);
		query.setLogicalPlan(op);

		plans.add(query);
		return plans;
	}

	public Object visit(ASTComplexSelectStatement node, Object data) {
		if (node.jjtGetNumChildren() == 1) {
			return node.jjtGetChild(0).jjtAccept(this, data);
		}
		AbstractLogicalOperator left = (AbstractLogicalOperator) node
				.jjtGetChild(0).jjtAccept(this, data);
		AbstractLogicalOperator right = (AbstractLogicalOperator) node
				.jjtGetChild(2).jjtAccept(this, data);
		ILogicalOperator setOperator = null;
		switch (((ASTSetOperator) node.jjtGetChild(1)).getOperation()) {
		case INTERSECT:
			setOperator = new IntersectionAO();
			break;
		case MINUS:
			setOperator = new DifferenceAO();
			break;
		case UNION:
			setOperator = new UnionAO();
			break;
		}
		if (left.getOutputSchema().size() != right.getOutputSchema().size()) {
			throw new IllegalArgumentException(
					"inputs of set operator have different schemas");
		}
		Iterator<SDFAttribute> iLeft = left.getOutputSchema().iterator();
		Iterator<SDFAttribute> iRight = right.getOutputSchema().iterator();
		while (iLeft.hasNext()) {
			SDFAttribute curLeft = iLeft.next();
			SDFAttribute curRight = iRight.next();
			// TODO problem: man kann keine zwei numerischen typen verknuepfen
			// dazu muesste etwas wie typkompatibilitaet definiert werden
			if (curLeft.getDatatype() != curRight.getDatatype()) {
				throw new IllegalArgumentException(
						"inputs of set operator have different schemas");
			}
		}
		setOperator.subscribeToSource(left, 0, 0, left.getOutputSchema());
		setOperator.subscribeToSource(right, 1, 1, right.getOutputSchema());
		return setOperator;
	}

	public Object visit(ASTSelectStatement statement, Object data) {
		try {
			CreateAccessAOVisitor access = new CreateAccessAOVisitor();
			access.visit(statement, null);
			AttributeResolver attributeResolver = access.getAttributeResolver();

			new CheckAttributes(attributeResolver).visit(statement, null);

			CheckGroupBy checkGroupBy = new CheckGroupBy();
			checkGroupBy.init(attributeResolver);
			checkGroupBy.visit(statement, null);
			if (!checkGroupBy.checkOkay()) {
				throw new RuntimeException(
						"missing attributes in GROUP BY clause");
			}

			CheckHaving checkHaving = new CheckHaving();
			checkHaving.init(attributeResolver);
			checkHaving.visit(statement, null);

			CreateJoinAOVisitor joinVisitor = new CreateJoinAOVisitor();
			joinVisitor.init(attributeResolver);
			ILogicalOperator top = (AbstractLogicalOperator) joinVisitor.visit(
					statement, null);

			CreateAggregationVisitor aggregationVisitor = new CreateAggregationVisitor();
			aggregationVisitor.init(top, attributeResolver);
			aggregationVisitor.visit(statement, null);
			top = aggregationVisitor.getResult();

			top = new CreateProjectionVisitor().createProjection(statement,
					top, attributeResolver);
			CreatePriorityAOVisitor prioVisitor = new CreatePriorityAOVisitor();
			prioVisitor.setTopOperator(top);
			prioVisitor.setAttributeResolver(attributeResolver);
			prioVisitor.visit(statement, null);
			top = prioVisitor.getTopOperator();
			return top;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	public Object visit(ASTCreateStatement node, Object data) {
		CreateStreamVisitor v = new CreateStreamVisitor();
		return v.visit(node, data);
	}
	
	@Override
	public Object visit(ASTCreateViewStatement node, Object data) {
		CreateViewVisitor v = new CreateViewVisitor();
		return v.visit(node, data);
	}


	public Object visit(ASTPriority node, Object data) {
		return node.getPriority();
	}

	@SuppressWarnings("unchecked")
	public static void initPredicates(ILogicalOperator curInputAO) {
		if (curInputAO.getPredicate() != null) {
			SDFAttributeList rightInputSchema = null;
			if (curInputAO.getSubscribedToSource().size() > 1) {
				rightInputSchema = curInputAO.getInputSchema(1);
			}
			initPredicate(curInputAO.getPredicate(), curInputAO
					.getInputSchema(0), rightInputSchema);
		}
		for (int i = 0; i < curInputAO.getSubscribedToSource().size(); ++i) {
			initPredicates(curInputAO.getSubscribedToSource(i).getTarget());
		}
	}

	@SuppressWarnings("unchecked")
	public static void initPredicate(IPredicate<RelationalTuple<?>> predicate,
			SDFAttributeList left, SDFAttributeList right) {
		if (predicate instanceof ComplexPredicate) {
			ComplexPredicate compPred = (ComplexPredicate) predicate;
			initPredicate(compPred.getLeft(), left, right);
			initPredicate(compPred.getRight(), left, right);
			return;
		}
		if (predicate instanceof NotPredicate) {
			initPredicate(((NotPredicate) predicate).getChild(), left, right);
			return;
		}
		if (predicate instanceof IRelationalPredicate) {
			((RelationalPredicate) predicate).init(left, right);
		}
		// NOTE: Das ProbabilityPredicate muss nicht mit linkem
		// und rechtem Schema initialisiert werden.
	}

	public Object visit(SimpleNode node, Object data) {
		return null;
	}

	public Object visit(ASTSQLStatement node, Object data) {
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

	public Object visit(ASTSetOperator node, Object data) {
		return null;
	}

	public Object visit(ASTSelectClause node, Object data) {
		return null;
	}

	public Object visit(ASTFromClause node, Object data) {
		return null;
	}

	public Object visit(ASTWhereClause node, Object data) {
		return null;
	}

	public Object visit(ASTGroupByClause node, Object data) {
		return null;
	}

	public Object visit(ASTHavingClause node, Object data) {
		return null;
	}

	public Object visit(ASTRenamedExpression node, Object data) {
		return null;
	}

	public Object visit(ASTAS node, Object data) {
		return null;
	}

	public Object visit(ASTPredicate node, Object data) {
		return null;
	}

	public Object visit(ASTSimplePredicate node, Object data) {
		return null;
	}

	public Object visit(ASTOrPredicate node, Object data) {
		return null;
	}

	public Object visit(ASTAndPredicate node, Object data) {
		return null;
	}

	public Object visit(ASTNotPredicate node, Object data) {
		return null;
	}

	public Object visit(ASTAnyPredicate node, Object data) {
		return null;
	}

	public Object visit(ASTAllPredicate node, Object data) {
		return null;
	}

	public Object visit(ASTInPredicate node, Object data) {
		return null;
	}

	public Object visit(ASTExists node, Object data) {
		return null;
	}

	public Object visit(ASTTuple node, Object data) {
		return null;
	}

	public Object visit(ASTTupleSet node, Object data) {
		return null;
	}

	public Object visit(ASTQuantificationOperator node, Object data) {
		return null;
	}

	public Object visit(ASTExpression node, Object data) {
		return null;
	}

	public Object visit(ASTSimpleToken node, Object data) {
		return null;
	}

	public Object visit(ASTFunctionExpression node, Object data) {
		return null;
	}

	public Object visit(ASTAggregateExpression node, Object data) {
		return null;
	}

	public Object visit(ASTAggregateFunction node, Object data) {
		return null;
	}

	public Object visit(ASTDistinctExpression node, Object data) {
		return null;
	}

	public Object visit(ASTStreamSQLWindow node, Object data) {
		return null;
	}

	public Object visit(ASTPartition node, Object data) {
		return null;
	}

	public Object visit(ASTAdvance node, Object data) {
		return null;
	}

	public Object visit(ASTSlide node, Object data) {
		return null;
	}

	public Object visit(ASTIdentifier node, Object data) {
		return null;
	}

	public Object visit(ASTInteger node, Object data) {
		return null;
	}

	public Object visit(ASTNumber node, Object data) {
		return null;
	}

	public Object visit(ASTString node, Object data) {
		return null;
	}

	public Object visit(ASTCompareOperator node, Object data) {
		return null;
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
		return null;
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
	public Object visit(ASTProjectionMatrix node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTProjectionVector node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTSelectAll node, Object data) {
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
		try {
			Class<?> brokerSourceVisitor = Class
					.forName("de.uniol.inf.is.odysseus.broker.parser.cql.BrokerVisitor");
			Object bsv = brokerSourceVisitor.newInstance();
			Method m = brokerSourceVisitor.getDeclaredMethod("visit",
					ASTCreateBroker.class, Object.class);
			AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m
					.invoke(bsv, node, data);

			addQuery(sourceOp);
			return plans;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"Brokerplugin is missing in CQL parser.", e.getCause());
		} catch (Exception e) {
			throw new RuntimeException(
					"Error while parsing CREATE BROKER statement", e.getCause());
		}
	}

	private void addQuery(AbstractLogicalOperator sourceOp) {
		Query query = new Query();
		query.setParserId(getLanguage());
		query.setLogicalPlan(sourceOp);
		plans.add(query);
	}

	@Override
	public Object visit(ASTBrokerSource node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTBrokerSelectInto node, Object data) {

		try {
			Class<?> brokerSourceVisitor = Class
					.forName("de.uniol.inf.is.odysseus.broker.parser.cql.BrokerVisitor");
			Object bsv = brokerSourceVisitor.newInstance();
			Method m = brokerSourceVisitor.getDeclaredMethod("visit",
					ASTBrokerSelectInto.class, Object.class);
			AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m
					.invoke(bsv, node, data);
			addQuery(sourceOp);
			return plans;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"Brokerplugin is missing in CQL parser.", e.getCause());
		} catch (Exception e) {
			throw new RuntimeException(
					"Error while parsing the SELECT INTO statement", e
							.getCause());
		}
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
		try {
			Class<?> brokerSourceVisitor = Class
					.forName("de.uniol.inf.is.odysseus.broker.parser.cql.BrokerVisitor");
			Object bsv = brokerSourceVisitor.newInstance();
			Method m = brokerSourceVisitor.getDeclaredMethod("visit",
					ASTMetric.class, Object.class);
			AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m
					.invoke(bsv, node, data);
			return sourceOp;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"Brokerplugin is missing in CQL parser.", e.getCause());
		} catch (Exception e) {
			throw new RuntimeException("Error while parsing the METRIC clause",
					e.getCause());
		}
	}

	@Override
	public Object visit(ASTCreateSensor node, Object data) {
		try{
			Class<?> sensorVisitor = Class.forName("de.uniol.inf.is.odysseus.objecttracking.parser.CreateSensorVisitor");
			Object sv = sensorVisitor.newInstance();
			Method m = sensorVisitor.getDeclaredMethod("visit",
					ASTCreateSensor.class, Object.class);
			return m.invoke(sv, node, data);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"Objecttracking plugin is missing in CQL parser.", e.getCause());
		} catch (Exception e) {
			throw new RuntimeException("Error while parsing the SENSOR clause",
					e.getCause());
		}
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
	public Object visit(ASTCreateUserStatement node, Object data) {
		String user = (String) node.jjtGetChild(0).jjtAccept(this, data);
		String password = node.getPassword();
		try {
			UserManagement.getInstance().registerUser(user, password);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}


}
