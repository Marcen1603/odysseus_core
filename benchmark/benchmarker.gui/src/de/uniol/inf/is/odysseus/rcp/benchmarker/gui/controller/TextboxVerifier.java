package de.uniol.inf.is.odysseus.rcp.benchmarker.gui.controller;

import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;

/**
 * Diese Klasse �berpr�ft, dass Symbole nur aus Zahlen bestehen d�rfen
 * 
 * @author Stefanie Witzke
 *
 */
public class TextboxVerifier implements VerifyListener {

	/**
	 * Es d�rfen nur Zahlen eingetragen
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
