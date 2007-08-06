package mg.dynaquest.queryexecution.po.relational;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.5 $
 Log: $Log: SelectLoaderPO.java,v $
 Log: Revision 1.5  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.4  2004/05/14 09:41:26  grawund
 Log: Umstellung: process_next liefert nun das Objekt, putToBuffer ist private
 Log:
 Log: Revision 1.3  2004/02/25 14:57:27  grawund
 Log: *** empty log message ***
 Log:
 Log: Revision 1.2  2002/01/31 16:14:28  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.io.BufferedReader;
import java.io.FileReader;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.access.PhysicalAccessPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Klasse dient zum Zugriff auf Oracle-Loader-Dateien (bzw. ganz allegemein Dateien, die Datensaetze durch ein Trennzeichen von einandern abgrenzen und jeden Datensatz durch ein Newline trennen)
 */
public class SelectLoaderPO extends PhysicalAccessPO {
    /**
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger logger = Logger.getLogger(this.getClass().getName());
	// --------------------------------------------------------------------
	// Eigenschaftsattribute
	// --------------------------------------------------------------------
	/**
	 * Dieses Attribut enthält die Restriktionsattribute, d.h. hier stehen die POSITIONEN (!) der Attribute drin, die weitergeleitet werden
	 * @uml.property  name="restrictToAttribs" multiplicity="(0 -1)" dimension="1"
	 */
	private int[] restrictToAttribs = null;

	/**
	 * Das Trennzeichen, welches in der Datei zum Abgrenzen der einzelnen Attribute verwendet wird
	 * @uml.property  name="delimiter"
	 */
	private char delimiter;

	/**
	 * Der Dateiname, der Loader-Datei Anmerkung: Es muss nicht unbedingt eine ORACLE-Loader-Datei sein, es ist einfach eine Liste von Attributen, die durch ein Trennzeichen voneinander abgegrenzt werden. Die einzelnen Objekte werden dabei jeweils durch eine Zeile voneinander getrennnext
	 * @uml.property  name="filename"
	 */
	private String filename;

	/**
	 * Anzahl der Attribute, die in dem Loader-File stehen, wird dynamisch beim Open ermittelt und dann zwischengespeichert darf nicht von aussen modifiziert werden
	 * @uml.property  name="noOfAttribs"
	 */
	private int noOfAttribs = -1;

	/**
	 * Nach dem Open enthält in einen Verweis auf den aktuellen Eingabestrom
	 * @uml.property  name="in"
	 */
	private BufferedReader in = null;

	// --------------------------------------------------------------------
	// Konstruktoren
	// --------------------------------------------------------------------

	/**
	 * Damit die Fabrik-Methoden funktionieren, muss immer eine Default-
	 * Konstruktor bereitgestellt werden
	 */
	public SelectLoaderPO() {
		in = null;
		noOfAttribs = -1;
		restrictToAttribs = null;
		delimiter = ' ';
		filename = null;
	}

	/**
	 * Erzeugen eines neuen Select-Loader-Planoperators
	 * 
	 * @param filename
	 *            Der vollständige Pfad zur Loader-Datei
	 * @delimiter das Trennzeichen, mit dem die Attribute von einander seperiert
	 *            werden
	 * @restrictToAttribs ist ein Array, welches die Positionen der Attribute
	 *                    enthaelt auf die die Datei reduziert werden soll
	 */
	public SelectLoaderPO(String filename, char delimiter,
			int[] restrictToAttribs) {
		this.filename = filename;
		this.delimiter = delimiter;
		this.restrictToAttribs = restrictToAttribs;
	}

	// --------------------------------------------------------------------
	// Methoden zum Auslesen und modifizieren der Planparamter
	// --------------------------------------------------------------------

