package de.uniol.inf.is.odysseus.probabilistic.transform;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalMapPO;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticMapPO;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TMapAORule extends AbstractTransformationRule<MapAO> {

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void execute(final MapAO mapAO, final TransformationConfiguration transformConfig) {

        IPhysicalOperator mapPO;
        if (this.isProbabilistic(mapAO)) {
            mapPO = new ProbabilisticMapPO<IMetaAttribute>(mapAO.getInputSchema(), mapAO.getExpressions().toArray(
                    new SDFExpression[0]));
        }
        else {
            mapPO = new RelationalMapPO<IMetaAttribute>(mapAO.getInputSchema(), mapAO.getExpressions().toArray(
                    new SDFExpression[0]));
        }
        this.defaultExecute(mapAO, mapPO, transformConfig, true, true);
    }

    @Override
    public boolean isExecutable(final MapAO operator, final TransformationConfiguration transformConfig) {
        if (transformConfig.getDataType().equals(TransformUtil.DATATYPE)) {
            if (operator.getPhysSubscriptionTo() != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "MapAO -> ProbabilisticMapPO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    @Override
    public Class<? super MapAO> getConditionClass() {
        return MapAO.class;
    }

    private boolean isProbabilistic(final MapAO mapAO) {
        boolean isProbabilistic = false;
        for (final SDFExpression expression : mapAO.getExpressions()) {
            final List<SDFAttribute> attributes = expression.getAllAttributes();

            for (final SDFAttribute attribute : attributes) {
                if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
                    isProbabilistic = true;
                }
            }
        }
        return isProbabilistic;

    }
}
