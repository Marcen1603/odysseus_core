/**
 * 
 */
package de.uniol.inf.is.odysseus.relational.transform;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.GeneratorAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.GeneratorPO;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalNoGroupProcessor;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TGeneratorAORule extends AbstractTransformationRule<GeneratorAO> {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void execute(GeneratorAO ao, TransformationConfiguration transformConfig) throws RuleException {
        List<SDFAttribute> gAttr = ao.getGroupingAttributes();
        @SuppressWarnings("rawtypes")
        IGroupProcessor gp = null;
        if (gAttr != null) {
            gp = new RelationalGroupProcessor<>(ao.getInputSchema(), ao.getOutputSchema(), gAttr, null, false);
        }
        else {
            gp = RelationalNoGroupProcessor.getInstance();
        }
        @SuppressWarnings("unchecked")
        GeneratorPO<?> po = new GeneratorPO<ITimeInterval>(ao.getInputSchema(), ao.getExpressions().toArray(new SDFExpression[0]), ao.isAllowNullInOutput(), gp, ao.getPredicate(), ao.getFrequency());
        SDFConstraint c = ao.getInputSchema().getConstraint(SDFConstraint.BASE_TIME_UNIT);
        if (c != null) {
            po.setBasetimeUnit((TimeUnit) c.getValue());
        }
        defaultExecute(ao, po, transformConfig, true, true);
    }

    @Override
    public boolean isExecutable(GeneratorAO ao, TransformationConfiguration transformConfig) {
        if (ao.getInputSchema().getType() == Tuple.class) {
            if (ao.getPhysSubscriptionTo() != null) {
                return true;
            }
        }
        return false;

    }

    @Override
    public String getName() {
        return "GeneratorAO -> GeneratorPO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    @Override
    public Class<? super GeneratorAO> getConditionClass() {
        return GeneratorAO.class;
    }

}