	/**
	 * Legt ein Restriktionsattribut (Position des Attributes) fest die
	 * Positionsangabe muss noch mal ueberdacht werden! String wird verwendet,
	 * damit die Eingabe direkt aus dem XML-File gelesen werden kann
	 */
	public void setRestrictToAttribute(int pos, String attrNo) {
		if (attrNo != null) {
			this.setRestrictToAttribute(pos, Integer.parseInt(attrNo));
		}
	}

	/**
	 * Legt ein Restriktionsattribut (Position des Attributes) fest die
	 * Positionsangabe muss noch mal ueberdacht werden!
	 */
	public  void setRestrictToAttribute(int pos, int attrNo) {
		// Fehlerfaelle anfangen, können auftreten, da diese
		// Werte auch mit Hilfe des XML initialisiert werden können
		// und da weiss ich vorher nicht, wie gross das ist
		if (restrictToAttribs == null) {
			restrictToAttribs = new int[pos + 1];
		}
		// Falls das Array keinen Platz fuer das Element hat muss
		// es vergrößert werden (ich hoffe das arraycopy aus System
		// ist einigermassen effizient
		if (restrictToAttribs.length <= pos) {
			int[] tmpArray = new int[pos + 1];
			System.arraycopy(restrictToAttribs, 0, tmpArray, 0,
					restrictToAttribs.length);
			this.restrictToAttribs = tmpArray;
		}

		// und nun kann der Wert schliesslich zugewiesen werden
		this.restrictToAttribs[pos] = attrNo;
	}

	/**
	 * Wieviele Attribute sollen später im Ergebnis sein, also wieviele
	 * Restriktionsattribute gibt es.
	 */
	public int getNoOfRestrictAttributes() {
		if (this.restrictToAttribs == null) {
			return 0;
		} else {
			return this.restrictToAttribs.length;
		}
	}

	/**
	 * Zum Iterieren durch die Liste der Restriktionsattribute Achtung. Wenn pos
	 * nicht vorhanden ist, gibt es eine Array out of Bounds-Exception!!
	 */
	public int getRestrictToAttribute(int pos) {
		return this.restrictToAttribs[pos];
	}

	/**
	 * Welches Trennzeichen wird in dem File zum Trennen der einzelnen Attribute verwendet?
	 * @uml.property  name="delimiter"
	 */
	public char getDelimiter() {
		return this.delimiter;
	}

	/**
	 * Setzen des Trennzeichen das in dem File zum Trennen der einzelnen Attribute verwendet wird
	 * @uml.property  name="delimiter"
	 */
	public  void setDelimiter(char delimiter) {
		this.delimiter = delimiter;
	}

	/**
	 * Setzen des Trennzeichen das in dem File zum Trennen der einzelnen
	 * Attribute verwendet wird. Von dem String wird nur das erste Zeichen
	 * verwendet!
	 */
	public  void setDelimiter(String delimiter) {
		if (delimiter != null && delimiter.length() > 0) {
			this.delimiter = delimiter.charAt(0);
		}
	}

	/**
	 * Wie lautet das File, welches die Daten liefert
	 * @uml.property  name="filename"
	 */
	public String getFilename() {
		return this.filename;
	}

	/**
	 * Festlegen der Datei, die die Daten liefert. ACHTUNG dieser Name kann nach dem Ausfuehren von open nicht mehr verändert werden (wird bis zum nächsten open ignoriert!)
	 * @uml.property  name="filename"
	 */
	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Wieviele Attribute sind in der Datei vorhanden ist erst nach dem Aufruf von Open bekannt!
	 * @uml.property  name="noOfAttribs"
	 */
	public int getNoOfAttribs() {
		return noOfAttribs;
	}

	/**
	 * Liefert den internen Namen des POs. Dieser kann bei Anzeigen immer dann
	 * verwendet werden, wenn der PO keinen eigenen Namen bekommen hat.
	 */
	public String getInternalPOName() {
		return "SelectLoader";
	}

	// --------------------------------------------------------------------
	// ONC-Methoden
	// --------------------------------------------------------------------

