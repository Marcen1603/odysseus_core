package de.uniol.inf.is.odysseus.test.set;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.collection.Pair;

public class TestSetFactory {

	public static TestSet createTestSetFromFile(URL queryFile, URL outputdata) {
		TestSet set = new TestSet();
		set.setQuery(fileToString(queryFile));
		set.setExpectedOutput(fileToTupleList(outputdata));
		return set;
	}

	public static List<TestSet> createTestSetsFromBundleRoot(URL bundleroot) {
		return seachRecursive(bundleroot);
	}

	private static List<TestSet> seachRecursive(URL bundleroot) {
		try {
			File dir = new File(bundleroot.toURI());
			List<File> queryFiles = new ArrayList<>();
			searchQueryFilesRecursive(dir, queryFiles);
			System.out.println("FOUND: ");
			for(File qf : queryFiles){
				System.out.println("- "+qf.getAbsolutePath());
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return new ArrayList<>();
		// return null;
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

	public static TestSet createTestSetFromFile(URL queryFile, URL outputdata, URL replaceRootPathInQuery) {
		TestSet set = new TestSet();
		String queryFileStr = fileToString(queryFile);
		queryFileStr = replaceRootPathInFile(queryFileStr, replaceRootPathInQuery);
		set.setQuery(queryFileStr);

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
