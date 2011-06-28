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
		double d = ((double[][]) getInputValue(0))[((Double)getInputValue(1)).intValue()][((Double)getInputValue(2)).intValue()];
		return d;
	}

	/*
	 * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
	 */
	@Override
	public String getReturnType() {
		return "Double";
	}

	public String[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 2){
			throw new IllegalArgumentException("AbsValue has only 1 argument.");
		}
		else{
			String[] accTypes = new String[1];
			accTypes[0] = "Integer";
			return accTypes;
		}
	}
}
