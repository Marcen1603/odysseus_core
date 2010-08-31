package de.uniol.inf.is.odysseus.rcp.editor.text.parser;

import java.util.HashMap;
import java.util.Map;

public class QueryTextParser {

	public static final String PARAMETER_KEY = "#";

	public static final String REPLACEMENT_DEFINITION_KEY = "DEFINE";
	public static final String REPLACEMENT_START_KEY = "${";
	public static final String REPLACEMENT_END_KEY = "}";

	public static final String SINGLE_LINE_COMMENT_KEY = "//";
	
	private final Map<String, String> replacements = new HashMap<String, String>();
	private Map<String, String> variables = new HashMap<String, String>();
	private int currentLine;

	public void setVariable(String var, String value) {
		getVariables().put(var, value);
	}

	public String getVariable(String var) {
		return getVariables().get(var);
	}

	protected Map<String, String> getVariables() {
		if (variables == null)
			variables = new HashMap<String, String>();
		return variables;
	}

	public void parse(String[] text, boolean test) throws QueryTextParseException {
		try {
			StringBuffer sb = null;

			String currentKey = null;
			for (currentLine = 0; currentLine < text.length; currentLine++) {
				String line = text[currentLine].trim();

				// Kommentare entfernen
				line = removeComments(line).trim();

				// Ersetzungen einsetzen
				line = useReplacements(line);
				if (line == null)
					continue;
				else
					line = line.trim();

				if (line.length() > 0) {

					// Neue Parameterzuweisung?
					boolean foundParam = false;
					for (String param : PreParserKeywordRegistry.getInstance().getKeywordNames()) {
						String toFind = PARAMETER_KEY + param;
						final int pos = line.indexOf(toFind);
						if (pos != -1) {

							// alten parameter ausführen
							if (sb != null && currentKey != null) {
								IPreParserKeyword keyword = PreParserKeywordRegistry.getInstance().createKeywordExecutor(currentKey);
								if (!test)
									keyword.execute(this, sb.toString());
								else
									keyword.validate(this, sb.toString());
							}

							// neue parameterzuweisung
							sb = new StringBuffer();
							sb.append(line.substring(pos + param.length() + 1).trim());
							currentKey = param;
							foundParam = true;
							break;
						}
					}
					if (foundParam)
						continue;

					if (sb == null)
						throw new QueryTextParseException("No key set in line " + (currentLine + 1));
					sb.append("\n").append(line.trim());
				}
			}

			// Last query
			if (sb != null && currentKey != null) {
				if (sb.length() > 0) {
					IPreParserKeyword keyword = PreParserKeywordRegistry.getInstance().createKeywordExecutor(currentKey);
					if (!test)
						keyword.execute(this, sb.toString());
					else
						keyword.validate(this, sb.toString());
				} else {
					throw new QueryTextParseException("Empty Key " + currentKey + " in line " + (text.length + 1));
				}
			}
		} catch (QueryTextParseException ex) {
			throw new QueryTextParseException("[Line " + (currentLine + 1) + "]" + ex.getMessage(), ex);
		}
	}

	protected String useReplacements(String line) throws QueryTextParseException {

		// neue Replacementdefinition?
		final int pos = line.indexOf(PARAMETER_KEY + REPLACEMENT_DEFINITION_KEY);
		if (pos != -1) {
			String[] parts = line.split(" |\t", 3);
			// parts[0] is #DEFINE
			replacements.put(parts[1].trim(), parts[2].trim());
			return null;
		} else {

			int posStart = line.indexOf(REPLACEMENT_START_KEY);
			while (posStart != -1) {
				if (posStart != -1) {
					int posEnd = line.indexOf(REPLACEMENT_END_KEY);
					if (posEnd != -1 && posStart < posEnd) {
						String key = line.substring(posStart + REPLACEMENT_START_KEY.length(), posEnd);
						if (replacements.containsKey(key)) {
							line = line.replace(REPLACEMENT_START_KEY + key + REPLACEMENT_END_KEY, replacements.get(key));
						} else {
							throw new QueryTextParseException("Replacer " + key + " not defined ");
						}
					}
				}

				posStart = line.indexOf(REPLACEMENT_START_KEY);
			}
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
}
