package de.uniol.inf.is.odysseus.transform.rules;

import de.uniol.inf.is.odysseus.core.collection.Resource;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AppendToPhysicalAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
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
			TransformationConfiguration config) throws RuleException {
		
		String appendTo = operator.getAppendTo();
		// To avoid potential security risk! We can add our source 
		// only to own operators!
		IPhysicalOperator po = getDataDictionary().getOperator(new Resource(getCaller().getUser(), appendTo), getCaller());
		if (po == null){
			po = getDataDictionary().getOperator(new Resource(getCaller().getUser(),appendTo), getCaller());
		}
		
		if (po == null){
			throw new TransformationException("Operator "+appendTo+" to append not found!");
		}
		
		//defaultExecute(operator, po, config, true, true, true);
		handleOperatorID(operator, po, config);		
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
