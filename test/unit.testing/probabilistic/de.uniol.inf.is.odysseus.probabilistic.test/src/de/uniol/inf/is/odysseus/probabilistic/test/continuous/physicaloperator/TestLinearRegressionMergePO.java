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

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.probabilistic.common.base.ProbabilisticTuple;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilisticTimeInterval;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.LinearRegressionMergePO;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class TestLinearRegressionMergePO extends LinearRegressionMergePO<IProbabilisticTimeInterval> {
    /** The logger for debug purpose. */
    @SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(TestLinearRegressionMergePO.class);

    /**
     * Test constructor of the LinearRegressionMerge PO.
     */
    public TestLinearRegressionMergePO() {
        super(TestLinearRegressionMergePO.getSchema(), new int[] {}, new int[] {}, 1, 2);
    }

//    /*
//     * 
//     * @see
//     * de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#
//     * transfer(java.lang.Object)
//     */
//    @Override
//    public final void transfer(final ProbabilisticTuple<IProbabilisticTimeInterval> object) {
//        TestLinearRegressionMergePO.LOG.debug(object.toString());
//        Assert.assertTrue(((IProbabilistic) object.getMetadata()).getExistence() <= 1.0);
//    }

    /**
     * Test the process method of the LinearRegressionMergePO.
     * 
     * 
     * FIXME enable test
     * 
     * @param tuple
     *            The prob. tuple
     */
    // @Test(dataProvider = "discreteProbabilisticTuple")
    public final void testProcess(final ProbabilisticTuple<IProbabilisticTimeInterval> tuple) {
        this.process_next(tuple, 0);

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
