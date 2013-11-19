/********************************************************************************** 
 * Copyright 2011 The Odysseus Team
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
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.activator.Activator;
import de.uniol.inf.is.odysseus.script.parser.keyword.ResumeOnErrorPreParserKeyword;

public class OdysseusScriptParser implements IOdysseusScriptParser, IQueryParser {
	private static final Logger LOG = LoggerFactory.getLogger(OdysseusScriptParser.class);
	private static final PreParserKeywordRegistry KEYWORD_REGISTRY = new PreParserKeywordRegistry();

	public static final String PARAMETER_KEY = "#";

	public static final String REPLACEMENT_DEFINITION_KEY = "DEFINE";
	public static final String REPLACEMENT_START_KEY = "${";
	public static final String REPLACEMENT_END_KEY = "}";

	public static final String LOOP_START_KEY = "LOOP";
	public static final String LOOP_END_KEY = "ENDLOOP";
	public static final String LOOP_UPTO = "UPTO";

	public static final String STORED_PROCEDURE_PROCEDURE = "PROCEDURE";
	public static final String STORED_PROCEDURE_BEGIN = "BEGIN";
	public static final String STORED_PROCEDURE_END = "END";
	public static final String EXECUTE = "EXECUTE";
	public static final String DROPPROCEDURE = "DROPPROCEDURE";

	public static final String SINGLE_LINE_COMMENT_KEY = "///";

	private int currentLine;
	private int keyStartedAtLine;

	private Map<String, String> defaultReplacements = new HashMap<>();

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

	@Override
	public Set<String> getStaticWords() {
		Set<String> strings = Sets.newHashSet();
		strings.add(REPLACEMENT_DEFINITION_KEY);
		strings.add(LOOP_END_KEY);
		strings.add(LOOP_START_KEY);
		strings.add(LOOP_UPTO);
		strings.add(STORED_PROCEDURE_BEGIN);
		strings.add(STORED_PROCEDURE_END);
		strings.add(STORED_PROCEDURE_PROCEDURE);
		strings.add(DROPPROCEDURE);
		strings.add(EXECUTE);
		strings.add(IfController.IFDEF_KEY);
		strings.add(IfController.ELSE_KEY);
		strings.add(IfController.ENDIF_KEY);
		strings.add(IfController.IFNDEF_KEY);
		strings.add(IfController.UNDEF_KEY);
		strings.add(IfController.SRCDEF_KEY);
		strings.add(IfController.SRCNDEF_KEY);
		return strings;
	}

	@Override
	public Set<String> getKeywordNames() {
		Set<String> keywordNames = Sets.newHashSet();
		keywordNames.addAll(KEYWORD_REGISTRY.getKeywordNames());
		keywordNames.addAll(getStaticWords());
		return keywordNames;
	}

	@Override
	public List<?> parseAndExecute(String completeText, ISession caller, ISink<?> defaultSink) throws OdysseusScriptException {
		return execute(parseScript(completeText, caller), caller, defaultSink);
	}

	@Override
	public List<?> parseAndExecute(String[] textLines, ISession caller, ISink<?> defaultSink) throws OdysseusScriptException {
		return execute(parseScript(textLines, caller), caller, defaultSink);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<?> execute(List<PreParserStatement> statements, ISession caller, ISink<?> defaultSink) throws OdysseusScriptException {

		validate(statements, caller, defaultSink);

		Map<String, Object> variables = prepareVariables(defaultSink);
		List results = Lists.newArrayList();
		for (PreParserStatement stmt : statements) {
			try {
				Optional<?> optionalResult = stmt.execute(variables, caller, this);
				if (optionalResult.isPresent()) {
					results.add(optionalResult.get());
				}
			} catch (OdysseusScriptException ex) {
				if (isResumeOnError(variables)) {
					LOG.error("Caught exception during executing script, but resumed execution", ex);
				} else {
					ex.setFailedStatement(stmt);
					throw ex;
				}
			}

		}

		return results;
	}

	private void validate(List<PreParserStatement> statements, ISession caller, ISink<?> defaultSink) throws OdysseusScriptException {
		Map<String, Object> variables = prepareVariables(defaultSink);
		for (PreParserStatement stmt : statements) {
			stmt.validate(variables, caller, this);
		}
	}

	@Override
	public void validate(String[] lines, ISession caller) throws OdysseusScriptException {
		validate(parseScript(lines, caller), caller, null);
	}

	private static Boolean isResumeOnError(Map<String, Object> variables) {
		try {
			Object o = variables.get(ResumeOnErrorPreParserKeyword.RESUME_ON_ERROR_FLAG);
			Boolean val = (Boolean) o; 
			return val != null ? val : false;
		} catch (Throwable t) {
			LOG.error("Exception during determining if resume on error was set", t);
			return false;
		}
	}

	private static Map<String, Object> prepareVariables(ISink<?> defaultSink) {
		Map<String, Object> variables = new HashMap<String, Object>();
		if (defaultSink != null) {
			variables.put("_defaultSink", defaultSink);
		}
		variables.put(ResumeOnErrorPreParserKeyword.RESUME_ON_ERROR_FLAG, false);
		return variables;
	}

	@Override
	public List<PreParserStatement> parseScript(String completeText, ISession caller) throws OdysseusScriptException {
		List<String> lines = null;
		lines = splitToList(completeText);
		return parseScript(lines.toArray(new String[lines.size()]), caller);
	}

	@Override
	public List<PreParserStatement> parseScript(String[] textToParse, ISession caller) throws OdysseusScriptException {

		List<PreParserStatement> statements = new LinkedList<PreParserStatement>();
		try {
			resetDefaultReplacements();

			textToParse = removeAllComments(textToParse);

			// first, we rewrite loops to serial query text
			String[] text = rewriteLoop(textToParse);

			// after that, we are looking for procedures and replace them
			text = runProcedures(text, caller);

			// initialize the replacement using the defaults
			Map<String, String> replacements = new HashMap<String, String>(this.defaultReplacements);

			IfController ifController = new IfController(text, caller);
			StringBuffer sb = null;

			String currentKey = null;
			boolean isInProcedure = false;
			StringBuilder procedureLines = new StringBuilder();
			keyStartedAtLine = 1;
			for (currentLine = 0; currentLine < text.length; currentLine++) {
				String line = text[currentLine].trim();

				// check if we are an executable line
				if (!ifController.canExecuteNextLine()) {
					continue;
				}

				line = line.trim();
				// use replacements if we are not in procedure
				if (!isInProcedure) {
					// first, we use replacements we know so far
					line = useReplacements(line, replacements).trim();
					// if there is a define-key for replacements, we add this to
					// the replacements
					if (line.indexOf(PARAMETER_KEY + REPLACEMENT_DEFINITION_KEY) != -1) {
						String[] parts = line.split(" |\t", 3);
						// parts[0] is #DEFINE
						// parts[1] is replacement name
						// parts[2] is replacement value (optional!)
						if (parts.length >= 3) {
							replacements.put(parts[1].trim().toUpperCase(), parts[2].trim());
						}
						continue;
					}

					// same with loop-definition lines: jump to next line,
					// because
					// we normally handled that before
					if (line.indexOf(PARAMETER_KEY + LOOP_END_KEY) != -1)
						continue;

					if (line.indexOf(PARAMETER_KEY + LOOP_START_KEY) != -1)
						continue;

					// If we find an UNDEFINE, we remove the replacement
					if (line.indexOf(PARAMETER_KEY + IfController.UNDEF_KEY) != -1) {
						String[] parts = line.trim().split(" |\t", 3);
						replacements.remove(parts[1].toUpperCase());
					}
				}
				// dropping procedures was already done
				if (line.indexOf(PARAMETER_KEY + DROPPROCEDURE) != -1) {
					String dropkey = PARAMETER_KEY + DROPPROCEDURE;
					String name = line.substring(dropkey.length()).trim();
					IPreParserKeyword keyword = KEYWORD_REGISTRY.createKeywordExecutor(DROPPROCEDURE);
					statements.add(new PreParserStatement(DROPPROCEDURE, keyword, name, keyStartedAtLine));
					keyStartedAtLine = currentLine + 1;
					continue;
				}

				// If we find an STORED_PROCEDURE_PROCEDURE, we omit all other
				// #KEYWORDS except of END
				if (line.indexOf(PARAMETER_KEY + STORED_PROCEDURE_PROCEDURE) != -1) {
					isInProcedure = true;
					procedureLines = new StringBuilder();
					procedureLines.append(line);
					continue;
				}

				// so, if we are in a procedure, check if we reached the END
				if (isInProcedure) {
					procedureLines.append(System.lineSeparator());
					procedureLines.append(line);
					if (line.indexOf(STORED_PROCEDURE_END) != -1) {
						// and put all into the keyword
						IPreParserKeyword keyword = KEYWORD_REGISTRY.createKeywordExecutor(STORED_PROCEDURE_PROCEDURE);
						statements.add(new PreParserStatement(STORED_PROCEDURE_PROCEDURE, keyword, procedureLines.toString(), keyStartedAtLine));
						keyStartedAtLine = currentLine + 1;
						isInProcedure = false;
					}
					// and we omit procedure-lines
					continue;
				}

				// else we handle the parameters (those starting with #)
				if (line.length() > 0) {

					// is this a line that starts a new parameter?
					if (line.startsWith(PARAMETER_KEY)) {

						// are we still in procedure declaration --> do not
						// search for end!
						if (!isInProcedure) {
							boolean foundParam = false;
							for (String param : KEYWORD_REGISTRY.getKeywordNames()) {
								String toFind = PARAMETER_KEY + param;
								final int pos = line.indexOf(toFind);
								if (pos != -1) {

									// yes, we have a parameter, if there was
									// one before: build this parameter and use
									// all
									// that we have seen until now
									if (sb != null && currentKey != null) {
										IPreParserKeyword keyword = KEYWORD_REGISTRY.createKeywordExecutor(currentKey);
										statements.add(new PreParserStatement(currentKey, keyword, sb.toString(), keyStartedAtLine));
										keyStartedAtLine = currentLine + 1;
									}

									// set values to find our new parameter
									// (that starts at this line)
									sb = new StringBuffer();
									sb.append(line.substring(pos + param.length() + 1).trim());
									currentKey = param;
									foundParam = true;
									break;
								}
							}

							if (!foundParam) {
								throw new OdysseusScriptException("Undefined key '" + line.substring(1) + "'");
							}
						}

					} else {
						if (sb == null) {
							throw new OdysseusScriptException("No key set in line " + (currentLine + 1));
						}
						sb.append("\n").append(line.trim());
					}

				}
			}

			// we reached the last line, so we need to wrap up the last found
			// statement/parameter
			if (sb != null && currentKey != null) {
				IPreParserKeyword keyword = KEYWORD_REGISTRY.createKeywordExecutor(currentKey);
				statements.add(new PreParserStatement(currentKey, keyword, sb.toString(), keyStartedAtLine));
				keyStartedAtLine = currentLine + 1;
			}

			return statements;
		} catch (OdysseusScriptException ex) {
			throw new OdysseusScriptException("[Line " + (currentLine + 1) + "]" + ex.getMessage(), ex);
		}
	}

	private String[] removeAllComments(String[] textToParse) {
		List<String> resultText = Lists.newArrayList();
		for (String line : textToParse) {
			String result = removeComments(line);
			if (result != null) {
				result = result.trim();
			}

			if (!Strings.isNullOrEmpty(result)) {
				resultText.add(result);
			}
		}
		return resultText.toArray(new String[resultText.size()]);
	}

	private void resetDefaultReplacements() {
		setReplacement("NOW", System.currentTimeMillis() + "");
	}

	@Override
	public void setReplacement(String key, String value) {
		defaultReplacements.put(key, value);
	}

	private String[] runProcedures(String[] text, ISession caller) throws OdysseusScriptException {
		List<String> lines = new ArrayList<>();
		for (String line : text) {
			if (line.indexOf(PARAMETER_KEY + EXECUTE) != -1) {
				lines.addAll(runProcedure(line, caller));
			} else {
				lines.add(line);
			}
		}
		return lines.toArray(new String[0]);
	}

	private List<String> runProcedure(String line, ISession caller) throws OdysseusScriptException {
		String key = PARAMETER_KEY + EXECUTE;
		int toCut = line.indexOf(key) + key.length();
		String head = line.substring(toCut).trim();
		int endNamePos = head.indexOf("(");
		if (endNamePos == -1) {
			throw new OdysseusScriptException("There is no parameter definition for stored procedure. Missing \"(\"");
		}
		String name = head.substring(0, endNamePos).trim();

		StoredProcedure proc = getExecutor().getStoredProcedure(name, caller);

		String procText = proc.getText();
		// check parameters
		int startParamPos = endNamePos + 1;
		int endParamPos = head.indexOf(")");
		String paramPart = head.substring(startParamPos, endParamPos);
		if (paramPart.trim().length() > 0) {
			String[] params = paramPart.split(",");
			if (params.length != proc.getVariables().size()) {
				throw new OdysseusScriptException("Stored procedure needs " + proc.getVariables().size() + " parameters!");
			}
			Map<String, String> replacements = new HashMap<>();
			for (int i = 0; i < params.length; i++) {
				replacements.put(proc.getVariables().get(i), params[i].trim());
			}
			// replace all
			procText = useReplacements(procText, replacements);
		} else {
			if (proc.getVariables().size() > 0) {
				throw new OdysseusScriptException("Stored procedure needs " + proc.getVariables().size() + " parameters!");
			}
		}

		return Arrays.asList(procText.split(System.lineSeparator()));
	}

	private String[] rewriteLoop(String[] textToParse) throws OdysseusScriptException {
		List<String> text = new ArrayList<String>();
		int from = -1;
		int to = -1;
		for (int linenr = 0; linenr < textToParse.length; linenr++) {
			String line = textToParse[linenr].trim();
			if (line.startsWith(SINGLE_LINE_COMMENT_KEY)) {
				text.add(line);
				continue;
			}
			if (line.indexOf(PARAMETER_KEY + LOOP_START_KEY) != -1) {
				if (from != -1) {
					throw new OdysseusScriptException("Nested loops are not allowed!");
				}
				from = linenr;
				continue;
			}
			if (line.indexOf(PARAMETER_KEY + LOOP_END_KEY) != -1) {
				if (from == -1) {
					throw new OdysseusScriptException("Missing start loop statement");
				}
				if (to != -1) {
					throw new OdysseusScriptException("Nested loops are not allowed");
				}
				to = linenr;
			}
			if (from != -1 && to != -1) {
				String loopDef = textToParse[from].replaceFirst(PARAMETER_KEY + LOOP_START_KEY, "").trim();
				try {					
					String[] parts = loopDef.split(" ");
					if (parts.length < 4) {
						throw new OdysseusScriptException("Missing parameters in loop definition. Definition should be like \"variable 1 TO 10\"");
					}
					Map<String, String> repl = getReplacements(Arrays.copyOf(textToParse, from - 1));
					String variable = parts[0].trim();
					String fromStr = parts[1].trim();
					if (fromStr.startsWith(REPLACEMENT_START_KEY) && fromStr.endsWith(REPLACEMENT_END_KEY)) {
						fromStr = repl.get(fromStr.substring(2, fromStr.length() - 1).toUpperCase());
					}
					String toStr = parts[3].trim();
					if (toStr.startsWith(REPLACEMENT_START_KEY) && toStr.endsWith(REPLACEMENT_END_KEY)) {
						toStr = repl.get(toStr.substring(2, toStr.length() - 1).toUpperCase());
					}
					
					String offsetVariable = "";
					int offsetValue = 0;
					if(parts.length>4){
						if(parts.length!=7){
							throw new OdysseusScriptException("Missing parameters in loop definition. Definition should be like \"variable 1 UPTO 10 WITH offset 5\"");
						}
						offsetVariable = parts[5];
						offsetValue = Integer.parseInt(parts[6]);
						
					}

					int startCount = Integer.parseInt(fromStr);
					int endCount = Integer.parseInt(toStr);

					for (int counter = startCount; counter <= endCount; counter++) {
						for (int i = from + 1; i < to; i++) {
							String toChange = textToParse[i];
							// replace ${i}
							toChange = toChange.replaceAll(Pattern.quote(REPLACEMENT_START_KEY + variable + REPLACEMENT_END_KEY), Integer.toString(counter));
							// replace ${i-1}
							toChange = toChange.replaceAll(Pattern.quote(REPLACEMENT_START_KEY + variable + "-1" + REPLACEMENT_END_KEY), Integer.toString(counter - 1));
							// replace ${i+1}
							toChange = toChange.replaceAll(Pattern.quote(REPLACEMENT_START_KEY + variable + "+1" + REPLACEMENT_END_KEY), Integer.toString(counter + 1));
							if(!offsetVariable.isEmpty()){
							// replace ${i+1}
								toChange = toChange.replaceAll(Pattern.quote(REPLACEMENT_START_KEY + offsetVariable + REPLACEMENT_END_KEY), Integer.toString(counter+offsetValue));
							}
							text.add(toChange);
						}
					}
				} catch (NumberFormatException e) {
					throw new OdysseusScriptException("Definition of loop is wrong. No count found.");
				}

				from = -1;
				to = -1;
			} else {
				if (from == -1) {
					text.add(line);
				}
			}

		}
		if (from != -1 || to != -1) {
			throw new OdysseusScriptException("Loop has missing start or end!");
		}

		// System.out.println("------------------");
		// for (String s : text) {
		// System.out.println(s);
		// }
		// System.out.println("------------------");
		return text.toArray(new String[0]);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser#getReplacements
	 * (java.lang.String)
	 */
	@Override
	public Map<String, String> getReplacements(String text) throws OdysseusScriptException {
		return getReplacements(splitToList(text).toArray(new String[0]));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.script.parser.IOdysseusScriptParser#getReplacements
	 * (java.lang.String[])
	 */
	@Override
	public Map<String, String> getReplacements(String[] text) throws OdysseusScriptException {
		Map<String, String> repl = new HashMap<String, String>();
		addDefaultReplacements(repl);
		boolean isInProcedure = false;

		for (String line : text) {

			String correctLine = removeComments(line).trim();
			// maybe, we have replacements within definitions...
			if (line.indexOf(PARAMETER_KEY + STORED_PROCEDURE_PROCEDURE) != -1) {
				isInProcedure = true;
				continue;
			}
			if (isInProcedure) {
				if (line.indexOf(STORED_PROCEDURE_END) != -1) {
					isInProcedure = false;
				}
				continue;
			}
			String replacedLine = useReplacements(correctLine, repl);
			final int pos = replacedLine.indexOf(PARAMETER_KEY + REPLACEMENT_DEFINITION_KEY);
			if (pos != -1) {
				String[] parts = replacedLine.split(" |\t", 3);
				// parts[0] is #DEFINE
				// parts[1] is replacement name
				// parts[2] is replacement value (optional!)
				if (parts.length >= 3) {
					repl.put(parts[1].trim().toUpperCase(), parts[2].trim());
				}
			}
			final int posLoop = replacedLine.indexOf(PARAMETER_KEY + LOOP_START_KEY);
			if (posLoop != -1) {
				String[] parts = replacedLine.split(" |\t");
				repl.put(parts[1].trim().toUpperCase(), parts[1].trim());
				if(parts.length>6){
					repl.put(parts[6].trim().toUpperCase(), parts[6].trim());
				}
			}
		}
		return repl;
	}

	private void addDefaultReplacements(Map<String, String> repl) {
		for (Entry<String, String> def : this.defaultReplacements.entrySet()) {
			repl.put(def.getKey().toUpperCase(), def.getValue());
		}
	}

	protected String useReplacements(String line, Map<String, String> replacements) throws OdysseusScriptException {
		// List<String> keys = findReplacements(line);

		int posStart = line.indexOf(REPLACEMENT_START_KEY);
		while (posStart != -1) {
			int posEnd = posStart + 1 + line.substring(posStart + 1).indexOf(REPLACEMENT_END_KEY);
			if (posEnd == posStart) {
				// end not found
				break;
			}
			if (posEnd != -1 && posStart < posEnd) {

				String key = line.substring(posStart + REPLACEMENT_START_KEY.length(), posEnd);
				String replacement = replacements.get(key.toUpperCase());
				if (replacement == null) {
					throw new OdysseusScriptException("Replacement key " + key + " not defined or has no value!");
				}
				line = line.substring(0, posStart) + replacement + line.substring(posEnd + REPLACEMENT_END_KEY.length());
			}
			int searchAt = posStart + REPLACEMENT_START_KEY.length();
			posStart = line.indexOf(REPLACEMENT_START_KEY, searchAt);
		}

		return line;
	}

	public static List<String> findReplacements(String line) {
		List<String> keys = new ArrayList<>();
		int posStart = line.indexOf(REPLACEMENT_START_KEY);
		while (posStart != -1) {
			int posEnd = posStart + 1 + line.substring(posStart + 1).indexOf(REPLACEMENT_END_KEY);
			if (posEnd == posStart) {
				// end not found
				break;
			}
			if (posEnd != -1 && posStart < posEnd) {
				String key = line.substring(posStart + REPLACEMENT_START_KEY.length(), posEnd);
				keys.add(key.toUpperCase());
			}
			int searchAt = posStart + REPLACEMENT_START_KEY.length();
			posStart = line.indexOf(REPLACEMENT_START_KEY, searchAt);
		}
		return keys;
	}

	protected String removeComments(String line) {
		if (line == null || line.length() == 0)
			return "";

		final int commentPos = line.indexOf(SINGLE_LINE_COMMENT_KEY);
		if (commentPos != -1) {
			if (commentPos == 0)
				return "";
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

	public void addKeywordProvider(IPreParserKeywordProvider provider) {
		Map<String, Class<? extends IPreParserKeyword>> keywords = provider.getKeywords();
		for (Entry<String, Class<? extends IPreParserKeyword>> entry : keywords.entrySet()) {
			KEYWORD_REGISTRY.addKeyword(entry.getKey(), entry.getValue());
			// System.out.println("Added Preparser-Keyword "+entry.getKey());
		}
	}

	public void removeKeywordProvider(IPreParserKeywordProvider provider) {
		Map<String, Class<? extends IPreParserKeyword>> keywords = provider.getKeywords();
		for (Entry<String, Class<? extends IPreParserKeyword>> entry : keywords.entrySet()) {
			KEYWORD_REGISTRY.removeKeyword(entry.getKey());
		}
	}

	@Override
	public PreParserKeywordRegistry getPreParserKeywordRegistry() {
		return KEYWORD_REGISTRY;
	}

	@Override
	public String getLanguage() {
		return "OdysseusScript";
	}

	@Override
	public List<IExecutorCommand> parse(String query, ISession user, IDataDictionary dd) throws QueryParseException {
		List<ILogicalQuery> queries = new ArrayList<ILogicalQuery>();
		try {
			List<?> commands = this.parseAndExecute(query, user, null);
			for (Object command : commands) {
				if (command instanceof ArrayList) {
					ArrayList<?> ids = (ArrayList<?>) command;
					for (Object id : ids) {
						int i = (Integer) id;
						queries.add(getExecutor().getLogicalQueryById(i));
					}
				}
			}
		} catch (OdysseusScriptException e) {
			if (e.getFailedStatement() != null) {
				StringBuffer message = new StringBuffer("Odysseus Script error in statement " + e.getFailedStatement().getKeywordText() + " in line " + e.getFailedStatement().getLine());
				if (e.getCause() instanceof QueryParseException) {
					QueryParseException qpe = (QueryParseException) e.getCause();
					if (qpe.getCause() != null && qpe.getCause() instanceof QueryParseException){
						qpe = (QueryParseException) qpe.getCause();
					}
					message.append("\n").append(qpe.getMessage());
					int line = qpe.getLine() + e.getFailedStatement().getLine() + 2; 
					int column = qpe.getColumn();
					throw new QueryParseException(message.toString(), e, line, column);
				}
				throw new QueryParseException(message.toString(), e);
			}
			throw new QueryParseException("Parsing Odysseus script failed with an unknown reason!", e);
		}
		return new ArrayList<IExecutorCommand>();
	}

	@Override
	public List<IExecutorCommand> parse(Reader reader, ISession user, IDataDictionary dd) throws QueryParseException {
		return new ArrayList<>();
	}

	@Override
	public IExecutor getExecutor() {
		return Activator.getExecutor();
	}

}
