package de.uniol.inf.is.odysseus.recovery.querystate.queryaddedInfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Context;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

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
	 * The query build configuration.
	 */
	public String buildConfiguration;

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
	public Context context;

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