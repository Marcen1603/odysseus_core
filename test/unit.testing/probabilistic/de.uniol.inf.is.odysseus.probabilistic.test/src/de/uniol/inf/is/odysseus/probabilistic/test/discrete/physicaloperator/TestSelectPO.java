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
package de.uniol.inf.is.odysseus.probabilistic.test.discrete.physicaloperator;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.aggregate.AggregateFunctionBuilderRegistry;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.probabilistic.ProbabilisticFunctionProvider;
import de.uniol.inf.is.odysseus.probabilistic.common.SchemaUtils;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.metadata.Probabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticDiscreteSelectPO;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
// @Test(singleThreaded = true)
public class TestSelectPO extends ProbabilisticDiscreteSelectPO<ProbabilisticTuple<IProbabilistic>> {
    /** The logger for debug purpose. */
    private static final Logger LOG = LoggerFactory.getLogger(TestSelectPO.class);

    /**
     * Test constructor of the Select PO.
     */
    public TestSelectPO() {
        super(TestSelectPO.getTestPredicate(), TestSelectPO.getProbabilisticAttributePos());
    }

    /*
     * 
     * @see
     * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#
     * transfer(java.lang.Object)
     */
    @Override
    public final void transfer(final ProbabilisticTuple<IProbabilistic> object) {
        TestSelectPO.LOG.debug(object.toString());
        Assert.assertTrue(object.getMetadata().getExistence() <= 1.0);
    }

    /**
     * Test the process method of the SelectPO.
     * 
     * @param tuple
     *            The prob. tuple
     */
    @Test(dataProvider = "discreteProbabilisticTuple")
    public final void testProcess(final ProbabilisticTuple<IProbabilistic> tuple) {
        tuple.setMetadata(new Probabilistic());
        this.process_next(tuple, 0);

    }

    /**
     * Data provider for prob. tuples.
     * 
     * @return An array of prob. tuples.
     */
    @DataProvider(name = "discreteProbabilisticTuple")
    public final Object[][] provideDiscreteProbabilisticTuple() {
        return new Object[][] {
        // { new ProbabilisticTuple<IProbabilistic>(new Object[] { new
        // ProbabilisticDouble(new Double[] { 1.0, 2.0, 3.0 }, new Double[] {
        // 0.25, 0.25, 0.5 }),
        // new ProbabilisticDouble(new Double[] { 4.0, 5.0, 6.0 }, new Double[]
        // { 0.25, 0.25, 0.5 }), new ProbabilisticDouble(new Double[] { 0.0 },
        // new Double[] { 1.0 }),
        // new ProbabilisticDouble(new Double[] { 10.0, 11.0, 12.0 }, new
        // Double[] { 0.25, 0.25, 0.5 }) }, true)
        //
        // },
        // { new ProbabilisticTuple<IProbabilistic>(new Object[] { new
        // ProbabilisticDouble(new Double[] { 1.0, 2.0, 3.0 }, new Double[] {
        // 0.25, 0.25, 0.5 }),
        // new ProbabilisticDouble(new Double[] { 4.0, 5.0, 6.0 }, new Double[]
        // { 0.25, 0.25, 0.5 }),
        // new ProbabilisticDouble(new Double[] { 7.0, 8.0, 9.0 }, new Double[]
        // { 0.25, 0.25, 0.5 }),
        // new ProbabilisticDouble(new Double[] { 10.0, 11.0, 12.0 }, new
        // Double[] { 0.25, 0.25, 0.5 }) }, true)
        //
        // },
        // { new ProbabilisticTuple<IProbabilistic>(new Object[] { new
        // ProbabilisticDouble(new Double[] { 1.0, 2.0, 3.0, 5.0 }, new Double[]
        // { 0.25, 0.25, 0.25, 0.25 }),
        // new ProbabilisticDouble(new Double[] { 4.0, 5.0, 6.0 }, new Double[]
        // { 0.25, 0.25, 0.5 }),
        // new ProbabilisticDouble(new Double[] { 7.0, 8.0, 9.0 }, new Double[]
        // { 0.25, 0.25, 0.5 }),
        // new ProbabilisticDouble(new Double[] { 10.0, 11.0, 12.0 }, new
        // Double[] { 0.25, 0.25, 0.5 }) }, true)
        //
        // }
        };
    }

    /**
     * 
     * @return The predicate used for testing
     */
    private static RelationalPredicate getTestPredicate() {
        MEP.getInstance().addFunctionProvider(new ProbabilisticFunctionProvider());
        final SDFSchema schema = TestSelectPO.getSchema();
        final DirectAttributeResolver resolver = new DirectAttributeResolver(TestSelectPO.getSchema());
        final SDFExpression expression = new SDFExpression("", "a < 3.0 && b > 4.0 && c < 9.0", resolver, MEP.getInstance(), AggregateFunctionBuilderRegistry.getAggregatePattern());
        final RelationalPredicate predicate = new RelationalPredicate(expression);
        predicate.init(schema, null, false);
        return predicate;
    }

    /**
     * Gets the positions of the prob. attributes in the schema.
     * 
     * @return The prob. attribute positions
     */
    private static int[] getProbabilisticAttributePos() {
        return SchemaUtils.getAttributePos(TestSelectPO.getSchema(), SchemaUtils.getProbabilisticAttributes(TestSelectPO.getTestPredicate().getAttributes()));
    }

    /**
     * 
     * @return The schema of the input stream for testing
     */
    private static SDFSchema getSchema() {
        final Collection<SDFAttribute> attr = new ArrayList<SDFAttribute>();
        attr.add(new SDFAttribute("", "a", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, null, null, null));
        attr.add(new SDFAttribute("", "b", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, null, null, null));
        attr.add(new SDFAttribute("", "c", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, null, null, null));
        attr.add(new SDFAttribute("", "d", SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, null, null, null));
        final SDFSchema schema = new SDFSchema("", ProbabilisticTuple.class, attr);
        return schema;
    }
}
