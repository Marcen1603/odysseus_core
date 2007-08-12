package mg.dynaquest.queryexecution.po.base;

/**
 Author: $Author: grawund $
 Date: $Date: 2004/09/16 08:57:12 $ 
 Version: $Revision: 1.10 $
 */

import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.ExceptionEvent;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.event.ReadDoneEvent;
import mg.dynaquest.queryexecution.event.ReadInitEvent;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFPredicate;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFSchemaElementSet;
import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author  Marco Grawunder
 */
public abstract class NAryPlanOperator extends TriggeredPlanOperator implements Cloneable{

    /**
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger logger = Logger.getLogger(this.getClass().getName());
    
    /**
	 * @uml.property  name="algebraPO"
	 * @uml.associationEnd  
	 */
    private AlgebraPO algebraPO;
  
    /**
	 * @uml.property  name="executionPlace"
	 */
    private URI executionPlace = null;    
    
	/**
	 * @uml.property  name="input"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	private PlanOperator[] input;

	/**
	 * @uml.property  name="inputID" multiplicity="(0 -1)" dimension="1"
	 */
	private String[] inputID;

	private ArrayList<Thread> openerThreads = null;

	
	// Menge Event-Objekte
	/**
	 * @uml.property  name="readInitEvent"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	protected ReadInitEvent[] readInitEvent;

	/**
	 * @uml.property  name="readDoneEvent"
	 * @uml.associationEnd  multiplicity="(0 -1)"
	 */
	protected ReadDoneEvent[] readDoneEvent;

	public NAryPlanOperator(){
	    super();
	}
	
    /**
     * Initialisierung eines PlanOperators aus einem
     * bestehenden. Wird benötigt, um sämtlich Daten eines
     * logischen Operators auf sein physisches Pendant zu
     * übertragen
     * @param operator
     */
    protected NAryPlanOperator(NAryPlanOperator operator) {
    	super(operator);
        this.executionPlace = operator.executionPlace;
        if (operator.input != null){
            input = new PlanOperator[operator.input.length];
            System.arraycopy(operator.input, 0, this.input, 0, operator.input.length);
        }
        if (operator.inputID != null){
            inputID = new String[operator.inputID.length];
            System.arraycopy(operator.inputID, 0, this.inputID, 0, operator.inputID.length);
        }
        if (operator.algebraPO == null){
        	log.warn("Algebra Operator in "+operator+" nicht gesetzt!!!");
        }
        setAlgebraPO(operator.algebraPO);
    }
  
    protected NAryPlanOperator(AlgebraPO algebraPO) {
    	setAlgebraPO(algebraPO);
    }
    
    
    public SDFAttributeList getOutElements(){
    	return getAlgebraPO().getOutElements();
    }
    
    public SDFPredicate getPredicate(){
    	return getAlgebraPO().getPredicate();
    }

    public SDFSchemaElementSet getSorted(){
    	return getAlgebraPO().getSorted();
    }
    
    /**
     * @return Returns the executionPlace.
     * 
     * @uml.property name="executionPlace"
     */
    public synchronized URI getExecutionPlace() {
        return executionPlace;
    }

    /**
     * @param executionPlace The executionPlace to set.
     * 
     * @uml.property name="executionPlace"
     */
    public synchronized void setExecutionPlace(URI executionPlace) {
        this.executionPlace = executionPlace;
    }


	public String getInputID(int no) {
		return inputID[no];
	}

	protected void setInputID(int pos, String id) {
		inputID[pos] = id;
	}

	public final int getNumberOfInputs() {
		if (input != null) {
			return input.length;
		} else {
			return 0;
		}
	}

	public boolean isInitialized() {
		return input != null;
	}

	public synchronized void setNoOfInputs(int count) {
		this.input = new PlanOperator[count];
		this.inputID = new String[count];
		readInitEvent = new ReadInitEvent[count];
		for (int i = 0; i < count; i++) {
			readInitEvent[i] = new ReadInitEvent(this, i);
		}
		readDoneEvent = new ReadDoneEvent[count];
		for (int i = 0; i < count; i++) {
			readDoneEvent[i] = new ReadDoneEvent(this, i);
		}
	}

