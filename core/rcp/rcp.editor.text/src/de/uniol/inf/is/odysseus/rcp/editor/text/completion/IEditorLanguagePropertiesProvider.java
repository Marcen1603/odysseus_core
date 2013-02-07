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

import de.uniol.inf.is.odysseus.core.planmanagement.executor.IExecutor;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.rcp.editor.text.editors.formatting.IOdysseusScriptFormattingStrategy;

/**
 * @author Dennis Geesen
 *
 */
public interface IEditorLanguagePropertiesProvider {
	
	public List<Character> getTokenSplitters();
	public List<String> getCompletionSuggestions(String currentToken, String lastToken, IExecutor executor, ISession iSession, IDocument document, int offset);
	public boolean ignoreWhitespaces();
	public String supportsParser();
	public List<String> getTerminals();	
	public IOdysseusScriptFormattingStrategy getFormattingStrategy();		
}
