package de.uniol.inf.is.odysseus.planmanagement.optimization.event.startoptimize;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.planmanagement.query.IEditableQuery;
import de.uniol.inf.is.odysseus.physicaloperator.base.plan.IExecutionPlan;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizable;
import de.uniol.inf.is.odysseus.planmanagement.optimization.IOptimizer;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize.AbstractEndEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.event.endoptimize.MultiQueryEndEvent;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.AbstractOptimizationParameter;
import de.uniol.inf.is.odysseus.planmanagement.optimization.optimizeparameter.OptimizeParameter;

public class MultiQueryStartEvent extends AbstractStartEvent<ArrayList<IEditableQuery>> {

	public static String QUERIES_ADD = "QUERIES_ADD";
	
	public MultiQueryStartEvent(IOptimizable sender, String id, ArrayList<IEditableQuery> value,
			OptimizeParameter parameter) {
		super(sender, id, value, parameter);
	}

	public MultiQueryStartEvent(IOptimizable sender, String id, ArrayList<IEditableQuery> value,
			AbstractOptimizationParameter<?>... parameter) {
		super(sender, id, value, parameter);
	}
	
	@Override
	public AbstractEndEvent<?> createEndOptimizeEvent(IOptimizer optimizer,
			IExecutionPlan newExecutionPlan) {
		return new MultiQueryEndEvent(optimizer, getID(), getValue(), newExecutionPlan);
	}
}