	public synchronized void setInputPO(int pos, PlanOperator input) { 
	    //System.out.println(this.toString()+" PlanOperator.setInputPO("+pos+","+input+")");
		this.input[pos] = input;
	}

    public synchronized void setInputPO(PlanOperator input) { 
        //System.out.println(this.toString()+" PlanOperator.setInputPO("+pos+","+input+")");
        setInputPO(0, input);
    }

    
	public PlanOperator getInputPO(int pos) {
		return input[pos];
	}

	public void replaceInput(PlanOperator oldInput,
			PlanOperator newInput) {
		for (int i = 0; i < this.getNumberOfInputs(); i++) {
			if (this.getInputPO(i) == oldInput) {
				this.setInputPO(i, newInput);
				break;
			}
		}
	}

	@SuppressWarnings("unchecked")
	protected <T> T getInputNext(int pos, SimplePlanOperator caller, long timeout)
			throws POException, TimeoutException {
		T retVal = null;
        logger.debug(this.getPOName()+" ::getInputNext(int "+ pos+", PlanOperator "+caller.getPOName()+")");
        this.notifyPOEvent(readInitEvent[pos]);
		retVal = (T) this.input[pos].next(caller, timeout);        
		this.notifyPOEvent(readDoneEvent[pos]);
        logger.debug(this.getPOName()+" ::getInputNext durch (int "+ pos+", PlanOperator "+caller.getPOName()+")");
		return retVal;
	}

