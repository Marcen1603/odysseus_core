package de.uniol.inf.is.odysseus.scars.emep.functions;

import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, zwei Matrizen zu addieren.
 */
public class MatrixAdd extends AbstractFunction<Object> {

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
		return "MatrixAdd";
	}

	/*
	 * Addiert zwei übergebene Matrizen und liefert die Ergebnismatrix zurück.
	 */
	@Override
	public Object getValue() {
		return new RealMatrixImpl((double[][]) getInputValue(0)).add(new RealMatrixImpl((double[][]) getInputValue(1))).getData();
	}

	/*
	 * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
	 */
	@Override
	public Class<? extends Object> getType() {
		return Object.class;
	}

}
