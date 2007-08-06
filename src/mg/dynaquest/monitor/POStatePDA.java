package mg.dynaquest.monitor;

/**
 Klasse implementiert den Kellerautomaten (pushdown automata (PDA)) der die 
 Zustandsübergänge der Planoperatoren abbildet

 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:11 $ 
 Version: $Revision: 1.17 $
 Log: $Log: POStatePDA.java,v $
 Log: Revision 1.17  2004/09/16 08:57:11  grawund
 Log: Quellcode durch Eclipse formatiert
 Log:
 Log: Revision 1.16  2004/08/25 11:26:29  grawund
 Log: Korrekturen bei der Behandlung der blockierten Eingabeports, ersten Übergang vergessen
 Log:
 Log: Revision 1.15  2004/08/25 09:33:36  grawund
 Log: Fehlerkorrekturen bei der Verwaltung der blockierten Ports (Cast-Fehler und Wenn Anzahl == 0)
 Log:
 Log: Revision 1.14  2004/08/25 09:20:41  grawund
 Log: Speicherung welche Inputports blockiert sind und Zugriffsmethode darauf
 Log: Konstruktor verlangt nun die Angabe der Anzahl der Eingabeports
 Log:
 Log: Revision 1.13  2004/08/25 09:11:14  grawund
 Log: Variable read_blocked in read_blocked_count umbenannt, da inzwischen die Anzahl der blockierten Eingabeports angegeben wird
 Log:
 Log: Revision 1.12  2004/08/25 08:54:31  grawund
 Log: POState ILLEGAL in ERROR umbenannt
 Log:
 Log: Revision 1.11  2004/08/25 08:44:54  grawund
 Log: Anpassungen an neuen Zustand finished
 Log:
 Log: Revision 1.10  2004/08/25 07:14:40  grawund
 Log: Anpassung an neues POEvent ProcessingTerminatedEvent
 Log:
 Log: Revision 1.9  2004/06/18 13:43:17  grawund
 Log: no message
 Log:
 Log: Revision 1.8  2004/06/11 10:19:34  grawund
 Log: Event-Urheber wird gekennzeichnet
 Log:
 Log: Revision 1.7  2003/07/25 09:55:03  hobelmann
 Log: Handling für ExceptionEvent
 Log:
 Log: Revision 1.6  2003/06/18 14:00:56  grawund
 Log: Debug_Infos erweitert
 Log:
 Log: Revision 1.5  2003/06/16 13:40:35  grawund
 Log: Einige Debug-Ausgaben
 Log: processEvent jetzt Synchronized
 Log:
 Log: Revision 1.4  2002/02/20 15:51:34  grawund
 Log: Fall 2 beim IMDB-Zugriff umgesetzt
 Log:
 Log: Revision 1.3  2002/02/18 13:45:07  grawund
 Log: Version in der der Collector funktioniert und keine illegalen Zustaende mehr liefert
 Log:
 Log: Revision 1.2  2002/01/31 16:15:41  grawund
 Log: Versionsinformationskopfzeilen eingefuegt
 Log:
 */

import mg.dynaquest.queryexecution.event.*;
import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * @author  Marco Grawunder
 */
public class POStatePDA {

	/**
	 * @uml.property  name="monState"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private POState monState = POState.INACTIVE;

	/**
	 * @uml.property  name="read_blocked_count"
	 */
	private int read_blocked_count = 0;

	/**
	 * @uml.property  name="lastStateChange"
	 */
	private long lastStateChange = -1;

	/**
	 * @uml.property  name="objects_written"
	 */
	private int objects_written = 0;

	/**
	 * @uml.property  name="objects_read"
	 */
	private int objects_read = 0;

	/**
	 * @uml.property  name="inportPortsBlocked" multiplicity="(0 -1)" dimension="1"
	 */
	private boolean[] inportPortsBlocked = null;

	/**
	 * @uml.property  name="noInputPorts"
	 */
	private int noInputPorts = 0;

	// Wechselt nicht in den Blockierungsmode
	/**
	 * @uml.property  name="ignoreBlockingModes"
	 */
	private boolean ignoreBlockingModes = false;

	/**
	 * @uml.property  name="debug"
	 */
	public boolean debug = false;

	public POStatePDA(int noInputPorts) {
		this.noInputPorts = noInputPorts;
		//System.out.println("no Of Input ports "+noInputPorts);
		reInit();
	}

	public void reInit() {
		monState = POState.INACTIVE;
		read_blocked_count = 0;
		lastStateChange = -1;
		objects_written = 0;
		objects_read = 0;
		ignoreBlockingModes = false;
		this.inportPortsBlocked = new boolean[noInputPorts];
	}

	private void setPOState(POState newState) {
		monState = newState;
		lastStateChange = System.currentTimeMillis();
	}

	/**
	 * @param ignoreBlockingModes  the ignoreBlockingModes to set
	 * @uml.property  name="ignoreBlockingModes"
	 */
	public void setIgnoreBlockingModes(boolean ignoreBlockingModes) {
		this.ignoreBlockingModes = ignoreBlockingModes;
	}

	public POState getPOState() {
		return monState;
	}

	public String getPOStateString() {
		if (monState == POState.READ_BLOCKED) {
			StringBuffer ret = new StringBuffer(monState.toString() + "(");
			for (int i = 0; i < this.noInputPorts; i++) {
				if (inportPortsBlocked[i])
					ret.append("-");
				else
					ret.append("+");
			}
			ret.append(")");
			return ret.toString();
		} else {
			return monState.toString();
		}
	}

	public int getReadBlockedCount() {
		return read_blocked_count;
	}

