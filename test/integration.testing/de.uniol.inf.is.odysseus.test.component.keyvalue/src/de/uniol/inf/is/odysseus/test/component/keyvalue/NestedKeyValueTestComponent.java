package de.uniol.inf.is.odysseus.test.component.keyvalue;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryExpectedOutputTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.ExpectedOutputTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

public class NestedKeyValueTestComponent extends AbstractQueryExpectedOutputTestComponent<BasicTestContext, ExpectedOutputTestSet> {

	public NestedKeyValueTestComponent() {
		super();
	}
	
	@Override
	public List<ExpectedOutputTestSet> createTestSets(BasicTestContext context) {
		try {
			return TestSetFactory.createExpectedOutputTestSetsFromBundleRoot(new URL(context.getDataRootPath() + "nestedkv/"), "NESTEDKEYVALUEOBJECT");
		} catch (MalformedURLException e) {
		}
		return null;
	}
	
	@Override
	public String getName() {
		return "NestedKeyValue Test Component";
	}
	
	@Override
	public boolean isActivated() {
		return true;
	}
}
