package de.uniol.inf.is.odysseus.test.component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.exception.PlanManagementException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.query.IPhysicalQuery;
import de.uniol.inf.is.odysseus.test.StatusCode;
import de.uniol.inf.is.odysseus.test.context.ITestContext;
import de.uniol.inf.is.odysseus.test.set.ExpectedOutputTestSet;
import de.uniol.inf.is.odysseus.test.sinks.physicaloperator.TICompareSink;

public abstract class AbstractQueryExpectedOutputTestComponent<T extends ITestContext, S extends ExpectedOutputTestSet> extends AbstractQueryTestComponent<T, S> {

	public AbstractQueryExpectedOutputTestComponent() {
		super(true);		
	}

	@Override
	protected StatusCode prepareQueries(Collection<Integer> ids, S set) {
		try {
			for (Integer queryId : ids) {
				IPhysicalQuery physicalQuery = executor.getExecutionPlan().getQueryById(queryId);
				executor.stopQuery(queryId, session);
				List<IPhysicalOperator> roots = new ArrayList<>();
				for (IPhysicalOperator operator : physicalQuery.getRoots()) {
					// TODO: this assumes same output for all sinks -> maybe
					// there are multiple sinks with different outputs
					List<Pair<String, String>> expected = set.getExpectedOutput();	
					String dataHandler = set.getDataHandler();
					TICompareSink sink = new TICompareSink(expected, dataHandler);
//					if(set.getName().equalsIgnoreCase("aggregate_time.qry")){
//						sink.setTracing(true);
//					}
					sink.addListener(this);
					roots.add(sink);
					@SuppressWarnings("unchecked")
					ISource<Tuple<ITimeInterval>> oldSink = (ISource<Tuple<ITimeInterval>>) operator;
					oldSink.subscribeSink(sink, 0, 0, operator.getOutputSchema());
				}
				physicalQuery.initializePhysicalRoots(roots);
			}

		} catch (PlanManagementException e) {
			e.printStackTrace();
			return StatusCode.EXCEPTION_DURING_TEST;
		} 
		return StatusCode.OK;

	}	
}
