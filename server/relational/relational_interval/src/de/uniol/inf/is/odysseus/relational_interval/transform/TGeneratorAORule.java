/**
 * 
 */
package de.uniol.inf.is.odysseus.relational_interval.transform;

import java.util.List;
import java.util.concurrent.TimeUnit;

import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFConstraint;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.GeneratorAO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.IGroupProcessor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalGroupProcessor;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalNoGroupProcessor;
import de.uniol.inf.is.odysseus.relational_interval.physicaloperator.GeneratorPO;
import de.uniol.inf.is.odysseus.ruleengine.rule.RuleException;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TGeneratorAORule extends AbstractRelationalIntervalTransformationRule<GeneratorAO> {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void execute(final GeneratorAO ao, final TransformationConfiguration transformConfig) throws RuleException {
        final List<SDFAttribute> gAttr = ao.getGroupingAttributes();
        @SuppressWarnings("rawtypes")
        IGroupProcessor gp = null;
        if (gAttr != null) {
            gp = new RelationalGroupProcessor<>(ao.getInputSchema(), ao.getOutputSchema(), gAttr, null, false);
        }
        else {
            gp = RelationalNoGroupProcessor.getInstance();
        }
        @SuppressWarnings("unchecked")
        final GeneratorPO<?> po = new GeneratorPO<ITimeInterval>(ao.getInputSchema(), ao.getExpressions().toArray(new SDFExpression[0]), ao.isAllowNullInOutput(), gp, ao.getPredicate(),
                ao.getFrequency(), ao.isMulti());
        final SDFConstraint c = ao.getInputSchema().getConstraint(SDFConstraint.BASE_TIME_UNIT);
        if (c != null) {
            po.setBasetimeUnit((TimeUnit) c.getValue());
        }
        this.defaultExecute(ao, po, transformConfig, true, true);
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
