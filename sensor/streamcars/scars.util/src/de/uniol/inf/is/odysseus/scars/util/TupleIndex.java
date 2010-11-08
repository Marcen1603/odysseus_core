package de.uniol.inf.is.odysseus.scars.util;


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

	private int valueIndex;

	// Interner Konstruktor
	TupleIndex(int valueIndex) {
		this.valueIndex = valueIndex;
	}

	TupleIndex(TupleIndex other) {
		this.valueIndex = other.valueIndex;
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
