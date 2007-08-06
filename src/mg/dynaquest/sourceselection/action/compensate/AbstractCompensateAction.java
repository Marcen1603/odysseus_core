package mg.dynaquest.sourceselection.action.compensate;

import java.util.ArrayList;

import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;
import mg.dynaquest.sourceselection.AnnotatedPlan;

abstract public class AbstractCompensateAction implements CompensateAction{

	/**
	 * Die eigentliche Nutzeranfrage
	 * @uml.property  name="query"
	 * @uml.associationEnd  
	 */
	private SDFQuery query = null;
	
	/** Pläne die nicht verarbeitet wurden (evtl. für CompensatePO von bedeutung) */
	private ArrayList <AnnotatedPlan> errorPlans = null;

	public SDFQuery getQuery() {
		return query;
	}

	public void setQuery(SDFQuery query) {
		this.query = query;
	}

	protected void setErrorPlans(ArrayList <AnnotatedPlan> errorPlans) {
		this.errorPlans = errorPlans;
	}

	public ArrayList <AnnotatedPlan> getErrorPlans() {
		return errorPlans;
	}


	
	
	
}
