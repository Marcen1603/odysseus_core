package de.uniol.inf.is.odysseus.scars.emep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, den Betrag eines Wertes zu erhalten.
 */
public class GetAbsoluteValue extends AbstractFunction<Object>{

	/*
	 * Liefert die Anzahl der m�glichen �bergabeparameter zur�ck.
	 */
	@Override
	public int getArity() {
		return 1;
	}

	/*
	 * Liefert den Namen zur�ck mit welchem die Funktion aufgerufen wird.
	 */
	@Override
	public String getSymbol() {
		return "AbsValue";
	}

	/*
	 * Liefert den Betrag eines �bergebenen Wertes zur�ck.
	 */
	@Override
	public Object getValue() {
		if((Double)getInputValue(0) < 1) {
			return ((Double)getInputValue(0)*(-1));
		} else {
			return getInputValue(0);
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