	public  boolean process_open() throws POException {

		// Wenn die Datei bereits offen ist, darf kein open aufgerufen werden
		// im Moment muss das noch die aufrufende Instanz garantieren!

		boolean retVal = true;
		try {
			// Erst mal die Anzahl der Attribute ermitteln
			in = new BufferedReader(new FileReader(filename));
			String line = in.readLine();
			noOfAttribs = RelationalTuple.countNoOfAttribs(line, delimiter);
			in.close();
			// und dann "richtig" oeffnen
			in = new BufferedReader(new FileReader(filename));
		} catch (Exception e) {
			retVal = false;
			POException ex = new POException(
					"Fehler in SelectLoaderPO.open beim Zugriff auf "
							+ filename);
			ex.fillInStackTrace();
			ex.printStackTrace();
			throw ex;
		}
		return retVal;
	}

	public  boolean process_close() throws POException {
		boolean retVal = true;
		try {
			in.close();
			System.out.println(in.toString() + " geschlossen");
		} catch (Exception e) {
			retVal = false;
			POException ex = new POException("Fehler in SelectLoaderPO.next()");
			ex.fillInStackTrace();
			throw ex;
		}
		return retVal;
	}

	//--------------------------------------------------------------------------
	// Process-Next
	//--------------------------------------------------------------------------
	public Object process_next() throws POException {
		//synchronized (in) {
			RelationalTuple newAttrList = null;
			try {
				String line = null;
				line = in.readLine();
				logger.debug(this.getPOName()+" "+line+" gelesen");
				if (line != null) {
					newAttrList = new RelationalTuple(line, delimiter,
							noOfAttribs);
					if (this.restrictToAttribs != null) {
						newAttrList = newAttrList.restrict(restrictToAttribs);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				POException ex = new POException(
						"Fehler in SelectLoaderPO.next()");
				ex.fillInStackTrace();
				throw ex;
			}
			return newAttrList;
		//}
	}

	//--------------------------------------------------------------------------
	// XML-Initialisierungs- und Serialisierungsmethoden
	//--------------------------------------------------------------------------

	protected void initInternalBaseValues(NodeList children) {
		int restrAttributesFound = 0;
		for (int i = 0; i < children.getLength(); i++) {
			Node cNode = children.item(i);

			// uns interessieren nur Element-Knoten
			// Kommentare, Textnodes etc. sind mir egal!
			if (cNode.getNodeType() != Node.ELEMENT_NODE)
				continue;

			if (cNode.getLocalName().equals("filename")) {
				Node firstChildNode = cNode.getFirstChild();
				if (firstChildNode != null) {
					this.setFilename(firstChildNode.getNodeValue());
				}
				continue;
			}

			if (cNode.getLocalName().equals("delimiter")) {
				Node firstChildNode = cNode.getFirstChild();
				if (firstChildNode != null) {
					this.setDelimiter(firstChildNode.getNodeValue());
				}
				continue;
			}

			if (cNode.getLocalName().equals("restrictToAttribute")) {
				Node firstChildNode = cNode.getFirstChild();
				if (firstChildNode != null) {
					this.setRestrictToAttribute(restrAttributesFound++,
							firstChildNode.getNodeValue());
				}
				continue;
			}

		}
	}

	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer retBuffer) {
		retBuffer.append(baseIndent + "<filename>" + this.getFilename()
				+ "</filename>\n");
		retBuffer.append(baseIndent + "<delimiter>" + this.getDelimiter()
				+ "</delimiter>\n");
		for (int i = 0; i < this.getNoOfRestrictAttributes(); i++) {
			retBuffer.append(baseIndent + "<restrictToAttribute>"
					+ this.getRestrictToAttribute(i)
					+ "</restrictToAttribute>\n");
		}
	}

	public SupportsCloneMe cloneMe() {
		// Diese Klasse kann im Moment nicht verwendet werden
		// es muss noch eine Anpassung der Ausgabewerte erfolgen
		throw new NotImplementedException();
	}

}