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
package de.uniol.inf.is.odysseus.mep.matrix;

import org.apache.commons.math.linear.RealMatrixImpl;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, die Inverse einer Matrix zu berechnen.
 */
public class MatrixInvert extends AbstractFunction<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2408562706605880454L;

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
	public SDFDatatype getReturnType() {
		return SDFDatatype.OBJECT;
	}
	
	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFDatatype.MATRIX_DOUBLE};
	
	@Override
    public SDFDatatype[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 0){
			throw new IllegalArgumentException("AbsValue has only 1 argument.");
		}
		else{
			return accTypes;
		}
	}

}
