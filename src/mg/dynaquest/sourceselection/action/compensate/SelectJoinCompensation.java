package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.SelectJoinPO;
import mg.dynaquest.sourcedescription.sdf.description.SDFInputPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFOutputPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFPattern;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimpleJoinPredicate;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import mg.dynaquest.sourceselection.exception.JoinCompensationImpossibleException;
import mg.dynaquest.sourceselection.exception.SelectJoinCompensationImpossibleException;


/**
 * @author  Marco Grawunder
 */
abstract public class SelectJoinCompensation extends JoinCompensation
{
	/**
	 * Attribute, die durch die Prädikate der Anfrage belegt sind
	 * @uml.property  name="predicateAttributes"
	 * @uml.associationEnd  
	 */
	//protected SDFAttributeList predicateAttributes = null;  
	
	/**
	 * Die Kostengrenze für jedes Zugriffsplan. Setzt ein gültiges Kostenmodell voraus.
	 * @uml.property  name="m"
	 */
	protected double M = 0; //1E300 * 1E20 ; // Unendlich?  alternativ 0.0/0.0
	/**
	 * Legt fest, wie ob der kompensierter Plan  eine minimal notwendige Menge von Eingabeplänen verknüpft oder alle. false = Alle Pläne, die für eine Ausführungsreihenfolge ausgewählt wurden, werden miteinander verknüpft.  Dies kann bedeuten, dass einige Pläne nichts zum Ergebnis der Anfrage beitragen können, weil bereits bestehende Ausgabeattribute mitbringen. true = Sobald gewünschter Ausgabemuster von den verknüpften Plänen abgedeckt ist, wird der generierte Plan als kompensiert übergeben.
	 * @uml.property  name="searchMinimalPlan"
	 */
	protected boolean searchMinimalPlan = true;
	/**
	 * Ist costControlled true, dann wird ein einziger Plan generiert. Dieser Plan entspricht genau der Ausgabe eines CHAIN-Algorithmus (GREEDY-Aproach),  indem die günstigsten Pläne immer zuerst ausgeführt werden Ist constControlled false, dann wird eine Permutation der Cluster-Inhalte generiert und als Menge der kompensierten Pläne zurückgegeben.
	 * @uml.property  name="costControlled"
	 */
	protected boolean costControlled = false; 
	
	/*
	 *  (non-Javadoc)
	 * @see mg.dynaquest.sourceselection.action.CompensateAction#compensate(java.util.ArrayList)
	 */
	abstract public ArrayList <AnnotatedPlan> compensate(ArrayList <AnnotatedPlan> toCompensate);
	
	/**
	 * Integriert den Algebra-Planoperator (SelectJoin) in einen neuen Plan. Der PO verküpft dabei zwei Eingabepläne(Teilziele)
	 * zu einem neuen Plan.
	 * @param masterPlan Master-Plan, wird immer zuerst ausgeführt
	 * @param slavePlan  Slave-Plan, kann nur mit bestimmten Belegungen ausgeführt werden, die Masterplan liefert
	 * @param joinPred Mit dem Join-Prädikat werden beide Pläne anschliessend verknüpft
	 * @return neu generierter Zugriffsplan, welches einen JoinPO integriert
	 * @throws JoinCompensationImpossibleException
	 */
    protected  AnnotatedPlan getSelectJoinedPlan(AnnotatedPlan masterPlan, AnnotatedPlan slavePlan)
	throws SelectJoinCompensationImpossibleException
	{
	    AnnotatedPlan newPlan = new AnnotatedPlan();

	    try{
	        // Zunächst das neue Zugriffsmuster bestimmen:
	        SDFInputPattern masterInputPattern = masterPlan.getGlobalInputPattern();
	        SDFInputPattern slaveInputPattern = slavePlan.getGlobalInputPattern();
	        
	        SDFPattern newInputPatternPRE = SDFInputPattern.union(masterInputPattern,slaveInputPattern);
	        
	        // SelectJoin-Atribute
	        SDFAttributeList selJoinAttributes = getSelectJoinAttributes(masterPlan, slavePlan);
     		
	        StringBuffer sb = new StringBuffer();
    		for(int l=0; l<selJoinAttributes.size(); l++) 	 // NUR DEBUG
   			sb.append(" "+selJoinAttributes.get(l));
    		logger.debug("> SelectJoin Attributes {"+sb+" }");
		    	 
	        //Attribute aus dem GIP entfernen die als SELECT_JOIN_Attribs dienen
	        SDFInputPattern newInputPattern = new SDFInputPattern();
	    	for(int i = 0; i<newInputPatternPRE.getAttributeAttributeBindingPairCount(); i++)
	    	{
	    		SDFAttribute nextAttr = newInputPatternPRE.getAttributeAttributeBindingPair(i).getAttribute();
	    		if(!selJoinAttributes.contains(nextAttr))
	    			newInputPattern.addAttributeAttributeBindingPair(newInputPatternPRE.getAttributeAttributeBindingPair(i));
	    	}
	        	        
	        
      
	        SDFOutputPattern leftOutputPattern = masterPlan.getGlobalOutputPattern();
	        SDFOutputPattern rightOutputPattern = slavePlan.getGlobalOutputPattern();
	        
	        SDFOutputPattern newOutputPattern = SDFOutputPattern.union(leftOutputPattern, rightOutputPattern);
	        
	        newPlan.setGlobalInputPattern(newInputPattern);
	        newPlan.setGlobalOutputPattern(newOutputPattern);
	        
	        AlgebraPO leftPlan = masterPlan.getAccessPlan();
	        AlgebraPO rightPlan = slavePlan.getAccessPlan();
	        
	        SDFSimpleJoinPredicate selJoinPred = getJoinPredicate(masterPlan, slavePlan);
			//logger.debug(">  SELJOINPRED "+selJoinPred.toString()+" ####", 3);
			 	        
	        
	        SelectJoinPO join = new SelectJoinPO(selJoinAttributes, selJoinPred);       
	        join.setLeftInput(leftPlan);
	        join.setRightInput(rightPlan);
            join.setPOName("SelectJoinPO "+System.currentTimeMillis());
	        
	        join.setOutElements(newOutputPattern.getAllAttributes());
	        
	        newPlan.setAccessPlan(join);
	        newPlan.addBasePlan(masterPlan);
	        newPlan.addBasePlan(slavePlan);
	        
	    }
	    catch(Exception e)
	    {
	        throw new SelectJoinCompensationImpossibleException(e.getMessage());
	    }
    return newPlan;
}
    
