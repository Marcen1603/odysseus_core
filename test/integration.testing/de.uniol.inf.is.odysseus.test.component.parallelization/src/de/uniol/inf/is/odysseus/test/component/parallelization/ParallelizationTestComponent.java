package de.uniol.inf.is.odysseus.test.component.parallelization;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryExpectedOutputTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.ExpectedOutputTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

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
				"test/interoperator/aggregate/AggregateNonGroupedStrategyRoundRobin.qry",
				"test/interoperator/aggregate/expected_output.csv", false));
		tests.add(new ParallelizationTest(
				"test/interoperator/aggregate/AggregateNonGroupedStrategyShuffle.qry",
				"test/interoperator/aggregate/expected_output.csv", false));

		// fastmedian
		tests.add(new ParallelizationTest(
				"test/interoperator/fastmedian/FastMedianSequential.qry",
				"test/interoperator/fastmedian/expected_output.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/fastmedian/FastMedianParallel.qry",
				"test/interoperator/fastmedian/expected_output.csv", false));

		// join
		tests.add(new ParallelizationTest(
				"test/interoperator/join/JoinSequential.qry",
				"test/interoperator/join/expected_output.csv", true));
		tests.add(new ParallelizationTest(
				"test/interoperator/join/JoinParallel.qry",
				"test/interoperator/join/expected_output.csv", true));

		// endoperator
		// TODO

		// optimization
		// TODO

		// INTRA-OPERATOR PARALLELIZATION
		// Aggregate
		tests.add(new ParallelizationTest(
				"test/intraoperator/aggregate/ThreadedAggregateHash.qry",
				"test/intraoperator/aggregate/expected_output.csv", false));
		tests.add(new ParallelizationTest(
				"test/intraoperator/aggregate/ThreadedAggregateRoundRobin.qry",
				"test/intraoperator/aggregate/expected_output.csv", false));
		return tests;
	}

}
