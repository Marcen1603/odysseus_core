package mg.dynaquest.queryexecution.po.relational.object;

import java.io.Serializable;
import java.util.List;

/**
 * Klasse repraesentiert ein Tupel im relationalen Modell und dient als
 * Austauschobjekt der relationalen Planoperatoren
 * Kann mit Hilfe einer Oracle Loader Zeile initialisiert werden
 */
public class RelationalTuple implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7119095568322125441L;

	/**
	 * @uml.property  name="attrs" multiplicity="(0 -1)" dimension="1"
	 */
	protected String[] attrs;

	/**
	 * @uml.property  name="delimiter"
	 */
	protected char delimiter = '|';

	// -----------------------------------------------------------------
	// static Hilfsmethoden
	// -----------------------------------------------------------------

	/**
	 * Zaehlen der Anzahl der Attribute anhand der Trennzeichen
	 * 
	 * @param line
	 *            enthaelt die konkatenierten Attribute
	 * @param delimiter
	 *            enthaelt das Trennzeichen
	 * @returns Anzahl der Attribute
	 */
	public static int countNoOfAttribs(String line, char delimiter) {
		int attrNo = 0;
		for (int i = 0; i < line.length(); i++) {
			if (line.charAt(i) == delimiter) {
				attrNo++;
			}
		}
		//System.out.println("CountNoOfAttribs "+line+" "+ delimiter "+
		// (attrNo+1));
		return attrNo + 1; // Es ist ein Feld mehr, als Trennzeichen da sind
	}

	/**
	 * Splittet die Zeile anhand des Trennzeichens in ein Array von Strings mit
	 * den jeweiligen Attributen auf
	 * 
	 * @param line
	 *            enthaelt die konkatenierten Attribute
	 * @param delimiter
	 *            enthaelt das Trennzeichen
	 * @param noOfAttribs
	 *            enthaelt die Anzahl der Attribute
	 * @returns Array mit den Attributen
	 */
	public static String[] splittLineToAttributes(String line, char delimiter,
			int noOfAttribs) {
		// Das geht leider nicht mit dem Tokenizer, da er bei '||' nicht das
		// leere
		// Element dazwischen beachtet
		//  StringTokenizer tokenizer = new StringTokenizer(line, delimiter);

		String[] tokens = new String[noOfAttribs];
		int tokenNo = 0;
		StringBuffer tmpToken = new StringBuffer();
		try {
			for (int i = 0; i < line.length(); i++) {
				if (line.charAt(i) == delimiter) {
					tokens[tokenNo] = new String(tmpToken.toString());
					tmpToken = new StringBuffer();
					tokenNo++;
				} else {
					tmpToken.append(line.charAt(i));
				}
			} // for
			// und den Rest noch
			tokens[tokenNo] = new String(tmpToken.toString());
		} catch (ArrayIndexOutOfBoundsException e) {
			// Die Exception wird hier eingebaut, damit im Fehlerfall
			// mehr Information zur Verfuegung steht
			System.err.println("Fehler in splittLinesToAttributes ");
			System.err.println("Parameter: " + line + "\n delimiter="
					+ delimiter + " noOfAttribs=" + noOfAttribs + " tokenNo="
					+ tokenNo);
			for (int i = 0; i < tokens.length; i++) {
				System.err.println("tokens[" + i + "]=" + tokens[i]);
			}
			throw e;
		}
		return tokens;
	}

	// -----------------------------------------------------------------
	// Attributzugriffsmethoden
	// -----------------------------------------------------------------

	public String getAttribute(int pos) {
		// aus Effizienzgründen keine Überprüfung der Array-Grenzen
		return this.attrs[pos];
	}

	public void setAttribute(int pos, String value) {
		// aus Effizienzgründen keine Überprüfung der Array-Grenzen
		this.attrs[pos] = value;
	}

	public int getAttributeCount() {
		return this.attrs.length;
	}

	// Erzeugen von neuen Objekten, basierend auf dem aktuellen

	/**
	 * erzeugen eines neuen Objektes, in dem nur die Attribute betrachtet
	 * werden, die in der attrList uebergeben werden, die Reihenfolge des neuen
	 * Objektes wird durch die Reihenfolge der Attribute im Array festgelegt
	 * Beispiel: attrList[1]=14,attrList[2]=12 erzeugt ein neues Objekt, welches
	 * die Attribute 14 und 12 enthaelt
	 * 
	 * @param attrList
	 *            erzeugt ein neues Objekt das die Attribute der Positionen aus
	 *            attrList enthält
	 */
	public RelationalTuple restrict(int[] attrList) {
		RelationalTuple newAttrList = new RelationalTuple(attrList.length);
		for (int i = 0; i < attrList.length; i++) {
			newAttrList.setAttribute(i, this.attrs[attrList[i]]);
		}
		return newAttrList;
	}

	/**
	 * zwei Attributlisten verschmelzen (mergen) auf den Positionen, an denen
	 * sich sich unterscheiden
	 */
	protected RelationalTuple merge(RelationalTuple c, RelationalTupleCorrelator compareAttrs,
			boolean overwriteValues) {
		// Erst mal die Tivialfälle abfangen

		try {
			if (this.attrs.length == 0)
				return (RelationalTuple) c.clone();
			if (c.getAttributeCount() == 0)
				return (RelationalTuple) this.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		RelationalTuple newAttrList = new RelationalTuple(this.attrs.length + // alle
																		  // von
																		  // hier
				c.getAttributeCount() - // alle von ueberg.
				compareAttrs.size()); // minus doppelte
		// Es werden zunächst alle Attribute aus der lokalen Menge genommen
		int attrPos = 0;
		for (int i = 0; i < this.attrs.length; i++) {
			// eventuell mit den Werten aus dem uebergebenen Objekt fuellen
			// macht bei Outer-Joins Sinn!
			if (overwriteValues && compareAttrs.containsSource(attrPos)) {
				int val = compareAttrs.findDestination(attrPos);
				newAttrList.setAttribute(attrPos, c.getAttribute(val));
			} else {
				newAttrList.setAttribute(attrPos, this.attrs[i]);
			}
			attrPos++;
		}
		// und dann die Attribute angehaengt, die _nicht_ in den comapareAttrs
		// auftreteten (denn die sind ja schon verarbeitet)
		for (int i = 0; i < c.getAttributeCount(); i++) {
			if (!compareAttrs.containsDestination(i)) {
				newAttrList.setAttribute(attrPos, c.getAttribute(i));
				attrPos++;
			}
		}
		return newAttrList;
	}

	/**
	 * mergeLeft nimmt die Attribute aus den Linken Operanden (also dem lokalen
	 * und haengt die neuen Attribute an
	 */
	public RelationalTuple mergeLeft(RelationalTuple c, RelationalTupleCorrelator compareAttrs) {
		return merge(c, compareAttrs, false);
	}

	/**
	 * mergeRight nimmt die Attribute aus dem linken Operanden und UEBERSCHREIBT
	 * die entsprechenden Attribute mit den Werten aus den entsprechenden
	 * rechten Feldern
	 */
	public RelationalTuple mergeRight(RelationalTuple c, RelationalTupleCorrelator compareAttrs) {
		return merge(c, compareAttrs, true);
	}

	// -----------------------------------------------------------------
	// Vergleichsmethoden
	// -----------------------------------------------------------------

	public boolean equals(Object o) {
		// Vergleich funktioniert nur wenn o auch eine RelationalTuple
		// ist
		if (o instanceof RelationalTuple) {
			return this.compareTo((RelationalTuple) o) == 0;
		}
		return false;
	}

	public int compareTo(Object o) {
		return compareTo((RelationalTuple) o);
	}

	/**
	 * Liefert 0 wenn die beiden Attributlisten gleich sind ansonsten das erste
	 * Element an denen sich die Attributlisten unterscheiden. Die
	 * Sortierreihenfolge ist implizit durch die Position in der Liste gegeben
	 * wenn das aktuelle Objekt kleiner ist ist der Rückgabewert negativ
	 * ansonsten positiv Es wird maximal die kleinere Anzahl der Felder
	 * verglichen
	 */
	public int compareTo(RelationalTuple c) {
		int min = c.getAttributeCount();
		if (min > this.attrs.length) {
			min = this.attrs.length;
		}
		int compare = 0;
		int i = 0;
		for (i = 0; i < min && compare == 0; i++) {
			compare = this.attrs[i].compareTo(c.getAttribute(i));
		}
		if (compare < 0) {
			compare = (-1) * i;
		}
		if (compare > 0) {
			compare = i;
		}
		return compare;
	}

	/**
	 * Spezielle Vergleichsmethode, die zwei AttribteList-Objekte auf den in
	 * compareAttrs angebenen Attributen vergleicht
	 * 
	 * @param compareAttrs
	 *            enhaelt die Attribute, die in den jeweiligen Listen verglichen
	 *            werden sollen 1 --> 3 heisst, dass das "lokale" Attribut 1 mit
	 *            dem "uebergebenen" Attribut 3 verglichen werden soll die
	 *            Reihenfolge der Attribute entscheidet auch ueber die
	 *            Gewichtung, d.h. ob ein Objekt groesser oder kleiner als das
	 *            andere ist. Rueckgabewert ist die Nummer des Attributs welches
	 *            nicht uebereinstimmte (negativ wenn lokal, positiv wenn
	 *            uebergeben). Bei Gleichheit wird 0 zurueckgegeben.
	 */
	public int compareTo(RelationalTuple c, RelationalTupleCorrelator compareAttrs) {
		int compare = 0;
		int source = -1;
		int dest = -1;
		for (int i = 0; i < compareAttrs.size() && compare == 0; i++) {
			source = compareAttrs.getSource(i);
			dest = compareAttrs.getDestination(i);
			compare = this.attrs[source].compareTo(c.getAttribute(dest));
		}
		if (compare < 0) {
			compare = (-1) * (source + 1);
		}
		if (compare > 0) {
			compare = (dest + 1);
		}
		return compare;
	}

	// -----------------------------------------------------------------
	// Ausgabe
	// -----------------------------------------------------------------

	public String toString() {
		StringBuffer retBuff = new StringBuffer();
		for (int i = 0; i < this.attrs.length; i++) {
			retBuff.append(this.attrs[i] + delimiter);
		}
		// das letzte Trennzeichen ist zu viel
		if (retBuff.length() > 0) {
			retBuff.deleteCharAt(retBuff.length() - 1);
		}
		return retBuff.toString();
	}

	// -----------------------------------------------------------------
	// Konstruktoren
	// -----------------------------------------------------------------

	/**
	 * Erzeugt ein neues Object, anhand der Zeile und des Trennzeichens da diese
	 * Implementierung immer zaehlen muss, wieviele Attribute vorhanden sind,
	 * ist es u.U. effizienter den zweiten Konstruktor zu verwenden, da die
	 * Anzahl der Attribute in der Regel konstant ist
	 * 
	 * @param line
	 *            enthaelt die konkatenierten Attribute
	 * @param delimiter
	 *            enthaelt das Trennzeichen
	 */
	public RelationalTuple(String line, char delimiter) {
		// Schritt 1: Zaehlen der Attribute
		int noOfAttribs = countNoOfAttribs(line, delimiter);
		// Schritt 2: auslesen der Attribute
		this.attrs = splittLineToAttributes(line, delimiter, noOfAttribs);
	}

	/**
	 * Erzeugt ein neues Object, anhand der Zeile und des Trennzeichens
	 * 
	 * @param line
	 *            enthaelt die konkatenierten Attribute
	 * @param delimiter
	 *            enthaelt das Trennzeichen
	 * @param noOfAttribs
	 *            enthaelt die Anzahl der Attribute (Effizienzgründe)
	 */
	public RelationalTuple(String line, char delimiter, int noOfAttribs) {
		this.attrs = splittLineToAttributes(line, delimiter, noOfAttribs);
	}

	/**
	 * Erzeugt ein neues leeres Object, zur Erzeugung von Zwischenergebnissen
	 * 
	 * @param noOfAttribs
	 *            enthaelt die Anzahl der Attribute die das Objekt speichern
	 *            koennen soll
	 */
	public RelationalTuple(int noOfAttribs) {
		this.attrs = new String[noOfAttribs];
		for (int i = 0; i < noOfAttribs; i++) {
			this.attrs[i] = "";
		}
	}

		
	public RelationalTuple(List<Object> list) {
		this.attrs = new String[list.size()];
		int i=0;
		for (Object o:list){
			this.attrs[i++] = ""+o;
		}
	}

	public int hashCode() {

		int ret = 0;
		for (int i = 0; i < attrs.length; i++) {
			ret = ret + attrs[i].hashCode();
		}
		return ret;
	}

}