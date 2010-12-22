package de.uniol.inf.is.odysseus.parser.cql;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.DifferenceAO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.IntersectionAO;
import de.uniol.inf.is.odysseus.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.parser.cql.parser.*;
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
import de.uniol.inf.is.odysseus.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.planmanagement.query.Query;
import de.uniol.inf.is.odysseus.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.predicate.NotPredicate;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.usermanagement.IServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.IUserAction;
import de.uniol.inf.is.odysseus.usermanagement.IllegalServiceLevelDefinition;
import de.uniol.inf.is.odysseus.usermanagement.PercentileConstraintOverlapException;
import de.uniol.inf.is.odysseus.usermanagement.PercentileContraint;
import de.uniol.inf.is.odysseus.usermanagement.ServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.TenantManagement;
import de.uniol.inf.is.odysseus.usermanagement.TenantNotFoundException;
import de.uniol.inf.is.odysseus.usermanagement.TimeBasedServiceLevelAgreement;
import de.uniol.inf.is.odysseus.usermanagement.TooManyUsersException;
import de.uniol.inf.is.odysseus.usermanagement.User;
import de.uniol.inf.is.odysseus.usermanagement.UserActionFactory;
import de.uniol.inf.is.odysseus.usermanagement.UserManagement;

public class CQLParser implements NewSQLParserVisitor, IQueryParser {

	private List<IQuery> plans = new ArrayList<IQuery>();
	private User caller;
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

	@Override
	public synchronized List<IQuery> parse(String query, User user)
			throws QueryParseException {
		this.caller = user;
		return parse(new StringReader(query), user);
	}

	@Override
	public synchronized List<IQuery> parse(Reader reader, User user)
			throws QueryParseException {
		this.caller = user;
		try {
			if (parser == null) {
				parser = new NewSQLParser(reader);
			} else {
				NewSQLParser.ReInit(reader);
			}
			ASTStatement statement = NewSQLParser.Statement();
			CQLParser cqlParser = new CQLParser();
			cqlParser.setUser(user);
			cqlParser.visit(statement, null);
			return cqlParser.plans;
		} catch (NoClassDefFoundError e) {
			throw new QueryParseException(
					"parse error: missing plugin for language feature",
					e.getCause());
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
	}

	public void setUser(User user) {
		this.caller = user;
	}

	@Override
	public Object visit(ASTStatement node, Object data) {
		return node.childrenAccept(this, null);
	}

	@Override
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
		query.setLogicalPlan(op, true);

		plans.add(query);
		return plans;
	}