	public long getLastStateChangeTime() {
		return lastStateChange;
	}

	public long getStateDurationTime() {
		return System.currentTimeMillis() - lastStateChange;
	}

	public int getObjectsWritten() {
		return this.objects_written;
	}

	public int getObjectsRead() {
		return this.objects_read;
	}

	public void processEvent(POEvent event) throws IllegalStateTransition {
		processEvent(event, ((TriggeredPlanOperator) event.getSource())
				.getPOName());
	}

	public synchronized void processEvent(POEvent event, String poname)
			throws IllegalStateTransition {
		if (debug) {
			System.out.println(this.toString() + "(" + this.hashCode()
					+ ") Verarbeite Event " + event + " aktueller Zustand "
					+ getPOStateString() + " von " + poname);
		}

		/* illegaler Zustand; nach einer ExceptionEvent steht der Automat still */
		if (event.getPOEventType() == POEventType.Exeception)
			setPOState(POState.ERROR);

		if (monState == POState.INACTIVE) {
			if (event.getPOEventType() == POEventType.OpenInit) {
				setPOState(POState.OPEN);
			} else {
				IllegalStateTransition exep = new IllegalStateTransition(
						monState + "(" + poname + ")" + " " + event.toString());
				exep.setLastState(monState);
				setPOState(POState.ERROR);
				throw exep;
			}
		} else if (monState == POState.OPEN) {
			if (event.getPOEventType() == POEventType.OpenDone) {
				setPOState(POState.INITIALIZED);
			} else {
				IllegalStateTransition exep = new IllegalStateTransition(
						monState + "(" + poname + ")" + " " + event.toString());
				exep.setLastState(monState);
				setPOState(POState.ERROR);
				throw exep;
			}
		} else if (monState == POState.INITIALIZED) {
			if (event.getPOEventType() == POEventType.NextInit) {
				setPOState(POState.NEXT);
			} else if (event.getPOEventType() == POEventType.ProcessingFinished) {
				setPOState(POState.FINISHED);
			} else {
				IllegalStateTransition exep = new IllegalStateTransition(
						monState + "(" + poname + ")" + " " + event.toString());
				exep.setLastState(monState);
				setPOState(POState.ERROR);
				throw exep;
			}
		} else if (monState == POState.FINISHED) {
			if (event.getPOEventType() == POEventType.CloseInit) {
				setPOState(POState.CLOSE);
			} else {
				IllegalStateTransition exep = new IllegalStateTransition(
						monState + "(" + poname + ")" + " " + event.toString());
				exep.setLastState(monState);
				setPOState(POState.ERROR);
				throw exep;
			}
		} else if (monState == POState.CLOSE) {
			if (event.getPOEventType() == POEventType.CloseDone) {
				//        setPOState(POState.INACTIVE);
				this.reInit();
			} else {
				IllegalStateTransition exep = new IllegalStateTransition(
						monState + "(" + poname + ")" + " " + event.toString());
				exep.setLastState(monState);
				setPOState(POState.ERROR);
				throw exep;
			}
		} else if (monState == POState.NEXT) {
			if (event.getPOEventType() == POEventType.ReadInit) {
				if (!this.ignoreBlockingModes) {
					setReadBlockedState(((ReadInitEvent) event).getPort());
				}
			} else if (event.getPOEventType() == POEventType.WriteInit) {
				if (!this.ignoreBlockingModes) {
					setPOState(POState.WRITE_BLOCKED);
				}
			} else if (event.getPOEventType() == POEventType.NextDone) {
				setPOState(POState.INITIALIZED);
			} else if ((event.getPOEventType() == POEventType.ReadDone && this.ignoreBlockingModes)) {
				this.objects_read++;
			} else if ((event.getPOEventType() == POEventType.WriteDone && this.ignoreBlockingModes)) {
				this.objects_written++;
			} else {
				IllegalStateTransition exep = new IllegalStateTransition(
						monState + "(" + poname + ")" + " " + event.toString());
				exep.setLastState(monState);
				setPOState(POState.ERROR);
				throw exep;
			}
		} else if (monState == POState.READ_BLOCKED) {
			if (event.getPOEventType() == POEventType.ReadInit) {
				this.setReadBlockedState(((ReadInitEvent) event).getPort());
			} else if (event.getPOEventType() == POEventType.ReadDone) {
				read_blocked_count--;
				if (this.noInputPorts > 0) {
					inportPortsBlocked[((ReadDoneEvent) event).getPort()] = false;
				}
				this.objects_read++;
				if (read_blocked_count == 0) {
					setPOState(POState.NEXT);
				}
			} else {
				IllegalStateTransition exep = new IllegalStateTransition(
						monState + "(" + poname + ")" + " " + event.toString());
				exep.setLastState(monState);
				setPOState(POState.ERROR);
				throw exep;
			}

		} else if (monState == POState.WRITE_BLOCKED) {
			if (event.getPOEventType() == POEventType.WriteDone) { // write_done
				setPOState(POState.NEXT);
				objects_written++;
			} else {
				IllegalStateTransition exep = new IllegalStateTransition(
						monState + "(" + poname + ")" + " " + event.toString());
				exep.setLastState(monState);
				setPOState(POState.ERROR);
				throw exep;
			}
		}
	}

	private void setReadBlockedState(int pos) {
		setPOState(POState.READ_BLOCKED);
		read_blocked_count++;
		if (this.noInputPorts > 0) {
			inportPortsBlocked[pos] = true;
			//System.out.println("Setze "+pos+" auf true");
		}
	}

	public boolean[] inportPortsBlocked() {
		return this.inportPortsBlocked;
	}
}