package mg.dynaquest.sourceselection.action.compensate;

/**
 * @author  Jurij Henne 
 * 
 * Die CompensateAction versucht die Menge der Eingabepläne (Teilziele) so miteinander zu verknüpfen, dass ein Plan ensteht,
 * der alle gewünschten Ausgabeattribute zurückliefert.
 * 
 * Umsetzung des CLUSTERING-Ansatzes, erweitert um einige weitere Features:
 * 1. Permutation der Pläne innerhalb der Cluster, wenn kein Kostenmodell vorliegt oder abgeschaltet ist.
 * 2. Optionaler Abbruch der Verarbeitung, sobald alle Teilziele gefunden wurden, die alle notwendigen Ausgabeattribute
 * 	  zurückliefern.
 * 3. Sollte eine Quelle mehrere AnnotatedPlans mit gleichem GOP(GlobalOutputPattern) aufweisen, kann jeweils nur ein Plan
 *    in der Verarbeitung berücksichtigt werden, da eine erneute Ausführung auf der Quelle bswp. mit anderem GIP keine neuen 
 *    Ergebnisse mitbringt und daher sinnlos und ineffizient ist. 
 * 3a.Liefert Grundlage für weitere Optimierung, indem bswp. generell solche Teilziele aus der Verarbeitung 
 * 	  ausgeschlossen werden(in den Ausführungsplan NICHT integriert), die keine neuen Ausgabeattribute liefern, als die 
 * 	  von den bisher in den Aushführungsplan eingebundenen Teilzielen schon bereits geliefert wurden.
 */

