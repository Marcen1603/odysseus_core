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
 * @author  Jurij Henne  Diese CompensateAction �berpr�ft, ob die in der Anfrage definierte Pr�dikate direkt auf den Quellen (Zugriffspl�nen)  ausf�hrbar sind. Ist dies nicht der Fall und die Pr�dikatattribute werden im GOP des jeweiligen Plan zur�ckgeliefert,  ist eine Kompensation trotzdem m�glich.  Die Klasse hat zwei Phasen:  1. Die erste Phase dient dazu, alle Pr�dikate zu bestimmen, deren Attribute nicht im GIP des jeweiligen Plans   vorkommen.  2. Die zweite Phase ist optional und �berpr�ft bei den Pr�dikaten, die durch GIP abgedeckt sind, ob die darin spezifizierte   Vergleichoperatoren mit den des jeweiligen Plans kompatibel sind, sonst werden die betroffenen Pr�dikate der bestehenden  Menge hinzugef�gt.  Weitere Features :  - Legt interne Interpretation f�r Gewichtung "notwendig" fest (@see weightingLimit)  - Man darf bestimmen, wie restriktiv die Kompensation vorgehen kann: alle kompensieren oder "ein auge zudr�cken"   (@see restrictiveCompensation)
 */
public class PredicateCompensation extends AbstractCompensateAction  {


