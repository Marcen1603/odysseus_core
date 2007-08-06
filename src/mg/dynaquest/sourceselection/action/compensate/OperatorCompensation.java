package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;
import java.util.Map;

import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorList;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimplePredicate;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.query.WSimplePredicateSet;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import mg.dynaquest.sourceselection.action.compensate.compareoperatorcompensationmethods.CompareOpCompMethod;
import mg.dynaquest.sourceselection.action.compensate.compareoperatorcompensationmethods.CompareOpCompMethodFactory;

import org.w3c.dom.NodeList;

/**
 * Diese Klasse ist eine CompensateAction f�r die Vergleichsoperatorkompensation.
 * 
 * @author  Marco Grawunder, Benjamin Reyels
 */
public class OperatorCompensation extends AbstractCompensateAction  {
	
//	protected ArrayList <AnnotatedPlan> compensatedPlans = null; // Pl�ne die im Verlauf des Kompensationsprozess entstanden sind
//	protected ArrayList <AnnotatedPlan> context = null;			 // Pl�ne zu kompensieren	
	
	public OperatorCompensation(){}

	
	public OperatorCompensation(SDFQuery query)
	{
    	this.setQuery(query);
	}

	
	/* (non-Javadoc)
     * @see mg.dynaquest.sourceselection.action.CompensateAction#compensate(java.util.ArrayList)
     */
	public ArrayList <AnnotatedPlan> compensate(ArrayList <AnnotatedPlan> toCompensate) {
		
		//Variablen f�r kompensierte Pl�ne und ErrorPlans anlegen bzw. initialisieren 
		ArrayList<AnnotatedPlan> compensatedPlans = new ArrayList <AnnotatedPlan>();
		this.setErrorPlans(new ArrayList <AnnotatedPlan>(toCompensate));
    	
		//F�r jeden zu kompensierenden Plan wird jedes Pr�dikat der Query 
		//auf Ausf�hrbarkeit getestet.
		WSimplePredicateSet predicates=this.getQuery().getQueryPredicates();
    	for(int i=0; i<toCompensate.size(); i++){
    		AnnotatedPlan currentPlan = toCompensate.get(i);
    		boolean planCompensated = true;
    		for(int j=0; j<predicates.getPredicateCount(); j++){
    			// Wenn das Pr�dikat nicht von dem Plan anwendbar ist, so wird
    			// versucht zu kompensieren.	
    			SDFSimplePredicate currentPred = predicates.getPredicate(j).getPredicate();
    			
    			if(!currentPlan.canProcess(currentPred)){
    				//Alle Vergleichsoperatoren des Plans f�r das Attribut holen
    				//und zur Liste hinzuf�gen
    				//ACHTUNG: Mit der Map ist es nicht m�glich einem Attribut mehrere
    				//		   Vergleichsoperatoren zuzuordnen, deswegen besteht die Liste
    				//         nur aus einem Vergl.Op.
    				//		   Wenn sich das �ndert, muss die Liste nat�rlich anders gef�llt 
    				//         werden
    				Map<SDFAttribute,SDFCompareOperatorList> compMap = currentPlan.getGlobalInputPattern().getAttributeCompareOperatorAssignment();
    				SDFCompareOperatorList apCompareOps = new SDFCompareOperatorList();
    				apCompareOps.addAll(compMap.get(currentPred.getAttribute()));
    				
    				//Die m�glichen Kompensations-Methoden generieren lassen (Factory)
    				ArrayList <CompareOpCompMethod> compMethods = CompareOpCompMethodFactory.createMethods(currentPlan, apCompareOps, currentPred, this.getQuery());
    				boolean predicateCompensated = false;
    				
    				//Alle Methoden testen. Sollte eine erfolgreich sein, ist das
    				//Pr�dikat, aber noch nicht der Plan, kompensiert.
    				for(int k=0; k < compMethods.size(); k++){
    					AnnotatedPlan newPlan = compMethods.get(k).compensate();
    					if(newPlan != null){
    						currentPlan=newPlan;
    						predicateCompensated = true;
    						break;
    					}    						
    				}
    				
    				//Sollte keine Methode erfolgreich gewesen sein, so ist das
    				//Pr�dikat nicht zu kompensieren und damit auch der Plan nicht.
    				if(!predicateCompensated){
    					planCompensated=false;
    					break;
    				}
    			}
    		}
    		//Wenn das Flag immer noch auf true gesetzt ist, sind alle 
    		//zu kompensierenden Pr�dikate erfolgreich kompensiert worden
    		//und der Plan kann doch noch verwendet werden.
    		if(planCompensated){
    			compensatedPlans.add(currentPlan);
    			this.getErrorPlans().remove(toCompensate.get(i));
    		}
    	}
    	
        return compensatedPlans;
    }

	public void initInternalBaseValues(NodeList children) 
	{
			//TODO
	}
    
    public  void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue) 
    {
    		//TODO
    }
    
	public static void main(String[] args) {
    }
}
