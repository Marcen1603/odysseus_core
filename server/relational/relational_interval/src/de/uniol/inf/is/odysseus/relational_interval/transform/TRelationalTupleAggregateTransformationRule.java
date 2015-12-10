package de.uniol.inf.is.odysseus.relational_interval.transform;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TupleAggregateAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.sweeparea.DefaultTISweepArea;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.RelationalTupleAggregatePO;
import de.uniol.inf.is.odysseus.relational_interval.tupleaggregate.FirstRelationalTupleAggregateMethod;
import de.uniol.inf.is.odysseus.relational_interval.tupleaggregate.IRelationalTupleAggregateMethod;
import de.uniol.inf.is.odysseus.relational_interval.tupleaggregate.LastRelationalTupleAggregateMethod;
import de.uniol.inf.is.odysseus.relational_interval.tupleaggregate.MaxRelationalTupleAggregateMethod;
import de.uniol.inf.is.odysseus.relational_interval.tupleaggregate.MinRelationalTupleAggregateMethod;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.sweeparea.ITimeIntervalSweepArea;
import de.uniol.inf.is.odysseus.sweeparea.SweepAreaRegistry;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

public class TRelationalTupleAggregateTransformationRule extends
		AbstractRelationalIntervalTransformationRule<TupleAggregateAO> {

	static final Map<String, IRelationalTupleAggregateMethod> aggMethods = new HashMap<>();
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void execute(TupleAggregateAO operator,
			TransformationConfiguration config) throws RuleException {
		IRelationalTupleAggregateMethod m = getMethod(operator.getMethod());
		SDFAttribute attribute = operator.getAttribute();
		int pos = -1;
		if (attribute != null){
			pos = operator.getOutputSchema().indexOf(attribute);
		}
		ITimeIntervalSweepArea sa;
		try {
			sa = (ITimeIntervalSweepArea) SweepAreaRegistry.getSweepArea(DefaultTISweepArea.NAME);
		} catch (InstantiationException | IllegalAccessException e) {
			throw new RuleException(e);
		}
		RelationalTupleAggregatePO po = new RelationalTupleAggregatePO(m,pos, sa);
		defaultExecute(operator, po, config, true, true);
	}

	private IRelationalTupleAggregateMethod getMethod(String method) {
		IRelationalTupleAggregateMethod ret = aggMethods.get(method.toLowerCase());
		if (ret == null){
			if (method.equalsIgnoreCase("MAX")){
				ret = new MaxRelationalTupleAggregateMethod();
				aggMethods.put(method.toLowerCase(), ret);
			}else if (method.equalsIgnoreCase("MIN")){
				ret = new MinRelationalTupleAggregateMethod();
				aggMethods.put(method.toLowerCase(), ret);				
			}else if (method.equalsIgnoreCase("FIRST")){
				ret = new FirstRelationalTupleAggregateMethod();
				aggMethods.put(method.toLowerCase(), ret);				
			}else if (method.equalsIgnoreCase("LAST")){
				ret = new LastRelationalTupleAggregateMethod();
				aggMethods.put(method.toLowerCase(), ret);				
			}
		}
		return ret;
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}
	
	@Override
	public Class<? super TupleAggregateAO> getConditionClass() {	
		return TupleAggregateAO.class;
	}
}
