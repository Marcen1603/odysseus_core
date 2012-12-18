package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AppendToPhysicalAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TAppendToPhysicalAORule extends AbstractTransformationRule<AppendToPhysicalAO> {

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public void execute(AppendToPhysicalAO operator,
			TransformationConfiguration config) {
		String appendTo = operator.getAppendTo();
		// FIXME: Potential security risk! We can add our source to
		// other queries --> Need a concept!!
		IPhysicalOperator po = getDataDictionary().getOperator(appendTo);
		if (po == null){
			po = getDataDictionary().getOperator(getDataDictionary().createUserUri(appendTo, getCaller()));
		}
		
		if (po == null){
			throw new TransformationException("Operator "+appendTo+" to append not found!");
		}
		
		//defaultExecute(operator, po, config, true, true, true);

		handleOperatorID(operator, po);
		replace(operator, po , config, true);
		retract(operator);
		insert(po);

	}

	@Override
	public boolean isExecutable(AppendToPhysicalAO operator,
			TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	@Override
	public String getName() {
		return "AppendToPhysical";
	}

	@Override
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

	@Override
	public Class<? super AppendToPhysicalAO> getConditionClass() {
		return AppendToPhysicalAO.class;
	}
}
