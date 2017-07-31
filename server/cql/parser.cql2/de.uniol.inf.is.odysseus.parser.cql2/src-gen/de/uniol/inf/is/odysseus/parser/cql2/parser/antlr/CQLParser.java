/*
 * generated by Xtext 2.10.0
 */
package de.uniol.inf.is.odysseus.parser.cql2.parser.antlr;

import com.google.inject.Inject;
import de.uniol.inf.is.odysseus.parser.cql2.parser.antlr.internal.InternalCQLParser;
import de.uniol.inf.is.odysseus.parser.cql2.services.CQLGrammarAccess;
import org.eclipse.xtext.parser.antlr.AbstractAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;

public class CQLParser extends AbstractAntlrParser {

	@Inject
	private CQLGrammarAccess grammarAccess;

	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	

	@Override
	protected InternalCQLParser createParser(XtextTokenStream stream) {
		return new InternalCQLParser(stream, getGrammarAccess());
	}

	@Override 
	protected String getDefaultRuleName() {
		return "Model";
	}

	public CQLGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(CQLGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
