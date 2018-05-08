package de.uniol.inf.is.odysseus.test.component.xml;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryExpectedOutputTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.ExpectedOutputTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

public class XMLTestComponent extends AbstractQueryExpectedOutputTestComponent<BasicTestContext, ExpectedOutputTestSet> {


	@Override
	public List<ExpectedOutputTestSet> createTestSets(BasicTestContext context) {
		List<ExpectedOutputTestSet> sets = new ArrayList<>();
		String[] tests = new String[] { 
				"enrich", 
				"map",
				"split",
//				"toTuple",
				"toXML1",
				"toXML2",
				"toXML3",
				"toXML4",
				"toXML5",
				"transform"
		};
		for (String t : tests) {
			URL query = getURL(t + ".qry");
			URL result = getURL(t + ".csv");
			ExpectedOutputTestSet set = TestSetFactory.createExpectedOutputTestSetFromFile(query, result,
					context.getDataRootPath(), "TUPLE");
			sets.add(set);
		}
		return sets;
	}

	private URL getURL(String file) {
		return Activator.getContext().getBundle().getEntry("queries/" + file);
	}

	@Override
	public String getName() {
		return "XML Test";
	}

	@Override
	public boolean isActivated() {
		return true;
	}

}
