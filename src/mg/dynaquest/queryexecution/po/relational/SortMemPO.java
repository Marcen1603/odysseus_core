package mg.dynaquest.queryexecution.po.relational;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/22 12:22:03 $ 
 Version: $Revision: 1.4 $
 Log: $Log: SortMemPO.java,v $
 Log: Revision 1.4  2004/09/22 12:22:03  grawund
 Log: Fehler in TPO korrigiert
 Log:
 Log: Revision 1.3  2004/09/16 08:57:12  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.2  2004/08/03 13:20:02  grawund
 Log: Neue Schnittstellen zum Auslesen der gelesen Daten bzw. der Hashtabellen
 Log:
 Log: Revision 1.1  2004/06/23 14:30:00  grawund
 Log: no message
 Log:
 Log: Revision 1.4  2004/06/18 13:43:17  grawund
 Log: no message
 Log:
 Log: Revision 1.3  2004/05/14 09:41:26  grawund
 Log: Umstellung: process_next liefert nun das Objekt, putToBuffer ist private
 Log:
 Log: Revision 1.2  2002/01/31 16:14:30  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.SortPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.UnaryPlanOperator;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTuple;
import mg.dynaquest.queryexecution.po.relational.object.RelationalTupleComparator;
import org.w3c.dom.NodeList;

/**
 * Planoperator, der die Daten im Hauptspeicher sortiert
 */

public class SortMemPO extends UnaryPlanOperator implements IsSetOperator {

	private SortedSet<RelationalTuple> s = null;

	/**
	 * @uml.property  name="sorted"
	 */
	private boolean sorted = false;

	/**
	 * @uml.property  name="iter"
	 * @uml.associationEnd  multiplicity="(0 -1)" elementType="mg.dynaquest.queryexecution.po.relational.object.RelationalTuple"
	 */
	private Iterator iter = null;

	public SortMemPO() {
	    super();
		s = null;
		sorted = false;
		iter = null;
	}

	public SortMemPO(SortMemPO memPO) {
		super(memPO);
		s = memPO.s;
		sorted = memPO.sorted;
		iter = memPO.iter;
	}
	
	public SortMemPO(SortPO sortPO){
		super(sortPO);
	}

	@SuppressWarnings("unchecked")
    public synchronized boolean process_open() throws POException {
		// TODO: HIER JETZT FESTLEGEN WIE SORTIERT WIRD ...
		Comparator<RelationalTuple> comp = new RelationalTupleComparator();
		s = Collections.synchronizedSortedSet(new TreeSet<RelationalTuple>(comp));
		sorted = false;
		iter = null;
		return true;
	}

	public Object process_next() throws POException, TimeoutException {
		synchronized (this) {
			RelationalTuple retVal = null;
			// Der erste Aufruf von next stoesst das sortieren an
			// und danach werden die Elemente nach und nach wieder
			// herausgegeben
			if (sorted) {
				if (iter.hasNext()) {
					// Diese Nachrichten duerfen hier nicht stehen,
					// da die Daten ja gar nicht von einem nachfolgenden
					// Knoten geholt werden !!!!
					//        this.notifyPOEvent("next_0_init");
					retVal = (RelationalTuple) iter.next();
					//          this.notifyPOEvent("next_0_done");
				}
			} else {
				// erst mal sortieren
				RelationalTuple line = null;
				boolean added = false;
				int addedItems = 0;
				int counter = 0;
				// while true, damit die init_Events rausgeschickt werdeb
				// koennen
				while (true) {
					//        this.notifyPOEvent("next_0_init");
					//        line = (RelationalTuple)getInputPO().next(this);
					//        this.notifyPOEvent("next_0_done");
					line = (RelationalTuple) this.getInputNext(this, -1);
					// ACHTUNG HIER IST DIE ABBRUCHBEDINGUNG!!
					if (line == null)
						break;

					added = s.add(line);
					// Hier jetzt die aktuell verbrauchten Speicher hochzählen
					this.requestMemory(1);
					if (added) {
						addedItems++;
					} else {
						//System.err.println("REJECTED: "+line);
					}
					counter++;
					//   if (counter % 10000 == 0){
					//    System.out.println(counter+" "+addedItems);
					//  }
					//if (addedItems >= 1350000) System.out.println(line);

				} // while
				//System.out.println("");
				// System.out.println("Alle Daten eingelesen:-->"+counter+"
				// Zeilen (Eingetragen "+addedItems+")");
				sorted = true;
				iter = s.iterator();
				// Das erste Element muss jetzt zurückgeliefert werden
				if (counter>0){
				    retVal = (RelationalTuple) iter.next();
				}else{
				    retVal = null;
				}

			} // else-Zweig von if(sorted)
			return retVal;
		}
	}

	public synchronized boolean process_close() throws POException {
		s = null;
		return true;
	}

	public String getInternalPOName() {
		return "SortMemLoader";
	}

	protected void initInternalBaseValues(NodeList children) {
		// Im Moment keine eigenen Parameter, dewegen nichts tun.
	}

	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer retBuffer) {
		// Im Moment keine eigenen Parameter, dewegen nichts tun.
	}

	public Collection<RelationalTuple> getAllReadElements() {
		return new ArrayList<RelationalTuple>(this.s);
	}

	public SupportsCloneMe cloneMe() {		
		return new SortMemPO(this);
	}

}