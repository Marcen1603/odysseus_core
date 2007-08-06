package mg.dynaquest.queryexecution.po.relational.object;

/**
 * Diese Klasse dient dazu, in zwei relationalen Tupeln die Positionen zu bestimmen,
 * die jeweils gleich sein sollen. Verwendung findet diese Klasse in den Join-Operatoren,
 * in denen die Elemente des linken Stroms mit den Elementen des rechten Strom in Verbindung 
 * gesetzt werden müssen
 * 
 * Ein Objekt wird mit der Anzahl der zu vergleichenden Attribute initialisiert und dann werden die Positionen
 * jeweils paarweise zugewiesen.
 * 
 *  Beispiel: sei 
 *    l der linke Strom mit dem relationalen Tupel: (A,B,C,D,E) und 
 *    r der rechte Strom mit dem relationalen Tupel: (A,F,D,E,C) und
 *    das Join-Prädikat: l.A == r.A && l.C == r.C 
 *    Diese würde hier wie folgt festgelegt werden:
 *    RelationalTupleCorrelator rel = new RelationalTupleCorrelator(2);
 *    rel.setPair(0,0,0);
 *    rel.setPair(1,2,4);
 *     
 * @author Marco Grawunder
 *
 */


public class RelationalTupleCorrelator {
	/**
	 * @uml.property  name="source" multiplicity="(0 -1)" dimension="1"
	 */
	int[] source;

	/**
	 * @uml.property  name="destination" multiplicity="(0 -1)" dimension="1"
	 */
	int[] destination;

	public RelationalTupleCorrelator(int size) {
		this.source = new int[size];
		this.destination = new int[size];
	}

	public int size() {
		return source.length;
	}

	public void setPair(int pos, int source, int destination) {
		// Tja, leider muss ich hier auch wohl den Fall abfangen,
		// dass groessere Elemente als in das Array passen übergeben
		// werden (da das nicht dauernd passiert, ist die Lösung hier
		// sehr einfach gehalten) Zustand von source und destination
		// ist immer gleich! Deswegen nur source-Test
		if (this.source == null) {
			this.source = new int[pos + 1];
			this.destination = new int[pos + 1];
		}
		if (this.source.length <= pos) {
			int[] tmpArray = new int[pos + 1];
			System.arraycopy(this.source, 0, tmpArray, 0, this.source.length);
			this.source = tmpArray;
			tmpArray = new int[pos + 1];
			System.arraycopy(this.destination, 0, tmpArray, 0,
					this.destination.length);
			this.destination = tmpArray;
		}
		this.source[pos] = source;
		this.destination[pos] = destination;
	}

	public int getSource(int pos) {
		return source[pos];
	}

	public int findSource(int val) {
		for (int i = 0; i < this.size(); i++) {
			if (source[i] == val)
				return i;
		}
		return -1;
	}

	public boolean containsSource(int source) {
		return findSource(source) >= 0;
	}

	public int getDestination(int pos) {
		return destination[pos];
	}

	public int findDestination(int val) {
		for (int i = 0; i < this.size(); i++) {
			if (destination[i] == val)
				return i;
		}
		return -1;
	}

	public boolean containsDestination(int source) {
		return findDestination(source) >= 0;
	}

	public int[] getAllSources() {
		return source;
	}

	public int[] getAllDestinations() {
		return destination;
	}
}