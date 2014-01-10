package de.uniol.inf.is.odysseus.test.component.operator;

import java.util.List;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.TestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

public class OperatorTestComponent extends AbstractQueryTestComponent<BasicTestContext> {

	@Override
	public List<TestSet> createTestSets(BasicTestContext context) {
		return TestSetFactory.createTestSetsFromBundleRoot(context.getDataRootPath());		
	}
	
	@Override
	public String getName() {
		return "Operator Test Component";
	}

}
