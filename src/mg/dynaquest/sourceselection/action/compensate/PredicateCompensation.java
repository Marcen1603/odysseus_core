package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.SelectPO;
import mg.dynaquest.sourcedescription.sdf.description.SDFInputPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFOutputPattern;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.query.WSimplePredicateSet;
import mg.dynaquest.sourcedescription.sdf.query.WeightedSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import mg.dynaquest.sourceselection.exception.PredicateCompensationImpossibleException;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author  Jurij Henne  Diese CompensateAction überprüft, ob die in der Anfrage definierte Prädikate direkt auf den Quellen (Zugriffsplänen)  ausführbar sind. Ist dies nicht der Fall und die Prädikatattribute werden im GOP des jeweiligen Plan zurückgeliefert,  ist eine Kompensation trotzdem möglich.  Die Klasse hat zwei Phasen:  1. Die erste Phase dient dazu, alle Prädikate zu bestimmen, deren Attribute nicht im GIP des jeweiligen Plans   vorkommen.  2. Die zweite Phase ist optional und überprüft bei den Prädikaten, die durch GIP abgedeckt sind, ob die darin spezifizierte   Vergleichoperatoren mit den des jeweiligen Plans kompatibel sind, sonst werden die betroffenen Prädikate der bestehenden  Menge hinzugefügt.  Weitere Features :  - Legt interne Interpretation für Gewichtung "notwendig" fest (@see weightingLimit)  - Man darf bestimmen, wie restriktiv die Kompensation vorgehen kann: alle kompensieren oder "ein auge zudrücken"   (@see restrictiveCompensation)
 */
public class PredicateCompensation extends AbstractCompensateAction  {


	/**
	 * Kompensationseigener Debuger
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected Logger logger = Logger.getLogger(this.getClass()); 
	/** Pläne die im Verlauf des Kompensationsprozess entstanden sind */
	//protected ArrayList <AnnotatedPlan> compensatedPlans = null; 
	/** Pläne zu kompensieren */
	//protected ArrayList <AnnotatedPlan> context = null;			 
	/**
	 * Attribute, die in den Prädikaten belegt sind
	 * @uml.property  name="predicateAttributes"
	 * @uml.associationEnd  
	 */
	//protected SDFAttributeList predicateAttributes = null;  		 // 
	/**
	 * Eine sog. Custom-Vorgabe für Gewichtung "notwendig"
	 * @uml.property  name="weightingLimit"
	 */
	protected double weightingLimit = 0.00;						// 
	/**
	 * Ist restrictiveCompensation = true, dann können nur Pläne generiert, die alle Predikate kompensieren können.  restrictiveCompensation = false bedeutet, dass zwar alle unberücksichtigte Prädikate betrachtet werden, aber auch Pläne generiert werden können, die NICHT alle Prädikate kompensieren (bspw. weil notwendige Predikatattribute nicht im GOP des Plans vorhanden sind)
	 * @uml.property  name="restrictiveCompensation"
	 */
	//TODO: Sinn und Zweck von restrictiveCompensation überprüfen
	boolean restrictiveCompensation = true;
	/**
	 * Legt fest, ob auch die inkompatiblen Vergleichsoperatoren berücksichtigt werden sollen.
	 * @uml.property  name="doOperatorCompensation"
	 */
	protected boolean doOperatorCompensation = false;

	public PredicateCompensation(){}

