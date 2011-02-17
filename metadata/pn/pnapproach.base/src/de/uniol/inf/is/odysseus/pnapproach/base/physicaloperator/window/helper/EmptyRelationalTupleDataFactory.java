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
package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window.helper;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * Diese Factory erzeugt ein leeres relationales Tupel. Die Factory wird
 * benötigt, um im PN-Fensteroperator leere negative Tupel erzeugen zu können.
 * 
 * @author Andre Bolles
 *
 * @param <M_In> Metadatentyp des Eingabeelementes (sollte in der Regel ? extends IPosNeg sein)
 * @param <M_Out> Metadatentype des Ausgabeelemenes (sollte in der Regel ? extends IPosNeg sein)
 */
public class EmptyRelationalTupleDataFactory<M_In extends IMetaAttribute, 
										M_Out extends IMetaAttribute> implements IDataFactory<M_In, M_Out, RelationalTuple<M_In>, RelationalTuple<M_Out>> {

	@Override
	public RelationalTuple<M_Out> createData(RelationalTuple<M_In> inElem) {
		return new RelationalTuple<M_Out>(0);
	}

}
