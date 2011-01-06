package de.uniol.inf.is.odysseus.scars.emep.functions;

import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, die Inverse einer Matrix zu berechnen.
 */
public class MatrixInvert extends AbstractFunction<Object> {

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
		return "MatrixInv";
	}

	/*
	 * Liefert die Inverse einer übergebenen Matrix zurück.
	 */
	@Override
	public Object getValue() {
		return new RealMatrixImpl((double[][])getInputValue(0)).inverse().getData();
	}

	/*
	 * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
	 */
	@Override
	public Class<? extends Object> getType() {
		return Object.class;
	}

}
