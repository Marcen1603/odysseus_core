package mg.dynaquest.sourceselection;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.factory.POFactory;
import mg.dynaquest.queryexecution.object.POElementBuffer;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.base.ReadsErrorElementsPO;
import mg.dynaquest.queryexecution.po.base.WritesErrorElements;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourceselection.action.compensate.CompensateAction;
import org.apache.log4j.Logger;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @autor  Jurij Henne  Kompensation-PO leistet durch die intergrierte CompensateAction die erforderliche   Verarbeitung der Eingabepläne
 */

public class CompensatePO extends ReadsErrorElementsPO implements
        WritesErrorElements, HasQuery {

    /**
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
    private Logger logger = Logger.getLogger(this.getClass().getName());
    
	/** Interne Name des PO. Wird beim Initialisieren überschrieben */
	//private String poName = "Unnamed CompensatePO"; 	
	/**
	 * CompensateAction leistet die eigentliche Kompensation
	 * @uml.property  name="compensateAction"
	 * @uml.associationEnd  
	 */
	private CompensateAction compensateAction;
	/** Menge der Pläne, die zunächst gesammelt und dann anschliessende an Action übergeben werden */
	private ArrayList <AnnotatedPlan> elements2Compensate = new ArrayList<AnnotatedPlan>();	
	/** Eigene Liste die ErrorElemente speichert /empfängt */
	private ArrayList <AnnotatedPlan> errorElements = new ArrayList<AnnotatedPlan>();	
	/** Pläne die von der Action als kompensiert zurück gegeben wurden */
	private ArrayList <AnnotatedPlan> compensatedPlans = new ArrayList<AnnotatedPlan>(); 
	/**
	 * Soll errorElementBuffer schreiben oder nicht
	 * @uml.property  name="doErrorElementWriting"
	 */
	private boolean doErrorElementWriting = false; 
	/**
	 * Ob auch bereits kompensierte Pläne(nicht neu generierte)  an den nachfolgenden PO weitergeleitet werden
	 * @uml.property  name="writeCompensatedToErrorBuffer"
	 */
	private boolean writeCompensatedToErrorBuffer = true; 
	/**
	 * Schreibt AnnotatedPlans zum nächsten CompensatePO raus
	 * @uml.property  name="errorElementBuffer"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	private POElementBuffer errorElementBuffer = null; 
	/**
	 * Mengenorientierte oder Satzorientierte Verarbeitung.
	 * @uml.property  name="setOriented"
	 */
	private boolean setOriented = false;
	/**
	 * Indiziert ob Kompensation stattgefunden hat
	 * @uml.property  name="compensationDone"
	 */
	private boolean compensationDone = false;

	/** Erstellt Instanz eines neuen CompensatePO mit der entsprecheden CompesateAction
	 * @param compensateAction 
	 */
	public CompensatePO(CompensateAction compensateAction, boolean setOriented) 
	{
		int maxElementsInErrorBuffer = 100;  // pi*daumen ?
		this.errorElementBuffer = new POElementBuffer(maxElementsInErrorBuffer);
		this.setOriented = setOriented;
		setCompensateAction(compensateAction);
		
	}
	
	/** Erstellt Instanz eines neuen CompensatePO mit der entsprecheden CompesateAction
	 * @param compensateAction 
	 */
	public CompensatePO() 
	{
		int maxElementsInErrorBuffer = 100;  // pi*daumen ?
		this.errorElementBuffer = new POElementBuffer(maxElementsInErrorBuffer);
	
	}

	
	/**
	 * Initialisiert den PO mit einer neuen Action
	 * @uml.property  name="compensateAction"
	 */
	public void setCompensateAction(CompensateAction compensateAction) 
	{
		this.compensateAction = compensateAction;
	}
	
	/** Liefert den internen PO-Namen */
    public String getInternalPOName() 
    {
        return "Unnamed CompensatePO";
    }

    

	/**
	 * @return  the doErrorElementWriting
	 * @uml.property  name="doErrorElementWriting"
	 */
	public boolean isDoErrorElementWriting() 
	{
		return doErrorElementWriting;
	}

	/**
	 * @param doErrorElementWriting  the doErrorElementWriting to set
	 * @uml.property  name="doErrorElementWriting"
	 */
	public void setDoErrorElementWriting(boolean state) 
	{
		this.doErrorElementWriting = state;
	}
	
	/**
	 * @param writeCompensatedToErrorBuffer  the writeCompensatedToErrorBuffer to set
	 * @uml.property  name="writeCompensatedToErrorBuffer"
	 */
	public void setWriteCompensatedToErrorBuffer(boolean state) 
	{
		this.writeCompensatedToErrorBuffer = state;
	}


	/** Schreibt ein Objekt in den Fehler-Puffer (wird von nachfolgenden CompensatePOs der selben Kompensationskette gelesen */
	protected void putToErrorBuffer(Object element) 
	{
		errorElementBuffer.put(element);
	}

	
	protected Object getFromErrorBuffer() {
        Object ret = null;
        try {
            ret = errorElementBuffer.get(-1);
        } catch (TimeoutException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return ret;
	}

	/** Holt das nächste Element(Plan) aus der Fehler-Puffer */
	public Object nextErrorElement() throws POException {
		Object obj = getFromErrorBuffer();
		return obj;
	}
    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_open()
     */
    protected boolean process_open() throws POException {
        // TODO Auto-generated method stub
        return true; 
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_next()
     */
    protected Object process_next() throws POException {
    	Object obj = null;
    	if(this.setOriented) // wenn Mengenorientierte Verarbeitung dann alle Errorpläne sammeln
    	{
            // ACHTUNG! Wenn alle Daten gelesen worden sind und dann wieder Daten angefordert werden
            // blockiert die Verarbeitung!! Ist ja nichts drin (auch kein Ende-Zeichen)
            // evtl. mal in getInputNext anpassen?
	    	while (!this.compensationDone && (obj = this.getInputNext(this,-1)) != null)
	    	{
				this.errorElements.add((AnnotatedPlan)obj);		
				this.logger.debug(this.getPOName()+"> Reading ErrorBuffer [S-O][1] > Counting ErrorElements...: "+errorElements.size());
	    	}
	    	
			if (!errorElements.isEmpty() && !this.compensationDone){
				this.elements2Compensate = new ArrayList <AnnotatedPlan> ();
				this.elements2Compensate.addAll(errorElements);  // Hack, gegen die identische Referenz
                this.compensatePlans();  // Pläne kompensieren
                this.writeErrorPlans(); // ErrorPlans an die Nachfolger leiten               
            }
            
		    if(!compensatedPlans.isEmpty()){
		        //this.logger.debug(this.getPOName()+"> Kompensation finisched [4] > Compensated Plans :"+this.compensatedPlans.size(),3);
                obj = this.compensatedPlans.get((this.compensatedPlans.size()-1));
                this.compensatedPlans.remove((this.compensatedPlans.size()-1));
               // this.logger.debug(this.getPOName()+"> Kompensation finisched [4] > Writing to Collector... Plan ["+((AnnotatedPlan)obj).getIndex()+"] / GIP:"+((AnnotatedPlan)obj).getGlobalInputPattern()+"/ GOP:"+((AnnotatedPlan)obj).getGlobalOutputPattern() ,3);
            }	
			
//			else if(obj== null &&  this.compensatedPlans.isEmpty())
//			{
//				logger.debug(this.getPOName()+"> ErrorBuffer leer [3] > Keine Pläne zu kompensieren: "+elements2Compensate.size(),2);	
//				// Wenn obj null dann am Ende zurückwerfen, damit auch in ErrorBuffer geschrieben wird !!
//				if (doErrorElementWriting) 
//				{
//					logger.debug(this.getPOName()+"> ErrorBuffer leer [3a] > Schreibe in den ErrorBuffer: null ",3);	
//					this.putToErrorBuffer(obj);
//				}				
//			}
    	}
    	else // Satzorientierte Verarbeitung (alle Stufen ab 2)
    	{
	    	while((obj = this.getInputNext(this,-1)) != null)
	    	{
    			this.errorElements = new ArrayList <AnnotatedPlan>(); //Liste immer reseten, da nur ein Plan darin vorkommen muss
				this.errorElements.add((AnnotatedPlan)obj);		
				this.logger.debug(this.getPOName()+"> Reading ErrorBuffer.. [T-O][1] > Counting ErrorElements...: "+errorElements.size());
	    	
				this.elements2Compensate = new ArrayList <AnnotatedPlan> ();
				this.elements2Compensate.addAll(errorElements);  // Hack, gegen die identische Referenz
				this.compensatePlans();  // Pläne kompensieren
				this.writeErrorPlans(); // ErrorPlans an die Nachfolger leiten
				
				if(!compensatedPlans.isEmpty())
	            {
	                obj = this.compensatedPlans.get((this.compensatedPlans.size()-1));
	                this.compensatedPlans = new  ArrayList <AnnotatedPlan>();
	              //  this.logger.debug(this.getPOName()+"> Compensation finisched [4] > Writing to Collector... Plan ["+((AnnotatedPlan)obj).getIndex()+"] / GIP: "+((AnnotatedPlan)obj).getGlobalInputPattern()+" / GOP: "+((AnnotatedPlan)obj).getGlobalOutputPattern() ,3);
	                break;
	            }		
	    	}  	    	
    	}
       // this.logger.debug(this.getPOName()+"> Kompensation abgeschlossen [8] > Schreibe zum Collector ["+((AnnotatedPlan)obj).getIndex()+"]: "+((AnnotatedPlan)obj).getGlobalOutputPattern() ,3);

        // Eine abschließende Null schreiben, falls in den Fehlerstrom geschrieben wird
        if (obj == null && doErrorElementWriting){
            this.putToErrorBuffer(obj);
        }
    	return obj;	
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#process_close()
     */
    protected boolean process_close() throws POException {
        // TODO Auto-generated method stub
        return true;
    }
    
    
    private void compensatePlans()
    {
		this.logger.debug(this.getPOName()+"> ErrorBuffer is EMPTY [2] > Counting ErrorElements...: "+elements2Compensate.size());
	    this.compensatedPlans = this.compensateAction.compensate(elements2Compensate);
		this.compensationDone = true;
		this.logger.debug(this.getPOName()+"> Compensation finished [2a] > Counting compensated Plans..: "+compensatedPlans.size());					
    }
    
    /**
     * Schreibt ErrorPläne weiter zum nächsten Planoperator
     * Wichtig: Bei der Satzorientierten Verarbeitung (alle Stufen ausser REQUIRED OUTPUT) 
     * MUSS die CompensateAction die errorList UNBEDUNGT(!) selbst festlegen, also bestimmen, welche
     * PLäne zum nachgeschalteten Planoperator derselben Stufe rausgeschrieben werden sollen.
     * Diesem Ansatz liegen folgende Überlegung zugrunde: Es kann passieren, dass bestimmte Attribute 
     * nach und nach von unterschiedlichen POs kompensiert werden können 
     * (Beispiel REQUIRED-INPUT: Ein Attribut durch die ConstantList, ein Weiteres durch ConstantInterval)
     * 
     *
     */
    private void writeErrorPlans()
    {
		this.logger.debug(this.getPOName()+"> in writeErrorPlans >  ( "+doErrorElementWriting+"/"+writeCompensatedToErrorBuffer+" )");	

    	if (this.doErrorElementWriting)
    	{
    		if(this.writeCompensatedToErrorBuffer && this.setOriented) // alle Errorpläne weiterleiten
    		{
    			for(int i=0;i<this.errorElements.size();i++)
    			{
    				this.putToErrorBuffer(this.errorElements.get(i));
    			//	this.logger.debug(this.getPOName()+"> Compensation finished [2a] > Writing to ErrorBuffer [ALL] : Plan ["+((AnnotatedPlan)errorElements.get(i)).getIndex()+"] / GIP: "+((AnnotatedPlan)errorElements.get(i)).getGlobalInputPattern()+"/ GOP:"+((AnnotatedPlan)errorElements.get(i)).getGlobalOutputPattern() ,3);	
    			}
    		}
    		else // nur die, die von der CompensateAction nicht verarbeitet werden konnten (TODO)
    		{
    			ArrayList <AnnotatedPlan> errorPlans = this.compensateAction.getErrorPlans();
    			for(int i=0;i<errorPlans.size();i++)
    			{
    		//		this.logger.debug(this.getPOName()+"> Compensation finished [2a] > Writing to ErrorBuffer [ERR only]: PLan ["+((AnnotatedPlan)elements2Compensate.get(i)).getIndex()+"] / GIP: "+((AnnotatedPlan)elements2Compensate.get(i)).getGlobalInputPattern()+"/ GOP:"+((AnnotatedPlan)elements2Compensate.get(i)).getGlobalOutputPattern() ,3);	
    				this.putToErrorBuffer(errorPlans.get(i));
    			}
    		}
    		//this.putToErrorBuffer(null); // Abschliessende NULL reinschreiben 		
    	}

    }

    /*
     *  (non-Javadoc)
     * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#initInternalBaseValues(org.w3c.dom.NodeList)
     */
	protected void initInternalBaseValues(NodeList children) {

		//int compareAttributesFound = 0;  // Keine Ahnung woher kommt und wozu^^

		for (int i = 0; i < children.getLength(); i++) 
		{
			Node cNode = children.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("doErrorElementWriting")) 
			{

				this.doErrorElementWriting = (Integer.parseInt(cNode.getFirstChild().getNodeValue())!=0);
			}
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("writeCompensatedToErrorBuffer")) 
			{
				this.writeCompensatedToErrorBuffer  = (Integer.parseInt(cNode.getFirstChild().getNodeValue()) != 0);
			}
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("setOriented")) 
			{
				this.setOriented  = (Integer.parseInt(cNode.getFirstChild().getNodeValue()) != 0);
			}
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("compensateAction")) 
			{
				NamedNodeMap attribs = cNode.getAttributes();
//				String id = attribs.getNamedItem("id").getNodeValue();
				String c_class = attribs.getNamedItem("class").getNodeValue();
				
				this.compensateAction = POFactory.createCompensateAction(c_class);
				this.compensateAction.initInternalBaseValues(cNode.getChildNodes());
			}		
		}
	}

	/*
	 *  (non-Javadoc)
	 * @see mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator#getInternalXMLRepresentation(java.lang.String, java.lang.String, java.lang.StringBuffer)
	 */
	protected void getInternalXMLRepresentation(String baseIndent,
			String indent, StringBuffer retBuffer) {

			retBuffer.append(baseIndent + "<doErrorElementWriting>");
			retBuffer.append((this.doErrorElementWriting)?1:0);
			retBuffer.append("</doErrorElementWriting>\n");
			
			retBuffer.append(baseIndent + "<writeCompensatedToErrorBuffer>");
			retBuffer.append((this.writeCompensatedToErrorBuffer)?1:0);
			retBuffer.append("</writeCompensatedToErrorBuffer>\n");
			
			retBuffer.append(baseIndent + "<setOriented>");
			retBuffer.append((this.setOriented)?1:0);
			retBuffer.append("</setOriented>\n");

			retBuffer.append(baseIndent + "<compensateAction  id='"
					+ this.compensateAction.hashCode() + "' class='"+this.compensateAction.getClass().getName()+"'>\n");
			compensateAction.getInternalXMLRepresentation(baseIndent+indent,indent,retBuffer);
			retBuffer.append(baseIndent + "</compensateAction>\n");

	}
	
	public void setQuery(SDFQuery query)
	{
		this.compensateAction.setQuery(query);
	}

	

    public static void main(String[] args) {
    }

    
	public SupportsCloneMe cloneMe() {
		// KompensatePO braucht nicht geklont werden 
		throw new NotImplementedException();
	}


}
