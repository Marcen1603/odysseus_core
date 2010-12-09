package de.uniol.inf.is.odysseus.broker.evaluation.rules;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.broker.evaluation.logicaloperator.UpdateEvaluationAO;
import de.uniol.inf.is.odysseus.broker.evaluation.physicaloperator.UpdateEvaluationPO;
import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TUpdateEvaluationAORule extends AbstractTransformationRule<UpdateEvaluationAO> {

	@Override
	public int getPriority() {		
		return 5;
	}

	@Override
	public void execute(UpdateEvaluationAO operator, TransformationConfiguration config) {			
		if(!exists(operator)){
			UpdateEvaluationPO<?> updatePO = new UpdateEvaluationPO<IMetaAttributeContainer<ITimeInterval>>(operator.getNumber());
			updatePO.setOutputSchema(operator.getOutputSchema());			
			Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(operator,updatePO);							
			for (ILogicalOperator o:toUpdate){			
				update(o);			
			}		
			insert(updatePO);
		}
		retract(operator);			
	}

	@Override
	public boolean isExecutable(UpdateEvaluationAO operator, TransformationConfiguration config) {
		return operator.isAllPhysicalInputSet();
	}

	private boolean exists(UpdateEvaluationAO operator){		
		List<UpdateEvaluationPO> liste = super.getAllOfSameTyp(new UpdateEvaluationPO(0));
		for(UpdateEvaluationPO<?> po : liste){			
			if(po.getNumber() == operator.getNumber()){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String getName() {		
		return "UpdateEvaluationAO -> UpdateEvaluationPO";
	}

	
	public IRuleFlowGroup getRuleFlowGroup() {
		return TransformRuleFlowGroup.TRANSFORMATION;
	}

}
