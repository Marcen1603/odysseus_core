package de.uniol.inf.is.odysseus.rcp.editor.text.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryTextParser {
	
	public static final String PARAMETER_KEY = "#";
	public static final String REPLACEMENT_START_KEY = "${";
	public static final String REPLACEMENT_END_KEY = "}";
	
	public static final String[] KEYS = new String[] {
		"PARSER", "TRANSCFG", "QUERY"
	};
	private static final String[] DEFAULT_VALUES = new String[] {
		"CQL", "Standard", ""
	};

	public static final String SINGLE_LINE_COMMENT_KEY = "//";
	
	private final Map<String,String> replacements = new HashMap<String, String>();
	
	public List<QueryEntry> parse( String[] text ) throws QueryTextParseException {
		StringBuffer sb = null;
		Map<String, String> parameters = new HashMap<String, String>();
		List<QueryEntry> queries = new ArrayList<QueryEntry>();
		
		for( int i = 0; i < KEYS.length; i++ ) 
			parameters.put(KEYS[i], DEFAULT_VALUES[i]);

		String currentKey = null;
		for( int currentLine = 0; currentLine < text.length; currentLine++) {
			String line = text[currentLine].trim();
			
			// Kommentare entfernen
			line = removeComments(line).trim();
			line = useReplacements(line);
			if( line == null ) 
				continue;
			else 
				line = line.trim();

			if (line.length() > 0) {
					
				// Neue Parameterzuweisung?
				boolean foundParam = false;
				for( String param : parameters.keySet() ) {
					String toFind = PARAMETER_KEY + param;
					final int pos = line.indexOf(toFind);
					if( pos != -1 ) {

						// alten parameter abspeichern
						if( sb != null && currentKey != null ) { 
							if( sb.length() > 0 ) {
								parameters.put(currentKey, sb.toString());
								
								if( currentKey.equals( "QUERY" ) ) 
									queries.add(new QueryEntry(parameters.get("PARSER"), parameters.get("TRANSCFG"), parameters.get("QUERY")));
							} 
						}
						
						// neue parameterzuweisung
						sb = new StringBuffer();
						sb.append(line.substring(pos+param.length()+1).trim());
						currentKey = param;
						foundParam = true;
						break;
					}
				}
				if( foundParam )
					continue;
				
				if( sb == null ) 
					throw new QueryTextParseException("No key set in line " + ( currentLine + 1 ) );
				sb.append(line.trim());
			}
		}

		// Last query
		if( sb != null && currentKey != null ) {
			if( sb.length() > 0 ) {
				parameters.put(currentKey, sb.toString());
				
				if( currentKey.equals( "QUERY" ) ) 
					queries.add(new QueryEntry(parameters.get("PARSER"), parameters.get("TRANSCFG"), parameters.get("QUERY")));
			} 
		}
		
		return queries;		
	}
	
	protected String useReplacements(String line) throws QueryTextParseException {
		
		// neue Replacementdefinition?
		final int pos = line.indexOf("#DEFINE");
		if( pos != -1 ) {
			String[] parts = line.split(" ", 3);
			// parts[0] is #DEFINE
			replacements.put(parts[1].trim(), parts[2].trim());
			return null;
		} else {
			
			int posStart = line.indexOf(REPLACEMENT_START_KEY);
			while( posStart != -1 ) {
				if( posStart != -1 ) {
					int posEnd = line.indexOf(REPLACEMENT_END_KEY);
					if( posEnd != -1 && posStart < posEnd ) {
						String key = line.substring(posStart + REPLACEMENT_START_KEY.length(), posEnd);
						if( replacements.containsKey(key )) {
							line = line.replace(REPLACEMENT_START_KEY + key + REPLACEMENT_END_KEY, replacements.get(key));
						} else {
							throw new QueryTextParseException( "Replacer " + key + " not defined ");
						}
					}
				}
				
				posStart = line.indexOf(REPLACEMENT_START_KEY);
			}
		}
		return line;
	}

	protected String removeComments( String line ) {
		if( line == null || line.length() == 0 )
			return "";
		
		final int commentPos = line.indexOf(SINGLE_LINE_COMMENT_KEY);
		if( commentPos != -1 ) {
			if( commentPos == 0 ) 
				return "";
			else 
				return line.substring(0, commentPos);
		}
		
		return line;
	}
}
