package de.uniol.inf.is.odysseus.recovery.querystate;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.recovery.IRecoveryExecutor;

/**
 * All information needed to recover a query added event (normal queries as well
 * as source definitions). <br />
 * To write those information to the system log and to read them,
 * {@link #toBase64Binary()} and {@link #fromBase64Binary(String)} can be used.
 * 
 * @author Michael Brand
 *
 */
public class QueryAddedInfo implements Serializable {

	// TODO Chance to log the human readable information

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 4007923324539604364L;

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(QueryAddedInfo.class);

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

	// TODO Problem with ISession while fromBase64Binary:
	// ClassNotFoundException.
	// Is only used for addQuery. All other events are done with SuperUser.
	// /**
	// * The current session (and corresponding user).
	// */
	// private ISession mSsession;

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

	/**
	 * Encode to a Base64Binary.
	 * 
	 * @return A string representing the binary.
	 */
	public String toBase64Binary() {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
			oos.writeObject(this);
			return new String(DatatypeConverter.printBase64Binary(baos
					.toByteArray()));
		} catch (IOException e) {
			cLog.error("Info is not serializable!", e);
			return null;
		}
	}

	/**
	 * Decode from a Base64Binary.
	 * 
	 * @param str
	 *            A string representing the binary.
	 * @return The decoded information.
	 */
	public static QueryAddedInfo fromBase64Binary(String str) {
		byte[] data = DatatypeConverter.parseBase64Binary(str);
		try (ObjectInputStream ois = new ObjectInputStream(
				new ByteArrayInputStream(data))) {
			return (QueryAddedInfo) ois.readObject();
		} catch (IOException e) {
			cLog.error("Info is not serializable!", e);
			return null;
		} catch (ClassNotFoundException e) {
			cLog.error("Unknown class for decoding!", e);
			return null;
		}
	}

}