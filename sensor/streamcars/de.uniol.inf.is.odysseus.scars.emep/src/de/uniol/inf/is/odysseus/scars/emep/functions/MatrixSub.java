package de.uniol.inf.is.odysseus.scars.emep.functions;

import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, zwei Matrizen zu subtrahieren.
 */
public class MatrixSub extends AbstractFunction<Object> {

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
		return "MatrixSub";
	}

	/*
	 * Subtrahiert zwei Matrizen und liefert die Ergebnismatrix zurück.
	 */
	@Override
	public Object getValue() {
		double[][] input0 = (double[][]) getInputValue(0);
		Object input1 = getInputValue(1);
		if( !(input1 instanceof double[][]) ){
			System.out.println("Blaaaa");
		}
		double[][] d = new RealMatrixImpl(input0)
				.subtract(new RealMatrixImpl((double[][])input1)).getData();
		return d;
	}

	/*
	 * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
	 */
	@Override
	public Class<? extends Object> getType() {
		return Object.class;
	}

}
