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

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 *
 * @author Christian Kuka <christian@kuka.cc>
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ //
        IntervalTest.class, //
        IntervalArithmeticAdditionTest.class, //
        IntervalArithmeticSubtractionTest.class, //
        IntervalArithmeticMultiplicationTest.class, //
        IntervalArithmeticDivisionTest.class, //

        MapPOTest.class, //

        MapPOExpressionsWithContinuousProbabilityDistributionTest.class, //

        SelectPOTest.class, //

        SelectPOCompareContinuousProbabilityDistributionsWithDiscreteNumbersTest.class, //
        SelectPOCompareContinuousProbabilityDistributionsWithContinuousProbabilityDistributionsTest.class, //
        SelectPOCompareMultiDimensionalContinuousProbabilityDistributionsWithDiscreteNumbersTest.class, //

        SelectPOCompareDiscreteProbabilityDistributionsWithDiscreteNumbersTest.class, //
        SelectPOCompareDiscreteProbabilityDistributionsWithDiscreteProbabilityDistributionsTest.class, //
        SelectPOCompareMultiDimensionalDiscreteProbabilityDistributionsWithDiscreteNumbersTest.class, //

        SelectPOCompareDeterministicNumbersWithDeterministicNumbersTest.class,//

})
public class AllTests {

}
