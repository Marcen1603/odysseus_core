package de.uniol.inf.is.odysseus.probabilistic.transform;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.ProjectAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticProjectPO;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class TProjectAORule extends AbstractTransformationRule<ProjectAO> {
    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public void execute(final ProjectAO projectAO, final TransformationConfiguration transformConfig) {
        IPhysicalOperator projectPO;
        if (this.isContinuous(projectAO)) {
            projectPO = new ProbabilisticProjectPO<IMetaAttribute>(null, projectAO.determineRestrictList());
        }
        else {
            projectPO = new RelationalProjectPO<IMetaAttribute>(projectAO.determineRestrictList());
        }
        this.defaultExecute(projectAO, projectPO, transformConfig, true, true);
    }

    @Override
    public boolean isExecutable(final ProjectAO operator, final TransformationConfiguration transformConfig) {
        if (transformConfig.getDataTypes().contains(TransformUtil.DATATYPE)) {
            if (operator.isAllPhysicalInputSet()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getName() {
        return "ProjectAO -> ProbabilisticProjectPO";
    }

    @Override
    public IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.TRANSFORMATION;
    }

    @Override
    public Class<? super ProjectAO> getConditionClass() {
        return ProjectAO.class;
    }

    private boolean isContinuous(final ProjectAO projectAO) {
        final SDFSchema schema = projectAO.getInputSchema();

        final int[] restrictList = projectAO.determineRestrictList();
        boolean isContinuous = false;
        for (final int index : restrictList) {
            final SDFAttribute attribute = schema.getAttribute(index);
            if (attribute.getDatatype() instanceof SDFProbabilisticDatatype) {
                if (((SDFProbabilisticDatatype) attribute.getDatatype()).isContinuous()) {
                    isContinuous = true;
                }
            }
        }
        return isContinuous;
    }
}
