package de.uniol.inf.is.odysseus.scars.emep.functions;

import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, zwei Matrizen zu multiplizieren.
 */
public class MatrixMult extends AbstractFunction<Object> {

	/*
	 * Liefert die Anzahl der möglichen Übergabeparameter zurück.
	 */
	@Override
	public int getArity() {
		return 2;
	}

	/*
	 * Liefert den Namen zurück mit welchem die Funktion aufgerufen wird.
	 */
	@Override
	public String getSymbol() {
		return "MatrixMult";
	}

	/*
	 * Multipliziert zwei Matrizen bzw. eine Matrix und einen Double-Wert und liefert die Ergebnismatrix zurück.
	 */
	@Override
	public Object getValue() {
		if (getInputValue(1) instanceof double[][]) {
			return new RealMatrixImpl((double[][]) getInputValue(0))
					.multiply(new RealMatrixImpl((double[][]) getInputValue(1))).getData();
		} else {
			return new RealMatrixImpl((double[][]) getInputValue(0))
					.scalarMultiply((Double) getInputValue(1)).getData();
		}
	}

	/*
	 * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
	 */
	@Override
	public Class<? extends Object> getType() {
		return Object.class;
	}

}
