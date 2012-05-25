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

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
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
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParser;

public class OdysseusScriptViewerConfiguration extends SourceViewerConfiguration {

	private IWhitespaceDetector whitespaceDetector;
	private IWordDetector wordDetector;
	private OdysseusScriptEditor editor;

	public OdysseusScriptViewerConfiguration(ColorManager colorManager, OdysseusScriptEditor editor) {
		this.editor = editor;
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
		RuleBasedScanner scanner = new OdysseusRuleBasedScanner();

		scanner.setRules(getRules());

		IToken def = createToken(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK), false);
		scanner.setDefaultReturnToken(def);

		return scanner;
	}

	// @Override
	// public IReconciler getReconciler(ISourceViewer sourceViewer) {
	// IReconciler reconciler = super.getReconciler(sourceViewer);
	// this.editor.setModel();
	// return reconciler;
	// }

	@Override
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		OdysseusScriptReconcilerStrategy strategy = new OdysseusScriptReconcilerStrategy(this.editor);
		MonoReconciler reconciler = new MonoReconciler(strategy, false);
		reconciler.setProgressMonitor(new NullProgressMonitor());
		reconciler.setDelay(500);
		return reconciler;
	}

	protected IRule[] getRules() {
		IToken parameter = createToken(Display.getCurrent().getSystemColor(SWT.COLOR_RED), false);
		IToken comment = createToken(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GREEN), false);
		IToken replacement = createToken(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_MAGENTA), false);
		IToken def = createToken(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK), false);
		IToken strings = createToken(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE), false);
		IToken staticKeywords = createToken(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY), false);

		ArrayList<IRule> rules = new ArrayList<IRule>();

		// Strings
		rules.add(new SingleLineRule("'", "'", strings));

		// PreParserKeywords
		WordRule wr = new WordRule(getWordDetector(), Token.UNDEFINED, false);
		if (OdysseusRCPEditorTextPlugIn.getScriptParser() != null) {
			for (String key : OdysseusRCPEditorTextPlugIn.getScriptParser().getKeywordNames()) {
				wr.addWord(OdysseusRCPEditorTextPlugIn.getScriptParser().getParameterKey() + key, parameter);
			}
		}

		// Static PreparserKeywords
		for (String s : OdysseusRCPEditorTextPlugIn.getScriptParser().getStaticWords()) {
			wr.addWord(OdysseusScriptParser.PARAMETER_KEY + s, staticKeywords);
		}
		rules.add(wr);

		// Replacements
		if (OdysseusRCPEditorTextPlugIn.getScriptParser() != null) {
			rules.add(new SingleLineRule(OdysseusRCPEditorTextPlugIn.getScriptParser().getReplacementStartKey(), OdysseusRCPEditorTextPlugIn.getScriptParser().getReplacementEndKey(), replacement));
		}
		// Extensions
		ParserDependentWordRule r = new ParserDependentWordRule(getWordDetector(), def, true);
		for (String grp : KeywordRegistry.getInstance().getKeywordGroups()) {
			IToken wordToken = createToken(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE), true);
			for (String word : KeywordRegistry.getInstance().getKeywords(grp)) {
				r.addWord(word, wordToken, grp);
			}
		}
		rules.add(r);

		// WordRule r = new WordRule(getWordDetector(), def, true);
		// for (String grp : KeywordRegistry.getInstance().getKeywordGroups()) {
		//
		// IToken wordToken =
		// createToken(Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE),
		// true);
		//
		// for (String word : KeywordRegistry.getInstance().getKeywords(grp)) {
		// r.addWord(word, wordToken);
		// }
		// }
		// rules.add(r);

		// Kommentare
		if (OdysseusRCPEditorTextPlugIn.getScriptParser() != null) {
			rules.add(new SingleLineRule(OdysseusRCPEditorTextPlugIn.getScriptParser().getSingleLineCommentKey(), "\n", comment, '\\', true));
		}
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

	protected IToken createToken(Color color, boolean bold) {
		if (bold)
			return new Token(new TextAttribute(color, null, SWT.BOLD));
		return new Token(new TextAttribute(color));
	}

	protected IWordDetector getWordDetector() {
		if (wordDetector == null) {
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

	@Override
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant ca = new ContentAssistant();
		IContentAssistProcessor cp = new OdysseusScriptCompletionProcessor();
		ca.setContentAssistProcessor(cp, IDocument.DEFAULT_CONTENT_TYPE);
		ca.enableAutoActivation(true);
		ca.setInformationControlCreator(getInformationControlCreator(sourceViewer));
		return ca;
	}
}