    /**
     * Ermittelt das erste mögliche Join-Prädikat und liefert es zurück. Das erste gefundene ist ok, weil
     * es nicht wichtig ist, auf welchem Attribut die Pläne gejoint werden. TODO: andere Theorien?
     * 
     * @param masterPlan 
     * @param slavePlan 
     * @return 
     */
    protected  SDFSimpleJoinPredicate getJoinPredicate(AnnotatedPlan masterPlan, AnnotatedPlan slavePlan)
    {
    	//Attribute des Ausgabemusters vom dem masterPlan holen
    	SDFAttributeList masterOutAttributes = masterPlan.getGlobalOutputPattern().getAllAttributes(); 
    	// Attribute des Ausgabemusters vom dem masterPlan holen
    	SDFAttributeList slaveOutAttribute = slavePlan.getGlobalOutputPattern().getAllAttributes(); 
	   	// Schnittmenge zwischen den beiden berechnen, ausgehend von den masterOutAttributes
    	for(int i = 0; i<masterOutAttributes.getAttributeCount(); i++)
    	{
    		SDFAttribute nextOutAttr = masterOutAttributes.getAttribute(i);
    		if(slaveOutAttribute.contains(nextOutAttr))
    		{
    			logger.debug("> Join -Predicate: "+nextOutAttr);
    	    	return  new SDFSimpleJoinPredicate("JPO:"+System.currentTimeMillis(), 
    	    			nextOutAttr, 
    						SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Equal), 
    						nextOutAttr); 	
    		}
    	}
    	
    	return null; // nichts gefunden(sollte im normalfall nicht vorkommen)
    }
    
    /**
     *  Sucht Attribute, welche in der Ausgabe des Masters liegen und als Eingabe für Slave dienen sollen.
     *  Sind Master und Slave demselben Cluster zugeordnet, dann muss dieses Attribut nicht existieren. 
     *  Ansonsten ist bedingt durch die Umsetzung immer ein solcher gewährleistet.
     */
    protected  SDFAttributeList getSelectJoinAttributes(AnnotatedPlan masterPlan, AnnotatedPlan slavePlan)
    {
    	//Aufgabe: Attribute im GIP des Slave suchen, die
    	// 1. Durch die predicateAttributes nicht abgedeckt werden UND notwendig(r) sind
    	// 2. Durch das GOP des Masters abgedeckt werden
    	 	
    	//Attribute des Ausgabemusters vom dem masterPlan holen
    	SDFAttributeList masterOutAttributes = masterPlan.getGlobalOutputPattern().getAllAttributes(); 
    	// GIP des masterPlans
    	SDFAttributeList masterInAttributes = masterPlan.getGlobalInputPattern().getAllAttributes();
    	//Required-Eingabeattribute von den slavePlan holen
    	SDFAttributeList slaveReqInAttributes = slavePlan.getRequiredInAttributes(); 
    	//Alle Attribute, die durch Query abgedeckt werden
    	SDFAttributeList queryPredAttributes = this.getQuery().getPredicateAttributes();
    	
    	SDFAttributeList selJoinAttributes = new SDFAttributeList();
	   	// Schnittmenge zwischen den beiden berechnen, ausgehend von den masterOutAttributes
    	for(int i = 0; i<masterOutAttributes.getAttributeCount(); i++)
    	{
    		SDFAttribute nextOutAttr = masterOutAttributes.getAttribute(i);
    		// Ist ein Attribut im GOP des Masters nicht durch die Prädikate der Query abgedeckt, jedoch im
    		// das GIP des Slaves liegt, dass ist es das gesuchte....
    		if(!queryPredAttributes.contains(nextOutAttr) && slaveReqInAttributes.contains(nextOutAttr))
    		{
    			if(!masterInAttributes.contains(nextOutAttr)) // GIP des Masterplans darf das Attribut nicht enthalten
    				selJoinAttributes.add(nextOutAttr);
    		}
    	}
    	
    	return selJoinAttributes;
    }
    
}

