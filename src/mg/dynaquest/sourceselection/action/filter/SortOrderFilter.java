package mg.dynaquest.sourceselection.action.filter;

import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourcedescription.sdf.schema.SDFAttributeList;
import mg.dynaquest.sourceselection.AnnotatedPlan;

public class SortOrderFilter extends AbstractFilterAction {
	
	public SortOrderFilter(SDFQuery query) {
		super(query);
	}
	

	public boolean positive(Object obj) {
		
		SDFAttributeList orderAttribute = getQuery().getOrderBy();
		if (orderAttribute != null && orderAttribute.size() > 0){
			AnnotatedPlan plan = (AnnotatedPlan) obj;
			SDFAttributeList planAttribs = plan.getAccessPlan().getSorted();
			// Nur wenn die Ordnung übereinstimmt muss nicht kompensiert werden
			return orderAttribute.equals(planAttribs);
			
		}
		return true;
		
	}

 

}
