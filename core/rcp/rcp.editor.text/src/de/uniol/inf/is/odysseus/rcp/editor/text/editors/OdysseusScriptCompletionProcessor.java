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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.rcp.OdysseusRCPPlugIn;
import de.uniol.inf.is.odysseus.rcp.editor.text.KeywordRegistry;
import de.uniol.inf.is.odysseus.rcp.editor.text.OdysseusRCPEditorTextPlugIn;
import de.uniol.inf.is.odysseus.script.parser.PreParserKeywordRegistry;
import de.uniol.inf.is.odysseus.usermanagement.client.GlobalState;

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
			IDocument document = viewer.getDocument();
			String prefix = lastWord(document, offset);
			String tokenBefore = tokenBefore(document, offset);
			int qlen = prefix.length();
			String words[] = {};
			List<ICompletionProposal> result = new ArrayList<ICompletionProposal>();
			if(tokenBefore.trim().equalsIgnoreCase("FROM")){
				Set<Entry<String, ILogicalOperator>> sources = GlobalState.getActiveDatadictionary().getStreamsAndViews(GlobalState.getActiveUser(OdysseusRCPPlugIn.RCP_USER_TOKEN));
				List<String> names = new ArrayList<String>();
				for(Entry<String, ILogicalOperator> e : sources){
					names.add(e.getKey());
				}
				words = names.toArray(new String[]{});
			}else{
				words = getWords();
				
			}
			
			for (int i = 0; i < words.length; i++) {
				if (words[i].toUpperCase().startsWith(prefix.toUpperCase())) {

					result.add(new CompletionProposal(words[i], offset - qlen, qlen, words[i].length()));
				}
			}

			return (ICompletionProposal[]) result.toArray(new ICompletionProposal[result.size()]);
		} catch (Exception e) {
			// ... log the exception ...
			return NO_COMPLETIONS;
		}
	}
	
	
	

	private String tokenBefore(IDocument doc, int offset) {
		try {
			int n = offset - 1;
			while (n >= 0 && Character.isSpaceChar(doc.getChar(n))) {
				n--;
			}
			return lastWord(doc, n+1);
		} catch (BadLocationException e) {
			return "";
		}
	}

	private String lastWord(IDocument doc, int offset) {
		try {
			for (int n = offset - 1; n >= 0; n--) {
				char c = doc.getChar(n);
				if (!(Character.isJavaIdentifierPart(c) || c == '#'))
					return doc.get(n + 1, offset - n - 1);
			}
		} catch (BadLocationException e) {
		}
		return "";
	}

	private String[] getWords() {
		List<String> words = new ArrayList<String>();
		String scriptkeywords[] = PreParserKeywordRegistry.getInstance().getKeywordNames().toArray(new String[] {});
		for (int i = 0; i < scriptkeywords.length; i++) {
			words.add("#" + scriptkeywords[i]);
		}
		for (String grp : KeywordRegistry.getInstance().getKeywordGroups()) {
			for (String word : KeywordRegistry.getInstance().getKeywords(grp)) {
				if (!words.contains(word)) {
					words.add(word);
				}
			}
		}
		String[] result = words.toArray(new String[] {});
		Arrays.sort(result);
		return result;
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

}
