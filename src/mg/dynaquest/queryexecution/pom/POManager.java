package mg.dynaquest.queryexecution.pom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import mg.dynaquest.monitor.POMonitor;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.base.SimplePlanOperator;
import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;

/**
 * Klasse die eine Instanz der POManagers bereitstellen Singleton! Wenn man sich für Veränderungen im POManager interessiert (z.B.) um sich bei den POMonitoren zu registrieren, kann man sich als POManagerEventListener registrieren.
 */

public class POManager implements Runnable {

	static private POManager manager = null;

	/**
	 * @uml.property  name="topOperators"
	 * @uml.associationEnd  qualifier="top:mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator"
	 */
	private HashMap<PlanOperator,PlanOperator> topOperators = new HashMap<PlanOperator,PlanOperator>();

	/**
	 * @uml.property  name="allOperators"
	 * @uml.associationEnd  qualifier="po:mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator"
	 */
	private HashMap<PlanOperator,PlanOperator> allOperators = new HashMap<PlanOperator,PlanOperator>();

	/**
	 * @uml.property  name="poMonitors"
	 * @uml.associationEnd  qualifier="po:mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator mg.dynaquest.monitor.POMonitor"
	 */
	private HashMap<PlanOperator,POMonitor> poMonitors = new HashMap<PlanOperator,POMonitor>();

	private ArrayList<POManagerEventListener> eventListener = new ArrayList<POManagerEventListener>();

	/**
	 * @uml.property  name="t"
	 */
	private Thread t = null;

	protected POManager() {
	}

