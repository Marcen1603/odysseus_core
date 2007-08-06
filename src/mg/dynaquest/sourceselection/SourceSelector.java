package mg.dynaquest.sourceselection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import mg.dynaquest.queryexecution.action.FilterAction;
import mg.dynaquest.queryexecution.event.POException;
import mg.dynaquest.queryexecution.po.base.FilterPO;
import mg.dynaquest.queryexecution.po.base.PhysicalCollectorPO;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryexecution.po.base.TriggeredPlanOperator;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdm.SourceDescriptionManager;
import mg.dynaquest.sourceselection.action.compensate.CompensateAction;
import mg.dynaquest.sourceselection.action.compensate.JoinCompensationJoin;
import mg.dynaquest.sourceselection.action.compensate.NecInAttrCompConstant;
import mg.dynaquest.sourceselection.action.compensate.NecInAttrCompInterval;
import mg.dynaquest.sourceselection.action.compensate.NecInAttrCompSource;
import mg.dynaquest.sourceselection.action.compensate.OperatorCompensation;
import mg.dynaquest.sourceselection.action.compensate.PredicateCompensation;
import mg.dynaquest.sourceselection.action.compensate.ProjectCompensation;
import mg.dynaquest.sourceselection.action.compensate.SelectJoinCompensationCluster;
import mg.dynaquest.sourceselection.action.compensate.SelectJoinCompensationGreedy;
import mg.dynaquest.sourceselection.action.compensate.SelectJoinCompensationSmartCluster;
import mg.dynaquest.sourceselection.action.compensate.SortCompensation;
import mg.dynaquest.sourceselection.action.filter.NecessaryInAttributesFilter;
import mg.dynaquest.sourceselection.action.filter.NecessaryOutAttributesFilter;
import mg.dynaquest.sourceselection.action.filter.OperatorCompatibiltyFilter;
import mg.dynaquest.sourceselection.action.filter.PredicatesRespectedFilter;
import mg.dynaquest.sourceselection.action.filter.ReturnAttributesFilter;
import mg.dynaquest.sourceselection.action.filter.SortOrderFilter;

import org.apache.log4j.Logger;

/**
 * 
 * @author Marco Grawunder
 *
 *	Testklasse, die einen Filtergraphen aufbaut
 *
 */

public class SourceSelector {
	/** Classeigener Debuger */
    protected static Logger logger = Logger.getLogger(SourceSelector.class.getName());

    /**
	 * @uml.property  name="planCache"
	 * @uml.associationEnd  qualifier="key:java.lang.Object mg.dynaquest.queryexecution.po.base.PhysicalCollectorPO"
	 */
    static Map<String, PhysicalCollectorPO> planCache = new HashMap<String, PhysicalCollectorPO>();
   

    static public PhysicalCollectorPO readXML(String filename){
        //TODO Init from XML
        return null;
    }
    
   static public void writeXML(PhysicalCollectorPO top, String filename) {
    	StringBuffer xmlRep = new StringBuffer();
        top.getXMLPlan(xmlRep);
        File file = new File(filename);
    
    	try 
    	{
    		FileWriter fw = new FileWriter( file, true);
    		fw.write(xmlRep.toString());
    		fw.close();
    	} 
    	catch (FileNotFoundException e){} 
    	catch (IOException e){}		
    }

    
    /** Sorgt dafür, das die CompensatePOs und FilterPOs ihre Actions mit der Anfrage initialisieren */
    static private void setQuery(PlanOperator nextPO, SDFQuery query)
    {           
        if (nextPO instanceof HasQuery){
            ((HasQuery)nextPO).setQuery(query);
        }

         // Und dann rekursiv für alle Kinder
         for (int i=0;i<nextPO.getNumberOfInputs();i++)
         { 
             setQuery(nextPO.getInputPO(i), query); 
         } 
    }

