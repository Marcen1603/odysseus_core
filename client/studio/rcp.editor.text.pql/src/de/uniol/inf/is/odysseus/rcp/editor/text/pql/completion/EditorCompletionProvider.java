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
package de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Point;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorInformation;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalParameterInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.ViewInformation;
import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.editor.text.completion.IEditorLanguagePropertiesProvider;
import de.uniol.inf.is.odysseus.rcp.editor.text.completion.Terminal;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.IOdysseusScriptFormattingStrategy;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.part.OperatorCompletionPart;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.part.ParameterCompletionPart;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.part.ParameterValueCompletionPart;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.completion.part.SourceCompletionPart;

/**
 * @author Dennis Geesen
 * 
 */
public class EditorCompletionProvider implements IEditorLanguagePropertiesProvider {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.editor.text.IEditorLanguagePropertiesProvider
	 * #getWords (java.lang.String, java.lang.String)
	 */
	@Override
	public List<ICompletionProposal> getCompletionSuggestions(String currentToken, String lastSplitters[], IExecutor executor, ISession session, IDocument document, int offset, Point selection) {
		List<ICompletionProposal> result = new ArrayList<>();
		currentToken = currentToken.trim().toLowerCase();
		String tokenbefore = "";
		if (lastSplitters.length > 0) {
			tokenbefore = lastSplitters[0].trim().toLowerCase();
		}

		// after comma it
		String prefix = getPrefix(currentToken);
		currentToken = currentToken.substring(0, currentToken.length() - prefix.length()).trim();
		if (tokenbefore.endsWith(",") && lastSplitters.length > 1) {
			// is an operator, if the one after the last splitter is a "}"
			if (lastSplitters[1].endsWith("}")) {
				String tokenAtOffset = getCompleteToken(document, offset);				
				// return all possible operators and sources...
				return buildCompletionProposalOperators(tokenAtOffset, document, offset, prefix);
			}
			// otherwise, we are maybe in a list
			if (inListParam(document, offset)) {
				LogicalOperatorInformation op = getOperatorAtPosition(document, offset);
				LogicalParameterInformation param = getParamAtPosition(document, offset, op);
				if (param != null) {
					// so we ask the parameter what we can insert here as the
					// list part --> the value of the
					result.addAll(ParameterValueCompletionPart.buildCompletionProposals(param, offset, document, ","));
				}
			}
			// or it must be a whole parameter including the name (if we are
			// after a comma)
			else {
				String op = findPreviousOperator(document, offset).trim();
				for (LogicalOperatorInformation opBuilder : PQLEditorTextPlugIn.getOperatorInformations()) {
					if (opBuilder.getOperatorName().equalsIgnoreCase(op)) {
						List<String> alreadydefinedparams = findAlreadyDefinedParametersBeforePosition(document, offset, opBuilder);
						for (LogicalParameterInformation param : opBuilder.getParameters()) {
							if (!alreadydefinedparams.contains(param.getName())) {
								result.addAll(ParameterCompletionPart.buildCompletionProposals(param, offset, prefix.length(), document));
							}
						}
						break;
					}
				}
			}

			// if it ends with "(", there must be a parameter-list, a closing
			// operator with a ")"
			// or an operator/source
		} else if (tokenbefore.endsWith("(")) {
			result.add(buildCompletionProposal("{", offset));
			result.add(buildCompletionProposal(")", offset));
			result.addAll(buildCompletionProposalOperators(currentToken, document, offset, ""));
			return result;
			// if it ends with a "{", we're starting a parameter list
		} else if (tokenbefore.endsWith("{")) {
			String op = findPreviousOperator(document, offset).trim();
			for (LogicalOperatorInformation opBuilder : PQLEditorTextPlugIn.getOperatorInformations()) {
				if (opBuilder.getOperatorName().equalsIgnoreCase(op)) {
					List<String> alreadydefinedparams = findAlreadyDefinedParametersBeforePosition(document, offset - 1, opBuilder);
					for (LogicalParameterInformation param : opBuilder.getParameters()) {
						if (!alreadydefinedparams.contains(param.getName())) {
							if (param.getName().toLowerCase().startsWith(prefix.toLowerCase())) {
								result.addAll(ParameterCompletionPart.buildCompletionProposals(param, offset, prefix.length(), document));
							}
						}
					}
					break;
				}
			}
			// if it ends with a...
		} else if (tokenbefore.endsWith("=")) {
			// we are currently defining a parameter
			if (isInParameterSection(document, offset - 1)) {
				LogicalOperatorInformation builder = getOperatorAtPosition(document, offset - 1);
				if (builder != null) {
					LogicalParameterInformation param = getParamAtPosition(document, offset - 1, builder);
					if (param != null) {
						result.addAll(ParameterValueCompletionPart.buildCompletionProposals(param, offset, document, "="));
						return result;
					}
				}
				return new ArrayList<>();
			}
			// ... or we're at the beginning after the first definition, so we
			// need an operator or source
			return buildCompletionProposalOperators(currentToken, document, offset, prefix);
		} else if (tokenbefore.endsWith("[")) {
			if (inListParam(document, offset)) {
				LogicalOperatorInformation op = getOperatorAtPosition(document, offset);
				LogicalParameterInformation param = getParamAtPosition(document, offset, op);
				if (param != null) {
					// so we ask the parameter what we can insert here as the
					// list part --> the value of the
					result.addAll(ParameterValueCompletionPart.buildCompletionProposals(param, offset, document, "="));
				}
			}
		}
		return result;
	}

