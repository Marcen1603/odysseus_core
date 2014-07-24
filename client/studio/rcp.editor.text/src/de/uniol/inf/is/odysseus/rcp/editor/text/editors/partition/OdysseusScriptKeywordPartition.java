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
package de.uniol.inf.is.odysseus.rcp.editor.text.editors.partition;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.formatter.IFormattingStrategy;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusScriptStructureProvider;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.KeyWordFormattingStrategy;

/**
 * @author Dennis Geesen
 * 
 */
public class OdysseusScriptKeywordPartition extends AbstractOdysseusScriptParition {

	private IWordDetector wordDetector;

	public OdysseusScriptKeywordPartition() {
		this("__odysseus_script_keyword");
	}

	public OdysseusScriptKeywordPartition(String string) {
		super(string);
	}

	@Override
	public Color getDefaultColor() {
		// normal color here is dark blue
		return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
	}

	@Override
	public List<IRule> getColoringRules() {
		List<IRule> rules = new ArrayList<>();

		rules.add(new MultiLineRule("'", "'", createToken(SWT.COLOR_BLUE)));
		rules.add(new EndOfLineRule(OdysseusScriptStructureProvider.SINGLE_LINE_COMMENT_KEY, createToken(SWT.COLOR_DARK_GREEN, false)));

		// if a keyword is found, color it red
		IToken keywordToken = createToken(SWT.COLOR_RED, false);
		IToken deprecatedKeywordToken = createToken(SWT.COLOR_RED, false, true);

		// everything else should be undefined (this gets the default color)
		WordRule wr = new WordRule(getWordDetector(), Token.UNDEFINED, true);
		for (String key : OdysseusScriptStructureProvider.getKeywords()) {
			if (!OdysseusScriptStructureProvider.isDeprecated(key)) {
				wr.addWord(OdysseusScriptStructureProvider.PARAMETER_KEY + key, keywordToken);
			} else {
				wr.addWord(OdysseusScriptStructureProvider.PARAMETER_KEY + key, deprecatedKeywordToken);
			}
		}
		rules.add(wr);

		IToken variable = createToken(SWT.COLOR_DARK_MAGENTA);
		rules.add(new SingleLineRule(OdysseusScriptStructureProvider.REPLACEMENT_START_KEY, OdysseusScriptStructureProvider.REPLACEMENT_END_KEY, variable));
		IToken staticKeywords = createToken(SWT.COLOR_DARK_GRAY);

		for (String s : OdysseusScriptStructureProvider.getStaticWords()) {
			wr.addWord(OdysseusScriptStructureProvider.PARAMETER_KEY + s, staticKeywords);
			wr.addWord(s, staticKeywords);
		}
		return rules;
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
	public List<IPredicateRule> getPartitioningRules() {
		List<IPredicateRule> rules = new ArrayList<>();

		List<String> end = new ArrayList<>();
		end.add(OdysseusScriptStructureProvider.PARAMETER_KEY);
		List<String> ignore = new ArrayList<>();
		ignore.add("#IFDEF");
		ignore.add("#ELSE");
		ignore.add("#ENDIF");
		rules.add(new SimpleMultiLineRule(OdysseusScriptStructureProvider.PARAMETER_KEY, end, ignore, getPartitioningToken(), true, true));
		rules.add(new EndOfLineRule(OdysseusScriptStructureProvider.PARAMETER_KEY, getPartitioningToken()));

		return rules;
	}

	@Override
	public IFormattingStrategy getFormattingStrategy() {
		return new KeyWordFormattingStrategy();
	}

}
