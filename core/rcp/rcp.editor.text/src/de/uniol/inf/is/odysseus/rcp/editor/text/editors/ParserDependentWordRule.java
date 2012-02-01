/** Copyright 2011 The Odysseus Team
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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.IWordDetector;
import org.eclipse.jface.text.rules.Token;

import de.uniol.inf.is.odysseus.planmanagement.executor.exception.NoCompilerLoadedException;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;

/**
 * 
 * @author Dennis Geesen Created at: 10.11.2011
 */
public class ParserDependentWordRule implements IRule {

	protected static final int UNDEFINED = -1;
	protected IWordDetector fDetector;
	protected IToken fDefaultToken;
	protected int fColumn = UNDEFINED;
	protected Map<String, IToken> fWords = new HashMap<String, IToken>();
	private Map<String, String> wordAndParser = new HashMap<String, String>();
	private StringBuffer fBuffer = new StringBuffer();

	private boolean fIgnoreCase = false;

	public ParserDependentWordRule(IWordDetector detector) {
		this(detector, Token.UNDEFINED, false);
	}

	public ParserDependentWordRule(IWordDetector detector, IToken defaultToken) {
		this(detector, defaultToken, false);
	}

	public ParserDependentWordRule(IWordDetector detector, IToken defaultToken, boolean ignoreCase) {
		Assert.isNotNull(detector);
		Assert.isNotNull(defaultToken);

		fDetector = detector;
		fDefaultToken = defaultToken;
		fIgnoreCase = ignoreCase;
	}

	public void addWord(String word, IToken token, String parser) {
		Assert.isNotNull(word);
		Assert.isNotNull(token);

		// If case-insensitive, convert to lower case before adding to the map
		if (fIgnoreCase)
			word = word.toLowerCase();
		fWords.put(word, token);
		wordAndParser.put(word, parser);
	}

	public void setColumnConstraint(int column) {
		if (column < 0)
			column = UNDEFINED;
		fColumn = column;
	}

	/*
	 * @see IRule#evaluate(ICharacterScanner)
	 */
	public IToken evaluate(ICharacterScanner scanner) {
		int c = scanner.read();
		if (c != ICharacterScanner.EOF && fDetector.isWordStart((char) c)) {
			if (fColumn == UNDEFINED || (fColumn == scanner.getColumn() - 1)) {

				fBuffer.setLength(0);
				do {
					fBuffer.append((char) c);
					c = scanner.read();
				} while (c != ICharacterScanner.EOF && fDetector.isWordPart((char) c));
				scanner.unread();

				String buffer = fBuffer.toString();
				// If case-insensitive, convert to lower case before accessing
				// the map
				if (fIgnoreCase)
					buffer = buffer.toLowerCase();				
				IToken token = (IToken) fWords.get(buffer);
				if (token != null) {
					String parser = wordAndParser.get(buffer);
					try {
						if (OdysseusRCPEditorTextPlugIn.getExecutor() != null &&
							OdysseusRCPEditorTextPlugIn.getExecutor().getSupportedQueryParsers().contains(parser)) {
							if (scanner instanceof OdysseusRuleBasedScanner) {
								OdysseusRuleBasedScanner oScanner = (OdysseusRuleBasedScanner) scanner;
								String currentParser = getValidParserAtPosition(oScanner.getDocument(), oScanner.getCurrentOffset());
								if (!currentParser.equalsIgnoreCase(parser)) {
									token = null;
								}
							}
						}
					} catch (NoCompilerLoadedException e) {
						e.printStackTrace();
					}
				}

				if (token != null)
					return token;

				if (fDefaultToken.isUndefined())
					unreadBuffer(scanner);

				return fDefaultToken;
			}
		}

		scanner.unread();
		return Token.UNDEFINED;
	}

	protected void unreadBuffer(ICharacterScanner scanner) {
		for (int i = fBuffer.length() - 1; i >= 0; i--)
			scanner.unread();
	}

	public String getValidParserAtPosition(IDocument document, int offset) {

		if (offset <= 0) {
			return "";
		}
		try {
			int position = document.get(0, offset).toUpperCase().lastIndexOf("#PARSER");

			if (position == -1) {
				return "";
			}
			int start = position + 7;
			while (start < document.getLength()) {
				if (!Character.isWhitespace(document.getChar(start))) {
					break;
				}
				start++;
			}
			int end = start + 1;
			while (end < document.getLength()) {
				// if (Character.isWhitespace(document.getChar(end))) {
				if (document.getChar(end) == '#') {
					break;
				}
				end++;
			}
			String parser = document.get(start, end - start).trim();			
			return parser;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return null;
	}

}
