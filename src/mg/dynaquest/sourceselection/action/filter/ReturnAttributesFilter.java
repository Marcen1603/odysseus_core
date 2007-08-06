package mg.dynaquest.sourceselection.action.filter;

import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourceselection.AnnotatedPlan;

/**
 * @author  Jurij Henne
 */
public class ReturnAttributesFilter extends AbstractFilterAction {

	
    public ReturnAttributesFilter(){}
	
    public ReturnAttributesFilter(SDFQuery query){
    	 super(query);
    }
	
    /* (non-Javadoc)
     * @see mg.dynaquest.queryexecution.action.FilterAction#positive(java.lang.Object)
     */
    public boolean positive(Object obj) {
        AnnotatedPlan plan = (AnnotatedPlan) obj;
		SDFAttributeList outputPattern = plan.getGlobalOutputPattern().getAllAttributes();
		SDFAttributeList dispensableAttributes = SDFAttributeList.difference(outputPattern, getQuery().getAttributes());
		if(dispensableAttributes.getAttributeCount()>0) // Sind überflüssige Attribute vorhanden?
			return false;
		else 
			return true;
    }
    
}
