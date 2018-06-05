package de.uniol.inf.is.odysseus.test.component.nexmark;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.osgi.framework.BundleContext;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryExpectedOutputTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.ExpectedOutputTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

public class NexmarkTestComponent extends AbstractQueryExpectedOutputTestComponent<BasicTestContext, ExpectedOutputTestSet> {

	@Override
	public List<ExpectedOutputTestSet> createTestSets(BasicTestContext context) {
		List<ExpectedOutputTestSet> sets = new ArrayList<>();
		String[] tests = new String[]{"1","2","3","3a","4","4a"};
		for (String t:tests) {
			URL query = getURL("query"+t+".qry");
			URL result = getURL("query"+t+".csv");
			ExpectedOutputTestSet set = TestSetFactory.createExpectedOutputTestSetFromFile(query, result, context.getDataRootPath(), "TUPLE");
			sets.add(set);
		}
		// This can be used to check, if the tests fails for wrong data
//		URL query = getURL("query1.qry");
//		URL result = getURL("query1_wrong.csv");
//		TestSet set = TestSetFactory.createTestSetFromFile(query, result, context.getDataRootPath());
//		sets.add(set);
		return sets;
	}
 
	private URL getURL(String file) {
		BundleContext context = Activator.getContext();
		return context.getBundle().getEntry("testdaten/" + file);
	}
	
	@Override
	public boolean isActivated() {
		return true;
	}


	@Override
	public String getName() {	
		return "Nexmark Test";
	}
}
