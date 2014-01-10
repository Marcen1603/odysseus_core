/**
 * 
 */
package de.uniol.inf.is.odysseus.relational.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AssociativeStorageAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TAssociativeStorageAORule extends AbstractTransformationRule<AssociativeStorageAO> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(AssociativeStorageAO operator, TransformationConfiguration config) {
        // TODO Auto-generated method stub
        // int dimension = operator.getDimension();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isExecutable(AssociativeStorageAO operator, TransformationConfiguration config) {
        if (operator.getInputSchema(0).getType() == Tuple.class) {
            return operator.isAllPhysicalInputSet();
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return "AssociativeStorageAO -> AssociativeStoragePO";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

}
