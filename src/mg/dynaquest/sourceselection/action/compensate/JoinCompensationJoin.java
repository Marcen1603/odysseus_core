package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;
import java.util.HashSet;

import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.JoinPO;
import mg.dynaquest.sourcedescription.sdf.description.SDFInputPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFOutputAttributeBinding;
import mg.dynaquest.sourcedescription.sdf.description.SDFOutputPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFPattern;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperator;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFCompareOperatorFactory;
import mg.dynaquest.sourcedescription.sdf.predicate.SDFSimpleJoinPredicate;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.vocabulary.SDFPredicates;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import mg.dynaquest.sourceselection.exception.JoinCompensationImpossibleException;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author Jurij Henne
 * Setzt die Join-basierte Kompensation der Eingabepläne um.
*/

public class JoinCompensationJoin extends JoinCompensation 
{
	/**
	 * @uml.property  name="avoidEqualURIJoins"
	 */
	private boolean avoidEqualURIJoins = true;
	
    public JoinCompensationJoin(){}
	/**
	 * Initialisiert Join-Kompensation mit der Anfrage und eine spezifischen Namen(für Serialisierungszwecke)
	 * @param query Die Anfrage
	 */
    public JoinCompensationJoin(SDFQuery query)
    {
    	setQuery(query);
    }
    
