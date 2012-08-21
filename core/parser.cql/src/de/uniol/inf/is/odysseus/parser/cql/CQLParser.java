/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.parser.cql;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.DataDictionaryException;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.DifferenceAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.FileSinkAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IntersectionAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SocketSinkAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnionAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.planmanagement.query.LogicalQuery;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicate;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.core.server.sla.Metric;
import de.uniol.inf.is.odysseus.core.server.sla.Penalty;
import de.uniol.inf.is.odysseus.core.server.sla.SLA;
import de.uniol.inf.is.odysseus.core.server.sla.SLADictionary;
import de.uniol.inf.is.odysseus.core.server.sla.Scope;
import de.uniol.inf.is.odysseus.core.server.sla.ServiceLevel;
import de.uniol.inf.is.odysseus.core.server.sla.Window;
import de.uniol.inf.is.odysseus.core.server.sla.factories.MetricFactory;
import de.uniol.inf.is.odysseus.core.server.sla.factories.PenaltyFactory;
import de.uniol.inf.is.odysseus.core.server.sla.factories.ScopeFactory;
import de.uniol.inf.is.odysseus.core.server.sla.factories.UnitFactory;
import de.uniol.inf.is.odysseus.core.server.sla.unit.TimeUnit;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.AttributeResolver;
import de.uniol.inf.is.odysseus.core.server.usermanagement.PermissionFactory;
import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.IPermission;
import de.uniol.inf.is.odysseus.core.usermanagement.IRole;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.core.usermanagement.IUser;
import de.uniol.inf.is.odysseus.logicaloperator.intervalapproach.TimestampToPayloadAO;
import de.uniol.inf.is.odysseus.parser.cql.parser.*;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CheckAttributes;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CheckGroupBy;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CheckHaving;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateAccessAOVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateAggregationVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateJoinAOVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateProjectionVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateStreamVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateTypeVisitor;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.CreateViewVisitor;
import de.uniol.inf.is.odysseus.relational.base.predicate.IRelationalPredicate;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class CQLParser implements NewSQLParserVisitor, IQueryParser {

	private List<ILogicalQuery> plans = new ArrayList<ILogicalQuery>();
	private ISession caller;
	private IDataDictionary dataDictionary;
	private static CQLParser instance = null;
	private static NewSQLParser parser;

	public static synchronized IQueryParser getInstance() {
		if (instance == null) {
			instance = new CQLParser();
		}
		return instance;
	}

	protected IDataDictionary getDataDictionary() {
		return this.dataDictionary;
	}

	protected ISession getCaller() {
		return this.caller;
	}

	@Override
	public String getLanguage() {
		return "CQL";
	}

	@Override
	public synchronized List<ILogicalQuery> parse(String query, ISession user, IDataDictionary dd) throws QueryParseException {
		this.caller = user;
		this.dataDictionary = dd;
		return parse(new StringReader(query), user, dd);
	}

	@Override
	public synchronized List<ILogicalQuery> parse(Reader reader, ISession user, IDataDictionary dd) throws QueryParseException {
		this.caller = user;
		this.dataDictionary = dd;
		try {
			if (parser == null) {
				parser = new NewSQLParser(reader);
			} else {
				NewSQLParser.ReInit(reader);
			}
			ASTStatement statement = NewSQLParser.Statement();
			CQLParser cqlParser = new CQLParser();
			cqlParser.setUser(user);
			cqlParser.setDataDictionary(dataDictionary);
			cqlParser.visit(statement, null);
			return cqlParser.plans;
		} catch (ParseException e) {
			throw new QueryParseException(e.getMessage(), e);
		}
	}

	public void setUser(ISession user) {
		this.caller = user;
	}

	public void setDataDictionary(IDataDictionary dataDictionary) {
		this.dataDictionary = dataDictionary;
	}

	@Override
	public Object visit(ASTStatement node, Object data) throws QueryParseException {
		return node.childrenAccept(this, null);
	}

	@Override
	public Object visit(ASTPriorizedStatement node, Object data) throws QueryParseException {
		AbstractLogicalOperator op;
		Integer priority = 0;
		op = (AbstractLogicalOperator) node.jjtGetChild(0).jjtAccept(this, data);
		if (node.jjtGetNumChildren() == 2) {
			if (node.jjtGetChild(1) instanceof ASTPriority) {
				priority = (Integer) node.jjtGetChild(1).jjtAccept(this, data);
			} else {
				if (node.jjtGetChild(1) instanceof ASTMetric) {
					op = (AbstractLogicalOperator) node.jjtGetChild(1).jjtAccept(this, op);
				}
			}
		}
		LogicalQuery query = new LogicalQuery();
		query.setParserId(getLanguage());
		query.setPriority(priority);
		query.setLogicalPlan(op, true);

		plans.add(query);
		return plans;
	}

	@Override
	public Object visit(ASTComplexSelectStatement node, Object data) throws QueryParseException {
		if (node.jjtGetNumChildren() == 1) {
			return node.jjtGetChild(0).jjtAccept(this, data);
		}
		AbstractLogicalOperator left = (AbstractLogicalOperator) node.jjtGetChild(0).jjtAccept(this, data);
		AbstractLogicalOperator right = (AbstractLogicalOperator) node.jjtGetChild(2).jjtAccept(this, data);
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
			throw new QueryParseException("inputs of set operator have different schemas");
		}
		Iterator<SDFAttribute> iLeft = left.getOutputSchema().iterator();
		Iterator<SDFAttribute> iRight = right.getOutputSchema().iterator();
		while (iLeft.hasNext()) {
			SDFAttribute curLeft = iLeft.next();
			SDFAttribute curRight = iRight.next();
			// TODO problem: man kann keine zwei numerischen typen verknuepfen
			// dazu muesste etwas wie typkompatibilitaet definiert werden
			if (curLeft.getDatatype() != curRight.getDatatype()) {
				throw new QueryParseException("inputs of set operator have different schemas");
			}
		}
		setOperator.subscribeToSource(left, 0, 0, left.getOutputSchema());
		setOperator.subscribeToSource(right, 1, 0, right.getOutputSchema());
		return setOperator;
	}

	@Override
	public Object visit(ASTSelectStatement statement, Object data) throws QueryParseException {
		try {
			CreateAccessAOVisitor access = new CreateAccessAOVisitor(caller, dataDictionary);
			access.visit(statement, null);
			AttributeResolver attributeResolver = access.getAttributeResolver();

			new CheckAttributes(attributeResolver).visit(statement, null);

			CheckGroupBy checkGroupBy = new CheckGroupBy();
			checkGroupBy.init(attributeResolver);
			checkGroupBy.visit(statement, null);
			if (!checkGroupBy.checkOkay()) {
				throw new QueryParseException("missing attributes in GROUP BY clause");
			}

			CheckHaving checkHaving = new CheckHaving();
			checkHaving.init(attributeResolver);
			checkHaving.visit(statement, null);

			CreateJoinAOVisitor joinVisitor = new CreateJoinAOVisitor(caller, dataDictionary);
			joinVisitor.init(attributeResolver);
			ILogicalOperator top = (AbstractLogicalOperator) joinVisitor.visit(statement, null);

			CreateAggregationVisitor aggregationVisitor = new CreateAggregationVisitor();
			aggregationVisitor.init(top, attributeResolver);
			aggregationVisitor.visit(statement, null);
			top = aggregationVisitor.getResult();

			// top = new CreateProjectionVisitor().createProjection(statement,
			// top, attributeResolver);

			CreateProjectionVisitor projectionVisitor = new CreateProjectionVisitor(top, attributeResolver);
			projectionVisitor.visit(statement, null);
			top = projectionVisitor.getTop();

			try {
				Class<?> prioVisitor = Class.forName("de.uniol.inf.is.odysseus.priority.CreatePriorityAOVisitor");
				Object pv = prioVisitor.newInstance();
				// prioVisitor.setTopOperator(top);

				Method m = prioVisitor.getDeclaredMethod("setTopOperator", ILogicalOperator.class);
				m.invoke(pv, top);

				// prioVisitor.setAttributeResolver(attributeResolver);
				m = prioVisitor.getDeclaredMethod("setAttributeResolver", AttributeResolver.class);
				m.invoke(pv, attributeResolver);

				// prioVisitor.visit(statement, null);
				m = prioVisitor.getDeclaredMethod("visit", AttributeResolver.class);
				m.invoke(pv, caller);

				// top = prioVisitor.getTopOperator();
				top = (ILogicalOperator) prioVisitor.getDeclaredMethod("getTopOperator", (Class[]) null).invoke(pv, (Object[]) (null));

			} catch (ClassNotFoundException e) {
				// Ignore --> Prio not loaded
			}
			return top;
		} catch (QueryParseException ex) {
			throw ex;
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
	}

	@Override
	public Object visit(ASTCreateStatement node, Object data) throws QueryParseException {
		CreateStreamVisitor v = new CreateStreamVisitor(caller, dataDictionary);
		return v.visit(node, data);
	}

	@Override
	public Object visit(ASTFileSource node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTCreateViewStatement node, Object data) throws QueryParseException {
		CreateViewVisitor v = new CreateViewVisitor(caller, dataDictionary);
		return v.visit(node, data);
	}

	@Override
	public Object visit(ASTPriority node, Object data) throws QueryParseException {
		return node.getPriority();
	}

	public static void initPredicates(ILogicalOperator curInputAO) {
		if (curInputAO.getPredicate() != null) {
			SDFSchema rightInputSchema = null;
			if (curInputAO.getSubscribedToSource().size() > 1) {
				rightInputSchema = curInputAO.getInputSchema(1);
			}
			initPredicate(curInputAO.getPredicate(), curInputAO.getInputSchema(0), rightInputSchema);
		}
		for (int i = 0; i < curInputAO.getSubscribedToSource().size(); ++i) {
			initPredicates(curInputAO.getSubscribedToSource(i).getTarget());
		}
	}

	public static void initPredicate(IPredicate<?> predicate, SDFSchema left, SDFSchema right) {
		if (predicate instanceof ComplexPredicate) {
			ComplexPredicate<?> compPred = (ComplexPredicate<?>) predicate;
			initPredicate(compPred.getLeft(), left, right);
			initPredicate(compPred.getRight(), left, right);
			return;
		}
		if (ComplexPredicateHelper.isNotPredicate(predicate)) {
			initPredicate(ComplexPredicateHelper.getChild(predicate), left, right);
			return;
		}
		if (predicate instanceof IRelationalPredicate) {
			((RelationalPredicate) predicate).init(left, right);
		}
		// NOTE: Das ProbabilityPredicate muss nicht mit linkem
		// und rechtem Schema initialisiert werden.
	}

	@Override
	public Object visit(SimpleNode node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTAttributeDefinitions node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTAttributeDefinition node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTTimedTuples node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTTimedTuple node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTTimeInterval node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTSimpleTuple node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTAttributeType node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTSetOperator node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTSelectClause node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTFromClause node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTWhereClause node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTGroupByClause node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTHavingClause node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTRenamedExpression node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTAS node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTPredicate node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTSimplePredicate node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTOrPredicate node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTAndPredicate node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTNotPredicate node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTAnyPredicate node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTAllPredicate node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTInPredicate node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTExists node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTTuple node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTTupleSet node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTQuantificationOperator node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTExpression node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTSimpleToken node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTFunctionExpression node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTAggregateExpression node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTAggregateFunction node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTDistinctExpression node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTStreamSQLWindow node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTPartition node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTAdvance node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTSlide node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTIdentifier node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTInteger node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTNumber node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTString node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTCompareOperator node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTElementPriorities node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTElementPriority node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTDefaultPriority node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTSocket node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTHost node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTChannel node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTSpatialCompareOperator node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTCovarianceRow node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTMatrixExpression node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTProbabilityPredicate node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTDateFormat node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTSimpleSource node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTSubselect node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTBasicPredicate node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTQuantificationPredicate node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTProjectionMatrix node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTProjectionVector node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTSelectAll node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTFunctionName node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTSilab node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTCreateBroker node, Object data) throws QueryParseException {
		try {
			Class<?> brokerSourceVisitor = Class.forName("de.uniol.inf.is.odysseus.broker.parser.cql.BrokerVisitor");
			Object bsv = brokerSourceVisitor.newInstance();
			Method m = brokerSourceVisitor.getDeclaredMethod("setUser", ISession.class);
			m.invoke(bsv, caller);

			Method m2 = brokerSourceVisitor.getDeclaredMethod("setDataDictionary", IDataDictionary.class);
			m2.invoke(bsv, dataDictionary);

			m = brokerSourceVisitor.getDeclaredMethod("visit", ASTCreateBroker.class, Object.class);
			AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m.invoke(bsv, node, data);

			addQuery(sourceOp);
			return plans;
		} catch (ClassNotFoundException e) {
			throw new QueryParseException("Brokerplugin is missing in CQL parser.", e.getCause());
		} catch (Exception e) {
			throw new QueryParseException("Error while parsing CREATE BROKER statement", e.getCause());
		}
	}

	private void addQuery(AbstractLogicalOperator sourceOp) {
		LogicalQuery query = new LogicalQuery();
		query.setParserId(getLanguage());
		query.setLogicalPlan(sourceOp, true);
		plans.add(query);
	}

	@Override
	public Object visit(ASTBrokerSource node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTBrokerSelectInto node, Object data) throws QueryParseException {

		try {
			Class<?> brokerSourceVisitor = Class.forName("de.uniol.inf.is.odysseus.broker.parser.cql.BrokerVisitor");
			Object bsv = brokerSourceVisitor.newInstance();
			Method m = brokerSourceVisitor.getDeclaredMethod("setUser", ISession.class);
			m.invoke(bsv, caller);
			Method m2 = brokerSourceVisitor.getDeclaredMethod("setDataDictionary", IDataDictionary.class);
			m2.invoke(bsv, dataDictionary);

			m = brokerSourceVisitor.getDeclaredMethod("visit", ASTBrokerSelectInto.class, Object.class);
			AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m.invoke(bsv, node, data);

			addQuery(sourceOp);
			return plans;
		} catch (ClassNotFoundException e) {
			throw new QueryParseException("Brokerplugin is missing in CQL parser.", e.getCause());
		} catch (Exception e) {
			throw new QueryParseException("Error while parsing the SELECT INTO statement", e.getCause());
		}
	}

	@Override
	public Object visit(ASTBrokerAsSource node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTBrokerSimpleSource node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTBrokerQueue node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTMetric node, Object data) throws QueryParseException {
		try {
			Class<?> brokerSourceVisitor = Class.forName("de.uniol.inf.is.odysseus.broker.parser.cql.BrokerVisitor");
			Object bsv = brokerSourceVisitor.newInstance();
			Method m2 = brokerSourceVisitor.getDeclaredMethod("setDataDictionary", IDataDictionary.class);
			m2.invoke(bsv, dataDictionary);
			Method m = brokerSourceVisitor.getDeclaredMethod("setUser", ISession.class);
			m.invoke(bsv, caller);
			m = brokerSourceVisitor.getDeclaredMethod("visit", ASTMetric.class, Object.class);
			AbstractLogicalOperator sourceOp = (AbstractLogicalOperator) m.invoke(bsv, node, data);
			return sourceOp;
		} catch (ClassNotFoundException e) {
			throw new QueryParseException("Brokerplugin is missing in CQL parser.", e.getCause());
		} catch (Exception e) {
			throw new QueryParseException("Error while parsing the METRIC clause", e.getCause());
		}
	}

	@Override
	public Object visit(ASTCreateSensor node, Object data) throws QueryParseException {
		try {
			Class<?> sensorVisitor = Class.forName("de.uniol.inf.is.odysseus.objecttracking.parser.CreateSensorVisitor");
			Object sv = sensorVisitor.newInstance();
			Method m = sensorVisitor.getDeclaredMethod("setUser", ISession.class);
			m.invoke(sv, caller);
			Method m2 = sensorVisitor.getDeclaredMethod("setDataDictionary", IDataDictionary.class);
			m2.invoke(sv, dataDictionary);

			m = sensorVisitor.getDeclaredMethod("visit", ASTCreateSensor.class, Object.class);
			return m.invoke(sv, node, data);
		} catch (ClassNotFoundException e) {
			throw new QueryParseException("Objecttracking plugin is missing in CQL parser.", e.getCause());
		} catch (Exception e) {
			throw new QueryParseException("Error while parsing the SENSOR clause", e.getCause());
		}
	}

	@Override
	public Object visit(ASTORSchemaDefinition node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTRecordDefinition node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTRecordEntryDefinition node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTListDefinition node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTAttrDefinition node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTCreateUserStatement node, Object data) throws QueryParseException {
		String username = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		String password = node.getPassword();
		IUser user = UserManagement.getUsermanagement().createUser(username, caller);
		if (user == null)
			throw new QueryParseException("User cannot be created.");
		UserManagement.getUsermanagement().changePassword(user, password.getBytes(), caller);
		UserManagement.getUsermanagement().activateUser(user, caller);
		return null;
	}

	@Override
	public Object visit(ASTAlterUserStatement node, Object data) throws QueryParseException {
		String username = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		String password = node.getPassword();
		try {
			IUser user = UserManagement.getUsermanagement().findUser(username, caller);
			if (user != null) {
				UserManagement.getUsermanagement().changePassword(user, password.getBytes(), caller);
			}
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
		return null;
	}

	@Override
	public Object visit(ASTDropStreamStatement node, Object data) throws QueryParseException {
		String streamname = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		boolean ifExists = false;
		if (node.jjtGetNumChildren() >= 2) {
			if (node.jjtGetChild(1) instanceof ASTIfExists) {
				ifExists = true;
			}
		}
		if (ifExists) {
			if (dataDictionary.containsViewOrStream(streamname, caller)) {
				dataDictionary.removeViewOrStream(streamname, caller);
			}
		} else {
			dataDictionary.removeViewOrStream(streamname, caller);
		}
		return null;
	}

	@Override
	public Object visit(ASTDropViewStatement node, Object data) throws QueryParseException {
		String viewname = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		dataDictionary.removeViewOrStream(viewname, caller);
		return null;
	}

	@Override
	public Object visit(ASTCreateFromDatabase node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTDatabaseTimeSensitiv node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTDropUserStatement node, Object data) throws QueryParseException {
		String userName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		try {
			IUser user = UserManagement.getUsermanagement().findUser(userName, caller);
			UserManagement.getUsermanagement().deleteUser(user, caller);
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
		return null;
	}

	@Override
	public Object visit(ASTCreateRoleStatement node, Object data) throws QueryParseException {
		String rolename = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		try {
			UserManagement.getUsermanagement().createRole(rolename, caller);
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
		return null;
	}

	@Override
	public Object visit(ASTDropRoleStatement node, Object data) throws QueryParseException {
		String rolename = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		try {
			IRole role = UserManagement.getUsermanagement().findRole(rolename, caller);
			if (role != null) {
				UserManagement.getUsermanagement().deleteRole(role, caller);
			}
		} catch (Exception e) {
			throw new QueryParseException(e);
		}
		return null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Object visit(ASTGrantStatement node, Object data) throws QueryParseException {
		List<String> rights = (List<String>) node.jjtGetChild(0).jjtAccept(this, data);
		// Validate if rights are User Actions
		List<IPermission> operations = new ArrayList<IPermission>();
		for (String r : rights) {
			IPermission action = PermissionFactory.valueOf(r);
			if (action != null) {
				operations.add(action);
			} else {
				throw new QueryParseException("Right " + r + " not defined.");
			}
		}

		List<String> objects = null;
		String userName = null;
		if (node.jjtGetNumChildren() == 3) {
			objects = (List<String>) node.jjtGetChild(1).jjtAccept(this, data);
			userName = ((ASTIdentifier) node.jjtGetChild(2)).getName();
		} else {
			userName = ((ASTIdentifier) node.jjtGetChild(1)).getName();
		}
		IUser user = UserManagement.getUsermanagement().findUser(userName, caller);
		if (user != null) {
			for (IPermission action : operations) {

				if (PermissionFactory.needsNoObject(action)) {
					UserManagement.getUsermanagement().grantPermission(user, action, null, caller);
				} else {
					for (String entityname : objects) {
						UserManagement.getUsermanagement().grantPermission(user, action, entityname, caller);
					}
				}

			}
		}
		IRole role = UserManagement.getUsermanagement().findRole(userName, caller);
		if (role != null) {
			for (IPermission action : operations) {

				if (PermissionFactory.needsNoObject(action)) {
					UserManagement.getUsermanagement().grantPermission(role, action, null, caller);
				} else {
					for (String entityname : objects) {
						UserManagement.getUsermanagement().grantPermission(role, action, entityname, caller);
					}
				}

			}
		}

		return null;
	}

	@Override
	public Object visit(ASTRevokeStatement node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTGrantRoleStatement node, Object data) throws QueryParseException {
		if (node.jjtGetNumChildren() == 2) {
			@SuppressWarnings("unchecked")
			List<String> roles = (List<String>) node.jjtGetChild(0).jjtAccept(this, data);
			String userName = ((ASTIdentifier) node.jjtGetChild(1)).getName();
			IUser user = UserManagement.getUsermanagement().findUser(userName, caller);
			for (String rolename : roles) {
				IRole role = UserManagement.getUsermanagement().getRole(rolename, caller);
				UserManagement.getUsermanagement().grantRole(user, role, caller);
			}
		}
		return null;
	}

	@Override
	public Object visit(ASTRevokeRoleStatement node, Object data) throws QueryParseException {
		if (node.jjtGetNumChildren() == 2) {
			@SuppressWarnings("unchecked")
			List<String> roles = (List<String>) node.jjtGetChild(0).jjtAccept(this, data);
			String userName = ((ASTIdentifier) node.jjtGetChild(1)).getName();
			IUser user = UserManagement.getUsermanagement().findUser(userName, caller);
			for (String rolename : roles) {
				IRole role = UserManagement.getUsermanagement().getRole(rolename, caller);
				UserManagement.getUsermanagement().revokeRole(user, role, caller);
			}
		}
		return null;
	}

	@Override
	public Object visit(ASTIdentifierList node, Object data) throws QueryParseException {
		List<String> identifier = new ArrayList<String>();
		for (int i = 0; i < node.jjtGetNumChildren(); i++) {
			identifier.add(((ASTIdentifier) node.jjtGetChild(i)).getName());
		}
		return identifier;
	}

	@Override
	public Object visit(ASTMVCovarianceRow node, Object data) throws QueryParseException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.parser.cql.parser.NewSQLParserVisitor#visit(
	 * de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateType,
	 * java.lang.Object)
	 */
	@Override
	public Object visit(ASTCreateType node, Object data) throws QueryParseException {
		CreateTypeVisitor v = new CreateTypeVisitor(this.caller, this.dataDictionary);
		return v.visit(node, data);
	}

	@Override
	public Object visit(ASTCreateSLAStatement node, Object data) throws QueryParseException {
		// String slaName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		// PercentileContraint pc = null;
		// IServiceLevelAgreement sla = null;
		// if (node.getLimit() > 0) {
		// sla = new TimeBasedServiceLevelAgreement(slaName, node.getLimit());
		// } else {
		// sla = new ServiceLevelAgreement(slaName);
		// }
		// for (int i = 2; i < node.jjtGetNumChildren(); i++) {
		// pc = (PercentileContraint) node.jjtGetChild(i)
		// .jjtAccept(this, data);
		// sla.addPercentilConstraint(pc);
		// }
		// sla.init();
		// sla.preCalc(100);
		// TenantManagement.getInstance().addSLA(slaName, sla);

		SLA sla = new SLA();

		// get name
		String slaName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		sla.setName(slaName);

		// get metric
		Metric<?> metric = (Metric<?>) node.jjtGetChild(1).jjtAccept(this, null);
		sla.setMetric(metric);

		// get scope
		Scope scope = (Scope) node.jjtGetChild(2).jjtAccept(this, null);
		sla.setScope(scope);

		// get window
		Window window = (Window) node.jjtGetChild(3).jjtAccept(this, null);
		sla.setWindow(window);

		// get service level(s)
		List<ServiceLevel> serviceLevels = new ArrayList<ServiceLevel>();
		for (int i = 4; i < node.jjtGetNumChildren(); i++) {
			ServiceLevel sl = (ServiceLevel) node.jjtGetChild(i).jjtAccept(this, null);
			sl.setSla(sla);
			serviceLevels.add(sl);
		}
		sla.setServiceLevel(serviceLevels);

		// save sla
		if (!SLADictionary.getInstance().exists(slaName)) {
			SLADictionary.getInstance().addSLA(slaName, sla);
			System.out.println("Added new Service Level Agreement: " + sla);
		} else {
			throw new QueryParseException("A Serivce Level Agreement with the " + "given name already exists: " + slaName);
		}

		return null;
	}

	@Override
	public Object visit(ASTSlaMetricDef node, Object data) throws QueryParseException {
		String metricID = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		double value = Double.parseDouble(((ASTNumber) node.jjtGetChild(1)).getValue());
		String unitID = ((ASTIdentifier) node.jjtGetChild(2)).getName();

		// create metric by factory and return it
		MetricFactory metricFactory = new MetricFactory();
		UnitFactory unitFactory = new UnitFactory();
		Metric<?> metric = metricFactory.buildMetric(metricID, value, unitFactory.buildUnit(unitID));

		return metric;
	}

	@Override
	public Object visit(ASTSlaScopeDef node, Object data) throws QueryParseException {
		String scopeID = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		ScopeFactory factory = new ScopeFactory();
		return factory.buildScope(scopeID);
	}

	@Override
	public Object visit(ASTSlaWindowDef node, Object data) throws QueryParseException {
		int windowSize = Integer.parseInt(((ASTNumber) node.jjtGetChild(0)).getValue());
		String unitID = ((ASTIdentifier) node.jjtGetChild(1)).getName();

		UnitFactory unitFactory = new UnitFactory();
		TimeUnit unit = (TimeUnit) unitFactory.buildUnit(unitID);
		Window window = new Window(windowSize, unit);

		return window;
	}

	@Override
	public Object visit(ASTSlaServiceLevelDef node, Object data) throws QueryParseException {
		double threshold = Double.parseDouble(((ASTNumber) node.jjtGetChild(0)).getValue());

		Penalty penalty = (Penalty) node.jjtGetChild(1).jjtAccept(this, null);
		// TODO set service level in penalty

		ServiceLevel sl = new ServiceLevel();
		sl.setThreshold(threshold);
		sl.setPenalty(penalty);

		return sl;
	}

	@Override
	public Object visit(ASTSlaPenaltyDef node, Object data) throws QueryParseException {
		String penaltyID = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		double value = Double.parseDouble(((ASTNumber) node.jjtGetChild(1)).getValue());

		PenaltyFactory factory = new PenaltyFactory();
		return factory.buildPenalty(penaltyID, value);
	}

	@Override
	public Object visit(ASTCreateSinkStatement node, Object data) throws QueryParseException {
		String sinkName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		node.jjtGetChild(1).jjtAccept(this, sinkName);
		return null;
	}

	@Override
	public Object visit(ASTStreamToStatement node, Object data) throws QueryParseException {
		String sinkName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		ASTSelectStatement statement = (ASTSelectStatement) node.jjtGetChild(1);
		ILogicalOperator top = (ILogicalOperator) visit(statement, data);

		ILogicalOperator sink;
		// Append plan to input and update subscriptions
		ILogicalOperator sinkInput;
		try {
			sink = dataDictionary.getSinkTop(sinkName, caller);
			sinkInput = dataDictionary.getSinkInput(sinkName, caller);
		} catch (Exception e) {
			throw new QueryParseException(e.getMessage());
		}
		sinkInput.subscribeToSource(top, -1, 0, top.getOutputSchema());
		updateSchemaInfos(sink);
		// if database -> be sure, that the schemas are equal
		if (sink.getClass().getCanonicalName().equals("de.uniol.inf.is.odysseus.database.logicaloperator.DatabaseSinkAO")) {
			invokeDatabaseVisitor(ASTStreamToStatement.class, node, sink);
		}

		LogicalQuery query = new LogicalQuery();
		query.setParserId(getLanguage());
		query.setLogicalPlan(sink, true);

		plans.add(query);
		return plans;
	}

	private void updateSchemaInfos(ILogicalOperator sink) {
		for (int i = 0; i < sink.getNumberOfInputs(); i++) {
			LogicalSubscription sub = sink.getSubscribedToSource(i);
			ILogicalOperator source = sub.getTarget();
			if (sub.getSchema() == null) {
				if (source.getOutputSchema() == null) {
					updateSchemaInfos(source);
				} else {
					// Set Schema for both directions (sink to source and source
					// to sink)!
					sub.setSchema(source.getOutputSchema());
					for (LogicalSubscription sourceSub : source.getSubscriptions()) {
						if (sourceSub.getTarget() == sink) {
							sourceSub.setSchema(source.getOutputSchema());
						}
					}
				}
			}
		}
	}

	@Override
	public Object visit(ASTLoginPassword node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTAutoReconnect node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTDatabaseSink node, Object data) throws QueryParseException {
		return invokeDatabaseVisitor(ASTDatabaseSink.class, node, data);
	}

	@Override
	public Object visit(ASTDatabaseSinkOptions node, Object data) throws QueryParseException {
		return invokeDatabaseVisitor(ASTDatabaseSinkOptions.class, node, data);
	}

	@Override
	public Object visit(ASTCreateDatabaseConnection node, Object data) throws QueryParseException {
		return invokeDatabaseVisitor(ASTCreateDatabaseConnection.class, node, data);
	}

	private Object invokeDatabaseVisitor(Class<?> nodeclass, Object node, Object data) throws QueryParseException {
		try {
			Class<?> visitor = Class.forName("de.uniol.inf.is.odysseus.database.cql.DatabaseVisitor");
			Object v = visitor.newInstance();
			Method m = visitor.getDeclaredMethod("setUser", ISession.class);
			m.invoke(v, caller);
			m = visitor.getDeclaredMethod("setDataDictionary", IDataDictionary.class);
			m.invoke(v, dataDictionary);
			m = visitor.getDeclaredMethod("visit", nodeclass, Object.class);
			return (AbstractLogicalOperator) m.invoke(v, node, data);
		} catch (ClassNotFoundException e) {
			throw new QueryParseException("Database plugin is missing in CQL parser.", e.getCause());
		} catch (NoSuchMethodException e) {
			throw new QueryParseException("Method in database plugin is missing.", e.getCause());
		} catch (SecurityException e) {
			throw new QueryParseException("Database plugin is missing in CQL parser.", e.getCause());
		} catch (IllegalAccessException e) {
			throw new QueryParseException("Database plugin is missing in CQL parser.", e.getCause());
		} catch (IllegalArgumentException e) {
			throw new QueryParseException("Database plugin is missing in CQL parser.", e.getCause());
		} catch (InvocationTargetException e) {
			throw new QueryParseException(e.getTargetException().getLocalizedMessage());
		} catch (InstantiationException e) {
			throw new QueryParseException("Cannot create instance of database plugin.", e.getCause());
		}
	}

	@Override
	public Object visit(ASTTime node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTFileSink node, Object data) throws QueryParseException {
		String sinkName = (String) data;
		String filename = node.getFilename();
		String type = "STRING";
		long writeAfterElements = 1;
		boolean printMetadata = false;
		if (node.jjtGetNumChildren() >= 1) {
			type = ((ASTIdentifier) node.jjtGetChild(0)).getName();

			for (int i = 1; i < node.jjtGetNumChildren(); i++) {
				if (node.jjtGetChild(i) instanceof ASTIdentifier) {
					String val = ((ASTIdentifier) node.jjtGetChild(i)).getName();
					if ("withmeta".equalsIgnoreCase(val)) {
						printMetadata = true;
					}
				} else if (node.jjtGetChild(i) instanceof ASTInteger)
					writeAfterElements = ((ASTInteger) node.jjtGetChild(i)).getValue();
			}

		}

		ILogicalOperator sink = new FileSinkAO(filename, type, writeAfterElements, printMetadata);
		try {
			dataDictionary.addSink(sinkName, sink, caller);
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e.getMessage());
		}
		return null;
	}

	@Override
	public Object visit(ASTSocketSink node, Object data) throws QueryParseException {
		String sinkName = (String) data;
		int port = ((ASTInteger) node.jjtGetChild(0)).getValue().intValue();
		String sinkType = ((ASTIdentifier) node.jjtGetChild(1)).getName();
		boolean loginNeeded = false;
		if (node.jjtGetNumChildren() == 3) {
			loginNeeded = true;
		}
		ILogicalOperator sink = new SocketSinkAO(port, sinkType, loginNeeded, sinkName);
		ILogicalOperator transformMeta = new TimestampToPayloadAO();
		sink.subscribeToSource(transformMeta, 0, 0, null);
		try {
			dataDictionary.addSink(sinkName, sink, caller);
		} catch (DataDictionaryException e) {
			throw new QueryParseException(e.getMessage());
		}
		return null;
	}

	@Override
	public Object visit(ASTIfExists node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTCreateContextStore node, Object data) throws QueryParseException {
		IVisitor visitor = VisitorFactory.getInstance().getVisitor("de.uniol.inf.is.odysseus.context.cql.ContextVisitor");
		visitor.setDataDictionary(dataDictionary);
		visitor.setUser(caller);
		visitor.visit(node, data, this);
		return null;
	}

	@Override
	public Object visit(ASTContextStoreType node, Object data) throws QueryParseException {
		return null;
	}

	@Override
	public Object visit(ASTDropContextStore node, Object data) throws QueryParseException {
		IVisitor visitor = VisitorFactory.getInstance().getVisitor("de.uniol.inf.is.odysseus.context.cql.ContextVisitor");
		visitor.setDataDictionary(dataDictionary);
		visitor.setUser(caller);
		visitor.visit(node, data, this);
		return null;
	}

	@Override
	public Object visit(ASTIfNotExists node, Object data) throws QueryParseException {
		return null;
	}

}
