/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.base;

import de.uniol.inf.is.odysseus.probabilistic.common.VarHelper;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.discrete.common.ProbabilisticDiscreteUtils;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class DiscreteDistributionPredicateEvaluator implements IPredicateEvaluator {
    VarHelper[] varHelper;
    SDFProbabilisticExpression expression;
    final int[] probabilisticAttributePos;

    /**
     * Creates a new instances of a predicate evaluator.
     */
    public DiscreteDistributionPredicateEvaluator(SDFProbabilisticExpression expression, VarHelper[] varHelper, int[] probabilisticAttributePos) {
        this.expression = expression;
        this.varHelper = varHelper;
        this.probabilisticAttributePos = probabilisticAttributePos;
    }

    @Override
	public ProbabilisticTuple<? extends IProbabilistic> evaluate(ProbabilisticTuple<? extends IProbabilistic> input) {
        final ProbabilisticTuple<? extends IProbabilistic> output = input.clone();
        // Dummy tuple to hold the different worlds during evaluation
        final ProbabilisticTuple<? extends IProbabilistic> selectObject = input.clone();

        // Input and output joint probabilities
        final double[] inSum = new double[this.probabilisticAttributePos.length];
        final double[] outSum = new double[this.probabilisticAttributePos.length];

        for (int i = 0; i < this.probabilisticAttributePos.length; i++) {
            ((AbstractProbabilisticValue<?>) output.getAttribute(this.probabilisticAttributePos[i])).getValues().clear();
            final AbstractProbabilisticValue<?> attribute = (AbstractProbabilisticValue<?>) input.getAttribute(this.probabilisticAttributePos[i]);
            for (final Double probability : attribute.getValues().values()) {
                inSum[i] += probability;
            }
        }

        final Object[][] worlds = ProbabilisticDiscreteUtils.getWorlds(input, this.probabilisticAttributePos);

        // Evaluate each world and store the possible ones in the output tuple
        for (int w = 0; w < worlds.length; w++) {
            for (int i = 0; i < this.probabilisticAttributePos.length; i++) {
                selectObject.setAttribute(this.probabilisticAttributePos[i], worlds[w][i]);
            }

            int distributionIndex = -1;
            final Object[] values = new Object[this.varHelper.length];
            for (int j = 0; j < this.varHelper.length; ++j) {
                Object attribute = input.getAttribute(this.varHelper[j].getPos());
                if (attribute.getClass() == ProbabilisticContinuousDouble.class) {
                    int index = ((ProbabilisticContinuousDouble) attribute).getDistribution();
                    attribute = input.getDistribution(index);
                    // FIXME What happens if we have more than one
                    // distribution inside an expression or even other
                    // functions? (CKu 17.12.2013)
                    if (distributionIndex >= 0) {
                        throw new IllegalArgumentException("More than one distribution not supported inside predicate");
                    }
                    distributionIndex = index;
                }
                values[j] = attribute;
            }

            expression.bindMetaAttribute(input.getMetadata());
            expression.bindDistributions(input.getDistributions());
            expression.bindAdditionalContent(input.getAdditionalContent());
            expression.bindVariables(values);

            final boolean result = expression.getValue();

            if (result) {
                for (int i = 0; i < this.probabilisticAttributePos.length; i++) {
                    final AbstractProbabilisticValue<?> inAttribute = (AbstractProbabilisticValue<?>) input.getAttribute(this.probabilisticAttributePos[i]);
                    @SuppressWarnings("unchecked")
                    final AbstractProbabilisticValue<Double> outAttribute = (AbstractProbabilisticValue<Double>) output.getAttribute(this.probabilisticAttributePos[i]);
                    final double probability = inAttribute.getValues().get(worlds[w][i]);
                    if (!outAttribute.getValues().containsKey(worlds[w][i])) {
                        outAttribute.getValues().put((Double) worlds[w][i], probability);
                        outSum[i] += probability;
                    }
                }
            }
        }
        // Update the joint probability
        double jointProbability = ((IProbabilistic) output.getMetadata()).getExistence();
        for (int i = 0; i < this.probabilisticAttributePos.length; i++) {
            jointProbability /= inSum[i];
            jointProbability *= outSum[i];
        }
        ((IProbabilistic) output.getMetadata()).setExistence(jointProbability);

        return output;
    }

}
