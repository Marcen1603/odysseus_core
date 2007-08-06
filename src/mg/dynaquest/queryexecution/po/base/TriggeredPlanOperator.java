package mg.dynaquest.queryexecution.po.base;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/22 12:22:03 $ 
 Version: $Revision: 1.28 $
  */

/** Diese Klasse erweitert die Planoperatoren um die Fähigkeit anderen
 Klassen (Observern) Nachrichten zu senden bzw. den Eintritt eines
 bestimmten Ereignisses mitzuteilen. Zu diesem Zweck können sich 
 Observer bei dieser Klasse (bzw. einer konkreten Implementierung
 in einer Unterklasse) fuer ein bestimmtes Ereignis registrieren
 Klasse kann nicht direkt verwendet werden, Abteilung von NArayPlanOperator
 notwendig!
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.*;
import mg.dynaquest.queryexecution.event.OutOfMemoryExceptionEvent;
import mg.dynaquest.queryexecution.object.POElementBuffer;
import mg.dynaquest.queryexecution.object.POElementBufferEventListener;
import mg.dynaquest.support.RandomGUID;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

/**
 * @author   Marco Grawunder
 */
public abstract class TriggeredPlanOperator implements PlanOperator, Runnable {

	/**
	 * PO Debuger
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private Logger logger = Logger.getLogger(this.getClass().getName()); 
	
	/**
	 * @uml.property  name="pO_ID"
	 */
	public final String PO_ID = (new RandomGUID(true)).toString();
	
	/* globale debug-Flag für TriggeredPOs */
	//public boolean debug = false;

//	public void setDebug(boolean debug) {
//		this.debug = debug;
//	}

	/* Hilfsmethode für Debug-Ausgaben */
//	public void debug(String s) {
//		//if (debug)
//		//	System.out.println(s);
//        logger.debug(s);
//	}
	
	

	// Die Menge der Event-Objekte, dann muss es nur einmal
	// angelegt werden und nicht bei jedem Aufruf eines
	// Events (geht, da die Werte in dem Objekt statisch sind)
	// Sind nicht static, da sie this übergeben bekommen müssen
	/**
	 * @uml.property  name="openInitEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final protected OpenInitEvent openInitEvent = new OpenInitEvent(this);

	/**
	 * @uml.property  name="openDoneEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final protected OpenDoneEvent openDoneEvent = new OpenDoneEvent(this);

	/**
	 * @uml.property  name="nextInitEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final protected NextInitEvent nextInitEvent = new NextInitEvent(this);

	/**
	 * @uml.property  name="nextDoneEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final protected NextDoneEvent nextDoneEvent = new NextDoneEvent(this);

	/**
	 * @uml.property  name="closeInitEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final protected CloseInitEvent closeInitEvent = new CloseInitEvent(this);

	/**
	 * @uml.property  name="closeDoneEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final protected CloseDoneEvent closeDoneEvent = new CloseDoneEvent(this);

	/**
	 * @uml.property  name="writeInitEvent"
	 * @uml.associationEnd  qualifier="po:mg.dynaquest.queryexecution.po.base.SimplePlanOperator mg.dynaquest.queryexecution.event.WriteInitEvent"
	 */
	final HashMap<SimplePlanOperator,WriteInitEvent>  writeInitEvent = new HashMap<SimplePlanOperator,WriteInitEvent>();

	/**
	 * @uml.property  name="writeDoneEvent"
	 * @uml.associationEnd  qualifier="po:mg.dynaquest.queryexecution.po.base.SimplePlanOperator mg.dynaquest.queryexecution.event.WriteDoneEvent"
	 */
	final HashMap<SimplePlanOperator,WriteDoneEvent> writeDoneEvent = new HashMap<SimplePlanOperator,WriteDoneEvent>();

	/**
	 * @uml.property  name="processingFinishedEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final ProcessingFinishedEvent processingFinishedEvent = new ProcessingFinishedEvent(
			this);

	// Die Registrierungliste fuer die Events.
	// Da sowohl die Anzahl der möglichen Events als auch die Liste
	// Registrierten unbekannt ist, eine Struktur der Form
	// "Event1" --> Liste der Registrierten
	// "Event2" --> Liste der Registrierten
	// etc.
	/**
	 * @uml.property  name="eventRegistrationList"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.util.ArrayList<POEventListener>"
	 */
	private Map<Integer,ArrayList<POEventListener>> eventRegistrationList = new HashMap<Integer,ArrayList<POEventListener>>();;