    public static List<AnnotatedPlan> makeSourceSection(SourceDescriptionManager sdm, SDFQuery query, boolean accessPlanDebug, String filename) throws POException, TimeoutException {
        PhysicalCollectorPO top =  planCache.get(filename);
        if (top == null){
            top = readXML(filename);
            planCache.put(filename,top);
        }
        setQuery(top, query);
        return makeSourceSection(sdm, query, top);
    }
    
    public static List<AnnotatedPlan> makeSourceSection(SourceDescriptionManager sdm, SDFQuery query) throws POException, TimeoutException {
        return makeSourceSection(sdm, query, createDefaultSelectionPlan(sdm, query));
    }
    
    /**
     * @param sdm
     * @param query
     * @throws POException
     * @throws TimeoutException
     */
    private static List<AnnotatedPlan> makeSourceSection(SourceDescriptionManager sdm, SDFQuery query, PhysicalCollectorPO top) throws POException, TimeoutException {
            List<AnnotatedPlan> plans = new ArrayList<AnnotatedPlan>();
            
            logger.debug("Starting Compensation Process at TOP");
            TriggeredPlanOperator dummy = new PhysicalCollectorPO();
            top.open(dummy);
            Object  retPlanObj = null;
            while ((retPlanObj = top.next(dummy, -1)) != null){
  //          	logger.debug("<br>[SourceSelector]> <b>Plan found ["+((AnnotatedPlan)retPlanObj).getIndex()+"]> GIP:"+((AnnotatedPlan)retPlanObj).getGlobalInputPattern()+" / GOP:"+((AnnotatedPlan)retPlanObj).getGlobalOutputPattern()+"</b><br>", 3);
                try{
                    AnnotatedPlan retPlan = (AnnotatedPlan) retPlanObj;
                    plans.add(retPlan);
                    //System.out.println("-->"+retPlan.getAccessPlan().hashCode());
                }catch(ClassCastException e){
                    e.printStackTrace();
                    System.out.println(retPlanObj.getClass());
                }
            }
            
            top.close(dummy);
            
            logger.debug(" ----------------------    TERMINATED  -------------------------");
           
            top.stopChildren(); 
            top.stop();
            return plans;
        }

