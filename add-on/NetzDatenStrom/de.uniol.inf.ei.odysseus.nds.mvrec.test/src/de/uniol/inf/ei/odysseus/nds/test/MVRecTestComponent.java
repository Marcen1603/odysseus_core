package de.uniol.inf.ei.odysseus.nds.test;

import java.util.List;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryExpectedOutputTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.ExpectedOutputTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

/**
 * Test component that searches for every ".qry" file in the bundle, runs the
 * queries and compares the result with an expected result.
 *
 * @author Michael Brand (michael.brand@uol.de)
 *
 */
public class MVRecTestComponent
		extends AbstractQueryExpectedOutputTestComponent<BasicTestContext, ExpectedOutputTestSet> {

	@Override
	public List<ExpectedOutputTestSet> createTestSets(BasicTestContext context) {
		return TestSetFactory.createExpectedOutputTestSetsFromBundleRoot(context.getDataRootPath());
	}

	@Override
	public String getName() {
		return "NetzDatenStrom MVRec Test Component";
	}

	@Override
	public boolean isActivated() {
		return true;
	}

}