	private ArrayList<POEventListener> registeredForAllEvents = new ArrayList<POEventListener>();

	//private POElementBuffer elementBuffer = new POElementBuffer(100);

	/**
	 * @uml.property  name="callerElementBuffer"
	 * @uml.associationEnd  qualifier="key:java.lang.Object mg.dynaquest.queryexecution.object.POElementBuffer"
	 */
	private Map<SimplePlanOperator,POElementBuffer> callerElementBuffer = new HashMap<SimplePlanOperator,POElementBuffer>();

	/**
	 * @uml.property  name="maxElementBufferSize"
	 */
	private int maxElementBufferSize = 100;

	/**
	 * @uml.property  name="useHDMode"
	 */
	private boolean useHDMode = false;

	//private boolean opened = false;
	//private boolean closed = false;

	/**
	 * @uml.property  name="poName"
	 */
	private String poName;

	/**
	 * Die Anzahl der POs, die Daten von diesem PO abrufen. Der default ist 1
	 * @uml.property  name="numberOfConsumerPOs"
	 */
	private int numberOfConsumerPOs = 1;

	// aktueller Thread
	/**
	 * @uml.property  name="t"
	 */
	private Thread t;

	// Speicherverwaltung
	/**
	 * @uml.property  name="maxMem"
	 */
	private int maxMem = 0;

	/**
	 * @uml.property  name="currMem"
	 */
	private int currMem = 0;

	/**
	 * @uml.property  name="outOfMemoryEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	final OutOfMemoryExceptionEvent outOfMemoryEvent = new OutOfMemoryExceptionEvent(
			this);

	private boolean pause = false;
	private boolean stopAfterNextTupel = false;;
	 public static Logger log;

	// ------------------------------------------------------------------------
	// Konstruktor
	// ------------------------------------------------------------------------

	public TriggeredPlanOperator() {
	    super();	 
	}

	/**
     * @param operator
     */
    public TriggeredPlanOperator(TriggeredPlanOperator operator) {
        //this.debug = operator.debug;
        this.eventRegistrationList = operator.eventRegistrationList;
        this.registeredForAllEvents = operator.registeredForAllEvents;
        this.callerElementBuffer = operator.callerElementBuffer;
        this.maxElementBufferSize = operator.maxElementBufferSize;
        this.useHDMode = operator.useHDMode;
        this.poName = operator.poName;
        this.numberOfConsumerPOs = operator.numberOfConsumerPOs;
        this.maxMem = operator.maxMem;
    }
	
	// ------------------------------------------------------------------------
	// Eigenschaftszugriffmethoden
	// ------------------------------------------------------------------------


    final protected synchronized void addConsumer(SimplePlanOperator consumer) {
		notifyAll();
		this.callerElementBuffer.put(consumer, new POElementBuffer(
				this.maxElementBufferSize));
		this.writeInitEvent.put(consumer, new WriteInitEvent(this,consumer));
		this.writeDoneEvent.put(consumer, new WriteDoneEvent(this,consumer));
	}

	public final void setMaxElementsBufferSize(int maxElementBufferSize) {
		// für alle Ausgabepuffer die Buffersize anpassen
		ArrayList<POElementBuffer> list = new ArrayList<POElementBuffer>(this.callerElementBuffer.values());
		this.maxElementBufferSize = maxElementBufferSize;
		for (int i = 0; i < list.size(); i++) {
			 list.get(i).setNewMaxMemBufferSize(maxElementBufferSize);
		}
		this.maxElementBufferSize = maxElementBufferSize;
	}

	public final int getMaxElementsBufferSize() {
		return maxElementBufferSize;
	}

	public void switchToDHMemoryMode() {
		// alle Puffer auf den HD-Modus umschalten
		ArrayList<POElementBuffer> list = new ArrayList<POElementBuffer>(this.callerElementBuffer.values());
		//this.maxElementBufferSize = maxElementBufferSize;
		for (int i = 0; i < list.size(); i++) {
			 list.get(i).switchToDHMemoryMode();
		}
		useHDMode = true;
	}

