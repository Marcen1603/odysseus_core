package mg.dynaquest.queryexecution.po.relational;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $
 Version: $Revision: 1.3 $
 Log: $Log: HashJoinPO.java,v $
 Log: Revision 1.3  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.2  2004/08/03 13:20:02  grawund
 Log: Neue Schnittstellen zum Auslesen der gelesen Daten bzw. der Hashtabellen
 Log:
 Log: Revision 1.1  2004/06/23 12:04:46  grawund
 Log: Einfache Version eines HashJoins
 Log:
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTupleCorrelator;
import org.w3c.dom.NodeList;

public class HashJoinPO extends RelationalPhysicalJoinPO implements UsesHashtable {

	private ArrayList<RelationalTuple> tmpResults = null;

	/**
	 * @uml.property  name="firstRun"
	 */
	private boolean firstRun = true;

	/**
	 * @uml.property  name="buildInput"
	 * @uml.associationEnd  multiplicity="(0 -1)" ordering="true" elementType="mg.dynaquest.queryexecution.po.relational.object.RelationalTuple" qualifier="key:mg.dynaquest.queryexecution.po.relational.object.RelationalTuple java.util.ArrayList"
	 */
	private HashMap<RelationalTuple, ArrayList<RelationalTuple>> buildInput = null;

	public HashJoinPO(RelationalTupleCorrelator compareAttrs) {
		this.setCompareAttrs(compareAttrs);
	}
	
	public HashJoinPO(){
	    super();
	    
	}

	
	
	public HashJoinPO(HashJoinPO joinPO) {
		super(joinPO);
	}

	public void addCompareAttribute(int pos, int left, int right) {
		if (getCompareAttrs() == null) {
			setCompareAttrs(new RelationalTupleCorrelator(pos));
		}
		getCompareAttrs().setPair(pos, left, right);
	}

	public synchronized boolean process_open() throws POException {
		firstRun = true;
		buildInput = new HashMap<RelationalTuple, ArrayList<RelationalTuple>>();
		tmpResults = new ArrayList<RelationalTuple>();
		return true;
	}

	public synchronized Object process_next() throws POException, TimeoutException {
		RelationalTuple newAttrList = null;
		RelationalTuple leftElement = null;
		RelationalTuple rightElement = null;

		// Auf dem linken Eingabestrom (der hoffentlich der kleinere ist)
		// eine HashMap aufbauen
		if (firstRun) {
			while ((leftElement = (RelationalTuple) this.getLeftNext(this, -1)) != null) {
				RelationalTuple key = leftElement.restrict(getCompareAttrs()
						.getAllSources());
				ArrayList<RelationalTuple> elements = buildInput.get(key);
				// Gibt es diesen Schlüssel schon?
				if (elements != null) {
					elements.add(leftElement);
				} else {
					elements = new ArrayList<RelationalTuple>();
					elements.add(leftElement);
					buildInput.put(key, elements);
				}
			}
			firstRun = false;
		}

		if (tmpResults.size() == 0) {
			// Jedes Element des rechten Stroms mit Elementen der HashMap Testen
			// solange ein Ergebnis gefunden wird, Verknüpfen und
			// Herausschreiben
			ArrayList<RelationalTuple> elements = null;
			while (elements == null) { // Es muss solange gesucht werden, bis
									   // ein
				// Treffer gefunden wird oder der andere Strom auch zu Ende ist
				if ((rightElement = (RelationalTuple) this.getRightNext(this, -1)) != null) {
					RelationalTuple key = rightElement.restrict(getCompareAttrs()
							.getAllDestinations());
					elements = buildInput.get(key);
					// in tmpResults alle gefundenen Treffer ablegen
					if (elements != null) {
						for (int i = 0; i < elements.size(); i++) {
							System.out.print("Verknüpfe "
									+ (RelationalTuple) elements.get(i) + " mit "
									+ rightElement);
							RelationalTuple res = rightElement.mergeRight(
									elements.get(i),
									this.getCompareAttrs());
							System.out.println("-->" + res);
							tmpResults.add(res);
						}
					}
				} else {
					break;
				}
			}
		}
		if (tmpResults.size() > 0) {
			newAttrList = (RelationalTuple) tmpResults.remove(0);
		}
		return newAttrList;
	}

	protected boolean process_close() throws POException {
		return true;
	}

	public String getInternalPOName() {
		return "HashJoin";
	}

	protected void initInternalBaseValues(NodeList childNodes) {
		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
	}

	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer xmlRetValue) {
		// TODO: Implement this
		// mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator abstract method
	}

	public HashMap<RelationalTuple, ArrayList<RelationalTuple>> getHashtable(int input) {
		return buildInput;
	}

	public boolean setHashtable(int input, HashMap<RelationalTuple, ArrayList<RelationalTuple>> newTable) {
		this.buildInput = newTable;
		return true;
	}

	public SupportsCloneMe cloneMe(){
		return new HashJoinPO(this);
	}

}