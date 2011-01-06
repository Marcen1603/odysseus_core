package de.uniol.inf.is.odysseus.scars.emep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, einen bestimmten Wert aus einer Matrix auszulesen.
 */
public class MatrixGetEntry extends AbstractFunction<Object>{

	/*
	 * Liefert die Anzahl der möglichen Übergabeparameter zurück.
	 */
	@Override
	public int getArity() {
		return 3;
	}

	/*
	 * Liefert den Namen zurück mit welchem die Funktion aufgerufen wird.
	 */
	@Override
	public String getSymbol() {
		return "MatrixEntry";
	}

	/*
	 * Liefert einen bestimmten Wert aus einer übergebenen Matrix zurück.
	 */
	@Override
	public Object getValue() {
		return ((double[][]) getInputValue(0))[((Double)getInputValue(1)).intValue()][((Double)getInputValue(2)).intValue()];
	}

	/*
	 * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
	 */
	@Override
	public Class<? extends Object> getType() {
		return double.class;
	}

}