	public final int getMaxFilledBufferElements() {
		int maxElem = 0;
		ArrayList<POElementBuffer> list = new ArrayList<POElementBuffer>(this.callerElementBuffer.values());
		// sind alle gleich:
		if (list.size() > 0) {
			POElementBuffer buffer = list.get(0);
			maxElem = buffer.getMaxSizeReached();
		}
		return maxElem;
	}

	public final void clearOutputBuffer() {
		// für alle Ausgabepuffer die Buffersize anpassen
		ArrayList<POElementBuffer> list = new ArrayList<POElementBuffer>(this.callerElementBuffer.values());
		//this.maxElementBufferSize = maxElementBufferSize;
		for (int i = 0; i < list.size(); i++) {
		    list.get(i).clearBuffer();
		}
	}

	final protected synchronized void removeConsumer(SimplePlanOperator consumer) {
		logger.debug("removeConsumer (" + consumer + ") --> "
				+ this.callerElementBuffer.remove(consumer));
		notifyAll();
	}

	// Offen, wenn ich alle Konsumenten angemeldet haben
	final protected boolean isOpened() {
		return (this.numberOfConsumerPOs == this.callerElementBuffer.size());
	}

	// Geschlossen, wenn sich keiner angemeldet hat
	final protected boolean isClosed() {
		return (this.callerElementBuffer.size() == 0);
	}

	final public String getPOName() {
		if (this.poName != null) {
			return poName;
		}else{
			return getClass().getName();
		}
	}

	final public void setPOName(String poName) {
		this.poName = poName;
	}

	public void setNOOfConsumerPOs(int count) {
		numberOfConsumerPOs = count;
	}

	// Zum Austauschen von Ops müssen auch die Konsumenten bekannt sein
	final public synchronized int getNoOfRegisteredConsumers() {
		return this.callerElementBuffer.size();
	}

	final public synchronized List<SimplePlanOperator> getRegisteredConsumers() {
		return new ArrayList<SimplePlanOperator>(this.callerElementBuffer.keySet());
	}

	// ------------------------------------------------------------------------
	// Event-Verarbeitung
	// ------------------------------------------------------------------------

	/**
	 * Diese Methode erlaubt es einer Klasse vom Typ POEventListener sich zu
	 * registrieren, um die Ereignisse einer bestimmten Klasse zu bekommen.
	 * 
	 * @param who
	 *            das Objekt (Observer) der über das Ereignis informiert weden
	 *            möchte
	 * @param eventNo
	 *            die Nummer des Ereignisses, ueber welches der Observer
	 *            informiert werden möchte
	 */
	final public synchronized void addPOEventListener(POEventListener who,
			Integer eventNo) {
		//System.out.println("addPOEventListener -->"+who);
		ArrayList<POEventListener> receiver = null;
		// Entweder ist das Event schon vorhanden
		if (this.eventRegistrationList.containsKey(eventNo)) {
			// dann die Menge der Empfänger auslesen und who anhaengen
			// Aus Effizienzgründen ist hier eine ArrayList, um
			// Doppeleintragungen
			// zu vermeiden könnte man hier (und dann natürlich auch unten
			// weiter)
			// auch ein Set verwenden
			receiver = this.eventRegistrationList.get(eventNo);
		} else {
			// oder neues Event eintragen und neue Emfpängerliste erzeugen
			receiver = new ArrayList<POEventListener>();
			this.eventRegistrationList.put(eventNo, receiver);
		}
		receiver.add(who);
	}

	/**
	 * Registrieren für alle Events, die von diesem Objekt versandt werden
	 * 
	 * @param who
	 *            ist das Objekt, dass über alle Ereignisse informiert werden
	 *            möchte
	 */
	final public synchronized void addPOEventListener(POEventListener who) {
		this.registeredForAllEvents.add(who);
	}

	public synchronized void dumpPOEventListenerList() {
		Iterator iter1 = this.eventRegistrationList.keySet().iterator();
		Iterator iter2 = this.eventRegistrationList.values().iterator();

		while (iter1.hasNext()) {
			Object val = iter1.next();
			logger.debug("Schluessel " + val + " " + val.getClass().getName());

		}

		while (iter2.hasNext()) {
			Object val = iter2.next();
			logger.debug("Wert " + val + " " + val.getClass().getName());
		}

		logger.debug(this.registeredForAllEvents.toString());
	}

