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
package de.uniol.inf.is.odysseus.probabilistic.test.continuous.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.probabilistic.ProbabilisticFunctionProvider;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.Probabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TestSelectPO extends ProbabilisticSelectPO<IProbabilistic> {
    /** The logger for debug purpose. */
    @SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(TestSelectPO.class);

    /**
     * Test constructor of the Select PO.
     */
    public TestSelectPO() {
        // super(TestSelectPO.getTestPredicate(), TestSelectPO
        // .getProbabilisticAttributePos());
        super(TestSelectPO.getSchema(), TestSelectPO.getTestPredicate());
    }

//    /*
//     * 
//     * @see
//     * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#
//     * transfer(java.lang.Object)
//     */
//    @Override
//    public final void transfer(final ProbabilisticTuple<IProbabilistic> object) {
//        TestSelectPO.LOG.debug(object.toString());
//        Assert.assertTrue(object.getMetadata().getExistence() <= 1.0);
//    }

    /**
     * Test the process method of the SelectPO.
     * 
     * @param tuple
     *            The prob. tuple
     */
    @Test(dataProvider = "continuousProbabilisticTuple")
    public final void testProcess(final ProbabilisticTuple<IProbabilistic> tuple) {
        tuple.setMetadata(new Probabilistic());
        this.process_next(tuple, 0);

    }

    /**
     * 
     * @return An array of different prob. tuple
     */
    @DataProvider(name = "continuousProbabilisticTuple")
    public final Object[][] provideDiscreteProbabilisticTuple() {
        return new Object[][] { { this.provideMultivariateTuple1() }, { this.provideMultivariateTuple2() }, { this.provideMultivariateTuple3() }, { this.provideMultivariateTuple4() } };
    }

    /**
     * 
     * @return A probabilistic tuple with a multivariate prob. distribution
     */
    private IStreamObject<?> provideMultivariateTuple1() {
        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[] { 2.0, 3.0 }, new double[] { 2.0, 2.0, 2.0 });
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(1.0, distribution);
        final Object[] attrs = new Object[] { new ProbabilisticDouble(0), new ProbabilisticDouble(0) };
        mixture.setAttributes(new int[] { 1, 3 });
        mixture.setScale(1.0);
        mixture.setSupport(new Interval[] { new Interval(-3.0, 6.0), new Interval(-7.0, 14.0) });
        final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(attrs, new MultivariateMixtureDistribution[] { mixture }, true);
        tuple.setMetadata(new Probabilistic());
        return tuple;
    }

    /**
     * 
     * @return A probabilistic tuple with a multivariate prob. distribution
     */
    private IStreamObject<?> provideMultivariateTuple2() {
        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[] { 2.0, 3.0 }, new double[] { 2.0, 2.0, 2.0 });
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(1.0, distribution);
        final Object[] attrs = new Object[] { new ProbabilisticDouble(0), new ProbabilisticDouble(0) };
        mixture.setAttributes(new int[] { 1, 3 });
        mixture.setScale(1.0);
        mixture.setSupport(new Interval[] { new Interval(-3.0, 6.0), new Interval(-7.0, 14.0) });
        final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(attrs, new MultivariateMixtureDistribution[] { mixture }, true);
        tuple.setMetadata(new Probabilistic());
        return tuple;
    }

    /**
     * 
     * @return A probabilistic tuple with a multivariate prob. distribution
     */
    private IStreamObject<?> provideMultivariateTuple3() {
        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[] { 2.0, 3.0 }, new double[] { 2.0, 2.0, 2.0 });
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(1.0, distribution);
        final Object[] attrs = new Object[] { new ProbabilisticDouble(0), new ProbabilisticDouble(0) };
        mixture.setAttributes(new int[] { 3, 1 });
        mixture.setScale(1.0);
        mixture.setSupport(new Interval[] { new Interval(-3.0, 6.0), new Interval(-7.0, 14.0) });
        final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(attrs, new MultivariateMixtureDistribution[] { mixture }, true);
        tuple.setMetadata(new Probabilistic());
        return tuple;
    }

    /**
     * 
     * @return A probabilistic tuple with a multivariate prob. distribution
     */
    private IStreamObject<?> provideMultivariateTuple4() {
        final MultivariateNormalDistribution distribution1 = new MultivariateNormalDistribution(new double[] { 2.0 }, new double[] { 1.5 });
        final MultivariateMixtureDistribution mixture1 = new MultivariateMixtureDistribution(1.0, distribution1);

        mixture1.setAttributes(new int[] { 3 });
        mixture1.setScale(1.0);
        mixture1.setSupport(new Interval[] { new Interval(-3.0, 6.0) });

        final MultivariateNormalDistribution distribution2 = new MultivariateNormalDistribution(new double[] { 3.0 }, new double[] { 2.5 });
        final MultivariateMixtureDistribution mixture2 = new MultivariateMixtureDistribution(1.0, distribution2);

        mixture2.setAttributes(new int[] { 1 });
        mixture2.setScale(1.0);
        mixture2.setSupport(new Interval[] { new Interval(-7.0, 14.0) });

        final Object[] attrs = new Object[] { new ProbabilisticDouble(1), new ProbabilisticDouble(0) };

        final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(attrs, new MultivariateMixtureDistribution[] { mixture1, mixture2 }, true);
        tuple.setMetadata(new Probabilistic());
        return tuple;
    }

    /**
     * 
     * @return The predicate used for testing
     */
    private static RelationalPredicate getTestPredicate() {
        MEP.getInstance().addFunctionProvider(new ProbabilisticFunctionProvider());
        final SDFSchema schema = TestSelectPO.getSchema();
        final DirectAttributeResolver resolver = new DirectAttributeResolver(TestSelectPO.getSchema());
        final SDFExpression expression = new SDFExpression("", "b < 3.0 && b > 4.0", resolver, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
        final RelationalPredicate predicate = new RelationalPredicate(expression);
        predicate.init(schema, null, false);
        return predicate;
    }

    /**
     * 
     * @return The schema of the input stream for testing
     */
    private static SDFSchema getSchema() {
        final Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
        attr.add(new SDFAttribute("", "a", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, null, null, null));
        attr.add(new SDFAttribute("", "b", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, null, null, null));
        final SDFSchema schema = new SDFSchema("", ProbabilisticTuple.class, attr);
        return schema;
    }
}
