package de.uniol.inf.is.odysseus.test.set;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.metadata.TimeInterval;

public class TestSetFactory {
	
	public static TestSet createTestSetFromFile(URL queryFile, URL outputdata) {
		TestSet set = new TestSet();
		set.setQuery(fileToString(queryFile));
		set.setExpectedOutput(fileToTupleList(outputdata));
		return set;
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

	private static List<Tuple<? extends ITimeInterval>> fileToTupleList(URL path) {
		List<Tuple<? extends ITimeInterval>> tuples = new ArrayList<>();				
		try {
			InputStreamReader is = new InputStreamReader(path.openStream());
			BufferedReader br = new BufferedReader(is);
			String line = br.readLine();
			while (line != null) {
				Tuple<? extends ITimeInterval> t = parseTupleString(line);
				tuples.add(t);
				line = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return tuples;
	}

	private static Tuple<? extends ITimeInterval> parseTupleString(String line) {
		String[] parts = line.split(Pattern.quote("| META |"));
		String[] attributes = parts[0].split("\\|");
		Tuple<TimeInterval> t = new Tuple<>(attributes, false);
		String[] time = parts[1].split("\\|");

		PointInTime start = PointInTime.parsePointInTime(time[0]);
		PointInTime end = PointInTime.parsePointInTime(time[1]);
		TimeInterval ti = new TimeInterval(start, end);
		t.setMetadata(ti);
		return t;
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
