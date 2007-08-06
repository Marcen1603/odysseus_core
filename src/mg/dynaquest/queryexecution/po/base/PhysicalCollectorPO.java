package mg.dynaquest.queryexecution.po.base;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.algebra.CollectorPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

public class PhysicalCollectorPO extends NAryPlanOperator {
	
	 /**
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger logger = Logger.getLogger(this.getClass().getName());
    
    private ArrayList<Thread> threadList = null;
    
    public PhysicalCollectorPO(PhysicalCollectorPO collectorPO) {
		super(collectorPO);
	}

	public PhysicalCollectorPO() {
		super();
	}

	public PhysicalCollectorPO(CollectorPO collectorPO) {
		super(collectorPO);
		setNoOfInputs(collectorPO.getNumberOfInputs());
	}

	public String getInternalPOName() {
        return "CollectorPO";
    }

    protected void getInternalXMLRepresentation(String baseIndent,
            String indent, StringBuffer xmlRetValue) {
        // DoNothing
    }

    protected void initInternalBaseValues(NodeList childNodes) {
        // DoNothing
    }

    protected synchronized boolean process_open() throws POException {
        // In Process_Open muss ich jetzt fuer jeden Input-Knoten
        // einen Thread erzeugen, der die Daten aus einem Input
        // Knoten liest und in den Output-Buffer schreibt

        // Aber natuerlich nur dann, wenn nicht schon
        // offen ist !
        logger.debug(this.getPOName() + " Process Open Versuch");

        if (threadList == null) {
            logger.debug(this.getPOName() + " Process Open");
            //inputFinished = new boolean[this.getNumberOfInputs()];
            threadList = new ArrayList<Thread>(this.getNumberOfInputs());
            for (int i = 0; i < this.getNumberOfInputs(); i++) {
                Thread worker = new NextInputReader(this,i,"next "+this.getPOName()+" "+i);
                threadList.add(worker);
            }
        }

        return true;
    }

    protected synchronized Object process_next() throws POException {

        // Hinweis: Dieses process_next() muss sich systembedingt anders verhalten
        // als die normalen process_next()-Methoden, da hier in jedem Durchlauf mehr als 
        // ein Element geschrieben werden kann, d.h. dieses process_next() wird aus
        // der run()-Methode von TriggerdPlanOperator genau einmal aufgerufen, startet 
        // alle Threads, die die Daten hochschaufeln und wenn alle Threads terminiert sind, wird 
        // null als Zeichen der Terminierung zurückgeliefert.
        
        // Threads erst hier starten, weil ich jetzt erst sicher sein
        // kann, dass der gesamte Baum initialisiert ist
        for (Thread t: threadList){
            logger.debug(t.getName()+" gestartet");
            t.start();
        }

        // Process-next muss jetzt so lange blockieren, bis alle anderen
        // Thread terminiert sind

        logger.debug(this.getPOName()
                + " warte darauf, dass alle Threads zum Daten holen terminieren");

        for (Thread t: threadList){
            try {
                logger.debug(this.getPOName()+" "+t.getName()+" warte auf Terminierung");
                t.join();
                logger.debug(this.getPOName()+" "+t.getName()+" terminiert");
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }           
        
       logger.debug(this.getPOName() + " alle Threads zum Daten holen terminiert");
        // Jetzt noch fuer den nachfolgenden Operator festlegen, dass
        // alle Elemente verarbeitet worden sind.      
        return null;
    }



    protected synchronized boolean process_close() throws POException {
        threadList = null;
        return true;
    }

	public SupportsCloneMe cloneMe() {
		return new PhysicalCollectorPO(this);
	}

}

/**
 * @author  Marco Grawunder
 */
class NextInputReader extends Thread{
    
        /**
		 * @uml.property  name="port"
		 */
        int port = -1;
        /**
		 * @uml.property  name="po"
		 * @uml.associationEnd  multiplicity="(1 1)"
		 */
        TriggeredPlanOperator po;
        /**
		 * @uml.property  name="debug"
		 */
        boolean debug = false;
        
        /**
		 * @uml.property  name="logger"
		 * @uml.associationEnd  multiplicity="(1 1)"
		 */
        private Logger logger = Logger.getLogger(this.getClass().getName());
    
        public NextInputReader(TriggeredPlanOperator po, int port, String name){
            super(name);
            this.po = po;
            this.port = port;
        }
    
        public void run() {
            try {                
                Object obj = null;
                boolean readOn = true;
                while (readOn){
                    logger.debug(po.getPOName()+" Warte auf Daten auf Port "+port);
                    obj = po.getInputNext(port, po, -1);                                    
                    logger.debug(getName() + " " + port + " " + obj + " gelesen");
                    if (obj == null){
                        readOn = false;
                    }else{
                        po.putToBufferInternal(obj);
                        logger.debug(getName() + " " + port + " " + obj + " geschrieben");
                    }
                }
                logger.debug(po.getPOName()+" Lesen auf Port "+port+" beendet.");
                
            } catch (POException e) {
                // TODO!!!!
                e.printStackTrace();
            } catch (TimeoutException e) {
            // TODO!!!!
            e.printStackTrace();
        }
        }
    
    
}
