package de.uniol.inf.is.odysseus.recovery.querystate.querystateinfo;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;

/**
 * All information needed to recover a query removal event. <br />
 * To write those information to the system log and to read them,
 * {@link #toBase64Binary()} and
 * {@link AbstractQueryStateInfo#fromBase64Binary(String)} can be used.
 * 
 * @author Michael Brand
 *
 */
public class QueryRemovedInfo extends QueryStateChangedInfo {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 1588955084029013203L;

	/**
	 * Sets the query state to {@link QueryState#UNDEF}
	 */
	public QueryRemovedInfo() {
		setQueryState(QueryState.UNDEF);
	}
	
	/**
	 * No-Op.
	 */
	@Override
	public void setQueryState(QueryState state) {
		return;
	}

}