	private List<ICompletionProposal> buildCompletionProposalOperators(String currentToken, IDocument document, int offset, String prefix) {
		List<ICompletionProposal> sources = new ArrayList<>();
		for (ViewInformation source : PQLEditorTextPlugIn.getCurrentSources()) {
			String name = source.getName().getResourceName();
			if (name.toLowerCase().startsWith(prefix)) {
				sources.add(SourceCompletionPart.buildCompletionProposal(name, offset-prefix.length(), currentToken.length(), document));
			}
			name = source.getName().toString();
			if (name.toLowerCase().startsWith(prefix)) {
				sources.add(SourceCompletionPart.buildCompletionProposal(name, offset-prefix.length(), currentToken.length(), document));
			}
		}

		// sort them
		Collections.sort(sources, new Comparator<ICompletionProposal>() {
			@Override
			public int compare(ICompletionProposal o1, ICompletionProposal o2) {
				return o1.getDisplayString().compareToIgnoreCase(o2.getDisplayString());
			}
		});

		// pre-build operator templates
		List<ICompletionProposal> operators = new ArrayList<ICompletionProposal>();
		for (LogicalOperatorInformation opBuilder : PQLEditorTextPlugIn.getOperatorInformations()) {
			if (opBuilder.getOperatorName().toLowerCase().startsWith(prefix)) {
				operators.add(OperatorCompletionPart.buildCompletionProposal(opBuilder, offset-prefix.length(), currentToken.length(), document));
			}
		}

		// sort them
		Collections.sort(operators, new Comparator<ICompletionProposal>() {
			@Override
			public int compare(ICompletionProposal o1, ICompletionProposal o2) {
				return o1.getDisplayString().compareToIgnoreCase(o2.getDisplayString());
			}
		});
		// first all sources, then all operators
		sources.addAll(operators);
		operators = sources;
		return operators;
	}

	private List<String> findAlreadyDefinedParametersBeforePosition(IDocument document, int offset, LogicalOperatorInformation opBuilder) {
		int n = offset;
		List<String> found = new ArrayList<>();
		try {
			while (n > 0) {
				if (document.getChar(n) == '{') {
					return found;
				}
				LogicalParameterInformation lpi = getParamAtPosition(document, n, opBuilder);
				if (lpi != null && !found.contains(lpi.getName())) {
					found.add(lpi.getName());
				}
				n--;
			}
		} catch (BadLocationException e) {
		}
		return new ArrayList<>();
	}

	private boolean inListParam(IDocument document, int offset) {
		int n = offset - 1;
		int closed = 0;
		try {
			while (n > 0) {
				if (document.getChar(n) == ']') {
					closed++;
				}
				if (document.getChar(n) == '=') {
					return false;
				}
				if (document.getChar(n) == '[') {
					if (closed == 0) {
						return true;
					} 
					closed--;
					
				}
				n--;
			}
		} catch (BadLocationException e) {
		}
		return false;
	}

	private LogicalParameterInformation getParamAtPosition(IDocument document, int offset, LogicalOperatorInformation builder) {
		if (builder == null) {
			return null;
		}
		try {
			int n = offset;
			int brakets = 0;
			String name = "";
			boolean read = false;
			while (n > 0) {
				char c = document.getChar(n);
				if (c == ']') {
					brakets++;
				}
				if (c == '[') {
					brakets--;
				}
				if (brakets <= 0) {

					if (read && (Character.isWhitespace(c) || c == ',' || c == '(' || c == '{')) {
						if (!(Character.isWhitespace(c) && name.isEmpty())) {
							break;
						}
					}

					if (read) {
						name = c + name;
					}

					if (c == '=') {
						read = true;
					}
				}
				n--;
			}
			name = name.trim();
			for (LogicalParameterInformation param : builder.getParameters()) {
				if (param.getName().equalsIgnoreCase(name)) {
					return param;
				}
			}
		} catch (BadLocationException e) {

		}
		return null;

	}

