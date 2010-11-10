package de.uniol.inf.is.odysseus.rcp.viewer.query;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class QueryHistory {

	private class QueryHistoryEntry {
		public String query;
		public String parser;
		
		public QueryHistoryEntry( String query, String parser ) {
			this.query = query;
			this.parser = parser;
		}
		
		@Override
		public String toString() {
			return "[" + parser + "] " + query;
		}
		
		@Override
		public boolean equals(Object obj) {
			if( obj == this ) return true;
			if( !(obj instanceof QueryHistoryEntry)) return false;
			
			QueryHistoryEntry entry = (QueryHistoryEntry)obj;
			return entry.query.equals(query) && entry.parser.equals(parser);
		}
		
	}
	public static final String HISTORY_FILENAME = System.getProperty("user.home")
	+ "/odysseus/.queryHistory";
	public static final String SEPARATOR = "###";
	public static final String PARSER = "[PARSER]";

	private final List<QueryHistoryEntry> queries = new ArrayList<QueryHistoryEntry>();
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
			
			File ff = new File(HISTORY_FILENAME);
			if (!ff.exists()) {
				File d = ff.getParentFile();
				if (d != null) {
					d.mkdirs();
				}
				ff.createNewFile();
				System.out.println("Created new File " + ff.getAbsolutePath());
			} else {
				System.out.println("Read from file " + ff.getAbsolutePath());
			}
			
			f = new BufferedReader(new FileReader(HISTORY_FILENAME));

			String actualQuery = null;
			String usedParser = null;
			while ((line = f.readLine()) != null) {

				if( line.startsWith(PARSER)) {
					usedParser = line.substring(PARSER.length());
				} else if (line.equals(SEPARATOR)) {
					
					//abwärtskompatibel
					if( usedParser == null ) 
						usedParser = "CQL";
					
					addQuery(usedParser, actualQuery);
					actualQuery = null;
					usedParser = null;
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
				QueryHistoryEntry e = queries.get(i);
				
				writer.write(e.query);
				writer.write("\n");
				writer.write(PARSER + e.parser);
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
		return Collections.unmodifiableList(getStringList());
	}
	
	public String getQuery( int index ) {
		return queries.get(index).query;
	}
	
	public String getParser( int index ) {
		return queries.get(index).parser;
	}

	public void addQuery(String parser, String queryString) {
		QueryHistoryEntry e = new QueryHistoryEntry(queryString, parser);
		
		int idx = queries.indexOf(e);	
		if (idx >= 0) 
			queries.remove(idx);
		
		queries.add(0, e);
		save();
	}

	public void removeQuery(String queryString) {
		queries.remove(queryString);
	}

	public boolean containsQuery(String queryString) {
		return queries.contains(queryString);
	}
	
	protected List<String> getStringList() {
		List<String> str = new ArrayList<String>();
		for( int i = 0; i < queries.size(); i++ ) {
			str.add(queries.get(i).toString());
		}
		return str;
	}
}
