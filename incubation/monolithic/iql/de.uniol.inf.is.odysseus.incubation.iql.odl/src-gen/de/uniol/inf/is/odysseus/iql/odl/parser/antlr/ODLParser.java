/*
* generated by Xtext
*/
package de.uniol.inf.is.odysseus.iql.odl.parser.antlr;

import com.google.inject.Inject;

import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import de.uniol.inf.is.odysseus.iql.odl.services.ODLGrammarAccess;

public class ODLParser extends org.eclipse.xtext.parser.antlr.AbstractAntlrParser {
	
	@Inject
	private ODLGrammarAccess grammarAccess;
	
	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	
	@Override
	protected de.uniol.inf.is.odysseus.iql.odl.parser.antlr.internal.InternalODLParser createParser(XtextTokenStream stream) {
		return new de.uniol.inf.is.odysseus.iql.odl.parser.antlr.internal.InternalODLParser(stream, getGrammarAccess());
	}
	
	@Override 
	protected String getDefaultRuleName() {
		return "ODLFile";
	}
	
	public ODLGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(ODLGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
	
}
