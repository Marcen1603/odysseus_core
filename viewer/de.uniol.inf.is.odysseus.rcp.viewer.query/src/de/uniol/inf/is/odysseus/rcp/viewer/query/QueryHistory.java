package de.uniol.inf.is.odysseus.rcp.viewer.query;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QueryHistory {

	public static final String HISTORY_FILENAME = ".queryHistory";
	public static final String SEPARATOR = "###";

	private final List<String> queries = new ArrayList<String>();
	private final Logger logger = LoggerFactory.getLogger(QueryHistory.class);
	private static QueryHistory instance = null;

	private QueryHistory() {
		load();
	}
	
	public static QueryHistory getInstance() {
		if( instance == null ) 
			instance = new QueryHistory();
		return instance;
	}

	private void load() {
		queries.clear();

		BufferedReader f = null;
		String line = "";

		try {
			f = new BufferedReader(new FileReader(HISTORY_FILENAME));

			String actualQuery = null;
			while ((line = f.readLine()) != null) {

				if (line.equals(SEPARATOR)) {
					addQuery(actualQuery);
					actualQuery = null;
				} else {
					if (actualQuery == null)
						actualQuery = line;
					else
						actualQuery = actualQuery + "\n" + line;
				}

			}
		} catch (Exception ex) {
			logger.error("error loading query-history", ex);
		} finally {
			try {
				if( f != null ) 
					f.close();
			} catch (Exception ex) {
				logger.error("error closing file", ex);
			}
		}
	}

	public void save() {
		FileWriter writer = null;
		try {
			writer = new FileWriter(HISTORY_FILENAME);

			for (int i = 0; i < queries.size(); i++) {
				writer.write(queries.get(i));
				writer.write("\n");
				writer.write(SEPARATOR);
				writer.write("\n");
			}
			
		} catch (Exception ex) {
			logger.error("error loading query-history", ex);
		} finally {
			try {
				if( writer != null )
					writer.close();
			} catch (Exception ex) {
				logger.error("error closing file", ex);
			}
		}

	}
	
	public void clear() {
		queries.clear();
	}

	public List<String> getQueryHistory() {
		return Collections.unmodifiableList(queries);
	}
	
	public String getQuery( int index ) {
		return queries.get(index);
	}

	public void addQuery(String queryString) {
		int idx = queries.indexOf(queryString);	
		if (idx >= 0) 
			queries.remove(idx);
		
		queries.add(0, queryString);
	}

	public void removeQuery(String queryString) {
		queries.remove(queryString);
	}

	public boolean containsQuery(String queryString) {
		return queries.contains(queryString);
	}
}
