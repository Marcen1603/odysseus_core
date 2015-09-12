/********************************************************************************** 
 * Copyright 2015 The Odysseus Team
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
package de.uniol.inf.is.odysseus.test.component.parallelization;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryExpectedOutputTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.ExpectedOutputTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

/**
 * Test component for parallelization feature
 * 
 * @author ChrisToenjesDeye
 *
 */
public class ParallelizationTestComponent
		extends
		AbstractQueryExpectedOutputTestComponent<BasicTestContext, ExpectedOutputTestSet> {

	@Override
	public List<ExpectedOutputTestSet> createTestSets(BasicTestContext context) {
		List<ExpectedOutputTestSet> testSets = new ArrayList<ExpectedOutputTestSet>();
		for (ParallelizationTest parallelizationTest : getTests()) {
			if (parallelizationTest.isEnabled()) {
				if (parallelizationTest.getQueryFile() != null
						&& parallelizationTest.getResultFile() != null) {
					ExpectedOutputTestSet set = TestSetFactory
							.createExpectedOutputTestSetFromFile(
									parallelizationTest.getQueryFile(),
									parallelizationTest.getResultFile(),
									context.getDataRootPath(), "TUPLE");
					testSets.add(set);
				}
			}
		}
		return testSets;
	}

	@Override
	public boolean isActivated() {
		return true;
	}

	@Override
	public String getName() {
		return "Parallelization Test Component";
	}

	private List<ParallelizationTest> getTests() {
		List<ParallelizationTest> tests = new ArrayList<ParallelizationTest>();

		// INTER-OPERATOR PARALLELIZATION
		// Aggregate
		tests.add(new ParallelizationTest(
				"test/interoperator/aggregate/AggregateSequential.qry",
				"test/interoperator/aggregate/expected_output.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/aggregate/AggregateGroupedStrategy.qry",
				"test/interoperator/aggregate/expected_output.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/aggregate/AggregateGroupedStrategyThreaded.qry",
				"test/interoperator/aggregate/expected_output.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/aggregate/AggregateNonGroupedStrategyRR.qry",
				"test/interoperator/aggregate/expected_output.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/aggregate/AggregateNonGroupedStrategyRRThreaded.qry",
				"test/interoperator/aggregate/expected_output.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/aggregate/AggregateNonGroupedStrategyShuffle.qry",
				"test/interoperator/aggregate/expected_output.csv", true));

		// fastmedian
		tests.add(new ParallelizationTest(
				"test/interoperator/fastmedian/FastMedianSequential.qry",
				"test/interoperator/fastmedian/expected_output.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/fastmedian/FastMedianParallel.qry",
				"test/interoperator/fastmedian/expected_output.csv", true));

		// join
		tests.add(new ParallelizationTest(
				"test/interoperator/join/JoinSequential.qry",
				"test/interoperator/join/expected_output.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/join/JoinParallel.qry",
				"test/interoperator/join/expected_output.csv", true));

		// endoperator
		tests.add(new ParallelizationTest(
				"test/interoperator/endoperator/NoEndoperator.qry",
				"test/interoperator/endoperator/expected_output.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/endoperator/EndoperatorOneOperator.qry",
				"test/interoperator/endoperator/expected_output.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/endoperator/EndoperatorMultipleOperators.qry",
				"test/interoperator/endoperator/expected_output.csv", true));

		// optimization
		tests.add(new ParallelizationTest(
				"test/interoperator/optimization/NoOptimization1.qry",
				"test/interoperator/optimization/expected_output1.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/optimization/Optimization1.qry",
				"test/interoperator/optimization/expected_output1.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/optimization/NoOptimization2.qry",
				"test/interoperator/optimization/expected_output2.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/optimization/Optimization2.qry",
				"test/interoperator/optimization/expected_output2.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/optimization/NoOptimization3.qry",
				"test/interoperator/optimization/expected_output3.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/optimization/Optimization3.qry",
				"test/interoperator/optimization/expected_output3.csv", true));

		// INTRA-OPERATOR PARALLELIZATION
		// Aggregate
		tests.add(new ParallelizationTest(
				"test/intraoperator/aggregate/ThreadedAggregateHash.qry",
				"test/intraoperator/aggregate/expected_output.csv", true));
		tests.add(new ParallelizationTest(
				"test/intraoperator/aggregate/ThreadedAggregateRoundRobin.qry",
				"test/intraoperator/aggregate/expected_output.csv", true));

		return tests;
	}

}