	public PredicateCompensation(SDFQuery query)
	{
    	this.setQuery(query);
	}
	
	
    /**
    *
    * Wir suchen alle Prädikate zusammen, deren Attribute nicht  im Eingabemuster der Quelle vorkommen 
    * ODER
    * deren Operatoren mit der der Quelle nicht kompatibel sind. 
    * Diese Prädikate können nur dann kompensiert werden, wenn entsprecheden Attribute 
    * im Ausgabemuster der Quelle vorhanden sind.
    * Die Menge der gefundenen Prädikate wird anschliessend an den SelectPO übergeben.
    */
    public ArrayList<AnnotatedPlan> compensate(ArrayList <AnnotatedPlan> toCompensate)  // identisch zu der CompensateConst
    { 	
    	ArrayList<AnnotatedPlan> compensatedPlans = new ArrayList <AnnotatedPlan>();
    	//this.context = new ArrayList <AnnotatedPlan>(toCompensate);
    	this.setErrorPlans(new ArrayList <AnnotatedPlan>(toCompensate));
    	SDFAttributeList predicateAttributes = new SDFAttributeList();
    	
    	//1a. Sammeln alle von der Query belegten Eingabeattribute.
    	//this.predicateAttributes = this.query.getPredicateAttributes(); // Berücksichtigt nicht Gewichtung , daher anders
    	WSimplePredicateSet queryWeightedPredicates = this.getQuery().getQueryPredicates();	
    	//WSimplePredicateSet requiredPredicates = new WSimplePredicateSet() ;  // Prädikate die als notwendig betrachtet werden
    	logger.debug("> QueryPredicatesCount: "+queryWeightedPredicates.getPredicateCount()); 
    	for(int i=0; i<queryWeightedPredicates.getPredicateCount(); i++)
    	{
    		WeightedSimplePredicate nextPred = queryWeightedPredicates.getPredicate(i);
    		//logger.debug("> QueryPredicates: "+nextPred,3);  	

    		if(nextPred.getWeighting()>=this.weightingLimit) // Gewichtung ist über dem Limit?
    		{
    			//requiredPredicates.addPredicate(nextPred); //Wird der Liste der zu kompensierenden Predikate hinzugefügt		
    			predicateAttributes.addAttribute(nextPred.getPredicate().getAttribute());// Attribut, das von dem aufgenommenen Prädikaten belegt wird		 
    		}
    	}
      	
    	// Zu kompensierende Pläne durchlaufen und folgendes machen....
    	for(int i=0; i<toCompensate.size(); i++)
    	{		
    		WSimplePredicateSet predicatesToCompensate = new WSimplePredicateSet(); // Die Liste zu kompensierenden Prädikate
    		AnnotatedPlan nextPlan = (AnnotatedPlan)toCompensate.get(i); // nächsten Plan holen (HIER immer nur EIN PLAN (Satzorientiert)
    		// Attribute des Eingabemusters extrahieren
    		SDFAttributeList inputPattern = nextPlan.getGlobalInputPattern().getAllAttributes(); // wir müssen alle Attribute berücksichtigen
    		// Zunächst schauen, welche Prädikate vom Eingabemuster nicht abgedeckt werden(dass dies der Fall ist, hat der Filter bereits gesagt)
    		SDFAttributeList predAttrToCompensate = SDFAttributeList.difference(predicateAttributes, inputPattern); 
    		// schon mal GOP bestimmen für später
    		SDFAttributeList outputPattern = nextPlan.getGlobalOutputPattern().getAllAttributes(); 
    		
    		logger.debug("> Phase1: PLAN ["+nextPlan.getIndex()+"] GIP:"+nextPlan.getGlobalInputPattern()+" / GOP:"+nextPlan.getGlobalOutputPattern()); 
     		StringBuffer sb = new StringBuffer();
     		for(int l=0; l<predAttrToCompensate.getAttributeCount(); l++) 	 // NUR DEBUG
    			sb.append(" "+predAttrToCompensate.getAttribute(l));
    		logger.debug("> Phase1: PLAN ["+nextPlan.getIndex()+"]:Predicates2Compensate {"+sb+" }");
    		  
	    	//-----------------------   ADVANCED PART -------------------------------------//
	    	// EXTRA: Bei den  Prädikaten, die  im GIP der Quelle ausgewertet werden können, werden 
	    	// zusätzlich Vergleichsoperatoren ausgewertet. Und bei Inkompatibilität der Liste der zu kompensierenden 
	    	// PrädikatAttribute hinzugefügt.    		
    		if(this.doOperatorCompensation)
    		{
    			throw new NotImplementedException();
//    			SDFAttributeList predAttrToCompensateOP = SDFAttributeList.intersection(predicateAttributes, inputPattern); // Prädikate, die durch GIP abgedeckt sind
//		    	for(int k = 0; k<queryWeightedPredicates.getPredicateCount();k++) // alle Prädikate durchlaufen
//		    	{
//		    		SDFAttribute queryPredAttr = queryWeightedPredicates.getPredicate(k).getPredicate().getAttribute();
//		    		if(predAttrToCompensateOP.contains(queryPredAttr)) // wenn Predikatattribut in GIP ist..
//		    		{
//		    			// Vergleichoperator des Prädikats...
//						SDFCompareOperator queryPredOP = queryWeightedPredicates.getPredicate(k).getPredicate().getCompareOp();
//                        System.err.println(queryWeightedPredicates.toString());
//						// Mit dem Vergleichoperator der Attributes im GIP des PLan vergleichen
//						SDFPattern planGIP = nextPlan.getGlobalInputPattern();
//		    			SDFAttributeAttributeBindingPair attributeBindingPair = planGIP.getAttributeAttributeBindingPair(queryPredAttr);
//		    			SDFInputAttributeBinding attributeBindingPairAttributeBinding = (SDFInputAttributeBinding)attributeBindingPair.getAttributeBinding();
//		    			// TODO: HIER WIRD JETZT IMMER DER ERSTE GENOMMEN ... sowieso geht das hier alles nicht mehr so ...
//		    			SDFCompareOperator planNextAttrOP = attributeBindingPairAttributeBinding.getCompareOps().;
//		    			if(planNextAttrOP == null )  // Wenn kein Operator definiert wurde, ist er null, daher "=" draus machen
//		    				planNextAttrOP = SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Equal);
//		        		logger.debug("> Phase2: PLAN ["+
//                                nextPlan.getIndex()+
//                                "] queryPredOP("+queryPredAttr+") "+
//                                queryPredOP+" isEqual("+
//                                queryPredOP == null?"null":(queryPredOP.equals(planNextAttrOP))+") "+ 
//                                planNextAttrOP+" of planNextAttrOP("+
//                                queryPredAttr+")");  
//	
//		    			if(!queryPredOP.equals(planNextAttrOP)) // sollten die OPs nicht übereinstimmen
//		    			{
//		    				predAttrToCompensate.addAttribute(queryPredAttr);// Attribut des Prädikats der Liste hinzufügen
//		    			}    			  			
//		    		}
//		    	}
//	    		logger.debug("> Phase2: PLAN ["+nextPlan.getIndex()+"] GIP:"+nextPlan.getGlobalInputPattern()+" / GOP:"+nextPlan.getGlobalOutputPattern()); 
//	     		
//	    		sb = new StringBuffer();
//	    		for(int l=0; l<predAttrToCompensate.getAttributeCount(); l++) 	 // NUR DEBUG
//	   			sb.append(" "+predAttrToCompensate.getAttribute(l));
//	    		logger.debug("> Phase2: PLAN ["+nextPlan.getIndex()+"]:Predicates2Compensate(withOP) {"+sb+" }");
    		}
    		
    		// Schauen ob alle predAttrOffInputPattern im GOP vorkommen. Gleichzeitig Abfrage, ob predAttrToCompensate nicht leer ist
 			// Zwei Möglichkeiten: entweder werden nur solche Pläne kompensiert, die alle Prädikate 
 			// berücksichtigen  => restrictiveCompensation ODER alle (weil machmal besser irgendwelche 
 			// Ergebnisse zu haben als gar keine Antwort)
    		if(predAttrToCompensate.getAttributeCount()>0 && predAttrToCompensate!=null 
    				&& ((this.restrictiveCompensation &&  SDFAttributeList.subset(predAttrToCompensate, outputPattern))
        																			|| !this.restrictiveCompensation)) 
    		{
     			// Prädikatenliste aus der Liste der Attribute zusammenstellen 
     			for(int j = 0; j<queryWeightedPredicates.getPredicateCount(); j++)
     			{
     				SDFAttribute predAttribute =  queryWeightedPredicates.getPredicate(j).getPredicate().getAttribute();
     				if(predAttrToCompensate.contains(predAttribute)) // enhalten?
     				{
     					predicatesToCompensate.addPredicate(queryWeightedPredicates.getPredicate(i)); // Prädikat hinzufügen
     				}
     			}
     	   		logger.debug("> Predicate Count: "+predicatesToCompensate.getPredicateCount()); 
    			// Anschliessend neuen Annotated-Plan bauen, der SelectPO integriert und die Prädikatenliste bekommt 
    		    try 
    		    {
    				AnnotatedPlan newPlan = getSelectPOCompensatedPlan(nextPlan, predicatesToCompensate);
    				if(newPlan!=null)
    				{
    					compensatedPlans.add(newPlan); // Plan hinzufügen
    					this.getErrorPlans().remove(nextPlan); // den Plan aus der Errorlist rausnehmen
    				}
    		    }
    		    catch(Exception e)
    		    {
    		    	e.printStackTrace();
    		    }    			
    		}
    		else
    		{
    			// Kann man nichts machen, Plan ist nicht kompensierbar
    			// evtl dem Menge der Ausschusspläne hinzufügen ==> TODO
                logger.info("Plan ist nicht kompensierbar");
    		}
    	}

    	// Evtl auch die Inputpläne weiterleiten
    	// compensatedPlans.addAll(context);
    	return compensatedPlans;
    }

