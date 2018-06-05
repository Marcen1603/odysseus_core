package de.uniol.inf.is.odysseus.incubation.crypt.logicaloperator;

import java.util.List;

/**
 * This interface provides functions to set the specifications to load
 * informations about a cryptographic receiver out of a vault. <br>
 * The vault and the loading has to be implemented somewhere else.
 * 
 * @author MarkMilster
 *
 */
public interface ReceiverVaultLoader extends VaultLoader {

	/**
	 * Returns the id of the receiver, which will be loaded.
	 * 
	 * @return The id of the receiver
	 */
	public List<Integer> getReceiverID();

	/**
	 * Sets the id of the receiver, which will be laoded.
	 * 
	 * @param receiverID
	 *            The id of the receiver
	 */
	public void setReceiverID(List<Integer> receiverID);

	/**
	 * Returns the id of the stream. <br>
	 * The id of the stream identifies the cryptography.
	 * 
	 * @return The id of the stream
	 */
	public int getStreamID();

	/**
	 * Sets the id of the stream. <br>
	 * The id of the stream identifies the cryptography.
	 * 
	 * @param streamID
	 *            The id of the stream
	 */
	public void setStreamID(int streamID);

}
