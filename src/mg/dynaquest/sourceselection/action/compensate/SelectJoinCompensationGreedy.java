/*
 * Created on 17.11.2004
 *
 */
package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;

import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import mg.dynaquest.sourceselection.AnnotatedPlanQueue;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Author: Jurij Henne
 * Spezielle Ausprägung der SelectJoin-CompensateAction Cluster-Ansatz.
 * Es wir nur ein Plan generiert, welcher mit Hilfe eines Kostenmodells als optimal bewertet wird.
 */

public class SelectJoinCompensationGreedy extends SelectJoinCompensation 
{
    public SelectJoinCompensationGreedy() {}
	/**
	 * Initialisiert Greedy-Kompensation mit der Anfrage und eine spezifischen Namen(für Serialisierungszwecke)
	 * @param query Die Anfrage
	 */   
    public SelectJoinCompensationGreedy(SDFQuery query)
    {
    	this.M = 100;//1E300 * 1E20 ; // Unendlich?  alternativ 0.0/0.0
    	this.setQuery(query);
    }

    /* (non-Javadoc)
     * @see mg.dynaquest.sourceselection.action.CompensateAction#compensate(java.util.ArrayList)
     */
    public ArrayList <AnnotatedPlan> compensate (ArrayList <AnnotatedPlan> toCompensate) 
    {	  
    	 // toCompensate wird im Verlauf der Kompensation u.U. verkleinert
    	SDFAttributeList queryRequiredAttributes = getQuery().getRequiredAttributes();
    	SDFAttributeList predicateAttributes = new SDFAttributeList(getQuery().getPredicateAttributes());
    	ArrayList<AnnotatedPlan> compensatedPlans = new ArrayList<AnnotatedPlan>();
       	//this.context = new ArrayList<AnnotatedPlan>(toCompensate);
       	this.setErrorPlans(new ArrayList<AnnotatedPlan>(toCompensate));
    	
    	// Es wird hier der CHAIN-Algorithmus (GREEDY-Ansatz) umgesetzt
    	//1. Ausführungsschlange initiieren
    	AnnotatedPlanQueue executionQueue = getExecutionQueue(toCompensate, predicateAttributes);

    	logger.debug("> Plans in Queue: "+executionQueue.getQueueSize()+" > #Uncovered Plans: "+toCompensate.size());  
			
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<executionQueue.getQueueSize();i++) // NUR DEBUG
			sb.append(" "+(executionQueue.getPlan(i)).getIndex());
		logger.debug("> ExecutionQueue: {"+sb+" }");

