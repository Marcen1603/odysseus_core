package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import mg.dynaquest.queryexecution.po.algebra.SchemaTransformationAccessPO;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.CollectorPO;
import mg.dynaquest.sourcedescription.sdf.description.SDFInputPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFOutputPattern;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstant;
import mg.dynaquest.sourcedescription.sdf.schema.SDFConstantList;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import mg.dynaquest.sourceselection.RepositoryManager;
import mg.dynaquest.sourceselection.exception.ReplaceByConCompensationImpossibleException;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * @author  Jurij Henne  Diese CompensateAction repräsentiert die Menge der Action-Klassen, die fehlende Eingabeattributbelegungen   kompensieren. Die fehlenden Belegungen werden mit Hilfe des RepositoryManagers aus dem ConstantRepository  abgerufen.  Weitere Informationen:
 * @see RepositoryManages
 * @see  ConstanRepository
 */
public abstract class NecInAttrCompensation extends AbstractCompensateAction 
{
	/**
	 * Kompensationseigener Debuger
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected Logger logger = Logger.getLogger(this.getClass()); 

	/**
	 * Repository-Manager zum Zugriff auf das Constant-Repository (Belegungen der Attribute)
	 * @uml.property  name="rm"
	 * @uml.associationEnd  
	 */
	protected RepositoryManager rm = null;
	/**
	 * Durch die Prädikate der Anfrage belegte Eingabeattribute
	 * @uml.property  name="predicateAttributes"
	 * @uml.associationEnd  
	 */
	//protected SDFAttributeList predicateAttributes = null;  	
	/** Pläne die im Verlauf des Kompensationsprozess entstanden sind */
	//protected ArrayList <AnnotatedPlan> compensatedPlans = null;
	/** Pläne zu kompensieren */
	//protected ArrayList <AnnotatedPlan> context = null;			
	/**
	 * Legt fest, ob der CollectorPO überhaupt integriert werden kann
	 * @uml.property  name="useCollector"
	 */
	protected boolean useCollector = true;
	/**
	 * Anzahl der Pläne, die notwendig ist, damit der CompensatePO einen CollectorPO einbindet
	 * @uml.property  name="bindCollectorAt"
	 */
	protected int bindCollectorAt = 10;
	/**
	 * Ist restrictiveCompensation = true, dann können nur Pläne generiert, die alle fehlenden Belegungen kompensieren können.   restrictiveCompensation = false bedeutet dagegen: 1. Attribute, für welche Belegungen in der ConstantRepository vorliegen, werden kompensiert 2. Kompensiert der Plan NICHT ALLE Attribute, wird er anstelle des ursprünglichen Plans als  ErrorPlan zum nächsten  CompensatePO herausgeschrieben. 3. Sonst natürlich als kompensierter Plan => keine Errorpläne
	 * @uml.property  name="restrictiveCompensation"
	 */
	protected boolean restrictiveCompensation = false;
	
