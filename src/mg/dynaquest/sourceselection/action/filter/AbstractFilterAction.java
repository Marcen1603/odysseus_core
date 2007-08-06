package mg.dynaquest.sourceselection.action.filter;

import mg.dynaquest.queryexecution.action.FilterAction;
import mg.dynaquest.sourcedescription.sdf.query.SDFQuery;

public abstract class AbstractFilterAction implements FilterAction {

	/**
	 * @uml.property  name="actionName"
	 */
	private String actionName = this.getClass().getName();
	/**
	 * @uml.property  name="query"
	 * @uml.associationEnd  
	 */
	private SDFQuery query = null;

	/**
	 * @return  the actionName
	 * @uml.property  name="actionName"
	 */
	public String getActionName() {
		return this.actionName;
	}
	
	/**
	 * @param query  the query to set
	 * @uml.property  name="query"
	 */
	public void setQuery(SDFQuery query) {
		this.query = query;
	}
	
	AbstractFilterAction(){};
	
	AbstractFilterAction(SDFQuery query){
		setQuery(query);
	}

	public SDFQuery getQuery() {
		return query;
	}

}
