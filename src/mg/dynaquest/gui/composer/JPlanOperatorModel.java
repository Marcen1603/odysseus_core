package mg.dynaquest.gui.composer;

/**
 * Author: $Author: grawund $ Date: $Date: 2004/09/16 08:57:12 $ Version:
 * $Revision: 1.13 $ Log: $Log: JPlanOperatorModel.java,v $
 * $Revision: 1.13 $ Log: Revision 1.13  2004/09/16 08:57:12  grawund
 * $Revision: 1.13 $ Log: Quellcode durch Eclipse formatiert
 * $Revision: 1.13 $ Log: Log: Revision 1.12
 * 2004/09/16 08:53:52 grawund Log: *** empty log message *** Log: Log: Revision
 * 1.11 2004/08/25 11:26:39 grawund Log: no message Log: Log: Revision 1.10
 * 2004/08/25 09:23:32 grawund Log: Anpassungen da POStatePDA jetzt einen
 * Konstruktor hat Log: Log: Revision 1.9 2004/07/01 08:43:20 grawund Log: no
 * message Log: Log: Revision 1.8 2004/06/22 14:07:33 grawund Log: no message
 * Log: Log: Revision 1.7 2004/06/18 13:43:17 grawund Log: no message Log: Log:
 * Revision 1.6 2002/02/20 15:51:47 grawund Log: Fall 2 beim IMDB-Zugriff
 * umgesetzt Log: Log: Revision 1.5 2002/02/18 13:45:09 grawund Log: Version in
 * der der Collector funktioniert und keine illegalen Zustaende mehr liefert
 * Log: Log: Revision 1.4 2002/02/07 13:33:52 grawund Log: Umstellung der
 * Planoperatoren auf mehrere Ausgabeströme Log: Log: Revision 1.3 2002/02/06
 * 14:02:17 grawund Log: Einbindung beliebiger Araneus-Konverter moeglich Log:
 * Log: Revision 1.2 2002/01/31 16:14:49 grawund Log:
 * Versionsinformationskopfzeilen eingefuegt Log:
 */

import java.util.concurrent.TimeoutException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.EventListenerList;
import mg.dynaquest.monitor.IllegalStateTransition;
import mg.dynaquest.monitor.POStatePDA;
import mg.dynaquest.queryexecution.event.POEvent;
import mg.dynaquest.queryexecution.event.POEventListener;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.base.SimplePlanOperator;
import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * @author  Marco Grawunder
 */
public class JPlanOperatorModel implements JPlanOperatorModelInterface,
		POEventListener, SimplePlanOperator{

	/**
	 * @uml.property  name="changeEvent"
	 * @uml.associationEnd  
	 */
	protected transient ChangeEvent changeEvent = null;

	/**
	 * @uml.property  name="listenerList"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected EventListenerList listenerList = new EventListenerList();

	// Diese vier sollten ab sofort ueberfluessig sein
	//  private int state = JPlanOperatorModelInterface.INACTIVE;
	//  private int blockedInput = -1;
	//  private int lastState = JPlanOperatorModelInterface.INACTIVE;
	//  private long lastStateChangeTime = System.currentTimeMillis();
	/**
	 * @uml.property  name="poStatePDA"
	 * @uml.associationEnd  
	 */
	private POStatePDA poStatePDA = null;

	/**
	 * @uml.property  name="realPO"
	 * @uml.associationEnd  
	 */
	private TriggeredPlanOperator realPO;


	/**
	 * @param realPO  the realPO to set
	 * @uml.property  name="realPO"
	 */
	public void setRealPO(TriggeredPlanOperator realPO) {
		this.realPO = realPO;
		this.poStatePDA = new POStatePDA(realPO.getNumberOfInputs());
		realPO.addPOEventListener(this);
		//realPO.addPOEventListener(this,OpenInitEvent.ID);
		//realPO.addPOEventListener(this,OpenDoneEvent.ID);
		//realPO.addPOEventListener(this,NextInitEvent.ID);
		//realPO.addPOEventListener(this,NextDoneEvent.ID);
		//realPO.addPOEventListener(this,CloseInitEvent.ID);
		//realPO.addPOEventListener(this,CloseDoneEvent.ID);
		//realPO.addPOEventListener(this,ReadInitEvent.ID);
		//realPO.addPOEventListener(this,ReadDoneEvent.ID);
		//realPO.addPOEventListener(this,WriteInitEvent.ID);
		//realPO.addPOEventListener(this,WriteDoneEvent.ID);

//		if (realPO.getNumberOfThreads() != 1) {
//			this.poStatePDA.setIgnoreBlockingModes(true);
//		}

		//realPO.dumpPOEventListenerList();

		fireChange();
	}

	/**
	 * @return  the realPO
	 * @uml.property  name="realPO"
	 */
	public TriggeredPlanOperator getRealPO() {
		return realPO;
	}

	// NOCH ERSETZEN DURCH DELEGATION
	public POStatePDA getStatePDA() {
		return poStatePDA;
	}

	public void poEventOccured(POEvent event) {

		try {
			poStatePDA.processEvent(event);
			fireChange();
		} catch (IllegalStateTransition e) {
			System.out.println("ACHTUNG FEHLER BEI DEN ZUSTANDSÜBERGÄNGEN!");
			System.out.println(e.getMessage());
			System.out.println("Letzter gültiger Zustand " + e.getLastState());
			e.printStackTrace();
		}
	}

	public String getPOName() {
		return realPO.getPOName();
	}

	public void setPOName(String name) {
		realPO.setPOName(name);
		fireChange();
	}

	public int getNumberOfInputs() {
		if (realPO != null) {
			return realPO.getNumberOfInputs();
		} else {
			return -1;
		}
	}

	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}

	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	protected void fireChange() {
		Object[] listeners = listenerList.getListenerList();
		if (changeEvent == null) {
			changeEvent = new ChangeEvent(this);
		}
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ChangeListener.class) {
				((ChangeListener) listeners[i + 1]).stateChanged(changeEvent);
			}
		}
	}

	// Aktionen an den Planoperator weiterleiten
	public void open(SimplePlanOperator po) throws POException {
		this.realPO.open(po);
	}

	public void open() throws POException {
		open(this);
	}

	public boolean close(SimplePlanOperator po) throws POException {
		return this.realPO.close(po);
	}

	public boolean close() throws POException {
		return close(this);
	}

	public Object next(SimplePlanOperator po, long timeout) throws POException, TimeoutException {
		return this.realPO.next(po, -1);
	}

	public Object next() throws POException, TimeoutException {
		return next(this, -1);
	}

	public void execute(boolean withClose) throws POException, TimeoutException {
		@SuppressWarnings("unused")
		Object obj = null;
		//System.out.println("Starte mit dem obersten Knoten
		// "+realPO.getPOName());
		realPO.open(this);
		//System.out.println(" Init fertig (open)");
		while ((obj = realPO.next(this, -1)) != null) {
			//System.out.println("JPlanOperatorModel Object angekommen
			// -->"+obj);
		}
		;
		if (withClose) {
			//System.out.println(realPO.getPOName()+" versuche die Verarbeitung
			// zu beenden");
			// Hier noch etwas auf die Events warten
			for (int i = 0; i < 10000; i++) {
			}
			;
			realPO.close(this);
			//System.out.println(realPO.getPOName()+" Query fertig (close)");
		}
	}


}