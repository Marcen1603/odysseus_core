/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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

package de.uniol.inf.is.odysseus.rcp.editor.text.editors.completion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.KeywordRegistry;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.completion.IEditorLanguagePropertiesProvider;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.DocumentUtils;
import de.uniol.inf.is.odysseus.script.parser.IPreParserKeyword;

/**
 * 
 * @author Dennis Geesen Created at: 27.10.2011
 */
public class OdysseusScriptCompletionProcessor implements IContentAssistProcessor {

	private final IContextInformation[] NO_CONTEXTS = {};
	private final char[] PROPOSAL_ACTIVATION_CHARS = { '#' };
	private ICompletionProposal[] NO_COMPLETIONS = {};

	@Override
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		try {
			if (OdysseusRCPEditorTextPlugIn.getExecutor() == null) {
				return null;
			}
			IDocument document = viewer.getDocument();
			String prefix = lastWord(document, offset);
			String tokenBefore = tokenBefore(document, offset);
			int qlen = prefix.length();
			List<String> words = new ArrayList<String>();
			List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
			String token = tokenBefore.trim().toUpperCase().substring(1);
			// first we look, if we are behind a script command like #PARSER, so
			// we try to loads its possible parameters
			boolean keyWordSearchfailed = false;
			try {
				IPreParserKeyword keyword = OdysseusRCPEditorTextPlugIn.getScriptParser().getPreParserKeywordRegistry().createKeywordExecutor(token);
				words.addAll(keyword.getAllowedParameters());
			} catch (Exception e) {
				keyWordSearchfailed = true;
			}
			// if we are not behind a #-Keyword, we try other things...
			if (keyWordSearchfailed) {
				// maybe, we currently started to write a keyword, then add all
				// possible words
				if (tokenBefore.trim().startsWith("#")) {
					words.addAll(getScriptKeywords());
				} else {
					// if not, we're going to load parser dependent completion
					// providers
					if (OdysseusRCPEditorTextPlugIn.getEditorCompletionProviders().size() > 0) {
						String parser = DocumentUtils.findValidParserAtPosition(document, offset);
						if (!parser.isEmpty()) {
							IEditorLanguagePropertiesProvider ecp = OdysseusRCPEditorTextPlugIn.getEditorCompletionProvider(parser);
							if (ecp != null) {
								String currentToken = lastWord(document, offset, ecp.getTokenSplitters(), ecp.ignoreWhitespaces());
								String beforeCurrentToken = lastLastWord(document, offset, ecp.getTokenSplitters(), ecp.ignoreWhitespaces());
								List<String> parserWords = ecp.getCompletionSuggestions(currentToken, beforeCurrentToken, OdysseusRCPEditorTextPlugIn.getExecutor(),
										OdysseusRCPPlugIn.getActiveSession(), document, offset);
								words.addAll(parserWords);
//								for (String s : parserWords) {									
//									result.add(new CompletionProposal(s, offset, 0, s.length()));
//								}
							}
						}
					} else {
						// if there are nothing, we add all keywords and
						// constants we know
						words.addAll(getScriptKeywords());
						words.addAll(getKeywordsFromRegistry());
					}
				}
			}

			for (String word : words) {
				if (word.toUpperCase().startsWith(prefix.toUpperCase())) {
					result.add(new CompletionProposal(word, offset - qlen, qlen, word.length()));
				}
			}

			return result.toArray(new ICompletionProposal[result.size()]);
		} catch (Exception e) {
			// ... log the exception ...
			return NO_COMPLETIONS;
		}
	}

	private static String tokenBefore(IDocument doc, int offset) {
		try {
			int n = offset - 1;
			while (n >= 0 && Character.isSpaceChar(doc.getChar(n))) {
				n--;
			}
			return lastWord(doc, n + 1);
		} catch (BadLocationException e) {
			return "";
		}
	}

	private static String lastWord(IDocument doc, int offset) {
		try {
			for (int n = offset - 1; n >= 0; n--) {
				char c = doc.getChar(n);
				if (!(Character.isJavaIdentifierPart(c) || c == '#' || c == '=' || c == ',' || c == '\'' || c == '{' || c == '}' || c == '[' || c == ']' || c == '(' || c == ')'))
					return doc.get(n + 1, offset - n - 1);
			}
		} catch (BadLocationException e) {
		}
		return "";
	}

	private static String lastWord(IDocument doc, int offset, List<Character> stopChars, boolean ignoreWhitespaces) {
		try {
			for (int n = offset - 1; n >= 0; n--) {
				char c = doc.getChar(n);
				if (ignoreWhitespaces && Character.isWhitespace(c)) {
					continue;
				}
				if ((stopChars.contains(c))) {
					return doc.get(n, offset - n);
				}
			}
		} catch (BadLocationException e) {
		}
		return "";
	}

	private static String lastLastWord(IDocument doc, int offset, List<Character> stopChars, boolean ignoreWhitespaces) {
		boolean foundFirst = false;
		int foundAt = offset;
		try {
			for (int n = offset - 1; n >= 0; n--) {
				char c = doc.getChar(n);
				if (ignoreWhitespaces && Character.isWhitespace(c)) {
					continue;
				}
				if ((stopChars.contains(c))) {
					if (!foundFirst) {
						foundAt = n;
						foundFirst = true;
					} else {
						return doc.get(n, foundAt - n);
					}
				}
			}
		} catch (BadLocationException e) {
		}
		return "";
	}

	private static List<String> getScriptKeywords() {
		List<String> words = new ArrayList<String>();
		String scriptkeywords[] = OdysseusRCPEditorTextPlugIn.getScriptParser().getKeywordNames().toArray(new String[] {});
		for (int i = 0; i < scriptkeywords.length; i++) {
			words.add("#" + scriptkeywords[i]);
		}
		Collections.sort(words);
		return words;
	}

	private static List<String> getKeywordsFromRegistry() {
		List<String> words = new ArrayList<String>();
		for (String grp : KeywordRegistry.getInstance().getKeywordGroups()) {
			for (String word : KeywordRegistry.getInstance().getKeywords(grp)) {
				if (!words.contains(word)) {
					words.add(word);
				}
			}
		}
		Collections.sort(words);
		return words;
	}

	@Override
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		return NO_CONTEXTS;
	}

	@Override
	public char[] getCompletionProposalAutoActivationCharacters() {
		return PROPOSAL_ACTIVATION_CHARS;
	}

	@Override
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}

	@Override
	public String getErrorMessage() {
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

}
