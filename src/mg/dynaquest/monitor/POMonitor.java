package mg.dynaquest.monitor;

/**
 * POMonitor: Monitoring der POs
 */

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.Timer;
import mg.dynaquest.queryexecution.event.InitTimeoutEvent;
import mg.dynaquest.queryexecution.event.NumberOfObjectsReadEvent;
import mg.dynaquest.queryexecution.event.NumberOfObjectsWrittenEvent;
import mg.dynaquest.queryexecution.event.POEvent;
import mg.dynaquest.queryexecution.event.POEventListener;
import mg.dynaquest.queryexecution.event.POEventType;
import mg.dynaquest.queryexecution.event.ReadInitEvent;
import mg.dynaquest.queryexecution.event.ReadTimeoutEvent;
import mg.dynaquest.queryexecution.event.WriteTimeoutEvent;
import mg.dynaquest.queryexecution.object.POElementBufferEvent;
import mg.dynaquest.queryexecution.object.POElementBufferEventListener;
import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * @author  Marco Grawunder
 */
public class POMonitor implements POEventListener, ActionListener, Runnable,
		POElementBufferEventListener {
	/**
	 * @uml.property  name="t"
	 */
	private Thread t;

	/**
	 * @uml.property  name="name"
	 */
	private String name = "";

	/**
	 * Timeout Zeit für die Initialisierung in milli sekunden, -1 keine Zeit
	 * @uml.property  name="initTimeOutMilliSec"
	 */
	private int initTimeOutMilliSec = -1;

	/**
	 * Timer der jedes mal gestartet wird, wenn initTimeOutMillSec > 0 und OpenInit-Event erfolgt, wird bei OpenDone gelöscht
	 * @uml.property  name="initTimer"
	 * @uml.associationEnd  
	 */
	private Timer initTimer = null;

	/**
	 * Timeout Zeit für das Lesen eines Objektes in milli sekunden, -1 keine Zeit
	 * @uml.property  name="readTimeOutMilliSec"
	 */
	private int readTimeOutMilliSec = -1;

	/**
	 * Timer der jedes mal gestartet wird, wenn readTimeOutMillSec > 0 und ReadInit-Event erfolgt, wird bei ReadDone gelöscht
	 * @uml.property  name="readTimer"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private Timer[] readTimer = null;

	/**
	 * Timeout Zeit für das Schreiben eines Objektes in milli sekunden, -1 keine Zeit
	 * @uml.property  name="writeTimeOutMilliSec"
	 */
	private int writeTimeOutMilliSec = -1;

	/**
	 * Timer der jedes mal gestartet wird, wenn writeTimeOutMillSec > 0 und WriteInit-Event erfolgt, wird bei WriteDone gelöscht
	 * @uml.property  name="writeTimer"
	 * @uml.associationEnd  
	 */
	private Timer writeTimer = null;

//	/** Event generieren wenn noOfObjectsRead Objekte gelesen wurden */
//	private int noOfObjectsReadEventCount = 0;
//
//	/** Event generieren wenn noOfObjectsWritten Objekte geschrieben wurden */
//	private int noOfObjectsWrittenEventCount = 0;
//
//	/** Event alle everyNObjectsRead gelesenen Objekte werfen */
//	private int everyNObjectsReadEventCount = 0;
//
//	/** Event alle everyNObjectsRead geschriebener Objekte werfen */
//	private int everyNObjectsWrittenEventCount = 0;

	/** Datenlieferung */
	/**
	 * Datenfenster für die Anzahl der Zeitpunkte die betrachtet werden soll Bei 0 werden keine Datenlieferungsaspekte betrachtet.
	 * @uml.property  name="timeWindow"
	 */
	private int timeWindow = 0;

	/**
	 * Ein Ring-Puffer mit der Kapazität timeWindow, Falls das Array voll ist, werden die Elemente wieder von vorne beschrieben
	 * @uml.property  name="readEventsOccured"
	 * @uml.associationEnd  
	 */
	private TimeRingBuffer readEventsOccured = null;

	/** Bursty etc. */
//	private BurstyDeliveryEvent burstyDeliveryEvent = null;
//
//	private SlowDeliveryEvent slowDeliveryEvent = null;
//
//	private DeferredDeliveryEvent deferredDeliveryEvent = null;

	/**
	 * Timer der das Testen anstösst
	 * @uml.property  name="datarateTestTimer"
	 * @uml.associationEnd  
	 */
	private Timer datarateTestTimer = null;

	// Events
	/**
	 * @uml.property  name="noObjectsReadEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	NumberOfObjectsReadEvent noObjectsReadEvent = null;

	/**
	 * @uml.property  name="everyNoObjectsReadEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	NumberOfObjectsReadEvent everyNoObjectsReadEvent = null;

	/**
	 * @uml.property  name="noObjectsWrittenEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	NumberOfObjectsWrittenEvent noObjectsWrittenEvent = null;

	/**
	 * @uml.property  name="everyNoObjectsWrittenEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	NumberOfObjectsWrittenEvent everyNoObjectsWrittenEvent = null;

	/**
	 * @uml.property  name="initTimeoutEvent"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	InitTimeoutEvent initTimeoutEvent = null;

	/**
	 * @uml.property  name="readTimeoutEvent"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	ReadTimeoutEvent[] readTimeoutEvent = null;

	//WriteTimeoutEvent writeTimeoutEvent = null;

	/* PO information */
	/**
	 * @uml.property  name="tpo"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private TriggeredPlanOperator tpo;

	/**
	 * @uml.property  name="pda"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private POStatePDA pda = null;

	/* wir brauchen einen Stack, weil sich die Events nesten können! */
	//private Stack startTimes = new Stack();
	/* eventName -> List */
	/**
	 * @uml.property  name="eventRegistrationList"
	 * @uml.associationEnd  qualifier="key:java.lang.Object java.util.ArrayList<POMonitorEventListener>"
	 */
	private Map<POEventType,ArrayList<POMonitorEventListener>> eventRegistrationList = new HashMap<POEventType,ArrayList<POMonitorEventListener>>();

	private ArrayList<POMonitorEventListener> registeredForAllEvents = new ArrayList<POMonitorEventListener>();

	/**
	 * @param name
	 *            Name für diesen POMonitor
	 * @param po
	 *            der zu überwachende PO
	 */
	public POMonitor(String name, TriggeredPlanOperator po) {
		po.addPOEventListener(this);
		//po.addPOEventListener(this,OpenInitEvent.ID);
		//po.addPOEventListener(this,OpenDoneEvent.ID);
		//po.addPOEventListener(this,NextInitEvent.ID);
		//po.addPOEventListener(this,NextDoneEvent.ID);
		//po.addPOEventListener(this,CloseInitEvent.ID);
		//po.addPOEventListener(this,CloseDoneEvent.ID);
		//po.addPOEventListener(this,ReadInitEvent.ID);
		//po.addPOEventListener(this,ReadDoneEvent.ID);
		//po.addPOEventListener(this,WriteInitEvent.ID);
		//po.addPOEventListener(this,WriteDoneEvent.ID);
		//po.addPOEventListener(this,ExceptionEvent.ID);
		this.name = name;
		this.tpo = po;
		this.pda = new POStatePDA(tpo.getNumberOfInputs());
		noObjectsReadEvent = new NumberOfObjectsReadEvent(tpo,0);
		everyNoObjectsReadEvent = new NumberOfObjectsReadEvent(tpo,0);
		noObjectsWrittenEvent = new NumberOfObjectsWrittenEvent(tpo,0);
		everyNoObjectsWrittenEvent = new NumberOfObjectsWrittenEvent(tpo,0);
		initTimeoutEvent = new InitTimeoutEvent(tpo);
		readTimeoutEvent = new ReadTimeoutEvent[tpo.getNumberOfInputs()];
		for (int i=0;i<tpo.getNumberOfInputs();i++){
		    readTimeoutEvent[i] = new ReadTimeoutEvent(po,i);
		}
		//writeTimeoutEvent = new WriteTimeoutEvent(this);
//		burstyDeliveryEvent = new BurstyDeliveryEvent(this);
//		slowDeliveryEvent = new SlowDeliveryEvent(this);
//		deferredDeliveryEvent = new DeferredDeliveryEvent(this);

	}

	/**
	 * @returns  den Namen des POMonitors
	 * @uml.property  name="name"
	 */
	public String getName() {
		return name;
	}

	/* Event Schnittstelle */

	/**
	 * Hier kann sich ein POMonitorEventListener für ein Ereignis registrieren
	 * 
	 * @param lis
	 *            ist der Listener, der das Ereignis empfängt
	 * @param evt
	 *            ist das Event, an dem der lis interessiert ist
	 */
	final public synchronized void addPOMonitorEventListener(
			POMonitorEventListener lis, POEventType evt) {
		ArrayList<POMonitorEventListener> receiver = null;
		// Entweder ist das Event schon vorhanden
		if (this.eventRegistrationList.containsKey(evt)) {
			// dann die Menge der Empfänger auslesen und who anhaengen
			// Aus Effizienzgründen ist hier eine ArrayList, um
			// Doppeleintragungen
			// zu vermeiden könnte man hier (und dann natürlich auch unten
			// weiter)
			// auch ein Set verwenden
			receiver = this.eventRegistrationList.get(evt);
		} else {
			// oder neues Event eintragen und neue Empfängerliste erzeugen
			receiver = new ArrayList<POMonitorEventListener>();
			this.eventRegistrationList.put(evt, receiver);
		}
		receiver.add(lis);
	}

	/**
	 * Registrieren für alle Events, die von diesem Objekt versandt werden
	 * 
	 * @param who
	 *            ist das Objekt, dass über alle Ereignisse informiert werden
	 *            möchte
	 */
	public synchronized void addPOMonitorEventListener(
			POMonitorEventListener who) {
		this.registeredForAllEvents.add(who);
	}

	/**
	 * Entferne (lis,evt) aus der Event-Registry, egal ob überhaupt eine
	 * Registrierung vorlag
	 * 
	 * @param lis
	 *            der EventListener
	 * @param evt
	 *            die zu löschende Event
	 */
	public synchronized void removePOMonitorEventListener(
			POMonitorEventListener lis, Integer evt) {
		ArrayList receiver = (ArrayList) this.eventRegistrationList.get(evt);
		if (receiver != null) {
			receiver.remove(lis);
		}
	}

	public synchronized void removePOMonitorEventListener(
			POMonitorEventListener who) {
		this.registeredForAllEvents.remove(who);
	}

	/* POEventListener.poEventOccured() */
	public void poEventOccured(POEvent ev) {
		long now = System.currentTimeMillis();
		//System.out.println(this.getName()+"poEventOccured "+ev.getClass());
		//if (getName().equals("xpMon")) System.err.println(getName()+":
		// "+ev.toString());
		if (ev.getSource() != tpo) {
			System.err.println("POMonitor: POEvent von unbekannter Quelle!");
			return;
		}
		try {
			pda.processEvent(ev, tpo.getPOName());
		} catch (IllegalStateTransition e) {
			System.err.println("POMonitor " + getName() + ":");
			System.err.println("Falscher Zustandsübergang: " + e);
			System.err.println("Letzter gültiger Zustand " + e.getLastState());
			System.err.println("PO " + tpo.getPOName());
			e.printStackTrace();
			return;
		}

		/* Art des Event */
		POEventType id = ev.getPOEventType();
		// Events verdichten
		while (true) { //Simulation von Case
			if (id == POEventType.OpenInit) {
				// Timer für Init starten
				if (this.initTimeOutMilliSec > 0) {
					this.initTimer.start();
				}
				break;
			}
			if (id == POEventType.OpenDone) {
				// Inittimer stoppen
				if (this.initTimeOutMilliSec > 0) {
					this.initTimer.stop();
				}
				// Erst jetzt macht es Sinn, sich über Datenraten
				// Gedanken zu machen
				if (this.datarateTestTimer != null) {
					datarateTestTimer.start();
				}
				break;
			}
			if (id == POEventType.ReadInit) {
				if (this.readTimeOutMilliSec > 0) {
					this.readTimer[((ReadInitEvent)ev).getPort()].start();
					//System.out.println("ReadTimer gestartet");
				}
				break;
			}
			if (id == POEventType.ReadDone) {
				// Timer zurücksetzen
				if (this.readTimeOutMilliSec > 0) {
				    this.readTimer[((ReadInitEvent)ev).getPort()].stop();
				}
				// Ablegen der aktuellen Zeit im Puffer
				if (readEventsOccured != null)
					this.readEventsOccured.add(now);
				// Test ob Anzahl Objekte gelesen
				int pda_noObjectsRead = pda.getObjectsRead();
				if (pda_noObjectsRead == this.noObjectsReadEvent.getCount()) {
					fireEvent(this.noObjectsReadEvent);
				}
				// Test ob n Objekte seit letztem Event gelesen wurden
				if (this.everyNoObjectsReadEvent.getCount() > 0
						&& (pda_noObjectsRead
								% this.everyNoObjectsReadEvent.getCount() == 0)) {
					fireEvent(this.everyNoObjectsReadEvent);
				}
				break;
			}
			if (id == POEventType.WriteInit) {
				if (this.writeTimeOutMilliSec > 0) {
					this.writeTimer.start();
				}
				break;
			}
			if (id == POEventType.WriteDone) {
				// Timer zurücksetzen
				if (this.writeTimeOutMilliSec > 0) {
					this.writeTimer.stop();
				}
				// Test ob Anzahl Objekte geschrieben
				int pda_noObjectsWritten = pda.getObjectsWritten();
				if (pda_noObjectsWritten == this.noObjectsWrittenEvent.getCount()) {
					fireEvent(this.noObjectsWrittenEvent);
				}
				// Test ob n Objekte seit letztem Event geschrieben wurden
				if (this.everyNoObjectsWrittenEvent.getCount() > 0
						&& (pda_noObjectsWritten % everyNoObjectsWrittenEvent.getCount()) == 0) {
					fireEvent(this.everyNoObjectsWrittenEvent);
				}
				break;
			}
			if (id == POEventType.CloseDone) {
				// Den Timer für die Ermittlung der Datenrate zurücksetzen:
				if (this.datarateTestTimer != null) {
					this.datarateTestTimer.stop();
				}
			}
			break;
		}
		// Das empfangene Event weiterleiten
		fireEvent(ev);
	}

	// Events feuern, achtung mit den Events darf nicht
	// gemacht werden. --> Sonst müsste jeder Empfaenger
	// ein eigenes Event bekommen
	private synchronized void fireEvent(POEvent evt) {
		//System.out.println(evt.getClass()+" "+evt.getID().intValue());
//		if (evt.getID().intValue() > 9000) {
//			//System.out.println(evt.getClass());
//			if (evt.getID() == ExceptionEvent.ID) {
//				System.err.println("POMontor fireEvent "
//						+ ((ExceptionEvent) evt).getMessage() + " ID "
//						+ evt.getID() + " Owner " + evt.getSource());
//			}
//		}
		try {
			/* Listener benachrichtigen */
			ArrayList<POMonitorEventListener> allReceivers = registeredForAllEvents;
			ArrayList<POMonitorEventListener> receivers = eventRegistrationList.get(evt.getPOEventType());
			if (receivers != null)
				allReceivers.addAll(receivers);
			if (allReceivers != null) {
				Iterator receiver = allReceivers.iterator();
				POMonitorEvent ev = new POMonitorEvent(this, evt, System
						.currentTimeMillis());
				while (receiver.hasNext()) {
					((POMonitorEventListener) receiver.next())
							.poMonitorEventOccured(ev);
				}
			}
		} catch (EmptyStackException e) {
			return;
		}
	}

	/*
	 * Die Timer werfen bei Ablauf ein actionPerformedEvent Hier dann
	 * entsprechender Ereignisse erzeugen. Wenn einmal ein Event geworfen wurde,
	 * wird im selben Zyklus (d.h. im selben Zustand) kein neues geworfen
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource().equals(this.initTimer)) {
			this.initTimer.stop();
			//System.out.println("OpenInitTimeout");
			this.fireEvent(this.initTimeoutEvent);
			return;
		}
		for (int i=0;i<readTimer.length;i++){
			if (e.getSource().equals(this.readTimer[i])) {
				this.readTimer[i].stop();
				//System.out.println("ReadTimeout");
				this.fireEvent(this.readTimeoutEvent[i]);
				return;
			}
		}
		if (e.getSource().equals(this.writeTimer)) {
			this.writeTimer.stop();
			//System.out.println("WriteTimeout");
			this.fireEvent(new WriteTimeoutEvent(this.getPlanOperator(),null));
			return;
		}
		// Wenn hier angekommen, dann Datenrate untersuchen (wenn überhaupt
		// Daten gesammelt wurden
		if (e.getSource().equals(this.datarateTestTimer)) {
			//System.out.println("Datenrate
			// "+this.readEventsOccured.getDatarate());
			//System.out.println("Zeit zwischen zwei Objekten
			// "+this.readEventsOccured.getAverageTimeBetweenObjects());
			//System.out.println("StdAbw Zeit zwischen zwei Objekten
			// "+this.readEventsOccured.getStandardDeviation());
		}
	}

	/**
	 * @param name  the name to set
	 * @uml.property  name="name"
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param initTimeOutMilliSec  the initTimeOutMilliSec to set
	 * @uml.property  name="initTimeOutMilliSec"
	 */
	public void setInitTimeOutMilliSec(int initTimeOutMilliSec) {
		this.initTimeOutMilliSec = initTimeOutMilliSec;
		if (initTimer == null) {
			this.initTimer = new Timer(initTimeOutMilliSec, this);
		} else {
			this.initTimer.setInitialDelay(initTimeOutMilliSec);
		}
	}

	/**
	 * @return  the initTimeOutMilliSec
	 * @uml.property  name="initTimeOutMilliSec"
	 */
	public int getInitTimeOutMilliSec() {
		return initTimeOutMilliSec;
	}

	/**
	 * @param readTimeOutMilliSec  the readTimeOutMilliSec to set
	 * @uml.property  name="readTimeOutMilliSec"
	 */
	public void setReadTimeOutMilliSec(int readTimeOutMilliSec) {
		this.readTimeOutMilliSec = readTimeOutMilliSec;
		if (readTimer == null) {
		    readTimer = new Timer[tpo.getNumberOfInputs()];
		    for (int i=0;i<tpo.getNumberOfInputs();i++){  
		        this.readTimer[i] = new Timer(readTimeOutMilliSec, this);
		    }
		} else {
		    for (int i=0;i<tpo.getNumberOfInputs();i++){
		        this.readTimer[i].setInitialDelay(readTimeOutMilliSec);
		    }
		}
	}

	/**
	 * @return  the readTimeOutMilliSec
	 * @uml.property  name="readTimeOutMilliSec"
	 */
	public int getReadTimeOutMilliSec() {
		return readTimeOutMilliSec;
	}

	/**
	 * @param writeTimeOutMilliSec  the writeTimeOutMilliSec to set
	 * @uml.property  name="writeTimeOutMilliSec"
	 */
	public void setWriteTimeOutMilliSec(int writeTimeOutMilliSec) {
		this.writeTimeOutMilliSec = writeTimeOutMilliSec;
		if (writeTimer == null) {
			this.writeTimer = new Timer(writeTimeOutMilliSec, this);
		} else {
			this.writeTimer.setInitialDelay(writeTimeOutMilliSec);
		}

	}

	/**
	 * @return  the writeTimeOutMilliSec
	 * @uml.property  name="writeTimeOutMilliSec"
	 */
	public int getWriteTimeOutMilliSec() {
		return writeTimeOutMilliSec;
	}

	public void setNoOfObjectsReadEventCount(int noOfObjectsRead) {
		this.noObjectsReadEvent = new  NumberOfObjectsReadEvent(this.tpo,noOfObjectsRead);
	}

	public void setNoOfObjectsWrittenEventCount(int noOfObjectsWritten) {
		this.noObjectsWrittenEvent = new NumberOfObjectsWrittenEvent(this.tpo, noOfObjectsWritten);
	}

	public void setEveryNObjectsReadEventCount(int everyNObjectsRead) {
		this.everyNoObjectsReadEvent = new NumberOfObjectsReadEvent(this.tpo, everyNObjectsRead);
	}


	public void setEveryNObjectsWrittenEventCount(int everyNObjectsWritten) {
		this.everyNoObjectsWrittenEvent = new NumberOfObjectsWrittenEvent(this.tpo, everyNObjectsWritten);
	}


	public void setTimeWindow(int timeWindow, int testDatarateEveryNMilliseconds) {
		this.readEventsOccured = new TimeRingBuffer(timeWindow);
		datarateTestTimer = new Timer(testDatarateEveryNMilliseconds, this);
	}

	/**
	 * @return  the timeWindow
	 * @uml.property  name="timeWindow"
	 */
	public int getTimeWindow() {
		return timeWindow;
	}

	public void start() {
		if (t == null) {
			t = new Thread(this, "Monitor "+ tpo.getPOName());
			t.start();
		}
	}

	public void stop() {
		t = null;
	}

	public void run() {
		while (t != null) {
			try {
				synchronized (this) {
					wait(1000);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		// Alle gestarteten Threads beenden
		if (initTimer != null && initTimer.isRunning())	initTimer.stop();
		if (readTimer != null){
			for (Timer t: readTimer){
				if (t!= null && t.isRunning()) t.stop();
			}
		}
		if (datarateTestTimer != null && datarateTestTimer.isRunning()) datarateTestTimer.stop();
		if (writeTimer != null && writeTimer.isRunning()) writeTimer.stop();
	}

	public TriggeredPlanOperator getPlanOperator() {
		return tpo;
	}

	public void poElementBufferEventOccured(POElementBufferEvent event) {
		this.fireEvent(event);
	}

	public long getLastStateChangeTime() {
		return pda.getLastStateChangeTime();
	}

	public int getObjectsRead() {
		return pda.getObjectsRead();
	}

	public int getObjectsWritten() {
		return pda.getObjectsWritten();
	}

	public POState getPOState() {
		return pda.getPOState();
	}

	public String getPOStateString() {
		return pda.getPOStateString();
	}

	public int getReadBlockedCount() {
		return pda.getReadBlockedCount();
	}

	public long getStateDurationTime() {
		return pda.getStateDurationTime();
	}
}