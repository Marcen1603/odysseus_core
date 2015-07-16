package de.uniol.inf.is.odysseus.recovery.querystate.querystateinfo;

import de.uniol.inf.is.odysseus.core.planmanagement.query.QueryState;

/**
 * All information needed to recover a query state change event. <br />
 * To write those information to the system log and to read them,
 * {@link #toBase64Binary()} and
 * {@link AbstractQueryStateInfo#fromBase64Binary(String)} can be used.
 * 
 * @author Michael Brand
 *
 */
public class QueryStateChangedInfo extends AbstractQueryStateInfo {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 1588955084029013203L;

	/**
	 * The query id.
	 */
	private int mId;

	/**
	 * Gets the query id.
	 * 
	 * @return An id identifying the query.
	 */
	public int getQueryId() {
		return this.mId;
	}

	/**
	 * Sets the query id.
	 * 
	 * @param id
	 *            An id identifying the query.
	 */
	public void setQueryId(int id) {
		this.mId = id;
	}

	/**
	 * The new query state.
	 */
	private QueryState mState;

	/**
	 * Gets the new query state.
	 * 
	 * @return The new query state.
	 */
	public QueryState getQueryState() {
		return this.mState;
	}

	/**
	 * Sets the new query state.
	 * 
	 * @param state
	 *            The new query state.
	 */
	public void setQueryState(QueryState state) {
		this.mState = state;
	}

	/**
	 * The shedding factor.
	 */
	private int mSheddingFactor;

	/**
	 * Gets the shedding factor.
	 * 
	 * @return An int representing the shedding factor.
	 */
	public int getSheddingFactor() {
		return this.mSheddingFactor;
	}

	/**
	 * Sets the query id.
	 * 
	 * @param factor
	 *            An int representing the shedding factor.
	 */
	public void setSheddingFactor(int factor) {
		this.mSheddingFactor = factor;
	}

}