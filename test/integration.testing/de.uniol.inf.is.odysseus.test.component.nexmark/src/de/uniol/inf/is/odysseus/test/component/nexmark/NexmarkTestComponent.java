package de.uniol.inf.is.odysseus.test.component.nexmark;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.osgi.framework.FrameworkUtil;

import de.uniol.inf.is.odysseus.test.component.AbstractTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.TestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

public class NexmarkTestComponent extends AbstractTestComponent<BasicTestContext> {

	@Override
	public List<TestSet> createTestSets(BasicTestContext context) {
		List<TestSet> sets = new ArrayList<>();
		URL query = getURL("query1.qry");
		URL result = getURL("query1.csv");
		TestSet set = TestSetFactory.createTestSetFromFile(query, result, context.getDataRootPath());
		sets.add(set);
		return sets;
	}

	private URL getURL(String file) {
		return Activator.getContext().getBundle().getEntry("testdaten/" + file);
	}

	@Override
	public BasicTestContext createTestContext() {
		BasicTestContext testcontext = new BasicTestContext();
		testcontext.setPassword("manager");
		testcontext.setUsername("System");
		URL bundleentry = FrameworkUtil.getBundle(getClass()).getEntry(".");

		URL rootPath;
		try {
			rootPath = FileLocator.toFileURL(bundleentry);
			testcontext.setDataRootPath(rootPath);
		} catch (IOException e) {
			e.printStackTrace();
		}		
		return testcontext;
	}

}
