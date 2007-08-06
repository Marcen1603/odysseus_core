package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;
import mg.dynaquest.queryexecution.po.algebra.AlgebraPO;
import mg.dynaquest.queryexecution.po.algebra.ProjectPO;
import mg.dynaquest.sourcedescription.sdf.description.SDFInputPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFOutputPattern;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttribute;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourceselection.AnnotatedPlan;
import mg.dynaquest.sourceselection.exception.ProjectCompensationImpossibleException;
import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

/**
 * @author  Jurij Henne  Diese CompensateAction filtert unerwünschte Attribute aus dem Ausgabestrom
 */
public class ProjectCompensation extends AbstractCompensateAction  {
	
	/**
	 * Kompensationseigener Debuger
	 * @uml.property  name="logger"
	 * @uml.associationEnd  multiplicity="(1 1)"
	 */
	protected Logger logger = Logger.getLogger(this.getClass()); 
    
    public ProjectCompensation(SDFQuery query)
    {
    	this.setQuery(query);
    }
 
 
	/**
	 *  Die Methode integriert einen Projektion-Planoperator, der die unterwünschten Attribute aus der GOP wegfiltert
	 */
	public ArrayList<AnnotatedPlan> compensate(ArrayList <AnnotatedPlan> toCompensate)
	{
		ArrayList <AnnotatedPlan> compensatedPlans = new ArrayList <AnnotatedPlan>();
    	setErrorPlans(new ArrayList <AnnotatedPlan>(toCompensate));
    		
		SDFAttributeList queryAttributes = new SDFAttributeList(this.getQuery().getAttributes());
		
    	// Zu kompensierende Pläne durchlaufen und folgendes machen....
    	for(int i=0; i<toCompensate.size(); i++)
    	{		
    		AnnotatedPlan nextPlan = toCompensate.get(i);
//    		SDFAttributeList outputPattern = new SDFAttributeList(nextPlan.getGlobalOutputPattern().getAllAttributes());
//    		SDFAttributeList dispensableAttributes = SDFAttributeList.difference(outputPattern, queryAttributes);
//    		if(dispensableAttributes.getAttributeCount()>0) // Sind überflüssige Attribute vorhanden?
//    		{
    			// ProjectPO integrieren und queryAttributes übergeben
    			try
    			{
  		    		this.logger.debug("> Plan to compensate ["+nextPlan.getIndex()+"]> GIP: "+nextPlan.getGlobalInputPattern()+" / GOP: "+nextPlan.getGlobalOutputPattern());
  		    		AnnotatedPlan newPlan = getCompensatedPlan(nextPlan, queryAttributes);
  		    		this.logger.debug("> Compensated Plan ["+newPlan.getIndex()+"]> GIP: "+newPlan.getGlobalInputPattern()+" / GOP: "+newPlan.getGlobalOutputPattern());
    				if(newPlan!=null)
    				{
    					compensatedPlans.add(newPlan); // Plan hinzufügen
    					this.getErrorPlans().remove(nextPlan); // den Plan aus der Errorlist rausnehmen
    				}
    			}
    			catch (Exception e)
    			{
    				e.printStackTrace();
    			}	
//    		}
    		
    	}
        return compensatedPlans;
    }
    
	/**
	 * Liefert einen neuen AnnotatedPlan mit dem integrierten ProjectPO
	 * @param inputPlan Plan zu kompensieren
	 * @param queryAttributes  Ausgabeattribute, die der Nutzer zurückgeliefert haben möchte
	 * @return neuer AnnotatedPlan
	 * @throws ProjectCompensationImpossibleException
	 */
	private AnnotatedPlan getCompensatedPlan(AnnotatedPlan inputPlan, SDFAttributeList queryAttributes)
	throws ProjectCompensationImpossibleException
	{
	    AnnotatedPlan newPlan = new AnnotatedPlan();

	    try
	    {
	        // Zunächst den neuen Zugriffsmuster bestimmen
	        SDFInputPattern inputPattern  =  new SDFInputPattern(inputPlan.getGlobalInputPattern()); // bleibt gleich
      
	        SDFOutputPattern newOutputPattern = new SDFOutputPattern();
	        SDFOutputPattern oldOutputPattern	= new SDFOutputPattern(inputPlan.getGlobalOutputPattern()); // outputPattern muss modifiziert werden
	        // Abgleichen mit den Attributen der Query
	        for(int i = 0; i<oldOutputPattern.getAttributeAttributeBindingPairCount(); i++)
	        {
	        	SDFAttribute nextAttribute = oldOutputPattern.getAttributeAttributeBindingPair(i).getAttribute();
		    	//this.logger.debug("> GOP-Anpassung  / Attribut : "+nextAttribute, 3);  		 
	        	if(queryAttributes.contains(nextAttribute))
	        	{
	        		this.logger.debug("> Constructing new GOP > Add Attribute : "+nextAttribute);
	        		newOutputPattern.addAttributeAttributeBindingPair(oldOutputPattern.getAttributeAttributeBindingPair(i));
	        	}
	        }
	        
	        newPlan.setGlobalInputPattern(inputPattern);
	        newPlan.setGlobalOutputPattern(newOutputPattern);
	        
	        AlgebraPO accessPlan = inputPlan.getAccessPlan();
	        
	        ProjectPO projectPO = new ProjectPO(queryAttributes);
	        projectPO.setInputPO(accessPlan);
	        projectPO.setOutElements(newOutputPattern.getAllAttributes());
            projectPO.setPOName("Project PO "+System.currentTimeMillis());
	        
	        newPlan.setAccessPlan(projectPO);
	        newPlan.addBasePlan(inputPlan);  
	    }
		catch(Exception e)
		{
			e.printStackTrace();
	        throw new ProjectCompensationImpossibleException("Integration des ProjectPO in neuen Plan fehlgeschlagen!");
		}
	    return newPlan;
	}
	/*
     *  (non-Javadoc)
     * @see mg.dynaquest.sourceselection.action.CompensateAction#initInternalBaseValues(org.w3c.dom.NodeList)
     */
	public void initInternalBaseValues(NodeList children) 
	{
			// TODO: nichts zu initialisieren
	}
    /*
     *  (non-Javadoc)
     * @see mg.dynaquest.sourceselection.action.CompensateAction#getInternalXMLRepresentation(java.lang.String, java.lang.String, java.lang.StringBuffer)
     */
    public  void getInternalXMLRepresentation(String baseIndent, String indent, StringBuffer xmlRetValue) 
    {
    	 //TODO: nichts zu serialisieren
    }

}
