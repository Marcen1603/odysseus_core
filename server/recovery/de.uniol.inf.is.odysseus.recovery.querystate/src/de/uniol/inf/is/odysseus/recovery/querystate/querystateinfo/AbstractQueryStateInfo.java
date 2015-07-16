package de.uniol.inf.is.odysseus.recovery.querystate.querystateinfo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.server.usermanagement.UserManagementProvider;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

/**
 * All information needed to recover a query state event (creation, change,
 * removal). <br />
 * To write those information to the system log and to read them,
 * {@link #toBase64Binary()} and {@link #fromBase64Binary(String)} can be used.
 * 
 * @author Michael Brand
 *
 */
public abstract class AbstractQueryStateInfo implements Serializable {

	// TODO Chance to log the human readable information

	/**
	 * The version of this class for serialization.
	 */
	private static final long serialVersionUID = 4007923324539604364L;

	/**
	 * The logger for this class.
	 */
	private static final Logger cLog = LoggerFactory
			.getLogger(AbstractQueryStateInfo.class);

	/**
	 * The name of the tenant.
	 */
	private String mTenant;

	/**
	 * The name of the user.
	 */
	private String mUser;

	/**
	 * Creates a session object from stored user and tenant information.
	 * 
	 * @return A valid session object with the stored user and tenant
	 *         information.
	 */
	public ISession getSession() {
		ISession superUserSession = UserManagementProvider
				.getUsermanagement(true).getSessionManagement()
				.loginSuperUser(null);
		return UserManagementProvider
				.getUsermanagement(false)
				.getSessionManagement()
				.loginAs(this.mUser,
						UserManagementProvider.getTenant(this.mTenant),
						superUserSession);
	}

	/**
	 * Sets the used session.
	 * 
	 * @param caller
	 *            The used session.
	 */
	public void setSession(ISession caller) {
		this.mTenant = caller.getTenant().getName();
		this.mUser = caller.getUser().getName();
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
	public static AbstractQueryStateInfo fromBase64Binary(String str) {
		byte[] data = DatatypeConverter.parseBase64Binary(str);
		try (ObjectInputStream ois = new ObjectInputStream(
				new ByteArrayInputStream(data))) {
			return (AbstractQueryStateInfo) ois.readObject();
		} catch (IOException e) {
			cLog.error("Info is not serializable!", e);
			return null;
		} catch (ClassNotFoundException e) {
			cLog.error("Unknown class for decoding!", e);
			return null;
		}
	}

}