package de.uniol.inf.is.odysseus.core.server.physicaloperator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.expression.RelationalExpression;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PlanModificationActionAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.ACQueryParameter;
import de.uniol.inf.is.odysseus.core.server.usermanagement.SessionManagement;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

public class PlanModificationActionPO extends AbstractSink<Tuple<IMetaAttribute>> {

	private static final Logger LOG = LoggerFactory.getLogger(PlanModificationActionPO.class);

	private final IServerExecutor executor;
	private final RelationalExpression<IMetaAttribute> actionExpression;
	private final RelationalExpression<IMetaAttribute> queryIDExpression;

	private ISession caller;

	public PlanModificationActionPO(PlanModificationActionAO ao, IServerExecutor executor) {
		Objects.requireNonNull(executor, "ServerExecutor must not be null!");
		Objects.requireNonNull(ao, "PlanModificationActionAO must not be null!");

		this.executor = executor;
		this.actionExpression = new RelationalExpression<IMetaAttribute>(ao.getCommandExpression());
		this.actionExpression.initVars(ao.getInputSchema(0));
		this.queryIDExpression = new RelationalExpression<IMetaAttribute>(ao.getQueryIDExpression());
		this.queryIDExpression.initVars(ao.getInputSchema(0));
	}

	@Override
	protected void process_open() throws OpenFailedException {

		List<ISession> callers = getSessions();

		if (callers.size() != 1) {
			throw new OpenFailedException("This operator cannot be sharded");
		}

		this.caller = callers.get(0);

	}

	@Override
	protected void process_next(Tuple<IMetaAttribute> object, int port) {
		List<Tuple<IMetaAttribute>> preProcessResult = null;

		String command = null;
		try {
			Object v = this.actionExpression.evaluate(object, getSessions(), preProcessResult);
			if (v != null) {
				command = v.toString();
			}
		} catch (Exception e) {

		}
		Integer queryID = null;
		try{
			Object v = this.queryIDExpression.evaluate(object, getSessions(), preProcessResult);
			if (v != null){
				queryID = Integer.parseInt(v.toString());
			}
		}catch(Exception e){

		}

		if (!Strings.isNullOrEmpty(command) && queryID != null) {
			tryExecuteCommand(command, queryID);
		}else{
			LOG.warn("Problems executing "+command+" on query id "+queryID);
		}
	}

	private void tryExecuteCommand(String command, Integer queryID) {
		ISession superUser =  SessionManagement.instance.loginSuperUser(null);
		IPhysicalQuery physicalQuery = executor.getExecutionPlan(superUser).getQueryById(queryID, superUser);
		if (physicalQuery == null) {
			LOG.error("Query id {} does not exist", queryID);
			return;
		}

		// avoid controlling rule-queries
		Object parameter = physicalQuery.getLogicalQuery().getParameter(ACQueryParameter.class.getSimpleName());
		if (parameter != null && parameter instanceof ACQueryParameter) {
			if (((ACQueryParameter) parameter).getValue()) {
				return;
			}
		}

		try {
			switch (command.toUpperCase()) {
			case "QUERY_ADD":
			case "QUERY_ADDED":
				LOG.error("Command QUERY_ADDED not supported, yet.");
				break;

			case "QUERY_REMOVE":
				executor.removeQuery(queryID, caller);
				break;

			case "QUERY_START":
				executor.startQuery(queryID, caller);
				break;

			case "QUERY_STOP":
				executor.stopQuery(queryID, caller);
				break;

			case "QUERY_SUSPEND":
				executor.suspendQuery(queryID, caller);
				break;

			case "QUERY_PARTIAL":
				executor.partialQuery(queryID, 50, caller);
				break;

			case "QUERY_FULL":
				executor.partialQuery(queryID, 100, caller);
				break;

			case "QUERY_RESUME":
				executor.resumeQuery(queryID, caller);
				break;

			default:
				LOG.error("Unknown command '{}' for query id {}", command, queryID);
			}
		} catch (Throwable t) {
			LOG.error("Could not execute command '{}' for query id {}", new Object[] { command, queryID, t });
		}
	}

	@Override
	public void processPunctuation(IPunctuation punctuation, int port) {
		// ignore
	}

	@Override
	public boolean isSemanticallyEqual(IPhysicalOperator ipo) {
		return false;
	}
}
