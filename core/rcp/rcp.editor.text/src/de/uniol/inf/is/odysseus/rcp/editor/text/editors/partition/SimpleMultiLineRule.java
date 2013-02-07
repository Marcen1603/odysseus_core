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

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

import de.uniol.inf.is.odysseus.script.parser.OdysseusScriptParser;

/**
 * @author Dennis Geesen
 * 
 */
public class SimpleMultiLineRule implements IPredicateRule {

	private char[] startSequnce;
	private char[][] endSequences;
	private char[][] ignore;
	private IToken successToken;
	private boolean eofisend;
	private boolean endSequenceExclusively;
	private char[] comment = OdysseusScriptParser.SINGLE_LINE_COMMENT_KEY.toCharArray();

	public SimpleMultiLineRule(String startSequence, String endSequence, IToken token, boolean eofIsEnd, boolean endSequenceExclusively) {
		this.startSequnce = startSequence.toCharArray();
		this.endSequences = new char[1][];
		this.endSequences[0] = endSequence.toCharArray();
		this.successToken = token;
		this.eofisend = eofIsEnd;
		this.endSequenceExclusively = endSequenceExclusively;
	}

	public SimpleMultiLineRule(String startSequence, List<String> endSequences, IToken token, boolean eofIsEnd, boolean endSequenceExclusively) {
		this(startSequence, endSequences, new ArrayList<String>(), token, eofIsEnd, endSequenceExclusively);
	}

	public SimpleMultiLineRule(String startSequence, List<String> endSequences, List<String> ignore, IToken token, boolean eofIsEnd, boolean endSequenceExclusively) {
		this.startSequnce = startSequence.toCharArray();
		this.endSequences = new char[endSequences.size()][];
		for (int i = 0; i < endSequences.size(); i++) {
			this.endSequences[i] = endSequences.get(i).toCharArray();
		}
		this.ignore = new char[ignore.size()][];
		for (int i = 0; i < ignore.size(); i++) {
			this.ignore[i] = ignore.get(i).toCharArray();
		}
		this.successToken = token;
		this.eofisend = eofIsEnd;
		this.endSequenceExclusively = endSequenceExclusively;

	}

	@Override
	public IToken evaluate(ICharacterScanner scanner) {
		return evaluate(scanner, false);
	}

	@Override
	public IToken getSuccessToken() {
		return successToken;
	}

	@Override
	public IToken evaluate(ICharacterScanner scanner, boolean resume) {	
		int c = scanner.read();
		scanner.unread();
		boolean commentMode = false;
		if (c == startSequnce[0]) {
			if (sequenceDetected(scanner, startSequnce)) {
				forward(scanner, startSequnce.length);
				int read = startSequnce.length + 1;
				while ((c = scanner.read()) != ICharacterScanner.EOF) {
					read++;

					if (!commentMode) {
						// if we're not in comment-mode, check if we have to
						// switch now
						scanner.unread();
						if (sequenceDetected(scanner, comment)) {
							commentMode = true;
						}
						scanner.read();
					} else {
						// until we're in comment-mode, ignore all until
						// newLine-Detection
						if (isNewline(c)) {
							commentMode = false;
						}
					}

					if (!commentMode) {
						for (char[] endSeq : endSequences) {
							if (endSeq[0] == c) {
								scanner.unread(); // check completely
								if (sequenceDetected(scanner, endSeq)) {
									boolean ignoreThis = false;
									// check if it is not ignore
									for (char[] ign : this.ignore) {
										if (sequenceDetected(scanner, ign)) {
											ignoreThis = true;
											break;
										}
									}
									if (!ignoreThis) {
										if (!endSequenceExclusively) {
											forward(scanner, endSeq.length);
										}										
										return successToken;
									}
								}
								// if not, read again
								scanner.read();
							}
						}
					}

				}
				if (c == ICharacterScanner.EOF && eofisend) {					
					return successToken;
				}
				rewind(scanner, read);
			}
		}
		return Token.UNDEFINED;
	}

	/**
	 * @param c
	 * @return
	 */
	private boolean isNewline(int c) {
		if (System.lineSeparator().indexOf(c) >= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param endSequences2
	 * @return
	 */
	protected String toS(char[][] seqs) {
		String str = "[";
		String sep = "";
		for (char[] s : seqs) {
			str = str + sep + toS(s);
			sep = ", ";
		}
		str = str + "]";
		return str;
	}

	private boolean sequenceDetected(ICharacterScanner scanner, char[] sequence) {
		int read = 0;
		boolean success = true;
		for (int i = 0; i < sequence.length; i++) {
			int c = scanner.read();
			read++;
			if (c != sequence[i]) {
				success = false;
				if (c == ICharacterScanner.EOF) {
					success = this.eofisend;
				}
				break;
			}

		}

		rewind(scanner, read);
		return success;
	}

	private void rewind(ICharacterScanner scanner, int chars) {
		for (int i = 0; i < chars; i++) {
			scanner.unread();
		}
	}

	private void forward(ICharacterScanner scanner, int chars) {
		for (int i = 0; i < chars; i++) {
			scanner.read();
		}
	}

	private String toS(char[] chars) {
		String s = "";
		for (char c : chars) {
			s = s + Character.toString(c);
		}
		return s;
	}

}