	public void start() {
		if (t == null) {
			t = new Thread(this, "POManager");
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
	}

	public static POManager getInstance() {
		if (manager == null) {
			manager = new POManager();
		}
		return manager;
	}

	// -----------------------------------------------------------------------
	// Event-Verarbeitung
	public void addPOManagerEventListener(POManagerEventListener listener) {
		this.eventListener.add(listener);
	}

	public void removePOManagerEventListener(POManagerEventListener listener) {
		this.eventListener.remove(listener);
	}

	public void fireEvent(POManagerEvent event) {
		ArrayList receivers = (ArrayList) eventListener.clone();
		Iterator iter = receivers.iterator();
		while (iter.hasNext()) {
			((POManagerEventListener) iter.next()).poManagerEventOccured(event);
		}
	}

	/**
	 * Installiert einen neuen Plan in der POBox
	 * 
	 * @param top
	 *            ist der oberste Operator des Plans, der installiert werden
	 *            soll.
	 */
	public boolean installNewPlan(TriggeredPlanOperator top) {
		boolean success = false;
		if (top != null) {
			if (topOperators.get(top) == null) {
				success = installPlan(top, false);
			}
		}
		return success;
	}

	private static void replaceConsumers(PlanOperator oldPart,
            PlanOperator newPart) {
		List<SimplePlanOperator> consumer = ((TriggeredPlanOperator) oldPart).getRegisteredConsumers();
		Iterator iter = consumer.iterator();
		while (iter.hasNext()) {
			// TODO: Replace Consumers
			//((PlanOperator) iter.next()).replaceInput(oldPart, newPart);
		}
		throw new NotImplementedException();
	}

	public void replaceSubPlan(TriggeredPlanOperator oldPart,
			TriggeredPlanOperator newPart) {
		removeTree(oldPart);
		insertTree(newPart);
		replaceConsumers(oldPart, newPart);
	}

	/**
	 * Entfernt einen Plan aus der POBox
	 */
	public boolean removePlan(PlanOperator top) {
		boolean success = false;
		if (top != null) {
			// Erst aus der Liste der TopOperatoren entfernen
			if (topOperators.remove(top) != null) {
				// Und dann aus der Liste aller Operatoren
				removeTree(top);
			}
		}
		return success;

	}

	protected void removeTree(PlanOperator po) {
		if (allOperators.get(po) != null) {
			((TriggeredPlanOperator) this.allOperators.remove(po)).stop();
			POMonitor pom = ((POMonitor) this.poMonitors.remove(po));
			((TriggeredPlanOperator) po).removePOElementBufferEventListener(pom);
			pom.stop();
			this.fireEvent(new PORemovedEvent(this, pom));
			for (int i = 0; i < po.getNumberOfInputs(); i++) {
				removeTree((PlanOperator) po.getInputPO(i));
			}
		}
	}

	protected boolean insertTree(TriggeredPlanOperator po) {
		boolean success = false;
		// Nur falls der Op noch nicht initialisiert ist (doppelte Wege im
		// Graph)
		if (allOperators.get(po) == null) {
			// Den Operator in die Verwaltungsstruktur aufnehmen
			this.allOperators.put(po, po);
			// Einen entsprechenden POMonitor erzeugen und zuordnen
			POMonitor pom = new POMonitor(po.getPOName() + "Monitor",
					 po);
			po.addPOElementBufferEventListener(pom);
			// Die Threads starten
//			po.start();
//			pom.start();
			// Den Monitor in die Verwaltungsstruktur aufnehmen
			this.poMonitors.put(po, pom);
			// Jetzt irgendwo noch mitteilen, dass sich hier
			// was getan hat --> für EventListener
			this.fireEvent(new POInsertedEvent(this, pom));
			for (int i = 0; i < po.getNumberOfInputs(); i++) {
				insertTree((TriggeredPlanOperator) po.getInputPO(i));
			}
			success = true;
		}
		return success;
	}

	/**
	 * Hilfsmethode die einen Plan in der POBox installiert und dabei einen
	 * eventuell vorhandenen Plan (mit dem selben top) entfernt
	 */
	protected boolean installPlan(TriggeredPlanOperator top, boolean removeExisting) {
		boolean success = false;
		if (removeExisting) {
			success = removePlan(top);
		}
		topOperators.put(top, top);
		success = insertTree(top);
		return success;
	}

	public void materializeResults(TriggeredPlanOperator po) {
		po.switchToDHMemoryMode();
	}

	public void stopSubtree(TriggeredPlanOperator po) {		
		po.stop();
		poMonitors.get(po).stop();
		for (int i = 0; i < po.getNumberOfInputs(); i++) {
			stopSubtree((TriggeredPlanOperator) po.getInputPO(i));
		}		
	}

	public void startSubtree(TriggeredPlanOperator po) {
		po.start();
		poMonitors.get(po).start();
		for (int i = 0; i < po.getNumberOfInputs(); i++) {
			startSubtree((TriggeredPlanOperator) po.getInputPO(i));
		}
	}

	private static void replaceInputs(TriggeredPlanOperator po, TriggeredPlanOperator newPo) {
		for (int i = 0; i < po.getNumberOfInputs(); i++) {
			newPo.setInputPO(i, po.getInputPO(i));
		}
	}

	public static void substitutePO(TriggeredPlanOperator po, TriggeredPlanOperator newPo) {
		replaceConsumers(po, newPo);
		replaceInputs(po, newPo);
	}

	public POMonitor getPOMonitor(PlanOperator po) {
		return this.poMonitors.get(po);
	}
	
	public Collection<POMonitor> getPOMonitors(){
		return this.poMonitors.values();
	}
	

	public static int getMaxMem(TriggeredPlanOperator po) {
		return po.getMaxMem();
	}

	public static void setMaxMem(TriggeredPlanOperator po, int maxMem) {
		po.setMaxMem(maxMem);
	}

	public static int getUsedMem(TriggeredPlanOperator po) {
		return po.getUsedMem();
	}

	public int getAvaiableMem() {
		// TODO!!
		return 100000;
	}

	public static int getMaxUsedBuffer(TriggeredPlanOperator po) {
		return po.getMaxFilledBufferElements();
	}

	public static int getBufferSize(TriggeredPlanOperator po) {
		return po.getMaxElementsBufferSize();
	}

	public static void setBufferSize(TriggeredPlanOperator po, int size) {
		po.setMaxElementsBufferSize(size);
	}
	
	public static void setSubtreeBufferSize(TriggeredPlanOperator po, int size) {
		setBufferSize(po, size);
		for (int i=0;i<po.getNumberOfInputs();i++){
			setSubtreeBufferSize((TriggeredPlanOperator)po.getInputPO(i), size);
		}
	}


	public static void main(String[] args) {

	}
}