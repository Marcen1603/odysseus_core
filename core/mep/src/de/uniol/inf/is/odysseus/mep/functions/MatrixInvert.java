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
 * Diese Klasse dient dazu, die Inverse einer Matrix zu berechnen.
 */
public class MatrixInvert extends AbstractFunction<Object> {

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
		return "MatrixInv";
	}

	/*
	 * Liefert die Inverse einer �bergebenen Matrix zur�ck.
	 */
	@Override
	public Object getValue() {
		double[][] i = null;
		try {
			i = new RealMatrixImpl((double[][]) getInputValue(0)).inverse()
					.getData();
		} catch (Exception ex) {
			System.out.println(getInputValue(0));
			i = getInputValue(0);
		}
		return i;
	}

	/*
	 * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
	 */
	@Override
	public Class<? extends Object> getReturnType() {
		return Object.class;
	}
	
	public Class<?>[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 0){
			throw new IllegalArgumentException("AbsValue has only 1 argument.");
		}
		else{
			Class<?>[] accTypes = new Class<?>[1];
			accTypes[0] = double[][].class;
			return accTypes;
		}
	}

}