	//final public synchronized boolean open(PlanOperator caller){
    // Nicht final, da ReadsErrorElements open überschreiben muss!
	public synchronized void open(SimplePlanOperator caller) {
		//boolean retValue = true;

		try {
			if (this.isClosed()) {
				logger.debug("Starte Open von " + this.getPOName());
				this.notifyPOEvent(this.openInitEvent);
				//retValue =
				process_open();
				
				
				//threadToOpenMap = new HashMap<Thread,PlanOperator>(this.getNumberOfInputs());
                openerThreads = new ArrayList<Thread>();
				for (int i = 0; i < this.getNumberOfInputs(); i++) {
					PlanOperator po = getInputPO(i);
					Thread worker = new OpenerThread("open "+this.getPOName()+" "+i, this,po);
                    openerThreads.add(worker);
					//threadToOpenMap.put(worker, po);
				}

				logger.debug(this.getPOName()+" Starte die Threads zum Öffnen ");

                for (Thread t:openerThreads){
                    logger.debug("Starte Thread "+t.getName());
                    t.start();                    
                }

				logger.debug(this.getPOName()+" warte darauf, dass alle  Worker-Threads terminieren ");
			             
                for (Thread t:openerThreads){
                    t.join();
                    logger.debug(t.getName()+" terminiert...");
                    notifyAll();
                }
                
                logger.debug(this.getPOName()+" alle Worker-Threads terminiert");

			}
			this.addConsumer(caller);
			if (this.isOpened()) {
				this.notifyPOEvent(this.openDoneEvent);
			}
			logger.debug("Beende Open von " + this.getPOName() + " #Consumer "
					+ this.getNoOfRegisteredConsumers());

		} catch (Exception e) {
			e.printStackTrace();
			this.notifyPOEvent(new ExceptionEvent(this, e.getMessage()));
			// Den Puffer auf null setzen, damit der PO anhält
			// this.setMaxElementsBufferSize(0);

			try {
				this.waitForOpen(); // Blockieren
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		//return retValue;
	}

   // Nicht final, da ReadsErrorElements close überschreiben muss!
	public boolean close(SimplePlanOperator caller)
			throws POException {
		boolean retValue = true;
		logger.debug("Starte Close von " + this.getPOName());
		// Nur fuer den ersten!
		if (this.isOpened()) {
			this.notifyPOEvent(this.closeInitEvent);
			retValue = process_close();
			for (int i = 0; i < getNumberOfInputs(); i++) {				
				PlanOperator po = getInputPO(i);
				retValue = retValue && po.close(this);
			}
		}
		this.removeConsumer(caller);
		if (this.isClosed()) {
			this.notifyPOEvent(this.closeDoneEvent);
		}
		logger.debug("Beende Close von " + this.getPOName() + " #Consumer "
				+ this.getNoOfRegisteredConsumers());
		return retValue;
	}

	protected synchronized void setOpenFinished(Thread t) {
		this.notifyAll();
	}

	// ------------------------------------------------------------------------
	// Serialierung bzw. Initialisierung mittels XML
	// ------------------------------------------------------------------------

	/**
	 * Schreibt XML-Repräsentation eines Anfrageplanes in einen übergebenen StringBuffer
	 * @param StringBuffer, in den der XML-Plan geschrieben wird
	 */
	public void getXMLPlan(StringBuffer retBuffer) {
		retBuffer.append("<?xml version='1.0'?>\n");
		retBuffer.append("<plan>\n");
		getXMLRepresentation("  ", "   ", retBuffer);
		retBuffer.append("</plan>");
	}
	
	// Diese Methode schreibt den Operator und alle Kinder in einer XML-Repraesentation in
	// den StringBuffer xmlRetValue und liefert als Rueckgabe seine
	// eigene (eindeutige ID)
	private String getXMLRepresentation(String baseIndent, String indent,
			StringBuffer retBuffer) {

		// Diese Daten muessen alls POs liefern
		retBuffer.append(baseIndent + "<po class='" + this.getClass().getName() + "' id='"
				+ this.PO_ID + "'");
		String name = this.getPOName();
		if (name != null && !name.equals("")) {
			retBuffer.append(" name='").append(name).append("'");
		}
		// Größe des Ausgabepuffers, kann bei Statistiken eine Rolle spielen
		retBuffer.append(" outputBufferSize='");
		retBuffer.append(getMaxElementsBufferSize());
		retBuffer.append("'");
		retBuffer.append(">\n");
		// Jetzt die fuer jeden PO spezifischen Daten auslesen
		getInternalXMLRepresentation(baseIndent + indent, indent, retBuffer);

		// Anzahl der inputPOs (Jurij)
		retBuffer.append(baseIndent + indent + "<inputIDs  count='" + this.getNumberOfInputs() + "'/>\n");

		// Die Input-Knoten
		StringBuffer[] subNodes = new StringBuffer[this.getNumberOfInputs()];
		for (int i = 0; i < this.getNumberOfInputs(); i++) {
            // Hier muss jetzt eine Unterscheidung erfolgen, ob hier
            // die Eingabe aus einem ErrorStrom oder aus einem normalen Strom 
            // erfolgen
            // TODO!!!
			subNodes[i] = new StringBuffer();
			String idref = ((NAryPlanOperator) this.getInputPO(i)).getXMLRepresentation(baseIndent,
					indent, subNodes[i]);

			retBuffer.append(baseIndent + indent + "<input pos='" + i
					+ "' idref='" + idref + "'/>\n");
		}

		// noch abschliessen
		retBuffer.append(baseIndent + "</po>\n");
		// und die Kinder-Knoten (also die Input liefernden Knoten) ausgeben
		for (int i = 0; i < subNodes.length; i++) {
			retBuffer.append("\n"); // Eine Leerzeile zur Übersichtlichkeit
			retBuffer.append(subNodes[i]);
		}

		// und die GUID zurückliefern (neu ab 27.9.06) 
		return this.PO_ID;
	}

	// Der Rückgabewert ist die ID, die jeder Knoten haben muss
	/**
	 * Initialisiert Werte eines Knotens 
	 * @return ID des Knotens 
	 */
	
	public String initBaseValues(Node node) {
		// Jetzt aus dem Knoten alle relevanten Daten herauslesen
		// Id und eventuell Name sind als Attribute codiert
		// dies ist wieder fuer alle Knoten gleich
		NamedNodeMap attribs = node.getAttributes();
		String id = attribs.getNamedItem("id").getNodeValue();
		String name = attribs.getNamedItem("name").getNodeValue();
		this.setPOName(name);

		// Erst mal die Input-Knoten auslesen
		NodeList children = node.getChildNodes();
		
		// Anzhal der inputID auslesen
		for (int i = 0; i < children.getLength(); i++) 
		{
			Node cNode = children.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("inputIDs")) 
			{
				NamedNodeMap posAttribs = cNode.getAttributes();
				String count = posAttribs.getNamedItem("count").getNodeValue();
				this.setNoOfInputs(Integer.parseInt(count));	 // Initialisieren des this.inputID[] => WICHTIG
				break;
			}
				
		}
		
		// Einzelne Inputs verarbeiten
		for (int i = 0; i < children.getLength(); i++) {
			Node cNode = children.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("input")) 
			{
				// Die Werte sind in den Attributen von Input codiert
				NamedNodeMap posAttribs = cNode.getAttributes();
				String pos = posAttribs.getNamedItem("pos").getNodeValue();
				String idref = posAttribs.getNamedItem("idref").getNodeValue();

				if (pos != null && idref != null) {
					this.setInputID(Integer.parseInt(pos), idref);
				} else {
					// Hier jetzt noch die Fehlermeldung werfen, dass
					// beim Init was schief gelaufen ist!!
					return null;
				}
				continue;
			}
		}

		// und jetzt das spezielle Processing der einzelnen Knoten
		initInternalBaseValues(children);

		return id;
	}
	

