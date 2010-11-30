package de.uniol.inf.is.odysseus.rcp.editor.text.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.editor.text.parser.keyword.ExecuteQueryPreParserKeyword;


public class QueryTextParser {

	private static QueryTextParser instance;

	public static final String PARAMETER_KEY = "#";

	public static final String REPLACEMENT_DEFINITION_KEY = "DEFINE";
	public static final String REPLACEMENT_START_KEY = "${";
	public static final String REPLACEMENT_END_KEY = "}";

	public static final String SINGLE_LINE_COMMENT_KEY = "//";

	private int currentLine;

	private QueryTextParser() {

	}	
	
	public static QueryTextParser getInstance() {
		if (instance == null)
			instance = new QueryTextParser();
		return instance;
	}

	public List<IQuery> parseAndExecute(String completeText) throws QueryTextParseException{
		return execute(parseScript(completeText));
	}
	
	@SuppressWarnings("unchecked")
	public List<IQuery> execute(List<PreParserStatement> statements) throws QueryTextParseException {

		List<IQuery> queries = new ArrayList<IQuery>();
		
		Map<String, Object> variables = new HashMap<String, Object>();
		// Validieren
		for (PreParserStatement stmt : statements) {
			stmt.validate(variables);
		}
		
		// Ausführen
		variables = new HashMap<String, Object>();
		int counter = 1;
		for (PreParserStatement stmt : statements) {
			Object ret = stmt.execute(variables);
			// If Statement generates Queries
			if (stmt.getKeyword() instanceof ExecuteQueryPreParserKeyword){
				queries.addAll((Collection<IQuery>)ret);
			}
			counter++;
		}
		
		return queries;
	}

	public List<PreParserStatement> parseScript(String completeText)
			throws QueryTextParseException {
		List<String> lines = null;
		try {
			lines = splitToList(completeText);
		} catch (Exception ex) {
			throw new QueryTextParseException("cannot parse query", ex);
		}
		return parseScript(lines.toArray(new String[lines.size()]));
	}

	public List<PreParserStatement> parseScript(String[] text)
			throws QueryTextParseException {

		List<PreParserStatement> statements = new LinkedList<PreParserStatement>();
		try {
			Map<String, String> replacements = getReplacements(text);
			StringBuffer sb = null;

			String currentKey = null;
			for (currentLine = 0; currentLine < text.length; currentLine++) {
				String line = text[currentLine].trim();

				// Kommentare entfernen
				line = removeComments(line).trim();

				// Ersetzungen einsetzen
				line = useReplacements(line, replacements).trim();
				if (line.indexOf(REPLACEMENT_DEFINITION_KEY) != -1)
					continue;

				if (line.length() > 0) {

					// Neue Parameterzuweisung?
					boolean foundParam = false;
					for (String param : PreParserKeywordRegistry.getInstance()
							.getKeywordNames()) {
						String toFind = PARAMETER_KEY + param;
						final int pos = line.indexOf(toFind);
						if (pos != -1) {

							// alten parameter ausfÃ¼hren
							if (sb != null && currentKey != null) {
								IPreParserKeyword keyword = PreParserKeywordRegistry
										.getInstance().createKeywordExecutor(
												currentKey);
								statements.add(new PreParserStatement(
										currentKey, keyword, sb.toString()));
							}

							// neue parameterzuweisung
							sb = new StringBuffer();
							sb.append(line.substring(pos + param.length() + 1)
									.trim());
							currentKey = param;
							foundParam = true;
							break;
						}
					}
					if (foundParam)
						continue;

					if (sb == null)
						throw new QueryTextParseException("No key set in line "
								+ (currentLine + 1));
					sb.append("\n").append(line.trim());
				}
			}

			// Last query
			if (sb != null && currentKey != null) {
				IPreParserKeyword keyword = PreParserKeywordRegistry
						.getInstance().createKeywordExecutor(currentKey);
				statements.add(new PreParserStatement(currentKey, keyword, sb
						.toString()));
			}

			return statements;
		} catch (QueryTextParseException ex) {
			throw new QueryTextParseException("[Line " + (currentLine + 1)
					+ "]" + ex.getMessage(), ex);
		}
	}

	public Map<String, String> getReplacements(String text)
			throws QueryTextParseException {
		return getReplacements(splitToList(text).toArray(new String[0]));
	}

	public Map<String, String> getReplacements(String[] text)
			throws QueryTextParseException {
		Map<String, String> repl = new HashMap<String, String>();
		for (String line : text) {
			String correctLine = removeComments(line).trim();
			String replacedLine = useReplacements(correctLine, repl);
			final int pos = replacedLine.indexOf(PARAMETER_KEY
					+ REPLACEMENT_DEFINITION_KEY);
			if (pos != -1) {
				String[] parts = replacedLine.split(" |\t", 3);
				// parts[0] is #DEFINE
				repl.put(parts[1].trim(), parts[2].trim());
			}
		}
		return repl;
	}

	protected String useReplacements(String line,
			Map<String, String> replacements) throws QueryTextParseException {
		int posStart = line.indexOf(REPLACEMENT_START_KEY);
		while (posStart != -1) {
			if (posStart != -1) {
				int posEnd = line.indexOf(REPLACEMENT_END_KEY);
				if (posEnd != -1 && posStart < posEnd) {
					String key = line.substring(posStart
							+ REPLACEMENT_START_KEY.length(), posEnd);
					if (replacements.containsKey(key)) {
						line = line.replace(REPLACEMENT_START_KEY + key
								+ REPLACEMENT_END_KEY, replacements.get(key));
					} else {
						throw new QueryTextParseException("Replacer " + key
								+ " not defined ");
					}
				}
			}

			posStart = line.indexOf(REPLACEMENT_START_KEY);
		}
		return line;
	}

	protected String removeComments(String line) {
		if (line == null || line.length() == 0)
			return "";

		final int commentPos = line.indexOf(SINGLE_LINE_COMMENT_KEY);
		if (commentPos != -1) {
			if (commentPos == 0)
				return "";
			else
				return line.substring(0, commentPos);
		}

		return line;
	}

	private static List<String> splitToList(String text) {
		ArrayList<String> lines = new ArrayList<String>();
		BufferedReader br = new BufferedReader(new StringReader(text));
		try {
			String line = br.readLine();
			while (line != null) {
				lines.add(line);
				line = br.readLine();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException e) {
			}
		}
		return lines;
	}


}
