package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;

import mg.dynaquest.queryexecution.po.algebra.SortPO;
import mg.dynaquest.sourcedescription.sdf.description.SDFInputPattern;
import mg.dynaquest.sourcedescription.sdf.description.SDFOutputPattern;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourceselection.AnnotatedPlan;

import org.apache.log4j.Logger;
import org.w3c.dom.NodeList;

public class SortCompensation extends AbstractCompensateAction {

	protected Logger logger = Logger.getLogger(this.getClass()); 

	public SortCompensation(SDFQuery query){
		setQuery(query);
	}
	
	public ArrayList<AnnotatedPlan> compensate(
			ArrayList<AnnotatedPlan> toCompensate) {
		ArrayList <AnnotatedPlan> compensatedPlans = new ArrayList <AnnotatedPlan>();
    	setErrorPlans(new ArrayList <AnnotatedPlan>(toCompensate));
    	for(AnnotatedPlan ap: toCompensate){
    		this.logger.debug("> Plan to compensate ["+ap.getIndex()+"]> GIP: "+ap.getGlobalInputPattern()+" / GOP: "+ap.getGlobalOutputPattern());
		    AnnotatedPlan newPlan = getSortCompensatedPlan(ap);
		    this.logger.debug("> Compensated Plan ["+newPlan.getIndex()+"]> GIP: "+newPlan.getGlobalInputPattern()+" / GOP: "+newPlan.getGlobalOutputPattern());
		    if(newPlan!=null){
				compensatedPlans.add(newPlan); // Plan hinzufügen
				this.getErrorPlans().remove(ap); // den Plan aus der Errorlist rausnehmen
			}

    	}				
		return compensatedPlans;
	}
	
	private AnnotatedPlan getSortCompensatedPlan(AnnotatedPlan inputPlan){
		AnnotatedPlan newPlan = new AnnotatedPlan();
		SDFInputPattern inputPattern  =  new SDFInputPattern(inputPlan.getGlobalInputPattern());
		SDFOutputPattern outputPattern	= new SDFOutputPattern(inputPlan.getGlobalOutputPattern());
		
        newPlan.setGlobalInputPattern(inputPattern);
        newPlan.setGlobalOutputPattern(outputPattern);
                
        SortPO sortPO = new SortPO(getQuery().getOrderBy());        
        sortPO.setInputPO(inputPlan.getAccessPlan());
        sortPO.setOutElements(outputPattern.getAllAttributes());
        
        newPlan.setAccessPlan(sortPO);
        newPlan.addBasePlan(inputPlan);  
		
		return newPlan;
	}

	public void getInternalXMLRepresentation(String baseIndent, String indent,
			StringBuffer xmlRetValue) {
		// TODO Auto-generated method stub

	}

	public void initInternalBaseValues(NodeList children) {
		// TODO Auto-generated method stub

	}

}
