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
package de.uniol.inf.is.odysseus.cep.metamodel.exception;

/**
 * Diese Exception stellt eine fehlende textuelle Beschreibung eines
 * JEP-Ausdrucks (Label) dar, was eine fehlenden JEP-Ausdruck zur Folge hat.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class UndefinedExpressionLabelException extends RuntimeException {

	private static final long serialVersionUID = -7202880806643290866L;

	public UndefinedExpressionLabelException() {
	}

}
