package mg.dynaquest.queryexecution.po.relational;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.2 $
 Log: $Log: SortMergeJoinPO.java,v $
 Log: Revision 1.2  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.1  2004/06/23 14:30:00  grawund
 Log: no message
 Log:
 Log: Revision 1.5  2004/06/23 12:04:18  grawund
 Log: Fehler korrigiert
 Log:
 Log: Revision 1.4  2004/06/18 13:43:17  grawund
 Log: no message
 Log:
 Log: Revision 1.3  2004/05/14 09:41:26  grawund
 Log: Umstellung: process_next liefert nun das Objekt, putToBuffer ist private
 Log:
 Log: Revision 1.2  2002/01/31 16:14:32  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

/** SortMergeJoin auf relationalTupeln
 **/

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTupleCorrelator;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

//import java.util.HashMap;

/**
 * @author  Marco Grawunder
 */
public class SortMergeJoinPO extends RelationalPhysicalJoinPO {

	/**
	 * @uml.property  name="firstTupel"
	 */
	private boolean firstTupel = true;

	private ArrayList<RelationalTuple> tmpResultSet = null;

	/**
	 * @uml.property  name="leftElement"
	 * @uml.associationEnd  
	 */
	private RelationalTuple leftElement = null;

	/**
	 * @uml.property  name="rightElement"
	 * @uml.associationEnd  
	 */
	private RelationalTuple rightElement = null;

	// Anmerkung: Der linke Strom ist der Master-Strom

	/**
	 * Initialisierung des Objektes
	 * 
	 * @param compareAttrs
	 *            enthaelt die Zuordnung der Attribute, die verglichen werden
	 *            sollen bsp. 4 --> 7 bedeutet, dass das vierte Attribut im
	 *            linken Strom mit dem siebten Attribut im rechten Strom
	 *            verglichen wird (wobei ein String-Vergleich verwendet wird)
	 */
	public SortMergeJoinPO(RelationalTupleCorrelator compareAttrs) {
		this.setCompareAttrs(compareAttrs);
		this.firstTupel = true;
	}

	public SortMergeJoinPO() {
	    super();
	}

	/**
     * @param joinPO
     */
    public SortMergeJoinPO(JoinPO joinPO) {
        super(joinPO);
        // TODO Auto-generated constructor stub
    }

    public SortMergeJoinPO(SortMergeJoinPO joinPO) {
    	super(joinPO);
		// TODO Auto-generated constructor stub
	}

	public void addCompareAttribute(int pos, int left, int right) {
		if (getCompareAttrs() == null) {
			setCompareAttrs(new RelationalTupleCorrelator(pos));
		}
		getCompareAttrs().setPair(pos, left, right);
	}

	public synchronized boolean process_open() throws POException {
		this.firstTupel = true;
		tmpResultSet = new ArrayList<RelationalTuple>();
		return true;
	}

