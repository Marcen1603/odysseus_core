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
package de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl;

import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IModelDataContainer;

/**
 * Standardimplementierung der IModelDataContainer-Schnittstelle. Sie stellt
 * den Inhalt eines Knotens dar.
 * 
 * @author Timo Michelsen
 *
 * @param <T>
 */
public class DefaultModelContainer<T> implements IModelDataContainer<T>{

	private T content;
	
	/**
	 * Konstruktor, welcher mit dem gegebenen Inhalt eine
	 * DefaultModelContainer-Instanz erzuegt. null als Inhalt
	 * ist erlaubt.
	 * 
	 * @param content Inhalt des DefaultModelContainer (auch null erlaubt)
	 */
	public DefaultModelContainer( T content ) {
		this.content = content;
	}
	
	@Override
	public T getContent() {
		return content;
	}

}
