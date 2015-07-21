package de.uniol.inf.is.odysseus.recovery.querystate.querystateinfo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.recovery.IRecoveryExecutor;

/**
 * All information needed to recover a query added event (normal queries as well
 * as source definitions). <br />
 * To write those information to the system log and to read them,
 * {@link #toBase64Binary()} and {@link AbstractQueryStateInfo#fromBase64Binary(String)} can be used.
 * 
 * @author Michael Brand
 *
 */
public class ScriptAddedInfo extends AbstractQueryStateInfo {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 4007923324539604364L;

	/**
	 * The parser id.
	 */
	private String mParserId;

	/**
	 * Gets the parser id.
	 * 
	 * @return A string identifying the parser.
	 */
	public String getParserId() {
		return this.mParserId;
	}

	/**
	 * Sets the parser id.
	 * 
	 * @param parserId
	 *            A string identifying the parser.
	 */
	public void setParserId(String parserId) {
		this.mParserId = parserId;
	}

	/**
	 * The full query text (e.g., Odysseus Script).
	 */
	private String mQueryText;

	/**
	 * Sets the query text.
	 * 
	 * @return The full query text (e.g., Odysseus Script).
	 */
	public String getQueryText() {
		return this.mQueryText;
	}

	/**
	 * Gets the query text.
	 * 
	 * @param queryText
	 *            The full query text (e.g., Odysseus Script).
	 */
	public void setQueryText(String queryText) {
		this.mQueryText = queryText;
	}

	/**
	 * The context.
	 */
	private Map<String, Serializable> mContext = Maps.newHashMap();

	/**
	 * Sets the context. <br />
	 * Field is not set directly because it is a {@link Map}. This is because
	 * {@link Context} is not serializable.
	 * 
	 * @param c
	 *            The context
	 */
	public void setContext(Context c) {
		for (String key : c.getKeys()) {
			this.mContext.put(key, c.get(key));
		}
	}

	/**
	 * Gets the context. <br />
	 * Field is not set directly because it is a {@link Map}. This is because
	 * {@link Context} is not serializable.
	 * 
	 * @return The context
	 */
	public Context getContext() {
		Context c = Context.empty();
		for (String key : this.mContext.keySet()) {
			c.put(key, this.mContext.get(key));
		}
		return c;
	}

	/**
	 * The recovery executor, if set.
	 */
	private Optional<String> mExecutorName = Optional.absent();

	/**
	 * Gets the recovery executor.
	 * 
	 * @return The name of the used {@link IRecoveryExecutor}, if a recovery
	 *         strategy was selected.
	 */
	public Optional<String> getRecoveryExecutor() {
		return this.mExecutorName;
	}

	/**
	 * Sets the recovery executor.
	 * 
	 * @param executor
	 *            The name of the used {@link IRecoveryExecutor}, if a recovery
	 *            strategy was selected.
	 */
	public void setRecoveryExecutor(String executor) {
		this.mExecutorName = Optional.fromNullable(executor);
	}

	/**
	 * The query ids.
	 */
	private List<Integer> mIds = Lists.newArrayList();

	/**
	 * Gets the query ids.
	 * 
	 * @return A list of all query ids for this entry or an empty list for
	 *         source definitions.
	 */
	public List<Integer> getQueryIds() {
		return this.mIds;
	}

	/**
	 * Gets the query ids.
	 * 
	 * @param ids
	 *            A list of all query ids (not for source definitions).
	 */
	public void setQueryIds(List<Integer> ids) {
		this.mIds = ids;
	}

}