	/**
	 * Kompensationseigener Debuger
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected Logger logger = Logger.getLogger(this.getClass()); 
	/** Pl�ne die im Verlauf des Kompensationsprozess entstanden sind */
	//protected ArrayList <AnnotatedPlan> compensatedPlans = null; 
	/** Pl�ne zu kompensieren */
	//protected ArrayList <AnnotatedPlan> context = null;			 
	/**
	 * Attribute, die in den Pr�dikaten belegt sind
	 * @uml.property  name="predicateAttributes"
	 * @uml.associationEnd  
	 */
	//protected SDFAttributeList predicateAttributes = null;  		 // 
	/**
	 * Eine sog. Custom-Vorgabe f�r Gewichtung "notwendig"
	 * @uml.property  name="weightingLimit"
	 */
	protected double weightingLimit = 0.00;						// 
	/**
	 * Ist restrictiveCompensation = true, dann k�nnen nur Pl�ne generiert, die alle Predikate kompensieren k�nnen.  restrictiveCompensation = false bedeutet, dass zwar alle unber�cksichtigte Pr�dikate betrachtet werden, aber auch Pl�ne generiert werden k�nnen, die NICHT alle Pr�dikate kompensieren (bspw. weil notwendige Predikatattribute nicht im GOP des Plans vorhanden sind)
	 * @uml.property  name="restrictiveCompensation"
	 */
	//TODO: Sinn und Zweck von restrictiveCompensation �berpr�fen
	boolean restrictiveCompensation = true;
	/**
	 * Legt fest, ob auch die inkompatiblen Vergleichsoperatoren ber�cksichtigt werden sollen.
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
    * Wir suchen alle Pr�dikate zusammen, deren Attribute nicht  im Eingabemuster der Quelle vorkommen 
    * ODER
    * deren Operatoren mit der der Quelle nicht kompatibel sind. 
    * Diese Pr�dikate k�nnen nur dann kompensiert werden, wenn entsprecheden Attribute 
    * im Ausgabemuster der Quelle vorhanden sind.
    * Die Menge der gefundenen Pr�dikate wird anschliessend an den SelectPO �bergeben.
    */
    public ArrayList<AnnotatedPlan> compensate(ArrayList <AnnotatedPlan> toCompensate)  // identisch zu der CompensateConst
    { 	
    	ArrayList<AnnotatedPlan> compensatedPlans = new ArrayList <AnnotatedPlan>();
    	//this.context = new ArrayList <AnnotatedPlan>(toCompensate);
    	this.setErrorPlans(new ArrayList <AnnotatedPlan>(toCompensate));
    	SDFAttributeList predicateAttributes = new SDFAttributeList();
    	
    	//1a. Sammeln alle von der Query belegten Eingabeattribute.
    	//this.predicateAttributes = this.query.getPredicateAttributes(); // Ber�cksichtigt nicht Gewichtung , daher anders
    	WSimplePredicateSet queryWeightedPredicates = this.getQuery().getQueryPredicates();	
    	//WSimplePredicateSet requiredPredicates = new WSimplePredicateSet() ;  // Pr�dikate die als notwendig betrachtet werden
    	logger.debug("> QueryPredicatesCount: "+queryWeightedPredicates.getPredicateCount()); 
    	for(int i=0; i<queryWeightedPredicates.getPredicateCount(); i++)
    	{
    		WeightedSimplePredicate nextPred = queryWeightedPredicates.getPredicate(i);
    		//logger.debug("> QueryPredicates: "+nextPred,3);  	

    		if(nextPred.getWeighting()>=this.weightingLimit) // Gewichtung ist �ber dem Limit?
    		{
    			//requiredPredicates.addPredicate(nextPred); //Wird der Liste der zu kompensierenden Predikate hinzugef�gt		
    			predicateAttributes.addAttribute(nextPred.getPredicate().getAttribute());// Attribut, das von dem aufgenommenen Pr�dikaten belegt wird		 
    		}
    	}
      	
    	// Zu kompensierende Pl�ne durchlaufen und folgendes machen....
    	for(int i=0; i<toCompensate.size(); i++)
    	{		
    		WSimplePredicateSet predicatesToCompensate = new WSimplePredicateSet(); // Die Liste zu kompensierenden Pr�dikate
    		AnnotatedPlan nextPlan = (AnnotatedPlan)toCompensate.get(i); // n�chsten Plan holen (HIER immer nur EIN PLAN (Satzorientiert)
    		// Attribute des Eingabemusters extrahieren
    		SDFAttributeList inputPattern = nextPlan.getGlobalInputPattern().getAllAttributes(); // wir m�ssen alle Attribute ber�cksichtigen
    		// Zun�chst schauen, welche Pr�dikate vom Eingabemuster nicht abgedeckt werden(dass dies der Fall ist, hat der Filter bereits gesagt)
    		SDFAttributeList predAttrToCompensate = SDFAttributeList.difference(predicateAttributes, inputPattern); 
    		// schon mal GOP bestimmen f�r sp�ter
    		SDFAttributeList outputPattern = nextPlan.getGlobalOutputPattern().getAllAttributes(); 
    		
    		logger.debug("> Phase1: PLAN ["+nextPlan.getIndex()+"] GIP:"+nextPlan.getGlobalInputPattern()+" / GOP:"+nextPlan.getGlobalOutputPattern()); 
     		StringBuffer sb = new StringBuffer();
     		for(int l=0; l<predAttrToCompensate.getAttributeCount(); l++) 	 // NUR DEBUG
    			sb.append(" "+predAttrToCompensate.getAttribute(l));
    		logger.debug("> Phase1: PLAN ["+nextPlan.getIndex()+"]:Predicates2Compensate {"+sb+" }");
    		  
	    	//-----------------------   ADVANCED PART -------------------------------------//
	    	// EXTRA: Bei den  Pr�dikaten, die  im GIP der Quelle ausgewertet werden k�nnen, werden 
	    	// zus�tzlich Vergleichsoperatoren ausgewertet. Und bei Inkompatibilit�t der Liste der zu kompensierenden 
	    	// Pr�dikatAttribute hinzugef�gt.    		
    		if(this.doOperatorCompensation)
    		{
    			throw new NotImplementedException();
//    			SDFAttributeList predAttrToCompensateOP = SDFAttributeList.intersection(predicateAttributes, inputPattern); // Pr�dikate, die durch GIP abgedeckt sind
//		    	for(int k = 0; k<queryWeightedPredicates.getPredicateCount();k++) // alle Pr�dikate durchlaufen
//		    	{
//		    		SDFAttribute queryPredAttr = queryWeightedPredicates.getPredicate(k).getPredicate().getAttribute();
//		    		if(predAttrToCompensateOP.contains(queryPredAttr)) // wenn Predikatattribut in GIP ist..
//		    		{
//		    			// Vergleichoperator des Pr�dikats...
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
//		    			if(!queryPredOP.equals(planNextAttrOP)) // sollten die OPs nicht �bereinstimmen
//		    			{
//		    				predAttrToCompensate.addAttribute(queryPredAttr);// Attribut des Pr�dikats der Liste hinzuf�gen
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
 			// Zwei M�glichkeiten: entweder werden nur solche Pl�ne kompensiert, die alle Pr�dikate 
 			// ber�cksichtigen  => restrictiveCompensation ODER alle (weil machmal besser irgendwelche 
 			// Ergebnisse zu haben als gar keine Antwort)
    		if(predAttrToCompensate.getAttributeCount()>0 && predAttrToCompensate!=null 
    				&& ((this.restrictiveCompensation &&  SDFAttributeList.subset(predAttrToCompensate, outputPattern))
        																			|| !this.restrictiveCompensation)) 
    		{
     			// Pr�dikatenliste aus der Liste der Attribute zusammenstellen 
     			for(int j = 0; j<queryWeightedPredicates.getPredicateCount(); j++)
     			{
     				SDFAttribute predAttribute =  queryWeightedPredicates.getPredicate(j).getPredicate().getAttribute();
     				if(predAttrToCompensate.contains(predAttribute)) // enhalten?
     				{
     					predicatesToCompensate.addPredicate(queryWeightedPredicates.getPredicate(i)); // Pr�dikat hinzuf�gen
     				}
     			}
     	   		logger.debug("> Predicate Count: "+predicatesToCompensate.getPredicateCount()); 
    			// Anschliessend neuen Annotated-Plan bauen, der SelectPO integriert und die Pr�dikatenliste bekommt 
    		    try 
    		    {
    				AnnotatedPlan newPlan = getSelectPOCompensatedPlan(nextPlan, predicatesToCompensate);
    				if(newPlan!=null)
    				{
    					compensatedPlans.add(newPlan); // Plan hinzuf�gen
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
    			// evtl dem Menge der Ausschusspl�ne hinzuf�gen ==> TODO
                logger.info("Plan ist nicht kompensierbar");
    		}
    	}

    	// Evtl auch die Inputpl�ne weiterleiten
    	// compensatedPlans.addAll(context);
    	return compensatedPlans;
    }

	/**
	 * Integriert den SelectPO in einen neuen AnnotatedPlan. Der SelectPO bekommt eine Liste der Pr�dikate, 
	 * die auf den Attributen im Ergebnis der Zugriffsplans ber�cksichtigt werden sollen.
	 * @param inputAnnoPlan Eingabeplan
	 * @param predicatesToCompensate Liste der Pr�dikate, die es zu ber�cksichtigen gilt
	 * @return
	 * @throws PredicateCompensationImpossibleException
	 */
    private  AnnotatedPlan getSelectPOCompensatedPlan(AnnotatedPlan inputAnnoPlan ,  WSimplePredicateSet  predicatesToCompensate)
	throws PredicateCompensationImpossibleException
	{
	    AnnotatedPlan newPlan = new AnnotatedPlan();

	    try{
	        // Zun�chst die neuen Zugriffsmuster bestimmen:
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