import java.util.ArrayList;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import mg.dynaquest.sourceselection.AnnotatedPlanQueue;
import mg.dynaquest.support.Permutation;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SelectJoinCompensationCluster extends SelectJoinCompensation  {
	
	/**
	 *  Nur für interne Überprüfung im Rahmen des "Avoid Same URI & GOP"-Checks
	 *  @see mg.dynaquest.sourceselection.AnnotatedPlan#inSetWithSameGOP() und
	 *  @see mg.dynaquest.sourceselection.AnnotatedPlan#hasSameGOP()
	 */
	protected ArrayList <AnnotatedPlan> clusteredPlans = new ArrayList<AnnotatedPlan>(); 
	
    public SelectJoinCompensationCluster(){}

    public SelectJoinCompensationCluster(SDFQuery query)
    {
    	this.M = 100;//1E300 * 1E20 ; // Unendlich?  alternativ 0.0/0.0
    	this.setQuery(query);
    }
  	
	/**
	 *  Umsetzung des PARTITION-Algoritmus (CLUSTER-Aproach).
	 *  Phase 2 ist angepasst, indem die Generierung der Pläne  durch optionale Berücksichtigung eines
	 *  Kostenmodels steuerbar ist. Ist kein Kostenmodell vorhanden oder dieses durch this.costControlled 
	 *  abgeschaltet ist, werden alle möglichen (GÜLTIGEN) Ausführungspläne generiert und zurückgeliefert.
	 *  Dadurch wird bswp. die Möglichkeit gegeben, die Pläne im Rahmen der Optimierungsphase zu bewerten.
	 */
    public ArrayList <AnnotatedPlan> compensate (ArrayList <AnnotatedPlan> toCompensate) 
    {	    
    	SDFAttributeList queryRequiredAttributes = new SDFAttributeList(getQuery().getRequiredAttributes());
    	SDFAttributeList predicateAttributes = new SDFAttributeList(getQuery().getPredicateAttributes());
    	ArrayList<AnnotatedPlan> compensatedPlans = new ArrayList<AnnotatedPlan>();
       	//this.context = new ArrayList<AnnotatedPlan>(toCompensate);
       	this.setErrorPlans(new ArrayList<AnnotatedPlan>(toCompensate));
   	 	
    	//1. Clustermenge initialiesen	
    	ArrayList <ArrayList> clusterSet = this.buildClusters(toCompensate, predicateAttributes);
    	//2. GO, Partition,  GO... PHASE 1 => Bilden von Clustern

    	logger.debug("> CLUSTER-COUNT: "+clusterSet.size());  
		 for(int j=0; j<clusterSet.size(); j++) // NUR DEBUG
		 {
			 ArrayList oneSet = clusterSet.get(j);
			 
	     		StringBuffer sb = new StringBuffer();
	     		for(int l=0; l<oneSet.size(); l++) 	 // NUR DEBUG
	    			sb.append(" "+((AnnotatedPlan)(oneSet.get(l))).getIndex());
	    		logger.debug("> CLUSTER "+(j+1)+" : {"+sb+" }");

		 }
    	//3. GO, Partition,  GO... PHASE 2 => Bilden von PLAN-QUEUEs => Ausführungssequenzen, zunächst absolut logisch
    	ArrayList <AnnotatedPlanQueue> executionQueues = getAnnotatedPlanQueues(clusterSet);
    	if(executionQueues.size()>0) // eigentlich überflüssig, da in Phase 1 abgefangen wird, aber doppelt hält besser
    	{
    		logger.debug("> AnnotatedPlanQueue IN executionQueue: "+executionQueues.size());  	
    		//one:
    		for(int i=0; i<executionQueues.size(); i++) // alle  AnnotatedPlanQueue durchlaufen, für jedes wir ein Zugriffsplan erstellt
    		{
        		logger.debug("> <<<<<<<<<<<<<<<<<<<<<<E-QUEUE: "+(i+1)+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" );  	

    			AnnotatedPlanQueue  nextQueue = executionQueues.get(i);
    			if(nextQueue.getQueueSize()>1) // nur ab 2 hat sinn
    			{
    		    	// Nun sollte eine AnnotatedPlanQueue vorliegen die betroffenen Annotated in der richtigen Ausführungsreihefolge enthält
    		    	// Daraus soll nun ein OP Baum konstruiert werden (mit Hilfe der SelectJoinPOs)
    		    	// z.B wird so aus A -> B -> C 
    		    	//
    		    	//				CompensatedPlan
    		    	//					 |
    		    	// 				SelectJoin2
    		    	//				/		\
    		    	//		Join/SelectJoin1		 Plan C
    		    	//		/		\		
    		    	// 	Plan A		Plan B
    		    	//
    		    	// Let's go ;)
    		    	//Sicherlich ist auch eine rekursiv Lösung eleganter, aber nicht mehr so flexibel handhabbar wie eine echte Kette von Teilbäumen	    	
    				ArrayList <AnnotatedPlan> sJoinedPlans = new ArrayList<AnnotatedPlan>(); // hier sammeln wir die Annotated, die Ausgaben von SelectOps kapseln
    		    	boolean planFailed = false; // Auch hier: sollte einer der selecJoins versagen, so ist die aktuelle AnnotatedPlanQueue zu verwerfen
    		    	two:
    		    	for(int j=0; j<nextQueue.getQueueSize()-1; j++)
    		    	{
    		    		AnnotatedPlan newPlan = null;
    		    		if(j==0) // für die beiden ersten Pläne einfach nur SelectJoin anwenden
    		    		{
    		        		AnnotatedPlan masterPlan = nextQueue.getPlan(j);
    		    			AnnotatedPlan slavePlan = nextQueue.getPlan(j+1); // überlaufgeschütz durch for()	
    	    				logger.debug(">  Preparing for S-Join:  MasterPlan ["+masterPlan.getIndex()+"]:GIP"+masterPlan.getGlobalInputPattern()+" / GOP"+masterPlan.getGlobalOutputPattern()+" ####");
    	    				logger.debug(">  Preparing for S-Join: SlavePlan ["+slavePlan.getIndex()+"]:GIP"+slavePlan.getGlobalInputPattern()+"/ GOP"+slavePlan.getGlobalOutputPattern()+" ####");   				   			
    	    				try
    		    		    {
    		    		    	newPlan = getSelectJoinedPlan(masterPlan, slavePlan);
    		    		    	sJoinedPlans.add(newPlan);
    	       		    		logger.debug("> SelectJoin successfull > NewPlan  ["+newPlan.getIndex()+"]("+masterPlan.getIndex()+"<->"+slavePlan.getIndex()+"):GIP"+newPlan.getGlobalInputPattern()+" /GOP"+newPlan.getGlobalOutputPattern());       	       		    	     		    		    	
    		    		    }
    		    		    catch(Exception e)
    		    		    {
    	       		    		logger.debug(">  SelectJoin failed ("+masterPlan.getIndex()+"<->"+slavePlan.getIndex()+") ####");
    		       		    	//Wenn die Pläne innerhalb von executionQueue nicht verknüüft werden können, 
    		       		    	//müssen	wir davon ausgehen , dass kein Plan existiert, daher
    	       		    		planFailed = true;
    	       		    		break two; 
    	       		    		//	e.printStackTrace();
    		    		    }
    		    		}
    		    		else // Ansonsten ist der Masterplan bereits ein Annotated aus der Ausgabe eines SelectOPs
    		    		{
    		        		//AnnotatedPlan masterMaster = sJoinedPlans.get(j-1);
    		        		AnnotatedPlan masterPlan = sJoinedPlans.get(sJoinedPlans.size()-1); // letzten SelectJoinPLan rausholen
    		    			AnnotatedPlan slavePlan = nextQueue.getPlan(j+1); // überlaufgeschütz durch for()   			 
    	    				logger.debug(">  Preparing for S-Join: MasterPlan ["+masterPlan.getIndex()+"]:GIP"+masterPlan.getGlobalInputPattern()+" / GOP"+masterPlan.getGlobalOutputPattern()+" ####");
    	    				logger.debug("> Preparing for S-Join: SlavePlan ["+slavePlan.getIndex()+"]:GIP"+slavePlan.getGlobalInputPattern()+"/ GOP"+slavePlan.getGlobalOutputPattern()+" ####");   				   			
    		    		    try
    		    		    {
    		    		    	newPlan = getSelectJoinedPlan(masterPlan, slavePlan);
    		    		    	sJoinedPlans.add(newPlan);
    	       		    		logger.debug("> SelectJoin successfull > NewPlan  ["+newPlan.getIndex()+"]("+masterPlan.getIndex()+"<->"+slavePlan.getIndex()+"):GIP"+newPlan.getGlobalInputPattern()+" /GOP"+newPlan.getGlobalOutputPattern());
    		    		    	
    		    		    }
    		    		    catch(Exception e)
    		    		    {
    	       		    		logger.debug(">  SelectJoin failed ("+masterPlan.getIndex()+"<->"+slavePlan.getIndex()+") ####");
    		       		    	//Wenn die Pläne innerhalb von executionQueue nicht verknüüft werden können, 
    		       		    	//müssen	wir davon ausgehen , dass kein Plan existiert, daher
    	       		    		planFailed = true;
    	       		    		break two; 
    	       		    		//	e.printStackTrace();
    		    		    }
    		    		}
    		    		
    		    		if(this.searchMinimalPlan) // ist die Verknüpfung überwacht, dann wird abgebrochen sobald Ausgabemuster abgedeckt ist
    		    		{
    		    			if(SDFAttributeList.subset(queryRequiredAttributes,newPlan.getGlobalOutputPattern().getAllAttributes()))
    		    				break two;
    		    		}    		
    		    	} // two:
		    	if(!planFailed)
		    		compensatedPlans.add(sJoinedPlans.get(sJoinedPlans.size()-1)); // den GREEDY-Plan hinzufügen	(immer letzter)				
    			}
    		}// one:
    	} 
    	return compensatedPlans;
    }
    
	/**
	 * Teilt die Menge der Eingabepläne in Cluster => Phase 1 des PARTITION-Algorithmus (DA, Abbildung 2.11)
	 * @param toCompensate
	 * @return
	 */
    protected ArrayList <ArrayList> buildClusters(ArrayList <AnnotatedPlan> toCompensate, SDFAttributeList predicateAttributes)
    {
    	ArrayList <ArrayList> clusterSet = new ArrayList<ArrayList>(); 
    	while(toCompensate.size()>0)   		
    	{
    		ArrayList <AnnotatedPlan> newCluster = new ArrayList<AnnotatedPlan>(); // Aktuelle Cluster
    		// WICHTIG: je zwei unterschiedliche Annotated EINER Beschreibung(mehr als ein Zugrifsmuster) werden als 
    		// eigenständige Pläne betrachtet, so dass der fertige logische Plan unter Umständen entsprechend soviele Male auf eine und
    		// dieselbe Quelle zugreifen wird, für jedes Zugrifsmuster einmal
    		for(int i=0; i<toCompensate.size();i++)
    		{		
    			AnnotatedPlan actPlan = (AnnotatedPlan)toCompensate.get(i); // Plan holen
    			SDFAttributeList inputAttributes = actPlan.getRequiredInAttributes(); // Required-Eingabeattribute von den Plan holen 
    			logger.debug("> ############################ NEW LOOP #############################################");     			
    			logger.debug("> NextPlan ["+actPlan.getIndex()+"]/ GIP: "+actPlan.getGlobalInputPattern()+"/ GOP: "+actPlan.getGlobalOutputPattern());   
     			if(actPlan.inSetWithSameGOP(clusteredPlans))
        			logger.debug("> Plan ["+actPlan.getIndex()+"] SAME GOP => KICKED");   
     			     				
    			if(SDFAttributeList.subset(inputAttributes,predicateAttributes) &&  !actPlan.inSetWithSameGOP(clusteredPlans)) // werden alle Required-Eingabeattribute von der Query abgedeckt
    			{  				  		   	  		    	 
		     		StringBuffer sb = new StringBuffer();
		     		for(int l=0; l<predicateAttributes.getAttributeCount(); l++) 	 // NUR DEBUG
		    			sb.append(" "+predicateAttributes.getAttribute(l));
		    		logger.debug("> InputAttributes of Plan ["+actPlan.getIndex()+"] GIP:"+actPlan.getGlobalInputPattern()+" are covered with PredicateAttributes {"+sb+" }");
  		    	
		    		newCluster.add(actPlan);
		    		this.clusteredPlans.add(actPlan);
    		    	logger.debug("> Adding Plan to Cluster: ["+actPlan.getIndex()+"] GIP:"+actPlan.getGlobalInputPattern());   
    			}
    		}
    		// Cluster gebildet => Analyse
			if(newCluster != null &&  !newCluster.isEmpty())    // aktueller Cluster nicht leer
			{
				clusterSet.add(newCluster); // Cluster in die Menge aufnehmen
				logger.debug(">  PlanCount in NewCluster: "+newCluster.size()); 
	    		for(int i=0; i<newCluster.size();i++) // Nun alle Annotated im Cluster durchlaufen und....
	    		{	
	    			logger.debug(">  Plans remanining: "+toCompensate.size()+"Preparing for cleanup.."); 
	    			//...die Output-Attribute des Plan in die Liste aufnehmen(keine Dubletten, darum kein addAttributes()
	    			AnnotatedPlan nextPlan = (AnnotatedPlan)newCluster.get(i); // Plan holen
	    			logger.debug("> Removing Plan ["+nextPlan.getIndex()+"]/ GIP: "+nextPlan.getGlobalInputPattern()); 
	    			// Ausgabeattribute des Plans der Menge der "bestückten" Eingabeattribute (predicateAttributes) hinzufügen
	    			predicateAttributes = SDFAttributeList.union(predicateAttributes, nextPlan.getGlobalOutputPattern().getAllAttributes());
	    	   	    toCompensate.remove(nextPlan); // und aus der toCompensate-Liste entfernen
	    	   	    logger.debug("> Plancount after cleanup: "+toCompensate.size()); 
	    		}		
			}
			else  // aktuelle Cluster nicht leer
			{
				logger.debug("> CLUSTER NULL" ); 
				if(clusterSet.isEmpty()) // Bisher überhaupt keine Cluster gebildet, kann man nichts machen
					return new ArrayList<ArrayList>(); // leere Liste der kompensierten Päne zurück
				// sonst  überprüfen ob alle erforderlichen Attribute zurückgeliefert werden
				// TODO: Also ob da wirklich nichts kompensiert werden soll, ist zu überlegen, denn
				// alternativ könnte man die NICHT vollständig kompensierten pläne den toCompensate hinzufügen
				SDFAttributeList outputPattern =  getOutputPatternForClusterSet(clusterSet);
				SDFAttributeList queryRequiredAttr =  getQuery().getRequiredAttributes();
				if(!SDFAttributeList.subset(queryRequiredAttr,outputPattern)) // alle QueryAttribute im Plan-GOP?
				{
	    			logger.debug("> outputPattern "+outputPattern.getAttributeCount());
	    			logger.debug("> queryRequiredAttr "+queryRequiredAttr.getAttributeCount());
					return new ArrayList<ArrayList>(); // leere Liste der kompensierten Päne zurück
				}
				// trifft nichts zu, dann haben wir  ersteinmal ausreichend viele Cluster für einen Kompensationsplan
				// gesammelt und die verbliebenen sollten verworfen werden, damit compensate() terminiert
				while(toCompensate.size()>0)  
				{
					AnnotatedPlan pl = toCompensate.get(toCompensate.size()-1);
	    			logger.debug("> Removing Plan ["+pl.getIndex()+"]/ GIP: "+pl.getGlobalInputPattern()); 
					toCompensate.remove(toCompensate.size()-1); // pop ;)

				}	
			} // gibts ne dritte möglichkeit? TODO zum Nachdenken
    	} 
    	return clusterSet;
    }
    
    /**
     * Abhängig davon ob ein Kostenmodell integriert werden soll oder nicht, wird eine Menge der
     * logischen ausführbaren Pläne generiert. Integriert strenggenommen die Ansätze des CHAIN-Algorithmus.
     * @param clusterSet Menge der Cluster (Ergebnis der Phase1 des Partition-Algorithmus
     * @return 
     */
    @SuppressWarnings("unchecked")
    protected ArrayList <AnnotatedPlanQueue> getAnnotatedPlanQueues(ArrayList <ArrayList> clusterSet)
    {
    	ArrayList <AnnotatedPlanQueue> executionQueues = new ArrayList<AnnotatedPlanQueue>();
    	if(this.costControlled)
    	{
    		// Man man ähnlich wie beim GREEDY vorgehen und Pläne mit 
    		//den geringsten Kosten bevorzugen. Bei jedem Durchlauf wird de günstigste der verbliebenen
	    	//Pläne ausgewählt => Ein Beispiel....
    		AnnotatedPlanQueue executionQueue = new AnnotatedPlanQueue();	
    		for(int i=0; i<clusterSet.size();i++) // Nun alle Cluster im Set  durchlaufen und....
    		{	
    			ArrayList nextCluster = (ArrayList)clusterSet.get(i);
		    	while(nextCluster.size()>0)
		    	{
		    		double M = this.M;
			    	for(int j=0; j<nextCluster.size();j++) //... alle Annotated im Cluster durchlaufen und....
					{	
			    		AnnotatedPlan nextPlan = (AnnotatedPlan)nextCluster.get(j);
			    		//double costs = calcCosts(nextPlan, this.query, this.context); // Kosten berechnen
			    		double costs = 10;
			    		if(costs<M)
			    		{
			    			executionQueue.addPlan(nextPlan);
			    			nextCluster.remove(nextPlan);
			    			//logger.debug("> PLAN TO QUEUE (Cost):["+nextPlan.getIndex()+"]/ GIP: "+nextPlan.getGlobalInputPattern()); 
			    			M = costs;
			    		}
					}	
		    	}
    		}
    		executionQueues.add(executionQueue);
    	}
    	else
    	{
    		ArrayList <ArrayList> permutations = this.getSubgoalPermutations(clusterSet);
    		
	   		 for(int j=0; j<permutations.size(); j++) // NUR DEBUG
			 {
				 ArrayList oneP = permutations.get(j);
				 for(int l=0; l<oneP.size(); l++) 
				 {
					ArrayList oneQ = (ArrayList)oneP.get(l);
					 
		     		StringBuffer sb = new StringBuffer();
		     		for(int k=0; k<oneQ.size(); k++) 	 // NUR DEBUG
		    			sb.append(" "+((AnnotatedPlan)(oneQ.get(k))).getIndex());
		    		logger.debug("> Permutation: ["+j+"] / CLUSTER "+(j+1)+" {"+sb+" }");
				 }	
			 }	
	   		 
	   		 //	Schritt 2: Nun aus den permutatations  die executionQueues bauen
	   	   	executionQueues = getSubplanPermutations(permutations);
    	}
   		
		 for(int j=0; j<executionQueues.size(); j++) // NUR DEBUG
		 {
			AnnotatedPlanQueue oneP = executionQueues.get(j);
			
     		StringBuffer sb = new StringBuffer();
     		for(int k=0; k<oneP.getQueueSize(); k++) 	 // NUR DEBUG
    			sb.append(" "+((AnnotatedPlan)(oneP.getPlan(k))).getIndex());
    		logger.debug("> QUEUE ["+j+"] {"+sb+" }");
		 }
	
		return executionQueues;
    }
   
   
	/**
	 * Bekommt die Menge der Cluster (Ergebnis der ersten Phase des Partition) und bildet für JEDEN Cluster eine 
	 * MENGE der neuen Cluster, wobei jede neue Cluster dieser Menge eine neue Permutation der 
	 * Teilziele darin darstellt. D.h wir  suchen alle PERMUTATIONEN der Elemente in jedem
	 * Cluster des gegebenen ClusterSets.
	 * 
	 * Beispiel: Sei clusterSet = {(A,B), (D,E), (F) }, also ein Set aus drei Clustern
	 * Die Methode liefert daraufhin:  { {(A,B),(B,A)}, {(D,E),(D,E)}, {F}}
	 * 
	 * Die Permuation der Elemente wird durch eine importierte Klasse umgesetzt.
	 * @see  mg.dynaquest.sourceselection.support.Permutation.java
	 * 
	 * @param clusterSet Die Menge der Cluster
	 * @return 
	 */
    @SuppressWarnings("unchecked")
    protected ArrayList <ArrayList> getSubgoalPermutations(ArrayList <ArrayList> clusterSet)
    {   
    	//	Unsere spätere Menge der Clustermengen
    	//  Vorsicht komplex! Ist eine ArrayList<ArrayList<ArrayList<Annotated>>>
		ArrayList <ArrayList> permutations = new ArrayList<ArrayList>(clusterSet.size()); 
		for(int i=0; i<clusterSet.size();i++) // Nun alle Cluster im Set  durchlaufen und....
		{			
			ArrayList  nextCluster = (ArrayList)clusterSet.get(i);
			ArrayList  <ArrayList> nextPermutationCluster = new ArrayList  <ArrayList> ();  // Liste für die Permutationen der einzelnen Cluster
			Permutation p = new Permutation(nextCluster.size());
			while(p.hasNext())
			{ 	 
				ArrayList nextPermutation = new ArrayList();  // Neue Permutation
				 for(int k=0;k<nextCluster.size();k++)
					 nextPermutation.add((AnnotatedPlan)nextCluster.get(p.get(k)));
				 nextPermutationCluster.add(nextPermutation);
				 p.next();
			}
			p.last(); // letze Permutation mitnehmen, leider nur als Hack, da while nicht erfasst
			ArrayList nextPermutation = new ArrayList();  // Neue Permutation
			 for(int k=0;k<nextCluster.size();k++)
				 nextPermutation.add((AnnotatedPlan)nextCluster.get(p.get(k)));
			nextPermutationCluster.add(nextPermutation);	
			 
			permutations.add(nextPermutationCluster);
		} 
		
		return permutations;
    }
    
	/**
	 * Eine besondere Methode. Diese arbeitet auf dem Ergebnis der getSubgoalPermutations() und versucht nun 
	 * eine Liste der Ausführungspläne zu bauen, wobei jeder Plan eine abweichende Ausführungsreihenfolge von mind zwei
	 * Teilziehen aufweist. Auf diese Weise entstehen Pläne, die Permutation der Teilziele UNTER DER BERÜCKSICHTIGUNG
	 * DER CLUSTER-REIHENFOLGE darstellen.
	 * 
	 * Beispiel: 
	 * Seien zwei Cluster gegeben. 
	 * CLUSTER 0 : { 0 4 } 
	 * CLUSTER 1 : { 2 3 } 
	 * Daraus macht getSubgoalPermutations() folgende 4 Permutation, je zwei für jeden Cluster (2²)
	 * Perm [0] > CLUSTER 1 { 0 4 } 
	 * Perm [0] > CLUSTER 1 { 4 0 } 
	 * Perm [1] > CLUSTER 2 { 2 3 } 
	 * Perm [1] > CLUSTER 2 { 3 2 } 
	 * Diese Methode muss nun aus diesen Clustern eine Menge der Pläne so bauen, dass keine zwei Pläne identisch sind.
	 * QUEUE [0] { 0 4 2 3 } 
	 * QUEUE [1] { 0 4 3 2 } 
	 * QUEUE [2] { 4 0 2 3 } 
	 * QUEUE [3] { 4 0 3 2 } 
	 * 
	 * WICHTIG!: Wie man erkennt, können bspw. die Teilziele 4 und 2 NIEMALS untereinander die 
	 * Plätze tauschen, da sie in unterschiedlichen Clustern liegen => Permutation unter Berücksichtig der 
	 * lokalen Abhängigkeiten.
	 * 
     * @param permutations Eine Menge von permutierten Clustermengen (Komplex: @see this.getSubgoalPermutations())
     * @return Menge der logischen, ausführbaren Zugriffspläne auf einem Quellenverbund
     */
    @SuppressWarnings("unchecked")
    private ArrayList <AnnotatedPlanQueue> getSubplanPermutations(ArrayList <ArrayList> permutations)
    {
    	ArrayList <AnnotatedPlanQueue> executionQueues = new ArrayList<AnnotatedPlanQueue>();
  		 int queueCount = 0;
   		 for(int i=0; i<permutations.size(); i++) 
		 {
			 ArrayList nextP = permutations.get(i);
			 if(i==0 && nextP.size()>0) // wenn im ersten Permutationscluster Pläne sind, lassen sich executionQueues bauen
				 queueCount=1;
			 queueCount*=nextP.size();
		 }
   		logger.debug("> ANZAHL "+queueCount);
    	int pre = 1; // Wie oft ALLE der Permutation durchlaufen werden
    	
   		
		for(int i=0;i<permutations.size();i++)
		{
			ArrayList <ArrayList> nextPermutationCluster = permutations.get(i); // Permutierte Version von jedem Cluster durchlaufen
			logger.debug("> PRE:"+pre);
			logger.debug("> MID(ClusterSIZE):"+nextPermutationCluster.size());
			logger.debug("> POST:"+queueCount/nextPermutationCluster.size()/pre); // Wie oft jedes einzelne element, hintereinander geschrieben wird
			int countHelper = 0;
				for(int h= 0; h<pre;h++)
					for(int j= 0; j<nextPermutationCluster.size();j++)
						for(int k=0; k<(queueCount/nextPermutationCluster.size()/pre);k++)				
						{
							 if(i == 0)
							 {
									AnnotatedPlanQueue initQ = new AnnotatedPlanQueue(nextPermutationCluster.get(j));
									executionQueues.add(initQ);				  				 
							 }
							 else
							 {
									ArrayList <AnnotatedPlan> nextClusterPermutation = nextPermutationCluster.get(j);
									executionQueues.get(countHelper).addAll(nextClusterPermutation);
									countHelper++;
							 }					
						}
			pre*= nextPermutationCluster.size();
		}
		
		return executionQueues;
    }
    /**
     * Berechnet die Menge der Ausgabeattribute, die von allen Plänen innerhalb einer Cluster-Menge geliefert werden.
     * @param clusterSet Menge der Cluster
     * @return 
     */
    protected  SDFAttributeList getOutputPatternForClusterSet(ArrayList <ArrayList> clusterSet)
    {
    	SDFAttributeList outputPattern = new SDFAttributeList();
		for(int i=0; i<clusterSet.size();i++) // Nun alle Cluster im Set  durchlaufen und....
		{	
	    	ArrayList  nextCluster = clusterSet.get(i);
	    	for(int j=0; j<nextCluster.size();j++) //... alle Annotated im Cluster durchlaufen und....
			{	
	    		AnnotatedPlan nextPlan = (AnnotatedPlan)nextCluster.get(j);
	    		// GOPs als Attributmengen verknüpfen
	    		outputPattern = SDFAttributeList.union(outputPattern, nextPlan.getGlobalOutputPattern().getAllAttributes());
			}	
		}
		
 		StringBuffer sb = new StringBuffer();
 		for(int l=0; l<outputPattern.getAttributeCount(); l++) 	 // NUR DEBUG
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
    
    /** 
     * Testwiese für Permutation.java
     * @param args
     */
    @SuppressWarnings("unchecked")
    public static void main(String[] args) 
    {
    	String[][][] s1 = {{{"1","2"},{"2","1"}},
    			{{"3","4"},{"4","3"}},
    			//{{"5","6"},{"6","5"}},
    			{{"5","6","7"},{"5","7","6"}, {"6","5","7"}, {"6","7","5"}, {"7","5","6"}, {"7","6","5"}}, 
    			//{{"8"}}, 
    			{{"9","10"},{"10","9"}}};
    	
    	int size = 1; 
    	 for(int i=0;i<s1.length;i++)
    		 size*=s1[i].length;
    		
    	 System.out.println("SIZE:"+size);
    		 
    	ArrayList <ArrayList> que= new ArrayList<ArrayList>();
    	int der = 1;
		for(int i=0;i<s1.length;i++)
		{
			String[][] sT  = s1[i]; // Permutierte Version von jedem Cluster durchlaufen
//
//			 if(i==0) // Initiale Versionen der Queues anlegen
//			 {
//				 der*= sT.length;
//				 System.out.println("DER:"+der);
//				 for(int j= 0; j<sT.length;j++) //  jedes Cluster queueCount / Anzahl der permutierten Version hinzufügen
//					 for(int k=0; k<(size/sT.length);k++)
//					 {
//						ArrayList <String>temp = new ArrayList<String>();
//						for(int g=0;g<sT.length;g++)
//							temp.add(sT[j][g]);
//						que.add(temp);
//					 }
//			 }
////			 else if(i%2==1)//ab hier die initialen Queues erweitern um die Pläne weiterer Cluster-Permutationen
////			 {
////				  	int countHelper = 0;
////				  	 for(int k=0; k<(48/sT.length);k++)
////					   for(int j= 0; j<sT.length;j++) //  jedes Cluster queueCount / Anzahl der permutierten Version hinzufügen
////						 {
////							ArrayList <String>temp = new ArrayList<String>();
////							for(int g=0;g<sT[j].length;g++)
////								temp.add(sT[j][g]);
////							que.get(countHelper).addAll(temp);
////							countHelper++;
////						}
////			 }
//			else
//			 {
//					
					 System.out.println("DER:"+der);
					 System.out.println("PRE:"+sT.length);
					 System.out.println("POST:"+size/sT.length/der);
				  	int countHelper = 0;
				  	for(int a=0; a<der;a++)
					 for(int j= 0; j<sT.length;j++) //  jedes Cluster queueCount / Anzahl der permutierten Version hinzufügen
						 for(int k=0; k<(size/sT.length/der);k++)			
						 {
								ArrayList <String>temp = new ArrayList<String>();
								if(i==0)
								{
									for(int g=0;g<sT.length;g++)
										temp.add(sT[j][g]);
									que.add(temp);
								}
								else
								{
									for(int g=0;g<sT[j].length;g++)
										temp.add(sT[j][g]);
									que.get(countHelper).addAll(temp);
									countHelper++;
								}
						}
		
				  	der*= sT.length;	
//					
//			 }	 
		}
		
		
 		 for(int j=0; j<que.size(); j++) // NUR DEBUG
		 {
  			ArrayList oneP = que.get(j);
  			System.out.print("##### QUEUE  ["+j+"] {");
  			for(int k=0; k<oneP.size(); k++)
  				System.out.print(" "+((String)(oneP.get(k))));  
  			System.out.println(" }");
		 }
 		 

 		 
 		 
//    	String[][] s = {{"1","2","3"},{"4","5"},{"6","7"}};
//	      for (int i=0; i<s.length; i++)
//	      {
//	    	  String [] d= s[i];
//		       // for(int n=0;n<d.length;n++)
//		            // System.out.print(d[n]+",");
//	    	 // String[][] f = new String[3][10];
//	    	  Permutation p = new Permutation(d.length);
//		      while(p.hasNext())
//		      {
//		    	  
//		         for(int k=0;k<d.length;k++)
//		             System.out.print(d[p.get(k)]+",");
//		         System.out.println(" PermutationNumber: "+p.getPermutationNumber());
//		         p.next();
//		      }
//		      p.last();
//		         for(int k=0;k<d.length;k++)
//		             System.out.print(d[p.get(k)]+",");
//		   
//	      }
    }


    
}
