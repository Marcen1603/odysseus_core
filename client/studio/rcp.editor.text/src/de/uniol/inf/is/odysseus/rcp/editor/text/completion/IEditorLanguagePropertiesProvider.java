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
package de.uniol.inf.is.odysseus.rcp.editor.text.completion;

import java.util.List;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Point;

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.IOdysseusScriptFormattingStrategy;

/**
 * @author Dennis Geesen
 *
 */
public interface IEditorLanguagePropertiesProvider {
	
	public List<Character> getTokenSplitters();
	public List<ICompletionProposal> getCompletionSuggestions(String currentToken, String lastSplitters[], IExecutor executor, ISession iSession, IDocument document, int offset, Point selection);
	public boolean ignoreWhitespaces();
	public String getSupportedParser();
	public List<Terminal> getTerminals();	
	public IOdysseusScriptFormattingStrategy getFormattingStrategy();		
}
