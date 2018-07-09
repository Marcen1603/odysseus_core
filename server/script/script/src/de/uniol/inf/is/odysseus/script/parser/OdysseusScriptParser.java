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

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISink;
import de.uniol.inf.is.odysseus.core.procedure.StoredProcedure;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.planmanagement.IQueryParser;
import de.uniol.inf.is.odysseus.core.server.planmanagement.QueryParseException;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.IServerExecutor;
import de.uniol.inf.is.odysseus.core.server.planmanagement.executor.command.IExecutorCommand;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.script.parser.keyword.ResumeOnErrorPreParserKeyword;

public class OdysseusScriptParser implements IOdysseusScriptParser, IQueryParser {

	private static final Logger LOG = LoggerFactory.getLogger(OdysseusScriptParser.class);

	private static final PreParserKeywordRegistry KEYWORD_REGISTRY = new PreParserKeywordRegistry();
	public static final String PARSER_NAME = "OdysseusScript";

	public static final String PARAMETER_KEY = "#";

	public static final String REPLACEMENT_DEFINITION_KEY = "DEFINE";
	public static final String REPLACEMENT_START_KEY = "${";
	public static final String REPLACEMENT_END_KEY = "}";

	public static final String LOOP_START_KEY = "LOOP";
	public static final String LOOP_END_KEY = "ENDLOOP";
	public static final String LOOP_UPTO = "UPTO";
	public static final String LOOP_FOREACH_IN = "FOREACH_IN";

	public static final String STORED_PROCEDURE_PROCEDURE = "PROCEDURE";
	public static final String STORED_PROCEDURE_BEGIN = "BEGIN";
	public static final String STORED_PROCEDURE_END = "END";
	public static final String EXECUTE = "EXECUTE";
	public static final String DROPPROCEDURE = "DROPPROCEDURE";

	public static final String SINGLE_LINE_COMMENT_KEY = "///";

	private int currentLine;
	private int keyStartedAtLine;

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
		strings.add(LOOP_FOREACH_IN);
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
		strings.add(IfController.IF_KEY);
		strings.add(IfController.PRINT_KEY);
		strings.add("EVAL");
		strings.addAll(InputStatementParser.INPUT_KEYS);
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
	public List<IExecutorCommand> parseAndExecute(String completeText, ISession caller, ISink<?> defaultSink, Context context, IServerExecutor executor) throws OdysseusScriptException {
		return execute(parseScript(completeText, caller, context, executor), caller, defaultSink, executor);
	}

	@Override
	public List<IExecutorCommand> parseAndExecute(String[] textLines, ISession caller, ISink<?> defaultSink, Context context, IServerExecutor executor) throws OdysseusScriptException {
		return execute(parseScript(textLines, caller, context, executor), caller, defaultSink, executor);
	}

