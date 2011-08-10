/** Copyright [2011] [The Odysseus Team]
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
package de.uniol.inf.is.odysseus.rcp.editor.text.editors;

import java.util.ArrayList;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.IWhitespaceDetector;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.editor.text.KeywordRegistry;
import de.uniol.inf.is.odysseus.script.parser.PreParserKeywordRegistry;
import de.uniol.inf.is.odysseus.script.parser.QueryTextParser;

public class OdysseusScriptViewerConfiguration extends SourceViewerConfiguration {

	private IWhitespaceDetector whitespaceDetector;
	private IWordDetector wordDetector;
	private ColorManager colorManager;

	public OdysseusScriptViewerConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	
	@Override
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getScanner());
		reconciler.setDamager(dr, "__queries");
		reconciler.setRepairer(dr, "__queries");

		dr = new DefaultDamagerRepairer(getScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		return reconciler;
	}

	protected ITokenScanner getScanner() {
		RuleBasedScanner scanner = new RuleBasedScanner();
		
		scanner.setRules(getRules());
		
		IToken def = createToken(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK), false);
		scanner.setDefaultReturnToken(def);
		
		return scanner;
	}
	
	protected IRule[] getRules() {
		IToken parameter = createToken(Display.getCurrent().getSystemColor(SWT.COLOR_RED), false);
		IToken comment = createToken(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN), false);
		IToken replacement = createToken(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_YELLOW), false);
		IToken def = createToken(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK), false);
		
		ArrayList<IRule> rules = new ArrayList<IRule>();
		
		// PreParserKeywords
		WordRule wr = new WordRule( getWordDetector(), Token.UNDEFINED, false );
		for( String key : PreParserKeywordRegistry.getInstance().getKeywordNames()) {
			wr.addWord(QueryTextParser.PARAMETER_KEY + key, parameter);
		}
		wr.addWord("#DEFINE", parameter);
		rules.add(wr);
			
		// Replacements
		rules.add( new SingleLineRule(QueryTextParser.REPLACEMENT_START_KEY, QueryTextParser.REPLACEMENT_END_KEY, replacement));
		
		// Extensions
		WordRule r = new WordRule(getWordDetector(), def, false);
		for( String grp : KeywordRegistry.getInstance().getKeywordGroups()) {
			
			int red = KeywordRegistry.getInstance().getGroupColorR(grp);
			int green = KeywordRegistry.getInstance().getGroupColorG(grp);
			int blue = KeywordRegistry.getInstance().getGroupColorB(grp);
			
			IToken wordToken = createToken( colorManager.get(red, green, blue), true);
			
			for( String word : KeywordRegistry.getInstance().getKeywords(grp)) {
				r.addWord(word, wordToken);
			}
		}
		rules.add(r);

		// Kommentare
		rules.add(new SingleLineRule( QueryTextParser.SINGLE_LINE_COMMENT_KEY, "\n", comment, '\\', true));
		
		// Whitespace
		rules.add(new WhitespaceRule(getWhitespaceDetector()));
		
		return rules.toArray(new IRule[rules.size()]);
	}

	protected IWhitespaceDetector getWhitespaceDetector() {
		if (whitespaceDetector == null)
			whitespaceDetector = new IWhitespaceDetector() {

				@Override
				public boolean isWhitespace(char c) {
					return (c == ' ' || c == '\t' || c == '\n' || c == '\r');
				}

			};
		return whitespaceDetector;
	}
	
	protected IToken createToken( Color color, boolean bold ) {
		if( bold ) 
			return new Token(new TextAttribute(color, null, SWT.BOLD));
		else 
			return new Token(new TextAttribute(color));
	}
	
	protected IWordDetector getWordDetector() {
		if( wordDetector == null ) {
			wordDetector = new IWordDetector() {
				
				@Override
				public boolean isWordStart(char c) {
					return Character.isLetter(c) || c == '#' || c == '$';
				}

				@Override
				public boolean isWordPart(char c) {
					return Character.isJavaIdentifierPart(c) || c == '-';
				}
				
			};
		}
		return wordDetector;
	}
}