	/**
	  * Die Methode versucht, Pläne paarweise an gemeinsamen Attributen im Ausgabemuster zu verknüpfen
	  *	Nähere Informationen siehe Abschnitt 4.3.1 (DA)
	 */
	public ArrayList<AnnotatedPlan> compensate(ArrayList <AnnotatedPlan> toCompensate)
    {
		 // toCompensate wird im Verlauf der Kompensation u.U. verkleinert
		ArrayList<AnnotatedPlan> compensatedPlans = new ArrayList<AnnotatedPlan>();
       	//this.context = new ArrayList<AnnotatedPlan>(toCompensate);
       	this.setErrorPlans(new ArrayList<AnnotatedPlan>(toCompensate));
       	SDFAttributeList queryRequiredAttributes = new SDFAttributeList(this.getQuery().getRequiredAttributes());
   	 	 	
    	//Muss erst alle Attribute sammeln, die in den Ausgabemustern vorkommen. Es werden nur Pläne gejoint, die gleiches Attribut in dem Ausgabemuster haben
    	HashSet <SDFAttribute> concernedAttributes = getConcernedAttributes(toCompensate);
	
    	// Zu Array convertieren, um eine feste Reihenfolge für spätere Verarbeitung zu garantieren (ein Hack^^)
    	SDFAttributeList concernedAttributesArray = new SDFAttributeList(concernedAttributes);
    	
		// Helper erstellen, jedes Element ist eine Liste von AnnotatedPlan, die über ein Attribut gejoint werden können.
		ArrayList<ArrayList> joinBuckets = getBuckets(toCompensate, concernedAttributesArray);
			
    	for(int i=0; i<joinBuckets.size(); i++){ // dient ausschliesslich den DEBUG-Zwecken
    		this.logger.debug("> Bucket  "+(i+1)+"/"+joinBuckets.size()+" / Pläne:"+joinBuckets.get(i).size());
        }
	
		// Nun Betrachten, welche Kombinationen sinn machen. Es werden nur Pläne gejoint, die gleiches Attribut in dem Ausgabemuster haben

    	for(int i=0; i<joinBuckets.size(); i++)
    	{
    		ArrayList joinPlanSet = joinBuckets.get(i);	// Liste der Pläne im aktuellen Bucket	    	
    		if(joinPlanSet!=null && joinPlanSet.size()>1) //die Liste ist nicht leer und sind mind 2 Pläne drin
    		{
				SDFAttribute joinAttribute = concernedAttributesArray.getAttribute(i); // darf den gleichen counter benutzen, da dieselbe länge

				//nun versuchen, die Pläne paarweise zu joinen. Versuchen alle Permutationen der Pläne im Bucket
   	    		this.logger.debug("> JOIN-Attribut:  "+joinAttribute);

				for(int n=0; n<joinPlanSet.size(); n++){ // dient ausschliesslich den DEBUG-Zwecken
    	    		this.logger.debug("> Bucket-Plan ["+((AnnotatedPlan)joinPlanSet.get(n)).getIndex()+"] / GIP:"+((AnnotatedPlan)joinPlanSet.get(n)).getGlobalInputPattern()+"/GOP:"+((AnnotatedPlan)joinPlanSet.get(n)).getGlobalOutputPattern());
                }
                          
				for(int j=0; j<joinPlanSet.size(); j++) 
		    	{
    		    	for(int k=j+1; k<joinPlanSet.size(); k++) 
    		    	{
    		    		
    		    		AnnotatedPlan leftPlan = (AnnotatedPlan)joinPlanSet.get(j); 
    		    		AnnotatedPlan rightPlan = (AnnotatedPlan)joinPlanSet.get(k);
    		    		SDFCompareOperator compOP = SDFCompareOperatorFactory.getCompareOperator(SDFPredicates.Equal);
    		    		
    		    		
    		    		SDFSimpleJoinPredicate joinPredicate = new SDFSimpleJoinPredicate("JPred:"+System.currentTimeMillis(),
								    		    									joinAttribute, 
								    		    									compOP, 
																					joinAttribute);
    		    		
    					SDFOutputAttributeBinding leftPlanOutputAttributeBinding = getOutputAttributeBinding(leftPlan, joinAttribute);
    					SDFOutputAttributeBinding rightPlanOutputAttributeBinding = getOutputAttributeBinding(rightPlan, joinAttribute);
           				logger.debug("> ID-CHECK >Pattern: "+leftPlanOutputAttributeBinding+">BINDING>"+rightPlanOutputAttributeBinding+">");

    		    		if( leftPlanOutputAttributeBinding.isIdAttribute() || rightPlanOutputAttributeBinding.isIdAttribute()){		           				
		    		    	try{
                                logger.debug("Versuche Pläne zu joinen");
                                AnnotatedPlan newPlan = getJoinCompensatedPlan(leftPlan,rightPlan,joinPredicate);
                                logger.debug("Neuer Plan "+newPlan);
	           					SDFAttributeList planGOP = newPlan.getGlobalOutputPattern().getAllAttributes();
                                logger.debug("Notwendige Attribute "+queryRequiredAttributes+" --> neue Attribute "+planGOP);

		           		    	if(newPlan!=null && 
		           		    			((!this.generatePartialPlans && SDFAttributeList.subset(queryRequiredAttributes, planGOP))
		           		    				|| this.generatePartialPlans)  )
		           		    	{
			           		    		compensatedPlans.add(newPlan);
			           		    		logger.debug("> Join successfull > NewPlan  ["+newPlan.getIndex()+"]("+leftPlan.getIndex()+"<->"+rightPlan.getIndex()+"):GIP"+newPlan.getGlobalInputPattern()+" /GOP"+newPlan.getGlobalOutputPattern());	    		    	
			           		    		this.getErrorPlans().remove(leftPlan);
			           		    		this.getErrorPlans().remove(rightPlan);
		           		    	}
		    		         }
		    		         catch(JoinCompensationImpossibleException e)
		    		         {
		           		    		logger.fatal("> Join failed > ("+leftPlan.getIndex()+"<->"+rightPlan.getIndex()+")");	    		    	
		    		         }  
    		    		} 
    		    		else
    		    		{
    		    			if(!this.getErrorPlans().contains(leftPlan))
    		    				this.getErrorPlans().add(leftPlan);
    		    			if(!this.getErrorPlans().contains(rightPlan))
    		    				this.getErrorPlans().add(rightPlan);
    		    		}
    		    	//	this.logger.debug(actionName+"> Join erfolgreich(FAKE)  "+(j+1)+"<->"+(k+1)+") on "+ joinAttribute.getQualName(), 3);

    		    	}	
		    	}
    		}	 		
    	}
    	// Nur kompensierte Pläne zurückgeben, nicht kompensierte Päne kann der PO über die getErrorPlans holen
   		this.logger.debug("> Pläne übergeben: "+compensatedPlans.size());

    	return compensatedPlans;
    }
    
