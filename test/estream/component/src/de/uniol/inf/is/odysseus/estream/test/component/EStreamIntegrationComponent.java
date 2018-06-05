package de.uniol.inf.is.odysseus.estream.test.component;

import java.util.List;

import de.uniol.inf.is.odysseus.test.component.AbstractQueryTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.QueryTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

public class EStreamIntegrationComponent extends AbstractQueryTestComponent<BasicTestContext, QueryTestSet> {

	public EStreamIntegrationComponent() {
		super(false);
	}
	
	@Override
	public List<QueryTestSet> createTestSets(BasicTestContext context) {
//		List<URL> expected = new ArrayList<>();
//		List<URL> queries = new ArrayList<>();
//		try {
//			searchQueryFilesRecursive(context.getDataRootPath().toURI(), queries, expected);
//		} catch (URISyntaxException e) {
//			e.printStackTrace();
//		}
//		List<ExpectedOutputTestSet> testSet = new ArrayList<>();
//		if (expected.size() == queries.size()) {
//			for (int i = 0; i < queries.size(); i++) {
//				URL query = queries.get(i);
//				URL output = expected.get(i);
//				String filename1 = query.getFile();
//				String filename2 = output.getFile();
//				if(filename1.substring(0, filename1.lastIndexOf(".")).equals(filename2.substring(0, filename2.lastIndexOf(".")))) {
//					testSet.add(TestSetFactory.createExpectedOutputTestSetFromFile(query, output, context.getDataRootPath(), "TUPLE"));
//				}
//			}
//		}
		return TestSetFactory.createQueryTestSetsFromBundleRoot(context.getDataRootPath());
	}

//	private static void searchQueryFilesRecursive(URI url, List<URL> queryFiles, List<URL> expected) {
//		File dir = new File(url);
//		if (dir.isDirectory()) {
//			for (File child : dir.listFiles()) {
//				if (child.isDirectory()) {
//					searchQueryFilesRecursive(child.toURI(), queryFiles, expected);
//				} else {
//					try {
//						if (child.getName().endsWith(".qry")) {
//							queryFiles.add(child.toURI().toURL());
//						}
//						if (child.getName().endsWith(".csv")) {
//							expected.add(child.toURI().toURL());
//						}
//					} catch (MalformedURLException e) { }
//				}
//			}
//		}
//	}

	@Override
	public String getName() {
		return "EStream Integration Test";
	}
}
