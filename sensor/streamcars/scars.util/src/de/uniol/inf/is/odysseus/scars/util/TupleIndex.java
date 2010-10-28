package de.uniol.inf.is.odysseus.scars.util;

import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;

/**
 * Repräsentiert einen Indexeintrag in einem Tupelpfad. Der Indexeintrag
 * speichert den MVRelationalTuple<?>, worin sich der Indexeintrag verweist.
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

	private MVRelationalTuple<?> parent;
	private int valueIndex;
	private SDFAttribute attribute;
	private Object value;

	// Interner Konstruktor
	TupleIndex(MVRelationalTuple<?> parent, int valueIndex, SDFAttribute attribute) {
		this.parent = parent;
		this.valueIndex = valueIndex;
		this.attribute = attribute;
		this.value = this.parent.getAttribute(this.valueIndex);
	}

	TupleIndex(TupleIndex other) {
		this.parent = other.getParent();
		this.valueIndex = other.valueIndex;
		this.attribute = other.attribute;
		this.value = other.value;
	}

	/**
	 * Liefert den übergeordneten MVRelationalTuple<?>, worin der Indexeintrag
	 * verweist. Der Index kann über getValueIndex() geliefert werden.
	 * 
	 * @return Übergeordnetes MVRelationalTuple<?>
	 */
	public MVRelationalTuple<?> getParent() {
		return parent;
	}

	/**
	 * Liefert den Index innerhalb des MVRelationalTuple<?>, worin sich das
	 * repräsentierte Objekt befindet. Das entsprechende MVRelationalTupel<?>
	 * ist über getParent() erreichbar.
	 * 
	 * @return Index innerhalb der MVRelationalTuple<?>
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
	public void setValue(Object obj) {
		parent.setAttribute(valueIndex, obj);
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