	/**
	 * Wenn ein Observer keine Benachrichtigungen über ein bestimmtes Ereignis
	 * haben möchte, dann kann er sich hiermit aus der Liste austragen, wenn es
	 * das Ereignis nicht gibt, wird kein(!) Fehler gemeldet
	 * 
	 * @param who
	 *            der Observer
	 * @param eventName
	 *            der Name des Ereignisses
	 * @returns true, wenn das Element entfernt werden konnte, false sonst
	 */
	public synchronized void removePOEventListener(POEventListener who,
			Integer eventNo) {
		ArrayList receiver = (ArrayList) this.eventRegistrationList
				.get(eventNo);
		if (receiver != null) {
			receiver.remove(who);
		}
	}

	// Auslösen eines Ereignisses bzw. Mitteilung über das Auslösen
	// Temporäre Umstellung auf synchronized, nicht schön aber funktioniert
	final public void notifyPOEvent(POEvent poEvent) {
		ArrayList<POEventListener> allReceivers = registeredForAllEvents;
		//    synchronized(this){
		ArrayList<POEventListener> allReceiversHelp = this.eventRegistrationList.get(poEvent.getPOEventType());
		if (allReceiversHelp != null) {
			if (allReceivers != null) {
				allReceivers.addAll(allReceiversHelp);
			} else {
				allReceivers = allReceiversHelp;
			}
		}//else{
		//  System.out.println("Keine Listener fuer das Event "+poEvent+"
		// ("+poEvent.getID()+") registriert");
		// }
		//    }
		// System.out.println("notifyPOEvent(2) "+poEvent);
		if (allReceivers != null) {
			Iterator<POEventListener> receiver = allReceivers.iterator();
			while (receiver.hasNext()) {
			    POEventListener receiver_l = receiver.next();
				//System.out.println("Receiver "+receiver_l);
				receiver_l.poEventOccured(poEvent);
			}
		}
		//System.out.println("notifyPOEvent "+poEvent+" FERTIG");
	}

	// ------------------------------------------------------------------------
	// Thread-Methoden
	// ------------------------------------------------------------------------

	//  public void reInit(){
	//      
	//      elementBuffer.reInit();
	//      System.out.println(this.getPOName()+"Verarbeitung neu Initialisert");
	//  }

	public void start() {
		if (t == null) {
			t = new Thread(this, this.getPOName());
			t.start();
		}
	}

	public void stop() {
		t = null;
		logger.debug(this.getPOName() + "...stoppe die Verarbeitung");
//		try{
//		    waitForClose();
//		}catch(InterruptedException e){
//		    e.printStackTrace();
//		}
	}

	final protected synchronized void waitForOpen() throws InterruptedException {
	    logger.debug(this.getPOName()+"waitForOpen");
	    while (!this.isOpened() && t != null)
			wait(1000);
		logger.debug(this.getPOName()+"waitForOpen durch");
		//setClosed(false);
	}

	final protected synchronized void waitForClose()
			throws InterruptedException {
	    logger.debug(this.getPOName()+" waitForClose");
		while (!this.isClosed() && t != null)
			wait(1000);
		logger.debug(this.getPOName()+" waitForClose durch");
		//setOpened(false);
	}

	final protected synchronized void waitForResume() throws InterruptedException{
		logger.debug(this.getPOName()+" waitForResume");
		while (pause && t!=null) wait(1000);
		logger.debug(this.getPOName()+" waitForResume durch");
	}
	
	final public void pause(){
		pause = true;
	}
	
	final synchronized public void resume(){
		pause = false;
		notifyAll();
	}
	
	final synchronized public void resumeOneTupel(){
		stopAfterNextTupel = true;
		resume();
	}
	
