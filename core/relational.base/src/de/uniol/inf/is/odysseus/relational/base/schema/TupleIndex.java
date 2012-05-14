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
package de.uniol.inf.is.odysseus.relational.base.schema;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * Repräsentiert einen Indexeintrag in einem Tupelpfad. Der Indexeintrag
 * speichert den MVTuple<?>, worin sich der Indexeintrag verweist.
 * Über getParent() lässt sich dieser abrufen.
 * <p>
 * Der dem Indexeintrag zugewiesene Wert kann abgerufen und gesetzt werden. Die
 * Klasse kann von Clients nicht instanziiert werden. Sie wird innerhalb der
 * TupleIndexPath-Klasse verwendet.
 * <p>
 * Von dieser Klasse sollte nicht abgeleitet werden.
 * 
 * @author Timo Michelsen
 * 
 */
public class TupleIndex {

	private Object parent;
	private int valueIndex;
	private SDFAttribute attribute;
	private Object value;

	// Interner Konstruktor
	@SuppressWarnings({ "rawtypes", "unchecked" })
	TupleIndex(Object parent, int valueIndex, SDFAttribute attribute) {
		this.parent = parent;
		this.valueIndex = valueIndex;
		this.attribute = attribute;
		if( parent instanceof Tuple)
			this.value = ((Tuple)this.parent).getAttribute(this.valueIndex);
		else if( parent instanceof List)
			this.value = ((List<Object>)this.parent).get(this.valueIndex);
	}

	TupleIndex(TupleIndex other) {
		this.parent = other.getParent();
		this.valueIndex = other.valueIndex;
		this.attribute = other.attribute;
		this.value = other.value;
	}

	/**
	 * Liefert den übergeordneten MVTuple<?>, worin der Indexeintrag
	 * verweist. Der Index kann über getValueIndex() geliefert werden.
	 * 
	 * @return Übergeordnetes MVTuple<?>
	 */
	public Object getParent() {
		return parent;
	}

	/**
	 * Liefert den Index innerhalb des MVTuple<?>, worin sich das
	 * repräsentierte Objekt befindet. Das entsprechende MVRelationalTupel<?>
	 * ist über getParent() erreichbar.
	 * 
	 * @return Index innerhalb der MVTuple<?>
	 */
	public int getValueIndex() {
		return valueIndex;
	}

	/**
	 * Liefert den tatsächlichen Wert im Tupel. Dieser Befehl ist äquivalent zu
	 * <code>index.getParent().getAttribute(index.getValueIndex())</code>
	 * 
	 * @return Wert des Tupels.
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Setzt den tatsächlichen Wert im Tupel. Es ist darauf zu achten, dass der
	 * Typ des Wertes mit dem alten Wert übereinstimmt. Dies wird in der Methode
	 * nicht geprüft.
	 * 
	 * @param obj
	 *            Neuer Wert.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setValue(Object obj) {
		if( parent instanceof Tuple)
			((Tuple)parent).setAttribute(valueIndex, obj);
		else if( parent instanceof List ) 
			((List<Object>)parent).set(valueIndex, obj);
	}

	/**
	 * Liefert zu diesem Tupelobjekt korrespondierende Schemaobjekt. Darüber
	 * lassen sich genaue Schemainformationen über das Tupelobjekt abrufen.
	 * 
	 * @return Korrespondierendes Schemaobjekt.
	 */
	public SDFAttribute getAttribute() {
		return attribute;
	}

	/**
	 * Liefert den Index im Tupel als Zahl. Entspricht getValueIndex()
	 * 
	 * @return Index im Tupel als Zahl.
	 */
	public int toInt() {
		return valueIndex;
	}

	@Override
	public String toString() {
		return String.valueOf(valueIndex);
	}

	/**
	 * Erstellt eine tiefe Kopie des aktuellen TupleIndex. Der entsprechende
	 * SDFAttribute wird nicht geklont.
	 */
	@Override
	public TupleIndex clone() {
		return new TupleIndex(this);
	}
	
	@Override
	public boolean equals(Object obj) {
		if( !(obj instanceof TupleIndex))
			return false;
		
		if( obj == this ) return true;
		
		return valueIndex == ((TupleIndex)obj).valueIndex;
	}
}
