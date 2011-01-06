package de.uniol.inf.is.odysseus.scars.emep.functions;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, einen bestimmten Wert aus einer Matrix auszulesen.
 */
public class MatrixGetEntry extends AbstractFunction<Object>{

	/*
	 * Liefert die Anzahl der m�glichen �bergabeparameter zur�ck.
	 */
	@Override
	public int getArity() {
		return 3;
	}

	/*
	 * Liefert den Namen zur�ck mit welchem die Funktion aufgerufen wird.
	 */
	@Override
	public String getSymbol() {
		return "MatrixEntry";
	}

	/*
	 * Liefert einen bestimmten Wert aus einer �bergebenen Matrix zur�ck.
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
