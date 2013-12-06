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
package de.uniol.inf.is.odysseus.probabilistic.transform.continuous;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.JoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.LeftJoinAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.RestructHelper;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.probabilistic.base.common.PredicateUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.continuous.logicaloperator.LinearRegressionAO;
import de.uniol.inf.is.odysseus.probabilistic.continuous.logicaloperator.LinearRegressionMergeAO;
import de.uniol.inf.is.odysseus.probabilistic.transform.TransformationConstants;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.flow.TransformRuleFlowGroup;
import de.uniol.inf.is.odysseus.transform.rule.AbstractTransformationRule;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TProbabilisticContinuousEquiJoinAOInsertLinearRegressionRule extends AbstractTransformationRule<JoinAO> {
    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getPriority()
     */
    @Override
    public final int getPriority() {
        return TransformationConstants.PRIORITY;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#execute(java.lang.Object,
     * java.lang.Object)
     */
    @Override
    public final void execute(final JoinAO operator, final TransformationConfiguration config) {

        final SDFExpression expr = this.getExpression(operator);
        final int port = this.getProbabilisticViewPort(operator, expr);
        if (!this.hasLinearRegressionAOAsChild(operator, port)) {
            this.insertLinearRegressionAO(operator, port, expr);
        }

        if (!this.hasLinearRegressionMergeAOAsFather(operator)) {
            this.insertLinearRegressionMergeAO(operator, port, expr);
        }

    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.IRule#isExecutable(java.lang
     * .Object, java.lang.Object)
     */
    @Override
    public final boolean isExecutable(final JoinAO operator, final TransformationConfiguration transformConfig) {
        final IPredicate<?> predicate = operator.getPredicate();
        if (predicate != null) {
            if ((operator.getInputSchema(0).getType() == ProbabilisticTuple.class) || (operator.getInputSchema(1).getType() == ProbabilisticTuple.class)) {
                if (!(operator instanceof LeftJoinAO)) {

                    if (!SchemaUtils.containsContinuousProbabilisticAttributes(operator.getPredicate().getAttributes())) {
                        return false;
                    }
                    final SDFExpression expr = this.getExpression(operator);

                    if (PredicateUtils.isEquiExpression(expr.getMEPExpression())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getName()
     */
    @Override
    public final String getName() {
        return "JoinAO -> Insert linear regression for Equi-Join";
    }

    /*
     * 
     * @see de.uniol.inf.is.odysseus.ruleengine.rule.IRule#getRuleFlowGroup()
     */
    @Override
    public final IRuleFlowGroup getRuleFlowGroup() {
        return TransformRuleFlowGroup.INIT;
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.ruleengine.rule.AbstractRule#getConditionClass()
     */
    @Override
    public final Class<? super JoinAO> getConditionClass() {
        return JoinAO.class;
    }

    /**
     * 
     * @param operator
     * @return
     */
    private boolean hasLinearRegressionMergeAOAsFather(final ILogicalOperator operator) {
        boolean hasLinearRegressionMergeAOAsFather = false;
        for (final LogicalSubscription sub : operator.getSubscriptions()) {
            if (sub.getTarget() instanceof LinearRegressionMergeAO) {
                hasLinearRegressionMergeAOAsFather = true;
                break;
            }
        }
        return hasLinearRegressionMergeAOAsFather;
    }

    /**
     * 
     * @param operator
     * @param port
     * @return
     */
    private boolean hasLinearRegressionAOAsChild(final ILogicalOperator operator, final int port) {
        final LogicalSubscription child = operator.getSubscribedToSource(port);
        return (child.getTarget() instanceof LinearRegressionAO);
    }

    /**
     * 
     * @param operator
     * @param port
     * @param expr
     */
    private void insertLinearRegressionMergeAO(final ILogicalOperator operator, final int port, final SDFExpression expr) {

        final Map<SDFAttribute, List<SDFAttribute>> attributes = PredicateUtils.getEquiExpressionAtributes(expr.getMEPExpression(), expr.getAttributeResolver());

        final Set<SDFAttribute> dependentList = new HashSet<SDFAttribute>();
        final Set<SDFAttribute> explanatoryList = new HashSet<SDFAttribute>();

        for (final SDFAttribute leftAttr : attributes.keySet()) {
            for (final SDFAttribute rightAttr : attributes.get(leftAttr)) {
                if ((leftAttr.getDatatype() instanceof SDFProbabilisticDatatype) && (rightAttr.getDatatype().isNumeric())) {
                    explanatoryList.add(rightAttr);
                }
                else if ((rightAttr.getDatatype() instanceof SDFProbabilisticDatatype) && (leftAttr.getDatatype().isNumeric())) {
                    explanatoryList.add(leftAttr);
                }
            }
        }
        for (final SDFAttribute attr : operator.getInputSchema(port)) {
            if (!explanatoryList.contains(attr)) {
                dependentList.add(attr);
            }
        }
        final LinearRegressionMergeAO linearRegressionMergeAO = new LinearRegressionMergeAO();
        linearRegressionMergeAO.setDependentAttributes(new ArrayList<SDFAttribute>(dependentList));
        linearRegressionMergeAO.setExplanatoryAttributes(new ArrayList<SDFAttribute>(explanatoryList));

        linearRegressionMergeAO.setName(operator.getName() + "_linearRegressionMerge");
        RestructHelper.insertOperatorBefore(linearRegressionMergeAO, operator);
        linearRegressionMergeAO.initialize();
        this.insert(linearRegressionMergeAO);
    }

    /**
     * 
     * @param operator
     * @param port
     * @param expr
     */
    private void insertLinearRegressionAO(final ILogicalOperator operator, final int port, final SDFExpression expr) {
        final Map<SDFAttribute, List<SDFAttribute>> attributes = PredicateUtils.getEquiExpressionAtributes(expr.getMEPExpression(), expr.getAttributeResolver());

        final Set<SDFAttribute> dependentList = new HashSet<SDFAttribute>();
        final Set<SDFAttribute> explanatoryList = new HashSet<SDFAttribute>();
        for (final SDFAttribute leftAttr : attributes.keySet()) {
            for (final SDFAttribute rightAttr : attributes.get(leftAttr)) {
                if ((leftAttr.getDatatype() instanceof SDFProbabilisticDatatype) && (((SDFProbabilisticDatatype) leftAttr.getDatatype()).isContinuous())) {
                    dependentList.add(rightAttr);
                }
                if ((rightAttr.getDatatype() instanceof SDFProbabilisticDatatype) && (((SDFProbabilisticDatatype) rightAttr.getDatatype()).isContinuous())) {
                    explanatoryList.add(leftAttr);
                }
            }
        }
        for (final SDFAttribute attr : operator.getInputSchema(port)) {
            if (!explanatoryList.contains(attr)) {
                dependentList.add(attr);
            }
        }
        final LinearRegressionAO linearRegressionAO = new LinearRegressionAO();
        linearRegressionAO.setDependentAttributes(new ArrayList<SDFAttribute>(dependentList));
        linearRegressionAO.setExplanatoryAttributes(new ArrayList<SDFAttribute>(explanatoryList));

        linearRegressionAO.setName(operator.getName() + "_linearRegression");

        RestructHelper.insertOperatorBefore(linearRegressionAO, operator.getSubscribedToSource(port).getTarget());
        linearRegressionAO.initialize();
        operator.getSubscribedToSource(port).setSchema(linearRegressionAO.getOutputSchema());

        this.insert(linearRegressionAO);
        SDFSchema outputSchema = null;
        for (final LogicalSubscription l : operator.getSubscribedToSource()) {
            outputSchema = SDFSchema.union(outputSchema, l.getSchema());
        }
        operator.setOutputSchema(outputSchema);

    }

    /**
     * 
     * @param operator
     * @return
     */
    private SDFExpression getExpression(final ILogicalOperator operator) {
        final String mepString = operator.getPredicate().toString();
        final SDFSchema leftInputSchema = operator.getInputSchema(0);
        final SDFSchema rightInputSchema = operator.getInputSchema(1);

        final SDFSchema inputSchema = SDFSchema.union(leftInputSchema, rightInputSchema);
        final IAttributeResolver attrRes = new DirectAttributeResolver(inputSchema);
        final SDFExpression expr = new SDFExpression(null, mepString, attrRes, MEP.getInstance());
        return expr;
    }

    /**
     * 
     * @param operator
     * @param expr
     * @return
     */
    private int getProbabilisticViewPort(final ILogicalOperator operator, final SDFExpression expr) {
        int port = -1;
        final Map<SDFAttribute, List<SDFAttribute>> attributes = PredicateUtils.getEquiExpressionAtributes(expr.getMEPExpression(), expr.getAttributeResolver());
        for (final SDFAttribute leftAttr : attributes.keySet()) {
            for (final SDFAttribute rightAttr : attributes.get(leftAttr)) {
                if ((leftAttr.getDatatype() instanceof SDFProbabilisticDatatype) && (((SDFProbabilisticDatatype) leftAttr.getDatatype()).isContinuous())) {
                    port = (operator.getInputSchema(0).contains(rightAttr) ? 0 : 1);
                }
                else if ((rightAttr.getDatatype() instanceof SDFProbabilisticDatatype) && (((SDFProbabilisticDatatype) rightAttr.getDatatype()).isContinuous())) {
                    port = (operator.getInputSchema(0).contains(leftAttr) ? 0 : 1);
                }
            }
        }
        return port;
    }
}
