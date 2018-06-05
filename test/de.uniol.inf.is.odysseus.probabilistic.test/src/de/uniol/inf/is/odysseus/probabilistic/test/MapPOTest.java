/**
 * Copyright 2017 The Odysseus Team
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
package de.uniol.inf.is.odysseus.probabilistic.test;

import java.util.Date;
import java.util.Optional;

import org.junit.Test;

import de.uniol.inf.is.odysseus.core.sdf.schema.DirectAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.IAttributeResolver;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticMapPO;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticExpression;

/**
 * Basic tests for {@link ProbabilisticSelectPO} operator.
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class MapPOTest extends AbstractMapPOTest {
    protected Optional<SDFProbabilisticExpression[]> otherExpressions = Optional.empty();

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticMapPO#process_open()}.
     */
    @Test
    public void testProcessOpen() {
        givenSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
        );

        givenExpression("x + y");
        givenProbabilisticMapPO();

        whenProcessOpen();
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticMapPO#process_isSemanticallyEqual(de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)}.
     */
    @Test
    public void testProcessIsSemanticallyEqual() {
        givenSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
        );
        givenOtherSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
        );
        givenExpression("x + y");
        givenOtherExpression("x + y");
        givenProbabilisticMapPO();
        givenOtherProbabilisticMapPO();

        whenProcess_isSemanticallyEqual();

        thenProcess_isSemanticallyEqualReturns(true);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticMapPO#process_isSemanticallyEqual(de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)}.
     */
    @Test
    public void testProcessIsSemanticallyEqualWithInvertedExpression() {
        givenSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
        );
        givenOtherSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
        );
        givenExpression("x + y");
        givenOtherExpression("x - y");
        givenProbabilisticMapPO();
        givenOtherProbabilisticMapPO();

        whenProcess_isSemanticallyEqual();

        thenProcess_isSemanticallyEqualReturns(false);
    }

    @Test
    public void testIsSemanticallyEqual() {
        givenSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
        );
        givenOtherSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
        );
        givenExpression("x + y");
        givenOtherExpression("x + y");
        givenProbabilisticMapPO();
        givenOtherProbabilisticMapPO();

        thenOperatorsAreSemanticallyEqual();
    }

    @Test
    public void testIsNotSemanticallyEqual() {
        givenSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
        );
        givenOtherSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
        );
        givenExpression("x + y");
        givenOtherExpression("x - y");
        givenProbabilisticMapPO();
        givenOtherProbabilisticMapPO();

        thenOperatorsAreNotSemanticallyEqual();
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO#getOutputMode()}.
     */
    @Test
    public void testGetOutputMode() {
        givenSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
        );
        givenExpression("x + y");
        givenProbabilisticMapPO();

        thenOutputModeIs(OutputMode.NEW_ELEMENT);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO#processPunctuation(de.uniol.inf.is.odysseus.core.physicaloperator.IPunctuation, int)}.
     */
    @Test
    public void testProcessPunctuation() {
        givenSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
        );
        givenExpression("x + y");
        givenProbabilisticMapPO();

        whenProcessPunctuationAt(new Date(0));

        thenPunctuationEquals(new Date(0));
    }

    private void givenOtherExpression(final String expression) {
        final IAttributeResolver resolver = new DirectAttributeResolver(this.schema.get());
        this.otherExpressions = Optional.of(new SDFProbabilisticExpression[] { new SDFProbabilisticExpression(new SDFExpression(expression, resolver, MEP.getInstance())) });
    }

    private void givenOtherProbabilisticMapPO() {
        this.otherOperator = Optional.of(new ProbabilisticMapPO<IProbabilistic>(this.otherSchema.orElse(null), this.otherExpressions.orElse(null), true));
    }

}
