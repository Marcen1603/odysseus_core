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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.physicaloperator.relational.RelationalProjectPO;
import de.uniol.inf.is.odysseus.probabilistic.common.Interval;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateMixtureDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.base.distribution.MultivariateNormalDistribution;
import de.uniol.inf.is.odysseus.probabilistic.common.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.metadata.Probabilistic;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TestProjectPO extends RelationalProjectPO<IMetaAttribute> {
    /** The logger for debug purpose. */
    private static final Logger LOG = LoggerFactory.getLogger(TestProjectPO.class);

    /**
     * Test constructor of the Project PO.
     */
    public TestProjectPO() {
        super(new int[] { 1 });
    }

//    /*
//     * 
//     * @see
//     * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#
//     * transfer(java.lang.Object)
//     */
//    @Override
//    public final void transfer(final Tuple<IMetaAttribute> object) {
//        TestProjectPO.LOG.debug(object.toString());
//    }

    /**
     * Test the process method of the ProjectPO.
     * 
     * @param tuple
     *            A prob. tuple
     */
    @Test(dataProvider = "tuple")
    public final void testprocess(final ProbabilisticTuple<IMetaAttribute> tuple) {
        TestProjectPO.LOG.debug("In: " + tuple);
        this.process_next(tuple, 0);
    }

    /**
     * 
     * @return An array of different prob. tuple
     */
    @DataProvider(name = "tuple")
    public final Object[][] provideTuple() {
        return new Object[][] { { this.provideSimpleTuple() }, { this.provideUnivariateTuple() }, { this.provideMultivariateTuple1() }, { this.provideMultivariateTuple2() },
                { this.provideMultivariateTuple3() }, { this.provideMultivariateTuple4() } };
    }

    /**
     * 
     * @return A probabilistic tuple with a uniivariate prob. distribution
     */
    private IStreamObject<?> provideSimpleTuple() {
        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[] { 2.0 }, new double[][] { { 1.5 } });
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(1.0, distribution);
        final Object[] attrs = new Object[] { "FirstAttribute", new ProbabilisticDouble(0) };
        mixture.setAttributes(new int[] { 1 });
        mixture.setScale(1.0);
        mixture.setSupport(new Interval[] { new Interval(-3.0, 6.0) });
        final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(attrs, new MultivariateMixtureDistribution[] { mixture }, true);
        tuple.setMetadata(new Probabilistic());
        return tuple;
    }

    /**
     * 
     * @return A probabilistic tuple with a univariate prob. distribution
     */
    private IStreamObject<?> provideUnivariateTuple() {
        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[] { 2.0 }, new double[][] { { 1.5 } });
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(1.0, distribution);
        final Object[] attrs = new Object[] { "FirstAttribute", new ProbabilisticDouble(0), "ThirdAttribute" };
        mixture.setAttributes(new int[] { 1 });
        mixture.setScale(1.0);
        mixture.setSupport(new Interval[] { new Interval(-3.0, 6.0) });
        final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(attrs, new MultivariateMixtureDistribution[] { mixture }, true);
        tuple.setMetadata(new Probabilistic());
        return tuple;
    }

    /**
     * 
     * @return A probabilistic tuple with a multivariate prob. distribution
     */
    private IStreamObject<?> provideMultivariateTuple1() {
        final MultivariateNormalDistribution distribution = new MultivariateNormalDistribution(new double[] { 2.0, 3.0 }, new double[] { 2.0, 2.0, 2.0 });
        final MultivariateMixtureDistribution mixture = new MultivariateMixtureDistribution(1.0, distribution);
        final Object[] attrs = new Object[] { new ProbabilisticDouble(0), "FirstAttribute", "ThirdAttribute", new ProbabilisticDouble(0) };
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
        final Object[] attrs = new Object[] { "FirstAttribute", new ProbabilisticDouble(0), "ThirdAttribute", new ProbabilisticDouble(0) };
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
        final Object[] attrs = new Object[] { "FirstAttribute", new ProbabilisticDouble(0), "ThirdAttribute", new ProbabilisticDouble(0) };
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
        final MultivariateNormalDistribution distribution1 = new MultivariateNormalDistribution(new double[] { 2.0 }, new double[][] { { 1.5 } });
        final MultivariateMixtureDistribution mixture1 = new MultivariateMixtureDistribution(0.5, distribution1);

        mixture1.setAttributes(new int[] { 3 });
        mixture1.setScale(1.0);
        mixture1.setSupport(new Interval[] { new Interval(-3.0, 6.0) });

        final MultivariateNormalDistribution distribution2 = new MultivariateNormalDistribution(new double[] { 3.0 }, new double[][] { { 2.5 } });
        final MultivariateMixtureDistribution mixture2 = new MultivariateMixtureDistribution(0.5, distribution2);

        mixture2.setAttributes(new int[] { 1 });
        mixture2.setScale(1.0);
        mixture2.setSupport(new Interval[] { new Interval(-7.0, 14.0) });

        final Object[] attrs = new Object[] { "FirstAttribute", new ProbabilisticDouble(1), "ThirdAttribute", new ProbabilisticDouble(0) };

        final ProbabilisticTuple<IMetaAttribute> tuple = new ProbabilisticTuple<>(attrs, new MultivariateMixtureDistribution[] { mixture1, mixture2 }, true);
        tuple.setMetadata(new Probabilistic());
        return tuple;
    }

}