	/**
	 * Liefert die Attributbindung für ein Ausgabeattribut eines Zugriffsplans.
	 * @param annoPlan Zuggriffsplan, welches das Ausgabeattribut enthält
	 * @param attribute Attrbut, welches betrachtet werden soll.
	 * @return
	 */
	private SDFOutputAttributeBinding getOutputAttributeBinding(AnnotatedPlan annoPlan, SDFAttribute attribute)
	{
		// VORSICHT: WERDEN NUR OUTPUT_AttributeBinding BETRACHTET, sindoftsmals nicht vorhandne daher liefert null
		SDFPattern annoPlanGOP = annoPlan.getGlobalOutputPattern();
		SDFOutputAttributeBinding planOutputAttributeBinding = (SDFOutputAttributeBinding)annoPlanGOP.getAttributeAttributeBindingPair(attribute).getAttributeBinding();
		return planOutputAttributeBinding;


	}
	
	/**
	 * Sammelt alle Attribute zusammen, die in den Ausgabemustern der Eingabepläne vorkommen. 
	 * @param toCompensate Liste der zu kompensierenden Eingabepläne
	 * @return Ein Hashset an Ausgabeattributen ohne Dupletten
	 */
    private HashSet <SDFAttribute> getConcernedAttributes(ArrayList toCompensate)
    {
    	HashSet<SDFAttribute> concernedAttributes = new HashSet<SDFAttribute>();
    	for(int i=0; i<toCompensate.size(); i++)
    	{
    		AnnotatedPlan nextPlan = (AnnotatedPlan)toCompensate.get(i);
    		SDFAttributeList outputAttributes =  nextPlan.getGlobalOutputPattern().getAllAttributes();
    		for(int j=0; j<outputAttributes.size(); j++)
    		{   	
    			concernedAttributes.add(outputAttributes.getAttribute(j));
    		}
    	}
    	return concernedAttributes;  	
    }
    