	@Override
	public List<IExecutorCommand> execute(List<PreParserStatement> statements, ISession caller, ISink<?> defaultSink, IServerExecutor executor) throws OdysseusScriptException {
		validate(statements, caller, defaultSink, executor);

		Map<String, Object> variables = prepareVariables(defaultSink);
		List<IExecutorCommand> results = Lists.newArrayList();
		for (PreParserStatement stmt : statements) {
			try {
				List<IExecutorCommand> commands = stmt.execute(variables, caller, this, executor);
				if (commands != null) {
					results.addAll(commands);
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

	private void validate(List<PreParserStatement> statements, ISession caller, ISink<?> defaultSink, IServerExecutor executor) throws OdysseusScriptException {
		Map<String, Object> variables = prepareVariables(defaultSink);
		for (PreParserStatement stmt : statements) {
			stmt.validate(variables, caller, this, executor);
		}
	}

	@Override
	public void validate(String[] lines, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		validate(parseScript(lines, caller, context, executor), caller, null, executor);
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
	public List<PreParserStatement> parseScript(String completeText, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {
		List<String> lines = null;
		lines = splitToList(completeText);
		return parseScript(lines.toArray(new String[lines.size()]), caller, context, executor);
	}

	@Override
	public synchronized List<PreParserStatement> parseScript(String[] script, ISession caller, Context context, IServerExecutor executor) throws OdysseusScriptException {

		String[] textToParse = Arrays.copyOf(script, script.length);
		List<PreParserStatement> statements = new LinkedList<PreParserStatement>();
		try {
			resetDefaultReplacements();
			ReplacementContainer replacements = new ReplacementContainer(ReplacementProviderManager.generateProviderMap());
			replacements.connect(context.copy());

			InputStatementParser inputParser = new InputStatementParser(textToParse, replacements);
			textToParse = inputParser.unwrap();

			textToParse = removeAllComments(textToParse);

			// first, we rewrite loops to serial query text
			String[] text = rewriteLoop(textToParse, context, caller);

			// after that, we are looking for procedures and replace them
			text = runProcedures(text, caller, executor);

			IfController ifController = new IfController(text, caller);
			StringBuffer sb = null;

			String currentKey = null;
			boolean isInProcedure = false;
			StringBuilder procedureLines = new StringBuilder();
			keyStartedAtLine = 1;
			for (currentLine = 0; currentLine < text.length; currentLine++) {
				String line = text[currentLine].trim();

				// check if we are an executable line
				if (!ifController.canExecuteNextLine(replacements)) {
					continue;
				}

				line = line.trim();
				// use replacements if we are not in procedure
				if (!isInProcedure) {
					line = replacements.use(line);
				}
				// dropping procedures was already done
				if (line.indexOf(PARAMETER_KEY + DROPPROCEDURE) != -1) {
					String dropkey = PARAMETER_KEY + DROPPROCEDURE;
					String name = line.substring(dropkey.length()).trim();
					IPreParserKeyword keyword = KEYWORD_REGISTRY.createKeywordExecutor(DROPPROCEDURE);
					statements.add(new PreParserStatement(DROPPROCEDURE, keyword, name, keyStartedAtLine, replacements.getCurrentContext()));
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
						statements.add(new PreParserStatement(STORED_PROCEDURE_PROCEDURE, keyword, procedureLines.toString(), keyStartedAtLine, replacements.getCurrentContext()));
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
										statements.add(new PreParserStatement(currentKey, keyword, sb.toString(), keyStartedAtLine, replacements.getCurrentContext()));
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
								if (replacements.parse(line)) {
									continue;
								}

								if (line.indexOf(PARAMETER_KEY + LOOP_END_KEY) != -1) {
									continue;
								}

								if (line.indexOf(PARAMETER_KEY + LOOP_START_KEY) != -1) {
									continue;
								}

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
				statements.add(new PreParserStatement(currentKey, keyword, sb.toString(), keyStartedAtLine, replacements.getNowContext()));
				keyStartedAtLine = currentLine + 1;
			}

			ifController.checkState();

			return statements;
		} catch (OdysseusScriptException | ReplacementException ex) {
			throw new OdysseusScriptException("[Line " + (currentLine + 1) + "]" + ex.getMessage(), ex);
		}
	}

	private static String[] removeAllComments(String[] textToParse) {
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
		setReplacement("OS.ARCH", System.getProperty("os.arch") + "");
		setReplacement("OS.VERSION", System.getProperty("os.version") + "");
		setReplacement("OS.NAME", System.getProperty("os.name") + "");
		setReplacement("VM.VERSION", System.getProperty("java.vm.version") + "");
		setReplacement("VM.VENDOR", System.getProperty("java.vm.vendor") + "");
		setReplacement("VM.NAME", System.getProperty("java.vm.name") + "");
		Runtime rt = Runtime.getRuntime();
		setReplacement("MEM", rt.totalMemory() + "");
		setReplacement("CPU", rt.availableProcessors() + "");
	}

	@Override
	public void setReplacement(String key, String value) {
		ReplacementContainer.addDefault(key, value);
	}

	private String[] runProcedures(String[] text, ISession caller, IServerExecutor executor) throws OdysseusScriptException {
		List<String> lines = new ArrayList<>();
		for (String line : text) {
			if (line.indexOf(PARAMETER_KEY + EXECUTE) != -1) {
				lines.addAll(runProcedure(line, caller, executor));
			} else {
				lines.add(line);
			}
		}
		return lines.toArray(new String[0]);
	}

	private List<String> runProcedure(String line, ISession caller, IServerExecutor executor) throws OdysseusScriptException {
		String key = PARAMETER_KEY + EXECUTE;
		int toCut = line.indexOf(key) + key.length();
		String head = line.substring(toCut).trim();
		int endNamePos = head.indexOf("(");
		if (endNamePos == -1) {
			throw new OdysseusScriptException("There is no parameter definition for stored procedure. Missing \"(\"");
		}
		String name = head.substring(0, endNamePos).trim();

		StoredProcedure proc = executor.getStoredProcedure(name, caller);

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

	private String[] rewriteLoop(String[] textToParse, Context context, ISession caller) throws OdysseusScriptException {

		List<String> text = new ArrayList<String>();

		ReplacementContainer replacements = new ReplacementContainer(ReplacementProviderManager.generateProviderMap());
		replacements.connect(context.copy());
		IfController ifController = new IfController(textToParse, caller);
		
		int from = -1;
		int to = -1;

		for (int linenr = 0; linenr < textToParse.length; linenr++) {
			String line = textToParse[linenr].trim();
			
			if( !ifController.canExecuteNextLine(replacements)) {
				continue;
			}	
			replacements.parse(line);
			
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
					if(loopDef.contains(LOOP_UPTO)) {
						rewriteLoopWithUpto(loopDef, text, replacements, from, to, textToParse);
					} else if(loopDef.contains(LOOP_FOREACH_IN)) {
						rewriteLoopWithForeachIn(loopDef, text, replacements, from, to, textToParse);
					}
					
				} catch (NumberFormatException | ReplacementException e) {
					throw new OdysseusScriptException("Definition of loop is wrong.", e);
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

		return text.toArray(new String[0]);
	}

//	private Map<String, Serializable> getReplacements(String[] text, Context context) throws OdysseusScriptException {
//		ReplacementContainer repl = new ReplacementContainer(ReplacementProviderManager.generateProviderMap());
//		repl.connect(context);
//		boolean isInProcedure = false;
//
//		try {
//			for (String line : text) {
//
//				String correctLine = removeComments(line).trim();
//				// maybe, we have replacements within definitions...
//				if (line.indexOf(PARAMETER_KEY + STORED_PROCEDURE_PROCEDURE) != -1) {
//					isInProcedure = true;
//					continue;
//				}
//				if (isInProcedure) {
//					if (line.indexOf(STORED_PROCEDURE_END) != -1) {
//						isInProcedure = false;
//					}
//					continue;
//				}
//				String replacedLine = repl.use(correctLine);
//				repl.parse(correctLine);
//
//				final int posLoop = replacedLine.indexOf(PARAMETER_KEY + LOOP_START_KEY);
//				if (posLoop != -1) {
//					String[] parts = replacedLine.split(" |\t");
//					String key = parts[1].trim();
//					repl.put(key.toUpperCase(), key);
//
//					if (parts.length > 6) {
//						repl.put(parts[6].trim().toUpperCase(), parts[6].trim());
//					}
//				}
//			}
//
//			return repl.toMap();
//		} catch (ReplacementException ex) {
//			throw new OdysseusScriptException(ex);
//		}
//
//	}

	/**
	 * @param loopDef
	 * @param text
	 * @param replacements
	 * @param from
	 * @param to
	 * @param textToParse
	 * @throws OdysseusScriptException 
	 * @throws ReplacementException 
	 * @throws NumberFormatException 
	 */
	private void rewriteLoopWithForeachIn(String loopDef, List<String> text, ReplacementContainer replacements,
			int from, int to, String[] textToParse) throws OdysseusScriptException, NumberFormatException, ReplacementException {
		String[] parts = loopDef.split(" ", 3);
		if (parts.length < 3) {
			throw new OdysseusScriptException(
					"Missing parameters in loop definition. Definition should be like \"variable " + LOOP_FOREACH_IN
							+ " 1,2,...,9,10,20,...,60,90,120\"");
		}
		String variable = parts[0].trim();
		String[] valuesStr = replacements.use(parts[2].trim()).split(",");
		List<String> values = new ArrayList<>();
		for (int i = 0; i < valuesStr.length; ++i) {
			if (valuesStr[i].trim().equals("...")) {
				if (i < 2 || i == valuesStr.length - 1) {
					throw new OdysseusScriptException("Wrong '...' pattern in LOOP definition. '...' occures at position " + i + " but needs two preceding and one subsequent numbers, e.g.: 2,4,..,8");
				}
				int startA = Integer.parseInt(replacements.use(valuesStr[i-2].trim()));
				int end = Integer.parseInt(replacements.use(valuesStr[i+1].trim()));
				int startB = Integer.parseInt(replacements.use(valuesStr[i-1].trim()));
				int step = startB - startA;
				for(int j = startB + step; j < end; j += step) {
					values.add(String.valueOf(j));
				}
			} else {
				values.add(replacements.use(valuesStr[i].trim()));
			}
		}
		LOG.debug("Values in foreach loop: {}", values);
		for (int i = 0; i < values.size(); i++) {
			for (int j = from + 1; j < to; j++) {
				String toChange = textToParse[j];
				String val = values.get(i);
				// replace ${i}
				toChange = toChange.replaceAll(Pattern.quote(REPLACEMENT_START_KEY + variable + REPLACEMENT_END_KEY), val);
				// replace ${i-1}
				if(i > 0) {
					toChange = toChange.replaceAll(Pattern.quote(REPLACEMENT_START_KEY + variable + "-1" + REPLACEMENT_END_KEY), values.get(i-1));
				}
				// replace ${i+1}
				if(i < values.size() - 1) {
					toChange = toChange.replaceAll(Pattern.quote(REPLACEMENT_START_KEY + variable + "+1" + REPLACEMENT_END_KEY), values.get(i+1));
				}
				// replace #IF i
				if(toChange.trim().startsWith(IfController.PARAMETER_KEY + IfController.IF_KEY)) {
					toChange = toChange.replaceAll(" " + variable + " ", " " + val + " ");
				}
				text.add(toChange);
			}
		}
	}

	/**
	 * @param text 
	 * @param replacements 
	 * @param to 
	 * @param from 
	 * @param textToParse 
	 * @throws OdysseusScriptException 
	 * @throws ReplacementException 
	 * 
	 */
	private void rewriteLoopWithUpto(String loopDef, List<String> text, ReplacementContainer replacements, int from, int to, String[] textToParse) throws OdysseusScriptException, ReplacementException {
		String[] parts = loopDef.split(" ");
		if (parts.length < 4) {
			throw new OdysseusScriptException("Missing parameters in loop definition. Definition should be like \"variable 1 " + LOOP_UPTO + " 10\"");
		}
//		Map<String, Serializable> repl = getReplacements(Arrays.copyOf(textToParse, from - 1), context);
		String variable = parts[0].trim();
		String fromStr = replacements.use(parts[1].trim());
		String toStr = replacements.use(parts[3].trim());

		String offsetVariable = "";
		int offsetValue = 0;
		if (parts.length > 4) {
			if (parts.length != 7) {
				throw new OdysseusScriptException("Missing parameters in loop definition. Definition should be like \"variable 1 " + LOOP_UPTO + " 10 WITH offset 5\"");
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
				// replace #IF i
				if(toChange.trim().startsWith(IfController.PARAMETER_KEY + IfController.IF_KEY)) {
					toChange = toChange.replaceAll(" " + variable + " ", " " + Integer.toString(counter) + " ");
				}
				if (!offsetVariable.isEmpty()) {
					// replace ${i+1}
					toChange = toChange.replaceAll(Pattern.quote(REPLACEMENT_START_KEY + offsetVariable + REPLACEMENT_END_KEY), Integer.toString(counter + offsetValue));
				}
				text.add(toChange);
			}
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

	private static String removeComments(String line) {
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

	// called by OSGi-DS
	public void addKeywordProvider(IPreParserKeywordProvider provider) {
		Map<String, Class<? extends IPreParserKeyword>> keywords = provider.getKeywords();
		for (Entry<String, Class<? extends IPreParserKeyword>> entry : keywords.entrySet()) {
			KEYWORD_REGISTRY.addKeyword(entry.getKey(), entry.getValue());
			// System.out.println("Added Preparser-Keyword "+entry.getKey());
		}
	}

	// called by OSGi-DS
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
		return PARSER_NAME;
	}

	@Override
	public List<IExecutorCommand> parse(String query, ISession user, IDataDictionary dd, Context context, IMetaAttribute metaAttribute, IServerExecutor executor) throws QueryParseException {
		List<IExecutorCommand> executorCommands = new ArrayList<>();
		try {
			List<?> results = parseAndExecute(query, user, null, context, executor);
			for (Object result : results) {
				if (result instanceof IExecutorCommand) {
					executorCommands.add((IExecutorCommand) result);
				}
			}
		} catch (OdysseusScriptException e) {
			processScriptException(e);
		}
		return executorCommands;
	}

	private static void processScriptException(OdysseusScriptException e) {
		if (e.getFailedStatement() != null) {
			StringBuffer message = new StringBuffer("Odysseus Script error in statement " + e.getFailedStatement().getKeywordText() + " in line " + e.getFailedStatement().getLine());
			if (e.getCause() instanceof QueryParseException) {
				QueryParseException qpe = (QueryParseException) e.getCause();
				if (qpe.getCause() != null && qpe.getCause() instanceof QueryParseException) {
					qpe = (QueryParseException) qpe.getCause();
				}
				message.append("\n").append(qpe.getMessage());
				int line = qpe.getLine() + e.getFailedStatement().getLine() + 2;
				int column = qpe.getColumn();
				throw new QueryParseException(message.toString(), e, line, column);
			}
		}
		if (e.getCause() instanceof OdysseusScriptException) {
			StringBuffer message = new StringBuffer("Parsing Odysseus script failed:" + e.getMessage());
			message.append("\n").append(e.getCause().getMessage());
			throw new QueryParseException(message.toString(), e.getCause());
		}
		throw new QueryParseException("Parsing Odysseus script failed:" + e.getMessage(), e);
	}

	@Override
	public Map<String, List<String>> getTokens(ISession user) {
		Map<String, List<String>> tokens = new HashMap<>();
		List<String> staticKeywords = new ArrayList<>(getStaticWords());
		tokens.put("STATIC", staticKeywords);
		tokens.put("KEYWORDS", new ArrayList<>(getKeywordNames()));
		tokens.put("DEPRECATED", getDeprecated());

		return tokens;
	}

	private List<String> getDeprecated() {
		List<String> deprecated = new ArrayList<>();
		for (String key : getKeywordNames()) {
			if (getPreParserKeywordRegistry().isDeprecated(key)) {
				deprecated.add(key);
			}
		}
		return deprecated;
	}

	@Override
	public List<String> getSuggestions(String hint, ISession user) {
		if (getPreParserKeywordRegistry().existsKeyword(hint)) {
			IPreParserKeyword keyword = getPreParserKeywordRegistry().createKeywordExecutor(hint);
			return new ArrayList<>(keyword.getAllowedParameters(user));
		}
		return new ArrayList<>();
	}

}