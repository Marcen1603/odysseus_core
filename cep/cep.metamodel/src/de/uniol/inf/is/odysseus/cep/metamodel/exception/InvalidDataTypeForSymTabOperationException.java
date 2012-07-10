/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
 * Diese Exception besagt, dass der Datentyp des in der Symboltabelle
 * gespeicherten Wertes von der auszuführenden Symboltabellen-Operation nicht
 * unterstützt wird.
 * 
 * @author Thomas Vogelgesang
 * 
 */
public class InvalidDataTypeForSymTabOperationException extends
		RuntimeException {

	private static final long serialVersionUID = -8861088345694270394L;

	public InvalidDataTypeForSymTabOperationException() {
	}

	public InvalidDataTypeForSymTabOperationException(String arg0) {
		super(arg0);
	}

	public InvalidDataTypeForSymTabOperationException(Throwable arg0) {
		super(arg0);
	}

	public InvalidDataTypeForSymTabOperationException(String arg0,
			Throwable arg1) {
		super(arg0, arg1);
	}

}
