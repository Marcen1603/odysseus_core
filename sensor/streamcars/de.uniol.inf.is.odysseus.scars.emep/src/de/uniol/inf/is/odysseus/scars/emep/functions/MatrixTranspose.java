package de.uniol.inf.is.odysseus.scars.emep.functions;

import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, aus einer übergebenen Matrix die Transponierte zu berechnen.
 */
public class MatrixTranspose extends AbstractFunction<Object> {

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
		return "MatrixTrans";
	}

	/*
	 * Liefert die Transponierte der übergebenen Matrix zurück.
	 */
	@Override
	public Object getValue() {
		double[][] v = new RealMatrixImpl((double[][]) getInputValue(0)).transpose().getData();
		return v;
	}

	/*
	 * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
	 */
	@Override
	public Class<? extends Object> getType() {
		return Object.class;
	}

}