	/*
	 *  (non-Javadoc)
	 * @see mg.dynaquest.sourceselection.action.CompensateAction#compensate(java.util.ArrayList)
	 */
	abstract public ArrayList <AnnotatedPlan> compensate(ArrayList <AnnotatedPlan> toCompensate);

    
	/**
	 * Generiert einen Plan, welcher den PhysicalAccessPO dazu nutzt, die fehlenden Belegungen zu iterieren und den Plan
	 * mit diesen auf der Quelle auszuführen
	 * Somit wird ReplaceByConPO "ausgemustert".
	 * @param inputAnnoPlan Plan, welcher die Kompensation erfordert
	 * @param constReplaceSet  Die Zuordnungen der Attribute zu den Konstanten-Belegungen
	 * @return
	 */
    protected AnnotatedPlan getReplacePOPlan(AnnotatedPlan inputAnnoPlan , HashMap constReplaceSet) 
    throws ReplaceByConCompensationImpossibleException
    {
		AnnotatedPlan newPlan = new AnnotatedPlan();		
		SDFAttributeList compensatedAttributes = new SDFAttributeList();
		ArrayList <SDFConstantList> constantListArray = new ArrayList <SDFConstantList> ();
		Iterator iter = constReplaceSet.entrySet().iterator();
		while ( iter.hasNext() ) 
		{
			Map.Entry entry = (Map.Entry) iter.next();
			compensatedAttributes.addAttribute((SDFAttribute)entry.getKey());
			SDFConstantList _cSet = (SDFConstantList)entry.getValue();
		 	constantListArray.add(_cSet);
		 	
	 		StringBuffer sb = new StringBuffer();
	 		for(int l = 0; l< _cSet.size(); l++)	 // NUR DEBUG
				sb.append(" "+((SDFConstant)_cSet.getConstant(l)).getString());
			logger.debug("> Content of constReplaceSet (Access):"+entry.getKey()+" {"+sb+" }");
		}

		try
		{
		    // Zunächst das neue Zugriffsmuster bestimmen:
			SDFInputPattern inputPattern  = new SDFInputPattern(inputAnnoPlan.getGlobalInputPattern());

			inputAnnoPlan.removeAttributeFromGlobalInputPattern(compensatedAttributes);
		 
		    SDFOutputPattern outputPattern = new SDFOutputPattern(inputAnnoPlan.getGlobalOutputPattern()); // outputPattern bleibt gleich
		
		    newPlan.setGlobalInputPattern(inputPattern);
		    newPlan.setGlobalOutputPattern(outputPattern);
		    
		    //PlanOperator inputPlan = inputAnnoPlan.getAccessPlan(); // kein InputPO beim PhysicalAccessPO
		    
		    SchemaTransformationAccessPO replaceByCon = new SchemaTransformationAccessPO();
		    // Zuordnungen im PhysicalAccessPO festlegen
			for(int i = 0; i< compensatedAttributes.getAttributeCount(); i++) 
				 replaceByCon.replaceGlobalAttributeByConstantList(compensatedAttributes.getAttribute(i), constantListArray.get(i) );
		 
		    replaceByCon.setOutElements(outputPattern.getAllAttributes());
		    
		    newPlan.setAccessPlan(replaceByCon);
		    newPlan.addBasePlan(inputAnnoPlan);        
		}
	    catch(Exception e)
	    {
	        throw new ReplaceByConCompensationImpossibleException("Erstellung des neuen Plans mit intergriertem PhysicalAccessPO ist fehlgeschlagen!");
	    }
	    return newPlan;
    }

	
	/**
	 * Generiert einen Plan, welcher einen CollectorPO dazu nutzt, die fehlenden Belegungen in Form 
	 * einzelner PhysicalAccessPO-Pläne zu iterieren und die Ergebnisse anschliessend zusammen zu fassen.
	 * Da u.U viele Attribute zu kompensieren sind und jedes Attribut dazu noch mehrere Belegungen mitbringt,
	 * entstehen u.U. sehr viele ACCESS-Pläne, die alle abschliessend von einem CollectorPO berücksichtigt werden müssen.
	 * Diese Version darf maximal nur ein Attribut kompensieren!!
	 * @param inputAnnoPlan Plan, welcher die Kompensation erfordert
	 * @param constReplaceSet  Die Zuordnungen der Attribute zu den Konstanten-Belegungen
	 * @return
	 */
    protected AnnotatedPlan getCollectorPOPlan(AnnotatedPlan inputAnnoPlan , HashMap constReplaceSet) 
    throws ReplaceByConCompensationImpossibleException
    {
    	if(constReplaceSet.size()>1) // Diese Version darf maximal nur ein Attribut kompensieren, sonst ABBRUCH!
    		return new AnnotatedPlan();
    	
    	
		AnnotatedPlan newPlan = new AnnotatedPlan();
		
		// Überführt die HashMap in eine handlichere Version von zwei Listen (bewusst auf Länge von >1 ausgelegt für 
		// die zukünftigen Erweiterungen)
		SDFAttributeList compensatedAttributes = new SDFAttributeList();
		ArrayList <SDFConstantList> constantListArray = new ArrayList <SDFConstantList> ();
		Iterator iter = constReplaceSet.entrySet().iterator();  
		while ( iter.hasNext() )   // wird DERZEIT nur einmalig durchlaufen
		{
			Map.Entry entry = (Map.Entry) iter.next();
			compensatedAttributes.addAttribute((SDFAttribute)entry.getKey());
			SDFConstantList _cSet = (SDFConstantList)entry.getValue();
		 	constantListArray.add(_cSet);
		 	
	 		StringBuffer sb = new StringBuffer();
	 		for(int l = 0; l< _cSet.size(); l++){	 // NUR DEBUG
				sb.append(" "+((SDFConstant)_cSet.getConstant(l)).getString());
            }
			logger.debug("> Content of constReplaceSet (Collect):"+entry.getKey()+" {"+sb+" }");
		}

		try
		{
		    // Zunächst das neue Zugriffsmuster bestimmen:
		    SDFInputPattern inputPattern = new SDFInputPattern(inputAnnoPlan.getGlobalInputPattern()); 

			// Bereinigen des Eingabemsuters um kompensierte Attribute
			for(int i = 0; i< inputPattern.getAttributeAttributeBindingPairCount(); i++) // alle Attribute des InputMusters durchlaufen
			{
				SDFAttribute nextAttribute = inputPattern.getAttributeAttributeBindingPair(i).getAttribute();
				// ist Attribute in der Liste zu kompensierenden Attribute?
				if(compensatedAttributes.contains(nextAttribute)) 
				{
					inputPattern.removeAttributeAttributeBindingtPair(i); // dann aus dem InputPattern entfernen
			    }
			}
			//logger.debug("> NewPLan GIP:"+inputPattern.toString(),3);
		 
		    SDFOutputPattern outputPattern = new SDFOutputPattern(inputAnnoPlan.getGlobalOutputPattern()); // outputPattern bleibt gleich
		
		    newPlan.setGlobalInputPattern(inputPattern);
		    newPlan.setGlobalOutputPattern(outputPattern);
		    
		    // Für jede Belegung ein AnnotatedPlan erzeugen, in welchem ein PhysicalAccessPO lediglich 
		    // eine Belegung des Attributes testet.
		    ArrayList <AnnotatedPlan> accessPOList = new ArrayList <AnnotatedPlan>();
		    for(int i = 0; i<compensatedAttributes.getAttributeCount(); i++) // Grösse ist immer eins
		    {
		    	SDFConstantList nextConstSet = (SDFConstantList)constantListArray.get(i);	// Alle Belegungen für das Attribut
		    	for(int j = 0; j<nextConstSet.size(); j++)
		    	{
		    		AnnotatedPlan nextPlan = new AnnotatedPlan();
		    		
		    		nextPlan.setGlobalInputPattern(inputPattern); // sind die gleichen wie vom Collector-Plan
		    		nextPlan.setGlobalOutputPattern(outputPattern);  // sind die gleichen wie vom Collector-Plan
				    
		    		SchemaTransformationAccessPO replaceByCon = new SchemaTransformationAccessPO();
		       		
		    		SDFAttributeList newAttribSet = new SDFAttributeList();
		    		newAttribSet.addAttribute(compensatedAttributes.getAttribute(i)); // Aktuelles Attribut (DERZEIT immer das gleiche)t
		    		SDFConstantList newConstSet = new SDFConstantList(); 	
		    		newConstSet.add(nextConstSet.getConstant(j)); // Neues Set mit nur EINER Belegung
					logger.debug("> COLLECTED / Plan("+j+"): Attribute: "+compensatedAttributes.getAttribute(0)+" / Constant { "+((SDFConstant)newConstSet.getConstant(0)).getString()+"}");
		    		replaceByCon.replaceGlobalAttributeByConstantList(compensatedAttributes.getAttribute(i), newConstSet);
   		
				    replaceByCon.setOutElements(outputPattern.getAllAttributes());

					nextPlan.setAccessPlan(replaceByCon);
					nextPlan.addBasePlan(inputAnnoPlan); 
		    		
		    		accessPOList.add(nextPlan);
		    	}
		    }
    
		    // Pläne sind da, nun alle an einen CollectorPO übergeben
		    CollectorPO collectorPO = new CollectorPO();
		    for(int i = 0; i<accessPOList.size(); i++)
		    {
		    	AnnotatedPlan nextPlan = accessPOList.get(i);
		    	AlgebraPO inputPlan = nextPlan.getAccessPlan();
		    	collectorPO.setNoOfInputs(accessPOList.size());
		    	collectorPO.setInputPO(i,inputPlan);
		    }
		    // CollectorPO in den kompensierten Plan integrieren
	    	collectorPO.setOutElements(outputPattern.getAllAttributes());
		    newPlan.setAccessPlan(collectorPO);
		    newPlan.addBasePlan(inputAnnoPlan);        
		    
		}
	    catch(Exception e)
	    {
	        throw new ReplaceByConCompensationImpossibleException("Erstellung des CollectorPO-Plans fehlgeschlagen!");
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
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("useCollector")) 
				this.useCollector = (Integer.parseInt(cNode.getFirstChild().getNodeValue()) != 0);	
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("bindCollectorAt")) 
				this.bindCollectorAt = Integer.parseInt(cNode.getFirstChild().getNodeValue());
			if (cNode.getNodeType() == Node.ELEMENT_NODE && cNode.getLocalName().equals("restrictiveCompensation")) 
				this.restrictiveCompensation = 	(Integer.parseInt(cNode.getFirstChild().getNodeValue()) != 0);
		
		}
	}    
    /*
     *  (non-Javadoc)
     * @see mg.dynaquest.sourceselection.action.CompensateAction#getInternalXMLRepresentation(java.lang.String, java.lang.String, java.lang.StringBuffer)
     */
    public  void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue) 
    {
    	xmlRetValue.append(baseIndent + "<useCollector>");
    	xmlRetValue.append((this.useCollector)?1:0);
    	xmlRetValue.append("</useCollector>\n");
    	
    	xmlRetValue.append(baseIndent + "<bindCollectorAt>");
    	xmlRetValue.append(this.bindCollectorAt);
    	xmlRetValue.append("</bindCollectorAt>\n");
    	
    	xmlRetValue.append(baseIndent + "<restrictiveCompensation>");
    	xmlRetValue.append((this.restrictiveCompensation)?1:0);
    	xmlRetValue.append("</restrictiveCompensation>\n");
    	

    	
    }
    
}