    	if(!executionQueue.isEmpty())
    	{

	    	// Nun sollte eine executionQueue vorliegen die betroffenen Annotated in der richtigen Ausführungsreihefolge enthält
	    	// Daraus soll nun ein OP Baum konstruiert werden (mit Hilfe der SelectJoinPOs)
	    	// z.B wird so aus A -> B -> C 
	    	//
	    	//				CompensatedPlan
	    	//					 |
	    	// 				SelectJoin2
	    	//				/		\
	    	//		SelectJoin1		 Plan C
	    	//		/		\		
	    	// 	Plan A		Plan B
	    	//
	    	// Let's go ;)
	
	    	//Sicherlich ist auch eine rekursiv Lösung eleganter, aber nicht mehr so flexibel handhabbar wie eine echte Kette von Teilbäumen
	    	ArrayList <AnnotatedPlan> sJoinedPlans = new ArrayList<AnnotatedPlan>(); // hier sammeln wir die Annotated, die Ausgaben von SelectOps kapseln
	    	boolean planFailed = false;
	    	for(int i=0; i<executionQueue.getQueueSize()-1; i++)
	    	{
	    		if(i==0) // für die beiden ersten Pläne einfach nur SelectJoin anwenden
	    		{
	        		AnnotatedPlan masterPlan = (AnnotatedPlan)executionQueue.getPlan(i);
	    			AnnotatedPlan slavePlan = (AnnotatedPlan)executionQueue.getPlan(i+1); // überlaufgeschütz durch for()	
    				logger.debug("> Preparing for S-Join: MasterPlan ["+masterPlan.getIndex()+"]:GIP"+masterPlan.getGlobalInputPattern()+" / GOP"+masterPlan.getGlobalOutputPattern());
    				logger.debug("> Preparing for S-Join: SlavePlan ["+slavePlan.getIndex()+"]:GIP"+slavePlan.getGlobalInputPattern()+"/ GOP"+slavePlan.getGlobalOutputPattern());   				   			
	    			try
	    		    {
	    		    	AnnotatedPlan newPlan = getSelectJoinedPlan(masterPlan, slavePlan);
	    		    	sJoinedPlans.add(newPlan);
       		    		logger.debug("> SelectJoin successfull / NewPlan  ["+newPlan.getIndex()+"]("+masterPlan.getIndex()+"<->"+slavePlan.getIndex()+"):GIP"+newPlan.getGlobalInputPattern()+" / GOP"+newPlan.getGlobalOutputPattern());	    		    	
	    		    }
	    		    catch(Exception e)
	    		    {
	    		    	logger.debug(">  SelectJoin failed (["+masterPlan.getIndex()+"]<->["+slavePlan.getIndex()+"] )");
	       		    	//Wenn die Pläne innerhalb von executionQueue nicht verknüüft werden können, 
	       		    	//müssen	wir davon ausgehen , dass kein Plan existiert, daher
       		    		planFailed = true;
       		    		break; 
       		    		//	e.printStackTrace();
	    		    }
	    		}
	    		else // Ansonsten ist der Masterplan bereits ein Annotated aus der Ausgabe eines SelectOPs
	    		{
	        		AnnotatedPlan masterPlan = (AnnotatedPlan)sJoinedPlans.get(sJoinedPlans.size()-1);
	    			AnnotatedPlan slavePlan = (AnnotatedPlan)executionQueue.getPlan(i+1); // überlaufgeschütz durch for()   			 
    				logger.debug("> Preparing for S-Join: MasterPlan ["+masterPlan.getIndex()+"]:GIP"+masterPlan.getGlobalInputPattern()+" / GOP"+masterPlan.getGlobalOutputPattern());
    				logger.debug("> Preparing for S-Join: SlavePlan ["+slavePlan.getIndex()+"]:GIP"+slavePlan.getGlobalInputPattern()+"/ GOP"+slavePlan.getGlobalOutputPattern());   				   			
	    		    try
	    		    {
	    		    	AnnotatedPlan newPlan = getSelectJoinedPlan(masterPlan, slavePlan);
	    		    	sJoinedPlans.add(newPlan);
       		    		logger.debug("> SelectJoin successfull / NewPlan  ["+newPlan.getIndex()+"]("+masterPlan.getIndex()+"<->"+slavePlan.getIndex()+"):GIP"+newPlan.getGlobalInputPattern()+" /GOP"+newPlan.getGlobalOutputPattern());	    		    	
	    		    }
	    		    catch(Exception e)
	    		    {
	    		    	logger.debug(">  SelectJoin failed: (["+masterPlan.getIndex()+"]<->["+slavePlan.getIndex()+"] )");
	       		    	//Wenn die Pläne innerhalb von executionQueue nicht verknüüft werden können, 
	       		    	//müssen	wir davon ausgehen , dass kein Plan existiert, daher
       		    		planFailed = true;
       		    		break; 
       		    		//e.printStackTrace();
	    		    }
	    		}
	    		
	    		if(this.searchMinimalPlan) // ist die Verknüpfung überwacht, dann wird abgebrochen sobald Ausgabemuster abgedeckt ist
	    		{
	    			if(SDFAttributeList.subset(queryRequiredAttributes,predicateAttributes))
	    				break;
	    		}
	    		
	    		
	    	}
	    	if(!planFailed)
	    		compensatedPlans.add(sJoinedPlans.get(sJoinedPlans.size()-1)); // den GREEDY-Plan hinzufügen	(immer letzter)
    	}
    	
