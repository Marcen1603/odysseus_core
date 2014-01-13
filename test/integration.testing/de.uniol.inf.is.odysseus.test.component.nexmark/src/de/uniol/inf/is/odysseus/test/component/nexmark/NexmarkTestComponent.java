package de.uniol.inf.is.odysseus.test.component.nexmark;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.TestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

public class NexmarkTestComponent extends AbstractQueryTestComponent<BasicTestContext> {

	@Override
	public List<TestSet> createTestSets(BasicTestContext context) {
		List<TestSet> sets = new ArrayList<>();
		for (int i = 1; i <= 0; i++) {
			URL query = getURL("query"+i+".qry");
			URL result = getURL("query"+i+".csv");
			TestSet set = TestSetFactory.createTestSetFromFile(query, result, context.getDataRootPath());
			sets.add(set);
		}
//		URL query = getURL("query1.qry");
//		URL result = getURL("query1_wrong.csv");
//		TestSet set = TestSetFactory.createTestSetFromFile(query, result, context.getDataRootPath());
//		sets.add(set);
		return sets;
	}

	private URL getURL(String file) {
		return Activator.getContext().getBundle().getEntry("testdaten/" + file);
	}


	@Override
	public String getName() {	
		return "Nexmark Test";
	}
}
