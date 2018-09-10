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
package de.uniol.inf.is.odysseus.rcp.editor.text.sase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Point;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.completion.IEditorLanguagePropertiesProvider;
import de.uniol.inf.is.odysseus.rcp.editor.text.completion.Terminal;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.IOdysseusScriptFormattingStrategy;

/**
 * @author Dennis Geesen
 * 
 */
public class SaseEditorCompletionProvider implements IEditorLanguagePropertiesProvider {

	@Override
	public List<Character> getTokenSplitters() {
		return new ArrayList<Character>();
	}

	@Override
	public boolean ignoreWhitespaces() {
		return true;
	}

	@Override
	public String getSupportedParser() {
		return "SASE_Relational";
	}

	@Override
	public List<Terminal> getTerminals() {
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		// add all parser tokens
		List<Terminal> liste = new ArrayList<Terminal>();
		Map<String, List<String>> values = OdysseusRCPEditorTextPlugIn.getExecutor().getQueryParserTokens(getSupportedParser(), caller);
		if (values.containsKey("TOKEN")) {
			for (String token : values.get("TOKEN")) {
				if (!token.startsWith("<")) {
					if (token.startsWith("\"")) {
						token = token.substring(1, token.length());
					}
					if (token.endsWith("\"")) {
						token = token.substring(0, token.length() - 1);
					}
					if (token.length() > 1 && !token.startsWith("\\")) {
						liste.add(new Terminal(token, false));
					}
				}
			}
		}
		// then, add also all datatypes
		for (String dataType : OdysseusRCPEditorTextPlugIn.getDatatypeNames()) {
			liste.add(new Terminal(dataType, false));
		}
		return liste;
	}

	@Override
	public List<ICompletionProposal> getCompletionSuggestions(String currentToken, String lastToken[], IExecutor executor, ISession iSession, IDocument document, int offset, Point selection) {
		return new ArrayList<ICompletionProposal>();
	}

	@Override
	public IOdysseusScriptFormattingStrategy getFormattingStrategy() {
		return new SaseFormattingStrategy();
	}

}
