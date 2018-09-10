/**
 * 
 */
package de.uniol.inf.is.odysseus.relational.transform;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.physicaloperator.storage.AssociativeStorageManager;
import de.uniol.inf.is.odysseus.core.physicaloperator.storage.IAssociativeStorage;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AssociativeStorageAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AssociativeStoragePO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.storage.AssociativeStorage2D;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.storage.AssociativeStorage3D;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationException;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
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
    public void execute(AssociativeStorageAO operator, TransformationConfiguration config) throws RuleException {
        int dimension = operator.getDimension();

        IAssociativeStorage<Tuple<?>> store = null;
        if (dimension == 2) {
            store = new AssociativeStorage2D(operator.getSizes());
        }
        else if (dimension == 3) {
            store = new AssociativeStorage3D(operator.getSizes());
        }
        else {
            throw new TransformationException("Dimension not supported");
        }
        AssociativeStorageManager.create(operator.getStorageName(), store);
        AssociativeStoragePO<Tuple<?>> storage = new AssociativeStoragePO<Tuple<?>>(store, operator.getHierachyPositions(), operator.getIndexPositions(), operator.getValuePosition());

        defaultExecute(operator, storage, config, true, true);
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
