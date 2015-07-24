/*
* generated by Xtext
*/
package de.uniol.inf.is.odysseus.iql.basic.parser.antlr;

import com.google.inject.Inject;

import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import de.uniol.inf.is.odysseus.iql.basic.services.BasicIQLGrammarAccess;

public class BasicIQLParser extends org.eclipse.xtext.parser.antlr.AbstractAntlrParser {
	
	@Inject
	private BasicIQLGrammarAccess grammarAccess;
	
	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	
	@Override
	protected de.uniol.inf.is.odysseus.iql.basic.parser.antlr.internal.InternalBasicIQLParser createParser(XtextTokenStream stream) {
		return new de.uniol.inf.is.odysseus.iql.basic.parser.antlr.internal.InternalBasicIQLParser(stream, getGrammarAccess());
	}
	
	@Override 
	protected String getDefaultRuleName() {
		return "IQLFile";
	}
	
	public BasicIQLGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}
	
	public void setGrammarAccess(BasicIQLGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
	
}
