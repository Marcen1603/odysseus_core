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
package de.uniol.inf.is.odysseus.core.physicaloperator;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.core.IClone;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

/**
 * Diese Klasse verarbeitet den in einem Operator u.U. notwendigen Heap, der die richtige Sortierung
 * der Elemente vor der Weitergabe in den Ausgabedatenstrom des Operators wiederherstellt.
 * 
 *
 * @param <R> Datentyp der Elemente, die Verarbeitet werden sollen.
 */
public interface ITransferArea<R extends IStreamObject<?>, W extends IStreamObject<?>> extends ISyncArea<R,W>,IClone, Serializable {
	
	void init(ITransfer<W> sendTo,  int inputPortCount);
	void setTransfer(ITransfer<W> po);

	@Override
	ITransferArea<R,W> clone();
}
