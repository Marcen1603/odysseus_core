/**
 * Copyright 2013 The Odysseus Team
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.probabilistic.transform.discrete;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.probabilistic.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.discrete.physicaloperator.ProbabilisticDiscreteMapPO;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;
import de.uniol.inf.is.odysseus.probabilistic.transform.TransformationConstants;
import de.uniol.inf.is.odysseus.relational.transform.TMapAORule;

/**
 * Transformation rule for probabilistic Map operator.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class TProbabilisiticDiscreteMapAORule extends TMapAORule {

    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
     */
    @Override
    public final int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public final void execute(final MapAO mapAO, final TransformationConfiguration transformConfig) {
        IPhysicalOperator mapPO;

        final SDFProbabilisticExpression[] expressions = new SDFProbabilisticExpression[mapAO.getExpressions().size()];
        for (int i = 0; i < expressions.length; i++) {
            expressions[i] = new SDFProbabilisticExpression(mapAO.getExpressions().get(i));
        }
        mapPO = new ProbabilisticDiscreteMapPO<IMetaAttribute>(mapAO.getInputSchema(), expressions, false, false);

        this.defaultExecute(mapAO, mapPO, transformConfig, true, true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
     * .Object, java.lang.Object)
     */
    @Override
    public final boolean isExecutable(final MapAO operator, final TransformationConfiguration transformConfig) {
        if (operator.isAllPhysicalInputSet()) {
            if (operator.getInputSchema().getType() == ProbabilisticTuple.class) {

                boolean isProbabilisticDiscrete = false;
                for (final SDFExpression expr : operator.getExpressions()) {
                    if (SchemaUtils.containsDiscreteProbabilisticAttributes(expr.getAllAttributes())) {
                        isProbabilisticDiscrete = true;
                    }
                    if ((expr.getType() instanceof SDFProbabilisticDatatype) && (((SDFProbabilisticDatatype) expr.getType()).isDiscrete())) {
                        isProbabilisticDiscrete = true;
                    }
                }
                if (isProbabilisticDiscrete) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * (non-Javadoc)
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
     */
    @Override
    public final String getName() {
        return "MapAO -> ProbabilisticDiscreteMapPO";
    }

}
