/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.base;

import org.apache.commons.math3.util.Pair;

import de.uniol.inf.is.odysseus.probabilistic.common.VarHelper;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.ProbabilisticContinuousDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ContinuousDistributionPredicateEvaluator implements IPredicateEvaluator {
    VarHelper[] varHelper;
    SDFProbabilisticExpression expression;

    /**
     * Creates a new instances of a predicate evaluator.
     */
    public ContinuousDistributionPredicateEvaluator(SDFProbabilisticExpression expression, VarHelper[] varHelper) {
        this.expression = expression;
        this.varHelper = varHelper;
    }

    @Override
    public ProbabilisticTuple<? extends IProbabilistic> evaluate(ProbabilisticTuple<? extends IProbabilistic> input) {
        final ProbabilisticTuple<? extends IProbabilistic> output = input.clone();
        double jointProbability = ((IProbabilistic) input.getMetadata()).getExistence();
        synchronized (this.expression) {
            final Object[] values = new Object[this.varHelper.length];
            for (int j = 0; j < this.varHelper.length; ++j) {
                Object attribute = input.getAttribute(this.varHelper[j].getPos());
                if (attribute.getClass() == ProbabilisticContinuousDouble.class) {
                    int index = ((ProbabilisticContinuousDouble) attribute).getDistribution();
                    attribute = input.getDistribution(index);
                }
                values[j] = attribute;
            }

            expression.bindMetaAttribute(input.getMetadata());
            expression.bindDistributions(input.getDistributions());
            expression.bindAdditionalContent(input.getAdditionalContent());
            expression.bindVariables(values);

            final Object expr = expression.getValue();
            if (expression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE)) {
                final MultivariateMixtureDistribution newDistribution = (MultivariateMixtureDistribution) expr;
                int index = ((ProbabilisticContinuousDouble) output.getAttribute(newDistribution.getAttribute(0))).getDistribution();
                final MultivariateMixtureDistribution oldDistribution = output.getDistribution(index);

                for (int i = 0; i < newDistribution.getSupport().length; i++) {
                    double newMu = 0.0;
                    double oldMu = 0.0;
                    for (Pair<Double, IMultivariateDistribution> component : ((MultivariateMixtureDistribution) newDistribution).getComponents()) {
                        newMu += component.getKey() * component.getValue().getMean()[i];
                    }
                    for (Pair<Double, IMultivariateDistribution> component : ((MultivariateMixtureDistribution) oldDistribution).getComponents()) {
                        oldMu += component.getKey() * component.getValue().getMean()[i];
                    }
                    oldDistribution.setSupport(i, newDistribution.getSupport()[i].subtract(newMu - oldMu));

                }
                jointProbability *= oldDistribution.getScale() / newDistribution.getScale();
                oldDistribution.setScale(newDistribution.getScale());
                output.setDistribution(index, oldDistribution);
                ((IProbabilistic) output.getMetadata()).setExistence(jointProbability);
            }
            else {
                if (!(Boolean) expr) {
                    jointProbability = 0.0;
                    ((IProbabilistic) output.getMetadata()).setExistence(jointProbability);
                }
            }
        }
        return output;
    }

}
