package de.uniol.inf.is.odysseus.scars.emep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, den Betrag eines Wertes zu erhalten.
 */
public class GetAbsoluteValue extends AbstractFunction<Object>{

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
		return "AbsValue";
	}

	/*
	 * Liefert den Betrag eines übergebenen Wertes zurück.
	 */
	@Override
	public Object getValue() {
		double d = (Double)getInputValue(0);
		if(d < 0) {
			return (d*(-1));
		} else {
			return d;
		}
	}

	/*
	 * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
	 */
	@Override
	public Class<? extends Object> getType() {
		return double.class;
	}

}
