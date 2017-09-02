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

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.Optional;

import org.junit.Test;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractPipe.OutputMode;
import de.uniol.inf.is.odysseus.probabilistic.base.predicate.ProbabilisticRelationalPredicate;
import de.uniol.inf.is.odysseus.probabilistic.metadata.IProbabilistic;
import de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO;

/**
 * Basic tests for {@link ProbabilisticSelectPO} operator.
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
public class SelectPOTest extends AbstractSelectPOTest {

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO#process_open()}.
     */
    @Test
    public void testProcessOpen() {
        givenSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
                );

        givenPredicate("x<y");
        givenProbabilisticSelectPO();

        whenProcessOpen();
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO#toString()}.
     */
    @Test
    public void testToString() {
        givenSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
                );
        givenPredicate("x<y");
        givenProbabilisticSelectPO();

        thenToStringContainsPredicate();
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO#process_isSemanticallyEqual(de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)}.
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
        givenPredicate("x<y");
        givenOtherPredicate("x<y");
        givenProbabilisticSelectPO();
        givenSemanticallyEqualProbabilisticSelectPO();

        whenProcess_isSemanticallyEqual();

        thenProcess_isSemanticallyEqualReturns(true);
    }

    /**
     * Test method for
     * {@link de.uniol.inf.is.odysseus.probabilistic.physicaloperator.ProbabilisticSelectPO#process_isSemanticallyEqual(de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator)}.
     */
    @Test
    public void testProcessIsSemanticallyEqualWithInvertedPredicate() {
        givenSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
                );
        givenOtherSchema(//
                attribute("x").as(SDFDatatype.DOUBLE), //
                attribute("y").as(SDFDatatype.DOUBLE) //
                );
        givenPredicate("x<y");
        givenOtherPredicate("!(x<y)");
        givenProbabilisticSelectPO();
        givenNotSemanticallyEqualProbabilisticSelectPO();

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
        givenPredicate("x<y");
        givenOtherPredicate("x<y");
        givenProbabilisticSelectPO();
        givenOtherProbabilisticSelectPO();

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
        givenPredicate("x<y");
        givenOtherPredicate("x>y");
        givenProbabilisticSelectPO();
        givenOtherProbabilisticSelectPO();

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
        givenPredicate("x<y");
        givenProbabilisticSelectPO();

        thenOutputModeIs(OutputMode.MODIFIED_INPUT);
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
        givenPredicate("x<y");
        givenProbabilisticSelectPO();

        whenProcessPunctuationAt(new Date(0));

        thenPunctuationEquals(new Date(0));
    }

    private void givenOtherProbabilisticSelectPO() {
        this.otherOperator = Optional.of(new ProbabilisticSelectPO<IProbabilistic>(this.otherSchema.orElse(null),
                this.otherPredicate.orElse(null)));
    }

    private void givenSemanticallyEqualProbabilisticSelectPO() {
        this.otherOperator = Optional
                .of(new ProbabilisticSelectPO<IProbabilistic>(this.schema.orElse(null), this.predicate.orElse(null)));
    }

    private void givenNotSemanticallyEqualProbabilisticSelectPO() {
        assertThat("Cannot create inverted predicate for null value", this.predicate.isPresent(), is(true));
        this.otherOperator = Optional.of(new ProbabilisticSelectPO<IProbabilistic>(this.schema.orElse(null),
                this.predicate.map(predicate -> (ProbabilisticRelationalPredicate) predicate.not()).orElse(null)));
    }

    private void thenToStringContainsPredicate() {
        assertThat("ToString() does not contain the predicate", this.operator.get().toString(),
                endsWith(this.predicate.get().getExpression().getExpressionString()));
    }

}
