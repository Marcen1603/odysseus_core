/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.mep.functions;

import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, zwei Matrizen zu multiplizieren.
 */
public class MatrixMult extends AbstractFunction<Object> {

	/*
	 * Liefert die Anzahl der m�glichen �bergabeparameter zur�ck.
	 */
	@Override
	public int getArity() {
		return 2;
	}

	/*
	 * Liefert den Namen zur�ck mit welchem die Funktion aufgerufen wird.
	 */
	@Override
	public String getSymbol() {
		return "MatrixMult";
	}

	/*
	 * Multipliziert zwei Matrizen bzw. eine Matrix und einen Double-Wert und liefert die Ergebnismatrix zur�ck.
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
	public String getReturnType() {
		return "Matrix";
	}
	
	public String[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 1){
			throw new IllegalArgumentException("AbsValue has only 1 argument.");
		}
		else{
			String[] accTypes = new String[1];
			accTypes[0] = "Matrix";
			return accTypes;
		}
	}

}
