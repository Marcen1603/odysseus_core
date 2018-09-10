package de.uniol.inf.is.odysseus.parser.cql2.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

public class CQLEditorCompletionProvider implements IEditorLanguagePropertiesProvider {

	@Override
	public List<Character> getTokenSplitters() {
		return new ArrayList<>();
	}

	@Override
	public List<ICompletionProposal> getCompletionSuggestions(String currentToken, String[] lastSplitters,
			IExecutor executor, ISession iSession, IDocument document, int offset, Point selection) {
		return new ArrayList<>();
	}

	@Override
	public boolean ignoreWhitespaces() {
		return false;
	}

	@Override
	public String getSupportedParser() {
		return "CQL";
	}

	@Override
	public List<Terminal> getTerminals() {
		ISession caller = OdysseusRCPPlugIn.getActiveSession();
		// add all parser tokens
		List<Terminal> tokens = new ArrayList<Terminal>();
		Map<String, List<String>> values = OdysseusRCPEditorTextPlugIn.getExecutor()
				.getQueryParserTokens(getSupportedParser(), caller);
		if (values != null && !values.isEmpty()) {
			for (String token : values.get("TOKEN")) {
				if (!token.startsWith("<")) {
					if (token.startsWith("\""))
						token = token.substring(1, token.length());
					if (token.endsWith("\""))
						token = token.substring(0, token.length() - 1);
					if (token.length() > 1 && !token.startsWith("\\"))
						tokens.add(new Terminal(token, false));
				}
			}
			// then, add also all datatypes
			for (String dataType : OdysseusRCPEditorTextPlugIn.getDatatypeNames())
				if (!tokens.stream().map(e -> e.getName()).collect(Collectors.toList()).contains(dataType))
					tokens.add(new Terminal(dataType, false));
		}
		return tokens;
	}

	@Override
	public IOdysseusScriptFormattingStrategy getFormattingStrategy() {
		return null;
	}

}
