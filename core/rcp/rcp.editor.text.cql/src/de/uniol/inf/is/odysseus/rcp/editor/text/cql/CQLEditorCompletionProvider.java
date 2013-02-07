/********************************************************************************** 
 * Copyright 2013 The Odysseus Team
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
package de.uniol.inf.is.odysseus.rcp.editor.text.cql;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IDocument;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.cql.parser.NewSQLParserConstants;
import de.uniol.inf.is.odysseus.rcp.editor.text.completion.IEditorLanguagePropertiesProvider;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.IOdysseusScriptFormattingStrategy;

/**
 * @author Dennis Geesen
 * 
 */
public class CQLEditorCompletionProvider implements IEditorLanguagePropertiesProvider {

	@Override
	public List<Character> getTokenSplitters() {
		return new ArrayList<Character>();
	}

	@Override
	public boolean ignoreWhitespaces() {
		return true;
	}

	@Override
	public String supportsParser() {
		return "CQL";
	}

	@Override
	public List<String> getTerminals() {

		// add all parser tokens
		List<String> liste = new ArrayList<String>();
		String[] tokens = NewSQLParserConstants.tokenImage;
		for (String token : tokens) {
			if (!token.startsWith("<")) {
				if (token.startsWith("\"")) {
					token = token.substring(1, token.length());
				}
				if (token.endsWith("\"")) {
					token = token.substring(0, token.length() - 1);
				}
				if (token.length() > 1 && !token.startsWith("\\")) {
					liste.add(token);
				}
			}
		}
		// then, add also all datatypes
		liste.addAll(Activator.getDataDictionary().getDatatypes());
		return liste;
	}

	@Override
	public List<String> getCompletionSuggestions(String currentToken, String lastToken, IExecutor executor, ISession iSession, IDocument document, int offset) {
		return new ArrayList<String>();
	}

	@Override
	public IOdysseusScriptFormattingStrategy getFormattingStrategy() {
		return new CQLFormattingStrategy();
	}
	

}
