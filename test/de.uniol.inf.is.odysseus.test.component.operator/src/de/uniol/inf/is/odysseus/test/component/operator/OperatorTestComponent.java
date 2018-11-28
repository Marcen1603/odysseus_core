package de.uniol.inf.is.odysseus.test.component.operator;

import java.util.List;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryExpectedOutputTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.ExpectedOutputTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

public class OperatorTestComponent extends AbstractQueryExpectedOutputTestComponent<BasicTestContext, ExpectedOutputTestSet> {

	@Override
	public List<ExpectedOutputTestSet> createTestSets(BasicTestContext context) {
		return TestSetFactory.createExpectedOutputTestSetsFromBundleRoot(context.getDataRootPath());		
	}
	
	@Override
	public String getName() {
		return "Operator Test Component";
	}
	
	@Override
	public boolean isActivated() {
		return true;
	}

}
