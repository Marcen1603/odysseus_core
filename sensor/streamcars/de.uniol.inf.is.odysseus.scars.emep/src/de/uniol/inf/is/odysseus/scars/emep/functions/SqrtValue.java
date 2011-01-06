package de.uniol.inf.is.odysseus.scars.emep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, aus einem übergebenen Double-Wert die Wurzel zu ziehen.
 */
public class SqrtValue extends AbstractFunction<Object>{

	/*
	 * Liefert die Anzahl der möglichen Übergabeparameter zurück.
	 */
	@Override
	public int getArity() {
		return 1;
	}

	/*
	 * Liefert den Namen zurück mit welchem die Funktion aufgerufen wird.
	 */
	@Override
	public String getSymbol() {
		return "SqrtValue";
	}

	/*
	 * Liefert die Wurzel des übergebenen Wertes zurück.
	 */
	@Override
	public Object getValue() {
		return Math.sqrt((Double)getInputValue(0));
	}

	/*
	 * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
	 */
	@Override
	public Class<? extends Object> getType() {
		return double.class;
	}

}
