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

import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/*
 * Diese Klasse dient dazu, aus einem �bergebenen Double-Wert die Wurzel zu ziehen.
 */
public class SqrtValue extends AbstractFunction<Object>{

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
		return "SqrtValue";
	}

	/*
	 * Liefert die Wurzel des �bergebenen Wertes zur�ck.
	 */
	@Override
	public Object getValue() {
		double d = Math.sqrt((Double)getInputValue(0));
		//System.out.println("Gating: " + d);
		return d;
	}

	/*
	 * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
	 */
	@Override
	public Class<? extends Object> getReturnType() {
		return double.class;
	}
	
	public Class<?>[] getAcceptedTypes(int argPos){
		if(argPos < 0){
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if(argPos > 0){
			throw new IllegalArgumentException(this.getSymbol() + " has only " +this.getArity() + " argument(s).");
		}
		else{
			Class<?>[] accTypes = new Class<?>[1];
			accTypes[0] = Number.class;
			return accTypes;
		}
	}

}
