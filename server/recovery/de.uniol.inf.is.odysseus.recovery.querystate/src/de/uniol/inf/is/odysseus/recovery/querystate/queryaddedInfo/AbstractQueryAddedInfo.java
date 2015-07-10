package de.uniol.inf.is.odysseus.recovery.querystate.queryaddedInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
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
public abstract class AbstractQueryAddedInfo implements Serializable {

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 4007923324539604364L;

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(AbstractQueryAddedInfo.class);

	/**
	 * The parser id.
	 */
	public String parserId;

	/**
	 * The full query text (e.g., Odysseus Script).
	 */
	public String queryText;

	/**
	 * The current session (and corresponding user).
	 */
	public ISession session;

	/**
	 * The context.
	 */
	private final Map<String, Serializable> context = Maps.newHashMap();

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
			this.context.put(key, c.get(key));
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
		for (String key : this.context.keySet()) {
			c.put(key, this.context.get(key));
		}
		return c;
	}

	/**
	 * The recovery executor, if set.
	 */
	public Optional<IRecoveryExecutor> recExecutor;

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
	public static AbstractQueryAddedInfo fromBase64Binary(String str) {
		byte[] data = DatatypeConverter.parseBase64Binary(str);
		try (ObjectInputStream ois = new ObjectInputStream(
				new ByteArrayInputStream(data))) {
			return (AbstractQueryAddedInfo) ois.readObject();
		} catch (IOException e) {
			cLog.error("Info is not serializable!", e);
			return null;
		} catch (ClassNotFoundException e) {
			cLog.error("Unknown class for decoding!", e);
			return null;
		}
	}

}