package de.uniol.inf.is.odysseus.core.server.planmanagement.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.ExecutorBinder;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

abstract public class AbstractQueryInformationFunction<T> extends AbstractFunction<T> {

	private static final long serialVersionUID = -4911393059093909222L;

	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{ SDFDatatype.INTEGER }};

	AbstractQueryInformationFunction(String symbol, SDFDatatype returnType){
		super(symbol, 1, ACC_TYPES, returnType, false);
	}

	protected IPhysicalQuery getQuery(int queryId){
		IServerExecutor exec = ExecutorBinder.getExecutor();
		return exec.getExecutionPlan(this.getSessions().get(0)).getQueryById(queryId, getSessions().get(0));
	}
}