	@Override
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
		setOperator.subscribeToSource(right, 1, 0, right.getOutputSchema());
		return setOperator;
	}

	@Override
	public Object visit(ASTSelectStatement statement, Object data) {
		try {
			CreateAccessAOVisitor access = new CreateAccessAOVisitor(caller);
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

			CreateJoinAOVisitor joinVisitor = new CreateJoinAOVisitor(caller);
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

	@Override
	public Object visit(ASTCreateStatement node, Object data) {
		CreateStreamVisitor v = new CreateStreamVisitor(caller);
		return v.visit(node, data);
	}

	@Override
	public Object visit(ASTCreateViewStatement node, Object data) {
		CreateViewVisitor v = new CreateViewVisitor(caller);
		return v.visit(node, data);
	}

	@Override
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
			initPredicate(curInputAO.getPredicate(),
					curInputAO.getInputSchema(0), rightInputSchema);
		}
		for (int i = 0; i < curInputAO.getSubscribedToSource().size(); ++i) {
			initPredicates(curInputAO.getSubscribedToSource(i).getTarget());
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
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

	@Override
	public Object visit(SimpleNode node, Object data) {
		return null;
	}

	public Object visit(ASTSQLStatement node, Object data) {
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
	public Object visit(ASTSetOperator node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTSelectClause node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTFromClause node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTWhereClause node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTGroupByClause node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTHavingClause node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTRenamedExpression node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTAS node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTPredicate node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTSimplePredicate node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTOrPredicate node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTAndPredicate node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTNotPredicate node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTAnyPredicate node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTAllPredicate node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTInPredicate node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTExists node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTTuple node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTTupleSet node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTQuantificationOperator node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTExpression node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTFunctionExpression node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTAggregateExpression node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTAggregateFunction node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTDistinctExpression node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTStreamSQLWindow node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTPartition node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTAdvance node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTSlide node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTInteger node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTNumber node, Object data) {
		return null;
	}

	@Override
	public Object visit(ASTString node, Object data) {
		return null;
	}

	@Override
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

			Method m = brokerSourceVisitor.getDeclaredMethod("setUser",
					User.class);
			m.invoke(bsv, caller);

			m = brokerSourceVisitor.getDeclaredMethod("visit",
					ASTCreateBroker.class, Object.class);
			AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m
					.invoke(bsv, node, data);

			addQuery(sourceOp);
			return plans;
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"Brokerplugin is missing in CQL parser.", e.getCause());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(
					"Error while parsing CREATE BROKER statement", e.getCause());
		}
	}

	private void addQuery(AbstractLogicalOperator sourceOp) {
		Query query = new Query();
		query.setParserId(getLanguage());
		query.setLogicalPlan(sourceOp, true);
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
					"Error while parsing the SELECT INTO statement",
					e.getCause());
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
		try {
			Class<?> sensorVisitor = Class
					.forName("de.uniol.inf.is.odysseus.objecttracking.parser.CreateSensorVisitor");
			Object sv = sensorVisitor.newInstance();
			Method m = sensorVisitor.getDeclaredMethod("setUser", User.class);
			m.invoke(sv, caller);
			m = sensorVisitor.getDeclaredMethod("visit", ASTCreateSensor.class,
					Object.class);
			return m.invoke(sv, node, data);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"Objecttracking plugin is missing in CQL parser.",
					e.getCause());
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
		String username = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		String password = node.getPassword();
		UserManagement.getInstance().registerUser(this.caller, username,
				password);
		return null;
	}

	@Override
	public Object visit(ASTAlterUserStatement node, Object data) {
		String username = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		String password = node.getPassword();
		try {
			UserManagement.getInstance().updateUserPassword(this.caller,
					username, password);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public Object visit(ASTDropStreamStatement node, Object data) {
		String streamname = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		DataDictionary.getInstance().removeViewOrStream(streamname, caller);
		return null;
	}

	@Override
	public Object visit(ASTDropViewStatement node, Object data) {
		String viewname = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		DataDictionary.getInstance().removeViewOrStream(viewname, caller);
		return null;
	}

	@Override
	public Object visit(ASTCreateTenantStatement node, Object data) {
		String tenant = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		String sla = ((ASTIdentifier) node.jjtGetChild(1)).getName();
		TenantManagement.getInstance().createTenant(tenant, sla, caller);
		return null;
	}

	@Override
	public Object visit(ASTAddUserToTenantStatement node, Object data) {
		String userName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		String tenantName = ((ASTIdentifier) node.jjtGetChild(1)).getName();
		User uToAdd = UserManagement.getInstance().findUser(userName, caller);
		try {
			TenantManagement.getInstance().addUserToTenant(tenantName, uToAdd,
					caller);
		} catch (TenantNotFoundException e) {
			throw new RuntimeException(e);
		} catch (TooManyUsersException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public Object visit(ASTRemoveUserFromTenantStatement node, Object data) {
		String tenantName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		String userName = ((ASTIdentifier) node.jjtGetChild(1)).getName();
		User uToRemove = UserManagement.getInstance()
				.findUser(userName, caller);
		try {
			TenantManagement.getInstance().removeUserFromTenant(tenantName,
					uToRemove, caller);
		} catch (TenantNotFoundException e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public Object visit(ASTCreateSLAStatement node, Object data) {
		String slaName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		PercentileContraint pc = null;
		IServiceLevelAgreement sla = null;
		if (node.getLimit() > 0) {
			sla = new TimeBasedServiceLevelAgreement(slaName, node.getLimit());
		} else {
			sla = new ServiceLevelAgreement(slaName);
		}
		for (int i = 2; i < node.jjtGetNumChildren(); i++) {
			pc = (PercentileContraint) node.jjtGetChild(i)
					.jjtAccept(this, data);
			sla.addPercentilConstraint(pc);
		}
		sla.init();
		sla.preCalc(100);
		TenantManagement.getInstance().addSLA(slaName, sla);
		return null;
	}

	@Override
	public Object visit(ASTPercentileConstraint node, Object data) {
		double left = Double.parseDouble(((ASTNumber) node.jjtGetChild(0))
				.getValue());
		double right = Double.parseDouble(((ASTNumber) node.jjtGetChild(1))
				.getValue());
		double penalty = Double.parseDouble(((ASTNumber) node.jjtGetChild(2))
				.getValue());
		return new PercentileContraint(right, left, penalty);
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
		AbstractLogicalOperator op;
		try {
			Class<?> visitor = Class
					.forName("de.uniol.inf.is.odysseus.storing.cql.DatabaseVisitor");
			Object v = visitor.newInstance();
			Method m = visitor.getDeclaredMethod("setUser", User.class);
			m.invoke(v, caller);
			m = visitor.getDeclaredMethod("visit",
					ASTInsertIntoStatement.class, Object.class);
			op = (AbstractLogicalOperator) m.invoke(v, node, data);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(
					"Storing plugin is missing in CQL parser.", e.getCause());
		} catch (Exception e) {
			throw new RuntimeException(
					"Error while parsing the insert into database clause",
					e.getCause());
		}

		Query query = new Query();
		query.setParserId(getLanguage());
		query.setLogicalPlan(op, true);

		plans.add(query);
		return plans;
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
		String userName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		try {
			UserManagement.getInstance().deleteUser(caller, userName);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public Object visit(ASTCreateRoleStatement node, Object data) {
		String rolename = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		try {
			UserManagement.getInstance().createRole(rolename, caller);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	public Object visit(ASTDropRoleStatement node, Object data) {
		String rolename = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		try {
			UserManagement.getInstance().deleteRole(rolename, caller);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object visit(ASTGrantStatement node, Object data) {
		List<String> rights = (List<String>) node.jjtGetChild(0).jjtAccept(
				this, data);
		// Validate if rights are User Actions
		List<IUserAction> operations = new ArrayList<IUserAction>();
		for (String r : rights) {
			IUserAction action = UserActionFactory.valueOf(r);
			if (action != null) {
				operations.add(action);
			} else {
				throw new RuntimeException("Right " + r + " not defined.");
			}
		}

		List<String> objects = null;
		String user = null;
		if (node.jjtGetNumChildren() == 3) {
			objects = (List<String>) node.jjtGetChild(1).jjtAccept(this, data);
			user = ((ASTIdentifier) node.jjtGetChild(2)).getName();
		} else {
			user = ((ASTIdentifier) node.jjtGetChild(1)).getName();
		}
		for (IUserAction action : operations) {

			if (UserActionFactory.needsNoObject(action)) {
				String object = UserActionFactory.getAliasObject(action);
				UserManagement.getInstance().grantPermission(caller, user,
						action, object);
			} else {
				for (String entityname : objects) {
					UserManagement.getInstance().grantPermission(caller, user,
							action, entityname);
				}
			}

		}

		return null;
	}

	@Override
	public Object visit(ASTRevokeStatement node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTGrantRoleStatement node, Object data) {
		if (node.jjtGetNumChildren() == 2) {
			@SuppressWarnings("unchecked")
			List<String> roles = (List<String>) node.jjtGetChild(0).jjtAccept(
					this, data);
			String user = ((ASTIdentifier) node.jjtGetChild(1)).getName();
			for (String rolename : roles) {
				UserManagement.getInstance().grantRole(caller, rolename, user);
			}
		}
		return null;
	}

	@Override
	public Object visit(ASTRevokeRoleStatement node, Object data) {
		if (node.jjtGetNumChildren() == 2) {
			@SuppressWarnings("unchecked")
			List<String> roles = (List<String>) node.jjtGetChild(0).jjtAccept(
					this, data);
			String user = ((ASTIdentifier) node.jjtGetChild(1)).getName();
			for (String rolename : roles) {
				UserManagement.getInstance().revokeRole(caller, rolename, user);
			}
		}
		return null;
	}

	@Override
	public Object visit(ASTIdentifierList node, Object data) {
		List<String> identifier = new ArrayList<String>();
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			identifier.add(((ASTIdentifier) node.jjtGetChild(i)).getName());
		}
		return identifier;
	}

	@Override
	public Object visit(ASTMVCovarianceRow node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

}
