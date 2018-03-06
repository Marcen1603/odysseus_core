package de.uniol.inf.is.odysseus.test.component.parser.cql;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.test.StatusCode;
import de.uniol.inf.is.odysseus.test.component.AbstractQueryTestComponent;
import de.uniol.inf.is.odysseus.test.context.BasicTestContext;
import de.uniol.inf.is.odysseus.test.set.QueryTestSet;
import de.uniol.inf.is.odysseus.test.set.TestSetFactory;

public class CQLParserTest extends AbstractQueryTestComponent<BasicTestContext, QueryTestSet> {

	public CQLParserTest() {
		super(false);
	}

	@Override
	public List<QueryTestSet> createTestSets(BasicTestContext context) {
		File dir;
		List<File> queryFiles = new ArrayList<>();
		List<File> pqlFiles = new ArrayList<>();
		List<QueryTestSet> testsets = new ArrayList<>();
		try {
			URL bundleroot = context.getDataRootPath();
			dir = new File(bundleroot.toURI());
			CQLParserUtil.searchQueryFilesRecursive(dir, queryFiles, pqlFiles);
			for (File qf : queryFiles) {

				QueryTestSet set = TestSetFactory.createQueryTestSetFromFile(qf.toURI().toURL(), bundleroot);
				if (set != null) {
					testsets.add(set);
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return testsets;
	}

	@Override
	protected StatusCode executeTestSet(QueryTestSet set) {

		StatusCode status = super.executeTestSet(set);
		switch (status) {
		case ERROR_QUERY_NOT_INSTALLABLE:
		case ERROR_EXCEPTION_DURING_TEST:
			break;
		default:
		}
		return status;
	}

	@Override
	public String getName() {
		return "CQL Parser Test";
	}

	@Override
	public boolean isActivated() {
		return true;
	}

}