	private LogicalOperatorInformation getOperatorAtPosition(IDocument document, int offset) {
		try {
			int n = offset;
			String name = "";
			boolean read = false;
			while (n > 0) {
				char c = document.getChar(n);
				if (read && (Character.isWhitespace(c) || getTokenSplitters().contains(c))) {
					break;
				}
				if (read) {
					name = c + name;
				}
				if (c == '(') {
					read = true;
				}
				n--;
			}
			for (LogicalOperatorInformation builder : PQLEditorTextPlugIn.getOperatorInformations()) {
				if (builder.getOperatorName().equalsIgnoreCase(name)) {
					return builder;
				}
			}
		} catch (BadLocationException e) {

		}
		return null;
	}

	/**
	 * @param currentToken
	 * @return
	 */
	private String getPrefix(String currentToken) {
		for (int n = currentToken.length() - 1; n >= 0; n--) {
			if (getTokenSplitters().contains(currentToken.charAt(n))) {
				return currentToken.substring(n + 1);
			}
		}
		return currentToken;
	}

	private String getCompleteToken(IDocument document, int offset) {

		try {
			int start = offset;
			while (start > 0) {
				char c = document.getChar(start);
				if (getTokenSplitters().contains(c)) {
					break;
				}
				start--;
			}
			start++;
			int end = offset;
			while (end < document.getLength()) {
				char c = document.getChar(end);
				if (getTokenSplitters().contains(c)) {
					break;
				}
				end++;
			}
			return document.get(start, end - start);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	private ICompletionProposal buildCompletionProposal(String word, int offset) {
		return new CompletionProposal(word, offset, 0, word.length());
	}

	/**
	 * @param document
	 * @param offset
	 */
	private String findPreviousOperator(IDocument doc, int offset) {
		boolean rawMode = false;
		boolean found = false;
		try {
			int start = offset;
			for (int n = offset - 1; n >= 0; n--) {
				char c = doc.getChar(n);
				if (c == '\'') {
					rawMode = !rawMode;
				}

				if (found && !Character.isJavaIdentifierPart(c)) {
					return doc.get(n, start - n);
				}

				if (!rawMode && c == '(') {
					start = n;
					found = true;
				}

			}
		} catch (BadLocationException e) {
		}
		return "";

	}

	private boolean isInParameterSection(IDocument document, int offset) {
		try {
			int n = offset;
			while (n > 0) {
				char c = document.getChar(n);
				if (c == '}') {
					return false;
				}
				if (c == '{') {
					return true;
				}
				n--;
			}
		} catch (BadLocationException e) {

		}
		return false;
	}

	// private String getParamString(LogicalParameterInformation param) {
	// String name = param.getName() + "=" + getParamValue(param, "", false);
	// return name;
	// }
	//
	// private String getParamValue(LogicalParameterInformation param, String
	// currentValue, boolean inList) {
	// IParameterProposal proposal = ParameterProposalFactory.getProposal(param,
	// inList);
	// return proposal.getParamValue(param, currentValue, inList);
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.rcp.editor.text.IEditorCompletionProvider#
	 * getTokenSplitters()
	 */
	@Override
	public List<Character> getTokenSplitters() {
		List<Character> result = new ArrayList<Character>();
		result.add('=');
		result.add('{');
		result.add('}');
		result.add(' ');
		result.add('(');
		result.add(')');
		result.add('[');
		result.add(']');
		result.add(',');
		return result;
	}

	@Override
	public boolean ignoreWhitespaces() {
		return true;
	}

	@Override
	public String getSupportedParser() {
		return "PQL";
	}

	@Override
	public List<Terminal> getTerminals() {
		List<Terminal> names = new ArrayList<>();
		for (LogicalOperatorInformation loi : PQLEditorTextPlugIn.getOperatorInformations()) {
			names.add(new Terminal(loi.getOperatorName(), loi.isDeprecated()));
		}
		return names;
	}

	@Override
	public IOdysseusScriptFormattingStrategy getFormattingStrategy() {
		return new PQLFormattingStrategy();
	}

}
