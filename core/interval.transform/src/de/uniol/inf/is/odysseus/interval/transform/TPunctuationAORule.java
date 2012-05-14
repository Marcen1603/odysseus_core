package de.uniol.inf.is.odysseus.interval.transform;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.PunctuationAO;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttributeContainer;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.intervalapproach.PunctuationPO;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

public class TPunctuationAORule extends AbstractTransformationRule<PunctuationAO> {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void execute(PunctuationAO punctuationAO, TransformationConfiguration config) {
        PunctuationPO<IMetaAttributeContainer<ITimeInterval>> punctuationPO = new PunctuationPO<IMetaAttributeContainer<ITimeInterval>>(punctuationAO.getRatio());
        punctuationPO.setOutputSchema(punctuationAO.getOutputSchema());

        Collection<ILogicalOperator> toUpdate = config.getTransformationHelper().replace(
                punctuationAO, punctuationPO);
        for (ILogicalOperator o : toUpdate) {
            update(o);
        }

        insert(punctuationPO);
        retract(punctuationAO);
    }

    @Override
    public boolean isExecutable(PunctuationAO operator, TransformationConfiguration config) {
        if (config.getMetaTypes().contains(ITimeInterval.class.getCanonicalName())) {
            if (operator.isAllPhysicalInputSet()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "PunctuationAO -> PunctuationPO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    @Override
    public Class<?> getConditionClass() {
    	return PunctuationAO.class;
    }
    
}