    /**
     * @param sdm
     * @param query
     * @return
     */
    private static PhysicalCollectorPO createDefaultSelectionPlan(SourceDescriptionManager sdm, SDFQuery query) {
    	boolean useOperatorCompensation = false;
        SDMAccessPO accPO = new SDMAccessPO(query, sdm);
        //accPO.setDebug(accessPlanDebug);
        accPO.setPOName("SDMAccessPO");
        accPO.start();
        
        SDFtoAnnotatedPlanPO initPO = new SDFtoAnnotatedPlanPO(query);
        initPO.setPOName("SDFtoAnnotatedPlanPO");
        initPO.setInputPO(accPO);
        //initPO.setDebug(accessPlanDebug);
        initPO.start();
        				
        // -------------------------------------------------------
        // STUFE 1: Kompensation der notwendigen Ausgabeattribute
        //-------------------------------------------------------
   
        // Filter
        FilterAction filterRequiredOutputAction = new NecessaryOutAttributesFilter(query);
        FilterPO filterRequiredOutput = new FilterPO(filterRequiredOutputAction);
        filterRequiredOutput.setPOName("[REQUIRED OUPUT] - FilterPO");
        filterRequiredOutput.setInputPO(initPO);
        filterRequiredOutput.setDoErrorElementWriting(true);
        filterRequiredOutput.start();		
        
        // Kompensation:  JOIN
        CompensateAction compRequiredOutputAction1 = new JoinCompensationJoin(query);
        CompensatePO compRequiredOutput1 = new CompensatePO(compRequiredOutputAction1, true);
        compRequiredOutput1.setPOName("[JOIN] - CompensatePO");
        compRequiredOutput1.setInputPO(filterRequiredOutput);
        compRequiredOutput1.setDoErrorElementWriting(true);
        compRequiredOutput1.start();	
        
        
        // Kompensation: GREEDY
        CompensateAction compRequiredOutputAction2 = new SelectJoinCompensationGreedy(query);
        CompensatePO compRequiredOutput2 = new CompensatePO(compRequiredOutputAction2, true);
        compRequiredOutput2.setPOName("[GREEDY] - CompensatePO");
        compRequiredOutput2.setInputPO(compRequiredOutput1);
        compRequiredOutput2.setDoErrorElementWriting(true);
        compRequiredOutput2.start();	
   
        // Kompensation: CLUSTER
        CompensateAction compRequiredOutputAction3 = new SelectJoinCompensationCluster(query);
        CompensatePO compRequiredOutput3 = new CompensatePO(compRequiredOutputAction3, true);
        compRequiredOutput3.setPOName("[CLUSTER] - CompensatePO");
        compRequiredOutput3.setInputPO(compRequiredOutput2);
        compRequiredOutput3.setDoErrorElementWriting(true);
        compRequiredOutput3.start();	
        
        // Kompensation: SMART CLUSTER
        CompensateAction compRequiredOutputAction4 = new SelectJoinCompensationSmartCluster(query);
        CompensatePO compRequiredOutput4 = new CompensatePO(compRequiredOutputAction4, true);
        compRequiredOutput4.setPOName("[SMART CLUSTER] - CompensatePO");
        compRequiredOutput4.setInputPO(compRequiredOutput3);
        compRequiredOutput4.setDoErrorElementWriting(true);
        compRequiredOutput4.start();	
        
        // Terminator
        TerminatorPO termRequiredOutput = new TerminatorPO();
        termRequiredOutput.setInternalPOName("[REQUIRED OUTPUT] - Terminator");
        termRequiredOutput.setInputPO(compRequiredOutput4);
        termRequiredOutput.start();
        			
        // Collector
        PhysicalCollectorPO coll1 = new PhysicalCollectorPO();
        coll1.setPOName("[REQUIRED OUTPUT] - CollectorPO");
        coll1.setNoOfInputs(6);
        coll1.setInputPO(0,filterRequiredOutput);
        coll1.setInputPO(1,compRequiredOutput1);
        coll1.setInputPO(2,compRequiredOutput2);
        coll1.setInputPO(3,compRequiredOutput3);
        coll1.setInputPO(4,compRequiredOutput4);
        coll1.setInputPO(5,termRequiredOutput);
        coll1.start();		
        
   //	
   //			// -------------------------------------------------------
   //			// STUFE 2: Kompensation der notwendigen Eingabeattribute
   //			//-------------------------------------------------------
   //
        // Filter
        FilterAction filterRequiredInputAction = new NecessaryInAttributesFilter(query);
        FilterPO filterRequiredInput = new FilterPO(filterRequiredInputAction);
        filterRequiredInput.setPOName("[REQUIRED INPUT] - FilterPO");
        filterRequiredInput.setInputPO(coll1);
        filterRequiredInput.setDoErrorElementWriting(true);
        filterRequiredInput.start();
        
        // Kompensation: Value List
        CompensateAction compRequiredInputAction1 = new NecInAttrCompConstant(query);
        CompensatePO compRequiredInput1 = new CompensatePO(compRequiredInputAction1, false);
        compRequiredInput1.setPOName("[PRE CONSTANT] - CompensatePO");
        compRequiredInput1.setInputPO(filterRequiredInput);
        compRequiredInput1.setDoErrorElementWriting(true);
        compRequiredInput1.start();	
        
        
        // Kompensation: Intervall
        CompensateAction compRequiredInputAction2 = new NecInAttrCompInterval(query);
        CompensatePO compRequiredInput2 = new CompensatePO(compRequiredInputAction2, false);
        compRequiredInput2.setPOName("[PRE INTERVAL] - CompensatePO");
        compRequiredInput2.setInputPO(compRequiredInput1);
        compRequiredInput2.setDoErrorElementWriting(true);
        compRequiredInput2.start();	
        
        
        // Kompensation: Values by Source
        CompensateAction compRequiredInputAction3 = new NecInAttrCompSource(query);
        CompensatePO compRequiredInput3 = new CompensatePO(compRequiredInputAction3, false);
        compRequiredInput3.setPOName("[PRE SOURCE] - CompensatePO");
        compRequiredInput3.setInputPO(compRequiredInput2);
        compRequiredInput3.setDoErrorElementWriting(true);
        compRequiredInput3.start();	
        
        // Terminator
        TerminatorPO termRequiredInput = new TerminatorPO();
        termRequiredInput.setInternalPOName("[REQUIRED INPUT] - Terminator");
        termRequiredInput.setInputPO(compRequiredInput3);
        termRequiredInput.start();
        			
        // Collector
        PhysicalCollectorPO coll2 = new PhysicalCollectorPO();
        coll2.setPOName("[REQUIRED INPUT] - CollectorPO");
        coll2.setNoOfInputs(5);
        coll2.setInputPO(0,filterRequiredInput);
        coll2.setInputPO(1,compRequiredInput1);
        coll2.setInputPO(2,compRequiredInput2);
        coll2.setInputPO(3,compRequiredInput3);
        coll2.setInputPO(4,termRequiredInput);
        coll2.start();		
        
        // -------------------------------------------------------
        // STUFE 3: Kompensation der unberücksichtigten Prädikate
        //-------------------------------------------------------
   
        // Filter
        FilterAction filterSelectAction = new PredicatesRespectedFilter(query);
        FilterPO filterSelect = new FilterPO(filterSelectAction);
        filterSelect.setPOName("[SELECT] - FilterPO");
        filterSelect.setInputPO(coll2);
        filterSelect.setDoErrorElementWriting(true);
        filterSelect.start();
        
        // Kompensation
        CompensateAction compSelectAction = new PredicateCompensation(query);
        CompensatePO compSelect = new CompensatePO(compSelectAction, false);
        compSelect.setPOName("[SELECT] - CompensatePO");
        compSelect.setInputPO(filterSelect);
        compSelect.setDoErrorElementWriting(true);
        compSelect.start();	
        
        // Terminator
        TerminatorPO termSelect = new TerminatorPO();
        termSelect.setInternalPOName("[SELECT] - Terminator");
        termSelect.setInputPO(compSelect);
        termSelect.start();
        			
        // Collector
        PhysicalCollectorPO coll3 = new PhysicalCollectorPO();
        coll3.setPOName("[SELECT] - CollectorPO");
        coll3.setNoOfInputs(3);
        coll3.setInputPO(0,filterSelect);
        coll3.setInputPO(1,compSelect);
        coll3.setInputPO(2,termSelect);
        coll3.start();
        
        // -------------------------------------------------------
        // STUFE 4: Kompensation der inkompatibeln Operatoren in den Prädikaten der Anfrage
        //-------------------------------------------------------
                
        PhysicalCollectorPO coll4 = null;
        if (useOperatorCompensation){
	        //Filter
	        FilterAction filterOperatorCompatibilityAction = new OperatorCompatibiltyFilter(query);
	        FilterPO filterOperatorCompatibility = new FilterPO(filterOperatorCompatibilityAction);
	        filterOperatorCompatibility.setPOName("[Operator Compatibility] - FilterPO");
	        filterOperatorCompatibility.setInputPO(coll3);
	        filterOperatorCompatibility.setDoErrorElementWriting(true);
	        filterOperatorCompatibility.start();
	        
	        //Kompensation
	        CompensateAction compOperatorAction = new OperatorCompensation(query);
	        CompensatePO compOperator = new CompensatePO(compOperatorAction, false);
	        compOperator.setPOName("[Operator Compatibility] - CompensatePO");
	        compOperator.setInputPO(filterOperatorCompatibility);
	        compOperator.setDoErrorElementWriting(true);
	        compOperator.start();
	        
	        //Terminator
	        TerminatorPO termOperatorComp = new TerminatorPO();
	        termOperatorComp.setInternalPOName("[Operator Compatibility] - Terminator");
	        termOperatorComp.setInputPO(compOperator);
	        termOperatorComp.start();
	        			
	        // Collector
	        coll4 = new PhysicalCollectorPO();
	        coll4.setPOName("[Operator Compatibility] - CollectorPO");
	        coll4.setNoOfInputs(3);
	        coll4.setInputPO(0,filterOperatorCompatibility);
	        coll4.setInputPO(1,compOperator);
	        coll4.setInputPO(2,termOperatorComp);
	        coll4.start();
        }
   
        // -------------------------------------------------------
        // STUFE 5: Entfernen der unerwünschten Attribute aus dem Ausgabemuster
        //-------------------------------------------------------
        
        // Filter 
        FilterAction filterProjectAction = new ReturnAttributesFilter(query);
        FilterPO filterProject = new FilterPO(filterProjectAction);
        filterProject.setPOName("[PROJECT] - FilterPO");
        if (useOperatorCompensation){
        	filterProject.setInputPO(coll4);
        }else{
        	filterProject.setInputPO(coll3);
        }
        filterProject.setDoErrorElementWriting(true);
        filterProject.start();
        
        // Kompensation
        CompensateAction compProjectAction = new ProjectCompensation(query);
        CompensatePO compProject = new CompensatePO(compProjectAction, false);
        compProject.setPOName("[PROJECT] - CompensatePO");
        compProject.setInputPO(filterProject);
        compProject.setDoErrorElementWriting(true);
        compProject.start();	
        
        // Terminator
        TerminatorPO termProject = new TerminatorPO();
        termProject.setInternalPOName("[PROJECT] - Terminator");
        termProject.setInputPO(compProject);
        termProject.start();
        
        // Collector
        PhysicalCollectorPO coll5 = new PhysicalCollectorPO();
        coll5.setPOName("[PROJECT] - CollectorPO");
        coll5.setNoOfInputs(3);
        coll5.setInputPO(0,filterProject);
        coll5.setInputPO(1,compProject);
        coll5.setInputPO(2,termProject);
        coll5.start();
        
        // -------------------------------------------------------
        // STUFE 6: Sortierung
        //-------------------------------------------------------
        
        // Filter 
        FilterAction filterSortAction = new SortOrderFilter(query);
        FilterPO filterSort = new FilterPO(filterSortAction);
        filterSort.setPOName("[SORT] - FilterPO");
        filterSort.setInputPO(coll5);
        filterSort.setDoErrorElementWriting(true);
        filterSort.start();
        
        // Kompensation
        CompensateAction compSortAction = new SortCompensation(query);
        CompensatePO compSort = new CompensatePO(compSortAction, false);
        compSort.setPOName("[SORT] - CompensatePO");
        compSort.setInputPO(filterSort);
        compSort.setDoErrorElementWriting(true);
        compSort.start();	
        
        // Terminator
        TerminatorPO termSort = new TerminatorPO();
        termSort.setInternalPOName("[SORT] - Terminator");
        termSort.setInputPO(compSort);
        termSort.start();
        
        // Collector
        PhysicalCollectorPO coll6 = new PhysicalCollectorPO();
        coll6.setPOName("[PROJECT] - CollectorPO");
        coll6.setNoOfInputs(3);
        coll6.setInputPO(0,filterSort);
        coll6.setInputPO(1,compSort);
        coll6.setInputPO(2,termSort);
        coll6.start(); 
        
        // -------------------------------------------------------
        // TOP
        //-------------------------------------------------------
        PhysicalCollectorPO top = new PhysicalCollectorPO();
        top.setPOName("Top");
        top.setNoOfInputs(1);
        top.setInputPO(0, coll6);
   
   
        
        //top.setDebug(false);
        top.start();
        return top;
    }

    
}