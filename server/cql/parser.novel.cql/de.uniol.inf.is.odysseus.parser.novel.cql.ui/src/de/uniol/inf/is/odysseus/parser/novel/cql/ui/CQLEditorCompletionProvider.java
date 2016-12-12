package de.uniol.inf.is.odysseus.parser.novel.cql.ui;

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
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.OdysseusScriptContentFormatter;

public class CQLEditorCompletionProvider implements IEditorLanguagePropertiesProvider
{

	@Override
	public List<Character> getTokenSplitters() {
		// TODO Auto-generated method stub
		return new ArrayList<>();
	}

	@Override
	public List<ICompletionProposal> getCompletionSuggestions(String currentToken, String[] lastSplitters,
			IExecutor executor, ISession iSession, IDocument document, int offset, Point selection) {
		return new ArrayList<>();
	}

	@Override
	public boolean ignoreWhitespaces() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getSupportedParser() { return "CQL"; }

	@Override
	public List<Terminal> getTerminals() 
	{
		return new ArrayList<Terminal>();
	}

	@Override
	public IOdysseusScriptFormattingStrategy getFormattingStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

}