    	return compensatedPlans;    
    		
    	
    }
    /**
     * Versucht die Eingabepläne so zu ordnen, dass eine Plan-Ausführungsreihenfolge 
     * alle gewünschten Ausgabeattribute liefert.
     * @param toCompensate Menge der Eingabepläne. Nicht alle Pläne müssen verarbeitet werden.
     * @return Liste der Pläne die zu einem neuen Zugriffsplan verknüpft werden.
     */
    private AnnotatedPlanQueue getExecutionQueue(ArrayList<AnnotatedPlan> toCompensate, SDFAttributeList predicateAttributes)
    {
    	AnnotatedPlanQueue executionQueue = new AnnotatedPlanQueue();
    	//2. GO, CHAIN,  GO...
    	while(toCompensate.size()>0)   		
    	{
    		//Für jeden Annotated prüfen, ob es mit den gegebenen Belegungen(predicateAttributes) ausführbar ist)
    		AnnotatedPlan queuePlan = null;
    		AnnotatedPlan actPlan = null;
    		this.M = 100;  // Reset
    		// WICHTIG: je zwei unterschiedliche Annotated EINER Beschreibung(mehr als ein Zugrifsmuster) werden als 
    		// eigenständige Pläne betrachtet, so dass der fertige logische Plan unter Umständen entsprechend oft auf eine und
    		// dieselbe Quelle zugreifen wird(für jeden Zugrifsmuster einmal)
    		for(int i=0; i<toCompensate.size();i++)
    		{		
    			actPlan = (AnnotatedPlan)toCompensate.get(i); // Plan holen
    			SDFAttributeList inputAttributes = actPlan.getRequiredInAttributes(); // Required-Eingabeattribute von den Plan holen 
    			logger.debug("> ############# NEW LOOP ################");
    			logger.debug("> NextPlan ["+actPlan.getIndex()+"] / GIP:"+actPlan.getGlobalInputPattern()+" / GOP:"+actPlan.getGlobalOutputPattern());
    			 
    			//werden alle Required-Eingabeattribute von der Query abgedeckt
    			//und kein Plan mit derselben GOP und URI in der QUEUE vorhanden
     			if(SDFAttributeList.subset(inputAttributes,predicateAttributes) && !actPlan.inSetWithSameGOP(executionQueue.getQueue())) 
    			{  				  		   	  
     				// double costs = calcCosts(actPlan, query, context); // Kosten berechnen
    				double costs = 10;
    				if(costs<this.M) // nur wenn Kosten kleinergleich sind
    				{
    		    		for(int l=0; l<inputAttributes.getAttributeCount();l++)
    		    			logger.debug("> InputAttributes of Plan ["+actPlan.getIndex()+"]: "+inputAttributes.getAttribute(l) );  
    		    		
    		    		StringBuffer sb = new StringBuffer();
    		    		for(int l=0; l<predicateAttributes.getAttributeCount();l++)	 // NUR DEBUG
    		    			sb.append(" "+predicateAttributes.getAttribute(l));
    		    		logger.debug("> are covered with PredicateAttributes {"+sb+" }");
    		    		
    		    		M = costs;	//Kostenschranke anpassen
    					queuePlan = actPlan;
    					logger.debug("> Found Plan ["+queuePlan.getIndex()+"] / GIP:"+queuePlan.getGlobalInputPattern()+" / GOP:"+queuePlan.getGlobalOutputPattern());   
    	    			//break; // nur aktivieren, wenn erste gefunden PLan genommen werden soll
    				}
    			}
    		}
			if(queuePlan != null) // Wird ein Plan gefunden 
			{
				logger.debug("> PLAN to ADD ["+queuePlan.getIndex()+"]/ GIP:"+queuePlan.getGlobalInputPattern()+" / GOP:"+queuePlan.getGlobalOutputPattern());  
				executionQueue.addPlan(queuePlan); // Plan der Queue hinzufügen
				//die Output-Attribute des Plan in die Liste aufnehmen(keine Dubletten, darum kein addAttributes()
				predicateAttributes = SDFAttributeList.union(predicateAttributes, queuePlan.getGlobalOutputPattern().getAllAttributes());
				logger.debug("> Plancount in QUEUE: "+executionQueue.getQueueSize());  
				
				logger.debug("> Plans remanining : "+toCompensate.size()+" Preparing for cleanup.."); 
				logger.debug("> Removing Plan ["+queuePlan.getIndex()+"]/ GIP:"+ queuePlan.getGlobalInputPattern()+" / GOP:"+queuePlan.getGlobalOutputPattern());  
				toCompensate.remove(queuePlan); // und aus der toCompensate-Liste entfernen
				logger.debug("> Plancount after cleanup: "+toCompensate.size()); 
			}
			else  //es wurde kein Plan gefunden
			{
				toCompensate.clear();
			}
			 

		
    	}
		this.setErrorPlans(toCompensate); //TODO; Falsch, da identische Referenz
		// 1. Ersten Plöne notieren , die nicht in einen Plan einfliessen konnten
		// 2. sollte GOP nicht alle QueryReqAttributs abdecken, alle reinschreiben

		if(!executionQueue.isEmpty())
		{
			SDFAttributeList outputPattern =  getOutputPatternForQueue(executionQueue);
			SDFAttributeList queryRequiredAttr =  this.getQuery().getRequiredAttributes();
			if(!SDFAttributeList.subset(queryRequiredAttr,outputPattern)) // alle QueryAttribute im Plan-GOP?
				return new AnnotatedPlanQueue(); // Leere Queue zurückgeben
		}
    	
    	return executionQueue;
    }
    
    
    /**
     * Liefert die Menge der Ausgabeattribute für alle Pläne innerhalb einer Executuion Queue.
     * @param clusterSet Menge der Cluster
     * @return 
     */
    protected  SDFAttributeList getOutputPatternForQueue(AnnotatedPlanQueue queue)
    {
    	SDFAttributeList outputPattern = new SDFAttributeList();
		for(int i=0; i<queue.getQueueSize();i++) // Nun alle Pläne in Queue durchlaufen und....
		{	
    		AnnotatedPlan nextPlan = (AnnotatedPlan)queue.getPlan(i);
    		// GOPs als Attributmengen verknüpfen
    		outputPattern = SDFAttributeList.union(outputPattern, nextPlan.getGlobalOutputPattern().getAllAttributes());
		}
    	 
 		StringBuffer sb = new StringBuffer();
 		for(int l=0; l<outputPattern.getAttributeCount();l++)		 // NUR DEBUG
			sb.append(" "+outputPattern.getAttribute(l));
		logger.debug("> Covered GOP {"+sb+" }");
		
    	return outputPattern;
    }
    
    /*
     *  (non-Javadoc)
     * @see mg.dynaquest.sourceselection.action.CompensateAction#initInternalBaseValues(org.w3c.dom.NodeList)
     */
	public void initInternalBaseValues(NodeList children) 
	{
		for (int i = 0; i < children.getLength(); i++) 
		{
			Node cNode = children.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("costControlled")) 
				this.costControlled = (Integer.parseInt(cNode.getFirstChild().getNodeValue()) != 0);		
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("planCostLimit")) 
				this.M = Double.parseDouble(cNode.getFirstChild().getNodeValue());
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("searchMinimalPlan")) 
				this.searchMinimalPlan = (Integer.parseInt(cNode.getFirstChild().getNodeValue()) != 0);		

		}
	}
    
	/*
	 *  (non-Javadoc)
	 * @see mg.dynaquest.sourceselection.action.CompensateAction#getInternalXMLRepresentation(java.lang.String, java.lang.String, java.lang.StringBuffer)
	 */
    public  void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue) 
    {
    	xmlRetValue.append(baseIndent + "<costControlled>");
    	xmlRetValue.append((this.costControlled)?1:0);
    	xmlRetValue.append("</costControlled>\n");
    	
    	xmlRetValue.append(baseIndent + "<planCostLimit>");
    	xmlRetValue.append(this.M);
    	xmlRetValue.append("</planCostLimit>\n");
    	
    	xmlRetValue.append(baseIndent + "<searchMinimalPlan>");
    	xmlRetValue.append((this.searchMinimalPlan)?1:0);
    	xmlRetValue.append("</searchMinimalPlan>\n");
    }
    
    public static void main(String[] args) 
    {

    }
}