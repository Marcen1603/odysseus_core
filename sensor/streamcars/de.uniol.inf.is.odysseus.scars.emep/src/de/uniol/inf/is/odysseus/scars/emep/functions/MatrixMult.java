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
		Object input0 = getInputValue(0);
		Object input1 = getInputValue(1);
		if (input1 instanceof double[][]) {
			double[][] res = new RealMatrixImpl((double[][]) input0)
					.multiply(new RealMatrixImpl((double[][]) input1)).getData();
			return res;
		} else {
			double[][] res = new RealMatrixImpl((double[][]) input0)
					.scalarMultiply((Double) input1).getData();
			return res;
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