	public void run() {
		logger.debug(this.getPOName() + " gestartet");
		pause = false;
		stopAfterNextTupel = false;
		// Das hier muss ich erweitern auf eine Thread-Liste
		//Thread myThread = Thread.currentThread();
		while (t != null) {
			// Auf das Open warten, vorher geht gar nichts
			try {
				logger.debug(this.getPOName()
						+ " warte auf die Initialisierung (Open)");
				waitForOpen();
				// Falls waitForOpen unterbrochen wurde (durch Stop) 
				// darf die Schleife nicht mehr durchlaufen werden.
				if (t==null) break;
			} catch (InterruptedException intEx) {
				intEx.printStackTrace();
			}
			logger.debug(this.getPOName() + " geoeffnet");

			// Erst jetzt können die Buffer in den HDModus geschaltet werden
			if (this.useHDMode)
				switchToDHMemoryMode();

			// Process_Next muss so lange rennen, wie noch Daten vom
			// Nachfolger kommen, die ungleich "null" sind ...

			try {
				Object prdObj = "";
				while (prdObj != null && t != null) {
					synchronized (this) {
							// Falls der Operator angehalten worden ist
							// noch vor dem ersten Tupel warten
							waitForResume();
							this.notifyPOEvent(this.nextInitEvent);
							prdObj = process_next();
							//debug(this.getPOName() + " Objekt geschrieben "
							//		+ prdObj);
							this.putToBuffer(prdObj);
							this.notifyPOEvent(this.nextDoneEvent);
							if (stopAfterNextTupel){
								stopAfterNextTupel = false;
								pause();
							}
					}
				}
			} catch (Exception poExep) {

				this.notifyPOEvent(new ExceptionEvent(this, poExep.toString()));
				this.stop();
				poExep.printStackTrace();
			}

			synchronized (this) {
				this.notifyPOEvent(this.processingFinishedEvent);
				logger.debug(this.getPOName() + " Alle Elemente verarbeitet");
			}

			// Solange ideln bis close aufgerufen wird
			// aber nur wenn der Thread noch aktiv ist.
			// Hier ist ein Problem! Wenn stop() aufgerufen wird
			// bevor waitForClose() durch ist, gibt es einen Fehler
			// auf der anderen Seite will man u.U. den Prozess auch
			// abschießen --> andere Methode dafür definieren?
			try {
				if (t != null) {
					waitForClose();
				}
			} catch (InterruptedException intEx) {
				intEx.printStackTrace();
			}
			//this.setOpened(false);

			logger.debug(this.getPOName() + " geschlossen");
		}

		logger.debug(this.getPOName() + " Terminiere.");
	}

	// ------------------------------------------------------------------------
	// Verarbeitung des Austauschsbuffers (pro Konsument)
	// ------------------------------------------------------------------------

	protected boolean elementsInBuffer() {
		Collection elems = callerElementBuffer.values();
		Iterator iter = elems.iterator();
		while (iter.hasNext()) {
			POElementBuffer elementBuffer = (POElementBuffer) iter.next();
			if (!elementBuffer.isEmpty()) {
				return true;
			}
		}
		return false;
	}

	// Ist notwendig bei Operatoren, in in einem Next-Durchlauf mehr
    // als ein Element schreiben müssen. In erster Linie für den
    // Collector gedacht.
	final protected void putToBufferInternal(Object element) {
        logger.debug("putToBufferInternal "+element);
		this.putToBuffer(element);
	}

	private final void putToBuffer(Object element) {
		// auf Keinen Fall null "nicht" eintragen, sonst weiss man nicht,
		// dass die Verarbeitung beendet ist!!
		// d.h. man kann nicht unterscheiden, ob das letzte Element
		// gelesen worden ist, oder ob nichts mehr zu lesen ist
		//    if (element != null){
		

		Collection<SimplePlanOperator> pOs = callerElementBuffer.keySet();
		Iterator<SimplePlanOperator> iter = pOs.iterator();
		while (iter.hasNext()) {
		    SimplePlanOperator po = iter.next();
		    this.notifyPOEvent(writeInitEvent.get(po));
			POElementBuffer elementBuffer = callerElementBuffer.get(po);
			elementBuffer.put(element);
			this.notifyPOEvent(writeDoneEvent.get(po));			
		}
		
		//    }
	}

	final protected Object getFromBuffer(SimplePlanOperator caller, long timeout) throws TimeoutException {
        //debug("final protected Object getFromBuffer(PlanOperator caller) "+caller);
		POElementBuffer elementBuffer = (POElementBuffer) callerElementBuffer
				.get(caller);
		Object elem = null;
		if (elementBuffer != null) {
			elem = elementBuffer.get(timeout);
		}else{
		    logger.error(this.getPOName()+" final protected Object getFromBuffer(PlanOperator caller) { Caller nicht registriert !!!"+caller.getPOName());
        }
		return elem;
	}

