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
 * Diese Klasse dient dazu, den Betrag eines Wertes zu erhalten.
 */
public class GetAbsoluteValue extends AbstractFunction<Object>{

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
		return "AbsValue";
	}

	/*
	 * Liefert den Betrag eines �bergebenen Wertes zur�ck.
	 */
	@Override
	public Object getValue() {
		double d = (Double)getInputValue(0);
		if(d < 0) {
			return (d*(-1));
		} else {
			return d;
		}
	}

	/*
	 * Liefert den Klassentyp des Wertes der durch getValue() berechnet wird.
	 */
	@Override
	public Class<? extends Object> getReturnType() {
		return double.class;
	}

}
