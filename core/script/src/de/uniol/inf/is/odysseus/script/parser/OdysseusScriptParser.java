/** Copyright [2011] [The Odysseus Team]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.uniol.inf.is.odysseus.script.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import de.uniol.inf.is.odysseus.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class OdysseusScriptParser implements IOdysseusScriptParser {

	private static PreParserKeywordRegistry registry = new PreParserKeywordRegistry();

	private static final String PARAMETER_KEY = "#";

	private static final String REPLACEMENT_DEFINITION_KEY = "DEFINE";
	private static final String REPLACEMENT_START_KEY = "${";
	private static final String REPLACEMENT_END_KEY = "}";

	private static final String LOOP_START_KEY = PARAMETER_KEY + "LOOP";
	private static final String LOOP_END_KEY = PARAMETER_KEY + "ENDLOOP";	
	private static final String LOOP_UPTO	= "UPTO";
	
	private static final String SINGLE_LINE_COMMENT_KEY = "///";

	private int currentLine;

	@Override
	public String getParameterKey() {
		return PARAMETER_KEY;
	}
	
	@Override
	public String getReplacementEndKey() {
		return REPLACEMENT_END_KEY;
	}
	
	@Override
	public String getReplacementStartKey() {
		return REPLACEMENT_START_KEY;
	}
	
	@Override
	public String getSingleLineCommentKey() {
		return SINGLE_LINE_COMMENT_KEY;
	}


//	public synchronized static QueryTextParser getInstance() {
//		if (instance == null)
//			instance = new QueryTextParser();
//		return instance;
//	}

	
	public static List<String> getStaticWords(){
		List<String> strings = new ArrayList<String>();
		strings.add(PARAMETER_KEY+REPLACEMENT_DEFINITION_KEY);
		strings.add(LOOP_END_KEY);
		strings.add(LOOP_START_KEY);		
		strings.add(LOOP_UPTO);
		return strings;
	}
	
	@Override
	public Set<String> getKeywordNames() {
		return registry.getKeywordNames();
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser#parseAndExecute(java.lang.String, de.uniol.inf.is.odysseus.usermanagement.User)
	 */

	@Override
	public void parseAndExecute(String completeText, User caller, ISink<?> defaultSink) throws OdysseusScriptParseException {
		execute(parseScript(completeText, caller), caller, defaultSink);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser#execute(java.util.List, de.uniol.inf.is.odysseus.usermanagement.User)
	 */
	@Override
	public void execute(List<PreParserStatement> statements, User caller, ISink<?> defaultSink) throws OdysseusScriptParseException {

		Map<String, Object> variables = new HashMap<String, Object>();
		if (defaultSink != null){
			variables.put("_defaultSink", defaultSink);
		}
		// Validieren
		for (PreParserStatement stmt : statements) {
			stmt.validate(variables, caller);
		}

		// Ausf�hren
		variables = new HashMap<String, Object>();
		if (defaultSink != null){
			variables.put("_defaultSink", defaultSink);
		}
		for (PreParserStatement stmt : statements) {
			stmt.execute(variables, caller, this);
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser#parseScript(java.lang.String, de.uniol.inf.is.odysseus.usermanagement.User)
	 */
	@Override
	public List<PreParserStatement> parseScript(String completeText, User caller) throws OdysseusScriptParseException {
		List<String> lines = null;
		try {
			lines = splitToList(completeText);
		} catch (Exception ex) {
			throw new OdysseusScriptParseException("cannot parse script ", ex);
		}
		return parseScript(lines.toArray(new String[lines.size()]), caller);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser#parseScript(java.lang.String[], de.uniol.inf.is.odysseus.usermanagement.User)
	 */
	@Override
	public List<PreParserStatement> parseScript(String[] textToParse, User caller) throws OdysseusScriptParseException {

		List<PreParserStatement> statements = new LinkedList<PreParserStatement>();
		try {
			String[] text = rewriteLoop(textToParse);
			Map<String, String> replacements = getReplacements(text);
			StringBuffer sb = null;

			String currentKey = null;
			for (currentLine = 0; currentLine < text.length; currentLine++) {
				String line = text[currentLine].trim();

				// Kommentare entfernen
				line = removeComments(line);
				if ((line == null) || (line.equals("null")))
					continue;

				line = line.trim();
				// Ersetzungen einsetzen
				line = useReplacements(line, replacements).trim();
				if (line.indexOf(REPLACEMENT_DEFINITION_KEY) != -1)
					continue;

				if (line.indexOf(LOOP_END_KEY) != -1)
					continue;

				if (line.indexOf(LOOP_START_KEY) != -1)
					continue;

				if (line.length() > 0) {

					// Neue Parameterzuweisung?
					boolean foundParam = false;
					for (String param : registry.getKeywordNames()) {
						String toFind = PARAMETER_KEY + param;
						final int pos = line.indexOf(toFind);
						if (pos != -1) {

							// alten parameter ausführen
							if (sb != null && currentKey != null) {
								IPreParserKeyword keyword = registry.createKeywordExecutor(currentKey);
								statements.add(new PreParserStatement(currentKey, keyword, sb.toString()));
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
						throw new OdysseusScriptParseException("No key set in line " + (currentLine + 1));
					sb.append("\n").append(line.trim());
				}
			}

			// Last query
			if (sb != null && currentKey != null) {
				IPreParserKeyword keyword = registry.createKeywordExecutor(currentKey);
				statements.add(new PreParserStatement(currentKey, keyword, sb.toString()));
			}

			return statements;
		} catch (OdysseusScriptParseException ex) {
			throw new OdysseusScriptParseException("[Line " + (currentLine + 1) + "]" + ex.getMessage(), ex);
		}
	}

	private String[] rewriteLoop(String[] textToParse) throws OdysseusScriptParseException {
		List<String> text = new ArrayList<String>();
		int from = -1;
		int to = -1;
		for (int linenr = 0; linenr < textToParse.length; linenr++) {
			String line = textToParse[linenr].trim();
			if (line.indexOf(LOOP_START_KEY) != -1) {
				if (from != -1) {
					throw new OdysseusScriptParseException("Nested loops are not allowed!");
				}
				from = linenr;
				continue;
			}
			if (line.indexOf(LOOP_END_KEY) != -1) {
				if (from == -1) {
					throw new OdysseusScriptParseException("Missing start loop statement");
				}
				if (to != -1) {
					throw new OdysseusScriptParseException("Nested loops are not allowed");
				}
				to = linenr;
			}
			if (from != -1 && to != -1) {
				String loopDef = textToParse[from].replaceFirst(LOOP_START_KEY, "").trim();
				try {
					String[] parts = loopDef.split(" ");
					if(parts.length!=4){
						throw new OdysseusScriptParseException("Missing parameters in loop definition. Definition should be like \"variable FROM 1 TO 10\"");
					}
					String variable = parts[0].trim();
					int startCount = Integer.parseInt(parts[1].trim());					
					int endCount = Integer.parseInt(parts[3].trim());					
					
					for (int counter = startCount; counter < endCount; counter++) {
						for (int i = from + 1; i < to; i++) {
							String toChange = textToParse[i];
							
							toChange=toChange.replaceAll(Pattern.quote(REPLACEMENT_START_KEY+variable+REPLACEMENT_END_KEY), Integer.toString(counter));							
							text.add(toChange);
						}
					}
				} catch (NumberFormatException e) {
					throw new OdysseusScriptParseException("Definition of loop is wrong. No count found.");
				}

				from = -1;
				to = -1;
			}else{
				if(from==-1){
					text.add(line);
				}
			}

		}
		if (from != -1 || to != -1) {
			throw new OdysseusScriptParseException("Loop has missing start or end!");
		}		
		
//		System.out.println("------------------");
//		for(String s: text){
//			System.out.println(s);
//		}
//		System.out.println("------------------");
		return text.toArray(new String[0]);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser#getReplacements(java.lang.String)
	 */
	@Override
	public Map<String, String> getReplacements(String text) throws OdysseusScriptParseException {
		return getReplacements(splitToList(text).toArray(new String[0]));
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser#getReplacements(java.lang.String[])
	 */
	@Override
	public Map<String, String> getReplacements(String[] text) throws OdysseusScriptParseException {
		Map<String, String> repl = new HashMap<String, String>();
		for (String line : text) {
			String correctLine = removeComments(line).trim();
			String replacedLine = useReplacements(correctLine, repl);
			final int pos = replacedLine.indexOf(PARAMETER_KEY + REPLACEMENT_DEFINITION_KEY);
			if (pos != -1) {
				String[] parts = replacedLine.split(" |\t", 3);
				// parts[0] is #DEFINE
				repl.put(parts[1].trim(), parts[2].trim());
			}
			final int posLoop = replacedLine.indexOf(LOOP_START_KEY);
			if(posLoop!= -1){
				String[] parts = replacedLine.split(" |\t", 3);
				repl.put(parts[1].trim(), parts[1].trim());
			}
		}
		return repl;
	}

	protected String useReplacements(String line, Map<String, String> replacements) throws OdysseusScriptParseException {
		int posStart = line.indexOf(REPLACEMENT_START_KEY);
		while (posStart != -1) {
			int posEnd = posStart + 1 + line.substring(posStart + 1).indexOf(REPLACEMENT_END_KEY);
			if (posEnd != -1 && posStart < posEnd) {
				String key = line.substring(posStart + REPLACEMENT_START_KEY.length(), posEnd);
				if (replacements.containsKey(key)) {
					line = line.replace(REPLACEMENT_START_KEY + key + REPLACEMENT_END_KEY, replacements.get(key));
				} else {								
					throw new OdysseusScriptParseException("Replacer " + key + " not defined ");
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
	
	// --------------------------------------------
	// Umstellung auf OSGi declarative Services
	// --------------------------------------------
	
	public void addKeywordProvider(IPreParserKeywordProvider provider){
		Map<String, Class<? extends IPreParserKeyword>> keywords = provider.getKeywords();
		for (Entry<String, Class<? extends IPreParserKeyword>> entry : keywords.entrySet()){
			registry.addKeyword(entry.getKey(), entry.getValue());
			//System.out.println("Added Preparser-Keyword "+entry.getKey());
		}
	}
	
	public void removeKeywordProvider(IPreParserKeywordProvider provider){
		Map<String, Class<? extends IPreParserKeyword>> keywords = provider.getKeywords();
		for (Entry<String, Class<? extends IPreParserKeyword>> entry : keywords.entrySet()){
			registry.removeKeyword(entry.getKey());
		}
	}

}
