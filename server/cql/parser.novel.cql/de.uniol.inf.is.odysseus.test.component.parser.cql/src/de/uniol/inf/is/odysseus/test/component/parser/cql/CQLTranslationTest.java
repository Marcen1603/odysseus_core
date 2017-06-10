package de.uniol.inf.is.odysseus.test.component.parser.cql;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.mwe.internal.core.ast.util.Injector;
//import org.junit.Before;
//import org.junit.Test;

import de.uniol.inf.is.odysseus.parser.novel.cql.CQLStandaloneSetup;


public class CQLTranslationTest {

	private class TestSet {

		String query;
		String operatorPlan;

		public TestSet(File query, File operatorPlan) {
			this.query = getData(query);
			this.operatorPlan = getData(operatorPlan);
		}

		private String getData(File file) {
			StringBuilder builder = new StringBuilder();
			try (InputStream in = new FileInputStream(file)) {
				String line;
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				while ((line = br.readLine()) != null) {
					if (!line.contains("#"))
						builder.append(line);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return builder.toString();
		}

	}

	List<TestSet> testSet = new ArrayList<>();

//	@Before
	public void load() {
		List<File> queryFiles = new ArrayList<>();
		List<File> pqlFiles = new ArrayList<>();
		CQLParserUtil.searchQueryFilesRecursive(new File("queries/.."), queryFiles, pqlFiles);
		for (File query : queryFiles) {
			for (File output : pqlFiles) {
				if (query.getName().replace(".qry", "").equals(output.getName().replace(".pql", ""))) {
					testSet.add(new TestSet(query, output));
					break;
				}
			}
		}
	}

//	@Test
	public void foo() {
		
		Injector injector = (Injector) new CQLStandaloneSetup().createInjector();
		
//		for (TestSet set : testSet) {
//			try (InputStream in = new ByteArrayInputStream(set.query.getBytes())) {
//				for (EObject component : parser.injectModel(in)) {
//					parser.generatePQL(component, new InMemoryFileSystemAccess(), null);
//				}
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
}