	// Hiermit werden die Eingabefelder festgelegt, die im ersten
	// Durchlauf beim Einlesen des XML-Files erst mal mit String-ID
	// belegt worden sind. In idToRefs findet sich die Abbildung dieser
	// String-IDs auf die tatsächlich initialisierten Objekte
	public boolean initInputs(HashMap idsToRefs) {
		boolean initSuccess = true;
		for (int i = 0; i < this.getNumberOfInputs() && initSuccess; i++) {
			PlanOperator tpo = (PlanOperator) idsToRefs
					.get(getInputID(i));
			// Falls keine Eingaben gefunden worden sind, kann der PO nicht
			// richtig
			// initialisiert werden (und vor allem auch nicht ausgeführt!)
			if (tpo == null) {
				initSuccess = false;
			} else {
				this.setInputPO(i, tpo);
			}
		}
		return initSuccess;
	}
    /**
     * @return
     */
    public double getCPUCost() {
        // TODO Auto-generated method stub
        return 0;
    }
    /**
     * @return
     */
    public double getMemCost() {
        // TODO Auto-generated method stub
        return 0;
    }
    /**
     * @return
     */
    public double getCommCost() {
        // TODO Auto-generated method stub
        return 0;
    }

	/**
	 * @return  the algebraPO
	 * @uml.property  name="algebraPO"
	 */
	public AlgebraPO getAlgebraPO() {
		return algebraPO;
	}

	/**
	 * @param algebraPO  the algebraPO to set
	 * @uml.property  name="algebraPO"
	 */
	public void setAlgebraPO(AlgebraPO algebraPO) {
		this.algebraPO = algebraPO;
	}

	public void startChildren() {
		if (input != null){
			for (PlanOperator p:input){
				((NAryPlanOperator)p).start();
				((NAryPlanOperator)p).startChildren();
			}
		}
	}
		

	public void stopChildren() {
		if (input != null){
			for (PlanOperator p:input){
				((NAryPlanOperator)p).stopChildren();
				((NAryPlanOperator)p).stop();
			}
		}
	}
}

/**
 * @author  Marco Grawunder
 */
class OpenerThread extends Thread {
    /**
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger logger = Logger.getLogger(this.getClass().getName());
    /**
	 * @uml.property  name="caller"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    PlanOperator caller = null;

    /**
	 * @uml.property  name="toCall"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    PlanOperator toCall = null;

    public OpenerThread(String name, PlanOperator caller,
            PlanOperator toCall) {
        super(name);
        this.caller = caller;
        this.toCall = toCall;

    }

    public void run() {
        logger.debug(caller.getPOName()+": Rufe Open auf " + toCall.getPOName()+ " auf");
        try {
            toCall.open(caller);
        } catch (POException e) {
        	logger.warn(e.getStackTrace().toString());
        }
    }
}

