package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;
import java.util.HashMap;

import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import mg.dynaquest.sourceselection.RepositoryManager;

/**
 * Die CompensateAction versucht die fehlenden Belegungen für notwendige EingabeAttribute mit Hilfe des
 * RepositoryManagers als Liste der Konstanten zu ermitteln. RepositoryManager berechnet die Belegungen aus den
 * Angaben zu einem Wert-Intervall
 * @author Jurij Henne
 *
 */
public class NecInAttrCompInterval extends NecInAttrCompensation {

	public NecInAttrCompInterval(){}
	/**
	 * Initialisiert die CompensateAction mit der Anfrage.
	 * @param query Nutzeranfrage
	 */
	public NecInAttrCompInterval(SDFQuery query)
	{
    	this.setQuery(query);
	}
	
    public void setQuery(SDFQuery query)
    {
		super.setQuery(query);
		this.rm = new RepositoryManager();
    }
	
    /* (non-Javadoc)
     * @see mg.dynaquest.sourceselection.action.CompensateAction#compensate(java.util.ArrayList)
     **/
	public ArrayList <AnnotatedPlan> compensate (ArrayList <AnnotatedPlan> toCompensate)  
	{
		ArrayList<AnnotatedPlan> compensatedPlans = new ArrayList<AnnotatedPlan>();
	   	//this.context = new ArrayList<AnnotatedPlan>(toCompensate);
	   	this.setErrorPlans(new ArrayList<AnnotatedPlan>(toCompensate));
		SDFAttributeList predicateAttributes = new SDFAttributeList(this.getQuery().getPredicateAttributes()); 
	   	
		// Für jeden Plan wird nun die zu kompensierende Menge von Eingabeattributen ermitteln
		for(int i=0; i<toCompensate.size(); i++)
		{
			AnnotatedPlan nextPlan = (AnnotatedPlan)toCompensate.get(i);
			this.logger.debug("> Plan to compensate ["+nextPlan.getIndex()+"]> GIP:"+nextPlan.getGlobalInputPattern()+"/ GOP:"+nextPlan.getGlobalOutputPattern());
			SDFAttributeList inputPattern = new SDFAttributeList(nextPlan.getGlobalInputPattern().getRequiredInAttributes()); // wir benötigen nur die REQUIRED
			SDFAttributeList attrToCompensate = SDFAttributeList.difference(inputPattern, predicateAttributes); // Differenz berechnen
			
	 		StringBuffer sb = new StringBuffer();
	 		for(int l=0; l<attrToCompensate.getAttributeCount(); l++)	 // NUR DEBUG
				sb.append(" "+attrToCompensate.getAttribute(l));
			logger.debug("> Attributes to compensate (#"+attrToCompensate.getAttributeCount()+") {"+sb+" }");
	
		
			// Alle Attribute durchlaufen und Belegungen(ConstantenListen) zuordnen
			HashMap <SDFAttribute, SDFConstantList> compList = new HashMap <SDFAttribute, SDFConstantList> (); // Map für die Zuordnungen, da mehrere Attribute geben kann, die kompensiert werden müssen
			for(int j=0; j<attrToCompensate.getAttributeCount(); j++)
			{
				SDFAttribute nextAttribute = attrToCompensate.getAttribute(j); // Attribut, welche Belegungen erfordert
				SDFConstantList constSet = rm.getIntervalList(nextAttribute);  // UNIQUE-Property of this CompensateAction
				if(constSet!= null && constSet.size()>0) // Belegungen verfügbar?
				{
					compList.put(nextAttribute, constSet);
				}

			}
			// siehe boolean NecInAttrCompensation.restrictiveCompensation
			if((this.restrictiveCompensation && compList.size()!=attrToCompensate.getAttributeCount())
					|| (!this.restrictiveCompensation && compList.size()==0) )
			{
				this.logger.debug("> Restrictive ("+this.restrictiveCompensation+") > compListSize("+compList.size()+")/attrToCompensate("+attrToCompensate.getAttributeCount()+")> ONE or MORE Attribute(s) w/o Values => ABORT");			
				return compensatedPlans; // Die Liste ist zu diesem Zeitpunkt leer			
			}
				
		
			//------------------------  PHASE 2 -------------------------------//
			// Alle Belegungen sind kompensierbar und wir können Operatoren integrieren
			// Ab HIER fahren wir ZWEIGLEISIG:
			// Wenn useCollector=true ist dann wird ab einer bestimmten Anzahl der zu kompensierenden Attribute
			// ConstCompCollectorPO integriert. Jedes Attribut wird dann von je einem AP mit Hilfe des PhysicalAccessPO kompensiert 
			// anschliessend werden Teil-Ergebnisse als Input an CollectorPO übergeben
	
			try  // kompensiere
			{
				AnnotatedPlan newPlan = new AnnotatedPlan();
				this.logger.debug("> useCol: "+this.useCollector+" / Const2Comp: "+compList.get(attrToCompensate.getAttribute(0)).size()+" / bindAt: "+this.bindCollectorAt+" /compListSize: "+compList.size());			
				if(this.useCollector && compList.get(attrToCompensate.getAttribute(0)).size()>=this.bindCollectorAt && compList.size()==1)
					newPlan = this.getCollectorPOPlan(nextPlan, compList);  // nur wenn ein Attribut zu kompensieren ist, sonst zuviele Pläne => böse Sache
				else 
					newPlan = this.getReplacePOPlan(nextPlan, compList); // sonst alle mit einem(!) PhysicalAccessPO
				this.logger.debug("> PHASE 2: Compensated Plan (Collected: "+(this.useCollector && compList.get(attrToCompensate.getAttribute(0)).size()>=this.bindCollectorAt && compList.size()==1)+") ["+newPlan.getIndex()+"] / GIP:"+newPlan.getGlobalInputPattern()+"/ GOP:"+newPlan.getGlobalOutputPattern());
				if(newPlan!=null)
				{
					// Hier WIEDER eine Weiche: siehe boolean NecInAttrCompensation.restrictiveCompensation
					if(compList.size()==attrToCompensate.getAttributeCount()) // Alle Attribute kompensiert
					{
						compensatedPlans.add(newPlan);
						this.logger.debug("> PHASE 2(FULL): Remove Plan from ErrorList (#"+getErrorPlans().size()+"):  ["+nextPlan.getIndex()+"]/ GIP:"+nextPlan.getGlobalInputPattern()+"/ GOP:"+nextPlan.getGlobalOutputPattern());
						this.getErrorPlans().remove(nextPlan);
						this.logger.debug("> PHASE 2(FULL): Plan removed from ErrorList (#"+getErrorPlans().size()+")");						
					}
					else // sonst nur die Möglichkeit, dass Teilmenge kompensiert wurde (andere Situationen wurden in Phase 1 ausgeschlossen
					{
						this.logger.debug("> PHASE 2(PARTIAL): Remove Plan from ErrorList (#"+getErrorPlans().size()+"):  ["+nextPlan.getIndex()+"]/ GIP:"+nextPlan.getGlobalInputPattern()+"/ GOP:"+nextPlan.getGlobalOutputPattern());
						this.getErrorPlans().remove(nextPlan); // Alten ErrorPlan entfernen
						this.logger.debug("> PHASE 2(PARTIAL): Plan removed from ErrorList (#"+getErrorPlans().size()+"), Add new ErrorPlan  ["+newPlan.getIndex()+"]/ GIP:"+newPlan.getGlobalInputPattern()+"/ GOP:"+newPlan.getGlobalOutputPattern());	
						this.getErrorPlans().add(newPlan);
					}
				}
			}
			catch(Exception e)
			{
				e.getMessage();
				e.printStackTrace();
			}    		    
	
		}
	
		return compensatedPlans;
	}
    
   

    
    public static void main(String[] args) {

    }
}