	public synchronized Object process_next() throws POException, TimeoutException {
		RelationalTuple newAttrList = null;

		if (firstTupel) {
            leftElement = (RelationalTuple) this.getLeftNext(this, -1);
            rightElement = (RelationalTuple) this.getRightNext(this, -1);   
			firstTupel = false;
		}

		// Liegen noch Ergebnisse aus dem letzten Lauf vor,
		// dann dieser erst liefern
		if (tmpResultSet.size() == 0) {

			// Ansonsten so lange im Kreis drehen wie
			// beiden Ströme noch Werte liefern
			while (leftElement != null && rightElement != null) {
				RelationalTuple lastLeftElement = null;
				// Vergleich des Elements im linken Strom mit dem im rechten
				int compare = leftElement.compareTo(rightElement, getCompareAttrs());
				//System.out.println("Compare :"+leftElement+"
				// "+rightElement+"-->"+compare);
				// der linke Strom enthaelt ein Element, welches kleiner ist
				// nächstes Element aus dem linken Strom lesen
				if (compare < 0) {
					leftElement = (RelationalTuple) this.getLeftNext(this, -1);
					continue;
				}
				// Der rechte Strom enthält ein Element das kleiner ist
				// d.h. jetzt rechts weiter lesen
				if (compare > 0) {
					rightElement = (RelationalTuple) this.getRightNext(this, -1);
					continue;
				}

				// Element gefunden:
				// if (compare == 0){ // nicht notwendig, da oben continue
				//System.out.print("Regulär: Verknüpfe "+leftElement+" mit
				// "+rightElement);
				// Erstes gefundenes Objekt verknüpfen
				RelationalTuple res = leftElement.mergeLeft(rightElement,
						this.getCompareAttrs());
				//System.out.println("-->"+res);
				// Und der Ausgabemenge hinzufügen
				tmpResultSet.add(res);

				ArrayList<RelationalTuple> openLeftElems = new ArrayList<RelationalTuple>();
				openLeftElems.add(leftElement);
				// Jetzt prüfen ob noch weitere Elemente des linken
				// Stroms mit dem aktuellen Element des rechten Stroms verknüpft
				// werden können
				while ((leftElement = (RelationalTuple) this.getLeftNext(this, -1)) != null
						&& (leftElement.compareTo(rightElement, getCompareAttrs()) == 0)) {
					openLeftElems.add(leftElement);
					//System.out.print("Linker Vergleich Verknüpfe
					// "+leftElement+" mit "+rightElement);
					res = leftElement
							.mergeLeft(rightElement, this.getCompareAttrs());
					//System.out.println("-->"+res);
					tmpResultSet.add(res);
				}
				// Das hier ist jetzt ein Objekt des linken Stroms, welches
				// nicht mehr geht --> merken!!

				if (leftElement != null)
					lastLeftElement = leftElement;

				// Und nun in Analogie mit dem rechten Strom
				leftElement = (RelationalTuple) openLeftElems.get(0);
				while ((rightElement = (RelationalTuple) this.getRightNext(this, -1)) != null
						&& (leftElement.compareTo(rightElement, getCompareAttrs()) == 0)) {
					// jetzt
					for (int i = 0; i < openLeftElems.size(); i++) {
						leftElement = (RelationalTuple) openLeftElems.get(i);
						//System.out.println("Rechter Vergleich Verknüpfe
						// "+leftElement+" mit\n "+rightElement);
						res = leftElement.mergeLeft(rightElement,
								this.getCompareAttrs());
						//System.out.println("-->"+res);
						tmpResultSet.add(res);
					}
				}
				//}// if (compare == 0)
				leftElement = lastLeftElement;
			}//while (leftElement != null && rightElement != null){

			if (leftElement == null) { // d.h. der linke Strom ist schon beendet
				// dann die übrigen Elemente von rechts holen
				while (rightElement != null)
					rightElement = (RelationalTuple) this.getRightNext(this, -1);
			}

			if (rightElement == null) { // d.h. der rechte Strom ist schon
										// beendet
				// dann die übrigen Elemente von links holen
				while (leftElement != null)
					leftElement = (RelationalTuple) this.getLeftNext(this, -1);
			}

		}
		if (tmpResultSet.size() > 0) {
			newAttrList = (RelationalTuple) tmpResultSet.remove(0);
		}

		return newAttrList;
	}

	protected boolean process_close() throws POException {
		return true;
	}

	public String getInternalPOName() {
		return "SortMergeJoin";
	}

	private void processCompareAttributePair(int pos, NodeList attributePair) {
		// Hier müssen werden jetzt die Attributpositionen verarbeitet
		// in denen steht welche Attribute des linken Stroms mit welchen
		// Attributen des rechten Strom verglichen werden
		int left = -1;
		int right = -1;
		for (int i = 0; i < attributePair.getLength(); i++) {
			Node cNode = attributePair.item(i);

			// uns interessieren nur Element-Knoten
			// Kommentare, Textnodes etc. sind mir egal!
			if (cNode.getNodeType() != Node.ELEMENT_NODE)
				continue;

			if (cNode.getLocalName().equals("leftInput")) {
				Node firstChildNode = cNode.getFirstChild();
				if (firstChildNode != null) {
					left = Integer.parseInt(firstChildNode.getNodeValue());
				}
				continue;
			}

			if (cNode.getLocalName().equals("rightInput")) {
				Node firstChildNode = cNode.getFirstChild();
				if (firstChildNode != null) {
					right = Integer.parseInt(firstChildNode.getNodeValue());
				}
				continue;
			}
		}// for
		this.addCompareAttribute(pos, left, right);
	}

	protected void initInternalBaseValues(NodeList children) {

		int compareAttributesFound = 0;

		for (int i = 0; i < children.getLength(); i++) {
			Node cNode = children.item(i);

			if (cNode.getNodeType() == Node.ELEMENT_NODE
					&& cNode.getLocalName().equals("compareAttributePair")) {
				// hier die Verarbeitung eines Paares
				processCompareAttributePair(compareAttributesFound++, cNode
						.getChildNodes());
				continue;
			}

		}
	}

	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer retBuffer) {

		for (int i = 0; i < this.getCompareAttrs().size(); i++) {
			retBuffer.append(baseIndent + "<compareAttributePair>\n");
			retBuffer.append(baseIndent + indent + "<leftInput>"
					+ this.getCompareAttrs().getSource(i) + "</leftInput>\n");
			retBuffer.append(baseIndent + indent + "<rightInput>"
					+ this.getCompareAttrs().getDestination(i) + "</rightInput>\n");
			retBuffer.append(baseIndent + "</compareAttributePair>\n");
		}
	}

	public SupportsCloneMe cloneMe() {
		return new SortMergeJoinPO(this);
	}

}