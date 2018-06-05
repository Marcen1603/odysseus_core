/********************************************************************************** 
 * Copyright 2013 The Odysseus Team
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

import org.eclipse.jface.text.rules.ICharacterScanner;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.Token;

/**
 * @author Dennis Geesen
 * 
 */
public class MultiLineBracketRule implements IPredicateRule {

	private IToken successToken;
	private char closeChar;
	private char openChar;

	public MultiLineBracketRule(char open, char close, IToken successToken) {
		this.successToken = successToken;
		this.openChar = open;
		this.closeChar = close;
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
		int read = 1;		
		int c = scanner.read();		
		if (c == openChar) {
			int openCounter = 1;
			while (c != ICharacterScanner.EOF) {				
				c = scanner.read();							
				read++;
				if (c == openChar && openChar!=closeChar) {
					openCounter++;
				}
				if (c == closeChar) {
					openCounter--;
					if (openCounter == 0) {
						return successToken;
					}
				}
			}
			for (int i = 0; i < read; i++) {
				scanner.unread();
			}
		} else {
			scanner.unread();
		}
		return Token.UNDEFINED;
	}
}
