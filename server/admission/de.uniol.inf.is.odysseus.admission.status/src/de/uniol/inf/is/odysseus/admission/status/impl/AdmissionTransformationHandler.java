package de.uniol.inf.is.odysseus.admission.status.impl;

import java.util.List;

import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IPreTransformationHandler;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.querybuiltparameter.QueryBuildConfiguration;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.logicaloperator.latency.CalcLatencyAO;

public class AdmissionTransformationHandler implements IPreTransformationHandler {

	private String name = "AdmissionTransformationHandler";
	
	public AdmissionTransformationHandler() {
	}
	
	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void preTransform(IServerExecutor executor, ISession caller, ILogicalQuery query,
			QueryBuildConfiguration config, List<Pair<String, String>> handlerParameters, Context context) {
		/*ILogicalOperator operator = query.getLogicalPlan();
		CalcLatencyAO latency = new CalcLatencyAO();
		latency.subscribeTo(operator, operator.getOutputSchema());*/
	}

}
