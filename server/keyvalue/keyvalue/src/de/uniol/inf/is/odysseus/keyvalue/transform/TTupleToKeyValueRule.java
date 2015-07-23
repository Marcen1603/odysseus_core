package de.uniol.inf.is.odysseus.keyvalue.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.keyvalue.logicaloperator.TupleToKeyValueAO;
import de.uniol.inf.is.odysseus.keyvalue.physicaloperator.TupleToKeyValuePO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TTupleToKeyValueRule extends AbstractTransformationRule<TupleToKeyValueAO> {


	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(TupleToKeyValueAO ao, TransformationConfiguration config) throws RuleException {
		SDFSchema newOutputSchema =  SDFSchema.changeType(ao.getInputSchema(), ao.getTypeClass());
		TupleToKeyValuePO<IMetaAttribute> po = new TupleToKeyValuePO<IMetaAttribute>();
		po.setOutputSchema(newOutputSchema);
		defaultExecute(ao, po, config, true, false);
	}

	@Override
	public String getName() {
		return "TupleToKeyValueAO --> TupleToKeyValuePO";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public boolean isExecutable(TupleToKeyValueAO operator, TransformationConfiguration config) {
		if (operator.getInputSchema().getType() == Tuple.class &&
				operator.isAllPhysicalInputSet()) {
			return true;
			
		}
		return false;
	}

}
