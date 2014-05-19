package de.uniol.inf.is.odysseus.test.set;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class TestSetFactory {

	private static Logger LOG = LoggerFactory.getLogger(TestSetFactory.class);
	private static final String[] OUTPUT_FILE_NAMES = { "output", "expected", "expected_output" };	
	
	public static QueryTestSet createQueryTestSetFromFile(URL queryFile, URL rootPath){
		QueryTestSet set = new QueryTestSet();
		String queryFileStr = fileToString(queryFile);
		queryFileStr = replaceRootPathInFile(queryFileStr, rootPath);
		set.setQuery(queryFileStr);	
		File f = new File(queryFile.getFile());
		set.setName(f.getName());
		return set;
	}

	public static List<ExpectedOutputTestSet> createExpectedOutputTestSetsFromBundleRoot(URL bundleroot) {
		List<ExpectedOutputTestSet> testsets = new ArrayList<>();
		try {
			File dir = new File(bundleroot.toURI());
			List<File> queryFiles = new ArrayList<>();
			searchQueryFilesRecursive(dir, queryFiles);
			for (File qf : queryFiles) {
				ExpectedOutputTestSet set = createExpectedOutputTestSetFromQuery(qf, bundleroot);
				if (set != null) {
					testsets.add(set);
				}
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return testsets;
	}

	public static List<QueryTestSet> createQueryTestSetsFromBundleRoot(URL bundleroot) {
		List<QueryTestSet> testsets = new ArrayList<>();
		try {
			File dir = new File(bundleroot.toURI());
			List<File> queryFiles = new ArrayList<>();
			searchQueryFilesRecursive(dir, queryFiles);
			for (File qf : queryFiles) {
				QueryTestSet set = createQueryTestSetFromFile(qf.toURI().toURL(), bundleroot);				
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

	private static ExpectedOutputTestSet createExpectedOutputTestSetFromQuery(File qf, URL bundleroot) {
		try {
			URL queryFile = qf.toURI().toURL();
			File outputFile = getOutputFile(qf);
			if (outputFile != null) {
				URL outputdata = outputFile.toURI().toURL();
				ExpectedOutputTestSet set = createExpectedOutputTestSetFromFile(queryFile, outputdata, bundleroot);
				return set;
			} 
			LOG.error("There is no corresponding outputfile for " + qf.getAbsoluteFile());
			LOG.error("Use same name or one of: " + Arrays.toString(OUTPUT_FILE_NAMES));
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static File getOutputFile(File qf) {
		File dir = qf.getParentFile();
		String name = qf.getName().substring(0, qf.getName().length() - 4);
		File f = new File(dir + File.separator + name + ".csv");
		if (f.exists()) {
			return f;
		}
		for (String filename : OUTPUT_FILE_NAMES) {
			f = new File(dir + File.separator +filename + ".csv");
			if (f.exists()) {
				return f;
			}
		}
		return null;
	}

	private static void searchQueryFilesRecursive(File dir, List<File> queryFiles) {
		if (dir.isDirectory()) {
			for (File child : dir.listFiles()) {
				if (child.isDirectory()) {
					searchQueryFilesRecursive(child, queryFiles);
				} else {
					if (child.getName().endsWith(".qry")) {
						queryFiles.add(child);
					}
				}
			}

		}
	}

	public static ExpectedOutputTestSet createExpectedOutputTestSetFromFile(URL queryFile, URL outputdata, URL replaceRootPathInQuery) {
		ExpectedOutputTestSet set = new ExpectedOutputTestSet();
		String queryFileStr = fileToString(queryFile);
		queryFileStr = replaceRootPathInFile(queryFileStr, replaceRootPathInQuery);
		set.setQuery(queryFileStr);	
		File f = new File(queryFile.getFile());
		set.setName(f.getName());
		set.setExpectedOutput(fileToTupleList(outputdata));
		return set;
	}

	private static String replaceRootPathInFile(String queryFileStr, URL rootPath) {
		return queryFileStr.replace("${BUNDLE-ROOT}", rootPath.getFile());
	}

	private static List<Pair<String, String>> fileToTupleList(URL path) {
		List<Pair<String, String>> tuples = new ArrayList<>();
		try {
			InputStreamReader is = new InputStreamReader(path.openStream());
			BufferedReader br = new BufferedReader(is);
			String line = br.readLine();
			while (line != null) {
				Pair<String, String> t = parseTupleString(line);
				tuples.add(t);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tuples;
	}

	private static Pair<String, String> parseTupleString(String line) {
		String[] parts = line.split(Pattern.quote("| META |"));
		Pair<String, String> pair = new Pair<String, String>(parts[0].trim(), parts[1].trim());
		return pair;
	}

	private static String fileToString(URL path) {
		try {
			InputStreamReader is = new InputStreamReader(path.openStream());
			BufferedReader br = new BufferedReader(is);
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			br.close();
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
