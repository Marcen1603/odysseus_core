package de.uniol.inf.is.odysseus.scars.emep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, einen übergebenen Double-Wert zu quadrieren.
 */
public class SquareValue extends AbstractFunction<Object>{

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
		return "SquareValue";
	}

	/*
	 * Liefert den quadrierten Wert zurück.
	 */
	@Override
	public Object getValue() {
		double dd = (Double)getInputValue(0)*(Double)getInputValue(0);
		return dd;
	}

	/*
	 * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
	 */
	@Override
	public Class<? extends Object> getType() {
		return double.class;
	}

}