    /**
     * Sammelt für jedes Attribute aus concernedAttributes Pläne in je einem neuen Bucket.
     * @param toCompensate Liste der Eingabepläne
     * @param concernedAttributes Liste der Attribute, die die Anzahl der Buckets vorgeben
     * @return Eine Menge von Buckets
     */
    private  ArrayList<ArrayList> getBuckets(ArrayList<AnnotatedPlan> toCompensate,  SDFAttributeList concernedAttributes)
    {
		ArrayList<ArrayList> joinBuckets = new ArrayList<ArrayList>();
		
		for(int i=0; i<concernedAttributes.size(); i++) // Für jedes Attribut aus der Liste (concernedAttributes)
    	{
			SDFAttribute concernedJoinAttribute = concernedAttributes.getAttribute(i); // Dieses Attribut wird gerad betrachtet
			
			//Hier kommen Annotated zusammen, die das betrachtete Attribut im GOS enthalten(Umsetzung der Buckets)
			ArrayList<AnnotatedPlan> joinPlanSet = new ArrayList<AnnotatedPlan>();	
			this.logger.debug("> Next Bucket-Attribute: "+concernedJoinAttribute.toString());
			
			//alle Annotated durchlaufen und nach dem aktuellen Ausgabeattribut suchen
			for(int j=0; j<toCompensate.size(); j++) 
	    	{			
	    		AnnotatedPlan nextPlan = (AnnotatedPlan)toCompensate.get(j); // da haben wir einen Plan
	    		SDFAttributeList outputAttributes =  nextPlan.getGlobalOutputPattern().getAllAttributes(); // seine Ausgabeattribute
	    		if(outputAttributes.contains(concernedJoinAttribute)) //Bucketattribut im Ausgabemuster?	  
	    		{
	    			if(joinPlanSet.size()==0) // Müssen  ersten Plan immer reinschreiben, nur dann kann ELSE greifen
	    			{
	    				this.logger.debug("> Bucket-Attribute in GOP of Plan  ["+nextPlan.getIndex()+"]:"+(j+1)+"/"+toCompensate.size()+"> GOP:"+((AnnotatedPlan)toCompensate.get(j)).getGlobalOutputPattern());
    		    		joinPlanSet.add(nextPlan);
	    			}
	    			else
	    				if(this.avoidEqualURIJoins)
	    				{
		    				if(!nextPlan.inSetWithSameGOP(joinPlanSet)) // ab dem zweiten Plan(sofern dieser existiert) wird hier gemacht
		    				{
		    					this.logger.debug("> Bucket-Attribute in GOP of Plan  ["+nextPlan.getIndex()+"]: (avoid)"+(j+1)+"/"+toCompensate.size()+"> GOP:"+((AnnotatedPlan)toCompensate.get(j)).getGlobalOutputPattern());
			    				joinPlanSet.add(nextPlan);
			    	    	}
		    				else
		     					this.logger.debug("> EQUAL GOP EXISTS,  Plan ["+nextPlan.getIndex()+"]:"+(j+1)+"/"+toCompensate.size()+"> GOP:"+((AnnotatedPlan)toCompensate.get(j)).getGlobalOutputPattern());

	    				}
	    				else 
	    				{
	    					this.logger.debug("> Bucket-Attribute in GOP of Plan ["+nextPlan.getIndex()+"] (allowed):"+(j+1)+"/"+toCompensate.size()+"> GOP:"+((AnnotatedPlan)toCompensate.get(j)).getGlobalOutputPattern());
		    				joinPlanSet.add(nextPlan);

	    				}
	   
	    		}
	    		else
	    		{
					this.logger.debug("> Bucket-Attribute NOT in GOP of Plan  ["+nextPlan.getIndex()+"]:"+(j+1)+"/"+toCompensate.size()+"> GOP:"+((AnnotatedPlan)toCompensate.get(j)).getGlobalOutputPattern()+" ####");
	    			if(!this.getErrorPlans().contains(nextPlan))
	    				this.getErrorPlans().add(nextPlan);
	    		}

	    	}
			// Auch leere Buckets aufnehmen, damit die Zuordnung zum concernedAttribut erhalten bleibt
			// Natürlich hätte man auch eine Map umsetzen können, aber die Handhabung wäre unnötig komplizierter 
			// => irgendwo auch eine Glaubensfrage, but.. we love ArrayLists
			joinBuckets.add(joinPlanSet);
		}
		
		return joinBuckets;
    }
    