	/**
	 * Integriert den SelectPO in einen neuen AnnotatedPlan. Der SelectPO bekommt eine Liste der Prädikate, 
	 * die auf den Attributen im Ergebnis der Zugriffsplans berücksichtigt werden sollen.
	 * @param inputAnnoPlan Eingabeplan
	 * @param predicatesToCompensate Liste der Prädikate, die es zu berücksichtigen gilt
	 * @return
	 * @throws PredicateCompensationImpossibleException
	 */
    private  AnnotatedPlan getSelectPOCompensatedPlan(AnnotatedPlan inputAnnoPlan ,  WSimplePredicateSet  predicatesToCompensate)
	throws PredicateCompensationImpossibleException
	{
	    AnnotatedPlan newPlan = new AnnotatedPlan();

	    try{
	        // Zunächst die neuen Zugriffsmuster bestimmen:
	        SDFInputPattern inputPattern  = new SDFInputPattern(inputAnnoPlan.getGlobalInputPattern()); // inputPattern bleibt gleich
	        SDFOutputPattern outputPattern = new SDFOutputPattern(inputAnnoPlan.getGlobalOutputPattern()); // outputPattern bleibt gleich
	        
	        newPlan.setGlobalInputPattern(inputPattern);
	        newPlan.setGlobalOutputPattern(outputPattern);

	        AlgebraPO inputPlan = inputAnnoPlan.getAccessPlan();
	        
	        SelectPO respectPredicatesPO = new SelectPO(predicatesToCompensate);
	        respectPredicatesPO.setInputPO(inputPlan);
	        respectPredicatesPO.setOutElements(outputPattern.getAllAttributes());
	        
	        newPlan.setAccessPlan(respectPredicatesPO);
	        newPlan.addBasePlan(inputAnnoPlan);        
	    }
	    catch(Exception e)
	    {
	        throw new PredicateCompensationImpossibleException("Integration des ProjectPO in neuen Plan fehlgeschlagen!");
	    }
	    return newPlan;
	}
    
    
    public  void initInternalBaseValues(NodeList children) 
	{
		for (int i = 0; i < children.getLength(); i++) 
		{
			Node cNode = children.item(i);
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("predicateWeightingLimit")) 
				this.weightingLimit = Double.parseDouble(cNode.getFirstChild().getNodeValue());
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("restrictiveCompensation")) 
					this.restrictiveCompensation = (Integer.parseInt(cNode.getFirstChild().getNodeValue()) != 0);
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("doOperatorCompensation")) 
				this.doOperatorCompensation = (Integer.parseInt(cNode.getFirstChild().getNodeValue()) != 0);	

		}
	}

    
    public  void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue) 
    {
		// CompensatePO kann erstmal nur Action-Class, ID und Name schreiben
    	xmlRetValue.append(baseIndent + "<predicateWeightingLimit>");
    	xmlRetValue.append(this.weightingLimit);
    	xmlRetValue.append("</predicateWeightingLimit>\n");
    	
    	xmlRetValue.append(baseIndent + "<restrictiveCompensation>");
    	xmlRetValue.append((this.restrictiveCompensation)?1:0);
    	xmlRetValue.append("</restrictiveCompensation>\n");
    	
    	xmlRetValue.append(baseIndent + "<doOperatorCompensation>");
    	xmlRetValue.append((this.doOperatorCompensation)?1:0);
    	xmlRetValue.append("</doOperatorCompensation>\n");
    }
    public static void main(String[] args) {

    }
}