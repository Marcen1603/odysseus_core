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
package de.uniol.inf.is.odysseus.cep.epa.exceptions;

/**
 * Diese Exception wird geworfen, wenn der Wert einer Variablen nicht ermittelt
 * werden konnte. Ursache dafür kann auch das Fehlen der Variablen in der
 * Symboltabelle sein.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class UndeterminableVariableValueException extends RuntimeException {

	private static final long serialVersionUID = 6009994326063071102L;

	public UndeterminableVariableValueException(String msg) {
		super(msg);
	}
}