	public void addPOElementBufferEventListener(
			POElementBufferEventListener listener) {
		Iterator iter = callerElementBuffer.values().iterator();
		while (iter.hasNext()) {
			((POElementBuffer) iter.next())
					.addPOElementBufferEventListener(listener);
		}
	}

	public void removePOElementBufferEventListener(
			POElementBufferEventListener listener) {
		Iterator iter = callerElementBuffer.values().iterator();
		while (iter.hasNext()) {
			((POElementBuffer) iter.next())
					.removePOElementBufferEventListener(listener);
		}
	}

	// ------------------------------------------------------------------------
	// Operator-Verarbeitung
	// ------------------------------------------------------------------------

	@SuppressWarnings("unchecked")
	public final <T> T next(SimplePlanOperator caller, long timeout) throws POException, TimeoutException {
		logger.debug("### "+this.getPOName()+" next aufgerufen von "+caller);
		Object obj = getFromBuffer(caller, timeout);
        logger.debug("### "+this.getPOName()+" liefere object "+obj);
		return (T) obj;
	}

	// -------------------------------------------------------------------------
	// Abstrakte Methoden, die in den Unterklassen ueberschrieben werden muessen
	// -------------------------------------------------------------------------

	// muss zwar nicht, ist aber für IDEs einfacher, dann muss man nur
	// Methoden aus dieser Klasse ueberschreiben
	/**
	 * @uml.property  name="internalPOName"
	 */
	public String getInternalPOName(){
		return this.getClass().toString();
	}

	// --------------------------------------
	// Verwaltung der Input-Knoten
	// --------------------------------------
	// public abstract int getNumberOfInputs();
	//  public abstract void setInputPO(int pos, TriggeredPlanOperator inputPO);
	//  public abstract TriggeredPlanOperator getInputPO(int pos);
	// Nach NAryPlanOperator verlegt

	// --------------------------------------
	// Verarbeitung
	// --------------------------------------
	// Eigentliche Verarbeitungsroutine, d.h. hier findet die Arbeit der
	// Planoperatoren statt (also z.B. der Nexted-Loop-Join)
	protected abstract boolean process_open() throws POException;

	protected abstract Object process_next() throws POException, TimeoutException;

	// Auslesen des nächsten Elements aus dem Strom mit der Nummer pos
	protected abstract <T> T getInputNext(int pos, SimplePlanOperator caller, long timeout)
			throws POException, TimeoutException;

	protected abstract boolean process_close() throws POException;

	// --------------------------------------
	// Initialisierungsmethoden
	// --------------------------------------

	/**
	 * Liefert die ID der Input-Operatoren, die in der initBaseValues
	 * Methode gesetzt worden sind
	 */
	protected abstract String getInputID(int pos);

	protected abstract void setInputID(int pos, String id);

	/**
	 *  Serialisieren und initialisieren aus einem XML-File bzw. aus einem
	 *  DOM-Knoten
	 */
	protected abstract void initInternalBaseValues(NodeList childNodes);

	/**
	 * Damit nicht jeder OP auch das Schreiben des kompletten Header
	 * implementieren
	 *  muss, gibt es eine Spezialmethode, die nur den aktuellen Zustand
	 * (ohne Name und id) herausschreibt
	 */
	protected abstract void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer xmlRetValue);

	// ------------------------------------------------------------------------
	// Methoden für die Speicherverwaltung
	// ------------------------------------------------------------------------
	/**
	 * @param maxMem  the maxMem to set
	 * @uml.property  name="maxMem"
	 */
	public void setMaxMem(int maxMem) {
		this.maxMem = maxMem;
	}

	/**
	 * @return  the maxMem
	 * @uml.property  name="maxMem"
	 */
	public int getMaxMem() {
		return this.maxMem;
	}

	public int getUsedMem() {
		return this.currMem;
	}

	public void requestMemory(int size) {
		if (maxMem == 0)
			return;
		if (this.currMem < this.maxMem + size) {
			this.currMem = this.currMem + size;
		} else {
			this.notifyPOEvent(this.outOfMemoryEvent);
			try {
				while (this.currMem + size > this.maxMem)
					this.wait(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void releaseMemory(int size) {
		this.currMem = this.currMem - size;
		if (this.currMem < 0)
			this.currMem = 0;
		this.notifyAll();
	}

}