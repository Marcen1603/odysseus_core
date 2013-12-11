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
package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

/**
 * Diese Klasse überprüft, dass Symbole nur aus Zahlen bestehen dürfen
 * 
 * @author Stefanie Witzke
 *
 */
public class TextboxVerifier implements VerifyListener {

	/**
	 * Es dürfen nur Zahlen eingetragen
	 */
	@Override
	public void verifyText(VerifyEvent event) {

		// Es wird nichts erlaubt
		event.doit = false;

		// Holt den Symbol-Typen
		char validatedChar = event.character;

		// Backspace ist erlaubt
		if (validatedChar == '\b') {
			event.doit = true;
		}

		// 0-9 ist erlaubt
		if (Character.isDigit(validatedChar)) {
			event.doit = true;
		}
	}
}
