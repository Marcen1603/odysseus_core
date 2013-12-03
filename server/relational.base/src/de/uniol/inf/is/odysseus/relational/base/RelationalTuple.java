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
package de.uniol.inf.is.odysseus.relational.base;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

/**
 * Klasse repraesentiert ein Tupel im relationalen Modell und dient als
 * Austauschobjekt der relationalen Planoperatoren Kann mit Hilfe einer Oracle
 * Loader Zeile initialisiert werden
 * 
 * @author Marco Grawunder, Jonas Jacobi
 * @deprecated use Tuple instead
 */
@Deprecated
public class RelationalTuple<T extends IMetaAttribute> extends Tuple<T> {

	private static final long serialVersionUID = 5138561279810299613L;

	/**
	 * Allows subclasses to call the implicit super constructor. To not allow
	 * other classes to use the constructor it is protected.
	 */
	protected RelationalTuple() {
	}

	/**
	 * Erzeugt ein leeres Tuple ohne Schemainformationen
	 * 
	 * @param attributeCount
	 *            Anzahl der Attribute des Tuples
	 */
	public RelationalTuple(int attributeCount) {
		super(attributeCount, false);
	}
	
	public RelationalTuple(Tuple<T> copy) {
		super(copy);
	}

	/**
	 * Erzeugt ein neues Tuple mit Attributen und ohne Schemainformationen
	 * 
	 * @param attributes
	 *            Attributbelegung des neuen Tuples
	 */
	public RelationalTuple(Object[] attributes) {
		super(attributes, false);
	}

	}