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
package de.uniol.inf.is.odysseus.sourcedescription.sdf.predicate;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.SDFElement;

public abstract class SDFLogicalOperator extends SDFElement {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2268751063961164179L;

	public SDFLogicalOperator(String URI) {
		super(URI);
	}

    /**
     * Wertet des Audruck mit Hilfe des eigenen logischen Operators
     * aus
     * @param left
     * @param right
     * @return die Wahrheitswert der Auswertung
     */
	abstract public boolean evaluate(boolean left, boolean right);
}