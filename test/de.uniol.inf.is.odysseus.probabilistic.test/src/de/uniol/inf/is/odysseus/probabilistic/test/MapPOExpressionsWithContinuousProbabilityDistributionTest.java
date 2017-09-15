package de.uniol.inf.is.odysseus.probabilistic.test;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.IMultivariateDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

@RunWith(Parameterized.class)
public class MapPOExpressionsWithContinuousProbabilityDistributionTest extends AbstractMapPOTest {

    @Parameters(name = "{index}: Expression: {0}, Input: [{1},{2}], Output: [{3}], Existence: {4}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] { //
                { "x + y",
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                                }), //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                                }), //
                        new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                new IMultivariateDistribution[] { //
                                        new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 2.0 })//
                                }), //
                        1.0 }, //
                { "x - y",
                            new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                    new IMultivariateDistribution[] { //
                                            new MultivariateNormalDistribution(new double[] { 0.0 }, new double[] { 1.0 })//
                                    }), //
                            new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                    new IMultivariateDistribution[] { //
                                            new MultivariateNormalDistribution(new double[] { 1.0 }, new double[] { 1.0 })//
                                    }), //
                            new MultivariateMixtureDistribution(new double[] { 1.0 }, //
                                    new IMultivariateDistribution[] { //
                                            new MultivariateNormalDistribution(new double[] { -1.0 }, new double[] { 2.0 })//
                                    }), //
                            1.0 }, //
        });
    }

    @Parameter(0)
    public String expressionString;
    @Parameter(1)
    public Object inputX;
    @Parameter(2)
    public Object inputY;
    @Parameter(3)
    public Object output;
    @Parameter(4)
    public double existence;

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticMapPO#process_next(de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple, int)}.
     *
     */
    @Test
    public void testProcess_next() {
        givenSchema(//
                attribute("x").as(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE), //
                attribute("y").as(SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE) //
        );
        givenExpression(this.expressionString);
        givenProbabilisticMapPO();
        givenInputTupleWithSeparateDistributions(new Object[] { this.inputX, this.inputY });

        whenProcessTuple();

        thenOutputWithMultipleDistributionsEquals(this.existence, new Object[] { this.output });
    }

}