    /**
     * Integriert den Algebra-Planoperator JoinPO in den kompensierten Zugriffsplan
     * @param annoPlanLeft  Linker Eingabeplan
     * @param annoPlanRight Rechter Eingabeplan
     * @param joinPredicate Verknüpfungsprädikat
     * @return neu generierter Zugriffsplan, welches einen JoinPO integriert
     * @throws JoinCompensationImpossibleException
     */
    private  AnnotatedPlan getJoinCompensatedPlan(AnnotatedPlan annoPlanLeft, AnnotatedPlan annoPlanRight, SDFSimpleJoinPredicate joinPredicate)
	throws JoinCompensationImpossibleException
	{    
	    AnnotatedPlan newPlan = new AnnotatedPlan();
	    try{
	        // Zunächst das neue Zugriffsmuster bestimmen
	        SDFInputPattern leftInputPattern = new SDFInputPattern(annoPlanLeft.getGlobalInputPattern());
	        SDFInputPattern rightInputPattern = new SDFInputPattern(annoPlanRight.getGlobalInputPattern());

	       // this.logger.debug(actionName+"> VOR UNION GIP (left) ["+annoPlanLeft.getIndex()+"]:/ GIP:"+annoPlanLeft.getGlobalInputPattern(), 3);
	       // this.logger.debug(actionName+"> VOR UNION GIP (right) ["+annoPlanRight.getIndex()+"]:/ GIP:"+annoPlanRight.getGlobalInputPattern(), 3);
	        SDFInputPattern newInputPattern = SDFInputPattern.union(leftInputPattern,rightInputPattern);
	       //SDFPattern newInputPattern = rightInputPattern;
	        
	        SDFOutputPattern leftOutputPattern = annoPlanLeft.getGlobalOutputPattern();
	        SDFOutputPattern rightOutputPattern = annoPlanRight.getGlobalOutputPattern();
	       // this.logger.debug(actionName+"> VOR UNION GIP (left) ["+annoPlanLeft.getIndex()+"]:/ GIP:"+annoPlanLeft.getGlobalOutputPattern(), 3);
	       // this.logger.debug(actionName+"> VOR UNION GIP (right) ["+annoPlanRight.getIndex()+"]:/ GIP:"+annoPlanRight.getGlobalOutputPattern(), 3);
	       SDFOutputPattern newOutputPattern = SDFOutputPattern.union(leftOutputPattern, rightOutputPattern);
	       // SDFPattern newOutputPattern = rightOutputPattern;
	       
	        // Diese auf den neuen Plan übertragen
	        newPlan.setGlobalInputPattern(newInputPattern);
	        newPlan.setGlobalOutputPattern(newOutputPattern);
	        
	        AlgebraPO leftPlan = annoPlanLeft.getAccessPlan();
	        AlgebraPO rightPlan = annoPlanRight.getAccessPlan();

	        // Algebra-PO integrieren
	        JoinPO join = new JoinPO(joinPredicate);
            join.setPOName("JOIN-PO "+System.currentTimeMillis());
	        join.setLeftInput(leftPlan);
	        join.setRightInput(rightPlan);
	        join.setOutElements(newOutputPattern.getAllAttributes());
	        
	        newPlan.setAccessPlan(join);
	        
	        // Beteiligte Pläne mit auf den Weg geben
	        newPlan.addBasePlan(annoPlanLeft);
	        newPlan.addBasePlan(annoPlanRight);            
	        
	    }
	    catch(Exception e)
	    {
            e.printStackTrace();
	        throw new JoinCompensationImpossibleException("Integration des JoinPO in neuen Plan fehlgeschlagen!");
	      
	    }
	    
	    return newPlan;
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
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("avoidEqualURIJoins")) 
				this.avoidEqualURIJoins = (Integer.parseInt(cNode.getFirstChild().getNodeValue()) != 0);
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("generatePartialPlans")) 
				this.generatePartialPlans = (Integer.parseInt(cNode.getFirstChild().getNodeValue()) != 0);	
		}
	}
    
	/*
	 *  (non-Javadoc)
	 * @see mg.dynaquest.sourceselection.action.CompensateAction#getInternalXMLRepresentation(java.lang.String, java.lang.String, java.lang.StringBuffer)
	 */
    public  void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue) 
    { 	
    	xmlRetValue.append(baseIndent + "<avoidEqualURIJoins>");
    	xmlRetValue.append((this.avoidEqualURIJoins)?1:0);
    	xmlRetValue.append("</avoidEqualURIJoins>\n");
 
    	xmlRetValue.append(baseIndent + "<generatePartialPlans>");
    	xmlRetValue.append((this.generatePartialPlans)?1:0);
    	xmlRetValue.append("</generatePartialPlans>\n");
    }
    
    
    
    public static void main(String[] args) {
    }
}
