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
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.editor.text.completion.IEditorLanguagePropertiesProvider;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.IOdysseusScriptFormattingStrategy;
import de.uniol.inf.is.odysseus.rcp.editor.text.pql.PQLEditorTextPlugIn;

/**
 * @author Dennis Geesen
 * 
 */
public class EditorCompletionProvider implements IEditorLanguagePropertiesProvider {


	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * de.uniol.inf.is.odysseus.rcp.editor.text.IEditorLanguagePropertiesProvider#getWords
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getCompletionSuggestions(String currentToken, String tokenbefore, IExecutor executor, ISession session, IDocument document, int offset) {
		List<String> result = new ArrayList<String>();
		currentToken = currentToken.trim().toLowerCase();
		tokenbefore = tokenbefore.trim().toLowerCase();
		// after comma it is
		String prefix = getPrefix(currentToken);
		currentToken = currentToken.substring(0, currentToken.length() - prefix.length()).trim();
		if (currentToken.endsWith(",")) {
			// is an operator, if the it also contains }
			if (tokenbefore.endsWith("}")) {
				for (int k = 0; k < PQLEditorTextPlugIn.getPQLKeywords().length; k++) {
					String word = PQLEditorTextPlugIn.getPQLKeywords()[k];
					if (word.toUpperCase().startsWith(prefix.toUpperCase())) {
						result.add(word.substring(prefix.length(), word.length()));
					}
				}
			}
			// or it must be a parameter
			else {
				String op = findPreviousOperator(document, offset).trim();
				for (IOperatorBuilder opBuilder : PQLEditorTextPlugIn.getOperatorBuilderFactory().getOperatorBuilder()) {
					if (opBuilder.getName().equalsIgnoreCase(op)) {
						for (IParameter<?> param : opBuilder.getParameters()) {
							result.add(param.getName());
						}
						break;
					}
				}
			}

		} else if (currentToken.endsWith("(")) {
			result.add("{");
			result.add(")");
			for (int k = 0; k < PQLEditorTextPlugIn.getPQLKeywords().length; k++) {
				String word = PQLEditorTextPlugIn.getPQLKeywords()[k];
				if (word.toUpperCase().startsWith(prefix.toUpperCase())) {
					result.add(word.substring(prefix.length(), word.length()));
				}
			}
		} else if (currentToken.endsWith("{")) {
			String op = findPreviousOperator(document, offset).trim();
			for (IOperatorBuilder opBuilder : PQLEditorTextPlugIn.getOperatorBuilderFactory().getOperatorBuilder()) {
				if (opBuilder.getName().equalsIgnoreCase(op)) {
					for (IParameter<?> param : opBuilder.getParameters()) {
						String name = param.getName() + "=";
						if (param instanceof StringParameter) {
							name = name + "''";
						} else if (param instanceof ListParameter) {
							name = name + "[]";
						}
						result.add(name);
					}
					break;
				}
			}
		} else if (currentToken.endsWith("=")) {
			List<String> operators = new ArrayList<String>();		
			for (IOperatorBuilder opBuilder : PQLEditorTextPlugIn.getOperatorBuilderFactory().getOperatorBuilder()) {
				operators.add(opBuilder.getName().toUpperCase());
			}
			return operators;
		}
		return result;
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

	public boolean ignoreWhitespaces() {
		return true;
	}

	@Override
	public String supportsParser() {
		return "PQL";
	}

	@Override
	public List<String> getTerminals() {
		List<String> names = new ArrayList<>();
		for (IOperatorBuilder b : PQLEditorTextPlugIn.getOperatorBuilderFactory().getOperatorBuilder()) {
			names.add(b.getName());
		}
		return names;
	}

	@Override
	public IOdysseusScriptFormattingStrategy getFormattingStrategy() {
		return new PQLFormattingStrategy();		
	}

	
	

}
