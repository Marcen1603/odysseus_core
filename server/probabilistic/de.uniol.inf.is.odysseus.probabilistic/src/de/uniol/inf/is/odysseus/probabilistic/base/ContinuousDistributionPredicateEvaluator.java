/**
 * 
 */
package de.uniol.inf.is.odysseus.probabilistic.base;

import de.uniol.inf.is.odysseus.probabilistic.common.VarHelper;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.continuous.datatype.NormalDistributionMixture;
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

    public ProbabilisticTuple<? extends IProbabilistic> evaluate(ProbabilisticTuple<? extends IProbabilistic> input) {
        final ProbabilisticTuple<? extends IProbabilistic> output = input.clone();
        double jointProbability = ((IProbabilistic) input.getMetadata()).getExistence();
        synchronized (this.expression) {
            double scale = 1.0;
            int distributionIndex = -1;
            final Object[] values = new Object[this.varHelper.length];
            for (int j = 0; j < this.varHelper.length; ++j) {
                Object attribute = input.getAttribute(this.varHelper[j].getPos());
                if (attribute.getClass() == ProbabilisticContinuousDouble.class) {
                    int index = ((ProbabilisticContinuousDouble) attribute).getDistribution();
                    attribute = input.getDistribution(index);
                    scale = ((NormalDistributionMixture) attribute).getScale();
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

            final Object expr = expression.getValue();
            if (expression.getType().equals(SDFProbabilisticDatatype.PROBABILISTIC_CONTINUOUS_DOUBLE)) {
                final NormalDistributionMixture distribution = (NormalDistributionMixture) expr;
                jointProbability *= scale / distribution.getScale();
                output.setDistribution(distributionIndex, distribution);
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
