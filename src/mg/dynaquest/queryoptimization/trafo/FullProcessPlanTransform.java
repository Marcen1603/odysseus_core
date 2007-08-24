/*
 * Created on 01.02.2005
 *
 */
package mg.dynaquest.queryoptimization.trafo;

import java.util.ArrayList;
import java.util.List;

import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.SupportsCloneMe;
import mg.dynaquest.queryexecution.po.algebra.TopPO;
import mg.dynaquest.queryexecution.po.base.PlanOperator;
import mg.dynaquest.queryoptimization.trafo.rules.TransformationNotApplicableExeception;
import mg.dynaquest.queryoptimization.trafo.rules.TransformationRule;

/**
 * Diese Klasse wandelt einen logischen Plan Top-Down in eine Menge
 * von physischen Plänen um. Bei jedem Algebra-Operator werden dabei
 * sämtliche Transformationsregeln angewandt 
 * @author Marco Grawunder
 *	
 *
 *
 */

public abstract class FullProcessPlanTransform extends ProcessPlanTransform {

    /* (non-Javadoc)
     * @see mg.dynaquest.queryoptimization.trafo.ProcessPlanTransform#transform(mg.dynaquest.queryexecution.po.base.NAryPlanOperator)
     */
    
	public List<PlanOperator> transform(AlgebraPO logicalPlan) {

		// Menge aller noch zu verarbeitenden Pläne, Pläne können sowohl logische 
		// als auch physische Operatoren enthalten
    	List<TopPO> allPlans = new ArrayList<TopPO>();
    	// Menge der vollständig transformierten Pläne
    	List<PlanOperator> transPlans = new ArrayList<PlanOperator>();
    	// Eindeutige Identifizierung des obersten Knotens eines Plans
    	TopPO top = new TopPO();
    	top.setInputPO(logicalPlan);
    	// Und diese Plan (zunächst als einzigen) als zu verarbeiten kennzeichnen
    	allPlans.add(top);
    	
    	// Solange nicht alle Pläne gewandelt sind
    	while (allPlans.size() > 0){
    		// Den ersten Plan entfernen
    		AlgebraPO p = allPlans.remove(0);
    		// Zu bearbeitenden Operatoren finden (ohne logische Kinder)
    		FatherChildConnection con = getNextLogicalOperatorWithoutLogicalChildren(p);
    		System.out.println(con.father+" "+con.port+" "+con.child);
    		
    		// Wieviele Transformationen können angewendet werden?
    		TransformationRule trafoRule = getTrafoRule(con.child);
    		int trafoCount = trafoRule.getNoOfTransformations();
    		
    		ArrayList<AlgebraPO> plans = new ArrayList<AlgebraPO>();
    		plans.add(p);
    		// Den Plan trafoCount oft -1 clonen
    		for (int i=0;i<trafoCount-1; i++){
    			plans.add((AlgebraPO)cloneTree(top));
    		}
    		
    		// Jetzt sind in plans trafoCount Pläne drin
    		
    		for (int i=0;i<trafoCount; i++){
    			AlgebraPO currentPlan = plans.remove(0);
    			FatherChildConnection con2 = getNextLogicalOperatorWithoutLogicalChildren(currentPlan);
    			PlanOperator newPO;
				try {
					newPO = trafoRule.transform(con2.child, i); /* transformiert logischen PlanOperator in physischen Wrapper */
				} catch (TransformationNotApplicableExeception e) {
					// Es sind nicht immer alle Transformationen anwendbar 
					// dann einfach weiter machen
					continue;
				}
    			// Nur wenn der zu behandelnde Knoten nicht der oberste (Top-Knoten) ist
    			// muss der transformierte Teilplan (besteht nur aus Planoperatoren!) 
    			// dem Vater zugeordnet werden
    			// Ansonsten ist man mit der Transformation dieses Planes fertig
    			if (!(con2.father instanceof TopPO)){
    				if(con2.father instanceof AlgebraPO){
    					((AlgebraPO)con2.father).setPhysInputPO(con2.port, newPO);
    					// Achtung den alten ggf. löschen
    					((AlgebraPO)con2.father).setInputPO(con2.port, null);
    				}else{ // PlanOperator
    					((PlanOperator)con2.father).setInputPO(con2.port, newPO);
    				}
    				// Diesen teilgewandelten Plan nun in die Menge der zu verarbeitenden
    				// Pläne einfügen (der oberste Operator ist dabei immer der TopOperator)
    				allPlans.add(((TopPO)currentPlan));
    			}else{
    				transPlans.add((PlanOperator)newPO);
    			}		
    		} 		
    	} // solange noch Pläne zu verarbeiten sind
        return transPlans;
    }
    
    
    
	private FatherChildConnection getNextLogicalOperatorWithoutLogicalChildren(AlgebraPO plan) {
    	
		FatherChildConnection ret = null;
		
		AlgebraPO currentNode = plan;
		AlgebraPO nextNode = plan;
		AlgebraPO lastNode = null;
		int port = -1;
		boolean cont = true;
		
		while (cont){
			lastNode = currentNode;
			currentNode = nextNode;
			boolean found = false;
			for (int i=0;i<currentNode.getNumberOfInputs() && !found;i++){
				if ((nextNode = currentNode.getInputPO(i)) != null){
					found = true;
					port = i; // Weg merken
				}
			}
			if (found == false){
				ret = new FatherChildConnection();
				ret.father = lastNode;
				ret.child = currentNode;
				ret.port = port;
				cont = false;
			}						
		}
		
		return ret;
        			
	}
	
		
}

/**
 * @author  Marco Grawunder
 */
class FatherChildConnection{
	/**
	 * @uml.property  name="father"
	 * @uml.associationEnd  
	 */
	SupportsCloneMe father;
	/**
	 * @uml.property  name="child"
	 * @uml.associationEnd  
	 */
	SupportsCloneMe child;
	/**
	 * @uml.property  name="port"
	 */
	int port;
}
